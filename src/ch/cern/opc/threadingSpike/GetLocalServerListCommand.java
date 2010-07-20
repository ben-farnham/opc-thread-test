package ch.cern.opc.threadingSpike;

import java.util.concurrent.SynchronousQueue;

import cern.ess.opclib.OPCException;
import cern.ess.opclib.OpcApi;

public class GetLocalServerListCommand extends OPCCommandBase implements OPCCommand 
{

	public GetLocalServerListCommand(OpcApi opcInterface,
			SynchronousQueue<OPCCommand> requestQueue,
			SynchronousQueue<OPCCommandResult> responseQueue) 
	{
		super(opcInterface, "!not item specific!", requestQueue, responseQueue);
	}

	@Override
	public void execute() throws OPCException, InterruptedException 
	{
		String[] localServerList = getOpcApi().getLocalServerList();
		responseQueue.put(new OPCCommandResult(localServerList));
	}

	@Override
	public String getCommandName() 
	{
		return "GetLocalServerList";
	}

}
