package bokjak.bokjakserver.config.security;

import bokjak.bokjakserver.common.exception.StatusCode;
import bokjak.bokjakserver.domain.user.exeption.UserException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static String getCurrentUserSocialEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new UserException(StatusCode.NOT_FOUND_USER);
        }
        return authentication.getName();
    }
}
