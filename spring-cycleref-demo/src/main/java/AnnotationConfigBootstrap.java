import bean.BeanA;
import bean.BeanB;
import config.SpringSingletonBeanConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-10 18:51
 **/
public class AnnotationConfigBootstrap {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext();
        context.register(SpringSingletonBeanConfig.class);
        context.refresh();
        BeanA bean = context.getBean(BeanA.class);
        bean.say();
        BeanB beanb = context.getBean(BeanB.class);
        beanb.say();
        beanb.getBeanA().say();
        context.destroy();
    }



}
