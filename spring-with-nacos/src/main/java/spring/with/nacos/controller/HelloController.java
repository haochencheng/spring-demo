package spring.with.nacos.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-15 21:42
 **/
@RestController
public class HelloController {

    /**
     * 从nacos读取配置
     */
    @Value("${hello:nihao}")
    private String hello;

    @GetMapping("/")
    public String hello(){
        return hello;
    }

}
