package ch.cern.opc.threadingSpike;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import cern.ess.opclib.OPCException;
import cern.ess.opclib.OpcApi;


public abstract class OPCCommandBase implements OPCCommand 
{
	private final OpcApi opcInterface;
	private final String opcItemAddress;
	private final SynchronousQueue<OPCCommand> requestQueue;
	protected final SynchronousQueue<OPCCommandResult> responseQueue;
	
	public OPCCommandBase(
			OpcApi opcInterface,
			String opcItemAddress, 
			SynchronousQueue<OPCCommand> requestQueue,
			SynchronousQueue<OPCCommandResult> responseQueue)
	{
		this.opcInterface = opcInterface;
		this.opcItemAddress = opcItemAddress;
		this.requestQueue = requestQueue;
		this.responseQueue = responseQueue;		
	}
	
	@Override
	public Object scheduleAndWaitForResponse() throws OPCException
	{	
		scheduleCommand();			
		return getResponse(responseQueue);
	}
	

	private Object getResponse(SynchronousQueue<OPCCommandResult> responseQueue) throws OPCException {
		try 
		{
			OPCCommandResult commandResult = responseQueue.poll(1, TimeUnit.SECONDS);
			if(commandResult != null)
			{
				if(commandResult.isSuccess())
				{					
					return commandResult.getResult();
				}
				else
				{
					System.err.println("**ERROR**: execution failure - "+this);
					throw commandResult.getException();
				}
			}
			else
			{
				System.err.println("**ERROR**: timeout failure - "+this);
			}
		} 
		catch (InterruptedException e) 
		{
			System.err.println("**ERROR**: interrupt failure while waiting for reponse for - "+this);
		}
		
		return null;
	}

	private void scheduleCommand() 
	{
		try 
		{
			System.out.println("scheduling command...");
			requestQueue.put(this);
			System.out.println("scheduled command");
		} 
		catch (InterruptedException e1) 
		{
			System.err.println("**ERROR**: interrupt failure while submitting request for - "+this);
		}
	}
	
	@Override
	public String toString() 
	{
		return getCommandName()+" for opc item address ["+getOpcItemAddress()+"]";
	}
	
	public String getOpcItemAddress()
	{
		return opcItemAddress;		
	}
	
	public OpcApi getOpcApi()
	{
		return opcInterface;
	}
	
	@Override
	public String getCommandName() 
	{
	    String fullyQualifiedName = this.getClass().getName();
	    return fullyQualifiedName.substring(fullyQualifiedName.lastIndexOf('.')+1);
	}
	
	@Override
	public boolean isInitCommand() 
	{
		return false;
	}
	
	@Override
	public void reportError(OPCException exception) throws InterruptedException 
	{
		responseQueue.put(new OPCCommandResult(exception));
	}
}
