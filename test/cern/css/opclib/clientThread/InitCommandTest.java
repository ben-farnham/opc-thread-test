package cern.css.opclib.clientThread;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cern.ess.opclib.clientThread.InitCommand;
import cern.ess.opclib.clientThread.OPCCommand;


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
