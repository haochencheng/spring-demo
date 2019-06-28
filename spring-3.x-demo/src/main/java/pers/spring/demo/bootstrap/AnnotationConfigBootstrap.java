package pers.spring.demo.bootstrap;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pers.spring.demo.config.SpringContextConfiguration;
import pers.spring.demo.model.User;

/**
 * Annotation 配置驱动引导程序
 * @description:
 * @author: haochencheng
 * @create: 2019-06-27 23:49
 **/
public class AnnotationConfigBootstrap {

    /**
     * 注解配置启动 注册@Configuration SpringContextConfiguration -> User(bean) 到上下文中
     * @param args
     */
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext();
        context.register(SpringContextConfiguration.class);
        context.refresh();
        User user = context.getBean(User.class);
        System.out.printf("user.getName() = %s \n", user.getName());
        context.close();
    }

}
