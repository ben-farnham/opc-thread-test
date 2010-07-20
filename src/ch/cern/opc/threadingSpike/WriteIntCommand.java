package ch.cern.opc.threadingSpike;

import java.util.concurrent.SynchronousQueue;

import cern.ess.opclib.OPCException;
import cern.ess.opclib.OpcApi;

public class WriteIntCommand extends OPCWriteCommandBase implements OPCCommand 
{
	private final String intType;
	
	public WriteIntCommand(OpcApi opcInterface, String opcItemAddress,
			SynchronousQueue<OPCCommand> requestQueue,
			SynchronousQueue<OPCCommandResult> responseQueue,
			Object valueToWrite,
			final String intType) 
	{
		super(opcInterface, opcItemAddress, requestQueue, responseQueue, valueToWrite);
		this.intType = intType;
	}

	@Override
	public void execute() throws OPCException, InterruptedException 
	{
		getOpcApi().writeInt(getOpcItemAddress(), intType, ((Integer)getValueToWrite()).intValue());
		responseQueue.put(new OPCCommandResult(new Object()));
	}

}
