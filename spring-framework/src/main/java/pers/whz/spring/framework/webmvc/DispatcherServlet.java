package pers.whz.spring.framework.webmvc;

import pers.whz.spring.framework.annotation.Controller;
import pers.whz.spring.framework.annotation.RequestMapping;
import pers.whz.spring.framework.context.ApplicationContext;
import pers.whz.spring.framework.webmvc.component.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private ApplicationContext                  applicationContext;
    private List<HandlerMapping>                handlerMappings   = new ArrayList<>();
    private Map<HandlerMapping, HandlerAdapter> handlerAdapterMap = new HashMap<>();
    private List<ViewResolver>                  viewResolvers     = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 委派,根据URL去找到一个对应的Method并通过response返回
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            try {
                processDispatchResult(req, resp, new ModelAndView("500"));
            } catch (Exception e1) {
                e1.printStackTrace();
                resp.getWriter().write("500 Exception,Detail : " + Arrays.toString(e.getStackTrace()));
            }
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // 完成了对HandlerMapping的封装
        // 完成了对方法返回值的封装ModelAndView

        // 1、通过URL获得一个HandlerMapping
        HandlerMapping handler = getHandler(req);
        if (handler == null) {
            processDispatchResult(req, resp, new ModelAndView("404"));
            return;
        }

        // 2、根据一个HandlerMapping获得一个HandlerAdapter
        HandlerAdapter ha = getHandlerAdapter(handler);

        // 3、解析某一个方法的形参和返回值之后，统一封装为ModelAndView对象
        ModelAndView mv = ha.handler(req, resp, handler);

        // 就把ModelAndView变成一个ViewResolver
        processDispatchResult(req, resp, mv);

    }

    /**
     * 获取处理器适配器
     *
     * @param handler
     * @return
     */
    private HandlerAdapter getHandlerAdapter(HandlerMapping handler) {
        if (this.handlerAdapterMap.isEmpty()) {
            return null;
        }
        return this.handlerAdapterMap.get(handler);
    }

    /**
     * 处理分发结果
     *
     * @param req
     * @param resp
     * @param mv
     * @throws Exception
     */
    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, ModelAndView mv) throws Exception {
        if (null == mv) {
            return;
        }
        if (this.viewResolvers.isEmpty()) {
            return;
        }

        for (ViewResolver viewResolver : this.viewResolvers) {
            View view = viewResolver.resolveViewName(mv.getViewName());
            // 直接往浏览器输出
            view.render(mv.getModel(), req, resp);
            return;
        }
    }

    /**
     * 获取处理器
     *
     * @param req
     * @return
     */
    private HandlerMapping getHandler(HttpServletRequest req) {
        if (this.handlerMappings.isEmpty()) {
            return null;
        }
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replaceAll(contextPath, "").replaceAll("/+", "/");

        for (HandlerMapping mapping : handlerMappings) {
            Matcher matcher = mapping.getPattern().matcher(url);
            if (!matcher.matches()) {
                continue;
            }
            return mapping;
        }
        return null;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        // ==============IOC DI部分==============
        // 1、初始化Spring核心IoC容器，包括加载配置文件、扫描相关类、将类放在IOC容器中
        applicationContext = new ApplicationContext(config.getInitParameter("contextConfigLocation"));

        System.out.println(applicationContext);

        // 2、初始化SpringMVC中 9 大组件
        initStrategies(applicationContext);

    }

    /**
     * 初始化SpringMVC 9 大组件
     *
     * @param context {@link ApplicationContext}
     */
    private void initStrategies(ApplicationContext context) {
        // 多文件上传的组件
        initMultipartResolver(context);

        // 初始化本地语言环境
        initLocaleResolver(context);

        // 初始化模板处理器
        initThemeResolver(context);

        // ========== 初始化处理映射器 ==========
        initHandlerMappings(context);

        // ========== 初始化参数适配器 ==========
        initHandlerAdapters(context);

        // 初始化异常拦截器
        initHandlerExceptionResolvers(context);

        // 初始化视图预处理器
        initRequestToViewNameTranslator(context);

        // ========== 初始化视图转换器 ==========
        initViewResolvers(context);

        // FlashMap管理器
        initFlashMapManager(context);
    }


    /**
     * 初始化多文件上传的组件
     *
     * @param context
     */
    private void initMultipartResolver(ApplicationContext context) {
    }

    /**
     * 初始化本地语言环境
     *
     * @param context
     */
    private void initLocaleResolver(ApplicationContext context) {
    }

    /**
     * 初始化模板处理器
     *
     * @param context
     */
    private void initThemeResolver(ApplicationContext context) {
    }

    /**
     * 初始化处理映射器
     *
     * @param context
     */
    private void initHandlerMappings(ApplicationContext context) {
        if (context.getBeanDefinitionCount() == 0) {
            return;
        }

        // 获取 bean 相关信息，准备初始化 URL 和method的映射
        for (String beanName : context.getBeanDefinitionNames()) {
            Object instance = context.getBean(beanName);
            Class<?> clazz = instance.getClass();

            if (!clazz.isAnnotationPresent(Controller.class)) {
                continue;
            }

            String baseUrl = "";
            // 1、提取类上的RequestMapping注解
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                baseUrl = requestMapping.value();
            }

            // 2、提取 public 方法上的RequestMapping注解，并将提取的信息放在handlerMappings中
            for (Method method : clazz.getMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    String regex = ("/" + baseUrl + "/"
                        + requestMapping.value().replaceAll("\\*", ".*")
                    ).replaceAll("/+", "/");
                    Pattern pattern = Pattern.compile(regex);
                    handlerMappings.add(new HandlerMapping(pattern, method, instance));

                }
            }
        }
    }

    /**
     * 初始化参数适配器
     *
     * @param context
     */
    private void initHandlerAdapters(ApplicationContext context) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            this.handlerAdapterMap.put(handlerMapping, new HandlerAdapter());
        }
    }

    /**
     * 初始化异常拦截器
     *
     * @param context
     */
    private void initHandlerExceptionResolvers(ApplicationContext context) {
    }

    /**
     * 初始化视图预处理器
     *
     * @param context
     */
    private void initRequestToViewNameTranslator(ApplicationContext context) {
    }

    /**
     * 初始化视图转换器
     *
     * @param context
     */
    private void initViewResolvers(ApplicationContext context) {
        String templateRoot = context.getConfig().getProperty("templateRoot");
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();

        File templateRootDir = new File(templateRootPath);
        for (File file : templateRootDir.listFiles()) {
            this.viewResolvers.add(new ViewResolver(templateRoot));
        }
    }

    /**
     * FlashMap管理器
     *
     * @param context
     */
    private void initFlashMapManager(ApplicationContext context) {
    }
}
