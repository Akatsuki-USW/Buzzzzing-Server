package bokjak.bokjakserver.util.client;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Objects;

public class ClientIPAddressUtils {

    public static String getClientIP(HttpServletRequest request) {
        final String[] ipHeaderNames = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR"
        };

        String ip = null;
        for (String headerName : ipHeaderNames) {
            ip = request.getHeader(headerName);
            if (Objects.nonNull(ip)) break;
        }

        if (Objects.isNull(ip) || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
