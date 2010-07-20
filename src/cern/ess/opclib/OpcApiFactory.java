package cern.ess.opclib;

import ch.cern.opc.threadingSpike.OPCClient;

public class OpcApiFactory 
{
	public OpcApi createOpcApi()
	{
		return new OPCClient(new OpcApiImpl());
	}
}
