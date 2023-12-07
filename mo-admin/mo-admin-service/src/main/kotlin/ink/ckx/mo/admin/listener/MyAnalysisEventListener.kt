package ink.ckx.mo.admin.listener

import com.alibaba.excel.event.AnalysisEventListener

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/23
 */
abstract class MyAnalysisEventListener<T> : AnalysisEventListener<T>() {

    abstract val msg: StringBuilder
}