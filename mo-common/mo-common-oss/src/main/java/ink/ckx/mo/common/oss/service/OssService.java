package ink.ckx.mo.common.oss.service;

import ink.ckx.mo.common.oss.vo.FileInfoVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/14
 */

public interface OssService {

    FileInfoVO uploadFile(MultipartFile file);

    void deleteFile(String key);
}