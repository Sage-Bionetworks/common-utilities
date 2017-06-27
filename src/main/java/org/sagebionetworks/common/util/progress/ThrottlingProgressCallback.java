package org.sagebionetworks.common.util.progress;

import org.sagebionetworks.common.util.Clock;
import org.sagebionetworks.common.util.ClockImpl;


/**
 * This implementation of a ProgressCallback limits the frequency at which
 * progress events that are forwarded to listeners. The first call made to
 * {@link #progressMade(Object)} will be forwarded to the listeners, while all
 * subsequent calls will only be forwarded to the listeners if the configured
 * frequency time in MS has elapsed since the last forwarded call.
 * 
 * This throttle allows a worker to call {@link #progressMade(Object)} as
 * frequently as possible without overwhelming the listeners.
 * 
 * @param <T>
 */
public final class ThrottlingProgressCallback<T> extends AbstractProgressCallback<T> {

	long frequencyMS;
	long lastFiredTime;
	Clock clock;

	/**
	 * @param targetCallback Calls to {@link #progressMade(Object)} will be forward to this target unless throttled.
	 * @param frequencyMS The frequency in milliseconds that calls should be forwarded to the target.
	 */
	public ThrottlingProgressCallback(long frequencyMS) {
		this(frequencyMS, new ClockImpl());
	}

	/**
	 * 
	 * @param targetCallback
	 * @param frequencyMS
	 * @param clock
	 */
	public ThrottlingProgressCallback(long frequencyMS, Clock clock) {
		super();
		this.frequencyMS = frequencyMS;
		if(clock == null){
			throw new IllegalArgumentException("Clock cannot be null");
		}
		this.clock = clock;
		this.lastFiredTime = -1;
	}


	@Override
	public void progressMade(T t) {
		long now = clock.currentTimeMillis();
		if (this.lastFiredTime < 0) {
			// first call is forwarded.
			this.lastFiredTime = now;
			fireProgressMade(t);
		} else {
			if (now - this.lastFiredTime > frequencyMS) {
				// first call is forwarded.
				this.lastFiredTime = now;
				fireProgressMade(t);
			}
		}
	}
}
