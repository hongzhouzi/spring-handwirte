package pers.whz.spring.framework.beans.supports;

import pers.whz.spring.framework.beans.config.BeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author hongzhou.wei
 * @date 2020/10/23
 */
public class BeanDefinitionReader {

    /**
     * 保存配置文件中的内容
     */
    private Properties contextConfig = new Properties();
    /**
     * 保存扫描的 .class 文件名
     */
    private List<String> registryBeanClasses = new ArrayList<>();

    public BeanDefinitionReader(String... configLocations) {
        // 1、加载配置文件
        for (String configLocation : configLocations) {
            doLoadConfig(configLocation);
        }

        // 2、扫描配置文件中的配置的相关的类
        doScanner(contextConfig.getProperty("scanPackage"));
    }



    /**
     * 1、加载配置文件
     *
     * @param contextConfigLocation
     */
    private void doLoadConfig(String contextConfigLocation) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation.replaceAll("classpath:",""));
        try {
            contextConfig.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null != is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 2、扫描配置文件中相关类
     *
     * @param scanPackage 配置的扫描目录
     */
    private void doScanner(String scanPackage) {
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.","/"));
        File classPath = new File(url.getFile());

        // 当成是一个ClassPath文件夹
        for (File file : classPath.listFiles()) {
            // 当前文件类型为文件夹，则递归往下扫描
            if(file.isDirectory()){
                doScanner(scanPackage + "." + file.getName());
            }else {
                // 当前文件类型是.class 文件，则添加到注册容器
                if(file.getName().endsWith(".class")){
                    // 全类名 = 包名.类名
                    String className = (scanPackage + "." + file.getName().replace(".class", ""));
                    registryBeanClasses.add(className);
                }
            }
        }
    }

    /**
     * 3、解析配置文件，将扫描到的.class文件相关信息以及配置的bean相关信息
     * 统一封装在BeanDefinition中，方便后序进行统一处理
     *
     * @return {@link List<BeanDefinition>}
     */
    public List<BeanDefinition> loadBeanDefinitions() {
        List<BeanDefinition> result = new ArrayList<>();

        try {
            for (String className : registryBeanClasses) {
                Class<?> beanClass = Class.forName(className);

                // 保存类对应的ClassName（全类名）、beanName
                // 1、默认是类名首字母小写
                result.add(doCreateBeanDefinition(toLowerFirstCase(beanClass.getSimpleName()), beanClass.getName()));
                // 2、自定义
                // 3、接口注入
                for (Class<?> i : beanClass.getInterfaces()) {
                    result.add(doCreateBeanDefinition(i.getName(),beanClass.getName()));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    public Properties getConfig(){
        return this.contextConfig;
    }

    /**
     * 3-1、创建beanDefinition对象
     *
     * @param beanName
     * @param beanClassName
     * @return
     */
    private BeanDefinition doCreateBeanDefinition(String beanName, String beanClassName) {
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setFactoryBeanName(beanName);
        beanDefinition.setBeanClassName(beanClassName);
        return beanDefinition;
    }

    private String toLowerFirstCase(String simpleName) {
        char [] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
