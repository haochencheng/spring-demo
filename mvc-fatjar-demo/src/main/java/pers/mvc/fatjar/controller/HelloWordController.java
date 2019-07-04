package pers.mvc.fatjar.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-04 21:35
 **/
@RestController
public class HelloWordController {

    @GetMapping("/hello")
    @ResponseBody
    public String hello(){
        return "hello world";
    }

}
