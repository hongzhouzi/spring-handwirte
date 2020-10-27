package pers.whz.spring.framework.webmvc.component;

import pers.whz.spring.framework.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理器适配器
 *
 * @author hongzhou.wei
 * @date 2020/10/26
 */
public class HandlerAdapter {

    public ModelAndView handler(HttpServletRequest req,
                                HttpServletResponse resp,
                                HandlerMapping handlerMapping) throws Exception {
        // 1、保存形参列表（参数名称和参数位置）
        Map<String, Integer> paramIndexMapping = new HashMap<>();
        Annotation[][] paramsAnnotation = handlerMapping.getMethod().getParameterAnnotations();
        for (int i = 0; i < paramsAnnotation.length; i++) {
            for (Annotation annotation : paramsAnnotation[i]) {
                if (annotation instanceof RequestParam) {
                    String paramName = ((RequestParam) annotation).value();
                    if (!"".equals(paramName.trim())) {
                        paramIndexMapping.put(paramName, i);
                    }
                }
            }
        }

        // 初始化参数类型 ？？？
        Class<?>[] paramTypes = handlerMapping.getMethod().getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> paramType = paramTypes[i];
            if (paramType == HttpServletRequest.class
                || paramType == HttpServletResponse.class) {
                paramIndexMapping.put(paramType.getName(), i);
            }
        }

        // 拼接实参列表
        Map<String, String[]> paramMap = req.getParameterMap();
        Object[] paramValues = new Object[paramTypes.length];

        for (Map.Entry<String, String[]> param : paramMap.entrySet()) {
            String val = Arrays.toString(paramMap.get(param.getKey()))
                .replaceAll("\\[|\\]", "")
                .replaceAll("\\s+", ",");
            if (paramIndexMapping.containsKey(param.getKey())) {
                int index = paramIndexMapping.get(param.getKey());
                paramValues[index] = castStringValue(val, paramTypes[index]);

            }
        }

        if (paramIndexMapping.containsKey(HttpServletRequest.class.getName())) {
            int index = paramIndexMapping.get(HttpServletRequest.class.getName());
            paramValues[index] = req;
        }

        if (paramIndexMapping.containsKey(HttpServletResponse.class.getName())) {
            int index = paramIndexMapping.get(HttpServletResponse.class.getName());
            paramValues[index] = resp;
        }

        Object result = handlerMapping.getMethod().invoke(handlerMapping.getController(), paramValues);
        if (result == null || result instanceof Void) {
            return null;
        }

        boolean isModelAndView = handlerMapping.getMethod().getReturnType() == ModelAndView.class;
        if (isModelAndView) {
            return (ModelAndView) result;
        }
        return null;

    }

    /**
     * 类型转换器
     *
     * @param value     参数值
     * @param paramType 参数类型
     * @return
     */
    private Object castStringValue(String value, Class<?> paramType) {
        if (String.class == paramType) {
            return value;
        } else if (Integer.class == paramType) {
            return Integer.valueOf(value);
        } else if (Double.class == paramType) {
            return Double.valueOf(value);
        } else {
            if (value != null) {
                return value;
            }
            return null;
        }
    }
}
