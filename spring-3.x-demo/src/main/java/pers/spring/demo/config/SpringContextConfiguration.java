package pers.spring.demo.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.*;
import pers.spring.demo.model.User;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-06-27 23:51
 **/
@Configuration
//@ComponentScan(basePackages = "pers.spring.demo")
@Profile("!prod")
public class SpringContextConfiguration {

    @Lazy
    @Primary
    @DependsOn("springContextConfiguration") // 依赖 "springContextConfiguration"
    @Bean(name = "user") // Bean 名称为 "user"
    @Role(BeanDefinition.ROLE_APPLICATION) // 为应用
    public User user() {
        User user = new User();
        user.setName("cc");
        return user;
    }

}
