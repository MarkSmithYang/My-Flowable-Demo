package com.yb.flowable.utils;

import org.apache.commons.collections.CollectionUtils;
import org.flowable.variable.api.history.HistoricVariableInstance;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Description:flowable使用中需要的工具
 * author biaoyang
 * date 2019/3/14 001413:52
 */
public class FlowableUtils {

    /**
     * 将历史参数列表设置到实体中去
     *
     * @param entity 实体
     * @param list   ,历史参数列表
     * @param <T>
     */
    public static <T> void setVarsToEntity(T entity, List<HistoricVariableInstance> list) {
        //获取实体的Class对象
        Class<?> entityClass = entity.getClass();
        //通过反射设置量
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(s -> {
                try {
                    //获取字段
                    Field field = entityClass.getDeclaredField(s.getVariableName());
                    //判断字段是否为空
                    if (field != null) {
                        //开启私字段访问权限
                        field.setAccessible(true);
                        //为字段赋值
                        field.set(entity, s.getValue());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
