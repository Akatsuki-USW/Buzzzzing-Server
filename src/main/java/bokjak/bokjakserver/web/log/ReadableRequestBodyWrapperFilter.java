package bokjak.bokjakserver.web.log;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@WebFilter(urlPatterns = "/*")  // 대상: 전체 URI
public class ReadableRequestBodyWrapperFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        // Do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        ReadableRequestBodyWrapper wrapper = new ReadableRequestBodyWrapper((HttpServletRequest) request);
        filterChain.doFilter(wrapper, response);    // 필터 체인에 Wrapper 추가
    }

    @Override
    public void destroy() {
        // Do nothing
    }

}
