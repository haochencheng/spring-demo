package pers.spring.demo.annotion;


import org.springframework.context.annotation.Import;
import pers.spring.demo.proxy.ProxyService;

import java.lang.annotation.*;

/**
 * 导入 @Import(ProxyImportSelector.class) 根据 proxy属性决定导入那个 ProxyService bean
 * @description:
 * @author: haochencheng
 * @create: 2019-06-27 23:49
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ProxyImportSelector.class)
public @interface EnableProxy {

    ProxyService.ProxyMode proxy() default ProxyService.ProxyMode.ASPECTJ;

}
