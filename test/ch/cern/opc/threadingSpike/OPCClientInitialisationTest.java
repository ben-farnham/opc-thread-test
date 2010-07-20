package ch.cern.opc.threadingSpike;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cern.ess.opclib.OPCException;

public class OPCClientInitialisationTest 
{
	private OPCClient testee;
	private MockOpcApiImpl mockOpcApi;
	
	@Before
	public void setup() throws OPCException
	{
		mockOpcApi = new MockOpcApiImpl();
			
		testee = new OPCClient(mockOpcApi);
		testee.start();
	}
	
	@After
	public void teardown()
	{
		testee.stop();
	}

	@Test
	public void testFirstInitCommandCallsInit() throws OPCException
	{
		assertEquals(0, mockOpcApi.getNumberOfTimesInitWasCalled());
		
		testee.init("host", "server");
		assertEquals(1, mockOpcApi.getNumberOfTimesInitWasCalled());
		
		testee.init("another host", "another server");
		assertEquals(1, mockOpcApi.getNumberOfTimesInitWasCalled());
	}
	
	@Test
	public void testInitialiseCommandRequiredBeforeProcessingAnyOtherCommands()
	{
		mockOpcApi.getOpcItemValues().put("whatever", Integer.valueOf(1));
		try
		{
			testee.readInt("whatever");
			fail("expected exception to be thrown");
		}
		catch(OPCException e)
		{
			assertEquals("init must be called before any other operation", e.getMessage());
		}
		
	}
}
