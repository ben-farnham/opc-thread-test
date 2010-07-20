package ch.cern.opc.threadingSpike;

import java.util.concurrent.SynchronousQueue;

import cern.ess.opclib.OPCException;
import cern.ess.opclib.OpcApi;

public class InitCommand extends OPCCommandBase implements OPCCommand 
{

	private final String host;
	private final String server;
	
	public InitCommand
	(
			OpcApi opcInterface, 
			SynchronousQueue<OPCCommand> requestQueue,
			SynchronousQueue<OPCCommandResult> responseQueue,
			String host,
			String server) 
	{
		super(opcInterface, "!not item specific!", requestQueue, responseQueue);
		this.host = host;
		this.server = server;
	}

	@Override
	public void execute() throws OPCException, InterruptedException 
	{
		getOpcApi().init(host, server);
		responseQueue.put(new OPCCommandResult(new Object()));
	}
	
	@Override
	public boolean isInitCommand() 
	{
		return true;
	}
}
