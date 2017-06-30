package org.sagebionetworks.common.util.progress;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ThrottlingProgressCallbackTest {
	
	@Mock
	ProgressListener mockListener;
	
	@Before
	public void before(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testThrottleDuplicateListener(){
		long frequency = 1000;
		ThrottlingProgressCallback throttle = new ThrottlingProgressCallback(frequency);
		// call under test
		throttle.addProgressListener(mockListener);
		// this should fail
		throttle.addProgressListener(mockListener);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testThrottle() throws InterruptedException{
		long frequency = 1000;
		ThrottlingProgressCallback throttle = new ThrottlingProgressCallback(frequency);
		throttle.addProgressListener(mockListener);
		// the first call should get forwarded
		throttle.progressMade();
		verify(mockListener).progressMade();
		reset(mockListener);
		// Next call should not go through
		throttle.progressMade();
		verify(mockListener, never()).progressMade();
		// wait for enough to to pass.
		Thread.sleep(frequency+1);
		throttle.progressMade();
		verify(mockListener).progressMade();
		reset(mockListener);
		// Next call should not go through
		throttle.progressMade();
		verify(mockListener, never()).progressMade();
	}

	
	@Test
	public void testRemoveListeners() throws InterruptedException{
		long frequency = 1;
		ThrottlingProgressCallback throttle = new ThrottlingProgressCallback(frequency);
		throttle.addProgressListener(mockListener);
		throttle.progressMade();
		verify(mockListener).progressMade();
		reset(mockListener);
		// wait for enough to to pass.
		Thread.sleep(frequency+1);
		throttle.removeProgressListener(mockListener);
		throttle.progressMade();
		verify(mockListener, never()).progressMade();
	}

}
