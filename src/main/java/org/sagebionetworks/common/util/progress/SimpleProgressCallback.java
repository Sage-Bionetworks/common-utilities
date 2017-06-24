package org.sagebionetworks.common.util.progress;

import java.util.LinkedList;
import java.util.List;

/**
 * Simple progress callback.
 *
 * @param <T>
 */
public class SimpleProgressCallback<T> implements ProgressCallback<T>{
	
	List<ProgressListener<T>> listeners = new LinkedList<ProgressListener<T>>();

	@Override
	public void progressMade(T t) {
		for(ProgressListener<T> listener: listeners){
			listener.progressMade(t);
		}
	}

	@Override
	public void addProgressListener(ProgressListener<T> listener) {
		listeners.add(listener);
	}

	@Override
	public void removeProgressListener(ProgressListener<T> listener) {
		listeners.remove(listener);
	}

}
