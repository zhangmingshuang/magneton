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

package javax.annotation.sql;

import java.lang.annotation.*;

/**
 * Annotation used to define a container <code>DataSource</code> to be registered with
 * JNDI. The <code>DataSource</code> may be configured by setting the annotation elements
 * for commonly used <code>DataSource</code> properties. Additional standard and
 * vendor-specific properties may be specified using the <code>properties</code> element.
 * <p>
 *
 * The data source will be registered under the name specified in the <code>name</code>
 * element. It may be defined to be in any valid Jakarta EE namespace, which will
 * determine the accessibility of the data source from other components.
 * <p>
 * A JDBC driver implementation class of the appropriate type, either
 * <code>DataSource</code>, <code>ConnectionPoolDataSource</code>, or
 * <code>XADataSource</code>, must be indicated by the <code>className</code> element. The
 * availability of the driver class will be assumed at runtime.
 * <p>
 * DataSource properties should not be specified more than once. If the url annotation
 * element contains a DataSource property that was also specified using the corresponding
 * annotation element or was specified in the properties annotation element, the
 * precedence order is undefined and implementation specific:
 * <p>
 * <pre>
 *   &#064;DataSourceDefinition(name="java:global/MyApp/MyDataSource",
 *      className="org.apache.derby.jdbc.ClientDataSource",
 *      url="jdbc:derby://localhost:1527/myDB;user=bill",
 *      user="lance",
 *      password="secret",
 *      databaseName="testDB",
 *      serverName="luckydog"
 *   )// DO NOT DO THIS!!!
 * </pre>
 * <p>
 * In the above example, the <code>databaseName</code>, <code>user</code> and
 * <code>serverName</code> properties were specified as part of the <code>url</code>
 * property and using the corresponding annotation elements. This should be avoided.
 * <p>
 * If the <code>properties</code> annotation element is used and contains a DataSource
 * property that was also specified using the corresponding annotation element, the
 * annotation element value takes precedence. For example:
 * <p>
 * <pre>
 *   &#064;DataSourceDefinition(name="java:global/MyApp/MyDataSource",
 *      className="org.apache.derby.jdbc.ClientDataSource",
 *      user="lance",
 *      password="secret",
 *      databaseName="testDB",
 *      serverName="luckydog",
 *       properties= {"databaseName=myDB", "databaseProp=doThis"}
 *   )// DO NOT DO THIS!!!
 * </pre>
 * <p>
 * This would result in the following values being used when configuring the DataSource:
 * <ul>
 * <li>serverName=luckydog</li>
 * <li>portNumber=1527</li>
 * <li>databaseName=testDB</li>
 * <li>user=lance</li>
 * <li>password=secret</li>
 * <li>databaseProp=doThis</li>
 * </ul>
 * <p>
 * Vendors are not required to support properties that do not normally apply to a specific
 * data source type. For example, specifying the <code>transactional</code> property to be
 * <code>true</code> but supplying a value for <code>className</code> that implements a
 * data source class other than <code>XADataSource</code> may not be supported.
 * <p>
 * Vendor-specific properties may be combined with or used to override standard data
 * source properties defined using this annotation.
 * <p>
 * <code>DataSource</code> properties that are specified and are not supported in a given
 * configuration or cannot be mapped to a vendor specific configuration property may be
 * ignored.
 * <p>
 * Examples: <br>
 * <pre>
 *   &#064;DataSourceDefinition(name="java:global/MyApp/MyDataSource",
 *      className="com.foobar.MyDataSource",
 *      portNumber=6689,
 *      serverName="myserver.com",
 *      user="lance",
 *      password="secret"
 *   )
 *
 * </pre>
 * <p>
 * Using a <code>URL</code>: <br>
 * <pre>
 *  &#064;DataSourceDefinition(name="java:global/MyApp/MyDataSource",
 *    className="org.apache.derby.jdbc.ClientDataSource",
 *    url="jdbc:derby://localhost:1527/myDB",
 *    user="lance",
 *    password="secret"
 * )
 * </pre>
 * <p>
 * An example lookup of the DataSource from an EJB: <pre>
 * &#064;Stateless
 * public class MyStatelessEJB {
 *   &#064;Resource(lookup="java:global/MyApp/myDataSource")
 *    DataSource myDB;
 *      ...
 * }
 * </pre>
 * <p>
 * @see javax.sql.DataSource
 * @see javax.sql.XADataSource
 * @see javax.sql.ConnectionPoolDataSource
 * @since 1.0.2
 * @author gaia
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(DataSourceDefinitions.class)
public @interface DataSourceDefinition {

	/**
	 * JNDI name by which the data source will be registered.
	 * @since 1.1
	 * @return the JNDI name
	 */
	String name();

	/**
	 * Name of a DataSource class that implements <code>javax.sql.DataSource</code> or
	 * <code>javax.sql.XADataSource</code> or
	 * <code>javax.sql.ConnectionPoolDataSource</code>.
	 * @since 1.1
	 * @return the class name
	 */
	String className();

	/**
	 * Description of this data source
	 * @since 1.1
	 * @return the description
	 */
	String description() default "";

	/**
	 * A JDBC URL. If the <code>url</code> annotation element contains a DataSource
	 * property that was also specified using the corresponding annotation element, the
	 * precedence order is undefined and implementation specific.
	 * @since 1.1
	 * @return the JDBC URL
	 */
	String url() default "";

	/**
	 * User name to use for connection authentication.
	 * @since 1.1
	 * @return the user name
	 */
	String user() default "";

	/**
	 * Password to use for connection authentication.
	 * @since 1.1
	 * @return the password
	 */
	String password() default "";

	/**
	 * Name of a database on a server.
	 * @since 1.1
	 * @return the database name
	 */
	String databaseName() default "";

	/**
	 * Port number where a server is listening for requests.
	 * @since 1.1
	 * @return the port number
	 */
	int portNumber() default -1;

	/**
	 * Database server name.
	 * @since 1.1
	 * @return the server name
	 */
	String serverName() default "localhost";

	/**
	 * Isolation level for connections. The Isolation level must be one of the following:
	 * <p>
	 * <ul>
	 * <li>Connection.TRANSACTION_NONE,
	 * <li>Connection.TRANSACTION_READ_ UNCOMMITTED,
	 * <li>Connection.TRANSACTION_READ_COMMITTED,
	 * <li>Connection.TRANSACTION_REPEATABLE_READ,
	 * <li>Connection.TRANSACTION_SERIALIZABLE
	 * </ul>
	 * <p>
	 * Default is vendor-specific.
	 * @since 1.1
	 * @return the isolation level for connects
	 */
	int isolationLevel() default -1;

	/**
	 * Set to <code>false</code> if connections should not participate in transactions.
	 * <p>
	 * Default is to enlist in a transaction when one is active or becomes active.
	 * @since 1.1
	 * @return <code>true</code> if connections should participate in transactions
	 */
	boolean transactional() default true;

	/**
	 * Number of connections that should be created when a connection pool is initialized.
	 * <p>
	 * Default is vendor-specific
	 * @since 1.1
	 * @return the initial pool size
	 */
	int initialPoolSize() default -1;

	/**
	 * Maximum number of connections that should be concurrently allocated for a
	 * connection pool.
	 * <p>
	 * Default is vendor-specific.
	 * @since 1.1
	 * @return the maximum pool size
	 */
	int maxPoolSize() default -1;

	/**
	 * Minimum number of connections that should be allocated for a connection pool.
	 * <p>
	 * Default is vendor-specific.
	 * @since 1.1
	 * @return the minimum pool size
	 */
	int minPoolSize() default -1;

	/**
	 * The number of seconds that a physical connection should remain unused in the pool
	 * before the connection is closed for a connection pool.
	 * <p>
	 * Default is vendor-specific
	 * @since 1.1
	 * @return the maximum idle time
	 */
	int maxIdleTime() default -1;

	/**
	 * The total number of statements that a connection pool should keep open. A value of
	 * 0 indicates that the caching of statements is disabled for a connection pool.
	 * <p>
	 * Default is vendor-specific
	 * @since 1.1
	 * @return the maximum number of statements
	 */
	int maxStatements() default -1;

	/**
	 * Used to specify vendor-specific properties and less commonly used
	 * <code>DataSource</code> properties such as:
	 * <p>
	 * <ul>
	 * <li>dataSourceName
	 * <li>networkProtocol
	 * <li>propertyCycle
	 * <li>roleName
	 * </ul>
	 * <p>
	 * Properties are specified using the format: <i>propertyName=propertyValue</i> with
	 * one property per array element.
	 * <p>
	 * If a DataSource property is specified in the <code>properties</code> element and
	 * the annotation element for the property is also specified, the annotation element
	 * value takes precedence.
	 * @since 1.1
	 * @return the properties
	 */
	String[] properties() default {};

	/**
	 * Sets the maximum time in seconds that this data source will wait while attempting
	 * to connect to a database. A value of zero specifies that the timeout is the default
	 * system timeout if there is one; otherwise, it specifies that there is no timeout.
	 * <p>
	 * Default is vendor-specific.
	 * @since 1.1
	 * @return the login timeout
	 */
	int loginTimeout() default 0;

}
