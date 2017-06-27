package org.sagebionetworks.common.util.progress;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A thread-safe abstract {@link ProgressCallback} that will only allow a single
 * instance of each listeners type at a time.
 * 
 * Use {@link #fireProgressMade(Object)} to forward progress events to all
 * listeners.
 *
 * @param <T>
 */
public abstract class AbstractProgressCallback<T> implements
		ProgressCallback<T> {

	/*
	 * The map of all listeners. This map is not synchronized, so all access to
	 * it should only occur in synchronized methods. LinkedHashMap is used to
	 * ensure progress events are fired in the same order as listeners are
	 * added.
	 */
	private Map<Class<? extends ProgressListener<T>>, ProgressListener<T>> listeners = new LinkedHashMap<Class<? extends ProgressListener<T>>, ProgressListener<T>>();

	/**
	 * Forward a progress event to all listeners.
	 * 
	 * @param t
	 */
	public synchronized void fireProgressMade(T t) {
		for (Class<? extends ProgressListener<T>> key : listeners.keySet()) {
			ProgressListener<T> listener = listeners.get(key);
			listener.progressMade(t);
		}
	}

	/**
	 * 
	 */
	@Override
	public synchronized void addProgressListener(ProgressListener<T> listener) {
		if (listener == null) {
			throw new IllegalArgumentException("Listener cannot be null");
		}
		Class<? extends ProgressListener<T>> key = (Class<? extends ProgressListener<T>>) listener
				.getClass();
		if (listeners.containsKey(key)) {
			throw new IllegalArgumentException(
					"Cannot add more than one listener of type: "
							+ key.getName()
							+ ".  Please remove the prevesouly added listener before adding an additional listener.");
		}
		listeners.put(key, listener);
	}

	@Override
	public synchronized void removeProgressListener(ProgressListener<T> listener) {
		if (listener == null) {
			throw new IllegalArgumentException("Listener cannot be null");
		}
		Class<? extends ProgressListener<T>> key = (Class<? extends ProgressListener<T>>) listener
				.getClass();
		listeners.remove(key);
	}

}
