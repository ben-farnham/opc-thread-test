package cern.ess.opclib.clientThread;

import java.util.concurrent.SynchronousQueue;

import cern.ess.opclib.OPCException;
import cern.ess.opclib.OpcApi;

public class ReadFloatCommand extends OPCCommandBase implements OPCCommand 
{

	public ReadFloatCommand(OpcApi opcInterface, String opcItemAddress,
			SynchronousQueue<OPCCommand> requestQueue,
			SynchronousQueue<OPCCommandResult> responseQueue) 
	{
		super(opcInterface, opcItemAddress, requestQueue, responseQueue);
	}

	@Override
	public Object execute() throws OPCException, InterruptedException 
	{
		return Float.valueOf(getOpcApi().readFloat(getOpcItemAddress()));
	}

}
