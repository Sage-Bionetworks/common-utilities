package org.sagebionetworks.common.util.progress;

/**
 * A callback for workers that need to notify containers that progress is being
 * made.
 *
 * @param <T>
 *            The parameter type passed to the
 *            {@link ProgressCallback#progressMade(Object)}.
 */
public interface ProgressCallback<T> extends ProgressListener<T> {

	/**
	 * Progress listeners are notified as progress is made. Listeners must be
	 * removed when no more progress events are expected.
	 * 
	 * @param listener
	 */
	public void addProgressListener(ProgressListener<T> listener);

	/**
	 * Remove the given progress listener. The given listener will no longer
	 * receive messages.
	 * 
	 * @param listener
	 */
	public void removeProgressListener(ProgressListener<T> listener);
}
