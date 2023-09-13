package bokjak.bokjakserver.web.log;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@WebFilter(urlPatterns = "/*")  // 대상: 전체 URI
public class ReadableRequestWrapperFilter implements Filter {   // TODO: 4xx 응답을 캐치 못한다?

    @Override
    public void init(FilterConfig filterConfig) {
        // Do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        ReadableRequestWrapper wrapper = new ReadableRequestWrapper((HttpServletRequest) request);
        filterChain.doFilter(wrapper, response);    // 필터 체인에 Wrapper 추가
    }

    @Override
    public void destroy() {
        // Do nothing
    }

}
