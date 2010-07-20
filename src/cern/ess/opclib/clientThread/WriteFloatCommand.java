package cern.ess.opclib.clientThread;

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
	public Object execute() throws OPCException, InterruptedException 
	{
		getOpcApi().writeFloat(getOpcItemAddress(), floatType, ((Float)getValueToWrite()).floatValue());
		return new Object();
	}

}
