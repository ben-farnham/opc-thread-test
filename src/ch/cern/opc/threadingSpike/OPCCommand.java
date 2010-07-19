package ch.cern.opc.threadingSpike;

import cern.ess.opclib.OPCException;



public interface OPCCommand 
{
	/**
	 * Called from context of OPC client handler thread
	 */
	public void execute() throws OPCException, InterruptedException;
	
	/**
	 * Called from context of some client to the OPC handler 
	 * thread
	 * 
	 * @return Object that contains the result
	 */
	public Object scheduleAndWaitForResponse();
	
	public String getCommandName();
	
	public boolean isInitCommand();
}
