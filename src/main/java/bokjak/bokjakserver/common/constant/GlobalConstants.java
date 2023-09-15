package bokjak.bokjakserver.common.constant;

public class GlobalConstants {
    /**
     * Common
     */
    public static final String[] AUTH_WHITELIST = {
            "/",
            "/csrf",
            "/error",

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
            "/swagger-ui/**"
    };

    /**
     * Location
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final int TOP_LOCATIONS_SIZE = 5;
    public static final String CONTENT_DATA = "statistics";
    public static final String CONTENT_HOUR = "time";
    public static final String CONTENT_CONGESTION_LEVEL = "congestionLevel";
    public static final int CONGESTION_STATISTIC_START_TIME = 9;
    public static final int CONGESTION_PREDICTION_WEEK = 1;

}
