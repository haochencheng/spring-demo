package classloader;

import classloader.Hello1;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-08 18:22
 **/
public class Hello {

    private String name;

    private Hello1 hello1;

    public Hello() {
        this.hello1 = new Hello1();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Hello1 getHello1() {
        return hello1;
    }

    public void setHello1(Hello1 hello1) {
        this.hello1 = hello1;
    }

    public void say(){
        System.out.println("hello");
    }
}
