package pers.spring.demo.bootstrap;

import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;
import pers.spring.demo.model.User;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-06-28 01:32
 **/
public class GenericBootstrap {

    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(context);
        xmlReader.loadBeanDefinitions(new ClassPathResource("context.xml"));
        context.refresh();
        User user = context.getBean("user", User.class);
        Assert.isTrue("cc".equals(user.getName()));
        context.close();
    }
}
