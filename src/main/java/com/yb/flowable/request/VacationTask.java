package com.yb.flowable.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.UUID;

/**
 * Description:请假任务信息封装类
 * author biaoyang
 * date 2019/3/14 001411:32
 */
@Setter
@Getter
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

}
