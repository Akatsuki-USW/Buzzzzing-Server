package bokjak.bokjakserver.util.s3.dto;

import bokjak.bokjakserver.common.constant.MessageConstants;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class AwsS3Dto {
    public record UploadFileRequest(
            @NotBlank @Size(max = 25)
            String type,
            @NotEmpty
            List<MultipartFile> files
    ) {
    }

    public record UpdateFileRequest(
            @NotBlank @Size(max = 25)
            String type,
            @NotEmpty
            List<String> urlsToDelete,
            @NotEmpty   // 파일은 Empty 검사가 안 됨 -> 따로 유효성 검사
            List<MultipartFile> newFiles
    ) {
    }

    public record FileDto(
            String filename,
            String fileUrl
    ) {
    }

    public record FileListDto(
            List<FileDto> files
    ) {
    }

    public record DeleteFileResponse(
            String message
    ) {
        public static DeleteFileResponse success() {
            return new DeleteFileResponse(MessageConstants.S3_FILE_DELETE_SUCCESS);
        }
    }
}



