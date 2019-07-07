### springMvc使用内嵌tomcat
使用web.xml配置，适用于 springMvc项目无改动切换为jar包启动
```text
WebXmlMountListener实现org.apache.catalina.LifecycleListener接口监听
LifecycleEvent -> Lifecycle.BEFORE_START_EVENT 
在tomcat启动前挂载resources/WEB-INF/web.xml配置

读取 contextConfigLocation -> classpath:springMvc-config.xml
通过  org.springframework.web.context.ContextLoaderListener -> contextInitialized -> initWebApplicationContext
初始化 spring  WebApplicationContext  

```

tomcat -> webMvc 加载顺序 
 \_ ServletContainerInitializer.onStartup() -> WebApplicationInitializer
   \_  ServletContextListener -> ContextLoaderListener
     \_ loadServlet() -> DispatcherServlet
     
     
#####   ServletContainerInitializers 接口 (SCIs)
- ServletContainerInitializers (SCIs) are registered via an entry in the file META-INF/services/javax.servlet.ServletContainerInitializer that must be included in the JAR file that contains the SCI implementation.
  SCIs register an interest in annotations (class, method or field) and/or types via the HandlesTypes annotation which is added to the class.
  
  
    
    spring-web jar包中META-INF/services/org.springframework.web.SpringServletContainerInitializer
    中定义了 ServletContainerInitializer的实现类SpringServletContainerInitializer
    SpringServletContainerInitializer中注解@HandlesTypes(WebApplicationInitializer.class)
    定义处理WebApplicationInitializer.class 接口
    SpringServletContainerInitializer.onStartup(Set<Class<?>> webAppInitializerClasses, ServletContext servletContext)
    webAppInitializerClasses 为 WebApplicationInitializer所有实现类
     \_ org.springframework.web.context.AbstractContextLoaderInitializer
     \_ org.springframework.web.servlet.support.AbstractDispatcherServletInitializer
     \_ org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer

#####   ServletContextListener 接口
- Implementations of this interface receive notifications about changes to the servlet context of the web application they are part of. To receive notification events, the implementation class must be configured in the deployment descriptor for the web application.
 这个接口的实现类会接受到servlet context 变更的消息，必须配置在 web application deployment descriptor （web应用部署描述符）中
 
-  deployment descriptor


    A deployment descriptor (DD) refers to a configuration file for an artifact that is deployed to some container/engine.
    部署描述符DD指发布到一些 container（容器）或者 engine（引擎）中的应用（artifact） 的可配置文件
    In the Java Platform, Enterprise Edition, a deployment descriptor describes how a component, module or application (such as a web application or enterprise application) should be deployed.[1] It directs a deployment tool to deploy a module or application with specific container options, security settings and describes specific configuration requirements. XML is used for the syntax of these deployment descriptor files.
    在java平台，企业版本中，一个部署描述符（DD）描述一个组件，模块或者一个应用（例如web应用或者企业应用）如何被部署。
    For web applications, the deployment descriptor must be called web.xml and must reside in the WEB-INF directory in the web application root. For Java EE applications, the deployment descriptor must be named application.xml and must be placed directly in the META-INF directory at the top level of the application .ear file.
    对于web应用，部署描述符必须叫做web.xml并且放置在web应用的根目录下的WEB-INF文件中：
    webApplication
    |- WEB-INF
       |- web.xml
       
 （3）loadServlet()
 
 
SpringServletContainerInitializer调用所有 WebApplicationInitializer.onStartup( ServletContext servletContext) 方法

 
tomcat 根据SCIs定义初始化ServletContainer时加载该类


![ServletContainerInitializer](https://raw.githubusercontent.com/haochencheng/spring-demo/master/pics/servlet/mvc-ServletContainerInitializer.png)

#####   servlet 容器初始化 
```text

ServletContainerInitializer 初始化 -> WebApplicationInitializer
spring | SpringServletContainerInitializer implements ServletContainerInitializer
 \_ onStartup(Set<Class<?>> webAppInitializerClasses, ServletContext servletContext)
    \_ WebApplicationInitializer.onStartup(ServletContext servletContext)
       \_ 加载WebApplicationInitializer所有实现类

ServletContextListener 加载 -> ContextLoader
spring | ContextLoaderListener extends ContextLoader implements ServletContextListener
 \_ contextInitialized(ServletContextEvent event)
   \_ initWebApplicationContext(ServletContext servletContext)

StandardWrapper -> loadServlet() 加载servlet -> DispatcherServlet.init()
tomcat | StandardWrapper.loadServlet()
 \_ servlet = (Servlet) instanceManager.newInstance(servletClass);
   servletClass="org.springframework.web.servlet.DispatcherServlet"
    \_ spring | HttpServletBean extends HttpServlet implements EnvironmentCapable, EnvironmentAware 
                 \_init()
                    \_ FrameworkServlet extends HttpServletBean implements ApplicationContextAware
                       \_ initServletBean()
                       

```