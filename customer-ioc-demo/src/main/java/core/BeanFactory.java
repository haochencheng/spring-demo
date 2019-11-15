package core;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-11-15 16:16
 **/
public interface BeanFactory {

    Object getBean(String name);

    <T> T getBean(String name, Class<T> requiredType);

    void registerSingleton(String beanName, Object singletonObject);

    boolean containsBean(String name);

}
