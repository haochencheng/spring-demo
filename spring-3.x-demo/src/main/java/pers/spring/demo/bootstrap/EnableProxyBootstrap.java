package pers.spring.demo.bootstrap;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import pers.spring.demo.annotion.EnableProxy;
import pers.spring.demo.model.User;
import pers.spring.demo.proxy.ProxyService;

/**
 * 使用注解驱动开发
 * @EnableProxy ->
 * @description:
 * @author: haochencheng
 * @create: 2019-06-27 23:49
 **/
//注册spring组件 。必须@Component 或者 @Configuration 中的一个
//@Component
@Configuration
@EnableProxy(proxy = ProxyService.ProxyMode.PROXY)
public class EnableProxyBootstrap {

    /**
     *
     * @EnableProxy 驱动 -> ProxyImportSelector 根据 @EnableProxy中的proxy 属性 选择注入那个 ProxyService bean
     * @param args
     */
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext();
        context.register(EnableProxyBootstrap.class);
        context.refresh();
        ProxyService proxyService = context.getBean(ProxyService.class);
        ProxyService.ProxyMode proxyMode = proxyService.invoke();
        Assert.isTrue(proxyMode.equals(ProxyService.ProxyMode.PROXY));
        context.close();
    }

}
