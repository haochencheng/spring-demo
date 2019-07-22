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

tomcat初始化ServletContainer -> ServletContainerInitializer.onStartup(Set<Class<?>> c, ServletContext ctx)
后会调用ServletContextListener.contextInitialized(ServletContextEvent event)发送事件
初始化springContext
```java

public class StandardContext {
    
     @Override
        protected synchronized void startInternal() throws LifecycleException {

            // Call ServletContainerInitializers
            for (Map.Entry<ServletContainerInitializer, Set<Class<?>>> entry :
                initializers.entrySet()) {
                try {
                    entry.getKey().onStartup(entry.getValue(),
                            getServletContext());
                } catch (ServletException e) {
                    log.error(sm.getString("standardContext.sciFail"), e);
                    ok = false;
                    break;
                }
            }
    
            //...
             // Configure and call application event listeners
            if (ok) {
                if (!listenerStart()) {
                    log.error(sm.getString("standardContext.listenerFail"));
                    ok = false;
                }
            }
     
     }
    
    //...
    public boolean listenerStart() {
        for (int i = 0; i < instances.length; i++) {
                if (!(instances[i] instanceof ServletContextListener))
                    continue;
                ServletContextListener listener =
                    (ServletContextListener) instances[i];
                try {
                    fireContainerEvent("beforeContextInitialized", listener);
                    // ServletContextListener 发送 ServletContextEvent 事件 
                    if (noPluggabilityListeners.contains(listener)) {
                        listener.contextInitialized(tldEvent);
                    } else {
                        listener.contextInitialized(event);
                    }
                    fireContainerEvent("afterContextInitialized", listener);
                } catch (Throwable t) {
                    ExceptionUtils.handleThrowable(t);
                    fireContainerEvent("afterContextInitialized", listener);
                    getLogger().error
                        (sm.getString("standardContext.listenerStart",
                                      instances[i].getClass().getName()), t);
                    ok = false;
                }
            }
        }
}
    
```
ServletContextListener.contextInitialized(ServletContextEvent event)
调用ContextLoaderListener（配置在web.xml中或者在java配置中）
```java
public class ContextLoaderListener extends ContextLoader implements ServletContextListener {

    	/**
    	 * Initialize the root web application context.
    	 */
    	@Override
    	public void contextInitialized(ServletContextEvent event) {
    		initWebApplicationContext(event.getServletContext());
    	}
    	
}
```
ContextLoaderListener.initWebApplicationContext()调用父类ContextLoader代理实现

```java
public class ContextLoader {
    
    /**
    * 初始化spring WebApplicationContext
    * @param servletContext
    * @return 
    */
    public WebApplicationContext initWebApplicationContext(ServletContext servletContext) {
    	// Store context in local instance variable, to guarantee that
        			// it is available on ServletContext shutdown.
        			if (this.context == null) {
        			    //创建webApplicationContext
        				this.context = createWebApplicationContext(servletContext);
        			}
        			if (this.context instanceof ConfigurableWebApplicationContext) {
        				ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext) this.context;
        				if (!cwac.isActive()) {
        					// The context has not yet been refreshed -> provide services such as
        					// setting the parent context, setting the application context id, etc
        					if (cwac.getParent() == null) {
        						// The context instance was injected without an explicit parent ->
        						// determine parent for root web application context, if any.
        						ApplicationContext parent = loadParentContext(servletContext);
        						cwac.setParent(parent);
        					}
        					//刷新方法refresh() 主要看这个方法
        					configureAndRefreshWebApplicationContext(cwac, servletContext);
        				}
        			}
        			servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.context);
        
        			ClassLoader ccl = Thread.currentThread().getContextClassLoader();
        			if (ccl == ContextLoader.class.getClassLoader()) {
        				currentContext = this.context;
        			}
        			else if (ccl != null) {
        				currentContextPerThread.put(ccl, this.context);
        			}
        
        			if (logger.isDebugEnabled()) {
        				logger.debug("Published root WebApplicationContext as ServletContext attribute with name [" +
        						WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE + "]");
        			}
        			if (logger.isInfoEnabled()) {
        				long elapsedTime = System.currentTimeMillis() - startTime;
        				logger.info("Root WebApplicationContext: initialization completed in " + elapsedTime + " ms");
        			}
        
        			return this.context;
    }
    
    /**
    * 通过反射 创建 WebApplicationContext
    * @param sc
    * @return 
    */
    protected WebApplicationContext createWebApplicationContext(ServletContext sc) {
        //找到  ContextClass 通过反射创建  WebApplicationContext
        Class<?> contextClass = determineContextClass(sc);
        if (!ConfigurableWebApplicationContext.class.isAssignableFrom(contextClass)) {
            throw new ApplicationContextException("Custom context class [" + contextClass.getName() +
                    "] is not of type [" + ConfigurableWebApplicationContext.class.getName() + "]");
        }
        return (ConfigurableWebApplicationContext) BeanUtils.instantiateClass(contextClass);
    }
    
    /**
    * 确定ContextClass 返回自定义的ContextClass 通过参数 CONTEXT_CLASS_PARAM = "contextClass";
     * or 默认的 ContextLoader.properties中配置的  org.springframework.web.context.WebApplicationContext=org.springframework.web.context.support.XmlWebApplicationContext
    * @param servletContext
    * @return 
    */
    protected Class<?> determineContextClass(ServletContext servletContext) {
    		String contextClassName = servletContext.getInitParameter(CONTEXT_CLASS_PARAM);
    		if (contextClassName != null) {
    			try {
    			    //	return (clToUse != null ? clToUse.loadClass(name) : Class.forName(name));
    			    // 如果类加载器 存在 则是以 loadClass 否则 使用 系统加载器
    			    //  在Java中，类装载器把一个类装入Java虚拟机中，要经过三个步骤来完成：装载、链接和初始化，
    			    //  其中链接又可以分成校验、准备和解析
    			    // 　  装载：查找和导入类或接口的二进制数据； 
                    //　　链接：执行下面的校验、准备和解析步骤，其中解析步骤是可以选择的； 
                    //　　校验：检查导入类或接口的二进制数据的正确性； 
                    //　　准备：给类的静态变量分配并初始化存储空间； 
                    //　　解析：将符号引用转成直接引用； 
                    //　　初始化：激活类的静态变量的初始化Java代码和静态Java代码块。
    			    // ClassLoader.loadClass(name) -> loadClass(name,false) 装载 不解析 
    			    // Class.forName(name) -> 
    				return ClassUtils.forName(contextClassName, ClassUtils.getDefaultClassLoader());
    			}
    			catch (ClassNotFoundException ex) {
    				throw new ApplicationContextException(
    						"Failed to load custom context class [" + contextClassName + "]", ex);
    			}
    		}
    		else {
    			contextClassName = defaultStrategies.getProperty(WebApplicationContext.class.getName());
    			try {
    				return ClassUtils.forName(contextClassName, ContextLoader.class.getClassLoader());
    			}
    			catch (ClassNotFoundException ex) {
    				throw new ApplicationContextException(
    						"Failed to load default context class [" + contextClassName + "]", ex);
    			}
    		}
    	}
    
    /**
    * 配置并且刷新WebApplicationContext
    * @param wac
    * @param sc
    */
    protected void configureAndRefreshWebApplicationContext(ConfigurableWebApplicationContext wac, ServletContext sc) {
    		if (ObjectUtils.identityToString(wac).equals(wac.getId())) {
    			// The application context id is still set to its original default value
    			// -> assign a more useful id based on available information
    			//如果CONTEXT_ID_PARAM 参数存在 使用参数配置的id
    			String idParam = sc.getInitParameter(CONTEXT_ID_PARAM);
    			if (idParam != null) {
    				wac.setId(idParam);
    			}
    			else {
    			    //根据getContextPath生成ConfigurableWebApplicationContext id
    				// Generate default id...
    				wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX +
    						ObjectUtils.getDisplayString(sc.getContextPath()));
    			}
    		}
            //设置 WebApplicationContext ServletContext上下文
    		wac.setServletContext(sc);
    		// public static final String CONFIG_LOCATION_PARAM = "contextConfigLocation";
            // default 没有配置使用默认 XmlWebApplicationContext#DEFAULT_CONFIG_LOCATION = "/WEB-INF/applicationContext.xml"
    		String configLocationParam = sc.getInitParameter(CONFIG_LOCATION_PARAM);
    		if (configLocationParam != null) {
    			wac.setConfigLocation(configLocationParam);
    		}
    
    		// The wac environment's #initPropertySources will be called in any case when the context
    		// is refreshed; do it eagerly here to ensure servlet property sources are in place for
    		// use in any post-processing or initialization that occurs below prior to #refresh
    		ConfigurableEnvironment env = wac.getEnvironment();
    		if (env instanceof ConfigurableWebEnvironment) {
    			((ConfigurableWebEnvironment) env).initPropertySources(sc, null);
    		}
            //自定义Context 
    		customizeContext(sc, wac);
    		//刷新ApplicationContext
    		wac.refresh();
    	}
    	
    	protected void customizeContext(ServletContext sc, ConfigurableWebApplicationContext wac) {
        		List<Class<ApplicationContextInitializer<ConfigurableApplicationContext>>> initializerClasses =
        				determineContextInitializerClasses(sc);
        
        		for (Class<ApplicationContextInitializer<ConfigurableApplicationContext>> initializerClass : initializerClasses) {
        			Class<?> initializerContextClass =
        					GenericTypeResolver.resolveTypeArgument(initializerClass, ApplicationContextInitializer.class);
        			if (initializerContextClass != null && !initializerContextClass.isInstance(wac)) {
        				throw new ApplicationContextException(String.format(
        						"Could not apply context initializer [%s] since its generic parameter [%s] " +
        						"is not assignable from the type of application context used by this " +
        						"context loader: [%s]", initializerClass.getName(), initializerContextClass.getName(),
        						wac.getClass().getName()));
        			}
        			this.contextInitializers.add(BeanUtils.instantiateClass(initializerClass));
        		}
        
        		AnnotationAwareOrderComparator.sort(this.contextInitializers);
        		for (ApplicationContextInitializer<ConfigurableApplicationContext> initializer : this.contextInitializers) {
        			initializer.initialize(wac);
        		}
        	}
        	
       /**
         * 返回 web.xml 中配置的CONTEXT_INITIALIZER_CLASSES_PARAM参数 =  ApplicationContextInitializer的实现类
       	 * Return the {@link ApplicationContextInitializer} implementation classes to use
       	 * if any have been specified by {@link #CONTEXT_INITIALIZER_CLASSES_PARAM}.
       	 * @param servletContext current servlet context
       	 * @see #CONTEXT_INITIALIZER_CLASSES_PARAM
       	 */
       	protected List<Class<ApplicationContextInitializer<ConfigurableApplicationContext>>>
       			determineContextInitializerClasses(ServletContext servletContext) {
       
       		List<Class<ApplicationContextInitializer<ConfigurableApplicationContext>>> classes =
       				new ArrayList<Class<ApplicationContextInitializer<ConfigurableApplicationContext>>>();
       
       		String globalClassNames = servletContext.getInitParameter(GLOBAL_INITIALIZER_CLASSES_PARAM);
       		if (globalClassNames != null) {
       			for (String className : StringUtils.tokenizeToStringArray(globalClassNames, INIT_PARAM_DELIMITERS)) {
       				classes.add(loadInitializerClass(className));
       			}
       		}
       
       		String localClassNames = servletContext.getInitParameter(CONTEXT_INITIALIZER_CLASSES_PARAM);
       		if (localClassNames != null) {
       			for (String className : StringUtils.tokenizeToStringArray(localClassNames, INIT_PARAM_DELIMITERS)) {
       				classes.add(loadInitializerClass(className));
       			}
       		}
       
       		return classes;
       	} 	

    
}


```

wac.refresh()会调用AbstractApplicationContext.refresh()方法
```java
public abstract class AbstractApplicationContext extends DefaultResourceLoader
		implements ConfigurableApplicationContext, DisposableBean {
    
    //...
    
    @Override
    	public void refresh() throws BeansException, IllegalStateException {
    		synchronized (this.startupShutdownMonitor) {
    			// Prepare this context for refreshing.
    			// 准备环境
    			prepareRefresh();
    
    			// Tell the subclass to refresh the internal bean factory.
    			// 告诉子类 刷新 内置bean factory
    			ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
    
    			// Prepare the bean factory for use in this context.
    			prepareBeanFactory(beanFactory);
    
    			try {
    				// Allows post-processing of the bean factory in context subclasses.
    				postProcessBeanFactory(beanFactory);
    
    				// Invoke factory processors registered as beans in the context.
    				invokeBeanFactoryPostProcessors(beanFactory);
    
    				// Register bean processors that intercept bean creation.
    				registerBeanPostProcessors(beanFactory);
    
    				// Initialize message source for this context.
    				initMessageSource();
    
    				// Initialize event multicaster for this context.
    				initApplicationEventMulticaster();
    
    				// Initialize other special beans in specific context subclasses.
    				onRefresh();
    
    				// Check for listener beans and register them.
    				registerListeners();
    
    				// Instantiate all remaining (non-lazy-init) singletons.
    				finishBeanFactoryInitialization(beanFactory);
    
    				// Last step: publish corresponding event.
    				finishRefresh();
    			}
    
    			catch (BeansException ex) {
    				if (logger.isWarnEnabled()) {
    					logger.warn("Exception encountered during context initialization - " +
    							"cancelling refresh attempt: " + ex);
    				}
    
    				// Destroy already created singletons to avoid dangling resources.
    				destroyBeans();
    
    				// Reset 'active' flag.
    				cancelRefresh(ex);
    
    				// Propagate exception to caller.
    				throw ex;
    			}
    
    			finally {
    				// Reset common introspection caches in Spring's core, since we
    				// might not ever need metadata for singleton beans anymore...
    				resetCommonCaches();
    			}
    		}
    	}
    
    	/**
    	 * 准备上下文，设置启动日期，设置活动状态，初始化property sources
    	 * Prepare this context for refreshing, setting its startup date and
    	 * active flag as well as performing any initialization of property sources.
    	 */
    	protected void prepareRefresh() {
    		// Switch to active.
    		this.startupDate = System.currentTimeMillis();
    		this.closed.set(false);
    		this.active.set(true);
    
    		if (logger.isInfoEnabled()) {
    			logger.info("Refreshing " + this);
    		}
    
    		// Initialize any placeholder property sources in the context environment.
    		// 初始化上下文环境中 任何 占位符属性资源
    		initPropertySources();
    
    		// 验证所有标记为必需的属性都是可解析的
    		// Validate that all properties marked as required are resolvable:
    		// see ConfigurablePropertyResolver#setRequiredProperties
    		getEnvironment().validateRequiredProperties();
    
    		// Allow for the collection of early ApplicationEvents,
    		// to be published once the multicaster is available...
    		// 允许手机早期的事件 一旦 组播可用就可以发布
    		// 通常，在传统的网络通讯中，有两种方式，一种是源主机和目标主机两台主机之间进行的“一对一”的通讯方式，即单播，第二种是一台源主机与网络中所有其他主机之间进行的通讯，即广播。那么，如果需要将信息从源主机发送到网络中的多个目标主机，要么采用广播方式，这样网络中所有主机都会收到信息，要么，采用单播方式，由源主机分别向各个不同目标主机发送信息。
            //   可以看出来，在广播方式下，信息会发送到不需要该信息的主机从而浪费带宽资源，甚至引起广播风暴：而单播方式下，会因为数据包的多次重复而浪费带宽资源，同时，源主机的负荷会因为多次的数据复制而加大，所以，单播与广播对于多点发送问题有缺陷。
    		// 组播又称多目标广播、多播。网络中使用的一种传输方式，它允许把 所发消息传送给所有可能目的地中的一个经过选择的子集，即向明确指出的多种地址输送信息。是一种在一个发送者和多个接收者之间进行通信的方法。与任播(anycast)和单播(unicast)一起，组播也是一种IPv6的包传送方式。组播在CDPD技术中的无线数据网络中也可以使用。
    		this.earlyApplicationEvents = new LinkedHashSet<ApplicationEvent>();
    	}
    	
    	protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
        		refreshBeanFactory();
        		ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        		if (logger.isDebugEnabled()) {
        			logger.debug("Bean factory for " + getDisplayName() + ": " + beanFactory);
        		}
        		return beanFactory;
        }
    	
    //...

}
```
refreshBeanFactory()抽象方法调用子类AbstractRefreshableApplicationContext.refreshBeanFactory()方法

```java
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {
    
        @Override
    	protected final void refreshBeanFactory() throws BeansException {
            //如果有BeanFactory 销毁bean 关闭beanFactory
    		if (hasBeanFactory()) {
    			destroyBeans();
    			closeBeanFactory();
    		}
    		try {
    		    // 创建默认列表bean factory
    			DefaultListableBeanFactory beanFactory = createBeanFactory();
    			beanFactory.setSerializationId(getId());
    			customizeBeanFactory(beanFactory);
    			//加载 BeanDefinition bean定义 重点看这个方法
    			// 不论是xml中配置的bean 还是 注解注入的bean 都将通过这个类注入
    			loadBeanDefinitions(beanFactory);
    			synchronized (this.beanFactoryMonitor) {
    				this.beanFactory = beanFactory;
    			}
    		}
    		catch (IOException ex) {
    			throw new ApplicationContextException("I/O error parsing bean definition source for " + getDisplayName(), ex);
    		}
    	}
    	
    	//创建默认bean factory
    	protected DefaultListableBeanFactory createBeanFactory() {
            return new DefaultListableBeanFactory(getInternalParentBeanFactory());
        }
    
}
```
DefaultListableBeanFactory 继承抽象可注入的beanFactory实现可配置的ListableBeanFactory,注册bean定义接口
调用父类构造器
```java

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory
		implements ConfigurableListableBeanFactory, BeanDefinitionRegistry, Serializable {

    /**
     * Create a new DefaultListableBeanFactory with the given parent.
     * @param parentBeanFactory the parent BeanFactory
     */
    public DefaultListableBeanFactory(BeanFactory parentBeanFactory) {
        super(parentBeanFactory);
    }

}
```
抽象有注入能力的BeanFactory 
```java
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory
		implements AutowireCapableBeanFactory {
    
    /**
     * Create a new AbstractAutowireCapableBeanFactory with the given parent.
     * @param parentBeanFactory parent bean factory, or {@code null} if none
     */
    public AbstractAutowireCapableBeanFactory(BeanFactory parentBeanFactory) {
        this();
        setParentBeanFactory(parentBeanFactory);
    }
    
    /**
     * Create a new AbstractAutowireCapableBeanFactory.
     */
    public AbstractAutowireCapableBeanFactory() {
        super();
        // 注册忽略的依赖接口
        ignoreDependencyInterface(BeanNameAware.class);
        ignoreDependencyInterface(BeanFactoryAware.class);
        ignoreDependencyInterface(BeanClassLoaderAware.class);
    }
    
}
```

loadBeanDefinitions(beanFactory);加载bean定义，由子类加载
```java

public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {

    /**
	 * Load bean definitions into the given bean factory, typically through
	 * delegating to one or more bean definition readers.
	 * @param beanFactory the bean factory to load bean definitions into
	 * @throws BeansException if parsing of the bean definitions failed
	 * @throws IOException if loading of bean definition files failed
	 * @see org.springframework.beans.factory.support.PropertiesBeanDefinitionReader
	 * @see org.springframework.beans.factory.xml.XmlBeanDefinitionReader
	 */
	protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory)
			throws BeansException, IOException;
	
    }

}

```
有4个子类重写了 loadBeanDefinitions 方法
1. public abstract class AbstractXmlApplicationContext extends AbstractRefreshableConfigApplicationContext 
2. public class AnnotationConfigWebApplicationContext extends AbstractRefreshableWebApplicationContext
   		implements AnnotationConfigRegistry 

3. public class GroovyWebApplicationContext extends AbstractRefreshableWebApplicationContext implements GroovyObject 
4. public class XmlWebApplicationContext extends AbstractRefreshableWebApplicationContext 

因为在WebApplicationInitializer中 servletContext.addListener(new ContextLoaderListener(rootContext));
ContextLoaderListener注入的是AnnotationConfigWebApplicationContext
这里分析AnnotationConfigWebApplicationContext.loadBeanDefinitions
```java
public class AnnotationConfigWebApplicationContext extends AbstractRefreshableWebApplicationContext
		implements AnnotationConfigRegistry {

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        //注解Bean定义读取器
        //ClassPathBeanDefinitionScanner的替代方案，一个适配器，只读取 编程的方式 显示 注入带注解的Bean 
        AnnotatedBeanDefinitionReader reader = getAnnotatedBeanDefinitionReader(beanFactory);
        //类路径Bean定义扫描器
        // @Component @Repository @Service @Controller @ManagedBean @Named
        ClassPathBeanDefinitionScanner scanner = getClassPathBeanDefinitionScanner(beanFactory);

        BeanNameGenerator beanNameGenerator = getBeanNameGenerator();
        if (beanNameGenerator != null) {
            reader.setBeanNameGenerator(beanNameGenerator);
            scanner.setBeanNameGenerator(beanNameGenerator);
            beanFactory.registerSingleton(AnnotationConfigUtils.CONFIGURATION_BEAN_NAME_GENERATOR, beanNameGenerator);
        }

        ScopeMetadataResolver scopeMetadataResolver = getScopeMetadataResolver();
        if (scopeMetadataResolver != null) {
            reader.setScopeMetadataResolver(scopeMetadataResolver);
            scanner.setScopeMetadataResolver(scopeMetadataResolver);
        }

        if (!this.annotatedClasses.isEmpty()) {
            if (logger.isInfoEnabled()) {
                logger.info("Registering annotated classes: [" +
                        StringUtils.collectionToCommaDelimitedString(this.annotatedClasses) + "]");
            }
            reader.register(ClassUtils.toClassArray(this.annotatedClasses));
        }

        if (!this.basePackages.isEmpty()) {
            if (logger.isInfoEnabled()) {
                logger.info("Scanning base packages: [" +
                        StringUtils.collectionToCommaDelimitedString(this.basePackages) + "]");
            }
            scanner.scan(StringUtils.toStringArray(this.basePackages));
        }

        String[] configLocations = getConfigLocations();
        if (configLocations != null) {
            for (String configLocation : configLocations) {
                try {
                    Class<?> clazz = getClassLoader().loadClass(configLocation);
                    if (logger.isInfoEnabled()) {
                        logger.info("Successfully resolved class for [" + configLocation + "]");
                    }
                    reader.register(clazz);
                }
                catch (ClassNotFoundException ex) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Could not load class for config location [" + configLocation +
                                "] - trying package scan. " + ex);
                    }
                    int count = scanner.scan(configLocation);
                    if (logger.isInfoEnabled()) {
                        if (count == 0) {
                            logger.info("No annotated classes found for specified class/package [" + configLocation + "]");
                        }
                        else {
                            logger.info("Found " + count + " annotated classes in package [" + configLocation + "]");
                        }
                    }
                }
            }
        }
    }

}


```
### 多环境打包

















