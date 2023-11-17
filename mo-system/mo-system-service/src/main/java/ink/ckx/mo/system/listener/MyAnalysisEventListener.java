package ink.ckx.mo.system.listener;

import com.alibaba.excel.event.AnalysisEventListener;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/23
 */
public abstract class MyAnalysisEventListener<T> extends AnalysisEventListener<T> {

    private String msg;

    public abstract String getMsg();
}