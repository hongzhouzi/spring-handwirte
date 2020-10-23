package pers.whz.spring.framework.beans;

/**
 * 保存扩展Bean信息的实体类，方便对Bean进行扩展
 *
 * @author hongzhou.wei
 * @date 2020/10/23
 */
public class BeanWrapper {
    private Object wrapperInstance;
    private Class<?> wrappedClass;

    public BeanWrapper(Object instance) {
        this.wrapperInstance = instance;
        this.wrappedClass = instance.getClass();
    }

    public Object getWrapperInstance() {
        return wrapperInstance;
    }

    public Class<?> getWrappedClass() {
        return wrappedClass;
    }
}
