package com.yb.flowable.request;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Description:请假信息封装类
 * author biaoyang
 * date 2019/3/12 001218:02
 */
@Setter
@Getter//实测,当此工程打成jar包以后运行,也没有报错,说明lombok是可以的在项目里运行的,而idea安不安装插件根本与之无关
public class Vacation {

    private String id;
    //申请日期
    private Date applyDate;
    //申请人
    @NotBlank(message = "申请人不能为空")
    @Length(max = 20,message = "申请人不能超过20字")
    private String applyUser;
    //申请状态
    @Length(max = 10,message = "申请状态不能超过10字")
    private String applyStatus;
    //请假理由
    @NotBlank(message = "请假理由不能为空")
    @Length(max = 200,message = "请假理由不能超过200字")
    private String reason;
    //请假天数
    @NotNull(message = "请假天数不能为空")
    @Min(value = 0, message = "请正确填写请假时间")
    @Max(value = 5, message = "请假不能超多5天")
    private double days;
//-----------------------------------------------------------------------------------------------
    //审核人
    private String auditor;
    //审核结果
    private String  auditResult;
    //审核日期
    private Date auditDate;

}
