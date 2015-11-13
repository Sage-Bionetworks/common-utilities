package org.sagebionetworks.common.util.progress;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

public class ForwardingProgressCallbackTest {
	
	ProgressCallback<String> mockForwardTo;
	String forwardParameter;
	
	@Before
	public void before(){
		mockForwardTo = Mockito.mock(ProgressCallback.class);
		forwardParameter = "forwardMe";
	}
	
	@Test
	public void testProgressMade(){
		ForwardingProgressCallback<Integer, String> forwarding = new ForwardingProgressCallback<Integer, String>(mockForwardTo, forwardParameter);
		// call under test
		forwarding.progressMade(123);
		//should be forwarded
		verify(mockForwardTo).progressMade(forwardParameter);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testForwardToNull(){
		// call under test
		ForwardingProgressCallback<Integer, String> forwarding = new ForwardingProgressCallback<Integer, String>(null, forwardParameter);
	}
	
	@Test
	public void testProgressMadeParamNull(){
		forwardParameter = null;
		ForwardingProgressCallback<Integer, String> forwarding = new ForwardingProgressCallback<Integer, String>(mockForwardTo, forwardParameter);
		// call under test
		forwarding.progressMade(123);
		//should be forwarded
		verify(mockForwardTo).progressMade(forwardParameter);
	}

}
