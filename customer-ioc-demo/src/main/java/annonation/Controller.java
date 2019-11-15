package annonation;

import java.lang.annotation.*;

/**
 *   @author haochencheng
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {

    boolean required() default true;

}
