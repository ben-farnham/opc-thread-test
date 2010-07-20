package cern.ess.opclib;

/**
 * 
 * Class is just a singleton wrapper (eager instantiation model) for 
 * the one and only OPCClient thread instance.
 * 
 * @author bfarnham
 */
public abstract class OPCClientInstance 
{
	
	public static OpcApi theOPCClient;
	
	static
	{
		theOPCClient = new OPCClient(new OpcApiFactory().createOpcApi());
		((OPCClient)theOPCClient).start();
	}
	
	private OPCClientInstance()
	{
		throw new UnsupportedOperationException("Not constructible - class is a holder for the single OPCClient instance");
	}
}
