package pers.whz.spring.framework.aop.aspect;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * aop通知-实体类
 *
 * @author hongzhou.wei
 * @date 2020/10/28
 */
@Data
public class Advice {
    private Object aspect;
    private Method adviceMethod;
    private String throwName;

    public Advice(Object aspect, Method adviceMethod) {
        this.aspect = aspect;
        this.adviceMethod = adviceMethod;
    }
}
