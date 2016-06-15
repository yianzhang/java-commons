package me.yczhang.kit.bank;

import java.lang.annotation.*;

/**
 * Created by YCZhang on 2/3/16.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface BeanField {

	String id();
	boolean isAllowNull() default true;

}
