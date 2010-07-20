package cern.ess.opclib.clientThread;

import java.util.concurrent.SynchronousQueue;

import cern.ess.opclib.OPCException;
import cern.ess.opclib.OpcApi;

public class WriteBooleanCommand extends OPCWriteCommandBase implements OPCCommand {

	public WriteBooleanCommand(OpcApi opcInterface, String opcItemAddress,
			SynchronousQueue<OPCCommand> requestQueue,
			SynchronousQueue<OPCCommandResult> responseQueue,
			Object valueToWrite) 
	{
		super(opcInterface, opcItemAddress, requestQueue, responseQueue, valueToWrite);
	}

	@Override
	public Object execute() throws OPCException, InterruptedException 
	{
		getOpcApi().writeBoolean(getOpcItemAddress(), ((Boolean)getValueToWrite()).booleanValue());
		return new Object();
	}

}
