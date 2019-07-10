package config;

import bean.BeanA;
import bean.BeanB;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-10 19:53
 **/
public class CustomBeanConfig {

    private static final Map<String,Object> singletonObjects=new ConcurrentHashMap<>();
    private static final Map<String,Object> earlySingletonObjects=new HashMap<>();

    private static final Set<String> registeredSingletons = new LinkedHashSet<>(64);

    public static void main(String[] args) {
        source();
        synchronized (singletonObjects){
            addSingletonFactory("beanA");

        }

    }

    public static void source(){
        //A 中有B的引用 可以创建成功 BeanB为null
        BeanA beanA=new BeanA();
        System.out.println("创建 BeanA时 beanA==>"+beanA);
        //创建B
        BeanB beanB=new BeanB();
        //填充B的A引用
        beanB.setBeanA(beanA);
        System.out.println("创建BeanB，填充B中的A引用"+beanB);
        //填充A中B引用
        beanA.setBeanB(beanB);
        beanA.setName("beanA");
        System.out.println("创建完成 BeanA beanA ===>"+beanA);
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

    public Object doCreateBean(String beanName){

        return null;
    }



}
