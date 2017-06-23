package org.sagebionetworks.common.util.progress;

/**
 * A callback for workers that need to notify containers that progress is being made.
 *
 * @param <T> The parameter type passed to the
 *            {@link ProgressCallback#progressMade(Object)}.
 */
public interface ProgressCallback<T> extends ProgressListener<T> {
	
	/**
	 * Progress listeners are notified as progress is made.
	 * 
	 * @param listener
	 */
	public void addProgressListener(ProgressListener<T> listener);
}
