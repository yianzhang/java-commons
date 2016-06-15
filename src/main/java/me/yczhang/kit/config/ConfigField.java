package me.yczhang.kit.config;

import java.lang.annotation.*;

/**
 * Created by YCZhang on 9/14/15.
 */

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ConfigField {

	/**
	 * 属性名
	 * @return
	 */
	public String propName();

	/**
	 * 默认值
	 * @return
	 */
	public String defValue() default "";

	/**
	 * 是否允许为null
	 * @return
	 */
	public boolean isAllowedNull() default true;

	/**
	 * 解析类的类名，需要实现ConfigPropParser
	 * @return
	 */
	public String parser() default "";

	/**
	 * 依赖的字段
	 * @return
	 */
	public String[] refFields() default {};

}
