/*
 * Copyright (c) 2020-2030  Xiamen Nascent Corporation. All rights reserved.
 *
 * https://www.nascent.cn
 *
 * 厦门南讯股份有限公司创立于2010年，是一家始终以技术和产品为驱动，帮助大消费领域企业提供客户资源管理（CRM）解决方案的公司。
 * 福建省厦门市软件园二期观日路22号501
 * 客服电话 400-009-2300
 * 电话 +86（592）5971731 传真 +86（592）5971710
 *
 * All source code copyright of this system belongs to Xiamen Nascent Co., Ltd.
 * Any organization or individual is not allowed to reprint, publish, disclose, embezzle, sell and use it for other illegal purposes without permission!
 */

package javax.annotation;

import java.lang.annotation.*;

/**
 * The <code>Resource</code> annotation marks a resource that is needed by the
 * application. This annotation may be applied to an application component class, or to
 * fields or methods of the component class. When the annotation is applied to a field or
 * method, the container will inject an instance of the requested resource into the
 * application component when the component is initialized. If the annotation is applied
 * to the component class, the annotation declares a resource that the application will
 * look up at runtime.
 * <p>
 * Even though this annotation is not marked <code>Inherited</code>, deployment tools are
 * required to examine all superclasses of any component class to discover all uses of
 * this annotation in all superclasses. All such annotation instances specify resources
 * that are needed by the application component. Note that this annotation may appear on
 * private fields and methods of superclasses; the container is required to perform
 * injection in these cases as well.
 * </p>
 *
 * @since 1.0.2
 * @author gaia
 */
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Resources.class)
public @interface Resource {

	/**
	 * The JNDI name of the resource. For field annotations, the default is the field
	 * name. For method annotations, the default is the JavaBeans property name
	 * corresponding to the method. For class annotations, there is no default and this
	 * must be specified.
	 * @return the JNDI name of the resource
	 */
	String name() default "";

	/**
	 * The name of the resource that the reference points to. It can link to any
	 * compatible resource using the global JNDI names.
	 *
	 * @since 1.7, Common Annotations 1.1
	 * @return the name of the resource that the reference points to
	 */

	String lookup() default "";

	/**
	 * The Java type of the resource. For field annotations, the default is the type of
	 * the field. For method annotations, the default is the type of the JavaBeans
	 * property. For class annotations, there is no default and this must be specified.
	 * @return the Java type of the resource
	 */
	Class<?> type() default Object.class;

	/**
	 * The two possible authentication types for a resource.
	 */
	enum AuthenticationType {

		/**
		 * Container authentication.
		 */
		CONTAINER,
		/**
		 * Application authentication.
		 */
		APPLICATION

	}

	/**
	 * The authentication type to use for this resource. This may be specified for
	 * resources representing a connection factory of any supported type, and must not be
	 * specified for resources of other types.
	 * @return the authentication type to use for this resource
	 */
	AuthenticationType authenticationType() default AuthenticationType.CONTAINER;

	/**
	 * Indicates whether this resource can be shared between this component and other
	 * components. This may be specified for resources representing a connection factory
	 * of any supported type, and must not be specified for resources of other types.
	 * @return whether this resource can be shared between this component and other
	 */
	boolean shareable() default true;

	/**
	 * A product-specific name that this resource should be mapped to. The
	 * <code>mappedName</code> element provides for mapping the resource reference to the
	 * name of a resource known to the applicaiton server. The mapped name could be of any
	 * form.
	 * <p>
	 * Application servers are not required to support any particular form or type of
	 * mapped name, nor the ability to use mapped names. The mapped name is
	 * product-dependent and often installation-dependent. No use of a mapped name is
	 * portable.
	 * </p>
	 * @return a product-specific name that this resource should be mapped to
	 */
	String mappedName() default "";

	/**
	 * Description of this resource. The description is expected to be in the default
	 * language of the system on which the application is deployed. The description can be
	 * presented to the Deployer to help in choosing the correct resource.
	 * @return description of this resource
	 */
	String description() default "";

}
