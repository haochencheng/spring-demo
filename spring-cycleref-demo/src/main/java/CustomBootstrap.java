import annotation.CustomerBean;
import annotation.CustomerComponentScan;
import annotation.CustomerConfiguration;
import bean.BeanA;
import bean.BeanB;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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

    public static void main(String[] args) throws Exception {
        //初始化 容器
        init(CustomBootstrap.class);
        BeanA beanA = (BeanA)singletonObjects.get("beanA");
        BeanB beanB = (BeanB)singletonObjects.get("beanB");
        System.out.println(beanA==beanB.getBeanA());
    }

    public static void init(Class<?> cl) throws Exception {
        //加载扫描指定配置下文件
        CustomerComponentScan customerComponentScan = cl.getDeclaredAnnotation(CustomerComponentScan.class);
        String scan = customerComponentScan.value();
        ClassLoader classLoader = cl.getClassLoader();
        Enumeration<URL> resources;
        try {
            resources = classLoader.getResources(scan);
        } catch (IOException e) {
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
            //读取 指定配置类注解
            CustomerConfiguration customerConfiguration = aClass.getDeclaredAnnotation(CustomerConfiguration.class);
            if (Objects.nonNull(customerConfiguration)){
                //配置文件
                Method[] declaredMethods = aClass.getMethods();
                for (Method method:declaredMethods){
                    CustomerBean customerBean = method.getDeclaredAnnotation(CustomerBean.class);
                    if (Objects.nonNull(customerBean)){
                        //配置 bean 解决循环依赖 直接加载无法解决 循环依赖问题 ，加载可以成功 但是 不是一个引用
                        getBean(customerBean.name(),method,aClass);
                    }
                }
            }
        }
    }

    public static void getBean(String beanName,Method method,Class cl) throws InstantiationException, IllegalAccessException, InvocationTargetException {
            addSingletonFactory(beanName,method,cl);
    }

    protected static void addSingletonFactory(String beanName,Method method,Class cl) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        synchronized (singletonObjects) {
            if (!singletonObjects.containsKey(beanName)) {
                Object invoke = method.invoke(cl.newInstance(), null);
                singletonObjects.putIfAbsent(beanName,invoke);
            }
        }
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



}
