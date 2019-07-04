package pers.mvc.fatjar.config.tomcat;

import org.apache.catalina.*;
import org.apache.catalina.webresources.StandardRoot;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-04 13:58
 **/
public class WebXmlMountListener implements LifecycleListener {

        @Override
        public void lifecycleEvent(LifecycleEvent event) {
            if (event.getType().equals(Lifecycle.BEFORE_START_EVENT)) {
                Context context = (Context) event.getLifecycle();
                WebResourceRoot resources = context.getResources();
                if (resources == null) {
                    resources = new StandardRoot(context);
                    context.setResources(resources);
                }

                /**
                 * <pre>
                 * when run as embeded tomcat, context.getParentClassLoader() is AppClassLoader,
                 * so it can load "WEB-INF/web.xml" from app classpath.
                 * </pre>
                 */
                URL resource = context.getParentClassLoader().getResource("WEB-INF/web.xml");
                if (resource != null) {
                    String webXmlUrlString = resource.toString();
                    URL root;
                    try {
                        root = new URL(webXmlUrlString.substring(0, webXmlUrlString.length() - "WEB-INF/web.xml".length()));
                        resources.createWebResourceSet(WebResourceRoot.ResourceSetType.RESOURCE_JAR, "/WEB-INF", root, "/WEB-INF");
                    } catch (MalformedURLException e) {
                        // ignore
                    }
                }
            }

        }

    }
