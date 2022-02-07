/*
 * Copyright (C) 2014 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.magneton.core.eventbus;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.annotation.CheckForNull;

import com.google.j2objc.annotations.Weak;
import javax.annotation.VisibleForTesting;
import org.magneton.core.base.MoreObjects;
import org.magneton.core.base.Objects;
import org.magneton.core.base.Preconditions;
import org.magneton.core.cache.CacheBuilder;
import org.magneton.core.cache.CacheLoader;
import org.magneton.core.cache.LoadingCache;
import org.magneton.core.collect.HashMultimap;
import org.magneton.core.collect.ImmutableList;
import org.magneton.core.collect.ImmutableSet;
import org.magneton.core.collect.Iterators;
import org.magneton.core.collect.Lists;
import org.magneton.core.collect.Maps;
import org.magneton.core.collect.Multimap;
import org.magneton.core.primitives.Primitives;
import org.magneton.core.reflect.TypeToken;
import org.magneton.core.util.concurrent.UncheckedExecutionException;

/**
 * Registry of subscribers to a single event bus.
 *
 * @author Colin Decker
 */
@ElementTypesAreNonnullByDefault
final class SubscriberRegistry {

	/**
	 * A thread-safe cache that contains the mapping from each class to all methods in
	 * that class and all super-classes, that are annotated with {@code @Subscribe}. The
	 * cache is shared across all instances of this class; this greatly improves
	 * performance if multiple EventBus instances are created and objects of the same
	 * class are registered on all of them.
	 */
	private static final org.magneton.core.cache.LoadingCache<Class<?>, ImmutableList<Method>> subscriberMethodsCache = org.magneton.core.cache.CacheBuilder
			.newBuilder().weakKeys().build(new org.magneton.core.cache.CacheLoader<Class<?>, ImmutableList<Method>>() {
				@Override
				public ImmutableList<Method> load(Class<?> concreteClass) throws Exception {
					return getAnnotatedMethodsNotCached(concreteClass);
				}
			});

	/** Global cache of classes to their flattened hierarchy of supertypes. */
	private static final LoadingCache<Class<?>, ImmutableSet<Class<?>>> flattenHierarchyCache = CacheBuilder
			.newBuilder().weakKeys().build(new CacheLoader<Class<?>, ImmutableSet<Class<?>>>() {
				// <Class<?>> is actually needed to compile
				@Override
				public ImmutableSet<Class<?>> load(Class<?> concreteClass) {
					return ImmutableSet.<Class<?>>copyOf(TypeToken.of(concreteClass).getTypes().rawTypes());
				}
			});

	/**
	 * All registered subscribers, indexed by event type.
	 *
	 * <p>
	 * The {@link CopyOnWriteArraySet} values make it easy and relatively lightweight to
	 * get an immutable snapshot of all current subscribers to an event without any
	 * locking.
	 */
	private final ConcurrentMap<Class<?>, CopyOnWriteArraySet<Subscriber>> subscribers = org.magneton.core.collect.Maps
			.newConcurrentMap();

	/** The event bus this registry belongs to. */
	@Weak
	private final EventBus bus;

	SubscriberRegistry(EventBus bus) {
		this.bus = Preconditions.checkNotNull(bus);
	}

	private static ImmutableList<Method> getAnnotatedMethods(Class<?> clazz) {
		try {
			return subscriberMethodsCache.getUnchecked(clazz);
		}
		catch (org.magneton.core.util.concurrent.UncheckedExecutionException e) {
			org.magneton.core.base.Throwables.throwIfUnchecked(e.getCause());
			throw e;
		}
	}

	private static ImmutableList<Method> getAnnotatedMethodsNotCached(Class<?> clazz) {
		Set<? extends Class<?>> supertypes = TypeToken.of(clazz).getTypes().rawTypes();
		Map<MethodIdentifier, Method> identifiers = Maps.newHashMap();
		for (Class<?> supertype : supertypes) {
			for (Method method : supertype.getDeclaredMethods()) {
				if (method.isAnnotationPresent(Subscribe.class) && !method.isSynthetic()) {
					// TODO(cgdecker): Should check for a generic parameter type and error
					// out
					Class<?>[] parameterTypes = method.getParameterTypes();
					Preconditions.checkArgument(parameterTypes.length == 1,
							"Method %s has @Subscribe annotation but has %s parameters. "
									+ "Subscriber methods must have exactly 1 parameter.",
							method, parameterTypes.length);

					Preconditions.checkArgument(!parameterTypes[0].isPrimitive(),
							"@Subscribe method %s's parameter is %s. " + "Subscriber methods cannot accept primitives. "
									+ "Consider changing the parameter to %s.",
							method, parameterTypes[0].getName(), Primitives.wrap(parameterTypes[0]).getSimpleName());

					MethodIdentifier ident = new MethodIdentifier(method);
					if (!identifiers.containsKey(ident)) {
						identifiers.put(ident, method);
					}
				}
			}
		}
		return ImmutableList.copyOf(identifiers.values());
	}

	/**
	 * Flattens a class's type hierarchy into a set of {@code Class} objects including all
	 * superclasses (transitively) and all interfaces implemented by these superclasses.
	 */
	@VisibleForTesting
	static ImmutableSet<Class<?>> flattenHierarchy(Class<?> concreteClass) {
		try {
			return flattenHierarchyCache.getUnchecked(concreteClass);
		}
		catch (UncheckedExecutionException e) {
			throw org.magneton.core.base.Throwables.propagate(e.getCause());
		}
	}

	/** Registers all subscriber methods on the given listener object. */
	void register(Object listener) {
		org.magneton.core.collect.Multimap<Class<?>, Subscriber> listenerMethods = findAllSubscribers(listener);

		for (Entry<Class<?>, Collection<Subscriber>> entry : listenerMethods.asMap().entrySet()) {
			Class<?> eventType = entry.getKey();
			Collection<Subscriber> eventMethodsInListener = entry.getValue();

			CopyOnWriteArraySet<Subscriber> eventSubscribers = subscribers.get(eventType);

			if (eventSubscribers == null) {
				CopyOnWriteArraySet<Subscriber> newSet = new CopyOnWriteArraySet<>();
				eventSubscribers = org.magneton.core.base.MoreObjects
						.firstNonNull(subscribers.putIfAbsent(eventType, newSet), newSet);
			}

			eventSubscribers.addAll(eventMethodsInListener);
		}
	}

	/** Unregisters all subscribers on the given listener object. */
	void unregister(Object listener) {
		org.magneton.core.collect.Multimap<Class<?>, Subscriber> listenerMethods = findAllSubscribers(listener);

		for (Entry<Class<?>, Collection<Subscriber>> entry : listenerMethods.asMap().entrySet()) {
			Class<?> eventType = entry.getKey();
			Collection<Subscriber> listenerMethodsForType = entry.getValue();

			CopyOnWriteArraySet<Subscriber> currentSubscribers = subscribers.get(eventType);
			if (currentSubscribers == null || !currentSubscribers.removeAll(listenerMethodsForType)) {
				// if removeAll returns true, all we really know is that at least one
				// subscriber was
				// removed... however, barring something very strange we can assume that
				// if at least one
				// subscriber was removed, all subscribers on listener for that event type
				// were... after
				// all, the definition of subscribers on a particular class is totally
				// static
				throw new IllegalArgumentException(
						"missing event subscriber for an annotated method. Is " + listener + " registered?");
			}

			// don't try to remove the set if it's empty; that can't be done safely
			// without a lock
			// anyway, if the set is empty it'll just be wrapping an array of length 0
		}
	}

	@javax.annotation.VisibleForTesting
	Set<Subscriber> getSubscribersForTesting(Class<?> eventType) {
		return MoreObjects.firstNonNull(subscribers.get(eventType), ImmutableSet.<Subscriber>of());
	}

	/**
	 * Gets an iterator representing an immutable snapshot of all subscribers to the given
	 * event at the time this method is called.
	 */
	Iterator<Subscriber> getSubscribers(Object event) {
		ImmutableSet<Class<?>> eventTypes = flattenHierarchy(event.getClass());

		List<Iterator<Subscriber>> subscriberIterators = Lists.newArrayListWithCapacity(eventTypes.size());

		for (Class<?> eventType : eventTypes) {
			CopyOnWriteArraySet<Subscriber> eventSubscribers = subscribers.get(eventType);
			if (eventSubscribers != null) {
				// eager no-copy snapshot
				subscriberIterators.add(eventSubscribers.iterator());
			}
		}

		return Iterators.concat(subscriberIterators.iterator());
	}

	/**
	 * Returns all subscribers for the given listener grouped by the type of event they
	 * subscribe to.
	 */
	private org.magneton.core.collect.Multimap<Class<?>, Subscriber> findAllSubscribers(Object listener) {
		Multimap<Class<?>, Subscriber> methodsInListener = HashMultimap.create();
		Class<?> clazz = listener.getClass();
		for (Method method : getAnnotatedMethods(clazz)) {
			Class<?>[] parameterTypes = method.getParameterTypes();
			Class<?> eventType = parameterTypes[0];
			methodsInListener.put(eventType, Subscriber.create(bus, listener, method));
		}
		return methodsInListener;
	}

	private static final class MethodIdentifier {

		private final String name;

		private final List<Class<?>> parameterTypes;

		MethodIdentifier(Method method) {
			name = method.getName();
			parameterTypes = Arrays.asList(method.getParameterTypes());
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(name, parameterTypes);
		}

		@Override
		public boolean equals(@CheckForNull Object o) {
			if (o instanceof MethodIdentifier) {
				MethodIdentifier ident = (MethodIdentifier) o;
				return name.equals(ident.name) && parameterTypes.equals(ident.parameterTypes);
			}
			return false;
		}

	}

}
