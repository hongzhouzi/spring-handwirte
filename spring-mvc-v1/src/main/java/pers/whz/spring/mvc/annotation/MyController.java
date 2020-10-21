package pers.whz.spring.mvc.annotation;

import java.lang.annotation.*;

/**
 * @author hongzhou.wei
 * @date 2020/10/20
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyController {
    String value() default "";
}
