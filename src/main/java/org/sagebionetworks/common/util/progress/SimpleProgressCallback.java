package org.sagebionetworks.common.util.progress;


/**
 * Simple progress callback.
 *
 * @param <T>
 */
public class SimpleProgressCallback<T> extends AbstractProgressCallback<T>{
	
	@Override
	public void progressMade(T t) {
		fireProgressMade(t);
	}

}
