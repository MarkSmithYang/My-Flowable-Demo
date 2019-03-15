package com.yb.flowable.controller;

import com.yb.flowable.common.ResponseResult;
import com.yb.flowable.request.FlowableUser;
import com.yb.flowable.service.FlowableUserService;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Description:flowable的用户接口类
 * author biaoyang
 * date 2019/3/15 00159:53
 */
@RestController
@CrossOrigin
@Validated
public class FlowableUserController {

    @Autowired
    private FlowableUserService flowableUserService;

    /**
     * 添加flowable用户
     *
     * @param flowableUser
     * @return
     */
    @PostMapping("/addUser")
    public ResponseResult<Object> addUser(@Valid @RequestBody FlowableUser flowableUser) {
        String result = flowableUserService.addUser(flowableUser);
        return ResponseResult.successResultData(result);
    }

    /**
     * flowable用户登录
     *
     * @param flowableUser
     * @return
     */
    @PostMapping("/login")
    public ResponseResult<Object> login(HttpSession session, @Valid @RequestBody FlowableUser flowableUser) {
        String result = flowableUserService.login(session, flowableUser);
        return ResponseResult.successResultData(result);
    }

    /**
     * 退出登录操作
     *
     * @param session
     * @return
     */
    @GetMapping("/logout")
    public ResponseResult<Object> login(HttpSession session) {
        session.removeAttribute("username");
        return ResponseResult.successResultData("操作成功");
    }

    /**
     * 查询所有flowable用户
     *
     * @return
     */
    @GetMapping("/queryAllUser")
    public ResponseResult<Object> queryAllUser() {
        List<User> result = flowableUserService.queryAllUser();
        return ResponseResult.successResultData(result);
    }

    /**
     * 查询所有flowable组
     *
     * @return
     */
    @GetMapping("/queryAllGroup")
    public ResponseResult<Object> queryAllGroup() {
        List<Group> result = flowableUserService.queryAllGroup();
        return ResponseResult.successResultData(result);
    }

    /**
     * 通过用户组查询flowable用户
     *
     * @return
     */
    @GetMapping("/queryUserByGroup")
    public ResponseResult<Object> queryUserByGroup(@RequestParam @NotBlank(message = "组名不能为空")
                                                   @Length(max = 20, message = "组名不能超过20字") String groupName) {
        List<User> result = flowableUserService.queryUserByGroup(groupName);
        return ResponseResult.successResultData(result);
    }


}
