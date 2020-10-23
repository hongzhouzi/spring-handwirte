package pers.whz.spring.framework.webmvc;

import pers.whz.spring.framework.context.ApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 初始化时加载spring容器
 * 负责任务调度，请求分发【委派模式】
 *
 * @author hongzhou.wei
 * @date 2020/10/23
 */
public class DispatcherServlet extends HttpServlet {

    /**
     * 应用上下文，加载springIOC部分
     */
    private ApplicationContext applicationContext;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        // ==============IOC DI部分==============
        // 1、初始化Spring核心IoC容器，包括加载配置文件、扫描相关类、将类放在IOC容器中
        applicationContext = new ApplicationContext(config.getInitParameter("contextConfigLocation"));

        System.out.println(applicationContext);

    }

}
