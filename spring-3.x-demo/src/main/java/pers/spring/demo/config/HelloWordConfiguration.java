package pers.spring.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-06-28 01:09
 **/
@Configuration
public class HelloWordConfiguration {

    @Bean
    public String helloWorld() { // 创建名为"helloWorld" String 类型的Bean
        return "Hello,World";
    }

}
