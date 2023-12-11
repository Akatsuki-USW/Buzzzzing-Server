package bokjak.bokjakserver.domain.image.controller;

import static bokjak.bokjakserver.common.dto.ApiResponse.success;

import bokjak.bokjakserver.common.constant.SwaggerConstants;
import bokjak.bokjakserver.common.dto.ApiResponse;
import bokjak.bokjakserver.domain.image.S3SaveDir;
import bokjak.bokjakserver.domain.image.dto.ImageDto;
import bokjak.bokjakserver.domain.image.dto.ImageDto.DeleteFileResponse;
import bokjak.bokjakserver.domain.image.dto.ImageDto.UpdateFilesResponse;
import bokjak.bokjakserver.domain.image.dto.ImageDto.UploadFilesResponse;
import bokjak.bokjakserver.domain.image.service.AwsS3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Tag(name = SwaggerConstants.TAG_S3, description = SwaggerConstants.TAG_S3_DESCRIPTION)
public class ImageController {
    private final AwsS3Service awsS3Service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = SwaggerConstants.S3_FILE_UPLOAD, description = SwaggerConstants.S3_FILE_UPLOAD_DESCRIPTION)
    public ApiResponse<UploadFilesResponse> uploadFiles(
            @Valid @ModelAttribute ImageDto.UploadFileRequest uploadFileRequest) {
        return success(awsS3Service.uploadFiles(uploadFileRequest));
    }

    @PostMapping(value = "/change", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = SwaggerConstants.SECURITY_SCHEME_NAME)
    @Operation(summary = SwaggerConstants.S3_FILE_UPDATE, description = SwaggerConstants.S3_FILE_UPDATE_DESCRIPTION)
    public ApiResponse<UpdateFilesResponse> updateFiles(
            @Valid @ModelAttribute ImageDto.UpdateFileRequest updateFileRequest) {
        return success(awsS3Service.updateFiles(updateFileRequest));
    }

    @DeleteMapping("/{type}")
    @SecurityRequirement(name = SwaggerConstants.SECURITY_SCHEME_NAME)
    @Operation(summary = SwaggerConstants.S3_FILE_DELETE, description = SwaggerConstants.S3_FILE_DELETE_DESCRIPTION)
    public ApiResponse<DeleteFileResponse> deleteFiles(
            @PathVariable(value = "type") String type,
            @RequestParam List<String> fileUrl
    ) {
        awsS3Service.deleteMultipleFile(S3SaveDir.toEnum(type), fileUrl);
        return success(DeleteFileResponse.success());
    }
}