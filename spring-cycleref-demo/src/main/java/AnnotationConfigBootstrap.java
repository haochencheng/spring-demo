import bean.BeanA;
import bean.BeanB;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-10 18:51
 **/
public class AnnotationConfigBootstrap {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext();
//        context.register(SpringSingletonBeanConfig.class);
        context.scan("config");
        context.refresh();
        BeanA beanA = context.getBean(BeanA.class);
        beanA.say();
        BeanB beanB = beanA.getBeanB();
        beanB.say();
        BeanB beanb = context.getBean(BeanB.class);
        System.out.println(beanB==beanb);
        context.destroy();
    }



}
