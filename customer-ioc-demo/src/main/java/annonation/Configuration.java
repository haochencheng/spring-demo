package annonation;

import java.lang.annotation.*;

/**
 *   @author haochencheng
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Configuration {

    String[] name() default {};

}
