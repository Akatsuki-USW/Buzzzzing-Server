package bokjak.bokjakserver.domain.image.dto;

import bokjak.bokjakserver.common.constant.ConstraintConstants;
import bokjak.bokjakserver.common.constant.MessageConstants;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImageDto {
    public record UploadFileRequest(
            @NotBlank @Size(max = ConstraintConstants.S3_FILE_TYPE_MAX_LENGTH)
            String type,
            @NotEmpty
            List<MultipartFile> files
    ) {
    }

    public record UpdateFileRequest(
            @NotBlank @Size(max = ConstraintConstants.S3_FILE_TYPE_MAX_LENGTH)
            String type,
            // 생성할 파일, 삭제할 파일은 NULL일 수 있음
            List<String> urlsToDelete,
            List<MultipartFile> newFiles
    ) {
        public UpdateFileRequest{   // NULL일 경우 빈 리스트로 설정
            if (Objects.isNull(urlsToDelete)) {
                urlsToDelete = new ArrayList<>();
            }
            if (Objects.isNull(newFiles)) {
                newFiles = new ArrayList<>();
            }
        }
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



