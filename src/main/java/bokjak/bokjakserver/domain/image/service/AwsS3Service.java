package bokjak.bokjakserver.domain.image.service;

import static bokjak.bokjakserver.config.security.SecurityUtils.getCurrentUserSocialEmail;
import static bokjak.bokjakserver.domain.image.service.ImageFilePathUtils.buildImageFilePath;
import static bokjak.bokjakserver.domain.image.service.ImageFilePathUtils.buildRootPath;

import bokjak.bokjakserver.common.exception.StatusCode;
import bokjak.bokjakserver.domain.image.S3SaveDir;
import bokjak.bokjakserver.domain.image.dto.ImageDto.FileDto;
import bokjak.bokjakserver.domain.image.dto.ImageDto.FileListDto;
import bokjak.bokjakserver.domain.image.dto.ImageDto.UpdateFileRequest;
import bokjak.bokjakserver.domain.image.dto.ImageDto.UploadFileRequest;
import bokjak.bokjakserver.domain.image.exception.ImageException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsS3Service {
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    public FileListDto uploadFiles(final UploadFileRequest uploadFileRequest) {
        String currentUserSocialEmail = getCurrentUserSocialEmail();

        List<FileDto> uploadedFiles = uploadFileRequest.files().stream()
                .map(file -> uploadSingleFile(file, S3SaveDir.toEnum(uploadFileRequest.type()), currentUserSocialEmail))
                .toList();
        return new FileListDto(uploadedFiles);
    }

    public FileDto uploadSingleFile(final MultipartFile multipartFile, final S3SaveDir saveDir, final String owner) {
        validateFileExist(multipartFile);
        String rootPath = buildRootPath(bucket, saveDir);
        String filePath = buildImageFilePath(multipartFile, owner);

        ObjectMetadata objectMetadata = getObjectMetadataFromMultipartFie(multipartFile);

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(rootPath, filePath, inputStream, objectMetadata)    // 업로드
                    .withCannedAcl(CannedAccessControlList.PublicRead));    // ACL public read로 설정
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (AmazonServiceException e) {
            log.warn("S3 파일 업로드 실패 = {}", e.getMessage());
            throw new ImageException(StatusCode.AWS_S3_UPLOAD_FAIL);
        }

        String fileUrl = amazonS3Client.getUrl(rootPath, filePath).toString();
        return new FileDto(multipartFile.getOriginalFilename(), fileUrl);
    }


    private ObjectMetadata getObjectMetadataFromMultipartFie(MultipartFile multipartFile) {
        ObjectMetadata objectMetadata = new ObjectMetadata();   // 생성할 S3 Object의 메타 데이터 설정
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());
        return objectMetadata;
    }

    private void validateFileExist(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new ImageException(StatusCode.INVALID_INPUT_VALUE);
        }
    }


    public FileListDto updateFiles(final UpdateFileRequest updateFileRequest) {
        S3SaveDir saveDir = S3SaveDir.toEnum(updateFileRequest.type());
        String currentUserSocialEmail = getCurrentUserSocialEmail();

        updateFileRequest.urlsToDelete()
                .forEach(url -> deleteSingleFile(saveDir, url));

        List<FileDto> uploadedFiles = updateFileRequest.newFiles().stream()
                .map(file -> uploadSingleFile(file, saveDir, currentUserSocialEmail))
                .toList();
        return new FileListDto(uploadedFiles);
    }

    public void deleteMultipleFile(S3SaveDir saveDir, List<String> imageUrls) {
        imageUrls.forEach(url -> deleteSingleFile(saveDir, url));
    }

    public void deleteSingleFile(final S3SaveDir saveDir, final String url) {
        String rootPath = buildRootPath(bucket, saveDir);
        String filePath = buildImageFilePath(url);

        try {
            amazonS3Client.deleteObject(new DeleteObjectRequest(rootPath, filePath));
        } catch (AmazonServiceException e) {
            log.warn("S3 파일 삭제 실패 = {}", e.getMessage());
            throw new ImageException(StatusCode.AWS_S3_DELETE_FAIL);
        }
    }


}
