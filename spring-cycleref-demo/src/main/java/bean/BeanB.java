package bean;

import java.util.Objects;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-10 18:52
 **/
public class BeanB {

    private BeanA beanA;

    public BeanA getBeanA() {
        return beanA;
    }

    public void setBeanA(BeanA beanA) {
        this.beanA = beanA;
    }

    public void say(){
        System.out.println("i'm bean b");
    }

    @Override
    public String toString() {
        return "BeanB{" +
                "beanA.name=" +
                '}';
    }
}
