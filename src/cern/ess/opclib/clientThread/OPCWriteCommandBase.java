package cern.ess.opclib.clientThread;

import java.util.concurrent.SynchronousQueue;

import cern.ess.opclib.OpcApi;

public abstract class OPCWriteCommandBase extends OPCCommandBase implements OPCCommand 
{
	private final Object valueToWrite;
	
	public OPCWriteCommandBase(OpcApi opcInterface, String opcItemAddress,
			SynchronousQueue<OPCCommand> requestQueue,
			SynchronousQueue<OPCCommandResult> responseQueue,
			final Object valueToWrite) 
	{
		super(opcInterface, opcItemAddress, requestQueue, responseQueue);
		this.valueToWrite = valueToWrite;
	}
	
	public Object getValueToWrite()
	{
		return valueToWrite;
	}
}
