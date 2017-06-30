package org.sagebionetworks.common.util.progress;

/**
 * Abstract listener to progress events.
 * 
 */
public interface ProgressListener {

	/**
	 * Called when progress is made.
	 * 
	 * Note: Progress events will be fired on a separate thread from the thread
	 * the worker is running on. This allows progress events to be generated
	 * even when the worker thread is block. Therefore, thread safety must be
	 * exercised for all objects access by both the progress listener and
	 * worker. Also, progress listeners cannot participate in database
	 * transactions started by the worker, and vis versa.
	 */
	public void progressMade();
}
