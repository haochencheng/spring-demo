### spring-with-nacos 启动顺序

####   servletContainer 初始化
tomcat中 startInternal
ServletContainerInitializers servlet容器初始化

    ServletContainerInitializers (SCIs) are registered via an entry in the
    file META-INF/services/javax.servlet.ServletContainerInitializer
    
META-INF/services/javax.servlet.ServletContainerInitializer=org.springframework.web.SpringServletContainerInitializer

```java

@HandlesTypes(WebApplicationInitializer.class)
public class SpringServletContainerInitializer implements ServletContainerInitializer {

     //...

     /**
     * 容器调用，传入webAppInitializerClasses集合，spring循环调用
     * @param webAppInitializerClasses
     * @param servletContext
     */
     public void onStartup(Set<Class<?>> webAppInitializerClasses, ServletContext servletContext){
            for (WebApplicationInitializer initializer : initializers) {
                    initializer.onStartup(servletContext);
            }
      }
      
      //...
}

```

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

####   servletContext 初始化
#####   ContextLoaderListener 
初始化springContext 上下文，通过ContextLoader代理实现
```java
public class ContextLoaderListener extends ContextLoader implements ServletContextListener {
    
    /**
    	 * Create a new {@code ContextLoaderListener} that will create a web application
    	 * context based on the "contextClass" and "contextConfigLocation" servlet
    	 * context-params. See {@link ContextLoader} superclass documentation for details on
    	 * default values for each.
    	 * <p>This constructor is typically used when declaring {@code ContextLoaderListener}
    	 * as a {@code <listener>} within {@code web.xml}, where a no-arg constructor is
    	 * required.
    	 * <p>The created application context will be registered into the ServletContext under
    	 * the attribute name {@link WebApplicationContext#ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE}
    	 * and the Spring application context will be closed when the {@link #contextDestroyed}
    	 * lifecycle method is invoked on this listener.
    	 * @see ContextLoader
    	 * @see #ContextLoaderListener(WebApplicationContext)
    	 * @see #contextInitialized(ServletContextEvent)
    	 * @see #contextDestroyed(ServletContextEvent)
    	 */
    	public ContextLoaderListener() {
    	}
    
    	/**
    	 * Create a new {@code ContextLoaderListener} with the given application context. This
    	 * constructor is useful in Servlet 3.0+ environments where instance-based
    	 * registration of listeners is possible through the {@link javax.servlet.ServletContext#addListener}
    	 * API.
    	 * <p>The context may or may not yet be {@linkplain
    	 * org.springframework.context.ConfigurableApplicationContext#refresh() refreshed}. If it
    	 * (a) is an implementation of {@link ConfigurableWebApplicationContext} and
    	 * (b) has <strong>not</strong> already been refreshed (the recommended approach),
    	 * then the following will occur:
    	 * <ul>
    	 * <li>If the given context has not already been assigned an {@linkplain
    	 * org.springframework.context.ConfigurableApplicationContext#setId id}, one will be assigned to it</li>
    	 * <li>{@code ServletContext} and {@code ServletConfig} objects will be delegated to
    	 * the application context</li>
    	 * <li>{@link #customizeContext} will be called</li>
    	 * <li>Any {@link org.springframework.context.ApplicationContextInitializer ApplicationContextInitializer}s
    	 * specified through the "contextInitializerClasses" init-param will be applied.</li>
    	 * <li>{@link org.springframework.context.ConfigurableApplicationContext#refresh refresh()} will be called</li>
    	 * </ul>
    	 * If the context has already been refreshed or does not implement
    	 * {@code ConfigurableWebApplicationContext}, none of the above will occur under the
    	 * assumption that the user has performed these actions (or not) per his or her
    	 * specific needs.
    	 * <p>See {@link org.springframework.web.WebApplicationInitializer} for usage examples.
    	 * <p>In any case, the given application context will be registered into the
    	 * ServletContext under the attribute name {@link
    	 * WebApplicationContext#ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE} and the Spring
    	 * application context will be closed when the {@link #contextDestroyed} lifecycle
    	 * method is invoked on this listener.
    	 * @param context the application context to manage
    	 * @see #contextInitialized(ServletContextEvent)
    	 * @see #contextDestroyed(ServletContextEvent)
    	 */
    	public ContextLoaderListener(WebApplicationContext context) {
    		super(context);
    	}
    
    
    	/**
    	 * Initialize the root web application context.
    	 */
    	@Override
    	public void contextInitialized(ServletContextEvent event) {
    	    //初始化根应用程序上下文
    		initWebApplicationContext(event.getServletContext());
    	}
    
    
    	/**
    	 * Close the root web application context.
    	 */
    	@Override
    	public void contextDestroyed(ServletContextEvent event) {
    	    //关闭根应用程序上下文
    		closeWebApplicationContext(event.getServletContext());
    		//清理上下文 中需要清理的数据
    		ContextCleanupListener.cleanupAttributes(event.getServletContext());
    	}
    
}
```

#####   ServletContextListener 
通知web应用程序初始化正在启动，所有的ServletContextListeners都会受到通知，在任何filter或者servlet初始化前。
```java

public interface ServletContextListener extends EventListener {

    /**
     ** Notification that the web application initialization process is starting.
     * All ServletContextListeners are notified of context initialization before
     * any filter or servlet in the web application is initialized.
     * @param sce Information about the ServletContext that was initialized
     */
    public void contextInitialized(ServletContextEvent sce);

    /**
     ** Notification that the servlet context is about to be shut down. All
     * servlets and filters have been destroy()ed before any
     * ServletContextListeners are notified of context destruction.
     * @param sce Information about the ServletContext that was destroyed
     */
    public void contextDestroyed(ServletContextEvent sce);
}

```

#####   ContextLoader
spring applicationContext应用程序初始化上下文代理类
```java

public class ContextLoader {

    //...
    
    /**
     *  ContextLoader 配置文件相对路径 相对于 ContextLoader.class
	 * Name of the class path resource (relative to the ContextLoader class)
	 * that defines ContextLoader's default strategy names.
	 */
	private static final String DEFAULT_STRATEGIES_PATH = "ContextLoader.properties";

    /**
    * ContextLoader 默认配置
    */
	private static final Properties defaultStrategies;

	static {
	    //初始化加载 ContextLoader 内置 配置文件 不允许自定义
		// Load default strategy implementations from properties file.
		// This is currently strictly internal and not meant to be customized
		// by application developers.
		try {
			ClassPathResource resource = new ClassPathResource(DEFAULT_STRATEGIES_PATH, ContextLoader.class);
			defaultStrategies = PropertiesLoaderUtils.loadProperties(resource);
		}
		catch (IOException ex) {
			throw new IllegalStateException("Could not load 'ContextLoader.properties': " + ex.getMessage());
		}
	}
	
	//...


}

```

ContextLoader.properties
```properties
# 如果 context-param 没有明确的指定WebApplicationContext 使用默认配置（fallback 兜底方案）
# Default WebApplicationContext implementation class for ContextLoader.
# Used as fallback when no explicit context implementation has been specified as context-param.
# Not meant to be customized by application developers.

org.springframework.web.context.WebApplicationContext=org.springframework.web.context.support.XmlWebApplicationContext


```




### 多环境打包

















