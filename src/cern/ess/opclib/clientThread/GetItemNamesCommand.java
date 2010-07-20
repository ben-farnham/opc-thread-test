package cern.ess.opclib.clientThread;

import java.util.concurrent.SynchronousQueue;

import cern.ess.opclib.OPCException;
import cern.ess.opclib.OpcApi;

public class GetItemNamesCommand extends OPCCommandBase implements OPCCommand {


	public GetItemNamesCommand(OpcApi opcInterface,
			SynchronousQueue<OPCCommand> reqQueue,
			SynchronousQueue<OPCCommandResult> rspQueue) 
	{
		super(opcInterface, "!not item specific!", reqQueue, rspQueue);
	}

	@Override
	public Object execute() throws OPCException, InterruptedException
	{
		return getOpcApi().getItemNames();
	}
}
