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
	ProgressListener<String> mockListener;
	
	@Before
	public void before(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testThrottleDuplicateListener(){
		long frequency = 1000;
		ThrottlingProgressCallback<String> throttle = new ThrottlingProgressCallback<String>(frequency);
		// call under test
		throttle.addProgressListener(mockListener);
		// this should fail
		throttle.addProgressListener(mockListener);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testThrottle() throws InterruptedException{
		long frequency = 1000;
		int maxNumberListeners = 1;
		ThrottlingProgressCallback<String> throttle = new ThrottlingProgressCallback<String>(frequency);
		throttle.addProgressListener(mockListener);
		// the first call should get forwarded
		throttle.progressMade("foo");
		verify(mockListener).progressMade("foo");
		reset(mockListener);
		// Next call should not go through
		throttle.progressMade("foo");
		verify(mockListener, never()).progressMade(anyString());
		// wait for enough to to pass.
		Thread.sleep(frequency+1);
		throttle.progressMade("foo");
		verify(mockListener).progressMade("foo");
		reset(mockListener);
		// Next call should not go through
		throttle.progressMade("foo");
		verify(mockListener, never()).progressMade(anyString());
	}

	
	@Test
	public void testRemoveListeners() throws InterruptedException{
		long frequency = 1;
		int maxNumberListeners = 1;
		ThrottlingProgressCallback<String> throttle = new ThrottlingProgressCallback<String>(frequency);
		throttle.addProgressListener(mockListener);
		throttle.progressMade("foo");
		verify(mockListener).progressMade("foo");
		reset(mockListener);
		// wait for enough to to pass.
		Thread.sleep(frequency+1);
		throttle.removeProgressListener(mockListener);
		throttle.progressMade("foo");
		verify(mockListener, never()).progressMade("foo");
	}

}
