# SpringMVC初体验

### Spring前身前世与思想

#### Spring的前世今生

##### Spring的设计初衷

> Spring是为解决企业级应用开发的复杂性而设计，她的使命是：“简化开发”。有许多框架在某些方面做了简化，而Spring则立志于全面的简化Java开发，还充当了粘合剂的作用，可以将许多优秀框架整合在一起使用。
>
> 4个关键策略：
>
> 1. 基于POJO的轻量级和最小入侵性编程
>
>    （如：一个类若要支持动态代理，原生JDK层面就必须实现一个接口作为条件，但Spring层面可以不要任何条件，用CGlib满足；类有复制操作，JDK层面就必须实现Cloneable接口，Spring层面就不需要任何条件，用原型模式满足）
>
> 2. 通过依赖注入和面向接口松耦合
>
>    （不要手动new对象）
>
> 3. 基于切面和惯性进行声明式编程 
>
>    声明式的代码增强，把公共操作全部放在切面；惯性声明式编程：更符合我们编程习惯的可见的自由配置编程，比如通过xml配置
>
> 4. 通过切面和模板减少样板式代码

##### BOP编程伊始

> Bean Oriented Programming（面向bean编程）Spring中所有都是bean（Java普通类），bean放在spring容器中，spring容器本身也是bean。
>
> 对bean的控制（bean创建、销毁、管理）
>
> BeanFactory接口（工厂模式实现）允许通过名称创建和检索对象，也可管理对象之间关系。它最底层支持两个对象模型。
>
> 1. 单例：提供了具有特定类名的全局共享实例对象，可以在查询时对其进行检索，它是默认的也是最常用的对象模型。
> 2. 原型：确保每次检索都会创建单独的实例对象。在每个用户都需要自己的对象时采用原型模式。

##### AOP编程理念

> 面向切面编程，核心是构造切面，将那些影响多个类且与具体业务无关的行为封装到可重用的模块中来。
>
> AOP编程常用的场景有：Authentication（权限认证）、Logging（日志记录）、Transactions（事务处理）、Auto Caching（自动缓存处理）、Error Handling（统一错误处理）、Debugging（调试信息输出）等。

#### Spring中的编程思想总总结

| Spring思想 | 应用场景（特点）                                 | 归纳        |
| -------- | ---------------------------------------- | --------- |
| OOP      | 面向对象编程(Object Oriented Programming)用程序归纳总结生活中的一切事物。 | 继承、封装、多态  |
| BOP      | 面向Bean编程(Bean Oriented Programming)面向bean(普通java类)设计程序 | 一切从bean开始 |
| AOP      | 面向切面编程(Aspect Oriented Programming)找出多个类中有一定规律的代码，开发时拆开，运行时再合并(动态代理) | 解耦，专人做专事  |
| IOC      | 控制反转(Inversion of Control)将new对象的动作交给spring管理，并由spring保存已创建的对象(IOC容器) | 转交控制权     |
| DI/DL    | 依赖注入(Dependency Injection)依赖查找(Dependency Lookup)Spring不仅保存自己创建的对象还保存对象之间的关系。注入即赋值，有三种方式：构造方法、set方法、直接赋值。 | 赋值        |
|          |                                          |           |

#### Spring 5系统架构

> 以support结尾的模块都是提供扩展支持的模块

##### 

#### Spring版本命名规则

##### 语义化版本命名通用规则

##### 商业软件中常见的修饰词

##### 软件版本号使用限定

##### Spring 版本命名规则



### SpringMVC的设计

#### 思考设计

##### 解决痛点

> 直接在servlet上编写Controller太繁琐了，每编写一个servlet类都需要在xml中配置、且servlet类继承servlet官方提供的类并重写里面的一些方法。随着业务越来越复杂servlet类也会越来越多、配置也会越来越臃肿，非常不好管理，后期维护就变得异常困难。

##### 项目需求

> 解决上面痛点，基于servlet开发将配置和开发中，主要注意集中处理请求、封装请求参数、返回内容。

##### 技术方案

> 1. 容器化技术，对所有servlet类进行统一管理
>
> 2. 请求分发，有请求来时，统一交给一个类将请求分发给对应的类处理
>
> 3. 封装参数，post：用反射技术拿到处理请求的方法对应的接收参数的类，然后将请求体中的参数按照类中属性设置进去。get：用正则处理，从url中取出参数和值。
>
>    整体思路：容器启动时扫描注解，注册请求URL和对应的处理方法，请求时根据请求的URL定位到具体的处理方法；处理类中通过注解中加上对应URL，在反射方式拿到注解URL下的方法然后用反射调用该处理方法。

#### 实现案例



### Spring5注解编程基础组件

#### 配置组件

> - @Configuration：把类作为一个IOC容器，它的某个方法头上个若注册了@Bean就会作为这个Spring。（注解中可指定value，includeFilter，useDefaultFilter）
> - @ComponentScan：在配置类上添加该注解，默认就会扫描该类所在的包下所有的配置类，相当于<<content:component-scan>>
> - @Scope：用于指定scope作用域（用在类上）
> - @Lazy：表示延迟初始化，单例bean才延迟加载，不配默认非延迟加载（容器启动时不加载，调用时才加载）
> - @Condition：spring4开始提供支持，按照一定条件进行判断，满足条件才将该bean注册进IOC容器。配合继承Condition类的带有判断条件的类使用
> - @Import：导入外部资源
> - 生命周期控制
>   - @PostConstruct：用于指定初始化方法（用在方法上）
>   - @PreDestroy：用于指定销毁方法（用在方法上）
>   - @DependsOn：定义bean初始化及销毁时的顺序

#### 赋值（自动装配）组件

> **给IOC容器注册bean的方式**
>
> 1. @Bean 直接导入单个类
>
> 2. @ComponentScan 默认扫描@Controller @Service @Repostory @Component
>
> 3. @Import 快速给容器导入bean。 @Import直接导入、或者实现ImportSelector自定义规则实现、或者实现ImportDefinitionRegistrar获得BeanDefinitionRegistry可以手动直接往IOC容器中塞
>
> 4. FactoryBean把需要注册的对象封装为FactoryBean。FactoryBean负责将bean注册到IOC的bean，BeanFactory从IOC容器中获得Bean对象。
>
>    
>
>    **对Bean生命周期的监控**
>
>    1. 配置@Bean的参数init 
>    2. 分别实现InitializingBean和DisposableBean接口
>    3. 使用@PostConstruct和@PreDestroy注解
>    4. 自定义一个类，实现BeanPostProcessor接口
>
>    
>
>    @Value：支持基本数据类型、Spring EL 表达式
>
>    ```java
>    // 基本数据类型
>    @Value("XXX")
>    // spring el表达式
>    @Value("#{8-5}")
>    // 从环境变量，配置文件中取值
>    Environment em = applicationContext.getEnvironment();
>    @Value("${env.field}")
>
>    ```
>
>    - @Component：泛指组件
>    - @Service：标注业务层组件
>    - @Controller：标注控制层组件
>    - @Repository：标注数据访问层组件
>    - @PropertySource：读取配置文件赋值
>    - @Value：普通数据类型赋值
>    - @Qualifier：给实例取名用，若存在多个实例基本都要用它
>    - @Autowired：默认按类型装配，若想按名称装配可以结合@Qualifier使用
>    - @Resources：默认按名称装配，找不到名称匹配的bean就按类型装配
>    - @Primary：自动装配时出现多个bean候选时，加了该注解的作为首选，否则将异常
>
>    
>
>    

# Spring手写实战

### 顶层设计IOC与DI

#### 思考设计

##### 解决痛点

> 以前项目中手动new对象，声明后直接new或者在构造方法中new。如果一个类在多个地方都被使用到就会被new出来多个。不好控制对象的生命周期

##### 项目需求

> 1. 对所有对象统一管理

##### 技术方案

> 1. 存放，将所有对象保存在一个地方，用map存放，需要该对象时直接从map中取。
> 2. 属性赋值，将扫描到的类依次实例化，有依赖关系的就将实例化的对象的引用赋值给它。（有个循环依赖不好解决）

#### 实现案例

第一章：从Servlet到ApplicationContext

> ApplicationContext 简单理解为工厂类；getBean() 从IOC容器中获取一个实例方法。
>
> 在调用Servlet init()时就要触发ApplicationContext
>
> Spring中调用DI由getBean触发，调用getBean先创建对象后触发DI。
>
> 1. 调用Servlet init()，创建ApplicationContext
> 2. 调用ApplicationContext中的getBean创建对象然后发生DI
>
>
>
> 1. 调用Servlet init()，创建ApplicationContext
>
> 2. 读取配置文件(properties|xm|yml…)，创建BeanDefinitionReader
>
> 3. 扫描相关类，**配置文件中信息和扫描到的类相关信息都封装成BeanDefinition**保存在内存中(getBean要用)。从ApplicationContext到BeanDefinition过程中有个BeanDefinitionReader(解析文件)用来从文件中读取信息并将信息封装在BeanDefinition，因为ApplicationContext只负责创建bean不负责读取文件。（扫描类(遍历文件夹和文件)时记录类名，之后根据类名用反射拿到类信息）
>
> 4. 初始化容器(ApplicationContext.getBean())，并实例化对象，考虑到能够对对象进行很好的扩展用到了装饰器模式，因为这个对象可能变成代理对象，又考虑到代理对象与原生对象有关联关系，用**BeanWrapper保存原生对象与未来创建的各种扩展出的对象之间的关联关系**。
>
> 5. 完成DI（循环依赖：用两个缓存，循环两次；1.把第一次读取结果为空的BeanDefinition存到一个缓存，2.等第一次循环之后，第2次循环再检查第一次的缓存再进行赋值）
>
>    @Controller和@Service都是@Component的类似子类关系

第二章：基础流程设计

2.2.1、application.properties配置

2.2.2、pom.xml配置

2.2.3、web.xml配置

2.2.4、GPDispatcherServlet实现

第三章：顶层结构设计（配置解析和IOC）

2.3.1、annotation（自定义配置）模块

2.3.2、beans（配置封装）模块

> - BeanDefinition
> - BeanWrapper

2.3.3、context（IOC容器）模块

> - ApplicationContext

第四章：完成DI依赖注入功能

2.4.1、从getBean()开始





### SpringMVC

> 核心处理流程：
>
> DispatcherServlet中调用init()作为入口，init()中调用initStategers()初始化九大组件。一个HandlerMapping对应一个HandlerAdapter，ViewResolver是根据ModeAndView来关联数据。
>
> 运行阶段：service()通过HandlerMapping拿到对应HandlerAdapter，再拿到ModeAndView，根据ModeAndView判断返回页面还是数据，若返回页面就启动模板引擎最后生成一个View，若返回数据就用response.getWrite().write()输出数据。View中的render()是渲染页面，读取模板中的文件内容，用正则替换占位符，最后通过response输出到浏览器。
>
> 
>
> **九大组件中部分：**
>
> - MultpartResolver：多文件上传组件
> - LocaleResolver：本地语言环境
> - ThemeResolver：主题模板处理器
> - HandlerMapping：根据url找到对应的处理方法’
>
> - HandlerAdapter：动态参数适配器
> - HandlerExceptionResolver：异常拦截器
> - RequestToViewNameTranslator：视图提取器，从request获取viewName
>
> - ViewResolvers：视图转换器，模板引擎
> - FlashMapManager：参数缓存器

SpringMVC核心组件执行流程

> 1. HandlerMapping：在处理映射关系时没有用map的数据结构存储，因为map只能存储URL和参数较简单的情况，稍微复杂些来个在URL路径中参数就需要用正则去匹配，或者带参数名的又是其他处理方式等等，用简单的map存储就不太合适。于是这儿用 List 存储 Handler 其中Handler中存储了匹配URL的正则表达式等。
> 2. HandlerAdapter：动态参数适配，通过handlerMapping找到对应处理方法后将请求传的参数动态绑定在handlerAdapter中。
> 3. ModelAndView：调用了业务处理方法后，将处理后的返回结果存储在ModelAndView中，方便进行下一步将数据交给视图处理器处理。
> 4. ViewResolver：处理视图数据
> 5. View：转换为视图显示



> 视频中第二段开始晕车，还需要再看看
>
> 将url中的正则转义成java中正则。



### AOP

> 底层用动态代理实现，可以对功能增强和代码解耦。
>
> 织入一些新的代码，生成一个新的类。要么跟目标类实现一个相同的接口，要么就是直接继承目标类（JDK），覆盖目标类的方法（CGLIB）。
>
> 真正调用代码逻辑时直接运行生成的代理类的代码。
>
> 
>
> Advice 通知，一个处理方法对应多个通知，Map<<Method, List<Advice>>>,
>
> IOC/AOP/DI/MVC的顺序
>
> 从ApplicationContext中进入，判断BeanWarper中的类是否要生成代理类。需要生成代理类就生成代理类(根据切面表达式判断)，并在实例化bean中将代理类实例化的对象作为实例对象。

核心组件执行流程：

> ApplicationContext  ->  AdvisedSupport  ->  AopConfig  ->  Advice  ->   JDKDynamicAopProxy  



# Spring核心原理源码分析

### IOC运行时序图

#### 思考设计

> **1、对象与对象之间的关系如何表示？**
>
> xml、yml、注解等
>
> **2、描述对象关系的文件存放在哪儿？**	
>
> classpath、network、filesystem、servletContext.
>
> **3、不同配置文件对对象的描述不一样（标准的、自定义声明式的的）如何统一？**
>
> 在内部需要有一个统一的关于对象的定义，所有外部描述都必须转成统一的描述定义。
>
> **4、如何对不同的配置文件进行解析？**
>
> 需要有不同的配置文件语法，解析不同文件时采用不同的解析器解析。
>
> 
>
>
> spring读取时先要找到文件在哪儿，再加载里面的内容。由于来源广泛且不同配置文件书写语法不一致，于是就定义 了一个配置文件标准，将配置文件中的信息统一解析放在BeanDefinition中，统一格式。
>
> 在解析配置文件时根据不同配置文件类型实施不同解析策略。
>
> ClassPathApplicationContext/AnnotationConfigApplicationContext/WebApplicationContext等解析策略。BeanDefinitionReader也有多种（XmlBeanDefinitionReader等）BeanDefinition层次也有多种封装策略（如：XmlBeanDefinition等 ），但最终都需封装成BeanDefinition。

#### 设计点关键

##### 1、BeanFactory接口（定义IOC容器规范）

> 作为顶层的一个接口类，它定义了IOC容器的基本功能规范，其中有三个非常重要的子类：ListableBeanFactory、HierarchicalBeanFactory、AutowireCapableBeanFactory。
>
> 为什么需要这么多层次的接口呢？主要是**区分在spring内部在操作过程中对象的传递和转化过程时对对象的数据访问所做的限制**。ListableBeanFactory 接口表示这些接口是可列表化的，而HierarchicalBeanFactory表示这些bean是有继承关系的，也就是每个bean有可能有父类bean。AutowireCapableBeanFactory定义bean的自动装配规则。这三个接口共同定义了bean的集合、bean之间的关系、bean的行为。
>
> ```java
> public interface BeanFactory {
> 
> 	/**
> 	 * Used to dereference a {@link FactoryBean} instance and distinguish it from
> 	 * beans <i>created</i> by the FactoryBean. For example, if the bean named
> 	 * {@code myJndiObject} is a FactoryBean, getting {@code &myJndiObject}
> 	 * will return the factory, not the instance returned by the factory.
> 	 */
> 	//对FactoryBean的转义定义，因为如果使用bean的名字检索FactoryBean得到的对象是工厂生成的对象，
> 	//如果需要得到工厂本身，需要转义
> 	String FACTORY_BEAN_PREFIX = "&";
> 
> 	//根据bean的名字，获取在IOC容器中得到bean实例
> 	Object getBean(String name) throws BeansException;
> 
> 	//根据bean的名字和Class类型来得到bean实例，增加了类型安全验证机制。
> 	<T> T getBean(String name, @Nullable Class<T> requiredType) throws BeansException;
> 	Object getBean(String name, Object... args) throws BeansException;
> 	<T> T getBean(Class<T> requiredType) throws BeansException;
> 	<T> T getBean(Class<T> requiredType, Object... args) throws BeansException;
> 
> 	//提供对bean的检索，看看是否在IOC容器有这个名字的bean
> 	boolean containsBean(String name);
> 
> 	/**
> 	 * Is this bean a shared singleton? That is, will {@link #getBean} always
> 	 * return the same instance?
> 	 * <p>Note: This method returning {@code false} does not clearly indicate
> 	 * independent instances. It indicates non-singleton instances, which may correspond
> 	 * to a scoped bean as well. Use the {@link #isPrototype} operation to explicitly
> 	 * check for independent instances.
> 	 * <p>Translates aliases back to the corresponding canonical bean name.
> 	 * Will ask the parent factory if the bean cannot be found in this factory instance.
> 	 * @param name the name of the bean to query
> 	 * @return whether this bean corresponds to a singleton instance
> 	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
> 	 * @see #getBean
> 	 * @see #isPrototype
> 	 */
> 	//根据bean名字得到bean实例，并同时判断这个bean是不是单例
> 	boolean isSingleton(String name) throws NoSuchBeanDefinitionException;
> 	boolean isPrototype(String name) throws NoSuchBeanDefinitionException;
> 	boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException;
> 	boolean isTypeMatch(String name, @Nullable Class<?> typeToMatch) throws NoSuchBeanDefinitionException;
> 
> 	//得到bean实例的Class类型
> 	@Nullable
> 	Class<?> getType(String name) throws NoSuchBeanDefinitionException;
> 
> 	//得到bean的别名，如果根据别名检索，那么其原名也会被检索出来
> 	String[] getAliases(String name);
> }
> ```
>
> 



##### 2、BeanDefinition（定义对象描述）

> 装配bean时可以有多种方式（如：xml配置、注解配置等），它用于将各种配置方式转化成统一的描述定义。

##### 3、BeanDefinitionReader（读取bean配置信息）

> 在读取Bean信息时解析的过程非常复杂，功能被分得很细，需要被扩展的地方非常多。



#### 容器初始化过程

**IOC容器初始化三部曲**

> 1. **定位：**定位配置文件和扫描相关注解，容器通过BeanDefinitionReader来完成定义信息的解析和Bean信息的注册（如使用XmlBeanDefinitionReader来解析Bean的xml定义文件，但实际处理是委托给BeanDefinitionParserDelegate来完成，从而得到bean的定义信息），这些信息在spring中使用BeanDefinition对象来表示。
>
> 2. **加载：**通过ResourceLoader来完成资源文件位置的定位，DefaultResourceLoader是默认实现，同时上下文本身就给出了ResourceLoader的实现，可以从类路径、文件系统、URL等方式来定位资源位置。
>
> 3. **注册：**由BeanDefinitionRegistry接口来实现，注册过程就是在IOC容器中用hashmap来保存BeanDefinition的过程。

**大致步骤**

> 1. 初始化入口在容器中实现的refresh()调用来完成。
> 2. 对bean定义载入IOC容器使用的方法是loadBeanDefinition()



#### 容器初始化实例

##### 基于Web的IOC 

init()

> 从DispatchServlet的init()开始，但是发现init()在父类HttpServletBean中重写的init()

initServletBean()

> 真正完成初始化容器的动作

initWebApplicationContext()

> 可以看到在 configAndRefreshWebApplicationContext()中调用了refresh()，这个就是IOC容器真正的入口。IOC容器初始化后调用了DispatchServlet中的onRefresh()，在onRefresh()中调用了initStrategies()初始化SpringMVC 9大组件





##### 基于XML的IOC

> ApplicationContext允许上下文嵌套，通过保持父上下文可以维持一个上下文体系。对于bean的查找可以在这个上下文体系中发生，首先检查当前上下文，其次是父上下文，逐级向上，这样为不同的Spring应用提供了一个共享的bean定义环境。

###### 1、寻找入口

```java
ApplicationContext app = new ClassPathXmlApplicationContext("application.xml"); 
```

> 于此类似的还有 AnnotationConfigApplicationContext、FileSystemXmlApplicationContext、XMLWebApplicationContext等都继承与父容器AbstractApplicationContext主要用到了装饰器模式和策略模式，最终都是调用的refresh()

**构造函数调用**

```java
public ClassPathXmlApplicationContext(String configLocation) throws BeansException {
    this(new String[] {configLocation}, true, null);
}

public ClassPathXmlApplicationContext(
    String[] configLocations, boolean refresh, @Nullable ApplicationContext parent)
    throws BeansException {
	// 调用父类构造方法为容器设置好spring资源加载器
    super(parent);
    setConfigLocations(configLocations);
    if (refresh) {
        // 重启、刷新、重置
        refresh();
    }
}
```



###### 2、设置配置路径

```java
// 解析Bean定义资源文件的路径，处理多个资源文件字符串数组
public void setConfigLocations(@Nullable String... locations) {
   if (locations != null) {
      Assert.noNullElements(locations, "Config locations must not be null");
      this.configLocations = new String[locations.length];
      for (int i = 0; i < locations.length; i++) {
         // resolvePath为同一个类中将字符串解析为路径的方法
         this.configLocations[i] = resolvePath(locations[i]).trim();
      }
   }
   else {
      this.configLocations = null;
   }
}
```



###### 3、开始启动

> spring对bean配置资源的载入是从refresh()开始的，refresh()是一个模板方法，规定了IOC容器的启动流程，有些逻辑交给子类去实现的。这儿ClassPathXmlApplicationContext通过调用父类AbstractApplicationContext#refresh()启动了IOC容器对bean的载入过程。

```java
public void refresh() throws BeansException, IllegalStateException {
   synchronized (this.startupShutdownMonitor) {
      // Prepare this context for refreshing.
      // 1、调用容器准备刷新的方法，获取容器的当时时间，同时给容器设置同步标识
      prepareRefresh();

      // Tell the subclass to refresh the internal bean factory.
      // ==================== 载入配置信息 ====================
      // 2、告诉子类启动refreshBeanFactory()方法，
      // Bean定义资源文件的载入从子类的refreshBeanFactory()方法启动
      ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

      // Prepare the bean factory for use in this context.
      // 3、为BeanFactory配置容器特性，例如类加载器、事件处理器等
      prepareBeanFactory(beanFactory);

      try {
         // Allows post-processing of the bean factory in context subclasses.
         // 4、为容器的某些子类指定特殊的BeanPost事件处理器
         postProcessBeanFactory(beanFactory);

         // Invoke factory processors registered as beans in the context.
         // 5、调用所有注册的BeanFactoryPostProcessor的Bean
         invokeBeanFactoryPostProcessors(beanFactory);

         // Register bean processors that intercept bean creation.
         // 6、为BeanFactory注册BeanPost事件处理器.
         // BeanPostProcessor是Bean后置处理器，用于监听容器触发的事件
         registerBeanPostProcessors(beanFactory);

         // Initialize message source for this context.
         // 7、初始化信息源，和国际化相关.
         initMessageSource();

         // Initialize event multicaster for this context.
         // 8、初始化容器事件传播器.
         initApplicationEventMulticaster();

         // Initialize other special beans in specific context subclasses.
         // 9、调用子类的某些特殊Bean初始化方法
         onRefresh();

         // Check for listener beans and register them.
         // 10、为事件传播器注册事件监听器.
         registerListeners();

         // Instantiate all remaining (non-lazy-init) singletons.
         // 11、初始化所有剩余的单例Bean
         finishBeanFactoryInitialization(beanFactory);

         // Last step: publish corresponding event.
         // 12、初始化容器的生命周期事件处理器，并发布容器的生命周期事件
         finishRefresh();
      } catch (BeansException ex) {
         // Destroy already created singletons to avoid dangling resources.
         // 13、销毁已创建的Bean
         destroyBeans();

         // Reset 'active' flag.
         // 14、取消refresh操作，重置容器的同步标识。
         cancelRefresh(ex);

         // Propagate exception to caller.
         throw ex;
      } finally {
         // Reset common introspection caches in Spring's core, since we
         // might not ever need metadata for singleton beans anymore...
         // 15、重设公共缓存
         resetCommonCaches();
      }
   }
}
```



###### 4、创建容器

```java
// AbstractApplicationContext#obtainFreshBeanFactory
protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
   // 使用了【委派模式】，父类定义了抽象的refreshBeanFactory()方法，
   // 具体实现调用子类容器的refreshBeanFactory()方法
   refreshBeanFactory();
   ConfigurableListableBeanFactory beanFactory = getBeanFactory();
   return beanFactory;
}
```

> 注意：下面是调用的子类AbstractRefreshableApplicationContext中的refreshBeanFactory
>
> 功能：若有容器则销毁后重新创建（操作BeanFactory），然后装载bean定义。
>
> 疑问：为什么需要销毁后再创建，不能用之前的容器吗，为什么？
```java
// AbstractRefreshableApplicationContext#refreshBeanFactory
protected final void refreshBeanFactory() throws BeansException {
   // 若已经有容器，则销毁容器中的bean，关闭容器
   if (hasBeanFactory()) {
      destroyBeans();
      closeBeanFactory();	
   }
   try {
      // 创建IOC容器
      DefaultListableBeanFactory beanFactory = createBeanFactory();
      beanFactory.setSerializationId(getId());
      // 对IOC容器进行定制化，如设置启动参数，开启注解的自动装配等
      customizeBeanFactory(beanFactory);
      // 调用载入Bean定义的方法，又使用了【委派模式】，
      // 在当前类中只定义了抽象的loadBeanDefinitions方法，具体的实现调用子类容器
      loadBeanDefinitions(beanFactory);
      synchronized (this.beanFactoryMonitor) {
         this.beanFactory = beanFactory;
      }
   }
   catch (IOException ex) {
      throw new ApplicationContextException("I/O error parsing bean definition source for " + getDisplayName(), ex);
   }
}
```



###### 5、载入配置路径

> 准备用XmlBeanDefinitionReader解析xml，设置xml解析器相关信息

```java
// AbstractXmlApplicationContext#loadBeanDefinitions(DefaultListableBeanFactory)
protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
   // Create a new XmlBeanDefinitionReader for the given BeanFactory.
   // 创建XmlBeanDefinitionReader，即创建Bean读取器，并通过回调设置到容器中去，容  器使用该读取器读取Bean定义资源
   XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);

   // Configure the bean definition reader with this context's
   // resource loading environment.
   // 为Bean读取器设置Spring资源加载器，AbstractXmlApplicationContext的
   // 祖先父类AbstractApplicationContext继承DefaultResourceLoader，因此，容器本身也是一个资源加载器
   beanDefinitionReader.setEnvironment(this.getEnvironment());
   // 【注意】这儿设置的this(当前对象的引用)，当前对象是DefaultResourceLoader的子类，后面在AbstractBeanDefinitionReader#loadBeanDefinitions(String, Set<Resource>) 中调用getResource(String)时实际就是调用的DefaultResourceLoader中的方法
   beanDefinitionReader.setResourceLoader(this);
   // 为Bean读取器设置SAX xml解析器（ResourceEntityResolver继承自SAX xml解析器的实体）
   beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));

   // Allow a subclass to provide custom initialization of the reader,
   // then proceed with actually loading the bean definitions.
   // 当Bean读取器读取Bean定义的Xml资源文件时，启用Xml的校验机制
   initBeanDefinitionReader(beanDefinitionReader);
   // Bean读取器真正实现加载的方法
   loadBeanDefinitions(beanDefinitionReader);
}
```



###### 6、分配路径处理策略

> 获取到要加载的资源（获取Resource和configLocations），根据配置信息为其分配读取该配置的策略，情况比较复杂比较绕。

```java
// ================ AbstractXmlApplicationContext ================ 
// Xml Bean读取器加载Bean定义资源
protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws BeansException, IOException {
   // 获取Bean定义资源的定位
   Resource[] configResources = getConfigResources();
   if (configResources != null) {
      // Xml Bean读取器【调用其父类AbstractBeanDefinitionReader】读取定位的Bean定义资源
      reader.loadBeanDefinitions(configResources);
   }
   // 若子类中获取的Bean定义资源定位为空，则获取FileSystemXmlApplicationContext构造方法中setConfigLocations方法设置的资源
   String[] configLocations = getConfigLocations();
   if (configLocations != null) {
      // Xml Bean读取器【调用其父类AbstractBeanDefinitionReader】读取定位的Bean定义资源
      reader.loadBeanDefinitions(configLocations);
   }
}

// ================ AbstractBeanDefinitionReader ================
// 重载方法，调用loadBeanDefinitions(String);
@Override
public int loadBeanDefinitions(String... locations) throws BeanDefinitionStoreException {
   Assert.notNull(locations, "Location array must not be null");
   int counter = 0;
   for (String location : locations) {
      counter += loadBeanDefinitions(location);
   }
   return counter;
}

// 重载方法，调用下面的loadBeanDefinitions(String, Set<Resource>);方法
@Override
public int loadBeanDefinitions(String location) throws BeanDefinitionStoreException {
    return loadBeanDefinitions(location, null);
}

public int loadBeanDefinitions(String location, @Nullable Set<Resource> actualResources) throws BeanDefinitionStoreException {
    // 获取在IOC容器初始化过程中设置的资源加载器
    ResourceLoader resourceLoader = getResourceLoader();
    if (resourceLoader == null) {
        throw new BeanDefinitionStoreException("……");
    }

    if (resourceLoader instanceof ResourcePatternResolver) {
        // Resource pattern matching available.
        try {
            // 将指定位置的Bean定义资源文件解析为Spring IOC容器封装的资源
            // 【7、解析配置文件，加载多个指定位置的Bean定义资源文件】 ==== getResources(location) ======在AbstractXmlApplicationContext#loadBeanDefinitions(DefaultListableBeanFactory)设置的resourceLoader，此处调用的DefaultResourceLoader#getResource(String)
            Resource[] resources = ((ResourcePatternResolver) resourceLoader).getResources(location);
            // 【8、读取配置文件】委派调用其子类XmlBeanDefinitionReader的方法，实现加载功能
            int loadCount = loadBeanDefinitions(resources);
            if (actualResources != null) {
                for (Resource resource : resources) {
                    actualResources.add(resource);
                }
            }
            return loadCount;
        }
        catch (IOException ex) {
            throw new BeanDefinitionStoreException(
                "Could not resolve bean definition resource pattern [" + location + "]", ex);
        }
    }
    else {
        // Can only load single resources by absolute URL.
        // 将指定位置的Bean定义资源文件解析为Spring IOC容器封装的资源
        // 加载单个指定位置的Bean定义资源文件
        Resource resource = resourceLoader.getResource(location);
        // 【委派调用其子类XmlBeanDefinitionReader的方法】，实现加载功能
        int loadCount = loadBeanDefinitions(resource);
        if (actualResources != null) {
            actualResources.add(resource);
        }
        return loadCount;
    }
}
```



###### 7、解析配置文件路径

> 加载资源时用getResource()获取要加载的资源
>
> 加载多个指定位置的Bean定义资源文件，在AbstractXmlApplicationContext#loadBeanDefinitions(DefaultListableBeanFactory)设置的resourceLoader，但因为AbstractXmlApplicationContext是DefaultResourceLoader的子类，所以此处调用的DefaultResourceLoader的中的getResource(String)

```java
public Resource getResource(String location) {
    Assert.notNull(location, "Location must not be null");

    for (ProtocolResolver protocolResolver : this.protocolResolvers) {
        Resource resource = protocolResolver.resolve(location, this);
        if (resource != null) {
            return resource;
        }
    }
    // 若是类路径的方式，则使用ClassPathResource 来得到bean文件的资源对象
    if (location.startsWith("/")) {
        return getResourceByPath(location);
    }
    else if (location.startsWith(CLASSPATH_URL_PREFIX)) {
        return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()), getClassLoader());
    }
    else {
        try {
            // Try to parse the location as a URL...
            // 若是URL 方式，使用UrlResource 作为bean 文件的资源对象
            URL url = new URL(location);
            return (ResourceUtils.isFileURL(url) ? new FileUrlResource(url) : new UrlResource(url));
        }
        catch (MalformedURLException ex) {
            // No URL -> resolve as resource path.
            // 若既不是classpath标识，又不是URL标识的Resource定位则调用
            // 容器本身的getResourceByPath方法获取Resource
            return getResourceByPath(location);
        }
    }
}
```

> DefaultResourceLoader中提供了getResourceByPath()的实现，是为了处理既不是classpath标识也不是URL标识的Resource定位的情况。
>
> ```java
> protected Resource getResourceByPath(String path) {
> return new ClassPathContextResource(path, getClassLoader());
> }
> ```
> 比如FileSystemXmlApplication容器就重写了getResourceByPath()，通过子类的覆盖巧妙完成了将类路径变成文件路径的转换。
>
> ```java
> // FileSystemResourceLoader#getResourceByPath
> protected Resource getResourceByPath(String path) {
>     if (path.startsWith("/")) {
>         path = path.substring(1);
>     }
>     // 这里使用文件系统资源对象来定义bean 文件
>     return new FileSystemResource(path);
> }
> ```



###### 8、读取配置文件内容

> 经过上面步骤解析到配置文件内容后，这步就准备读取配置文件信息了。
>
> 此步骤主要是载入bean配置信息，然后将其转换成流，准备进一步将其转为Document对象。

```java
// ============== AbstractBeanDefinitionReader ================
@Override
public int loadBeanDefinitions(Resource... resources) throws BeanDefinitionStoreException {
    Assert.notNull(resources, "Resource array must not be null");
    int counter = 0;
    for (Resource resource : resources) {
        counter += loadBeanDefinitions(resource);
    }
    return counter;
}

// ================= XmlBeanDefinitionReader ===================
// XmlBeanDefinitionReader加载资源的入口方法
@Override
public int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException {
    // 将读入的XML资源进行特殊编码处理
    return loadBeanDefinitions(new EncodedResource(resource));
}

// 这里是载入XML形式Bean定义资源文件方法
public int loadBeanDefinitions(EncodedResource encodedResource) throws BeanDefinitionStoreException {
    Assert.notNull(encodedResource, "EncodedResource must not be null");

    Set<EncodedResource> currentResources = this.resourcesCurrentlyBeingLoaded.get();
    if (currentResources == null) {
        currentResources = new HashSet<>(4);
        this.resourcesCurrentlyBeingLoaded.set(currentResources);
    }
    if (!currentResources.add(encodedResource)) {
        throw new BeanDefinitionStoreException("……");
    }
    try {
        // 将资源文件转为InputStream的IO流
        InputStream inputStream = encodedResource.getResource().getInputStream();
        try {
            // 从InputStream中得到XML的解析源
            InputSource inputSource = new InputSource(inputStream);
            if (encodedResource.getEncoding() != null) {
                inputSource.setEncoding(encodedResource.getEncoding());
            }
            // 这里是具体的读取过程
            return doLoadBeanDefinitions(inputSource, encodedResource.getResource());
        }
        finally {
            // 关闭从Resource中得到的IO流
            inputStream.close();
        }
    }
    catch (IOException ex) {
        throw new BeanDefinitionStoreException("……");
    }
    finally {
        currentResources.remove(encodedResource);
        if (currentResources.isEmpty()) {
            this.resourcesCurrentlyBeingLoaded.remove();
        }
    }
}

// 从特定XML文件中实际载入Bean定义资源的方法
protected int doLoadBeanDefinitions(InputSource inputSource, Resource resource)
    throws BeanDefinitionStoreException {
    try {
        // 将XML文件转换为DOM对象，解析过程由documentLoader实现
        Document doc = doLoadDocument(inputSource, resource);
        // 这里是启动对Bean定义解析的详细过程，该解析过程会用到Spring的Bean配置规则
        return registerBeanDefinitions(doc, resource);
    }
    // ……
}
```



###### 9、准备文档

> 将配置信息的流转成文档对象

```java
// ================= XmlBeanDefinitionReader ===================
protected Document doLoadDocument(InputSource inputSource, Resource resource) throws Exception {
    return this.documentLoader.loadDocument(inputSource, getEntityResolver(), this.errorHandler,
                                            getValidationModeForResource(resource), isNamespaceAware());
}

// ================= DefaultDocumentLoader ===================
// 使用标准的JAXP将载入的Bean定义资源转换成document对象
@Override
public Document loadDocument(InputSource inputSource, EntityResolver entityResolver,
                             ErrorHandler errorHandler, int validationMode, boolean namespaceAware) throws Exception {

    // 创建文件解析器工厂
    DocumentBuilderFactory factory = createDocumentBuilderFactory(validationMode, namespaceAware);
    if (logger.isDebugEnabled()) {
        logger.debug("Using JAXP provider [" + factory.getClass().getName() + "]");
    }
    // 创建文档解析器
    DocumentBuilder builder = createDocumentBuilder(factory, entityResolver, errorHandler);
    // 解析Spring的Bean定义资源
    return builder.parse(inputSource);
}

protected DocumentBuilderFactory createDocumentBuilderFactory(int validationMode, boolean namespaceAware)
    throws ParserConfigurationException {

    // 创建文档解析工厂
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(namespaceAware);

    // 设置解析XML的校验
    if (validationMode != XmlValidationModeDetector.VALIDATION_NONE) {
        factory.setValidating(true);
        if (validationMode == XmlValidationModeDetector.VALIDATION_XSD) {
            // Enforce namespace aware for XSD...
            factory.setNamespaceAware(true);
            try {
                factory.setAttribute(SCHEMA_LANGUAGE_ATTRIBUTE, XSD_SCHEMA_LANGUAGE);
            }
            catch (IllegalArgumentException ex) {
                ParserConfigurationException pcex = new ParserConfigurationException("……");
                pcex.initCause(ex);
                throw pcex;
            }
        }
    }

    return factory;
}
```

> 此解析过程调用javaEE标准的JAXP标准进行处理。至此Sprig IOC容器根据定位的bean配置信息，将其加载并转换为Document对象过程完成。



###### 10、分配解析策略

> bean配置资源的载入解析分为两个过程：
>
> 1. 调用xml解析器将bean配置信息转换到document对象
> 2. 按照spring bean的定义规则对document对象进行解析，其解析过程在BeanDefinitionDocumentReader接口的实现类DefaultBeanDefinitionDocumentReader中实现。

```java
// ============ XmlBeanDefinitionReader ============
// 按照Spring的Bean语义要求将Bean定义资源解析并转换为容器内部数据结构
public int registerBeanDefinitions(Document doc, Resource resource) throws BeanDefinitionStoreException {
// 得到BeanDefinitionDocumentReader来对xml格式的BeanDefinition解析
    BeanDefinitionDocumentReader documentReader = createBeanDefinitionDocumentReader();
	// 获得容器中注册的Bean数量
    int countBefore = getRegistry().getBeanDefinitionCount();
    // 解析过程入口，使用了【委派模式】，BeanDefinitionDocumentReader只是接口,具体解析实现过程由实现类DefaultBeanDefinitionDocumentReader完成
    documentReader.registerBeanDefinitions(doc, createReaderContext(resource));
    // 统计解析的Bean数量
    return getRegistry().getBeanDefinitionCount() - countBefore;
}
```



###### 11、将配置载入内存

> BeanDefinitionDocumentReader通过registerBeanDefinitions()调用其实现类DefaultBeanDefinitionDocumentReader对Document对象进行解析

```java
// ============ DefaultBeanDefinitionDocumentReader ============
/**
 * 根据Spring DTD对Bean的定义规则解析Bean定义Document对象
 */
@Override
public void registerBeanDefinitions(Document doc, XmlReaderContext readerContext) {
    // 获得XML描述符
    this.readerContext = readerContext;
    logger.debug("Loading bean definitions");
    // 获得Document的根元素
    Element root = doc.getDocumentElement();
    doRegisterBeanDefinitions(root);
}

/**
 * 解析<beans/>根节点下的所有bean
 */
protected void doRegisterBeanDefinitions(Element root) {
    // Any nested <beans> elements will cause recursion in this method. In
    // order to propagate and preserve <beans> default-* attributes correctly,
    // keep track of the current (parent) delegate, which may be null. Create
    // the new (child) delegate with a reference to the parent for fallback purposes,
    // then ultimately reset this.delegate back to its original (parent) reference.
    // this behavior emulates a stack of delegates without actually necessitating one.

    // 具体的解析过程由BeanDefinitionParserDelegate实现，
    // BeanDefinitionParserDelegate中定义了Spring Bean定义XML文件的各种元素
    BeanDefinitionParserDelegate parent = this.delegate;
    this.delegate = createDelegate(getReaderContext(), root, parent);
    // ……

    // 在解析Bean定义之前，进行自定义的解析，增强解析过程的可扩展性
    preProcessXml(root);
    // 从Document的根元素开始进行Bean定义的Document对象
    parseBeanDefinitions(root, this.delegate);
    // 在解析Bean定义之后，进行自定义的解析，增加解析过程的可扩展性
    postProcessXml(root);

    this.delegate = parent;
}

/**
 * 创建BeanDefinitionParserDelegate，用于完成真正的解析过程
 */
protected BeanDefinitionParserDelegate createDelegate(
    XmlReaderContext readerContext, Element root, @Nullable BeanDefinitionParserDelegate parentDelegate) {

    BeanDefinitionParserDelegate delegate = new BeanDefinitionParserDelegate(readerContext);
    // BeanDefinitionParserDelegate初始化Document根元素
    delegate.initDefaults(root, parentDelegate);
    return delegate;
}

/**
 * 使用Spring的Bean规则从Document的根元素开始进行Bean定义的Document对象
 */
protected void parseBeanDefinitions(Element root, BeanDefinitionParserDelegate delegate) {
    // Bean定义的Document对象使用了Spring默认的XML命名空间
    if (delegate.isDefaultNamespace(root)) {
        // 获取Bean定义的Document对象根元素的所有子节点
        NodeList nl = root.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            // 获得Document节点是XML元素节点
            if (node instanceof Element) {
                Element ele = (Element) node;
                // Bean定义的Document的元素节点使用的是Spring默认的XML命名空间
                if (delegate.isDefaultNamespace(ele)) {
                    // 使用Spring的Bean规则解析元素节点
                    parseDefaultElement(ele, delegate);
                }
                else {
                    // 没有使用Spring默认的XML命名空间，则使用用户自定义的解析规则解析元素节点
                    delegate.parseCustomElement(ele);
                }
            }
        }
    }
    else {
        // Document的根节点没有使用Spring默认的命名空间，则使用用户自定义的
        // 解析规则解析Document根节点
        delegate.parseCustomElement(root);
    }
}

/**
 * 使用Spring的Bean规则解析Document元素节点
 */
private void parseDefaultElement(Element ele, BeanDefinitionParserDelegate delegate) {
    // 如果元素节点是<Import>导入元素，进行导入解析
    if (delegate.nodeNameEquals(ele, IMPORT_ELEMENT)) {
        importBeanDefinitionResource(ele);
    }
    // 如果元素节点是<Alias>别名元素，进行别名解析
    else if (delegate.nodeNameEquals(ele, ALIAS_ELEMENT)) {
        processAliasRegistration(ele);
    }
    // 元素节点既不是导入元素，也不是别名元素，即普通的<Bean>元素，
    // 按照Spring的Bean规则解析元素
    else if (delegate.nodeNameEquals(ele, BEAN_ELEMENT)) {
        processBeanDefinition(ele, delegate);
    }
    else if (delegate.nodeNameEquals(ele, NESTED_BEANS_ELEMENT)) {
        // recurse
        doRegisterBeanDefinitions(ele);
    }
}
```

> 可以使用< import>元素来导入IOC容器所需要的其他资源，spring IOC容器在解析时会首先将指定导入的资源加载进容器中。
>
> 使用< ailas>别名时，spring IOC容器首先将别名元素所定义的别名注册到容器中。
>
> 对于其他的普通< bean>元素的解析由BeanDefinitionParserDelegate类的parseDefaultElement()来实现。



###### 12、载入< bean>元素





### AOP运行时序图

##### 必须明白的几个概念

> 1. 切面（Aspect）：面向规则，具有相同规则的方法集合体
> 2. 通知（Advice）：回调
> 3. 切入点（Point）：需要代理的具体方法
> 4. 目标对象（Target Object）：被代理的对象
> 5. AOP代理（AOP Proxy）：
> 6. 前置通知
> 7. 后置通知
> 8. 返回后通知
> 9. 环绕通知
> 10. 异常通知







主要流程

> 1. 寻找入口
> 2. 选择策略
> 3. 调用方法
> 4. 触发通知











# Mybatis

### Mybatis应用分析与最佳实践



**特性**

> 1. 使用连接池对连接进行管理
> 2. SQL和代码分离

