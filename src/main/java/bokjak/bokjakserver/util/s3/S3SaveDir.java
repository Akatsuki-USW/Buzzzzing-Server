package bokjak.bokjakserver.util.s3;

import bokjak.bokjakserver.common.exception.StatusCode;
import bokjak.bokjakserver.util.s3.exception.AwsS3Exception;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum S3SaveDir {
    USER_PROFILE("/user/profile"),
    SPOT("/spot"),
    ETC("/etc");

    public final String path;

    public static S3SaveDir toEnum(String stringParam) {
        return switch (stringParam) {
            case "profile" -> USER_PROFILE;
            case "spot" -> SPOT;
            case "etc" -> ETC;

            default -> throw new AwsS3Exception(StatusCode.AWS_S3_FILE_TYPE_INVALID);
        };
    }
}
