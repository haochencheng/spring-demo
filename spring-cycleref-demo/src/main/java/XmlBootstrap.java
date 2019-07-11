import bean.BeanA;
import bean.BeanB;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-10 18:51
 **/
public class XmlBootstrap {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("spring.xml");
        context.refresh();
        BeanA bean = context.getBean(BeanA.class);
        bean.say();
        BeanB beanb = context.getBean(BeanB.class);
        beanb.say();
        beanb.getBeanA().say();
        context.destroy();
    }



}
