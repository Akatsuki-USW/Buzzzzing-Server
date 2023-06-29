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
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;

import static bokjak.bokjakserver.config.security.SecurityUtils.getCurrentUserSocialEmail;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsS3Service {
    private static final String FILE_EXTENSION_SEPARATOR = ".";
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    // 복수 파일 업로드
    public FileListDto uploadFiles(final UploadFileRequest uploadFileRequest) {
        String currentUserSocialEmail = getCurrentUserSocialEmail();    // 유저 식별정보
        List<MultipartFile> files = uploadFileRequest.files();

        if (CollectionUtils.isEmpty(files))     // 파일 존재 여부 검사
            throw new AwsS3Exception(StatusCode.INVALID_INPUT_VALUE);

        List<FileDto> uploadedFiles = uploadFileRequest.files().stream()    // 업로드
                .map(file -> uploadSingleFile(file, uploadFileRequest.type(), currentUserSocialEmail))
                .toList();
        return new FileListDto(uploadedFiles);
    }

    // 단일 파일 업로드
    public FileDto uploadSingleFile(final MultipartFile multipartFile, final String stringParam, final String owner) {
        validateFileExist(multipartFile);   // 파일 유효성 검사

        String fileName = buildFileName(Objects.requireNonNull(multipartFile.getOriginalFilename()), owner);

        ObjectMetadata objectMetadata = new ObjectMetadata();   // 생성할 S3 Object 메타 데이터 설정
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());
        S3SaveDir saveDir = S3SaveDir.toEnum(stringParam);  // 경로명 변수
        String bucketPath = bucketName + saveDir.path;

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucketPath, fileName, inputStream, objectMetadata)    // 업로드
                    .withCannedAcl(CannedAccessControlList.PublicRead));    // ACL public read로 설정
        } catch (IOException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        } catch (AmazonServiceException e) {
            throw new AwsS3Exception(StatusCode.AWS_S3_UPLOAD_FAIL);
        }

        String fileUrl = amazonS3Client.getUrl(bucketPath, fileName).toString();    // 생성된 파일 URL
        return new FileDto(multipartFile.getOriginalFilename(), fileUrl);
    }

    // 파일 존재 여부 검사
    private void validateFileExist(MultipartFile multipartFile) {
        if (multipartFile.isEmpty())
            throw new AwsS3Exception(StatusCode.AWS_S3_UPLOAD_FAIL);
    }

    // 파일 확장자 추출
    private String getFileExtension(String originalFileName) {
        int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        return originalFileName.substring(fileExtensionIndex + 1);
    }

    // 파일명 생성
    private String buildFileName(String originalFileName, String owner) {
        return MessageFormat.format("{0}_{1}.{2}", owner, UUID.randomUUID(), getFileExtension(originalFileName));
    }

    // 업데이트
    public FileListDto updateFiles(final UpdateFileRequest updateFileRequest) {
        String type = updateFileRequest.type();
        String currentUserSocialEmail = getCurrentUserSocialEmail();

        List<String> urls = updateFileRequest.urlsToDelete();
        if (CollectionUtils.isEmpty(urls)) {    // url 존재 여부 검사
            throw new AwsS3Exception(StatusCode.AWS_S3_DELETE_FAIL);
        }

        updateFileRequest.urlsToDelete()    // 기존 파일 삭제
                .forEach(file -> deleteSingleFile(type, file));

        List<FileDto> uploadedFiles = updateFileRequest.newFiles().stream() // 업로드
                .map(file -> uploadSingleFile(file, type, currentUserSocialEmail))
                .toList();
        return new FileListDto(uploadedFiles);
    }

    // 단일 파일 삭제
    public void deleteSingleFile(final String type, final String url) {
        String filename = getFileName(url); // 파일 경로명 구하기
        S3SaveDir saveDir = S3SaveDir.toEnum(type);
        String bucketPath = bucketName + saveDir.path;

        try {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucketPath, filename)); // 삭제
        } catch (AmazonServiceException e) {
            log.warn("S3 파일 삭제 실패 = {}", e.getMessage());
            throw new AwsS3Exception(StatusCode.AWS_S3_DELETE_FAIL);
        }
    }

    // url로부터 파일 이름 추출
    private String getFileName(String url) {
        String[] parsedUrl = url.split("/");
        String last = parsedUrl[parsedUrl.length - 1];  // 파일명
        return URLDecoder.decode(last, StandardCharsets.UTF_8);
    }
}
