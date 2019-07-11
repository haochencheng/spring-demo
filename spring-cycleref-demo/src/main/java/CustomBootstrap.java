import annotation.CustomerBean;
import annotation.CustomerComponentScan;
import annotation.CustomerConfiguration;
import bean.BeanA;
import bean.BeanB;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.InvalidPathException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-10 19:53
 **/
@CustomerComponentScan("config")
public class CustomBootstrap {

    private static final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();
    private static final Map<String, Object> earlySingletonObjects = new HashMap<>();

    private static final Set<String> registeredSingletons = new LinkedHashSet<>(64);

    public static void main(String[] args) throws Exception {
        init(CustomBootstrap.class);
    }

    private static final HashMap beanClassHashMap=new HashMap();

    /**
     * 通过启动类初始化
     *
     * @param cl 启动类
     */
    public static void init(Class<?> cl) throws URISyntaxException, MalformedURLException, ClassNotFoundException {
        //加载扫描指定配置下文件
        CustomerComponentScan customerComponentScan = cl.getDeclaredAnnotation(CustomerComponentScan.class);
        System.out.println(customerComponentScan.value());
        String scan = customerComponentScan.value();
        ClassLoader classLoader = cl.getClassLoader();
        Enumeration<URL> resources;
        try {
            resources = classLoader.getResources(scan);
        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidPathException(scan, "文件不存在");
        }
        URL url = null;
        while (resources.hasMoreElements()) {
            url = resources.nextElement();
            System.out.println(url.toString());
        }

        File file=new File(url.toURI());
        File[] files = file.listFiles();
        URL[] urls=new URL[files.length];
        for (int i = 0; i < files.length ;i++) {
            urls[i]=files[i].toURI().toURL();
        }
        //spring使用asm加载类
        URLClassLoader urlClassLoader=URLClassLoader.newInstance(urls, classLoader);
        for (int i = 0; i < files.length ;i++) {
            Class<?> aClass = urlClassLoader.loadClass(scan + "." + files[i].getName().split("\\.")[0]);
            System.out.println(aClass.getName());
            System.out.println(aClass.getSimpleName());
            //spring 中有 是否可以覆盖类
            beanClassHashMap.putIfAbsent(aClass.getSimpleName(),aClass);

            //读取 指定配置类注解
            CustomerConfiguration customerConfiguration = aClass.getDeclaredAnnotation(CustomerConfiguration.class);
            if (Objects.nonNull(customerConfiguration)){
                //配置文件
                Method[] declaredMethods = aClass.getMethods();
                for (Method method:declaredMethods){
                    CustomerBean customerBean = method.getDeclaredAnnotation(CustomerBean.class);
                    if (Objects.nonNull(customerBean)){
                        //配置 bean 解决循环依赖
                        getBean(customerBean.name(),method,aClass);
                    }
                }
            }


        }

    }

    public static void getBean(String beanName,Method method,Class cl) {
            addSingletonFactory("beanA");
    }

    public static void source() {
        //A 中有B的引用 可以创建成功 BeanB为null
        BeanA beanA = new BeanA();
        System.out.println("创建 BeanA时 beanA==>" + beanA);
        //创建B
        BeanB beanB = new BeanB();
        //填充B的A引用
        beanB.setBeanA(beanA);
        System.out.println("创建BeanB，填充B中的A引用" + beanB);
        //填充A中B引用
        beanA.setBeanB(beanB);
        beanA.setName("beanA");
        System.out.println("创建完成 BeanA beanA ===>" + beanA);
        //创建完成
        beanA.say();
        beanB.say();
    }

    protected static void addSingletonFactory(String beanName) {
        synchronized (singletonObjects) {
            if (!singletonObjects.containsKey(beanName)) {
                earlySingletonObjects.remove(beanName);
                registeredSingletons.add(beanName);
            }
        }
    }

    public Object doCreateBean(String beanName) {

        return null;
    }


}