package pers.whz.spring.framework.aop;

/**
 * 代理顶层接口
 * 下面分别有基于jdk的和cglib的动态代理
 *
 * @author hongzhou.wei
 * @date 2020/10/29
 */
public interface AopProxy {
    Object getProxy();

    Object getProxy(ClassLoader classLoader);
}
