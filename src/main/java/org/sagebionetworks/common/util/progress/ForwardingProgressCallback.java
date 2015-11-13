package org.sagebionetworks.common.util.progress;

import org.sagebionetworks.common.util.progress.ProgressCallback;

/**
 * A ProgressCallback that forwards progressMade() call to a provided callback.
 * 
 * @param <T>
 *            The parameter type passed into this object.
 *            {@link ProgressCallback#progressMade(Object)}.
 * @param <R>
 *            The parameter type forwarded to the provided callback.
 */
public class ForwardingProgressCallback<T, R> implements ProgressCallback<T> {

	final ProgressCallback<R> forwardTo;
	final R paramterOut;

	/**
	 * When this instance receives a progressMade() call it will forward the
	 * call to the provided callback, passing the provided parameter.
	 * 
	 * @param forwardTo
	 *            The callback to forward to.
	 * @param forwardParam
	 *            The parameter to passed to the forward callback.
	 */
	public ForwardingProgressCallback(ProgressCallback<R> forwardTo,
			R forwardParam) {
		if(forwardTo == null){
			throw new IllegalArgumentException("forwardTo cannot be null");
		}
		this.forwardTo = forwardTo;
		this.paramterOut = forwardParam;
	}

	@Override
	public void progressMade(T param) {
		forwardTo.progressMade(paramterOut);
	}

}
