package bokjak.bokjakserver.util.s3.service;

import bokjak.bokjakserver.common.exception.StatusCode;
import bokjak.bokjakserver.util.s3.S3SaveDir;
import bokjak.bokjakserver.util.s3.dto.AwsS3Dto.FileDto;
import bokjak.bokjakserver.util.s3.dto.AwsS3Dto.FileListDto;
import bokjak.bokjakserver.util.s3.dto.AwsS3Dto.UpdateFileRequest;
import bokjak.bokjakserver.util.s3.dto.AwsS3Dto.UploadFileRequest;
import bokjak.bokjakserver.util.s3.exception.AwsS3Exception;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static bokjak.bokjakserver.config.security.SecurityUtils.getCurrentUserSocialEmail;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsS3Service {
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    private static final String FILE_EXTENSION_SEPARATOR = ".";
    private static final String URL_SEPARATOR = "/";
    private static final String S3_OBJECT_NAME_PATTERN = "{0}_{1}.{2}";

    public FileListDto uploadFiles(final UploadFileRequest uploadFileRequest) {
        String currentUserSocialEmail = getCurrentUserSocialEmail();

        List<FileDto> uploadedFiles = uploadFileRequest.files().stream()
                .map(file -> uploadSingleFile(file, uploadFileRequest.type(), currentUserSocialEmail))
                .toList();
        return new FileListDto(uploadedFiles);
    }

    public FileDto uploadSingleFile(final MultipartFile multipartFile, final String stringParam, final String owner) {
        validateFileExist(multipartFile);

        String key = buildKey(Objects.requireNonNull(multipartFile.getOriginalFilename()), owner);

        ObjectMetadata objectMetadata = new ObjectMetadata();   // 생성할 S3 Object의 메타 데이터 설정
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());
        S3SaveDir saveDir = S3SaveDir.toEnum(stringParam);  // 경로명 변수
        String bucketName = bucket + saveDir.backPath;

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucketName, key, inputStream, objectMetadata)    // 업로드
                    .withCannedAcl(CannedAccessControlList.PublicRead));    // ACL public read로 설정
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (AmazonServiceException e) {
            log.warn("S3 파일 업로드 실패 = {}", e.getMessage());
            throw new AwsS3Exception(StatusCode.AWS_S3_UPLOAD_FAIL);
        }

        String fileUrl = amazonS3Client.getUrl(bucketName, key).toString();
        return new FileDto(multipartFile.getOriginalFilename(), fileUrl);
    }

    private void validateFileExist(MultipartFile multipartFile) {
        if (multipartFile.isEmpty())
            throw new AwsS3Exception(StatusCode.INVALID_INPUT_VALUE);
    }

    private String getFileExtension(String originalFileName) {// 파일 확장자 추출
        int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        return originalFileName.substring(fileExtensionIndex + 1);
    }

    public FileListDto updateFiles(final UpdateFileRequest updateFileRequest) {
        String type = updateFileRequest.type();
        String currentUserSocialEmail = getCurrentUserSocialEmail();

        updateFileRequest.urlsToDelete()
                .forEach(url -> deleteSingleFile(type, url));

        List<FileDto> uploadedFiles = updateFileRequest.newFiles().stream()
                .map(file -> uploadSingleFile(file, type, currentUserSocialEmail))
                .toList();
        return new FileListDto(uploadedFiles);
    }

    public void deleteSingleFile(final String type, final String url) {
        String key = buildKey(url);
        S3SaveDir saveDir = S3SaveDir.toEnum(type);
        String bucketName = bucket + saveDir.backPath;

        try {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, key)); // TODO 삭제 응답 확인 -> 예외 처리
        } catch (AmazonServiceException e) {
            log.warn("S3 파일 삭제 실패 = {}", e.getMessage());
            throw new AwsS3Exception(StatusCode.AWS_S3_DELETE_FAIL);
        }
    }


    private String buildKey(String originalFileName, String owner) {    // 실제 파일 이름으로부터 key 생성
        return MessageFormat.format(S3_OBJECT_NAME_PATTERN, owner, UUID.randomUUID(), getFileExtension(originalFileName));
    }

    private String buildKey(String url) {   // URL로부터 key 생성
        String[] parsedUrl = url.split(URL_SEPARATOR);
        String fileName = parsedUrl[parsedUrl.length - 1];
        return URLDecoder.decode(fileName, StandardCharsets.UTF_8);
    }
}
