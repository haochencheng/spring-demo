package annonation;

import java.lang.annotation.*;

/**
 *   @author haochencheng
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {

    String[] name() default {};

}
