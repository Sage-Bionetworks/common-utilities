package org.sagebionetworks.common.util.progress;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SynchronizedProgressCallbackTest {

	SynchronizedProgressCallback synchronizedProgressCallback;
	
	@Mock
	ProgressListener mockProgressListener;

	static interface OtherProgressListenerInterface extends ProgressListener {}
	@Mock
	OtherProgressListenerInterface mockProgressListener2;

	private int typeOneCallCount;
	private int typeTwoCallCount;
	
	
	@Before
	public void before(){
		MockitoAnnotations.initMocks(this);
		// use the simple callback for testing.
		synchronizedProgressCallback = new SynchronizedProgressCallback();
		typeOneCallCount = 0;
		typeTwoCallCount = 0;
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testAddListnerNull(){
		synchronizedProgressCallback.addProgressListener(null);
	}
	
	@Test
	public void testAddListner(){
		// call under test
		synchronizedProgressCallback.addProgressListener(mockProgressListener);
		synchronizedProgressCallback.fireProgressMade();
		verify(mockProgressListener, times(1)).progressMade();
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testAddListnerDuplicate(){
		// call under test
		synchronizedProgressCallback.addProgressListener(mockProgressListener);
		synchronizedProgressCallback.addProgressListener(mockProgressListener);
	}
	
	@Test
	public void testAddDuplicateAnonymousInnerClass(){
		ProgressListener typeOneInstanceOne = createAnonymousInnerOne();
		ProgressListener typeOneInstanceTwo = createAnonymousInnerOne();
		// call under test
		synchronizedProgressCallback.addProgressListener(typeOneInstanceOne);
		try{
			// should fail on added of a second instance of the same type.
			synchronizedProgressCallback.addProgressListener(typeOneInstanceTwo);
			fail("Should have thrown an exception");
		} catch( IllegalArgumentException e){
			// expected.
		}
		
		ProgressListener typeTwoInstanceOne = createAnonymousInnerTwo();
		// should be able to add a different type.
		synchronizedProgressCallback.addProgressListener(typeTwoInstanceOne);
		
		// Fire progress and check each type is called once.
		assertEquals(0, typeOneCallCount);
		assertEquals(0, typeTwoCallCount);
		synchronizedProgressCallback.fireProgressMade();
		assertEquals(1, typeOneCallCount);
		assertEquals(1, typeTwoCallCount);
		
	}
	
	/**
	 * Helper to create an anonymous inner ProgressListener class.
	 * @return
	 */
	public ProgressListener createAnonymousInnerOne(){
		return new ProgressListener() {
			
			@Override
			public void progressMade() {
				typeOneCallCount++;
			}
		};
	}
	
	/**
	 * Helper to create a different type of anonymous inner ProgressListener class.
	 * @return
	 */
	public ProgressListener createAnonymousInnerTwo(){
		return new ProgressListener() {
			
			@Override
			public void progressMade() {
				typeTwoCallCount++;
			}
		};
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testRemoveListnerNull(){
		// call under test
		synchronizedProgressCallback.removeProgressListener(null);
	}
	
	@Test
	public void testRemoveListner(){
		synchronizedProgressCallback.addProgressListener(mockProgressListener);
		// call under test
		synchronizedProgressCallback.removeProgressListener(mockProgressListener);
		synchronizedProgressCallback.fireProgressMade();
		verify(mockProgressListener, never()).progressMade();
	}

	@Test
	public void testRunnerShouldTerminate_progressListenersFailed(){
		synchronizedProgressCallback.addProgressListener(mockProgressListener);
		synchronizedProgressCallback.addProgressListener(mockProgressListener2);

		//method under test
		assertFalse(synchronizedProgressCallback.runnerShouldTerminate());

		//make an exception occur for listeners
		RuntimeException exception = new RuntimeException("test exception");
		doThrow(exception).when(mockProgressListener).progressMade();
		try {
			synchronizedProgressCallback.fireProgressMade();
			fail();
		} catch (RuntimeException e){
			assertEquals(exception, e);
		}

		//method under test
		assertTrue(synchronizedProgressCallback.runnerShouldTerminate());

		verify(mockProgressListener, times(1)).progressMade();
		verifyZeroInteractions(mockProgressListener2);

	}

	@Test
	public void testRunnerShouldTerminate_progressListenersNotFailed(){
		synchronizedProgressCallback.addProgressListener(mockProgressListener);
		synchronizedProgressCallback.addProgressListener(mockProgressListener2);

		//method under test
		assertFalse(synchronizedProgressCallback.runnerShouldTerminate());

		//No exceptions occurred for listeners
		doNothing().when(mockProgressListener).progressMade();

		synchronizedProgressCallback.fireProgressMade();

		//method under test
		assertFalse(synchronizedProgressCallback.runnerShouldTerminate());

		verify(mockProgressListener, times(1)).progressMade();
		verify(mockProgressListener2, times(1)).progressMade();
	}
}
