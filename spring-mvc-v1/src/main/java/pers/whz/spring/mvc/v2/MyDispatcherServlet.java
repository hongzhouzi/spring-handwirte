package pers.whz.spring.mvc.v2;

import pers.whz.spring.mvc.annotation.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * 在v1上优化了：
 * 1、封装方法
 * 2、URL中传多个参数
 *
 * @author hongzhou.wei
 * @date 2020/10/21
 */
public class MyDispatcherServlet extends HttpServlet {
    /**
     * 享元模式，缓存
     */
    private List<String>        classNames     = new ArrayList<>();
    /**
     * IoC容器，key默认是类名首字母小写，value就是对应的实例对象
     */
    private Map<String, Object> ioc            = new HashMap<>();
    /**
     * 存放URL和对应处理method
     */
    private Map<String, Method> handlerMapping = new HashMap<>();
    /**
     * 读取配置文件
     */
    private Properties          contextConfig  = new Properties();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 6、委派,根据URL去找到一个对应的Method并通过response返回
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("500 Exception,Detail : " + Arrays.toString(e.getStackTrace()));
        }

    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replaceAll(contextPath, "").replaceAll("/+", "/");

        if (!this.handlerMapping.containsKey(url)) {
            resp.getWriter().write("404 Not Found!!!");
            return;
        }

        Map<String, String[]> params = req.getParameterMap();

        Method method = this.handlerMapping.get(url);

        // 获取形参列表
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] paramValues = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            Class parameterType = parameterTypes[i];
            if (parameterType == HttpServletRequest.class) {
                paramValues[i] = req;
            } else if (parameterType == HttpServletResponse.class) {
                paramValues[i] = resp;
            } else if (parameterType == String.class) {
                // 通过运行时的状态去拿
                Annotation[][] pa = method.getParameterAnnotations();
                for (int j = 0; j < pa.length; j++) {
                    for (Annotation a : pa[i]) {
                        if (a instanceof MyRequestParam) {
                            String paramName = ((MyRequestParam) a).value();
                            if (!"".equals(paramName.trim())) {
                                String value = Arrays.toString(params.get(paramName))
                                    .replaceAll("\\[|\\]", "")
                                    .replaceAll("\\s+", ",");
                                paramValues[i] = value;
                            }
                        }
                    }
                }

            }
        }

        // 暂时硬编码
        String beanName = toLowerFirstCase(method.getDeclaringClass().getSimpleName());
        // 赋值实参列表
        method.invoke(ioc.get(beanName), paramValues);

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        // 1、加载配置文件
        doLoadConfig(config.getInitParameter("contextConfigLocation"));

        // 2、扫描相关的类
        doScanner(contextConfig.getProperty("scanPackage"));

        // ==============IoC部分==============
        // 3、初始化IoC容器，将扫描到的相关的类实例化，保存到IoC容器中
        doInstance();

        // AOP，新生成的代理对象

        // ==============DI部分==============
        // 4、完成依赖注入
        doAutowired();

        // ==============MVC部分==============
        // 5、初始化HandlerMapping
        doInitHandlerMapping();

        System.out.println("My Spring framework is init.");
    }

    private void doInitHandlerMapping() {
        if (ioc.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<?> clazz = entry.getValue().getClass();

            if (!clazz.isAnnotationPresent(MyController.class)) {
                continue;
            }

            // 提取 class上配置的url
            String baseUrl = "";
            if (clazz.isAnnotationPresent(MyRequestMapping.class)) {
                MyRequestMapping requestMapping = clazz.getAnnotation(MyRequestMapping.class);
                baseUrl = requestMapping.value();
            }

            // 只获取public的方法
            for (Method method : clazz.getMethods()) {
                if (!method.isAnnotationPresent(MyRequestMapping.class)) {
                    continue;
                }
                // 提取每个方法上面配置的url
                MyRequestMapping requestMapping = method.getAnnotation(MyRequestMapping.class);

                // 将URL中多个/替换成一个，如//demo////query -> /demo/query
                String url = ("/" + baseUrl + "/" + requestMapping.value()).replaceAll("/+", "/");
                handlerMapping.put(url, method);
                System.out.println("Mapped : " + url + "," + method);
            }

        }
    }

    private void doAutowired() {
        if (ioc.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : ioc.entrySet()) {

            // 把所有的包括private/protected/default/public 修饰字段都取出来
            for (Field field : entry.getValue().getClass().getDeclaredFields()) {
                if (!field.isAnnotationPresent(MyAutowired.class)) {
                    continue;
                }

                MyAutowired autowired = field.getAnnotation(MyAutowired.class);

                // 如果用户没有自定义的beanName，就默认根据类型注入
                String beanName = autowired.value().trim();
                if ("".equals(beanName)) {
                    // field.getType().getName() 获取字段的类型
                    beanName = field.getType().getName();
                }

                // 暴力访问
                field.setAccessible(true);

                try {
                    // ioc.get(beanName) 相当于通过接口的全名拿到接口的实现的实例
                    field.set(entry.getValue(), ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void doInstance() {
        if (classNames.isEmpty()) {
            return;
        }

        try {
            for (String className : classNames) {
                Class<?> clazz = Class.forName(className);

                if (clazz.isAnnotationPresent(MyController.class)) {
                    // key提取出来了，把value也搞出来
                    String beanName = toLowerFirstCase(clazz.getSimpleName());
                    Object instance = clazz.newInstance();
                    ioc.put(beanName, instance);
                } else if (clazz.isAnnotationPresent(MyService.class)) {
                    // 1、自定义命名-在多个包下出现相同的类名，只能自己起一个全局唯一的名字
                    String beanName = clazz.getAnnotation(MyService.class).value();
                    if ("".equals(beanName.trim())) {
                        beanName = toLowerFirstCase(clazz.getSimpleName());
                    }

                    // 2、默认的类名首字母小写
                    Object instance = clazz.newInstance();
                    ioc.put(beanName, instance);

                    //3、如果是接口
                    //判断有多少个实现类，如果只有一个，默认就选择这个实现类
                    //如果有多个，只能抛异常
                    for (Class<?> i : clazz.getInterfaces()) {
                        if (ioc.containsKey(i.getName())) {
                            throw new Exception("The " + i.getName() + " is exists!!");
                        }
                        ioc.put(i.getName(), instance);
                    }

                } else {
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //自己写，自己用
    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
//        if(chars[0] > )
        chars[0] += 32;
        return String.valueOf(chars);
    }

    private void doScanner(String scanPackage) {
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        File classPath = new File(url.getFile());

        // 当成是一个ClassPath文件夹
        for (File file : classPath.listFiles()) {
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                // 全类名 = 包名.类名
                String className = (scanPackage + "." + file.getName().replace(".class", ""));
                classNames.add(className);
            }
        }
    }

    private void doLoadConfig(String contextConfigLocation) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            contextConfig.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
