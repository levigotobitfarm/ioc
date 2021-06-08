package org.levi.learn.annotation;

import java.lang.annotation.*;

/**
 * @author DevCenter
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Transactional {

}
