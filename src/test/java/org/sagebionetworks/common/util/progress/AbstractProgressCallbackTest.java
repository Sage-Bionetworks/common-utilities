package org.sagebionetworks.common.util.progress;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AbstractProgressCallbackTest {

	AbstractProgressCallback abstractProgressCallback;
	
	@Mock
	ProgressListener mockProgressListner;
	
	private int typeOneCallCount;
	private int typeTwoCallCount;
	
	
	@Before
	public void before(){
		MockitoAnnotations.initMocks(this);
		// use the simple callback for testing.
		abstractProgressCallback = new SimpleProgressCallback();
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
		abstractProgressCallback.fireProgressMade();
		verify(mockProgressListner, times(1)).progressMade();
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testAddListnerDuplicate(){
		// call under test
		abstractProgressCallback.addProgressListener(mockProgressListner);
		abstractProgressCallback.addProgressListener(mockProgressListner);
	}
	
	@Test
	public void testAddDuplicateAnonymousInnerClass(){
		ProgressListener typeOneInstanceOne = createAnonymousInnerOne();
		ProgressListener typeOneInstanceTwo = createAnonymousInnerOne();
		// call under test
		abstractProgressCallback.addProgressListener(typeOneInstanceOne);
		try{
			// should fail on added of a second instance of the same type.
			abstractProgressCallback.addProgressListener(typeOneInstanceTwo);
			fail("Should have thrown an exception");
		} catch( IllegalArgumentException e){
			// expected.
		}
		
		ProgressListener typeTwoInstanceOne = createAnonymousInnerTwo();
		// should be able to add a different type.
		abstractProgressCallback.addProgressListener(typeTwoInstanceOne);
		
		// Fire progress and check each type is called once.
		assertEquals(0, typeOneCallCount);
		assertEquals(0, typeTwoCallCount);
		abstractProgressCallback.fireProgressMade();
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
		abstractProgressCallback.removeProgressListener(null);
	}
	
	@Test
	public void testRemoveListner(){
		abstractProgressCallback.addProgressListener(mockProgressListner);
		// call under test
		abstractProgressCallback.removeProgressListener(mockProgressListner);
		abstractProgressCallback.fireProgressMade();
		verify(mockProgressListner, never()).progressMade();
	}

}
