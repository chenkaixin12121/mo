package ink.ckx.mo.common.oss.controller;

import ink.ckx.mo.common.core.result.Result;
import ink.ckx.mo.common.oss.query.DeleteFileQuery;
import ink.ckx.mo.common.oss.service.OssService;
import ink.ckx.mo.common.oss.vo.FileInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/14
 */
@Tag(name = "文件接口")
@RestController
@RequestMapping("/oss")
@RequiredArgsConstructor
public class OssController {

    private final OssService ossService;

    @PreAuthorize("@pms.hasPerm('oss:upload')")
    @Operation(summary = "上传文件")
    @PostMapping
    public Result<FileInfoVO> uploadFile(@Parameter(description = "文件对象") @RequestParam(value = "file") MultipartFile file) {
        FileInfoVO fileInfoVO = ossService.uploadFile(file);
        return Result.success(fileInfoVO);
    }

    @PreAuthorize("@pms.hasPerm('oss:delete')")
    @Operation(summary = "删除文件")
    @DeleteMapping
    public Result<Void> deleteFile(@Valid @RequestBody DeleteFileQuery deleteFileQuery) {
        ossService.deleteFile(deleteFileQuery.getKey());
        return Result.success();
    }
}