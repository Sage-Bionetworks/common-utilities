package org.sagebionetworks.common.util.progress;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class AutoProgressingCallableTest {

	@Mock
	ExecutorService mockExecutor;

	@Mock
	Future<Integer> mockFuture;

	@Mock
	ProgressingCallable<Integer, Void> mockCallable;
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
		doAnswer(new Answer<Future<Integer>>(){

			@Override
			public Future<Integer> answer(InvocationOnMock invocation)
					throws Throwable {
				Callable<Integer> callable = (Callable<Integer>) invocation.getArguments()[0];
				callable.call();
				return mockFuture;
			}}).when(mockExecutor).submit(any(Callable.class));
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
		verify(mockExecutor).submit(any(Callable.class));
		verify(mockCallback, times(3)).progressMade(null);
		verify(mockCallable).call(mockCallback);
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
