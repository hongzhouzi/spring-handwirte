package pers.whz.spring.framework.demo.aspect;

import lombok.extern.slf4j.Slf4j;

/**
 * 测试aop的日志记录类
 *
 * @author hongzhou.wei
 * @date 2020/10/28
 */
@Slf4j
public class LogAspect {

    /**
     * 在调用一个方法之前，执行before方法
     */
    public void before() {
        //这个方法中的逻辑，是由我们自己写的
        String info = "Invoker Before Method!!!";
        System.out.println(info);
        log.info(info);
    }

    /**
     * 在调用一个方法之后，执行after方法
     */
    public void after() {
        String info = "Invoker After Method!!!";
        System.out.println(info);
        log.info(info);
    }

    public void afterThrowing() {
        String info = "Invoker afterThrowing Method!!!";
        System.out.println(info);
        log.info(info);
    }
}

