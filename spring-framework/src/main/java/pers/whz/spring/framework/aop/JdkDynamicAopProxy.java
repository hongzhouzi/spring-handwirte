package pers.whz.spring.framework.aop;

import pers.whz.spring.framework.aop.aspect.Advice;
import pers.whz.spring.framework.aop.support.AdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * 使用jdk动态代理技术生成代理类
 *
 * @author hongzhou.wei
 * @date 2020/10/28
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {
    private AdvisedSupport config;

    public JdkDynamicAopProxy(AdvisedSupport config) {
        this.config = config;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Map<String, Advice> advices = config.getAdvices(method, null);

        invokeAdvice(advices.get("before"));

        Object ret;
        try {
            ret = method.invoke(this.config.getTarget(), args);
        } catch (Exception e) {
            invokeAdvice(advices.get("afterThrow"));
            throw e;
        }

        invokeAdvice(advices.get("after"));

        return ret;
    }

    private void invokeAdvice(Advice advice) {
        try {
            advice.getAdviceMethod().invoke(advice.getAspect());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getProxy() {
        return getProxy(this.config.getTargetClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader, this.config.getTargetClass().getInterfaces(), this);
    }
}
