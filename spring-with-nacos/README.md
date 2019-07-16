### spring-with-nacos 启动顺序
tomcat中 startInternal
ServletContainerInitializers servlet容器初始化

    ServletContainerInitializers (SCIs) are registered via an entry in the
    file META-INF/services/javax.servlet.ServletContainerInitializer
    
META-INF/services/javax.servlet.ServletContainerInitializer=org.springframework.web.SpringServletContainerInitializer

```java
@HandlesTypes(WebApplicationInitializer.class)
public class SpringServletContainerInitializer implements ServletContainerInitializer {
}
```
SpringServletContainerInitializer.onStartup(Set<Class<?>> webAppInitializerClasses, ServletContext servletContext)

容器调用，传入webAppInitializerClasses集合，spring循环调用

    for (WebApplicationInitializer initializer : initializers) {
        initializer.onStartup(servletContext);
    }

初始化servlet
WebApplicationInitializer.startup(ServletContext servletContext)
 \- WebApplicationInitializerConfig


WebApplicationInitializerConfig上标注注解@EnableWebMvc @ComponentScan 扫描包
```java
@EnableWebMvc
@ComponentScan("spring.with.nacos")
public class WebApplicationInitializerConfig implements WebApplicationInitializer {
    
    /**
    * 创建springApplicationContext 应用程序上下文
    * 
    * @param servletContext
    */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // Create the 'root' Spring application context
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(WebApplicationInitializerConfig.class);
        // Manage the lifecycle of the root application context
        servletContext.addListener(new ContextLoaderListener(rootContext));
        // Create the dispatcher servlet's Spring application context
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(rootContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }
    
    
    /**
    *  注册一个或多个要处理的注解类
     * Register one or more annotated classes to be processed.
     * 注意refresh必须全部处理所有的注册类
     * <p>Note that {@link #refresh()} must be called in order for the context
     * to fully process the new classes.
     * @param annotatedClasses one or more annotated classes,
     * e.g. {@link org.springframework.context.annotation.Configuration @Configuration} classes
     * @see #scan(String...)
     * @see #loadBeanDefinitions(DefaultListableBeanFactory)
     * @see #setConfigLocation(String)
     * @see #refresh()
     */
    public void register(Class<?>... annotatedClasses) {
        Assert.notEmpty(annotatedClasses, "At least one annotated class must be specified");
        this.annotatedClasses.addAll(Arrays.asList(annotatedClasses));
    }
}
```

@EnableWebMvc 导入 DelegatingWebMvcConfiguration.class
```java
@Import(DelegatingWebMvcConfiguration.class)
public @interface EnableWebMvc {
}
```


servlet初始化
ContextLoaderListener 
```java
public class ContextLoaderListener extends ContextLoader implements ServletContextListener {
}
```

### 多环境打包

















