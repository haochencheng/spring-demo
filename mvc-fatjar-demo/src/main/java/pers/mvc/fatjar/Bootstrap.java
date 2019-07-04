package pers.mvc.fatjar;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import pers.mvc.fatjar.config.tomcat.EmbededContextConfig;
import pers.mvc.fatjar.config.tomcat.TomcatUtil;
import pers.mvc.fatjar.config.tomcat.WebXmlMountListener;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-03 19:28
 **/
public class Bootstrap {

    private static final int PORT = 6789;

    public static void main(String[] args)throws Exception  {
        String hostName = "localhost";
        String contextPath = "";
        String tomcatBaseDir = TomcatUtil.createTempDir("tomcat", PORT).getAbsolutePath();
        String contextDocBase = TomcatUtil.createTempDir("tomcat-docBase", PORT).getAbsolutePath();

        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(tomcatBaseDir);

        tomcat.setPort(PORT);
        tomcat.setHostname(hostName);

//        Context context = tomcat.addWebapp(tomcatBaseDir, contextDocBase);
        Context context = tomcat.addWebapp(tomcat.getHost(), contextPath, contextDocBase, new EmbededContextConfig());

        ClassLoader classLoader = Bootstrap.class.getClassLoader();
        context.setParentClassLoader(classLoader);

        // context load WEB-INF/web.xml from classpath
        context.addLifecycleListener(new WebXmlMountListener());

        tomcat.start();
        tomcat.getServer().await();

    }

}
