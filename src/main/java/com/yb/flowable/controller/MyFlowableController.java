package com.yb.flowable.controller;

import com.yb.flowable.common.ResponseResult;
import com.yb.flowable.request.Vacation;
import com.yb.flowable.request.VacationTask;
import com.yb.flowable.service.MyFlowableService;
import org.flowable.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * Description:请假流程接口类
 * author biaoyang
 * date 2019/3/12 001217:58
 */
@RestController
@CrossOrigin
@Validated
public class MyFlowableController {

    @Autowired
    private MyFlowableService myFlowableService;

    /**
     * 填写请假信息开始请假
     *
     * @param vacation
     * @return
     */
    @PostMapping("/start")
    public ResponseResult<Object> startVacation(@Valid @RequestBody Vacation vacation ) {
        String result = myFlowableService.startVacation(vacation);
        return ResponseResult.successResultData(result);
    }

    /**
     * 查询我的请假信息
     *
     * @return
     */
    @GetMapping("/queryMyVacation")
    public ResponseResult<Object> queryMyVacation(@RequestParam String userName) {
        List<Vacation> result = myFlowableService.queryMyVacation(userName);
        return ResponseResult.successResultData(result);
    }

    /**
     * 查询我的请假记录
     * @return
     */
    @GetMapping("/myVacationRecord")
    public ResponseResult<Object> myVacationRecord(@RequestParam String userName) {
        List<Vacation> result = myFlowableService.myVacationRecord(userName);
        return ResponseResult.successResultData(result);
    }

    //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    /**
     * 查询我的审批信息
     * @return
     */
    @GetMapping("/queryMyAudit")
    public ResponseResult<Object> queryMyAudit(@RequestParam String userName) {
        List<VacationTask> result = myFlowableService.queryMyAudit(userName);
        return ResponseResult.successResultData(result);
    }

    /**
     * 通过审批
     * @return
     */
    @PostMapping("/passAudit")
    public ResponseResult<Object> passAudit(@Valid @RequestBody VacationTask vacationTask) {
        String result = myFlowableService.passAudit(vacationTask);
        return ResponseResult.successResultData(result);
    }

    /**
     * 查询我的审批记录
     * @return
     */
    @GetMapping("/queryMyAuditRecord")
    public ResponseResult<Object> queryMyAuditRecord(@RequestParam String userName) {
        List<Vacation> result = myFlowableService.queryMyAuditRecord(userName);
        return ResponseResult.successResultData(result);
    }

    //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    /**
     * 下载(查看)流程图(PNG),未被标红的是没有开始的流程
     * @param processId
     */
    @GetMapping(value = "/generateProcessDiagram",produces = MediaType.IMAGE_PNG_VALUE)
    public void generateProcessDiagram(HttpServletResponse response, @RequestParam @NotBlank(message = "流程id不能为空") String processId) throws IOException {
        myFlowableService.generateProcessDiagram(response,processId);
    }
}
