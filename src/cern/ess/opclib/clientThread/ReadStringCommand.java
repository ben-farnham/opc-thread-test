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
	public Object execute() throws OPCException, InterruptedException 
	{
		return getOpcApi().readString(getOpcItemAddress());
	}

}
