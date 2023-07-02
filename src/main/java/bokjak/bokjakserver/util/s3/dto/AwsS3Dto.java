package bokjak.bokjakserver.util.s3.dto;

import bokjak.bokjakserver.common.constant.MessageConstants;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class AwsS3Dto {
    public record UploadFileRequest(
            String type,
            List<MultipartFile> files
    ) {
    }

    public record UpdateFileRequest(
            String type,
            List<String> urlsToDelete,
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



