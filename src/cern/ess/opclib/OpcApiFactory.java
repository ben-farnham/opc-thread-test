package cern.ess.opclib;

public class OpcApiFactory 
{
	public OpcApi createOpcApi()
	{
		return new OpcApiImpl();
	}
}
