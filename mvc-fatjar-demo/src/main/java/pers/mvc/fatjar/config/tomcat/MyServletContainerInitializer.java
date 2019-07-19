package pers.mvc.fatjar.config.tomcat;

import pers.mvc.fatjar.web.MyWebApplicationInitializer;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.util.*;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-07 12:43
 **/
@HandlesTypes(MyWebApplicationInitializer.class)
public class MyServletContainerInitializer implements ServletContainerInitializer {

    /**
     * ServletContainerInitializer servlet容器初始化 调用 应用程序初始化
     * @param webAppInitializerClasses @see ServletContainerInitializer 实现类中 @HandlesTypes 的所有实现类
     * @param ctx ServletContext
     * @throws ServletException
     */
    @Override
    public void onStartup(Set<Class<?>> webAppInitializerClasses, ServletContext ctx) throws ServletException {
        List<MyWebApplicationInitializer> initializers = new LinkedList<>();
        if (Objects.nonNull(webAppInitializerClasses)){
            //实例化 web应用程序 初始化类
            for (Class<?> waiClass : webAppInitializerClasses) {
                try {
                    initializers.add((MyWebApplicationInitializer)waiClass.newInstance());
                } catch (Throwable ex) {
                    throw new ServletException("Failed to instantiate WebApplicationInitializer class", ex);
                }
            }
        }
        //执行 web应用程序 初始化操作
        for (MyWebApplicationInitializer initializer : initializers) {
            initializer.onStartup(ctx);
        }
    }
}
