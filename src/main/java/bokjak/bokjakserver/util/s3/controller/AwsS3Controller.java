package bokjak.bokjakserver.util.s3.controller;

import bokjak.bokjakserver.common.constant.SwaggerConstants;
import bokjak.bokjakserver.common.dto.ApiResponse;
import bokjak.bokjakserver.util.s3.S3SaveDir;
import bokjak.bokjakserver.util.s3.dto.AwsS3Dto.DeleteFileResponse;
import bokjak.bokjakserver.util.s3.dto.AwsS3Dto.FileListDto;
import bokjak.bokjakserver.util.s3.dto.AwsS3Dto.UpdateFileRequest;
import bokjak.bokjakserver.util.s3.dto.AwsS3Dto.UploadFileRequest;
import bokjak.bokjakserver.util.s3.service.AwsS3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static bokjak.bokjakserver.common.dto.ApiResponse.success;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Tag(name = SwaggerConstants.TAG_S3, description = SwaggerConstants.TAG_S3_DESCRIPTION)
public class AwsS3Controller {
    private final AwsS3Service awsS3Service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = SwaggerConstants.S3_FILE_UPLOAD, description = SwaggerConstants.S3_FILE_UPLOAD_DESCRIPTION)
    public ApiResponse<FileListDto> uploadFiles(@Valid @ModelAttribute UploadFileRequest uploadFileRequest) {
        return success(awsS3Service.uploadFiles(uploadFileRequest));
    }

    @PostMapping(value = "/change", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = SwaggerConstants.SECURITY_SCHEME_NAME)
    @Operation(summary = SwaggerConstants.S3_FILE_UPDATE, description = SwaggerConstants.S3_FILE_UPDATE_DESCRIPTION)
    public ApiResponse<FileListDto> updateFiles(@Valid @ModelAttribute UpdateFileRequest updateFileRequest) {
        return success(awsS3Service.updateFiles(updateFileRequest));
    }

    @DeleteMapping("/{type}")
    @Operation(summary = SwaggerConstants.S3_FILE_DELETE, description = SwaggerConstants.S3_FILE_DELETE_DESCRIPTION)
    public ApiResponse<DeleteFileResponse> deleteFile(
            @PathVariable(value = "type") String type,
            @RequestParam String fileUrl
    ) {
        awsS3Service.deleteSingleFile(S3SaveDir.toEnum(type), fileUrl);
        return success(DeleteFileResponse.success());
    }
}