package cern.ess.opclib.clientThread;

import java.util.concurrent.SynchronousQueue;

import cern.ess.opclib.OPCException;
import cern.ess.opclib.OpcApi;

public class ReadStringCommand extends OPCCommandBase implements OPCCommand {

	public ReadStringCommand(OpcApi opcInterface, String opcItemAddress,
			SynchronousQueue<OPCCommand> requestQueue,
			SynchronousQueue<OPCCommandResult> responseQueue) 
	{
		super(opcInterface, opcItemAddress, requestQueue, responseQueue);
	}

	@Override
	public void execute() throws OPCException, InterruptedException 
	{
		String value = getOpcApi().readString(getOpcItemAddress());
		responseQueue.put(new OPCCommandResult(value));
	}

}
