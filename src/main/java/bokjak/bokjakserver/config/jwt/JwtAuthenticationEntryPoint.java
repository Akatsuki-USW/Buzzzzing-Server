package bokjak.bokjakserver.config.jwt;

import bokjak.bokjakserver.common.dto.ApiResponse;
import bokjak.bokjakserver.common.exception.StatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint{

    private final ObjectMapper objectMapper;


    //로그인 안 한 상태로 접근할 경우 401
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        log.warn("Unauthorized access = {}", e.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        String body = objectMapper.writeValueAsString(
                ApiResponse.error(StatusCode.FILTER_ACCESS_DENIED.getStatusCode(), StatusCode.FILTER_ACCESS_DENIED.getMessage())
        );
        response.getWriter().write(body);
    }
}
