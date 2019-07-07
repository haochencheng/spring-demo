package pers.mvc.fatjar.web.support;

import pers.mvc.fatjar.web.MyWebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-07 12:56
 **/
public class MyContextLoaderInitializer implements MyWebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.out.println("MyContextLoaderInitializer===");
    }

}
