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
>
> ApplicationContext  ->  AdvisedSupport  ->  AopConfig  ->  Advice  ->   JDKDynamicAopProxy  



# Spring核心原理源码分析

### IOC运行时序图

> 对象与对象之间的关系表示：xml/yml/注解等
>
> 描述对象关系的文件存放：classpath/network/filesystem/servletContext/annotation.
>
> spring读取时先要找到文件在哪儿，再加载里面的内容。由于来源广泛且不同配置文件书写语法不一致，于是就定义 了一个配置文件标准，将配置文件中的信息统一解析放在BeanDefinition中，统一格式。
>
> 在解析配置文件时根据不同配置文件类型实施不同解析策略。
>
> ClassPathApplicationContext/AnnotationConfigApplicationContext/WebApplicationContext等解析策略。BeanDefinitionReader也有多种（XmlBeanDefinitionReader等）BeanDefinition层次也有多种封装策略（如：XmlBeanDefinition等 ），但最终都需封装成BeanDefinition。

**IOC容器初始化三部曲**

> 定位：定位配置文件和扫描相关注解
>
> 加载
>
> 注册



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

