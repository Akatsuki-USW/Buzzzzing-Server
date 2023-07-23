package bokjak.bokjakserver.common.constant;

public class ConstraintConstants {
    /**
     * AWS S3
     */
    public static final int S3_FILE_TYPE_MAX_LENGTH = 25;

    /**
     * Category
     */
    public static final int LOCATION_CATEGORY_NAME_MAX_LENGTH = 45;
    public static final int SPOT_CATEGORY_NAME_MAX_LENGTH = 45;

    /**
     * Congestion
     */
    public static final int CONGESTION_LEVEL_MAX_VALUE = 3;

    /**
     * Spot & Comment
     */
    public static final int SPOT_TITLE_MAX_LENGTH = 50;
    public static final int SPOT_ADDRESS_MAX_LENGTH = 500;
    public static final int SPOT_CONTENT_MAX_LENGTH = 1500;
    public static final int SPOT_IMAGE_MAX_SIZE = 5;

    public static final int COMMENT_CONTENT_MAX_LENGTH = 300;
}
