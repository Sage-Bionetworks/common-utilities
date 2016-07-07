package org.sagebionetworks.common.util.progress;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AutoProgressingCallableTest {

	@Mock
	ExecutorService mockExecutor;

	@Mock
	Future<Integer> mockFuture;

	@Mock
	Callable<Integer> mockCallable;
	@Mock
	ProgressCallback<Void> mockCallback;

	AutoProgressingCallable<Integer> auto;

	Integer returnValue;
	long progressFrequencyMs;

	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);
		returnValue = 101;

		progressFrequencyMs = 1000;
		when(mockExecutor.submit(mockCallable)).thenReturn(mockFuture);
		// throw timeout twice then return a value.
		when(mockFuture.get(anyLong(), any(TimeUnit.class)))
				.thenThrow(new TimeoutException())
				.thenThrow(new TimeoutException()).thenReturn(returnValue);

		auto = new AutoProgressingCallable<Integer>(mockExecutor,
				mockCallable, progressFrequencyMs);
	}

	@Test
	public void testCallHappy() throws Exception {
		// call under test.
		Integer result = auto.call(mockCallback);
		assertEquals(returnValue, result);
		verify(mockExecutor).submit(mockCallable);
		verify(mockCallback, times(3)).progressMade(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCallNonTimeoutException() throws Exception {
		reset(mockFuture);
		// future fails with non
		when(mockFuture.get(anyLong(), any(TimeUnit.class))).thenThrow(
				new ExecutionException(new IllegalArgumentException("Unexpected")));
		// call under test.
		auto.call(mockCallback);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testNullExecutor(){
		mockExecutor = null;
		auto = new AutoProgressingCallable<Integer>(mockExecutor,
				mockCallable, progressFrequencyMs);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testNullCallable(){
		mockCallable = null;
		auto = new AutoProgressingCallable<Integer>(mockExecutor,
				mockCallable, progressFrequencyMs);
	}
	

}
