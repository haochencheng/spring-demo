package pers.spring.demo.annotion;

import com.sun.net.httpserver.HttpServer;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import pers.spring.demo.proxy.AspectJPServiceImpl;
import pers.spring.demo.proxy.ProxyService;
import pers.spring.demo.proxy.ProxyServiceServiceImpl;

import java.util.Map;

/**
 * spring context启动后调用该实现类 selectImports 注入方法中返回的bean名称集合 String[]
 *
 * @description:
 * @author: haochencheng
 * @create: 2019-06-28 00:13
 **/
public class ProxyImportSelector implements ImportSelector {

    /**
     * 返回指定bean名称
     *
     * @param importingClassMetadata
     * @return
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        //获取@EnableProxy 中配置的 proxy 属性 导入对应的bean
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(EnableProxy.class.getName());
        ProxyService.ProxyMode proxyMode = (ProxyService.ProxyMode) annotationAttributes.get("proxy");
        String[] importClassNames = new String[0];
        switch (proxyMode) {
            case ASPECTJ:
                importClassNames = new String[]{AspectJPServiceImpl.class.getName()};
                break;
            case PROXY:
                importClassNames = new String[]{ProxyServiceServiceImpl.class.getName()};
                break;
        }
        return importClassNames;
    }
}
