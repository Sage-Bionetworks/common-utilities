package org.sagebionetworks.common.util.progress;

/**
 * Abstract listener to progress events.
 *
 * @param <T>
 */
public interface ProgressListener<T> {

	/**
	 * Called when progress is made.
	 * 
	 * @param t
	 */
	public void progressMade(T t);
}
