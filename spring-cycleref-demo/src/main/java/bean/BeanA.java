package bean;

import bean.BeanB;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-10 18:52
 **/
public class BeanA {

    private String name;

    private BeanB beanB;

    public BeanB getBeanB() {
        return beanB;
    }

    public void setBeanB(BeanB beanB) {
        this.beanB = beanB;
    }

    public void say(){
        System.out.println("i'm bean a");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
