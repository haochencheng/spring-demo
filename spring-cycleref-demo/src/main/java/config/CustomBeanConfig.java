package config;

import annotation.CustomerBean;
import annotation.CustomerConfiguration;
import bean.BeanA;
import bean.BeanB;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-10 19:53
 **/
@CustomerConfiguration
public class CustomBeanConfig {

    @CustomerBean(name = "beanA")
    public BeanA beanA(){
        BeanA beanA = new BeanA();
        beanA.setName("beanA");
        beanA.setBeanB(new BeanB());
        return beanA;
    }

    @CustomerBean(name = "beanB")
    public BeanB beanB(){
        BeanB beanB = new BeanB();
        beanB.setBeanA(new BeanA());
        return beanB;
    }

}
