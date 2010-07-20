package cern.ess.opclib;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface OpcApi 
{
	public void init(String host, String server) throws OPCException;
	public String[] getItemNames() throws OPCException;
	public String[] getLocalServerList() throws OPCException;
	public boolean readBoolean(String item) throws OPCException;
	public String readString(String item) throws OPCException;
	public int readInt(String item) throws OPCException;
	public float readFloat(String item) throws OPCException;
	public void writeBoolean(String item, boolean val) throws OPCException;
	public void writeString(String item, String val) throws OPCException;
	public void writeInt(String item, String type, int val) throws OPCException;
	public void writeFloat(String item, String type, float val) throws OPCException;
	
	public int stop() throws InterruptedException, ExecutionException, TimeoutException;
}
