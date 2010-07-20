package ch.ess.opclib.clientThread;

import java.util.concurrent.SynchronousQueue;

import cern.ess.opclib.OPCException;
import cern.ess.opclib.OpcApi;

public class ReadIntCommand extends OPCCommandBase implements OPCCommand {

	public ReadIntCommand(OpcApi opcInterface, String opcItemAddress,
			SynchronousQueue<OPCCommand> requestQueue,
			SynchronousQueue<OPCCommandResult> responseQueue) 
	{
		super(opcInterface, opcItemAddress, requestQueue, responseQueue);
	}

	@Override
	public void execute() throws OPCException, InterruptedException 
	{
		Integer value = Integer.valueOf(getOpcApi().readInt(getOpcItemAddress()));
		responseQueue.put(new OPCCommandResult(value));
	}
}
