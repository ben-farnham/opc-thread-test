package cern.ess.opclib.clientThread;

import cern.ess.opclib.OPCException;

public class OPCCommandResult 
{
	private final boolean success;
	private final Object result;
	private final OPCException exception;
		
	public OPCCommandResult(Object result)
	{
		this.success = true;
		this.result = result;
		this.exception = null;
	}
	
	public OPCCommandResult(OPCException opcException) 
	{
		this.success = false;
		this.result = null;
		this.exception = opcException;
	}

	public boolean isSuccess() 
	{
		return success;
	}
	
	public Object getResult()
	{
		return result;
	}
	
	public OPCException getException()
	{
		return exception;
	}
}
