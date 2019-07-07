### springMvc使用内嵌tomcat
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
   \_  ServletContextListener
     \_ loadServlet()
     
     
#####   ServletContainerInitializers (SCIs)
ServletContainerInitializers (SCIs) are registered via an entry in the file META-INF/services/javax.servlet.ServletContainerInitializer that must be included in the JAR file that contains the SCI implementation.

#####   servlet 容器初始化     
```text
spring-web jar包中META-INF/services/org.springframework.web.SpringServletContainerInitializer
中定义了 ServletContainerInitializer的实现类
tomcat 根据SCIs定义初始化ServletContainer时加载该类


ServletContainerInitializer 初始化 -> WebApplicationInitializer
spring | SpringServletContainerInitializer implements ServletContainerInitializer
 \_ onStartup(Set<Class<?>> webAppInitializerClasses, ServletContext servletContext)
    \_ WebApplicationInitializer.onStartup(ServletContext servletContext)

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