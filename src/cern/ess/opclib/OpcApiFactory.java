package cern.ess.opclib;

class OpcApiFactory 
{
	public OpcApi createOpcApi()
	{
		return new OpcApiImpl();
	}
}
