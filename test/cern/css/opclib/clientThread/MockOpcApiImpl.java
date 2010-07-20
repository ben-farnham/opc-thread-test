/**
 * 
 */
package cern.css.opclib.clientThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cern.ess.opclib.OPCException;
import cern.ess.opclib.OpcApi;

class MockOpcApiImpl implements OpcApi
{
	private String[] localServerList;
	private List<String> requestedItems;
	private final Map<String, Object> opcItemValues;
	private int initCalledCount;
	
	public MockOpcApiImpl()
	{
		requestedItems = new ArrayList<String>();
		this.opcItemValues = new HashMap<String, Object>();
		initCalledCount = 0;
	}
	
	public void setLocalServerList(String[] serverList) 
	{
		this.localServerList = serverList;
	}

	public List<String> getRequestedItems()
	{
		return requestedItems;
	}
	
	public Map<String, Object> getOpcItemValues()
	{
		return opcItemValues;
	}

	@Override
	public String[] getItemNames() throws OPCException 
	{
		Set<String> itemNames = opcItemValues.keySet();
		return itemNames.toArray(new String[0]);
	}

	@Override
	public String[] getLocalServerList() throws OPCException 
	{
		return localServerList;
	}

	@Override
	public boolean readBoolean(String item) throws OPCException 
	{
		return ((Boolean)getRequestedItem(item)).booleanValue();				
	}

	@Override
	public float readFloat(String item) throws OPCException 
	{
		return ((Float)getRequestedItem(item)).floatValue();
	}

	@Override
	public int readInt(String item) throws OPCException 
	{
		return ((Integer)getRequestedItem(item)).intValue();
	}

	@Override
	public String readString(String item) throws OPCException 
	{
		return (String)getRequestedItem(item);
	}

	@Override
	public void writeBoolean(String item, boolean val) throws OPCException 
	{
		setRequestedItem(item, Boolean.valueOf(val));
	}

	@Override
	public void writeFloat(String item, String type, float val) throws OPCException 
	{
		setRequestedItem(item, Float.valueOf(val));
	}

	@Override
	public void writeInt(String item, String type, int val) throws OPCException 
	{
		setRequestedItem(item, Integer.valueOf(val));
	}

	@Override
	public void writeString(String item, String val) throws OPCException 
	{
		setRequestedItem(item, val);
	}	
	
	private Object getRequestedItem(final String opcItemAddress) throws OPCException
	{
		requestedItems.add(opcItemAddress);
		Object value = opcItemValues.get(opcItemAddress);
		
		if(value == null)
		{
			throw new OPCException("failed to find opc item ["+opcItemAddress+"]");
		}
		
		return value;
	}
	
	private void setRequestedItem(final String opcItemAddress, Object value) throws OPCException
	{
		if(!opcItemValues.containsKey(opcItemAddress))
		{
			throw new OPCException("failed to find opc item ["+opcItemAddress+"]");				
		}
		
		opcItemValues.put(opcItemAddress, value);
	}

	@Override
	public void init(String host, String server) throws OPCException 
	{
		initCalledCount++;
	}
	
	public int getNumberOfTimesInitWasCalled()
	{
		return initCalledCount;
	}
}