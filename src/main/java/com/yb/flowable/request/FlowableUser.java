package com.yb.flowable.request;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * Description:flowable封装用户参数的类
 * author biaoyang
 * date 2019/3/15 001510:01
 */
public class FlowableUser {

    //用户名
    @NotBlank(message = "用户名不能为空")
    @Length(max = 20,message = "用户名不能超过20字")
    private String username;
    //密码
    @NotBlank(message = "用户密码不能为空")
    @Length(max = 20,message = "用户密码不能超过20字")
    private String password;
    //用户(所属)组
    @Length(max = 20,message = "用户组不能超过20字")
    private String groupName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
