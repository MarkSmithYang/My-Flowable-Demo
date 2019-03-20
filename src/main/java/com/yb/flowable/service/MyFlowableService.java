package com.yb.flowable.service;

import com.yb.flowable.exception.ParameterErrorException;
import com.yb.flowable.request.Vacation;
import com.yb.flowable.request.VacationTask;
import com.yb.flowable.utils.FlowableUtils;
import org.apache.commons.collections.CollectionUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.OptionPaneUI;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Description:
 * author biaoyang
 * date 2019/3/12 001217:59
 */
@Service
public class MyFlowableService {

    public static final String PROCESS_KEY = "qj";

    @Qualifier("processEngine")
    @Autowired
    private ProcessEngine processEngine;


    /**
     * 填写请假信息开始请假
     *
     * @param vacation
     * @return
     */
    public String startVacation(Vacation vacation) {
        //为流程添加StartUserId(开始流程用户)--不添加则在后面通过
        //processEngine.getRuntimeService().createProcessInstanceQuery().startedBy(jack).list()
        //不能获取到内容,而且这个设置必须在流程开始之前进行设置
        //authenticatedUserId：当前已通过身份验证的用户的ID,如果没有用户通过身份验证,则该变量不可用
        processEngine.getIdentityService().setAuthenticatedUserId(vacation.getApplyUser());
        //开始流程
        ProcessInstance instance = processEngine.getRuntimeService().startProcessInstanceByKey(PROCESS_KEY);
        //可以在开始流程的时候设置为流程设置变量
        ProcessInstance in = processEngine.getRuntimeService().startProcessInstanceByKey(PROCESS_KEY, new HashMap<String, Object>() {{
            put("hah", "hello-word");
        }});
        //查询当前任务
        Task task = processEngine.getTaskService().createTaskQuery().processInstanceId(instance.getId()).singleResult();
        //申明任务属于谁
        processEngine.getTaskService().claim(task.getId(), vacation.getApplyUser());
        //设置任务信息
        Map<String, Object> map = new HashMap<>();
        map.put("applyUser", vacation.getApplyUser());
        map.put("days", vacation.getDays());
        map.put("reason", vacation.getReason());
        //完成任务
        processEngine.getTaskService().complete(task.getId(), map);
        //返回信息
        return "操作成功";
    }

    /**
     * 通过流程实例获取请假信息
     *
     * @param processInstance
     * @return
     */
    public Vacation getVacationByProcessInstance(ProcessInstance processInstance) {
        //获取流程的变量信息
        Double days = processEngine.getRuntimeService().getVariable(processInstance.getId(), "days", Double.class);
        String reason = processEngine.getRuntimeService().getVariable(processInstance.getId(), "reason", String.class);
        //封装数据
        Vacation vacation = new Vacation();
        vacation.setApplyDate(processInstance.getStartTime());
        vacation.setApplyStatus(processInstance.isEnded() ? "完成" : "审批中");
        vacation.setApplyUser(processInstance.getStartUserId());
        vacation.setDays(days);
        vacation.setReason(reason);
        return vacation;
    }

    /**
     * 查询我的请假信息
     *
     * @param jack
     * @return
     */
    public List<Vacation> queryMyVacation(String jack) {
        //初始化一个封账请假信息的集合
        List<Vacation> vacations = new ArrayList<>(5);
        //查询我发起的流程信息
        List<ProcessInstance> list = processEngine.getRuntimeService().createProcessInstanceQuery().startedBy(jack).list();
        //判断并处理结果
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(s -> {
                //通过流程实例获取请假信息
                Vacation vacation = getVacationByProcessInstance(s);
                //添加到封装集合中
                vacations.add(vacation);
            });
        }
        //返回数据
        return vacations;
    }

    /**
     * 查询我得审批信息
     *
     * @param rose
     * @return
     */
    public List<VacationTask> queryMyAudit(String rose) {
        //初始化请假任务集合
        List<VacationTask> vacationTasks = new ArrayList<>(10);
        //查询该用户的任务列表
        List<Task> list = processEngine.getTaskService().createTaskQuery().taskAssignee(rose).orderByTaskCreateTime().desc().list();
        //判断并处理数据
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(s -> {
                VacationTask task = new VacationTask();
                task.setTaskId(s.getId());
                task.setTaskName(s.getName());
                task.setCreateDate(s.getCreateTime());
                //获取任务流程实例的id
                String pid = s.getProcessInstanceId();
                //通过流程实例id获取流程实例
                ProcessInstance instance = processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(pid).singleResult();
                //调用通过流程实例获取请假信息的方法获取请假信息
                Vacation vacation = getVacationByProcessInstance(instance);
                //封装请假信息到请假任务里
                task.setVacation(vacation);
                //添加到集合
                vacationTasks.add(task);
            });
        }
        //------------------------------------------------------------------------------------------------
        //------------------------------------------------------------------------------------------------
        //返回信息
        return vacationTasks;
    }

    /**
     * 查询我的请假记录
     *
     * @param jack
     * @return
     */
    public List<Vacation> myVacationRecord(String jack) {
        //初始化请假任务集合
        List<Vacation> vacations = new ArrayList<>(10);
        //因为流程走过之后是不会留存数据的,所以只能去历史流程实例里去获取数据
        List<HistoricProcessInstance> list = processEngine.getHistoryService().createHistoricProcessInstanceQuery()
                .processDefinitionKey(PROCESS_KEY).involvedUser(jack).orderByProcessInstanceEndTime().desc().list();
        //这个和上面的有何不同呢--------------------------------------------------------------------------------
        List<HistoricProcessInstance> list1 = processEngine.getHistoryService().createHistoricProcessInstanceQuery()
                .startedBy(jack).orderByProcessInstanceEndTime().desc().list();
        //判断并处理数据
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(s -> {
                Vacation vacation = new Vacation();
                vacation.setApplyUser(s.getStartUserId());
                vacation.setApplyDate(s.getStartTime());
                //因为只有流程走完之后数据才会到历史里去,而且审批中的请假记录也是不完整的,所以请假记录里只有请假完成的信息
                vacation.setApplyStatus("完成");
                //获取历史流程实例变量
                List<HistoricVariableInstance> results = processEngine.getHistoryService().createHistoricVariableInstanceQuery()
                        .processInstanceId(s.getId()).list();
                //因为只能获取到变量对应的名称和值无法给实体对应字段赋值,所以调用工具方法通过变量名获取对应的字段,并为其赋值
                FlowableUtils.setVarsToEntity(vacation, results);
                //封装实体
                vacations.add(vacation);
            });
        }
        //返回数据
        return vacations;
    }

    /**
     * 查询我的审批记录
     *
     * @param rose
     * @return
     */
    public List<Vacation> queryMyAuditRecord(String rose) {
        //初始化请假任务集合
        List<Vacation> vacations = new ArrayList<>(10);
        //因为流程走过之后是不会留存数据的,所以只能去历史流程实例里去获取数据
        List<HistoricProcessInstance> list = processEngine.getHistoryService().createHistoricProcessInstanceQuery()
                .processDefinitionKey(PROCESS_KEY).involvedUser(rose).orderByProcessInstanceEndTime().desc().list();
        //初始化审批任务人名集合
        List<String> auditTaskNames = new ArrayList<>(3);
        auditTaskNames.add("班长审批");
        auditTaskNames.add("班主任审批");
        auditTaskNames.add("任课老师审批");
        //判断并处理集合数据
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(s -> {
                List<HistoricTaskInstance> hisTaskList = processEngine.getHistoryService().createHistoricTaskInstanceQuery()
                        .processInstanceId(s.getId())
                        .processFinished()
                        //流程任务处理人
                        .taskAssignee(rose)
                        //流程任务名
                        .taskNameIn(auditTaskNames)
                        .orderByHistoricTaskInstanceEndTime()
                        .desc().list();
                //获取历史任务实例里的处理人查看是不是我的
                if (CollectionUtils.isNotEmpty(hisTaskList)) {
                    hisTaskList.forEach(f -> {
                        //判断处理任务的人是不是我
                        if (rose.equals(f.getAssignee())) {
                            Vacation vacation = new Vacation();
                            vacation.setApplyStatus("完成");
                            vacation.setApplyUser(s.getStartUserId());
                            vacation.setApplyDate(s.getStartTime());
                            //获取流程实例的变量
                            List<HistoricVariableInstance> hisVars = processEngine.getHistoryService()
                                    .createHistoricVariableInstanceQuery().processInstanceId(s.getId()).list();
                            //把历史流程实例里的变量赋值到实体里(主要就是days和reason)
                            FlowableUtils.setVarsToEntity(vacation, hisVars);
                            //封装请假信息列表
                            vacations.add(vacation);
                        }
                    });
                }
            });
        }
        //返回数据
        return vacations;
    }

    /**
     * 通过审批
     *
     * @param vacationTask
     * @return
     */
    public String passAudit(VacationTask vacationTask) {
        //获取审批信息
        String taskId = vacationTask.getTaskId();
        String auditResult = vacationTask.getVacation().getAuditResult();
        //封装数据
        Map<String, Object> vars = new HashMap<>(5);
        vars.put("auditor", vacationTask.getAuditor());
        vars.put("auditResult", auditResult);
        vars.put("auditDate", new Date());
        //声明审批人
        processEngine.getTaskService().claim(taskId, vacationTask.getAuditor());
        //完成任务
        processEngine.getTaskService().complete(taskId, vars);
        //返回结果
        return "操作成功";
    }

    /**
     * 下载(查看)流程图(PNG),未被标红的是没有开始的流程
     *
     * @param response
     * @param processId
     */
    public void generateProcessDiagram(HttpServletResponse response, String processId) throws IOException {
        //初始化一个封装id的集合
        List<String> activitys = new ArrayList<>();
        //根据流程实例id获取流程实例-->可以不查看(下载)已经完成的流程通过流程,
        //想看已经完成的流程图的话可以通过History查询去查询操作接口
        ProcessInstance instance = processEngine.getRuntimeService().createProcessInstanceQuery()
                .processInstanceId(processId).singleResult();
        if (instance == null) {
            ParameterErrorException.message("查询流程不存在或者流程已经走完了");
        }
        //获取当前进行中的任务--(没有意义,看到网上通过task去获取processDefinitionId)
        Task task = processEngine.getTaskService().createTaskQuery()
                .processInstanceId(instance.getId()).singleResult();
        //其实可以直接通过流程实例获取流程的定义id的
        String processDefinitionId = instance.getProcessDefinitionId();
        //通过流程定义id获取正在执行的对象列表
        List<Execution> list = processEngine.getRuntimeService().createExecutionQuery()
                .processDefinitionId(processDefinitionId).list();
        //判断并处理程序
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(execution -> {
                List<String> ids = processEngine.getRuntimeService().getActiveActivityIds(execution.getId());
                //把id添加到集合里
                activitys.addAll(ids);
            });
        }
        //获取流程图模型
        BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(instance.getProcessDefinitionId());
        //获取流程图表生成对象
        ProcessEngineConfiguration engconf = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator generator = engconf.getProcessDiagramGenerator();
        //获取流程图的流文件
        //实测这个流能生成图,但是任务名称全是框框样的乱码,而且正在活动的任务没有被标红
        InputStream is = generator.generatePngDiagram(bpmnModel, true);
        //实测这个流也能生成图,而且任务名称全是框框样的乱码,而且正在活动的任务没有被标红
        InputStream in = generator.generatePngDiagram(bpmnModel, 1.0, false);
        //实测这个流能生成图,正在活动的任务没有被标红,并且人物名没有乱码
        InputStream inputStream = generator.generateDiagram(bpmnModel, "png", activitys, new ArrayList<>(),
                engconf.getActivityFontName(), engconf.getLabelFontName(), engconf.getAnnotationFontName(),
                engconf.getClassLoader(), 1.0, false);
        //声明变量
        OutputStream outputStream = null;
        byte[] bytes = new byte[1024];
        int length;
        try {
            //获取响应流
            outputStream = response.getOutputStream();
            //输出流文件
            while ((length = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, length);
            }
        } finally {
            //关闭流
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
