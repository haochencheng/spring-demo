package spring.with.nacos.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-13 22:55
 **/
@Configuration
@PropertySource(value="classpath:application.properties")
@Import(ImportPropertiesBeanDefinitionRegistrar.class)
public class CustomerSpringConfiguration  implements BeanDefinitionRegistryPostProcessor, Ordered {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }


    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
