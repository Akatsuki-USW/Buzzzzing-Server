package bokjak.bokjakserver.config.jwt;

import bokjak.bokjakserver.common.exception.StatusCode;
import bokjak.bokjakserver.config.redis.RedisService;
import bokjak.bokjakserver.config.security.PrincipalDetailService;
import bokjak.bokjakserver.domain.user.dto.AuthDto.SigningUser;
import bokjak.bokjakserver.domain.user.exeption.AuthException;
import bokjak.bokjakserver.domain.user.model.Role;
import bokjak.bokjakserver.domain.user.model.User;
import bokjak.bokjakserver.domain.user.service.UserService;
import bokjak.bokjakserver.util.CustomEncryptUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@Transactional
public class JwtProvider {

    private final PrincipalDetailService principalDetailService;
    private final Key privatekey;
    private final CustomEncryptUtil customEncryptUtil;
    private final RedisService redisService;
    private final UserService userService;

    public JwtProvider(@Value("${jwt.secret-key}") String secretKey,
                       PrincipalDetailService principalDetailService,
                       CustomEncryptUtil customEncryptUtil,
                       RedisService redisService, UserService userService){
        this.privatekey= Keys.hmacShaKeyFor(secretKey.getBytes());      //인코딩된 byte array
        this.principalDetailService = principalDetailService;
        this.customEncryptUtil = customEncryptUtil;
        this.redisService = redisService;
        this.userService = userService;
    }

    @Value("${jwt.access-duration}")
    public long accessDuration;

    @Value("${jwt.refresh-duration}")
    public long refreshDuration;

    //Request 헤더에서 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        String rawToken = request.getHeader("Authorization");
        if (rawToken != null && rawToken.startsWith("Bearer "))
            return rawToken.replace("Bearer ","");
        else return null;
    }

    public String resolveSignToken(String rawToken) {
        if (rawToken != null && rawToken.startsWith("Bearer "))
            return rawToken.replace("Bearer ","");
        else throw new AuthException(StatusCode.SIGNUP_TOKEN_ERROR);
    }

    //SignToken 생성
    public String createSignToken(String socialEmail) {
        Date now = new Date(System.currentTimeMillis());
        return Jwts.builder()
                .setSubject(customEncryptUtil.encrypt(socialEmail))
                .claim("sign", Role.ROLE_USER)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessDuration))
                .signWith(privatekey)
                .compact();
    }

    //accessToken 생성
    private String createAccessToken(User user) {
        Date now = new Date(System.currentTimeMillis());
        return Jwts.builder()
                .setSubject(customEncryptUtil.encrypt(user.getSocialEmail()))
                .claim("role", user.getRole())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+accessDuration))
                .signWith(privatekey)
                .compact();
    }

    //RefreshToken 생성
    private String createRefreshToken(User user) {
        Date now = new Date(System.currentTimeMillis());
        return Jwts.builder()
                .setSubject(customEncryptUtil.encrypt(user.getSocialEmail()))
                .setIssuedAt(now)
                .claim("tokenType", "refresh")
                .signWith(privatekey)
                .compact();
    }

    //Access, Refresh Token 검증 (만료 여부 검사)
    public boolean validate(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(privatekey)  //검증키 지정
                    .build()
                    .parseClaimsJws(token); //토큰의 유효 기간을 확인하기 위해 exp claim을 가져와 현재와 비교
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 입니다.");
        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 JWT입니다.");
        } catch (IllegalArgumentException e) {
            log.warn("JWT 잘못 되었습니다.");
        }
        return false;
    }

    //Authentication 객체 가져오기
    public Authentication getAuthentication(String accessToken) {
        Claims body = Jwts.parserBuilder()
                .setSigningKey(privatekey)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
        String socialEmail = customEncryptUtil.decrypt(body.getSubject());
        UserDetails userDetails = principalDetailService.loadUserByUsername(socialEmail);
        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }

    public SigningUser getSignKey(String signToken) {
        Claims body = Jwts.parserBuilder()
                .setSigningKey(privatekey)
                .build()
                .parseClaimsJws(signToken)
                .getBody();
        String socialEmail = customEncryptUtil.decrypt(body.getSubject());
        String[] split = socialEmail.split("@");
        return new SigningUser(socialEmail, split[0],split[1]);
    }


    /**
     * Access, Refresh 최초 발행
     */
    public JwtDto issue(User user) {
        String access = createAccessToken(user);
        String refresh = createRefreshToken(user);

        redisService.setValues(user.getSocialEmail(), refresh, refreshDuration, TimeUnit.MILLISECONDS);
        return new JwtDto(access,refresh);
    }

    public JwtDto adminIssue(User user) {
        String accessToken = createAccessToken(user);
        return new JwtDto(accessToken,"");
    }

    public JwtDto reissue(String refreshToken) {

        Claims body = Jwts.parserBuilder()
                .setSigningKey(privatekey)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();
        if (body.get("tokenType") == null || !body.get("tokenType").equals("refresh"))
            throw new AuthException(StatusCode.IS_NOT_REFRESH);
        String socialEmail = customEncryptUtil.decrypt(body.getSubject());
        String valueToken = redisService.getValues(socialEmail);
        if (Objects.isNull(valueToken)) throw new AuthException(StatusCode.EXPIRED_REFRESH);
        if (!valueToken.equals(refreshToken)) throw new AuthException(StatusCode.IS_NOT_CORRECT_REFRESH);

        User user = userService.getUserBySocialEmail(socialEmail);
        String newAccessToken = createAccessToken(user);
        String newRefreshToken = createRefreshToken(user);

        redisService.setValues(socialEmail, newRefreshToken, refreshDuration, TimeUnit.MILLISECONDS);
        return new JwtDto(newAccessToken,newRefreshToken);

    }
}
