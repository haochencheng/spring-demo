package pers.spring.demo.annotion;

import org.springframework.context.annotation.Import;
import pers.spring.demo.config.HelloWordConfiguration;

import java.lang.annotation.*;

/**
 * 导入HelloWordConfiguration.class
 * @description:
 * @author: haochencheng
 * @create: 2019-06-28 01:13
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(HelloWordConfiguration.class)
public @interface EnableHelloWord {
}
