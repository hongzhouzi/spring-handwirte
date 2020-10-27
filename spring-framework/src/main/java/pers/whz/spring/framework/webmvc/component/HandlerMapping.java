package pers.whz.spring.framework.webmvc.component;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 处理器映射-实体类
 *
 * @author hongzhou.wei
 * @date 2020/10/26
 */
public class HandlerMapping {
    /**
     * URL，用正则匹配
     */
    private Pattern pattern;
    /**
     * 对应的Method
     */
    private Method  method;
    /**
     * Method对应的实例对象
     */
    private Object  controller;

    public HandlerMapping(Pattern pattern, Method method, Object controller) {
        this.pattern = pattern;
        this.method = method;
        this.controller = controller;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HandlerMapping that = (HandlerMapping) o;
        return Objects.equals(pattern, that.pattern) &&
            Objects.equals(method, that.method) &&
            Objects.equals(controller, that.controller);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pattern, method, controller);
    }
}
