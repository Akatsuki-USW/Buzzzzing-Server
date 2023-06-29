package bokjak.bokjakserver.util.s3.controller;

import bokjak.bokjakserver.common.constant.MessageConstants;
import bokjak.bokjakserver.common.dto.ApiResponse;
import bokjak.bokjakserver.util.s3.dto.AwsS3Dto;
import bokjak.bokjakserver.util.s3.dto.AwsS3Dto.DeleteFileResponse;
import bokjak.bokjakserver.util.s3.dto.AwsS3Dto.FileListDto;
import bokjak.bokjakserver.util.s3.dto.AwsS3Dto.UploadFileRequest;
import bokjak.bokjakserver.util.s3.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static bokjak.bokjakserver.common.dto.ApiResponse.success;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class AwsS3Controller {
    private final AwsS3Service awsS3Service;

    @PostMapping
    public ApiResponse<FileListDto> uploadFiles(@ModelAttribute UploadFileRequest uploadFileRequest) {
        return success(awsS3Service.uploadFiles(uploadFileRequest));
    }

    @PostMapping("/change")
    public ApiResponse<FileListDto> updateFiles(@ModelAttribute AwsS3Dto.UpdateFileRequest updateFileRequest) {
        return success(awsS3Service.updateFiles(updateFileRequest));
    }

    @DeleteMapping("/{type}")
    public ApiResponse<DeleteFileResponse> deleteFile(
            @PathVariable(value = "type") String type,
            @RequestParam String fileUrl
    ) {
        awsS3Service.deleteSingleFile(type, fileUrl);
        return success(DeleteFileResponse.success());
    }
}