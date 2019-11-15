package core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-11-15 16:20
 **/
public class AnnotationBeanFactoryImpl implements BeanFactory {

    private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>(16);

    @Override
    public Object getBean(String name) {
        return factoryBeanObjectCache.get(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        Object bean = getBean(name);
        return (T) bean;
    }

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        factoryBeanObjectCache.put(beanName,singletonObject);
    }

    @Override
    public boolean containsBean(String name) {
        return factoryBeanObjectCache.containsKey(name);
    }


}
