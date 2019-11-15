package config;

import annonation.Bean;
import annonation.Configuration;
import model.User;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-11-15 16:15
 **/
@Configuration
public class MyConfig {

    @Bean(name = "user")
    public User user(){
        return new User("张三",1);
    }

}
