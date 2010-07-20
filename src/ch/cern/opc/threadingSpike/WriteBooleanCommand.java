package ch.cern.opc.threadingSpike;

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
	public void execute() throws OPCException, InterruptedException 
	{
		getOpcApi().writeBoolean(getOpcItemAddress(), ((Boolean)getValueToWrite()).booleanValue());
		responseQueue.put(new OPCCommandResult(new Object()));
	}

}
