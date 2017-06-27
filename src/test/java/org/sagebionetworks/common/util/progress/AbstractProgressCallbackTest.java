package org.sagebionetworks.common.util.progress;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AbstractProgressCallbackTest {

	AbstractProgressCallback<Void> abstractProgressCallback;
	
	@Mock
	ProgressListener<Void> mockProgressListner;
	
	private int typeOneCallCount;
	private int typeTwoCallCount;
	
	
	@Before
	public void before(){
		MockitoAnnotations.initMocks(this);
		// use the simple callback for testing.
		abstractProgressCallback = new SimpleProgressCallback<Void>();
		typeOneCallCount = 0;
		typeTwoCallCount = 0;
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testAddListnerNull(){
		abstractProgressCallback.addProgressListener(null);
	}
	
	@Test
	public void testAddListner(){
		// call under test
		abstractProgressCallback.addProgressListener(mockProgressListner);
		abstractProgressCallback.fireProgressMade(null);
		verify(mockProgressListner, times(1)).progressMade(null);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testAddListnerDuplicate(){
		// call under test
		abstractProgressCallback.addProgressListener(mockProgressListner);
		abstractProgressCallback.addProgressListener(mockProgressListner);
	}
	
	@Test
	public void testAddDuplicateAnonymousInnerClass(){
		ProgressListener<Void> typeOneInstanceOne = createAnonymousInnerOne();
		ProgressListener<Void> typeOneInstanceTwo = createAnonymousInnerOne();
		// call under test
		abstractProgressCallback.addProgressListener(typeOneInstanceOne);
		try{
			// should fail on added of a second instance of the same type.
			abstractProgressCallback.addProgressListener(typeOneInstanceTwo);
			fail("Should have thrown an exception");
		} catch( IllegalArgumentException e){
			// expected.
		}
		
		ProgressListener<Void> typeTwoInstanceOne = createAnonymousInnerTwo();
		// should be able to add a different type.
		abstractProgressCallback.addProgressListener(typeTwoInstanceOne);
		
		// Fire progress and check each type is called once.
		assertEquals(0, typeOneCallCount);
		assertEquals(0, typeTwoCallCount);
		abstractProgressCallback.fireProgressMade(null);
		assertEquals(1, typeOneCallCount);
		assertEquals(1, typeTwoCallCount);
		
	}
	
	/**
	 * Helper to create an anonymous inner ProgressListener class.
	 * @return
	 */
	public ProgressListener<Void> createAnonymousInnerOne(){
		return new ProgressListener<Void>() {
			
			@Override
			public void progressMade(Void t) {
				typeOneCallCount++;
			}
		};
	}
	
	/**
	 * Helper to create a different type of anonymous inner ProgressListener class.
	 * @return
	 */
	public ProgressListener<Void> createAnonymousInnerTwo(){
		return new ProgressListener<Void>() {
			
			@Override
			public void progressMade(Void t) {
				typeTwoCallCount++;
			}
		};
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testRemoveListnerNull(){
		// call under test
		abstractProgressCallback.removeProgressListener(null);
	}
	
	@Test
	public void testRemoveListner(){
		abstractProgressCallback.addProgressListener(mockProgressListner);
		// call under test
		abstractProgressCallback.removeProgressListener(mockProgressListner);
		abstractProgressCallback.fireProgressMade(null);
		verify(mockProgressListner, never()).progressMade(null);
	}

}
