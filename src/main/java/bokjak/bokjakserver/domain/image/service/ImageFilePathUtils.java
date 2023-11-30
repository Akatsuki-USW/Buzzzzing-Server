package bokjak.bokjakserver.domain.image.service;

import bokjak.bokjakserver.domain.image.S3SaveDir;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public class ImageFilePathUtils {

    private static final String FILE_EXTENSION_SEPARATOR = ".";
    private static final String URL_SEPARATOR = "/";
    private static final String S3_OBJECT_NAME_PATTERN = "{0}_{1}.{2}";


    static String buildRootPath(String bucket, S3SaveDir saveDir) {
        return bucket + saveDir.path;
    }

    static String buildImageFilePath(MultipartFile multipartFile, String owner) {    // 실제 파일 이름으로부터 key 생성
        String fileExtension = getFileExtension(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        return MessageFormat.format(S3_OBJECT_NAME_PATTERN, owner, UUID.randomUUID(), fileExtension);
    }

    static String buildImageFilePath(String url) {   // URL로부터 key 생성
        String[] parsedUrl = url.split(URL_SEPARATOR);
        String fileName = parsedUrl[parsedUrl.length - 1];
        return URLDecoder.decode(fileName, StandardCharsets.UTF_8);
    }


    private static String getFileExtension(String originalFileName) {// 파일 확장자 추출
        int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        return originalFileName.substring(fileExtensionIndex + 1);
    }


}
