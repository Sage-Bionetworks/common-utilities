package org.sagebionetworks.common.util.progress;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * An implementation of ProgressingCallable that can provide progress events for
 * a {@link java.util.concurrent.Callable}. The provided Callable will be run on
 * separate thread. While the Callable is running progress events will be made
 * on the original calling thread at the provided frequency.
 * 
 * @param <R>
 *            The return type of the callable.
 *            {@link #call(Object)}.
 * @param <T>
 *            The parameter type passed to the
 *            {@link ProgressCallback#progressMade(Object)}.
 */
public class AutoProgressingCallable<R, T> implements ProgressingCallable<R, T> {

	ExecutorService executor;
	Callable<R> callable;
	long progressFrequencyMs;
	T parameter;

	/**
	 * Create a new AutoProgressingCallable for each use.
	 * 
	 * @param executor
	 *            Provides threading
	 * @param callable
	 *            Callable to be run on a separate thread. Progress events will
	 *            be made on the calling thread while the callable runs on the
	 *            separate thread.
	 * @param progressFrequencyMs
	 *            The frequency in MS at which progress events will be made
	 *            while waiting for the callable's thread.
	 * @param parameter
	 *            The parameter to be passed to the progress callback.
	 */
	public AutoProgressingCallable(ExecutorService executor,
			Callable<R> callable, long progressFrequencyMs, T parameter) {
		super();
		if(executor == null){
			throw new IllegalArgumentException("Executor cannot be null");
		}
		if(callable == null){
			throw new IllegalArgumentException("Callable cannot be null");
		}
		this.executor = executor;
		this.callable = callable;
		this.progressFrequencyMs = progressFrequencyMs;
		this.parameter = parameter;
	}

	@Override
	public R call(ProgressCallback<T> callback) throws Exception {
		// start the process
		Future<R> future = executor.submit(callable);
		// make progress at least once.
		callback.progressMade(parameter);
		while (true) {
			// wait for the process to finish
			try {
				return future.get(progressFrequencyMs, TimeUnit.MILLISECONDS);
			}catch (ExecutionException e) {
				// concert to the cause if we can.
				Throwable cause = e.getCause();
				if(cause instanceof Exception){
					throw ((Exception)cause);
				}
				throw e;
			}catch (TimeoutException e) {
				// make progress for each timeout
				callback.progressMade(parameter);
			}
		}
	}

}
