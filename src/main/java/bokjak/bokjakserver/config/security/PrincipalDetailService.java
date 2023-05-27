package bokjak.bokjakserver.config.security;

import bokjak.bokjakserver.domain.user.model.User;
import bokjak.bokjakserver.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String socialEmail) throws UsernameNotFoundException {
        User principal = userRepository.findBySocialEmail(socialEmail)
                .orElseThrow(()-> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다. :" + socialEmail));
        return new PrincipalDetails(principal);
    }
}
