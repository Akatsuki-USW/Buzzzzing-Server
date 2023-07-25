package bokjak.bokjakserver.common.constant;

public class GlobalConstants {
    /**
     * Common
     */
    public static final String[] APPOINTED_URIS = {
            "/auth/login/admin",
            "/auth/reissue",
            "/auth/login",
            "/auth/signup",
            "/users/check/nickname/**",
            "/hello/**",
            "/categories/**",
            "/files",

            "/api-docs/**",
            "/v1/api-docs",
            "/v2/api-docs",
            "/docs/**",
            "/favicon.ico",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/swagger-ui/#",
            "/webjars/**",
            "/swagger/**",
            "/swagger-ui/**",
            "/",
            "/csrf",
            "/error"
    };


    /**
     * swagger
     */
    public static final String[] SWAGGER_PATHS = {
            "/**"
    };
    public static final String SWAGGER_TITLE = "복쟉복쟉 API 명세서";
    public static final String SWAGGER_DESCRIPTION = "\uD83D\uDE80 Realtime Congestion Based Location Recommendation Service - 복쟉복쟉 Server의 API 명세서입니다.";
    public static final String SWAGGER_VERSION_1 = "v1";

    /**
     * Location
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final int TOP_LOCATIONS_SIZE = 5;
    public static final String CONTENT_DATA = "statistics";
    public static final String CONTENT_HOUR = "time";
    public static final String CONTENT_CONGESTION_LEVEL = "congestionLevel";
    public static final int CONGESTION_STATISTIC_START_TIME = 9;
    public static final int CONGESTION_PREDICTION_WEEK = 1;

}
