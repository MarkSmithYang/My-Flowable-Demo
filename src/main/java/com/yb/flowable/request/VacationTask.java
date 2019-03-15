package com.yb.flowable.request;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.UUID;

/**
 * Description:请假任务信息封装类
 * author biaoyang
 * date 2019/3/14 001411:32
 */
public class VacationTask {

    private String taskId;
    @NotBlank(message = "审批人不能为空")
    private String auditor;
    private String taskName;
    private Vacation vacation;
    private Date createDate;

    public VacationTask() {
        this.taskId = UUID.randomUUID().toString().replaceAll("-", "");
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Vacation getVacation() {
        return vacation;
    }

    public void setVacation(Vacation vacation) {
        this.vacation = vacation;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
