package ch.cern.opc.threadingSpike;

public class OPCCommandResult 
{
	private final boolean success;
	private final Object result;
		
	public OPCCommandResult(boolean success, Object result)
	{
		this.success = success;
		this.result = result;
	}
	
	public boolean isSuccess() 
	{
		return success;
	}
	
	public Object getResult()
	{
		return result;
	}
}
