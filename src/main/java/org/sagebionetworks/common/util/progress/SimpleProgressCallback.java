package org.sagebionetworks.common.util.progress;


/**
 * Simple progress callback.
 *
 * @param <T>
 */
public class SimpleProgressCallback extends AbstractProgressCallback {

	@Override
	public void progressMade() {
		fireProgressMade();
	}
	
}
