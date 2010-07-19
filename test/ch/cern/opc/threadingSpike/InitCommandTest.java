package ch.cern.opc.threadingSpike;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class InitCommandTest 
{
	private OPCCommand testee;
	
	@Before
	public void setup()
	{
		testee = new InitCommand(new MockOpcApiImpl(), null, null, null, null);
	}
	
	@Test
	public void testIsInitCommand()
	{
		assertTrue(testee.isInitCommand());
	}	
}
