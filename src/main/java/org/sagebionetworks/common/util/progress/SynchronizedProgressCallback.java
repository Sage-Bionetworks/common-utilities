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
 */
public class SynchronizedProgressCallback implements ProgressCallback {

	/*
	 * The map of all listeners. This map is not synchronized, so all access to
	 * it should only occur in synchronized methods. LinkedHashMap is used to
	 * ensure progress events are fired in the same order as listeners are
	 * added.
	 */
	private Map<Class<? extends ProgressListener>, ProgressListener> listeners = new LinkedHashMap<Class<? extends ProgressListener>, ProgressListener>();
	private boolean shouldTerminate = false;
	/**
	 * Forward a progress event to all listeners.
	 * 
	 * @param t
	 */
	public synchronized void fireProgressMade() {
		for (ProgressListener listener : listeners.values()) {
			try {
				listener.progressMade();
			}catch (Exception e){
				this.shouldTerminate = true;
				throw e;
			}
		}
	}

	public synchronized boolean runnerShouldTerminate() {
		return shouldTerminate;
	}

	/**
	 * 
	 */
	@Override
	public synchronized void addProgressListener(ProgressListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("Listener cannot be null");
		}
		Class<? extends ProgressListener> key = (Class<? extends ProgressListener>) listener
				.getClass();
		if (listeners.containsKey(key)) {
			throw new IllegalArgumentException(
					"Cannot add more than one listener of type: "
							+ key.getName()
							+ ".  Please remove the previously added listener before adding an additional listener.");
		}
		listeners.put(key, listener);
	}

	@Override
	public synchronized void removeProgressListener(ProgressListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("Listener cannot be null");
		}
		Class<? extends ProgressListener> key = (Class<? extends ProgressListener>) listener
				.getClass();
		listeners.remove(key);
	}

}
