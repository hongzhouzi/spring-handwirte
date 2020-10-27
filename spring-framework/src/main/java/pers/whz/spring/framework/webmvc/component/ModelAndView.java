package pers.whz.spring.framework.webmvc.component;

import java.util.Map;

/**
 * 模型和视图-实体类
 *
 * @author hongzhou.wei
 * @date 2020/10/26
 */
public class ModelAndView {
    /**
     * 视图名
     */
    private String         viewName;
    /**
     * 模型
     */
    private Map<String, ?> model;

    public ModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public ModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, ?> getModel() {
        return model;
    }

    public void setModel(Map<String, ?> model) {
        this.model = model;
    }
}
