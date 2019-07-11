package config;

import annotation.CustomerBean;
import annotation.CustomerConfiguration;
import bean.BeanA;
import bean.BeanB;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-10 19:49
 **/
@Configuration
@CustomerConfiguration
public class SpringSingletonBeanConfig {

    private BeanA beanA;

    @Bean
    @CustomerBean(name = "beanA")
    public BeanA beanA(){
        beanA = new BeanA();
        beanA.setName("beanA");
        beanA.setBeanB(beanB());
        return beanA;
    }

    @Bean
    @CustomerBean(name = "beanB")
    public BeanB beanB(){
        BeanB beanB = new BeanB();
        beanB.setBeanA(beanA);
        return beanB;
    }

}
