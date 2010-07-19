

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import cern.ess.opclib.OPC;
import cern.ess.opclib.OPCException;

public class OPCThread implements Callable<List<ErrorMessage>> 
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

	public OPCThread(String host, String opcServer, final int id)
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
			initialise();
			
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

	private void initialise() throws OPCException {
		OPC.GetInstance().opcInit(host, opcServer);
		
		String[] names = OPC.GetInstance().opcGetItemNames();
		for(int i = 0; i<names.length; i++)
		{
			System.out.println("index ["+i+"] name ["+names[i]+"]");
		}
		
		System.out.println("**Yawn, woken up after sleeping ["+randomSleep(100)+"] ms");
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
		boolean bValue = OPC.GetInstance().opcReadBoolean(opcItemAddress);
		System.out.println(opcItemAddress + " (pre-write) has value["+bValue+"]");
		
		OPC.GetInstance().opcWriteBoolean(opcItemAddress, !bValue);
		System.out.println(opcItemAddress + " (post-write) has value["+OPC.GetInstance().opcReadBoolean(opcItemAddress)+"]");		
	}
	
	private void readWriteInt(String opcItemAddress, String intType) throws OPCException
	{
		int nValue = OPC.GetInstance().opcReadInt(opcItemAddress);
		System.out.println(opcItemAddress + " (pre-write) has value["+nValue+"]");
		
		OPC.GetInstance().opcWriteInt(opcItemAddress, intType, ++nValue);
		System.out.println(opcItemAddress + " (post-write) has value["+OPC.GetInstance().opcReadInt(opcItemAddress)+"]");	
	}
	
	private void readWriteFloat(String opcItemAddress, String floatType) throws OPCException
	{
		float fValue = OPC.GetInstance().opcReadFloat(opcItemAddress);
		System.out.println(opcItemAddress + " (pre-write) has value["+fValue+"]");
		
		OPC.GetInstance().opcWriteFloat(opcItemAddress, floatType, fValue += 0.5);
		System.out.println(opcItemAddress + " (post-write) has value["+OPC.GetInstance().opcReadFloat(opcItemAddress)+"]");		
	}
	
	private void readWriteString(String opcItemAddress) throws OPCException
	{
		String sValue = OPC.GetInstance().opcReadString(opcItemAddress);
		System.out.println(opcItemAddress + " (pre-write) has value["+sValue+"]");
		
		OPC.GetInstance().opcWriteString(opcItemAddress, sValue.equals("woo")?"waa":"woo");
		System.out.println(opcItemAddress + " (post-write) has value["+OPC.GetInstance().opcReadString(opcItemAddress)+"]");		
	}
}
