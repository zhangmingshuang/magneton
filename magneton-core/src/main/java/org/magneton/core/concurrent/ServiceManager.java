/*
 * Copyright (C) 2012 The Guava Authors
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

package org.magneton.core.concurrent;

import java.lang.ref.WeakReference;
import java.time.Duration;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotations.CanIgnoreReturnValue;
import javax.annotations.GuardedBy;
import javax.annotations.WeakOuter;

import org.magneton.core.base.Function;
import org.magneton.core.base.MoreObjects;
import org.magneton.core.base.Stopwatch;
import org.magneton.core.collect.Collections2;
import org.magneton.core.collect.ImmutableCollection;
import org.magneton.core.collect.ImmutableList;
import org.magneton.core.collect.ImmutableMap;
import org.magneton.core.collect.ImmutableSet;
import org.magneton.core.collect.ImmutableSetMultimap;
import org.magneton.core.collect.Lists;
import org.magneton.core.collect.Maps;
import org.magneton.core.collect.MultimapBuilder;
import org.magneton.core.collect.Multimaps;
import org.magneton.core.collect.Multiset;
import org.magneton.core.collect.Ordering;
import org.magneton.core.collect.SetMultimap;
import org.magneton.core.concurrent.Service.State;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.magneton.core.base.Preconditions.checkArgument;
import static org.magneton.core.base.Preconditions.checkNotNull;
import static org.magneton.core.base.Preconditions.checkState;
import static org.magneton.core.base.Predicates.equalTo;
import static org.magneton.core.base.Predicates.in;
import static org.magneton.core.base.Predicates.instanceOf;
import static org.magneton.core.base.Predicates.not;
import static org.magneton.core.concurrent.Internal.toNanosSaturated;
import static org.magneton.core.concurrent.Service.State.FAILED;
import static org.magneton.core.concurrent.Service.State.NEW;
import static org.magneton.core.concurrent.Service.State.RUNNING;
import static org.magneton.core.concurrent.Service.State.STARTING;
import static org.magneton.core.concurrent.Service.State.STOPPING;
import static org.magneton.core.concurrent.Service.State.TERMINATED;

/**
 * A manager for monitoring and controlling a set of {@linkplain Service services}. This
 * class provides methods for {@linkplain #startAsync() starting},
 * {@linkplain #stopAsync() stopping} and {@linkplain #servicesByState inspecting} a
 * collection of {@linkplain Service services}. Additionally, users can monitor state
 * transitions with the {@linkplain Listener listener} mechanism.
 *
 * <p>
 * While it is recommended that service lifecycles be managed via this class, state
 * transitions initiated via other mechanisms do not impact the correctness of its
 * methods. For example, if the services are started by some mechanism besides
 * {@link #startAsync}, the listeners will be invoked when appropriate and
 * {@link #awaitHealthy} will still work as expected.
 *
 * <p>
 * Here is a simple example of how to use a {@code ServiceManager} to start a server.
 *
 * <pre>{@code
 * class Server {
 *   public static void main(String[] args) {
 *     Set<Service> services = ...;
 *     ServiceManager manager = new ServiceManager(services);
 *     manager.addListener(new Listener() {
 *         public void stopped() {}
 *         public void healthy() {
 *           // Services have been initialized and are healthy, start accepting requests...
 *         }
 *         public void failure(Service service) {
 *           // Something failed, at this point we could log it, notify a load balancer, or take
 *           // some other action.  For now we will just exit.
 *           System.exit(1);
 *         }
 *       },
 *       MoreExecutors.directExecutor());
 *
 *     Runtime.getRuntime().addShutdownHook(new Thread() {
 *       public void run() {
 *         // Give the services 5 seconds to stop to ensure that we are responsive to shutdown
 *         // requests.
 *         try {
 *           manager.stopAsync().awaitStopped(5, TimeUnit.SECONDS);
 *         } catch (TimeoutException timeout) {
 *           // stopping timed out
 *         }
 *       }
 *     });
 *     manager.startAsync();  // start all the services asynchronously
 *   }
 * }
 * }</pre>
 *
 * <p>
 * This class uses the ServiceManager's methods to start all of its services, to respond
 * to service failure and to ensure that when the JVM is shutting down all the services
 * are stopped.
 *
 * @author Luke Sandberg
 * @since 14.0
 */

@ElementTypesAreNonnullByDefault
public final class ServiceManager implements ServiceManagerBridge {

	private static final Logger logger = Logger.getLogger(ServiceManager.class.getName());

	private static final ListenerCallQueue.Event<Listener> HEALTHY_EVENT = new ListenerCallQueue.Event<Listener>() {
		@Override
		public void call(Listener listener) {
			listener.healthy();
		}

		@Override
		public String toString() {
			return "healthy()";
		}
	};

	private static final ListenerCallQueue.Event<Listener> STOPPED_EVENT = new ListenerCallQueue.Event<Listener>() {
		@Override
		public void call(Listener listener) {
			listener.stopped();
		}

		@Override
		public String toString() {
			return "stopped()";
		}
	};

	/**
	 * An encapsulation of all of the state that is accessed by the
	 * {@linkplain ServiceListener service listeners}. This is extracted into its own
	 * object so that {@link ServiceListener} could be made {@code static} and its
	 * instances can be safely constructed and added in the {@link ServiceManager}
	 * constructor without having to close over the partially constructed
	 * {@link ServiceManager} instance (i.e. avoid leaking a pointer to {@code this}).
	 */
	private final ServiceManagerState state;

	private final ImmutableList<Service> services;

	/**
	 * Constructs a new instance for managing the given services.
	 * @param services The services to manage
	 * @throws IllegalArgumentException if not all services are {@linkplain State#NEW new}
	 * or if there are any duplicate services.
	 */
	public ServiceManager(Iterable<? extends Service> services) {
		ImmutableList<Service> copy = ImmutableList.copyOf(services);
		if (copy.isEmpty()) {
			// Having no services causes the manager to behave strangely. Notably,
			// listeners are never
			// fired. To avoid this we substitute a placeholder service.
			logger.log(Level.WARNING,
					"ServiceManager configured with no services.  Is your application configured properly?",
					new EmptyServiceManagerWarning());
			copy = ImmutableList.<Service>of(new NoOpService());
		}
		this.state = new ServiceManagerState(copy);
		this.services = copy;
		WeakReference<ServiceManagerState> stateReference = new WeakReference<>(this.state);
		for (Service service : copy) {
			service.addListener(new ServiceListener(service, stateReference), MoreExecutors.directExecutor());
			// We check the state after adding the listener as a way to ensure that our
			// listener was added
			// to a NEW service.
			checkArgument(service.state() == NEW, "Can only manage NEW services, %s", service);
		}
		// We have installed all of our listeners and after this point any state
		// transition should be
		// correct.
		this.state.markReady();
	}

	/**
	 * Registers a {@link Listener} to be {@linkplain Executor#execute executed} on the
	 * given executor. The listener will not have previous state changes replayed, so it
	 * is suggested that listeners are added before any of the managed services are
	 * {@linkplain Service#startAsync started}.
	 *
	 * <p>
	 * {@code addListener} guarantees execution ordering across calls to a given listener
	 * but not across calls to multiple listeners. Specifically, a given listener will
	 * have its callbacks invoked in the same order as the underlying service enters those
	 * states. Additionally, at most one of the listener's callbacks will execute at once.
	 * However, multiple listeners' callbacks may execute concurrently, and listeners may
	 * execute in an order different from the one in which they were registered.
	 *
	 * <p>
	 * RuntimeExceptions thrown by a listener will be caught and logged. Any exception
	 * thrown during {@code Executor.execute} (e.g., a {@code RejectedExecutionException})
	 * will be caught and logged.
	 *
	 * <p>
	 * When selecting an executor, note that {@code directExecutor} is dangerous in some
	 * cases. See the discussion in the {@link ListenableFuture#addListener
	 * ListenableFuture.addListener} documentation.
	 * @param listener the listener to run when the manager changes state
	 * @param executor the executor in which the listeners callback methods will be run.
	 */
	public void addListener(Listener listener, Executor executor) {
		this.state.addListener(listener, executor);
	}

	/**
	 * Initiates service {@linkplain Service#startAsync startup} on all the services being
	 * managed. It is only valid to call this method if all of the services are
	 * {@linkplain State#NEW new}.
	 * @return this
	 * @throws IllegalStateException if any of the Services are not {@link State#NEW new}
	 * when the method is called.
	 */
	@CanIgnoreReturnValue
	public ServiceManager startAsync() {
		for (Service service : this.services) {
			checkState(service.state() == NEW, "Not all services are NEW, cannot start %s", this);
		}
		for (Service service : this.services) {
			try {
				this.state.tryStartTiming(service);
				service.startAsync();
			}
			catch (IllegalStateException e) {
				// This can happen if the service has already been started or stopped
				// (e.g. by another
				// service or listener). Our contract says it is safe to call this method
				// if
				// all services were NEW when it was called, and this has already been
				// verified above, so we
				// don't propagate the exception.
				logger.log(Level.WARNING, "Unable to start Service " + service, e);
			}
		}
		return this;
	}

	/**
	 * Waits for the {@link ServiceManager} to become {@linkplain #isHealthy() healthy}.
	 * The manager will become healthy after all the component services have reached the
	 * {@linkplain State#RUNNING running} state.
	 * @throws IllegalStateException if the service manager reaches a state from which it
	 * cannot become {@linkplain #isHealthy() healthy}.
	 */
	public void awaitHealthy() {
		this.state.awaitHealthy();
	}

	/**
	 * Waits for the {@link ServiceManager} to become {@linkplain #isHealthy() healthy}
	 * for no more than the given time. The manager will become healthy after all the
	 * component services have reached the {@linkplain State#RUNNING running} state.
	 * @param timeout the maximum time to wait
	 * @throws TimeoutException if not all of the services have finished starting within
	 * the deadline
	 * @throws IllegalStateException if the service manager reaches a state from which it
	 * cannot become {@linkplain #isHealthy() healthy}.
	 * @since 28.0
	 */
	public void awaitHealthy(Duration timeout) throws TimeoutException {
		this.awaitHealthy(toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
	}

	/**
	 * Waits for the {@link ServiceManager} to become {@linkplain #isHealthy() healthy}
	 * for no more than the given time. The manager will become healthy after all the
	 * component services have reached the {@linkplain State#RUNNING running} state.
	 * @param timeout the maximum time to wait
	 * @param unit the time unit of the timeout argument
	 * @throws TimeoutException if not all of the services have finished starting within
	 * the deadline
	 * @throws IllegalStateException if the service manager reaches a state from which it
	 * cannot become {@linkplain #isHealthy() healthy}.
	 */
	// should accept a java.time.Duration
	public void awaitHealthy(long timeout, TimeUnit unit) throws TimeoutException {
		this.state.awaitHealthy(timeout, unit);
	}

	/**
	 * Initiates service {@linkplain Service#stopAsync shutdown} if necessary on all the
	 * services being managed.
	 * @return this
	 */
	@CanIgnoreReturnValue
	public ServiceManager stopAsync() {
		for (Service service : this.services) {
			service.stopAsync();
		}
		return this;
	}

	/**
	 * Waits for the all the services to reach a terminal state. After this method returns
	 * all services will either be {@linkplain State#TERMINATED terminated} or
	 * {@linkplain State#FAILED failed}.
	 */
	public void awaitStopped() {
		this.state.awaitStopped();
	}

	/**
	 * Waits for the all the services to reach a terminal state for no more than the given
	 * time. After this method returns all services will either be
	 * {@linkplain State#TERMINATED terminated} or {@linkplain State#FAILED failed}.
	 * @param timeout the maximum time to wait
	 * @throws TimeoutException if not all of the services have stopped within the
	 * deadline
	 * @since 28.0
	 */
	public void awaitStopped(Duration timeout) throws TimeoutException {
		this.awaitStopped(toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
	}

	/**
	 * Waits for the all the services to reach a terminal state for no more than the given
	 * time. After this method returns all services will either be
	 * {@linkplain State#TERMINATED terminated} or {@linkplain State#FAILED failed}.
	 * @param timeout the maximum time to wait
	 * @param unit the time unit of the timeout argument
	 * @throws TimeoutException if not all of the services have stopped within the
	 * deadline
	 */
	// should accept a java.time.Duration
	public void awaitStopped(long timeout, TimeUnit unit) throws TimeoutException {
		this.state.awaitStopped(timeout, unit);
	}

	/**
	 * Returns true if all services are currently in the {@linkplain State#RUNNING
	 * running} state.
	 *
	 * <p>
	 * Users who want more detailed information should use the {@link #servicesByState}
	 * method to get detailed information about which services are not running.
	 */
	public boolean isHealthy() {
		for (Service service : this.services) {
			if (!service.isRunning()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Provides a snapshot of the current state of all the services under management.
	 *
	 * <p>
	 * N.B. This snapshot is guaranteed to be consistent, i.e. the set of states returned
	 * will correspond to a point in time view of the services.
	 *
	 * @since 29.0 (present with return type {@code ImmutableMultimap} since 14.0)
	 */
	@Override
	public ImmutableSetMultimap<State, Service> servicesByState() {
		return this.state.servicesByState();
	}

	/**
	 * Returns the service load times. This value will only return startup times for
	 * services that have finished starting.
	 * @return Map of services and their corresponding startup time in millis, the map
	 * entries will be ordered by startup time.
	 */
	public ImmutableMap<Service, Long> startupTimes() {
		return this.state.startupTimes();
	}

	/**
	 * Returns the service load times. This value will only return startup times for
	 * services that have finished starting.
	 * @return Map of services and their corresponding startup time, the map entries will
	 * be ordered by startup time.
	 * @since 31.0
	 */
	public ImmutableMap<Service, Duration> startupDurations() {
		return ImmutableMap.copyOf(org.magneton.core.collect.Maps
				.<Service, Long, Duration>transformValues(this.startupTimes(), Duration::ofMillis));
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(ServiceManager.class)
				.add("services", Collections2.filter(this.services, not(instanceOf(NoOpService.class)))).toString();
	}

	/**
	 * A listener for the aggregate state changes of the services that are under
	 * management. Users that need to listen to more fine-grained events (such as when
	 * each particular {@linkplain Service service} starts, or terminates), should attach
	 * {@linkplain Service.Listener service listeners} to each individual service.
	 *
	 * @author Luke Sandberg
	 * @since 15.0 (present as an interface in 14.0)
	 */
	public abstract static class Listener {

		/**
		 * Called when the service initially becomes healthy.
		 *
		 * <p>
		 * This will be called at most once after all the services have entered the
		 * {@linkplain State#RUNNING running} state. If any services fail during start up
		 * or {@linkplain State#FAILED fail}/{@linkplain State#TERMINATED terminate}
		 * before all other services have started {@linkplain State#RUNNING running} then
		 * this method will not be called.
		 */
		public void healthy() {
		}

		/**
		 * Called when the all of the component services have reached a terminal state,
		 * either {@linkplain State#TERMINATED terminated} or {@linkplain State#FAILED
		 * failed}.
		 */
		public void stopped() {
		}

		/**
		 * Called when a component service has {@linkplain State#FAILED failed}.
		 * @param service The service that failed.
		 */
		public void failure(Service service) {
		}

	}

	/**
	 * An encapsulation of all the mutable state of the {@link ServiceManager} that needs
	 * to be accessed by instances of {@link ServiceListener}.
	 */
	private static final class ServiceManagerState {

		final Monitor monitor = new Monitor();

		@GuardedBy("monitor")
		final SetMultimap<State, Service> servicesByState = MultimapBuilder.enumKeys(State.class).linkedHashSetValues()
				.build();

		@GuardedBy("monitor")
		final Multiset<State> states = this.servicesByState.keys();

		@GuardedBy("monitor")
		final Map<Service, Stopwatch> startupTimers = org.magneton.core.collect.Maps.newIdentityHashMap();

		final int numberOfServices;

		/**
		 * Controls how long to wait for all the services to either become healthy or
		 * reach a state from which it is guaranteed that it can never become healthy.
		 */
		final Monitor.Guard awaitHealthGuard = new AwaitHealthGuard();

		/** Controls how long to wait for all services to reach a terminal state. */
		final Monitor.Guard stoppedGuard = new StoppedGuard();

		/** The listeners to notify during a state transition. */
		final ListenerCallQueue<Listener> listeners = new ListenerCallQueue<>();

		/**
		 * These two booleans are used to mark the state as ready to start.
		 *
		 * <p>
		 * {@link #ready}: is set by {@link #markReady} to indicate that all listeners
		 * have been correctly installed
		 *
		 * <p>
		 * {@link #transitioned}: is set by {@link #transitionService} to indicate that
		 * some transition has been performed.
		 *
		 * <p>
		 * Together, they allow us to enforce that all services have their listeners
		 * installed prior to any service performing a transition, then we can fail in the
		 * ServiceManager constructor rather than in a Service.Listener callback.
		 */
		@GuardedBy("monitor")
		boolean ready;

		@GuardedBy("monitor")
		boolean transitioned;

		/**
		 * It is implicitly assumed that all the services are NEW and that they will all
		 * remain NEW until all the Listeners are installed and {@link #markReady()} is
		 * called. It is our caller's responsibility to only call {@link #markReady()} if
		 * all services were new at the time this method was called and when all the
		 * listeners were installed.
		 */
		ServiceManagerState(ImmutableCollection<Service> services) {
			this.numberOfServices = services.size();
			this.servicesByState.putAll(NEW, services);
		}

		/**
		 * Attempts to start the timer immediately prior to the service being started via
		 * {@link Service#startAsync()}.
		 */
		void tryStartTiming(Service service) {
			this.monitor.enter();
			try {
				Stopwatch stopwatch = this.startupTimers.get(service);
				if (stopwatch == null) {
					this.startupTimers.put(service, Stopwatch.createStarted());
				}
			}
			finally {
				this.monitor.leave();
			}
		}

		/**
		 * Marks the {@link State} as ready to receive transitions. Returns true if no
		 * transitions have been observed yet.
		 */
		void markReady() {
			this.monitor.enter();
			try {
				if (!this.transitioned) {
					// nothing has transitioned since construction, good.
					this.ready = true;
				}
				else {
					// This should be an extremely rare race condition.
					List<Service> servicesInBadStates = org.magneton.core.collect.Lists.newArrayList();
					for (Service service : this.servicesByState().values()) {
						if (service.state() != NEW) {
							servicesInBadStates.add(service);
						}
					}
					throw new IllegalArgumentException("Services started transitioning asynchronously before "
							+ "the ServiceManager was constructed: " + servicesInBadStates);
				}
			}
			finally {
				this.monitor.leave();
			}
		}

		void addListener(Listener listener, Executor executor) {
			this.listeners.addListener(listener, executor);
		}

		void awaitHealthy() {
			this.monitor.enterWhenUninterruptibly(this.awaitHealthGuard);
			try {
				this.checkHealthy();
			}
			finally {
				this.monitor.leave();
			}
		}

		void awaitHealthy(long timeout, TimeUnit unit) throws TimeoutException {
			this.monitor.enter();
			try {
				if (!this.monitor.waitForUninterruptibly(this.awaitHealthGuard, timeout, unit)) {
					throw new TimeoutException("Timeout waiting for the services to become healthy. The "
							+ "following services have not started: "
							+ Multimaps.filterKeys(this.servicesByState, in(ImmutableSet.of(NEW, STARTING))));
				}
				this.checkHealthy();
			}
			finally {
				this.monitor.leave();
			}
		}

		void awaitStopped() {
			this.monitor.enterWhenUninterruptibly(this.stoppedGuard);
			this.monitor.leave();
		}

		void awaitStopped(long timeout, TimeUnit unit) throws TimeoutException {
			this.monitor.enter();
			try {
				if (!this.monitor.waitForUninterruptibly(this.stoppedGuard, timeout, unit)) {
					throw new TimeoutException("Timeout waiting for the services to stop. The following "
							+ "services have not stopped: "
							+ Multimaps.filterKeys(this.servicesByState, not(in(EnumSet.of(TERMINATED, FAILED)))));
				}
			}
			finally {
				this.monitor.leave();
			}
		}

		ImmutableSetMultimap<State, Service> servicesByState() {
			ImmutableSetMultimap.Builder<State, Service> builder = ImmutableSetMultimap.builder();
			this.monitor.enter();
			try {
				for (Entry<State, Service> entry : this.servicesByState.entries()) {
					if (!(entry.getValue() instanceof NoOpService)) {
						builder.put(entry);
					}
				}
			}
			finally {
				this.monitor.leave();
			}
			return builder.build();
		}

		ImmutableMap<Service, Long> startupTimes() {
			List<Entry<Service, Long>> loadTimes;
			this.monitor.enter();
			try {
				loadTimes = Lists.newArrayListWithCapacity(this.startupTimers.size());
				// N.B. There will only be an entry in the map if the service has started
				for (Entry<Service, Stopwatch> entry : this.startupTimers.entrySet()) {
					Service service = entry.getKey();
					Stopwatch stopwatch = entry.getValue();
					if (!stopwatch.isRunning() && !(service instanceof NoOpService)) {
						loadTimes.add(Maps.immutableEntry(service, stopwatch.elapsed(MILLISECONDS)));
					}
				}
			}
			finally {
				this.monitor.leave();
			}
			Collections.sort(loadTimes, Ordering.natural().onResultOf(new Function<Entry<Service, Long>, Long>() {
				@Override
				public Long apply(Entry<Service, Long> input) {
					return input.getValue();
				}
			}));
			return ImmutableMap.copyOf(loadTimes);
		}

		/**
		 * Updates the state with the given service transition.
		 *
		 * <p>
		 * This method performs the main logic of ServiceManager in the following steps.
		 *
		 * <ol>
		 * <li>Update the {@link #servicesByState()}
		 * <li>Update the {@link #startupTimers}
		 * <li>Based on the new state queue listeners to run
		 * <li>Run the listeners (outside of the lock)
		 * </ol>
		 */
		void transitionService(Service service, State from, State to) {
			checkNotNull(service);
			checkArgument(from != to);
			this.monitor.enter();
			try {
				this.transitioned = true;
				if (!this.ready) {
					return;
				}
				// Update state.
				checkState(this.servicesByState.remove(from, service),
						"Service %s not at the expected location in the state map %s", service, from);
				checkState(this.servicesByState.put(to, service), "Service %s in the state map unexpectedly at %s",
						service, to);
				// Update the timer
				Stopwatch stopwatch = this.startupTimers.get(service);
				if (stopwatch == null) {
					// This means the service was started by some means other than
					// ServiceManager.startAsync
					stopwatch = Stopwatch.createStarted();
					this.startupTimers.put(service, stopwatch);
				}
				if (to.compareTo(RUNNING) >= 0 && stopwatch.isRunning()) {
					// N.B. if we miss the STARTING event then we may never record a
					// startup time.
					stopwatch.stop();
					if (!(service instanceof NoOpService)) {
						logger.log(Level.FINE, "Started {0} in {1}.", new Object[] { service, stopwatch });
					}
				}
				// Queue our listeners

				// Did a service fail?
				if (to == FAILED) {
					this.enqueueFailedEvent(service);
				}

				if (this.states.count(RUNNING) == this.numberOfServices) {
					// This means that the manager is currently healthy. N.B. If other
					// threads call isHealthy
					// they are not guaranteed to get 'true', because any service could
					// fail right now.
					this.enqueueHealthyEvent();
				}
				else if (this.states.count(TERMINATED) + this.states.count(FAILED) == this.numberOfServices) {
					this.enqueueStoppedEvent();
				}
			}
			finally {
				this.monitor.leave();
				// Run our executors outside of the lock
				this.dispatchListenerEvents();
			}
		}

		void enqueueStoppedEvent() {
			this.listeners.enqueue(STOPPED_EVENT);
		}

		void enqueueHealthyEvent() {
			this.listeners.enqueue(HEALTHY_EVENT);
		}

		void enqueueFailedEvent(Service service) {
			this.listeners.enqueue(new ListenerCallQueue.Event<Listener>() {
				@Override
				public void call(Listener listener) {
					listener.failure(service);
				}

				@Override
				public String toString() {
					return "failed({service=" + service + "})";
				}
			});
		}

		/** Attempts to execute all the listeners in {@link #listeners}. */
		void dispatchListenerEvents() {
			checkState(!this.monitor.isOccupiedByCurrentThread(),
					"It is incorrect to execute listeners with the monitor held.");
			this.listeners.dispatch();
		}

		@GuardedBy("monitor")
		void checkHealthy() {
			if (this.states.count(RUNNING) != this.numberOfServices) {
				IllegalStateException exception = new IllegalStateException(
						"Expected to be healthy after starting. The following services are not running: "
								+ Multimaps.filterKeys(this.servicesByState, not(equalTo(RUNNING))));
				for (Service service : this.servicesByState.get(State.FAILED)) {
					exception.addSuppressed(new FailedService(service));
				}
				throw exception;
			}
		}

		@WeakOuter
		final class AwaitHealthGuard extends Monitor.Guard {

			AwaitHealthGuard() {
				super(ServiceManager.ServiceManagerState.this.monitor);
			}

			@Override
			@GuardedBy("ServiceManagerState.this.monitor")
			public boolean isSatisfied() {
				// All services have started or some service has terminated/failed.
				return ServiceManagerState.this.states.count(RUNNING) == ServiceManagerState.this.numberOfServices
						|| ServiceManagerState.this.states.contains(STOPPING)
						|| ServiceManagerState.this.states.contains(TERMINATED)
						|| ServiceManagerState.this.states.contains(FAILED);
			}

		}

		@WeakOuter
		final class StoppedGuard extends Monitor.Guard {

			StoppedGuard() {
				super(ServiceManagerState.this.monitor);
			}

			@Override
			@GuardedBy("ServiceManagerState.this.monitor")
			public boolean isSatisfied() {
				return ServiceManagerState.this.states.count(TERMINATED)
						+ ServiceManagerState.this.states.count(FAILED) == ServiceManagerState.this.numberOfServices;
			}

		}

	}

	/**
	 * A {@link Service} that wraps another service and times how long it takes for it to
	 * start and also calls the
	 * {@link ServiceManagerState#transitionService(Service, State, State)}, to record the
	 * state transitions.
	 */
	private static final class ServiceListener extends Service.Listener {

		final Service service;

		// We store the state in a weak reference to ensure that if something went wrong
		// while
		// constructing the ServiceManager we don't pointlessly keep updating the state.
		final WeakReference<ServiceManagerState> state;

		ServiceListener(Service service, WeakReference<ServiceManagerState> state) {
			this.service = service;
			this.state = state;
		}

		@Override
		public void starting() {
			ServiceManagerState state = this.state.get();
			if (state != null) {
				state.transitionService(this.service, NEW, STARTING);
				if (!(this.service instanceof NoOpService)) {
					logger.log(Level.FINE, "Starting {0}.", this.service);
				}
			}
		}

		@Override
		public void running() {
			ServiceManagerState state = this.state.get();
			if (state != null) {
				state.transitionService(this.service, STARTING, RUNNING);
			}
		}

		@Override
		public void stopping(State from) {
			ServiceManagerState state = this.state.get();
			if (state != null) {
				state.transitionService(this.service, from, STOPPING);
			}
		}

		@Override
		public void terminated(State from) {
			ServiceManagerState state = this.state.get();
			if (state != null) {
				if (!(this.service instanceof NoOpService)) {
					logger.log(Level.FINE, "Service {0} has terminated. Previous state was: {1}",
							new Object[] { this.service, from });
				}
				state.transitionService(this.service, from, TERMINATED);
			}
		}

		@Override
		public void failed(State from, Throwable failure) {
			ServiceManagerState state = this.state.get();
			if (state != null) {
				// Log before the transition, so that if the process exits in response to
				// server failure,
				// there is a higher likelihood that the cause will be in the logs.
				boolean log = !(this.service instanceof NoOpService);
				/*
				 * We have already exposed startup exceptions to the user in the form of
				 * suppressed exceptions. We don't need to log those exceptions again.
				 */
				log &= from != State.STARTING;
				if (log) {
					logger.log(Level.SEVERE, "Service " + this.service + " has failed in the " + from + " state.",
							failure);
				}
				state.transitionService(this.service, from, FAILED);
			}
		}

	}

	/**
	 * A {@link Service} instance that does nothing. This is only useful as a placeholder
	 * to ensure that the {@link ServiceManager} functions properly even when it is
	 * managing no services.
	 *
	 * <p>
	 * The use of this class is considered an implementation detail of ServiceManager and
	 * as such it is excluded from {@link #servicesByState}, {@link #startupTimes},
	 * {@link #toString} and all logging statements.
	 */
	private static final class NoOpService extends AbstractService {

		@Override
		protected void doStart() {
			this.notifyStarted();
		}

		@Override
		protected void doStop() {
			this.notifyStopped();
		}

	}

	/** This is never thrown but only used for logging. */
	private static final class EmptyServiceManagerWarning extends Throwable {

	}

	private static final class FailedService extends Throwable {

		FailedService(Service service) {
			super(service.toString(), service.failureCause(),
					false /* don't enable suppression */,
					false /* don't calculate a stack trace. */);
		}

	}

}
