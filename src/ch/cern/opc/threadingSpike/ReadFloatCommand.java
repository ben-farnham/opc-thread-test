package ch.cern.opc.threadingSpike;

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
	public void execute() throws OPCException, InterruptedException 
	{
		Float value = Float.valueOf(getOpcApi().readFloat(getOpcItemAddress()));
		responseQueue.put(new OPCCommandResult(true, value));		
	}

}
