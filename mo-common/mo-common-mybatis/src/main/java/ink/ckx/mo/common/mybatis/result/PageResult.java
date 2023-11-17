package ink.ckx.mo.common.mybatis.result;

import com.baomidou.mybatisplus.core.metadata.IPage;
import ink.ckx.mo.common.core.result.ResultCode;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author chenkaixin
 * @description
 * @since 2023/10/19
 */
@Data
public class PageResult<T> implements Serializable {

    private String code;

    private Data<T> data;

    private String msg;

    public static <T> PageResult<T> success(IPage<T> page) {
        PageResult<T> result = new PageResult<>();
        result.setCode(ResultCode.SUCCESS.getCode());

        Data<T> data = new Data<T>();
        data.setList(page.getRecords());
        data.setTotal(page.getTotal());

        result.setData(data);
        result.setMsg(ResultCode.SUCCESS.getMsg());
        return result;
    }

    @lombok.Data
    public static class Data<T> {

        private List<T> list;

        private long total;
    }
}