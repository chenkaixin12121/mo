package ink.ckx.mo.common.oss.controller

import ink.ckx.mo.common.core.result.Result
import ink.ckx.mo.common.core.result.Result.Companion.success
import ink.ckx.mo.common.oss.query.DeleteFileQuery
import ink.ckx.mo.common.oss.service.OssService
import ink.ckx.mo.common.oss.vo.FileInfoVO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/14
 */
@Tag(name = "文件接口")
@RestController
@RequestMapping("/oss")
class OssController(private val ossService: OssService) {

    @PreAuthorize("@pms.hasPerm('oss:upload')")
    @Operation(summary = "上传文件")
    @PostMapping
    fun uploadFile(@Parameter(description = "文件对象") @RequestParam(value = "file") file: MultipartFile): Result<FileInfoVO> {
        val fileInfoVO = ossService.uploadFile(file)
        return success(fileInfoVO)
    }

    @PreAuthorize("@pms.hasPerm('oss:delete')")
    @Operation(summary = "删除文件")
    @DeleteMapping
    fun deleteFile(@RequestBody @Valid deleteFileQuery: DeleteFileQuery): Result<Void?> {
        deleteFileQuery.key?.let { ossService.deleteFile(it) }
        return success();
    }
}