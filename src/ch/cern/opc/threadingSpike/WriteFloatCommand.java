package ch.cern.opc.threadingSpike;

import java.util.concurrent.SynchronousQueue;

import cern.ess.opclib.OPCException;
import cern.ess.opclib.OpcApi;

public class WriteFloatCommand extends OPCWriteCommandBase implements OPCCommand 
{
	private final String floatType;
	
	public WriteFloatCommand(OpcApi opcInterface, String opcItemAddress,
			SynchronousQueue<OPCCommand> requestQueue,
			SynchronousQueue<OPCCommandResult> responseQueue,
			Float valueToWrite,
			String floatType) 
	{
		super(opcInterface, opcItemAddress, requestQueue, responseQueue, valueToWrite);
		this.floatType = floatType;
	}

	@Override
	public void execute() throws OPCException, InterruptedException 
	{
		getOpcApi().writeFloat(getOpcItemAddress(), floatType, ((Float)getValueToWrite()).floatValue());
		responseQueue.put(new OPCCommandResult(new Object()));
	}

}
