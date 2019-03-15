package com.yb.flowable.service;

import com.yb.flowable.request.FlowableUser;
import org.flowable.engine.ProcessEngine;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Description:flowable用户服务类
 * author biaoyang
 * date 2019/3/15 00159:55
 */
@Service
public class FlowableUserService {

    @Qualifier("processEngine")
    @Autowired
    private ProcessEngine processEngine;

    /**
     * 添加flowable用户
     *
     * @param flowableUser
     * @return
     */
    public String addUser(FlowableUser flowableUser) {
        //获取用户名
        String userId = flowableUser.getUsername();
        //获取用户密码
        String password = flowableUser.getPassword();
        //获取用户组
        String groupId = flowableUser.getGroupName();
        //通过用户名创建flowable的用户对象
        User user = processEngine.getIdentityService().newUser(userId);
        //为用户对象设置信息
        user.setPassword(password);
        //保存用户
        processEngine.getIdentityService().saveUser(user);
        //通过用户组创建flowable的用户组对象
        if (StringUtils.hasText(groupId)) {
            Group group = processEngine.getIdentityService().createGroupQuery().groupId(groupId).singleResult();
            if (group == null) {
                //如果组不存在则新建组
                group = processEngine.getIdentityService().newGroup(groupId);
                processEngine.getIdentityService().saveGroup(group);
            }
            //把用户添加到组里去(关联组)
            processEngine.getIdentityService().createMembership(user.getId(), group.getId());
        }
        //返回信息
        return "操作成功";
    }

    /**
     * flowable用户登录
     *
     * @param flowableUser
     * @return
     */
    public String login(HttpSession session, FlowableUser flowableUser) {
        //获取用户名
        String username = flowableUser.getUsername();
        //获取用户密码
        String password = flowableUser.getPassword();
        //校验用户名和密码信息
        boolean flag = processEngine.getIdentityService().checkPassword(username, password);
        //判断并处理数据
        if (flag) {
            session.setAttribute("username", username);
            return "登录成功";
        } else {
            return "用户名或密码不正确";
        }
    }

    /**
     * 查询所有flowable用户
     *
     * @return
     */
    public List<User> queryAllUser() {
        List<User> list = processEngine.getIdentityService().createUserQuery().list();
        return list;
    }

    /**
     * 查询所有flowable组
     *
     * @return
     */
    public List<Group> queryAllGroup() {
        List<Group> list = processEngine.getIdentityService().createGroupQuery().list();
        return list;
    }

    /**
     * 通过用户组查询flowable用户
     *
     * @return
     */
    public List<User> queryUserByGroup(String groupName) {
        List<User> list = processEngine.getIdentityService().createUserQuery().memberOfGroup(groupName).list();
        return list;
    }
}
