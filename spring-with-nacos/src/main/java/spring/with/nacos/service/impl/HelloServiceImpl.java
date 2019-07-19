package spring.with.nacos.service.impl;

import org.springframework.stereotype.Service;
import spring.with.nacos.service.HelloService;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-17 14:25
 **/
@Service
public class HelloServiceImpl implements HelloService {

    @Override
    public String say(String str) {
        System.out.println(str);
        return str;
    }
}
