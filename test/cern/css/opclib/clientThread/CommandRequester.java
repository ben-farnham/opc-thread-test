/**
 * 
 */
package cern.css.opclib.clientThread;

import java.util.concurrent.Callable;

import ch.ess.opclib.clientThread.OPCClient;

class CommandRequester implements Callable<Boolean>
{
	private final OPCClient opcClient;
	private final String opcItemAddress;
	
	public CommandRequester(OPCClient opcClient, String opcItemAddress)
	{
		this.opcClient = opcClient;
		this.opcItemAddress = opcItemAddress;		
	}
		
	@Override
	public Boolean call() throws Exception 
	{
		boolean result = opcClient.readBoolean(opcItemAddress);
		return Boolean.valueOf(result);
	}
}