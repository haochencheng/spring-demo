package pers.mvc.fatjar.config.tomcat;

import pers.mvc.fatjar.web.MyWebApplicationInitializer;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.annotation.HandlesTypes;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-07 12:43
 **/
@HandlesTypes(MyWebApplicationInitializer.class)
public class MyServletContainerInitializer1 extends MyServletContainerInitializer implements ServletContainerInitializer {

}
