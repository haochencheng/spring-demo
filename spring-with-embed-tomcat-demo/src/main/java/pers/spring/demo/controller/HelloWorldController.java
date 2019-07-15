package pers.spring.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-02 22:02
 **/
@Controller
public class HelloWorldController {

    @RequestMapping("/aa")
    @ResponseBody
    public String hello(){
        return "hello";
    }


}
