package spring.with.nacos.controller;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.with.nacos.service.HelloService;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-15 21:42
 **/
@RestController
public class HelloController {

    @NacosInjected
    private NamingService namingService;

    /**
     * 从nacos读取配置
     */
    @Value("${hello:nihao}")
    private String hello;

    @GetMapping("/")
    public String hello(){
        return hello;
    }

    @GetMapping("/discover")
    public String discover(){
        Instance instance;
        return "ok";
    }

}
