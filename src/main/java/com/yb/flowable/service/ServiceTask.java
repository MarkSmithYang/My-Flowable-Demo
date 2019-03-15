package com.yb.flowable.service;

import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

/**
 * Description:自动流程设置
 * author biaoyang
 * date 2019/3/13 001318:28
 */
public class ServiceTask implements JavaDelegate {

    private Expression text;

    @Override
    public void execute(DelegateExecution execution) {
        System.err.println("我直接放假了啦啦啦");
    }
}
