package cern.ess.opclib.clientThread;

import java.util.concurrent.SynchronousQueue;

import cern.ess.opclib.OpcApi;

public class OpcCommandFactory 
{
	private final OpcApi opcInterface;
	private final SynchronousQueue<OPCCommand> reqQueue;
	
	public OpcCommandFactory(final OpcApi opcInterface, final SynchronousQueue<OPCCommand> reqQueue)
	{
		this.opcInterface = opcInterface;
		this.reqQueue = reqQueue;
	}
	
	public OPCCommand createReadBooleanCommand(final String opcItemAddress, final SynchronousQueue<OPCCommandResult> rspQueue)
	{
		return new ReadBooleanCommand(opcInterface, opcItemAddress, reqQueue, rspQueue);		
	}
	
	public OPCCommand createGetItemNamesCommand(final SynchronousQueue<OPCCommandResult> rspQueue)
	{
		return new GetItemNamesCommand(opcInterface, reqQueue, rspQueue);
	}

	public OPCCommand createGetLocalServerList(SynchronousQueue<OPCCommandResult> rspQueue) 
	{
		return new GetLocalServerListCommand(opcInterface, reqQueue, rspQueue);
	}

	public OPCCommand createReadFloatCommand(String opcItemAddress, SynchronousQueue<OPCCommandResult> rspQueue) 
	{
		return new ReadFloatCommand(opcInterface, opcItemAddress, reqQueue, rspQueue);
	}

	public OPCCommand createReadIntCommand(String opcItemAddress, SynchronousQueue<OPCCommandResult> rspQueue) 
	{
		return new ReadIntCommand(opcInterface, opcItemAddress, reqQueue, rspQueue);
	}

	public OPCCommand createReadStringCommand(String opcItemAddress, SynchronousQueue<OPCCommandResult> rspQueue) 
	{
		return new ReadStringCommand(opcInterface, opcItemAddress, reqQueue, rspQueue);
	}

	public OPCCommand createWriteBooleanCommand(String opcItemAddress, SynchronousQueue<OPCCommandResult> rspQueue, boolean value) 
	{
		return new WriteBooleanCommand(opcInterface, opcItemAddress, reqQueue, rspQueue, value);
	}

	public OPCCommand createWriteFloatCommand(String opcItemAddress, SynchronousQueue<OPCCommandResult> rspQueue, float value, String floatType) 
	{
		return new WriteFloatCommand(opcInterface, opcItemAddress, reqQueue, rspQueue, value, floatType);
	}

	public OPCCommand createWriteIntCommand(String opcItemAddress, SynchronousQueue<OPCCommandResult> rspQueue, int value, String intType) 
	{
		return new WriteIntCommand(opcInterface, opcItemAddress, reqQueue, rspQueue, value, intType);
	}

	public OPCCommand createWriteStringCommand(String opcItemAddress, SynchronousQueue<OPCCommandResult> rspQueue, String value) 
	{
		return new WriteStringCommand(opcInterface, opcItemAddress, reqQueue, rspQueue, value);
	}

	public OPCCommand createInitCommand(String host, String server, SynchronousQueue<OPCCommandResult> rspQueue) 
	{
		return new InitCommand(opcInterface, reqQueue, rspQueue, host, server);
	}
}
