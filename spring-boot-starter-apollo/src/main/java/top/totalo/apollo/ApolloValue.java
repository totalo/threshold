package top.totalo.apollo;

import java.lang.annotation.*;

/**
 * Apollo value.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApolloValue {

    /**
     * apollo key
     */
    String key();
    
    /**
     * defaultValue
     */
    String defaultValue() default "";

    /**
     * apollo namespace
     */
    String namespace() default "application";
}
