/**
 * 
 */
package cern.css.opclib.clientThread;

import java.util.concurrent.Callable;

import cern.ess.opclib.OpcApi;

public class CommandRequester implements Callable<Boolean>
{
	private final OpcApi opcClient;
	private final String opcItemAddress;
	
	public CommandRequester(OpcApi opcClient, String opcItemAddress)
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