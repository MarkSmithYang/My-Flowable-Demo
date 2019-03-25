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

    /**
     * 其实自动流程这里和那个任课老师审批不应该用并行网关,用排他网关更好,或者直接在任课老师审批之后,现在懒得改了
     * @param execution
     */
    @Override
    public void execute(DelegateExecution execution) {
        System.err.println("我直接放假了啦啦啦");
        //getVariable(String variableName, boolean fetchAllVariables);
        //当对参数fetchAllVariables使用true时，行为将完全如上所述：获取或设置变量时，将获取并缓存所有其他变量。
        //但是，当使用false作为值时，将使用特定查询，并且不会获取或缓存其他变量
        Object hah = execution.getVariable("hah", false);//这个由于没有设置在流程中,所以为空
        Object days = execution.getVariable("days", false);
        Object reason = execution.getVariable("reason", true);
        Object applyUser = execution.getVariable("applyUser", true);
        System.err.println("hah==="+hah);
        System.err.println("days==="+days);
        System.err.println("reason==="+reason);
        System.err.println("applyUser==="+applyUser);
    }
}
