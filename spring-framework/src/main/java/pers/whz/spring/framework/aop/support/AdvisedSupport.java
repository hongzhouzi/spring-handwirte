package pers.whz.spring.framework.aop.support;

import pers.whz.spring.framework.aop.aspect.Advice;
import pers.whz.spring.framework.aop.config.AopConfig;
import pers.whz.spring.framework.utils.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解析AOP配置-工具类
 *
 * @author hongzhou.wei
 * @date 2020/10/28
 */
public class AdvisedSupport {
    private AopConfig config;
    private Object    target;
    private Class     targetClass;
    private Pattern   pointCutClassPattern;

    /**
     * 方法和通知，一个方法可以对应多个通知
     */
    private Map<Method, Map<String, Advice>> methodCache;

    public AdvisedSupport(AopConfig config) {
        this.config = config;
    }

    /**
     * 解析配置文件
     */
    private void parse() {
        // 将Spring的Express变成Java能处理的正则表达式
        String pointCut = config.getPointCut()
            .replaceAll("\\.", "\\\\.")
            .replaceAll("\\\\.\\*", ".*")
            .replaceAll("\\(", "\\\\(")
            .replaceAll("\\)", "\\\\)");

        // 正则匹配三段：方法的修饰符合返回值、类名、方法名称和形参列表

        // 匹配class的正则表达式
        String pointCutForClassRegex = pointCut.substring(0, pointCut.lastIndexOf("\\(") - 4);
        pointCutClassPattern = Pattern.compile("class " + pointCutForClassRegex.substring(pointCutForClassRegex.lastIndexOf(" ") + 1));

        // 享元的共享池
        methodCache = new HashMap<>();
        // 匹配方法的正则表达式
        Pattern pointCutPattern = Pattern.compile(pointCut);
        try {
            Class<?> aspectClass = Class.forName(this.config.getAspectClass());
            HashMap<String, Method> aspectMethods = new HashMap<>();
            for (Method method : aspectClass.getMethods()) {
                aspectMethods.put(method.getName(), method);
            }

            // 封装 advices
            for (Method method : targetClass.getMethods()) {
                // 暂时先匹配方法名，若方法上有异常抛出则将其去掉
                String methodStr = method.toString();
                if (methodStr.contains("throws")) {
                    methodStr = methodStr.substring(0, methodStr.lastIndexOf("throws")).trim();
                }

                Matcher matcher = pointCutPattern.matcher(methodStr);
                if (matcher.matches()) {
                    // 获取前置、后置、异常通知方法，【注意下面是将 aspectClass 的实例和对应方法填充进去】
                    Map<String, Advice> advices = new HashMap<>();
                    if (StringUtils.isNotBlank(config.getAspectBefore())) {
                        advices.put("before", new Advice(aspectClass.newInstance(), aspectMethods.get(config.getAspectBefore())));
                    }
                    if (StringUtils.isNotBlank(config.getAspectAfter())) {
                        advices.put("after", new Advice(aspectClass.newInstance(), aspectMethods.get(config.getAspectAfter())));
                    }
                    if (StringUtils.isNotBlank(config.getAspectAfterThrow())) {
                        advices.put("afterThrow", new Advice(aspectClass.newInstance(), aspectMethods.get(config.getAspectAfterThrow())));
                    }

                    // 与目标代理类的业务方法和Advices建立一对多个关联关系，以便在Proxy类中获得
                    methodCache.put(method, advices);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    /**
     * 设置目标类时解析配置文件
     */
    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
        parse();
    }

    /**
     * 在ApplicationContext中的对象初始化时调用，决定要不要生成代理类的逻辑
     */
    public boolean pointCutMatcher() {
        return pointCutClassPattern.matcher(this.targetClass.toString()).matches();
    }

    /**
     * 根据一个目标代理类的方法，获得其对应的通知
     */
    public Map<String, Advice> getAdvices(Method method, Object o) throws NoSuchMethodException {
        // 享元模式的应用
        Map<String, Advice> cache = methodCache.get(method);
        if (cache == null) {
            Method m = targetClass.getMethod(method.getName(), method.getParameterTypes());
            cache = methodCache.get(m);
            this.methodCache.put(m, cache);
        }
        return cache;
    }
}
