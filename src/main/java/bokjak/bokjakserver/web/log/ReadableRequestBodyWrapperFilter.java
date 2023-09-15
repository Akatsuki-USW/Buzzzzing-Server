package bokjak.bokjakserver.web.log;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@WebFilter(urlPatterns = "/*")  // 대상: 전체 URI
public class ReadableRequestBodyWrapperFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(new ReadableRequestBodyWrapper(request), response);    // 필터 체인에 커스텀 Wrapper 추가
    }

}
