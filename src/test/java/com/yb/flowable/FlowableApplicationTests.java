package com.yb.flowable;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.repository.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FlowableApplicationTests {

    @Qualifier("processEngine")
    @Autowired
    private ProcessEngine processEngine;

    @Test
    public void contextLoads() {
        Deployment deploy = processEngine.getRepositoryService().createDeployment()
                .addClasspathResource("processes\\请假.bpmn20.xml")
                //设置流程名称
                .name("学校请假流程")
                //设置流程的分类
                .category("人事类")
                //部署流程
                .deploy();
    }

}
