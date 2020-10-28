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
public class JdkDynamicAopProxy implements InvocationHandler {
    private AdvisedSupport config;

    public JdkDynamicAopProxy(AdvisedSupport config) {
        this.config = config;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Map<String, Advice> advices = config.getAdvices(method, null);
        Object ret = null;
        try {
            invokeAdvice(advices.get("before"));

            ret = method.invoke(this.config.getTarget(), args);

            invokeAdvice(advices.get("after"));

        } catch (Exception e) {
            invokeAdvice(advices.get("afterThrow"));
            throw e;
        }
        return ret;
    }

    private void invokeAdvice(Advice advice) {
        try {
            advice.getAdviceMethod().invoke(advice.getAspect());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public Object getProxy() {
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), this.config.getTargetClass().getInterfaces(), this);
    }
}
