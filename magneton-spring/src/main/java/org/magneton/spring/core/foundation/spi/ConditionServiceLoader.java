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

package org.magneton.spring.core.foundation.spi;

import cn.hutool.core.util.ClassLoaderUtil;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;

/**
 * A simple service-provider loading facility.
 *
 * <p>
 * A <i>service</i> is a well-known set of interfaces and (usually abstract) classes. A
 * <i>service provider</i> is a specific implementation of a service. The classes in a
 * provider typically implement the interfaces and subclass the classes defined in the
 * service itself. Service providers can be installed in an implementation of the Java
 * platform in the form of extensions, that is, jar files placed into any of the usual
 * extension directories. Providers can also be made available by adding them to the
 * application's class path or by some other platform-specific means.
 *
 * <p>
 * For the purpose of loading, a service is represented by a single type, that is, a
 * single interface or abstract class. (A concrete class can be used, but this is not
 * recommended.) A provider of a given service contains one or more concrete classes that
 * extend this <i>service type</i> with data and code specific to the provider. The
 * <i>provider class</i> is typically not the entire provider itself but rather a proxy
 * which contains enough information to decide whether the provider is able to satisfy a
 * particular request together with code that can create the actual provider on demand.
 * The details of provider classes tend to be highly service-specific; no single class or
 * interface could possibly unify them, so no such type is defined here. The only
 * requirement enforced by this facility is that provider classes must have a
 * zero-argument constructor so that they can be instantiated during loading.
 *
 * <p>
 * <a name="format"> A service provider is identified by placing a
 * <i>provider-configuration file</i> in the resource directory
 * <tt>META-INF/services</tt>.</a> The file's name is the fully-qualified
 * <a href="../lang/ClassLoader.html#name">binary name</a> of the service's type. The file
 * contains a list of fully-qualified binary names of concrete provider classes, one per
 * line. Space and tab characters surrounding each name, as well as blank lines, are
 * ignored. The comment character is <tt>'#'</tt> (<tt>'&#92;u0023'</tt>,
 * <font style="font-size:smaller;">NUMBER SIGN</font>); on each line all characters
 * following the first comment character are ignored. The file must be encoded in UTF-8.
 *
 * <p>
 * If a particular concrete provider class is named in more than one configuration file,
 * or is named in the same configuration file more than once, then the duplicates are
 * ignored. The configuration file naming a particular provider need not be in the same
 * jar file or other distribution unit as the provider itself. The provider must be
 * accessible from the same class loader that was initially queried to locate the
 * configuration file; note that this is not necessarily the class loader from which the
 * file was actually loaded.
 *
 * <p>
 * Providers are located and instantiated lazily, that is, on demand. A service loader
 * maintains a cache of the providers that have been loaded so far. Each invocation of the
 * {@link #iterator iterator} method returns an iterator that first yields all of the
 * elements of the cache, in instantiation order, and then lazily locates and instantiates
 * any remaining providers, adding each one to the cache in turn. The cache can be cleared
 * via the {@link #reload reload} method.
 *
 * <p>
 * Service loaders always execute in the security context of the caller. Trusted system
 * code should typically invoke the methods in this class, and the methods of the
 * iterators which they return, from within a privileged security context.
 *
 * <p>
 * Instances of this class are not safe for use by multiple concurrent threads.
 *
 * <p>
 * Unless otherwise specified, passing a <tt>null</tt> argument to any method in this
 * class will cause a {@link NullPointerException} to be thrown.
 *
 * <p>
 * <span style="font-weight: bold; padding-right: 1em">Example</span> Suppose we have a
 * service type <tt>com.example.CodecSet</tt> which is intended to represent sets of
 * encoder/decoder pairs for some protocol. In this case it is an abstract class with two
 * abstract methods:
 *
 * <blockquote>
 *
 * <pre>
 * public abstract Encoder getEncoder(String encodingName);
 * public abstract Decoder getDecoder(String encodingName);</pre>
 *
 * </blockquote>
 *
 * Each method returns an appropriate object or <tt>null</tt> if the provider does not
 * support the given encoding. Typical providers support more than one encoding.
 *
 * <p>
 * If <tt>com.example.impl.StandardCodecs</tt> is an implementation of the
 * <tt>CodecSet</tt> service then its jar file also contains a file named
 *
 * <blockquote>
 *
 * <pre>
 * META-INF/services/com.example.CodecSet</pre>
 *
 * </blockquote>
 *
 * <p>
 * This file contains the single line:
 *
 * <blockquote>
 *
 * <pre>
 * com.example.impl.StandardCodecs    # Standard codecs</pre>
 *
 * </blockquote>
 *
 * <p>
 * The <tt>CodecSet</tt> class creates and saves a single service instance at
 * initialization:
 *
 * <blockquote>
 *
 * <pre>
 * private static ServiceLoader&lt;CodecSet&gt; codecSetLoader
 *     = ServiceLoader.load(CodecSet.class);</pre>
 *
 * </blockquote>
 *
 * <p>
 * To locate an encoder for a given encoding name it defines a static factory method which
 * iterates through the known and available providers, returning only when it has located
 * a suitable encoder or has run out of providers.
 *
 * <blockquote>
 *
 * <pre>
 * public static Encoder getEncoder(String encodingName) {
 *     for (CodecSet cp : codecSetLoader) {
 *         Encoder enc = cp.getEncoder(encodingName);
 *         if (enc != null)
 *             return enc;
 *     }
 *     return null;
 * }</pre>
 *
 * </blockquote>
 *
 * <p>
 * A <tt>getDecoder</tt> method is defined similarly.
 *
 * <p>
 * <span style="font-weight: bold; padding-right: 1em">Usage Note</span> If the class path
 * of a class loader that is used for provider loading includes remote network URLs then
 * those URLs will be dereferenced in the process of searching for provider-configuration
 * files.
 *
 * <p>
 * This activity is normal, although it may cause puzzling entries to be created in
 * web-server logs. If a web server is not configured correctly, however, then this
 * activity may cause the provider-loading algorithm to fail spuriously.
 *
 * <p>
 * A web server should return an HTTP 404 (Not Found) response when a requested resource
 * does not exist. Sometimes, however, web servers are erroneously configured to return an
 * HTTP 200 (OK) response along with a helpful HTML error page in such cases. This will
 * cause a {@link ServiceConfigurationError} to be thrown when this class attempts to
 * parse the HTML page as a provider-configuration file. The best solution to this problem
 * is to fix the misconfigured web server to return the correct response code (HTTP 404)
 * along with the HTML error page.
 *
 * @param <S> The type of the service to be loaded by this loader
 * @author Mark Reinhold
 * @since 1.6
 */
public final class ConditionServiceLoader<S> implements Iterable<Class<S>> {

	private static final String PREFIX = "META-INF/services/";

	/**
	 * The class or interface representing the service being loaded
	 */
	private final Class<S> service;

	/**
	 * The class loader used to locate, load, and instantiate providers
	 */
	private final ClassLoader loader;

	/**
	 * The access control context taken when the ServiceLoader is created
	 */
	private final AccessControlContext acc;

	/**
	 * Cached providers, in instantiation order
	 */
	private final LinkedHashMap<String, Class<S>> providers = new LinkedHashMap<>();

	/**
	 * Cached providers, in instantiation order
	 */
	private final LinkedHashMap<Class<S>, List<S>> providersInstance = new LinkedHashMap<>();

	/**
	 * The current lazy-lookup iterator
	 */
	private LazyIterator lookupIterator;

	private ConditionServiceLoader(Class<S> svc, ClassLoader cl, boolean instantiation) {
		this.service = Objects.requireNonNull(svc, "Service interface cannot be null");
		this.loader = (cl == null) ? ClassLoader.getSystemClassLoader() : cl;
		this.acc = (System.getSecurityManager() != null) ? AccessController.getContext() : null;
		this.reload(instantiation);
	}

	private static void fail(Class<?> service, String msg, Throwable cause) throws ServiceConfigurationError {
		throw new ServiceConfigurationError(service.getName() + ": " + msg, cause);
	}

	private static void fail(Class<?> service, String msg) throws ServiceConfigurationError {
		throw new ServiceConfigurationError(service.getName() + ": " + msg);
	}

	private static void fail(Class<?> service, URL u, int line, String msg) throws ServiceConfigurationError {
		fail(service, u + ":" + line + ": " + msg);
	}

	/**
	 * Creates a new service loader for the given service type and class loader.
	 * @param <S> the class of the service type
	 * @param service The interface or abstract class representing the service
	 * @param loader The class loader to be used to load provider-configuration files and
	 * provider classes, or <tt>null</tt> if the system class loader (or, failing that,
	 * the bootstrap class loader) is to be used
	 * @return A new service loader
	 */
	public static <S> ConditionServiceLoader<S> load(Class<S> service, ClassLoader loader) {
		return new ConditionServiceLoader<>(service, loader, false);
	}

	public static <S> ConditionServiceLoader<S> load(Class<S> service, ClassLoader loader, boolean instantiation) {
		return new ConditionServiceLoader<>(service, loader, instantiation);
	}

	/**
	 * Creates a new service loader for the given service type, using the current thread's
	 * {@linkplain Thread#getContextClassLoader context class loader}.
	 *
	 * <p>
	 * An invocation of this convenience method of the form
	 *
	 * <blockquote>
	 *
	 * <pre>
	 * ServiceLoader.load(<i>service</i>)</pre>
	 *
	 * </blockquote>
	 *
	 * is equivalent to
	 *
	 * <blockquote>
	 *
	 * <pre>
	 * ServiceLoader.load(<i>service</i>,
	 *                    Thread.currentThread().getContextClassLoader())</pre>
	 *
	 * </blockquote>
	 * @param <S> the class of the service type
	 * @param service The interface or abstract class representing the service
	 * @return A new service loader
	 */
	public static <S> ConditionServiceLoader<S> load(Class<S> service) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		return ConditionServiceLoader.load(service, cl);
	}

	/**
	 * Creates a new service loader for the given service type, using the extension class
	 * loader.
	 *
	 * <p>
	 * This convenience method simply locates the extension class loader, call it
	 * <tt><i>extClassLoader</i></tt>, and then returns
	 *
	 * <blockquote>
	 *
	 * <pre>
	 * ServiceLoader.load(<i>service</i>, <i>extClassLoader</i>)</pre>
	 *
	 * </blockquote>
	 *
	 * <p>
	 * If the extension class loader cannot be found then the system class loader is used;
	 * if there is no system class loader then the bootstrap class loader is used.
	 *
	 * <p>
	 * This method is intended for use when only installed providers are desired. The
	 * resulting service will only find and load providers that have been installed into
	 * the current Java virtual machine; providers on the application's class path will be
	 * ignored.
	 * @param <S> the class of the service type
	 * @param service The interface or abstract class representing the service
	 * @return A new service loader
	 */
	public static <S> ConditionServiceLoader<S> loadInstalled(Class<S> service) {
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		ClassLoader prev = null;
		while (cl != null) {
			prev = cl;
			cl = cl.getParent();
		}
		return ConditionServiceLoader.load(service, prev);
	}

	/**
	 * Clear this loader's provider cache so that all providers will be reloaded.
	 *
	 * <p>
	 * After invoking this method, subsequent invocations of the {@link #iterator()
	 * iterator} method will lazily look up and instantiate providers from scratch, just
	 * as is done by a newly-created loader.
	 *
	 * <p>
	 * This method is intended for use in situations in which new providers can be
	 * installed into a running Java virtual machine.
	 * @param instantiation 是否实例化
	 */
	public void reload(boolean instantiation) {
		this.providers.clear();
		this.providersInstance.clear();
		this.lookupIterator = new LazyIterator(this.service, this.loader, instantiation);
	}

	// Parse a single line from the given configuration file, adding the name
	// on the line to the names list.

	private int parseLine(Class<?> service, URL u, BufferedReader r, int lc, List<String> names)
			throws IOException, ServiceConfigurationError {
		String ln = r.readLine();
		if (ln == null) {
			return -1;
		}
		int ci = ln.indexOf('#');
		if (ci >= 0) {
			ln = ln.substring(0, ci);
		}
		ln = ln.trim();
		int n = ln.length();
		if (n != 0) {
			if ((ln.indexOf(' ') >= 0) || (ln.indexOf('\t') >= 0)) {
				fail(service, u, lc, "Illegal configuration-file syntax");
			}
			int cp = ln.codePointAt(0);
			if (!Character.isJavaIdentifierStart(cp)) {
				fail(service, u, lc, "Illegal provider-class name: " + ln);
			}
			for (int i = Character.charCount(cp); i < n; i += Character.charCount(cp)) {
				cp = ln.codePointAt(i);
				if (!Character.isJavaIdentifierPart(cp) && (cp != '.')) {
					fail(service, u, lc, "Illegal provider-class name: " + ln);
				}
			}
			if (!this.providers.containsKey(ln) && !names.contains(ln)) {
				names.add(ln);
			}
		}
		return lc + 1;
	}

	// Parse the content of the given URL as a provider-configuration file.
	//
	// @param service
	// The service type for which providers are being sought;
	// used to construct error detail strings
	//
	// @param u
	// The URL naming the configuration file to be parsed
	//
	// @return A (possibly empty) iterator that will yield the provider-class
	// names in the given configuration file that are not yet members
	// of the returned set
	//
	// @throws ServiceConfigurationError
	// If an I/O error occurs while reading from the given URL, or
	// if a configuration-file format error is detected

	private Iterator<String> parse(Class<?> service, URL u) throws ServiceConfigurationError {
		InputStream in = null;
		BufferedReader r = null;
		ArrayList<String> names = new ArrayList<>();
		try {
			in = u.openStream();
			r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
			int lc = 1;
			// noinspection StatementWithEmptyBody
			while ((lc = this.parseLine(service, u, r, lc, names)) >= 0) {
				;
			}
		}
		catch (IOException x) {
			fail(service, "Error reading configuration file", x);
		}
		finally {
			try {
				if (r != null) {
					r.close();
				}
				if (in != null) {
					in.close();
				}
			}
			catch (IOException y) {
				fail(service, "Error closing configuration file", y);
			}
		}
		return names.iterator();
	}

	/**
	 * Lazily loads the available providers of this loader's service.
	 *
	 * <p>
	 * The iterator returned by this method first yields all of the elements of the
	 * provider cache, in instantiation order. It then lazily loads and instantiates any
	 * remaining providers, adding each one to the cache in turn.
	 *
	 * <p>
	 * To achieve laziness the actual work of parsing the available provider-configuration
	 * files and instantiating providers must be done by the iterator itself. Its
	 * {@link Iterator#hasNext hasNext} and {@link Iterator#next next} methods can
	 * therefore throw a {@link ServiceConfigurationError} if a provider-configuration
	 * file violates the specified format, or if it names a provider class that cannot be
	 * found and instantiated, or if the result of instantiating the class is not
	 * assignable to the service type, or if any other kind of exception or error is
	 * thrown as the next provider is located and instantiated. To write robust code it is
	 * only necessary to catch {@link ServiceConfigurationError} when using a service
	 * iterator.
	 *
	 * <p>
	 * If such an error is thrown then subsequent invocations of the iterator will make a
	 * best effort to locate and instantiate the next available provider, but in general
	 * such recovery cannot be guaranteed.
	 *
	 * <blockquote style="font-size: smaller; line-height: 1.2">
	 *
	 * <span style="padding-right: 1em; font-weight: bold">Design Note</span> Throwing an
	 * error in these cases may seem extreme. The rationale for this behavior is that a
	 * malformed provider-configuration file, like a malformed class file, indicates a
	 * serious problem with the way the Java virtual machine is configured or is being
	 * used. As such it is preferable to throw an error rather than try to recover or,
	 * even worse, fail silently.
	 *
	 * </blockquote>
	 *
	 * <p>
	 * The iterator returned by this method does not support removal. Invoking its
	 * {@link Iterator#remove() remove} method will cause an
	 * {@link UnsupportedOperationException} to be thrown.
	 *
	 * @implNote When adding providers to the cache, the {@link #iterator Iterator}
	 * processes resources in the order that the {@link ClassLoader#getResources(String)
	 * ClassLoader.getResources(String)} method finds the service configuration files.
	 * @return An iterator that lazily loads providers for this loader's service
	 */
	@Override
	public Iterator<Class<S>> iterator() {
		return new Iterator<Class<S>>() {

			Iterator<Map.Entry<String, Class<S>>> knownProviders = ConditionServiceLoader.this.providers.entrySet()
					.iterator();

			@Override
			public boolean hasNext() {
				if (this.knownProviders.hasNext()) {
					return true;
				}
				return ConditionServiceLoader.this.lookupIterator.hasNext();
			}

			@Override
			public Class<S> next() {
				if (this.knownProviders.hasNext()) {
					return this.knownProviders.next().getValue();
				}
				return ConditionServiceLoader.this.lookupIterator.next();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public List<S> getProviderInstances(Class<S> clazz) {
		List<S> instances = ConditionServiceLoader.this.providersInstance.get(clazz);
		if (instances == null) {
			return Collections.emptyList();
		}
		return instances;
	}

	/**
	 * Returns a string describing this service.
	 * @return A descriptive string
	 */
	@Override
	public String toString() {
		return "java.util.ServiceLoader[" + this.service.getName() + "]";
	}

	// Private inner class implementing fully-lazy provider lookup

	private class LazyIterator implements Iterator<Class<S>> {

		Class<S> service;

		ClassLoader loader;

		Enumeration<URL> configs = null;

		Iterator<String> pending = null;

		String nextName = null;

		boolean instantiation;

		private LazyIterator(Class<S> service, ClassLoader loader, boolean instantiation) {
			this.service = service;
			this.loader = loader;
			this.instantiation = instantiation;
		}

		private boolean hasNextService() {
			if (this.nextName != null) {
				return true;
			}
			if (this.configs == null) {
				try {
					String fullName = PREFIX + this.service.getName();
					if (this.loader == null) {
						this.configs = ClassLoader.getSystemResources(fullName);
					}
					else {
						this.configs = this.loader.getResources(fullName);
					}
				}
				catch (IOException x) {
					fail(this.service, "Error locating configuration files", x);
				}
			}
			while ((this.pending == null) || !this.pending.hasNext()) {
				if (!this.configs.hasMoreElements()) {
					return false;
				}
				this.pending = ConditionServiceLoader.this.parse(this.service, this.configs.nextElement());
			}
			this.nextName = this.pending.next();
			return true;
		}

		@Nullable
		private Class<S> nextService() {
			if (!this.hasNextService()) {
				throw new NoSuchElementException();
			}
			String cn = this.nextName;
			this.nextName = null;
			Class<?> c = null;
			try {
				c = Class.forName(cn, false, this.loader);

				SPIConditionOnClass conditionOnClass = c.getAnnotation(SPIConditionOnClass.class);
				if (conditionOnClass != null) {
					Class<?>[] classes = conditionOnClass.value();
					for (Class<?> clazz : classes) {
						if (!ClassLoaderUtil.isPresent(clazz.getName(), this.loader)) {
							// not present, skip
							return null;
						}
					}
					String[] classNames = conditionOnClass.name();
					for (String className : classNames) {
						if (!ClassLoaderUtil.isPresent(className, this.loader)) {
							// not present, skip
							return null;
						}
					}
				}
			}
			catch (ClassNotFoundException x) {
				fail(this.service, "Provider " + cn + " not found");
			}
			if (!this.service.isAssignableFrom(c)) {
				fail(this.service, "Provider " + cn + " not a subtype");
			}
			try {
				// different with ServiceLoader.
				Class<S> clazz = (Class<S>) c;
				ConditionServiceLoader.this.providers.put(cn, clazz);
				if (this.instantiation) {
					S p = this.service.cast(c.newInstance());
					ConditionServiceLoader.this.providersInstance.computeIfAbsent(clazz, k -> new ArrayList<>(2))
							.add(p);
				}
				return (Class<S>) c;
			}
			catch (Throwable x) {
				fail(this.service, "Provider " + cn + " could not be instantiated", x);
			}
			throw new Error(); // This cannot happen
		}

		@Override
		public boolean hasNext() {
			if (ConditionServiceLoader.this.acc == null) {
				return this.hasNextService();
			}
			else {
				PrivilegedAction<Boolean> action = this::hasNextService;
				return AccessController.doPrivileged(action, ConditionServiceLoader.this.acc);
			}
		}

		@Override
		@Nullable
		public Class<S> next() {
			if (!this.hasNextService()) {
				throw new NoSuchElementException();
			}
			if (ConditionServiceLoader.this.acc == null) {
				return this.nextService();
			}
			else {
				PrivilegedAction<Class<S>> action = this::nextService;
				return AccessController.doPrivileged(action, ConditionServiceLoader.this.acc);
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}
