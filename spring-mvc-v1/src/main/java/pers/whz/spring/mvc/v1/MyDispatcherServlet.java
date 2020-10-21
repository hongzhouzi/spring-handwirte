package pers.whz.spring.mvc.v1;

import pers.whz.spring.mvc.annotation.MyAutowired;
import pers.whz.spring.mvc.annotation.MyController;
import pers.whz.spring.mvc.annotation.MyRequestMapping;
import pers.whz.spring.mvc.annotation.MyService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * 自定义请求分发类
 *
 * @author hongzhou.wei
 * @date 2020/10/20
 */
public class MyDispatcherServlet extends HttpServlet {
    private Map<String, Object> mapping = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            resp.getWriter().write("500 Exception " + Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * 初始化所有的相关的类，IOC容器、servletBean
     *
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        InputStream is = null;
        try {
            // 读取配置文件
            Properties configContext = new Properties();
            is = this.getClass().getClassLoader().getResourceAsStream(config.getInitParameter("contextConfigLocation"));
            configContext.load(is);

            // 扫描包下面的类
            String scanPackage = configContext.getProperty("scanPackage");
            doScanner(scanPackage);

            // 读取类中的注解
            // 这儿用fori形式遍历，若用迭代器形式会在删除或增加map中的值时会出现ConcurrentModificationException 异常
            Object[] clazzNameSet =  mapping.keySet().toArray();
            for (int i = 0; i < clazzNameSet.length; i++) {
                String clazzName = (String) clazzNameSet[i];
                if (!clazzName.contains(".")) {
                    continue;
                }
                Class<?> clazz = Class.forName(clazzName);
                // 扫描 Controller 注解并实例化Controller类
                if (clazz.isAnnotationPresent(MyController.class)) {
                    mapping.put(clazzName, clazz.newInstance());
                    String baseUrl = "";
                    // 扫描类上的 RequestMapping 注解
                    if (clazz.isAnnotationPresent(MyRequestMapping.class)) {
                        MyRequestMapping requestMapping = clazz.getAnnotation(MyRequestMapping.class);
                        baseUrl = requestMapping.value();
                    }
                    // 扫描方法上的 RequestMapping 并将该URL和方法放入mapping中
                    Method[] methods = clazz.getMethods();
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(MyRequestMapping.class)) {
                            MyRequestMapping requestMapping = method.getAnnotation(MyRequestMapping.class);
                            String url = (baseUrl + "/" + requestMapping.value()).replaceAll("/+", "/");
                            mapping.put(url, method);
                        }
                    }
                }
                // 扫描service注解
                else if (clazz.isAnnotationPresent(MyService.class)) {
                    MyService service = clazz.getAnnotation(MyService.class);
                    String beanName = service.value();
                    if ("".equals(beanName)) {
                        beanName = clazz.getName();
                    }
                    Object instance = clazz.newInstance();
                    mapping.put(beanName, instance);
                    for (Class<?> itf : clazz.getInterfaces()) {
                        mapping.put(itf.getName(), instance);
                    }
                }
            }

            for (Object obj : mapping.values()) {
                if (obj == null) {
                    continue;
                }
                Class clazz = obj.getClass();
                if (clazz.isAnnotationPresent(MyController.class)) {
                    Field[] fields = clazz.getDeclaredFields();
                    for (Field fiedl : fields) {
                        // 处理 Autowired 注解并完成属性注入（先只考虑简单情况）
                        if (fiedl.isAnnotationPresent(MyAutowired.class)) {
                            MyAutowired autowired = fiedl.getAnnotation(MyAutowired.class);
                            String beanName = autowired.value();
                            if ("".equals(beanName)) {
                                beanName = fiedl.getType().getName();
                            }
                            fiedl.setAccessible(true);
                            fiedl.set(mapping.get(clazz.getName()), mapping.get(beanName));
                        }
                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 扫描包
     *
     * @param scanPackage
     */
    private void doScanner(String scanPackage) {
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        File classDir = new File(url.getFile());
        for (File file : classDir.listFiles()) {
            // 若为目录则继续向下扫描
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {
                if (file.getName().endsWith(".class")) {
                    String clazzName = scanPackage + "." + file.getName().replace(".class", "");
                    mapping.put(clazzName, null);
                }
            }
        }
    }

    /**
     * URL分发
     *
     * @param req
     * @param resp
     * @throws Exception
     */
    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // 处理URL
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");

        // 分配给对应 method 处理
        if (!mapping.containsKey(url)) {
            resp.getWriter().write("404 Not Found!!");
            return;
        }
        Method method = (Method) this.mapping.get(url);
        // 从req中拿到url传过来的参数
        Map<String, String[]> parameterMap = req.getParameterMap();
        // 暂时只处理URL中只有一个参数的情况
        method.invoke(mapping.get(method.getDeclaringClass().getName()), new Object[]{req, resp, parameterMap.get("name")[0]});
    }
}
