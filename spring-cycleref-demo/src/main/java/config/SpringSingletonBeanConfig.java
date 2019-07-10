package config;

import bean.BeanA;
import bean.BeanB;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-10 19:49
 **/
@Configuration
public class SpringSingletonBeanConfig {

    @Bean
    public BeanA beanA(){
        BeanA beanA = new BeanA();
        beanA.setName("beanA");
        beanA.setBeanB(beanB());
        return beanA;
    }

    @Bean
    public BeanB beanB(){
        BeanB beanB = new BeanB();
        beanB.setBeanA(beanA());
        return beanB;
    }

}
