### spring处理循环依赖





### spring scan 扫描包
AnnotationConfigApplicationContext 中有两个属性
```java

public class AnnotationConfigApplicationContext extends GenericApplicationContext {

    private final AnnotatedBeanDefinitionReader reader;

	private final ClassPathBeanDefinitionScanner scanner;
	
}

```

其中 AnnotatedBeanDefinitionReader reader 用来读取指定注解类
```java

public void register(Class<?>... annotatedClasses) {
		Assert.notEmpty(annotatedClasses, "At least one annotated class must be specified");
		this.reader.register(annotatedClasses);
	}


```

其中  ClassPathBeanDefinitionScanner scanner 用来扫描配置的包
其实现为 ClassPathBeanDefinitionScanner 

```java
public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {
    
    /** 
     * 返回注册的bean的数量
     * Perform a scan within the specified base packages.
     * @param basePackages the packages to check for annotated classes
     * @return number of beans registered
     */
    public int scan(String... basePackages) {
    		int beanCountAtScanStart = this.registry.getBeanDefinitionCount();
            
    		//调用doScan 方法
    		doScan(basePackages);
    
    		// Register annotation config processors, if necessary.
    		if (this.includeAnnotationConfig) {
    			AnnotationConfigUtils.registerAnnotationConfigProcessors(this.registry);
    		}
    
    		return this.registry.getBeanDefinitionCount() - beanCountAtScanStart;
    }
    
    
    /**
     * Perform a scan within the specified base packages,
     * returning the registered bean definitions.
     * <p>This method does <i>not</i> register an annotation config processor
     * but rather leaves this up to the caller.
     * @param basePackages the packages to check for annotated classes
     * @return set of beans registered if any for tooling registration purposes (never {@code null})
     */
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Assert.notEmpty(basePackages, "At least one base package must be specified");
        Set<BeanDefinitionHolder> beanDefinitions = new LinkedHashSet<BeanDefinitionHolder>();
        for (String basePackage : basePackages) {
            //发现spring组件 返回 Set<BeanDefinition> 集合
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            for (BeanDefinition candidate : candidates) {
                //遍历传入多个参数 
                ScopeMetadata scopeMetadata = this.scopeMetadataResolver.resolveScopeMetadata(candidate);
                candidate.setScope(scopeMetadata.getScopeName());
                String beanName = this.beanNameGenerator.generateBeanName(candidate, this.registry);
                if (candidate instanceof AbstractBeanDefinition) {
                    postProcessBeanDefinition((AbstractBeanDefinition) candidate, beanName);
                }
                if (candidate instanceof AnnotatedBeanDefinition) {
                    AnnotationConfigUtils.processCommonDefinitionAnnotations((AnnotatedBeanDefinition) candidate);
                }
                if (checkCandidate(beanName, candidate)) {
                    BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(candidate, beanName);
                    definitionHolder = AnnotationConfigUtils.applyScopedProxyMode(scopeMetadata, definitionHolder, this.registry);
                    beanDefinitions.add(definitionHolder);
                    registerBeanDefinition(definitionHolder, this.registry);
                }
            }
        }
        return beanDefinitions;
    }
    	
    	
    
}
```

其中 doScan 调用 ClassPathScanningCandidateComponentProvider 中的 

```java

public class ClassPathScanningCandidateComponentProvider implements EnvironmentCapable, ResourceLoaderAware {


/**
	 * Scan the class path for candidate components.
	 * @param basePackage the package to check for annotated classes
	 * @return a corresponding Set of autodetected bean definitions
	 */
	public Set<BeanDefinition> findCandidateComponents(String basePackage) {
	    //eg 我传入的basePackage=config
		Set<BeanDefinition> candidates = new LinkedHashSet<BeanDefinition>();
		try {
		    // packageSearchPath=classpath*:config/**/*.class
			String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
					resolveBasePackage(basePackage) + "/" + this.resourcePattern;
			// config 中有一个类 SpringSingletonBeanConfig 
			// resources = [/Users/haochencheng/Workspace/java/demo/spring-demo/spring-cycleref-demo/target/classes/config/SpringSingletonBeanConfig.class]
			Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
			boolean traceEnabled = logger.isTraceEnabled();
			boolean debugEnabled = logger.isDebugEnabled();
			for (Resource resource : resources) {
				if (traceEnabled) {
					logger.trace("Scanning " + resource);
				}
				if (resource.isReadable()) {
					try {
					    //获取类的元信息 使用  org.springframework.asm.ClassReader 读取
						MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
						if (isCandidateComponent(metadataReader)) {
							ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader);
							sbd.setResource(resource);
							sbd.setSource(resource);
							if (isCandidateComponent(sbd)) {
								if (debugEnabled) {
									logger.debug("Identified candidate component class: " + resource);
								}
								candidates.add(sbd);
							}
							else {
								if (debugEnabled) {
									logger.debug("Ignored because not a concrete top-level class: " + resource);
								}
							}
						}
						else {
							if (traceEnabled) {
								logger.trace("Ignored because not matching any filter: " + resource);
							}
						}
					}
					catch (Throwable ex) {
						throw new BeanDefinitionStoreException(
								"Failed to read candidate component class: " + resource, ex);
					}
				}
				else {
					if (traceEnabled) {
						logger.trace("Ignored because not readable: " + resource);
					}
				}
			}
		}
		catch (IOException ex) {
			throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
		}
		return candidates;
	}
	
}

```