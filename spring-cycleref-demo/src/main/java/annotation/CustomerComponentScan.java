package annotation;

import java.lang.annotation.*;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-11 11:30
 **/
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomerComponentScan {

    String value() default "";

}
