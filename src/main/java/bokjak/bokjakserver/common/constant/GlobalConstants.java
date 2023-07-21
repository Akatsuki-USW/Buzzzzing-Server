package bokjak.bokjakserver.common.constant;

public class GlobalConstants {
    /**
     * Common
     */
    public static final String[] APPOINTED_URIS = {"/auth/login/admin", "/auth/reissue", "/auth/login", "/auth/signup", "/users/check/nickname/**", "/hello/**", "/categories/**", "/files"};


    /**
     * Category
     */
    public static final int WEEK_SIZE = 7;

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
