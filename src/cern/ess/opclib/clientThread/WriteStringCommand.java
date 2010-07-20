package cern.ess.opclib.clientThread;

import java.util.concurrent.SynchronousQueue;

import cern.ess.opclib.OPCException;
import cern.ess.opclib.OpcApi;

public class WriteStringCommand extends OPCWriteCommandBase implements OPCCommand 
{

	public WriteStringCommand(OpcApi opcInterface, String opcItemAddress,
			SynchronousQueue<OPCCommand> requestQueue,
			SynchronousQueue<OPCCommandResult> responseQueue,
			Object valueToWrite) 
	{
		super(opcInterface, opcItemAddress, requestQueue, responseQueue, valueToWrite);
	}

	@Override
	public Object execute() throws OPCException, InterruptedException 
	{
		getOpcApi().writeString(getOpcItemAddress(), (String)getValueToWrite());
		return new Object();
	}

}
