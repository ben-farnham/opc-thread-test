package ch.cern.opc.threadingSpike;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class ReadBooleanCommandTest 
{
	private ReadBooleanCommand testee;
	
	@Before
	public void setup()
	{
		testee = new ReadBooleanCommand(null, null, null, null);		
	}
	
	@Test
	public void testGetCommandName()
	{
		assertEquals("ReadBooleanCommand", testee.getCommandName());
	}
}