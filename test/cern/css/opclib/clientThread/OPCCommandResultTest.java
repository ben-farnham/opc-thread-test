package cern.css.opclib.clientThread;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cern.ess.opclib.OPCException;
import ch.ess.opclib.clientThread.OPCCommandResult;


public class OPCCommandResultTest 
{
	private OPCCommandResult testee;
	
	@Test
	public void testSuccessResult()
	{
		Object resultValue = new Object();
		testee = new OPCCommandResult(resultValue);
		
		assertTrue(testee.isSuccess());
		assertEquals(resultValue, testee.getResult());
		assertNull(testee.getException());
	}
	
	@Test
	public void testExceptionResult()
	{
		OPCException exception = new OPCException("random failure");
		testee = new OPCCommandResult(exception);
		
		assertFalse(testee.isSuccess());
		assertNull(testee.getResult());
		assertEquals(exception, testee.getException());
	}
	
	
}
