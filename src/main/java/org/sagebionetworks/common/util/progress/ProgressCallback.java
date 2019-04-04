package org.sagebionetworks.common.util.progress;

/**
 * A callback that will notify progress listeners when progress is made.
 *
 */
public interface ProgressCallback {

	/**
	 * Progress listeners are notified as progress is made. Listeners must be
	 * removed when no more progress events are expected.
	 * 
	 * @param listener
	 */
	public void addProgressListener(ProgressListener listener);

	/**
	 * Remove the given progress listener. The given listener will no longer
	 * receive messages.
	 * 
	 * @param listener
	 */
	public void removeProgressListener(ProgressListener listener);


	/**
	 * Indicates whether the callers of this should terminate.
	 * This occurs when something has gone wrong with the progress listeners
	 * @return
	 */
	public boolean runnerShouldTerminate();
}
