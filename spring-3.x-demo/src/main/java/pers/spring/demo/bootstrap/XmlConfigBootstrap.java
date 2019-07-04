package pers.spring.demo.bootstrap;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.util.Assert;
import pers.spring.demo.config.SpringContextConfiguration;
import pers.spring.demo.model.User;

/**
 * XML 配置驱动引导程序
 * @description:
 * @author: haochencheng
 * @create: 2019-06-28 01:14
 **/
public class XmlConfigBootstrap {

    public static void main(String[] args) {
        // 构建 XML 配置驱动 Spring 上下文
        /**
         * AbstractXmlApplicationContext
         *  \-ClassPathXmlApplicationContext 从classpath相对路径读取xml配置
         *  \-FileSystemXmlApplicationContext 支持从文件系统绝对路径读取xml配置
         */
//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
////         设置 XML 配置文件的位置
//        context.setConfigLocation("classpath:context.xml");
        FileSystemXmlApplicationContext context=new FileSystemXmlApplicationContext();
        //如果路径不存在报错 java.io.FileNotFoundException
        context.setConfigLocation("file:/Users/haochencheng/Workspace/java/demo/spring-demo/src/main/resources/context.txt");
        // 启动上下文
        context.refresh();
        // 获取名称为 "user" Bean 对象
        User user = context.getBean("user", User.class);
        Assert.isTrue("cc".equals(user.getName()));
        // 关闭 Spring 上下文
        context.close();
    }

}
