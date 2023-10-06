package bokjak.bokjakserver.web.log;

import bokjak.bokjakserver.util.client.ClientIPAddressUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;
import java.util.Objects;

@Component
@Aspect
@Slf4j
public class LoggerAspect {
    @Pointcut("execution(* bokjak.bokjakserver..*Controller.*(..)) || execution(* bokjak.bokjakserver..*GlobalExceptionHandler.*(..))")
    // 이런 패턴이 실행될 경우 수행
    public void loggerPointCut() {
    }

    @Around("loggerPointCut()")
    public Object logRequestUri(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (Objects.nonNull(requestAttributes)) {
            HttpServletRequest request = requestAttributes.getRequest(); // request 정보를 가져온다.
            String controllerName = proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName();
            String methodName = proceedingJoinPoint.getSignature().getName();
            log.info("{} {}.{}: {} {} PARAM={}",
                    ClientIPAddressUtils.getClientIP(request),  // IP
                    controllerName,
                    methodName,
                    request.getMethod(),
                    request.getRequestURI(),
                    extractParams(request)
            ); // param에 담긴 정보들을 한번에 로깅한다.
        }

        return result;
    }

    private static JSONObject extractParams(HttpServletRequest request) {   // request로부터 param 추출, JSONObject로 변환
        JSONObject jsonObject = new JSONObject();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            String replaceParam = param.replaceAll("\\.", "-");
            jsonObject.put(replaceParam, request.getParameter(param));
        }
        return jsonObject;
    }
}
