package ink.ckx.mo.common.oss.service.impl

import cn.hutool.core.date.DateUtil
import cn.hutool.core.io.FileUtil
import cn.hutool.core.util.IdUtil
import com.aliyun.oss.OSS
import com.aliyun.oss.model.ObjectMetadata
import com.aliyun.oss.model.PutObjectRequest
import ink.ckx.mo.common.core.exception.BusinessException
import ink.ckx.mo.common.core.result.ResultCode
import ink.ckx.mo.common.oss.config.AliyunOssConfigProperties
import ink.ckx.mo.common.oss.service.OssService
import ink.ckx.mo.common.oss.vo.FileInfoVO
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/14
 */
@Service
class OssServiceImpl(
    private val aliyunOssConfigProperties: AliyunOssConfigProperties,
    private val oss: OSS,
) : OssService {

    override fun uploadFile(file: MultipartFile): FileInfoVO {

        val bucketName = aliyunOssConfigProperties.bucketName
        val endpoint = aliyunOssConfigProperties.endpoint
        // 生成文件名
        val suffix = FileUtil.getSuffix(file.originalFilename)
        val uuid = IdUtil.simpleUUID()
        val fileName = DateUtil.today() + "/" + uuid + "." + suffix
        //  上传文件
        try {
            file.inputStream.use {
                // 设置上传文件的元信息，例如 Content-Type
                val metadata = ObjectMetadata()
                metadata.contentType = file.contentType
                // 创建PutObjectRequest对象，指定Bucket名称、对象名称和输入流
                val putObjectRequest = PutObjectRequest(bucketName, fileName, it, metadata)
                // 上传文件
                oss.putObject(putObjectRequest)
            }
        } catch (e: Exception) {
            throw BusinessException(ResultCode.FILE_UPLOAD_ERROR)
        }
        // 获取文件访问路径
        val fileUrl = "https://$bucketName.$endpoint/$fileName"
        return FileInfoVO(fileName, fileUrl)
    }

    override fun deleteFile(key: String) {
        oss.deleteObject(aliyunOssConfigProperties.bucketName, key)
    }
}