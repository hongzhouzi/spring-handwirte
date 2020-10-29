package pers.whz.spring.framework.context;

import pers.whz.spring.framework.annotation.Autowired;
import pers.whz.spring.framework.annotation.Controller;
import pers.whz.spring.framework.annotation.Service;
import pers.whz.spring.framework.aop.JdkDynamicAopProxy;
import pers.whz.spring.framework.aop.config.AopConfig;
import pers.whz.spring.framework.aop.support.AdvisedSupport;
import pers.whz.spring.framework.beans.BeanWrapper;
import pers.whz.spring.framework.beans.config.BeanDefinition;
import pers.whz.spring.framework.beans.supports.BeanDefinitionReader;
import sun.misc.ProxyGenerator;

import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 完成bean的创建以及依赖注入
 *
 * @author hongzhou.wei
 * @date 2020/10/23
 */
public class ApplicationContext {

    /**
     * 解析配置文件类
     */
    private BeanDefinitionReader        reader;
    private Map<String, BeanDefinition> beanDefinitionMap        = new ConcurrentHashMap<>();
    private Map<String, BeanWrapper>    factoryBeanInstanceCache = new ConcurrentHashMap<>();
    private Map<String, Object>         factoryBeanObjectCache   = new ConcurrentHashMap<>();


    public ApplicationContext(String... configLocations) {
        // 1、加载配置文件，可能有各种各样的配置文件，为了划分职责单独拎个类出来
        reader = new BeanDefinitionReader(configLocations);

        try {
            // 2、解析配置文件，统一封装成BeanDefinition
            List<BeanDefinition> beanDefinitions = reader.loadBeanDefinitions();

            // 3、把BeanDefinition缓存在当前类的容器中，方便后序进行DI操作
            doRegistryBeanDefinition(beanDefinitions);

            // 4、DI
            doAutowrited();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 3、把BeanDefinition缓存在当前类的容器中，方便后序进行DI操作
     *
     * @param beanDefinitions
     * @throws Exception
     */
    private void doRegistryBeanDefinition(List<BeanDefinition> beanDefinitions) throws Exception {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            if (this.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                throw new Exception("The " + beanDefinition.getFactoryBeanName() + " is exists");
            }
            beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
            beanDefinitionMap.put(beanDefinition.getBeanClassName(), beanDefinition);
        }
    }


    /**
     * 4、
     */
    private void doAutowrited() {
        Set<Map.Entry<String, BeanDefinition>> beanDefinitionEntries = this.beanDefinitionMap.entrySet();
        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : beanDefinitionEntries) {
            String beanName = beanDefinitionEntry.getKey();
            getBean(beanName);
        }
    }

    /**
     * 4-1、Bean实例化，DI开始的地方
     *
     * @param beanName
     * @return 封装在 {@link BeanWrapper}中的instance
     */
    public Object getBean(String beanName) {
        // 1、拿到BeanDefinition配置信息
        BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);

        // 2、反射实例化
        Object instance = instantiateBean(beanName, beanDefinition);

        // 3、将实例信息封装在BeanWrapper
        BeanWrapper beanWrapper = new BeanWrapper(instance);

        // 4、将beanWrapper保存在容器
        factoryBeanInstanceCache.put(beanName, beanWrapper);

        // 5、依赖注入
        populateBean(beanName, beanDefinition, beanWrapper);

        return beanWrapper.getWrapperInstance();
    }


    public Object getBean(Class beanClass) {
        return getBean(beanClass.getName());
    }

    /**
     * 4-1-1、通过反射实例化bean
     *
     * @param beanName
     * @param beanDefinition
     * @return
     */
    private Object instantiateBean(String beanName, BeanDefinition beanDefinition) {
        String className = beanDefinition.getBeanClassName();
        Object instance = null;
        try {
            if (this.factoryBeanObjectCache.containsKey(beanName)) {
                instance = this.factoryBeanObjectCache.get(beanName);
            } else {
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();

                // ========== AOP start ==========
                // 1、加载aop配置文件
                AdvisedSupport config = instantionAopConfig(beanDefinition);
                config.setTargetClass(clazz);
                config.setTarget(instance);

                // 2、判断该实例是否配置了代理
                // 用正则匹配，该类是否在aop配置中
                if(config.pointCutMatcher()){
                    instance = new JdkDynamicAopProxy(config).getProxy();

                    /*// 将临时代码输出到磁盘，可通过反编译tool查看到源码
                    byte[] bytes = ProxyGenerator.generateProxyClass("$Proxy5",
                        new Class[]{clazz});
                    try {
                        FileOutputStream fos = new FileOutputStream("C://$Proxy5.class");
                        fos.write(bytes);
                        fos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                }
                // ========== AOP end ============

                this.factoryBeanObjectCache.put(beanName, instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }

    /**
     * 4-1-2、依赖注入
     *
     * @param beanName
     * @param beanDefinition
     * @param beanWrapper
     */
    private void populateBean(String beanName, BeanDefinition beanDefinition, BeanWrapper beanWrapper) {

        Object instance = beanWrapper.getWrapperInstance();
        Class<?> clazz = beanWrapper.getWrappedClass();

        // 判断是否是spring中的组件，不是则不做处理
        if (!(clazz.isAnnotationPresent(Controller.class) || clazz.isAnnotationPresent(Service.class))) {
            return;
        }

        // 把所有的包括 private/protected/default/public 修饰字段都取出来
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                Autowired autowired = field.getAnnotation(Autowired.class);

                // 如果用户没有自定义的beanName，则默认根据类型注入
                String autowiredBeanName = autowired.value().trim();
                if ("".equals(autowiredBeanName)) {
                    autowiredBeanName = field.getType().getName();
                }

                // 暴力访问
                field.setAccessible(true);

                try {
                    // 这儿从实例化的bean缓存中取
                    if (this.factoryBeanInstanceCache.get(autowiredBeanName) != null) {
                        field.set(instance, this.factoryBeanInstanceCache.get(autowiredBeanName).getWrapperInstance());
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取已注册bean的数量
     *
     * @return
     */
    public int getBeanDefinitionCount() {
        return beanDefinitionMap.size();
    }

    /**
     * 获取已注册bean的beanName
     *
     * @return
     */
    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new String[getBeanDefinitionCount()]);
    }

    public Properties getConfig() {
        return this.reader.getConfig();
    }

    /**
     * 将aop配置文件实例化到类中
     *
     * @param beanDefinition
     * @return
     */
    private AdvisedSupport instantionAopConfig(BeanDefinition beanDefinition) {
        AopConfig config = new AopConfig();
        config.setPointCut(this.reader.getConfig().getProperty("pointCut"));
        config.setAspectClass(this.reader.getConfig().getProperty("aspectClass"));
        config.setAspectBefore(this.reader.getConfig().getProperty("aspectBefore"));
        config.setAspectAfter(this.reader.getConfig().getProperty("aspectAfter"));
        config.setAspectAfterThrow(this.reader.getConfig().getProperty("aspectAfterThrow"));
        config.setAspectAfterThrowingName(this.reader.getConfig().getProperty("aspectAfterThrowingName"));
        return new AdvisedSupport(config);
    }
}
