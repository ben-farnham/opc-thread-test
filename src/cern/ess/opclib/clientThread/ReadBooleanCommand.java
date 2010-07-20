package cern.ess.opclib.clientThread;

import java.util.concurrent.SynchronousQueue;

import cern.ess.opclib.OPCException;
import cern.ess.opclib.OpcApi;

public class ReadBooleanCommand extends OPCCommandBase implements OPCCommand 
{
	public ReadBooleanCommand(OpcApi opcInterface, String opcItemAddress,
			SynchronousQueue<OPCCommand> reqQueue,
			SynchronousQueue<OPCCommandResult> rspQueue) {
		super(opcInterface, opcItemAddress, reqQueue, rspQueue);
	}

	@Override
	public void execute() throws OPCException, InterruptedException
	{
		Boolean value = Boolean.valueOf(getOpcApi().readBoolean(getOpcItemAddress()));
		responseQueue.put(new OPCCommandResult(value));
	}
}
