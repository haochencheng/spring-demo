package pers.mvc.fatjar.config.tomcat;

import ch.qos.logback.ext.spring.web.WebLogbackConfigurer;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

/**
 * Created by liubo on 2016/1/25.
 */
public class RentContextLoaderListener extends ContextLoaderListener {
    public RentContextLoaderListener() {
        super();
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();
        System.out.println("日志文件加载：test");
        servletContext.setInitParameter(WebLogbackConfigurer.CONFIG_LOCATION_PARAM, "classpath:logback-test.xml");
        WebLogbackConfigurer.initLogging(servletContext);
        super.contextInitialized(event);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        WebLogbackConfigurer.shutdownLogging(event.getServletContext());
        super.contextDestroyed(event);
    }


}
