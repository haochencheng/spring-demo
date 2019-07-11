package config;

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
public class SpringSingletonBeanConfig {

    private BeanA beanA;

    @Bean
    public BeanA beanA(){
        beanA = new BeanA();
        beanA.setName("beanA");
        beanA.setBeanB(beanB());
        return beanA;
    }

    @Bean
    public BeanB beanB(){
        BeanB beanB = new BeanB();
        beanB.setBeanA(beanA);
        return beanB;
    }

}
