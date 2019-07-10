package config;

import bean.BeanA;
import bean.BeanB;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-10 19:49
 **/
@Configuration
public class SpringSingletonBeanConfig implements ApplicationContextAware {

    @Bean
    public BeanA beanA(){
        BeanA beanA = new BeanA();
        beanA.setName("beanA");
        beanA.setBeanB(new BeanB());
        return beanA;
    }

    @Bean
    public BeanB beanB(){
        return new BeanB();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanA beanA = applicationContext.getBean(BeanA.class);
        BeanB beanB = applicationContext.getBean(BeanB.class);
        beanA.setBeanB(beanB);
        beanB.setBeanA(beanA);

    }
}
