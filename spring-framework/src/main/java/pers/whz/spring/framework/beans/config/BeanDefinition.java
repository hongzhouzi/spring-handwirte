package pers.whz.spring.framework.beans.config;

/**
 * 封装Bean信息的实体类，方便对扫描到的类信息统一进行封装
 *
 * @author hongzhou.wei
 * @date 2020/10/23
 */
public class BeanDefinition {
    /**
     * 保存beanName
     */
    private String factoryBeanName;
    /**
     * 保存bean的全类名
     */
    private String beanClassName;

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }
}
