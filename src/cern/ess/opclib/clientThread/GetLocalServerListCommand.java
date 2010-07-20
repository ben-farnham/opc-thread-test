package cern.ess.opclib.clientThread;

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
	public Object execute() throws OPCException, InterruptedException 
	{
		return getOpcApi().getLocalServerList();
	}
}
