import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import cern.ess.opclib.OPCException;
import cern.ess.opclib.OpcApiFactory;
import cern.ess.opclib.clientThread.OPCClient;

public class ImprovedOpcThread implements Callable<List<ErrorMessage>> 
{

	public static final String MY_BOOL = "testGroup.myBool";
	public static final String MY_SHORT_INT = "testGroup.myShortInt";
	public static final String MY_LONG_INT = "testGroup.myLongInt";
	public static final String MY_SMALL_FLOAT = "testGroup.mySmallFloat";
	public static final String MY_BIG_FLOAT = "testGroup.myBigFloat";
	public static final String MY_STRING = "testGroup.myString";
	
	private final String host;
	private final String opcServer;
	private final int id;
	
	public static OPCClient theClient;
	static
	{
		theClient = new OPCClient(new OpcApiFactory().createOpcApi());
		theClient.start();
	}

	public ImprovedOpcThread(String host, String opcServer, final int id)
	{
		this.host = host;
		this.opcServer = opcServer;
		this.id = id;
	}
	
	@Override
	public List<ErrorMessage> call() {
		System.out.println("Thread [id: "+id+"] started");
		List<ErrorMessage> result = new ArrayList<ErrorMessage>();
		try
		{
			theClient.init(host, opcServer);
			
			for (int i=0; i<10; i++)
			{
				readWriteValues();
			}
		}
		catch(OPCException e)
		{
			result.add(new ErrorMessage(e));
		}
		
		System.out.println("Thread [id: "+id+"] ended, error count ["+result.size()+"]");
		return result;
	}

	private void readWriteValues() throws OPCException
	{		
		readWriteBoolean(MY_BOOL);
		randomSleep(10);
		readWriteInt(MY_SHORT_INT, "I2");
		randomSleep(10);
		readWriteInt(MY_LONG_INT, "I4");
		randomSleep(10);
		readWriteFloat(MY_SMALL_FLOAT, "R4");
		randomSleep(10);
		readWriteFloat(MY_BIG_FLOAT, "R8");
		randomSleep(10);
		readWriteString(MY_STRING);
		randomSleep(10);
	}
	
	private int randomSleep(final int maxSleep)
	{
		int nSleepTime = (int)(Math.random()*maxSleep);
		try 
		{
			Thread.sleep(nSleepTime);
			return nSleepTime;
		} 
		catch (InterruptedException e) 
		{
			System.out.println("Sleep interrupted");
		}
		
		return -1;
	}
	
	private void readWriteBoolean(String opcItemAddress) throws OPCException
	{
		boolean bValue = theClient.readBoolean(opcItemAddress);
		System.out.println(opcItemAddress + " (pre-write) has value["+bValue+"]");
		
		theClient.writeBoolean(opcItemAddress, !bValue);
		System.out.println(opcItemAddress + " (post-write) has value["+theClient.readBoolean(opcItemAddress)+"]");		
	}
	
	private void readWriteInt(String opcItemAddress, String intType) throws OPCException
	{
		int nValue = theClient.readInt(opcItemAddress);
		System.out.println(opcItemAddress + " (pre-write) has value["+nValue+"]");
		
		theClient.writeInt(opcItemAddress, intType, ++nValue);
		System.out.println(opcItemAddress + " (post-write) has value["+theClient.readInt(opcItemAddress)+"]");	
	}
	
	private void readWriteFloat(String opcItemAddress, String floatType) throws OPCException
	{
		float fValue = theClient.readFloat(opcItemAddress);
		System.out.println(opcItemAddress + " (pre-write) has value["+fValue+"]");
		
		theClient.writeFloat(opcItemAddress, floatType, fValue += 0.5);
		System.out.println(opcItemAddress + " (post-write) has value["+theClient.readFloat(opcItemAddress)+"]");		
	}
	
	private void readWriteString(String opcItemAddress) throws OPCException
	{
		String sValue = theClient.readString(opcItemAddress);
		System.out.println(opcItemAddress + " (pre-write) has value["+sValue+"]");
		
		theClient.writeString(opcItemAddress, sValue.equals("woo")?"waa":"woo");
		System.out.println(opcItemAddress + " (post-write) has value["+theClient.readString(opcItemAddress)+"]");		
	}

}
