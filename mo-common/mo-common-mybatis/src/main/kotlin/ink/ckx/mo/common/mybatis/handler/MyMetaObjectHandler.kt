package ink.ckx.mo.common.mybatis.handler

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
import org.apache.ibatis.reflection.MetaObject
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * @author chenkaixin
 * @description 字段自动填充
 * @since 2023/11/09
 */
@Component
class MyMetaObjectHandler : MetaObjectHandler {

    override fun insertFill(metaObject: MetaObject) {
//        this.strictInsertFill(metaObject, "deleted", { 0 }, Int::class.java) TODO
        this.strictInsertFill(metaObject, "createTime", { LocalDateTime.now() }, LocalDateTime::class.java)
        this.strictUpdateFill(metaObject, "updateTime", { LocalDateTime.now() }, LocalDateTime::class.java)
    }

    override fun updateFill(metaObject: MetaObject) {
        this.strictUpdateFill(metaObject, "updateTime", { LocalDateTime.now() }, LocalDateTime::class.java)
    }
}