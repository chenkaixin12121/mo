package ink.ckx.mo.common.oss.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import ink.ckx.mo.common.core.exception.BusinessException;
import ink.ckx.mo.common.core.result.ResultCode;
import ink.ckx.mo.common.oss.config.AliyunOssConfig;
import ink.ckx.mo.common.oss.service.OssService;
import ink.ckx.mo.common.oss.vo.FileInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/14
 */
@RequiredArgsConstructor
@Service
public class OssServiceImpl implements OssService {

    private final AliyunOssConfig aliyunOssConfig;

    private final OSS oss;

    @Override
    public FileInfoVO uploadFile(MultipartFile file) {
        String bucketName = aliyunOssConfig.getBucketName();
        String endpoint = aliyunOssConfig.getEndpoint();
        // 生成文件名
        String suffix = FileUtil.getSuffix(file.getOriginalFilename());
        String uuid = IdUtil.simpleUUID();
        String fileName = DateUtil.today() + "/" + uuid + "." + suffix;
        //  上传文件
        try (InputStream inputStream = file.getInputStream()) {
            // 设置上传文件的元信息，例如 Content-Type
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            // 创建PutObjectRequest对象，指定Bucket名称、对象名称和输入流
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, inputStream, metadata);
            // 上传文件
            oss.putObject(putObjectRequest);
        } catch (Exception e) {
            throw new BusinessException(ResultCode.FILE_UPLOAD_ERROR);
        }
        // 获取文件访问路径
        String fileUrl = "https://" + bucketName + "." + endpoint + "/" + fileName;
        FileInfoVO fileInfoVO = new FileInfoVO();
        fileInfoVO.setKey(fileName);
        fileInfoVO.setAccessUrl(fileUrl);
        return fileInfoVO;
    }

    @Override
    public void deleteFile(String key) {
        oss.deleteObject(aliyunOssConfig.getBucketName(), key);
    }
}