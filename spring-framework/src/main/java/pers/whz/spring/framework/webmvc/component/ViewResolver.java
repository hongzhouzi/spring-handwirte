package pers.whz.spring.framework.webmvc.component;

import java.io.File;

/**
 * 视图转换器，模板引擎
 *
 * @author hongzhou.wei
 * @date 2020/10/27
 */
public class ViewResolver {
    private final String DEFAULT_TEMPLATE_SUFFIX = ".html";
    private       File   tempateRootDir;

    public ViewResolver(String templateRoot) {
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        tempateRootDir = new File(templateRootPath);
    }

    public View resolveViewName(String viewName) {
        if (null == viewName || "".equals(viewName.trim())) {
            return null;
        }
        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX)
            ? viewName
            : (viewName + DEFAULT_TEMPLATE_SUFFIX);
        File templateFile = new File((tempateRootDir.getPath() + "/" + viewName)
            .replaceAll("/+", "/"));
        return new View(templateFile);
    }
}
