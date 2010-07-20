package ch.ess.opclib.clientThread;

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
	public void execute() throws OPCException, InterruptedException 
	{
		getOpcApi().writeString(getOpcItemAddress(), (String)getValueToWrite());
		responseQueue.put(new OPCCommandResult(new Object()));
	}

}
