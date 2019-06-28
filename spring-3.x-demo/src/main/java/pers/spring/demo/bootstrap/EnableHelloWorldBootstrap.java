package pers.spring.demo.bootstrap;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import pers.spring.demo.annotion.EnableHelloWord;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-06-28 01:08
 **/
@Configuration
@EnableHelloWord
public class EnableHelloWorldBootstrap {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext();
        context.register(EnableHelloWorldBootstrap.class);
        context.refresh();
        String helloWorld = context.getBean("helloWorld",String.class);
        System.out.println(helloWorld);
        context.close();
    }


}
