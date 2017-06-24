package org.sagebionetworks.common.util.progress;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ThrottlingProgressCallbackTest {
	
	@Mock
	ProgressListener<String> mockListener;
	
	@Before
	public void before(){
		MockitoAnnotations.initMocks(this);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testThrottle() throws InterruptedException{
		long frequency = 1000;
		int maxNumberListeners = 1;
		ThrottlingProgressCallback<String> throttle = new ThrottlingProgressCallback<String>(frequency, maxNumberListeners);
		throttle.addProgressListener(mockListener);
		// adding the listener again should not result in double messages
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
	
	@Test (expected=IllegalArgumentException.class)
	public void testAddMaxNumberOfListeners(){
		long frequency = 1000;
		int maxNumberListeners = 1;
		ThrottlingProgressCallback<String> throttle = new ThrottlingProgressCallback<String>(frequency, maxNumberListeners);
		for(int i = 0; i <maxNumberListeners+1; i++){
			ProgressListener<String> listener = Mockito.mock(ProgressListener.class);
			throttle.addProgressListener(listener);
		}
	}
	
	@Test
	public void testRemoveListeners() throws InterruptedException{
		long frequency = 1;
		int maxNumberListeners = 1;
		ThrottlingProgressCallback<String> throttle = new ThrottlingProgressCallback<String>(frequency, maxNumberListeners);
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
