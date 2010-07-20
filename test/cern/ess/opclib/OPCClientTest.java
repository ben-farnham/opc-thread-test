package cern.ess.opclib;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import cern.css.opclib.clientThread.CommandRequester;
import cern.ess.opclib.OPCException;


public class OPCClientTest
{
	private OPCClient testee;
	private MockOpcApiImpl mockOpcApi;
	
	@Before
	public void setup() throws OPCException
	{
		mockOpcApi = new MockOpcApiImpl();
			
		testee = new OPCClient(mockOpcApi);
		testee.start();
		
		testee.init("host", "server");
	}
	
	@After
	public void teardown()
	{
		try {
			testee.stop();
		} 
		catch (InterruptedException e) 
		{
			fail("could not stop OPCClient thread");
		} 
		catch (ExecutionException e) 
		{
			fail("could not stop OPCClient thread");
		} 
		catch (TimeoutException e) 
		{
			fail("could not stop OPCClient thread");
		}
	}	
	@Test
	public void testReadBooleanRequestsCorrectItem() throws InterruptedException, OPCException
	{		
		String itemAddress = "My.Boolean.Item.Address"; 
		mockOpcApi.getOpcItemValues().put(itemAddress, Boolean.TRUE);
		
		testee.readBoolean(itemAddress);
		
		assertEquals(1, mockOpcApi.getRequestedItems().size());
		assertEquals(itemAddress, mockOpcApi.getRequestedItems().get(0));
	}

	@Test
	public void testMultipleReadBooleanCommandsFromSingleThread() throws InterruptedException, OPCException
	{	
		String opcItemAddress = "My.Repeat.Test";
		mockOpcApi.getOpcItemValues().put(opcItemAddress, Boolean.TRUE);

		for(int i=0; i<100; i++)
		{
			testee.readBoolean(opcItemAddress);
		}
		
		List<String> requestedItems = mockOpcApi.getRequestedItems();
		assertEquals(100, requestedItems.size());
		
		for(Iterator<String> iterator = requestedItems.iterator(); iterator.hasNext();)
		{
			assertEquals(opcItemAddress, iterator.next());
		}
		
	}

	@Test
	public void testMultipleReadBooleanCommandsFromMultipleThreads() throws InterruptedException, ExecutionException
	{
		Collection<CommandRequester> tasks = new ArrayList<CommandRequester>();
		for(int i=0; i<500; i++)
		{
			String opcItemAddress = "Boolean.OPC.Item.Number."+i;
			mockOpcApi.getOpcItemValues().put(opcItemAddress, Boolean.TRUE);
			tasks.add(new CommandRequester(testee, opcItemAddress));
		}
		
		// split tasks amongst 5 threads
		ExecutorService threadRunner = Executors.newFixedThreadPool(5);
		List<Future<Boolean>> results = threadRunner.invokeAll(tasks);
		
		for(Iterator<Future<Boolean>> iter = results.iterator(); iter.hasNext(); )
		{
			Future<Boolean> result = iter.next();
			try 
			{
				assertTrue("task result was not true", result.get(5, TimeUnit.SECONDS));
			} 
			catch (TimeoutException e) 
			{
				fail("Thread failed to complete within time limit");
			}			
		}
	}
	
	@Test
	public void testGetItemNames() throws OPCException
	{
		String itemNames[] = {"item.1", "item.2", "item.3", "item.4", "item.5"};
		for(int i=0; i<itemNames.length; i++)
		{
			mockOpcApi.getOpcItemValues().put(itemNames[i], null);
		}
		
		String result[] = testee.getItemNames();		
		assertEquals(itemNames.length, result.length);

		List<String> searchableResult = asList(result);
		assertTrue(searchableResult.containsAll(asList(itemNames)));
	}
	
	@Test
	public void testGetLocalServerList() throws OPCException
	{
		String serverList[] = {"server.1", "server.2", "server.3"};
		mockOpcApi.setLocalServerList(serverList);
		
		String result[] = testee.getLocalServerList();
		assertEquals(serverList.length, result.length);
		
		List<String> searchableResult = asList(result);
		assertTrue(searchableResult.containsAll(asList(serverList)));		
	}
	
	@Test
	public void testReadFloat() throws OPCException
	{
		Map<String, Object> opcItemValues = mockOpcApi.getOpcItemValues();
		opcItemValues.put("opc.item.float1", (float)1.0);
		opcItemValues.put("opc.item.float2", (float)2.0);
		opcItemValues.put("opc.item.float3", (float)3.0);
				
		assertEquals((float)1.0, testee.readFloat("opc.item.float1"), 0.00001);
		assertEquals((float)2.0, testee.readFloat("opc.item.float2"), 0.00001);
		assertEquals((float)3.0, testee.readFloat("opc.item.float3"), 0.00001);
	}
	
	@Test
	public void testReadInt() throws OPCException
	{
		Map<String, Object> opcItemValues = mockOpcApi.getOpcItemValues();
		opcItemValues.put("opc.item.int.1", 1);
		opcItemValues.put("opc.item.int.2", 2);
		opcItemValues.put("opc.item.int.3", 3);
		
		assertEquals(1, testee.readInt("opc.item.int.1"));
		assertEquals(2, testee.readInt("opc.item.int.2"));
		assertEquals(3, testee.readInt("opc.item.int.3"));
	}
	
	@Test
	public void testReadString() throws OPCException
	{
		Map<String, Object> opcItemValues = mockOpcApi.getOpcItemValues();
		opcItemValues.put("opc.item.string.1", "one");
		opcItemValues.put("opc.item.string.2", "two");
		opcItemValues.put("opc.item.string.3", "three");
		
		assertEquals("one", testee.readString("opc.item.string.1"));
		assertEquals("two", testee.readString("opc.item.string.2"));
		assertEquals("three", testee.readString("opc.item.string.3"));		
	}
	
	@Test
	public void testWriteBoolean() throws OPCException
	{
		Map<String, Object> opcItemValues = mockOpcApi.getOpcItemValues();
		opcItemValues.put("opc.item.boolean.1", false);
		opcItemValues.put("opc.item.boolean.2", false);
		opcItemValues.put("opc.item.boolean.3", false);
		
		testee.writeBoolean("opc.item.boolean.1", true);
		testee.writeBoolean("opc.item.boolean.2", true);
		testee.writeBoolean("opc.item.boolean.3", true);
		
		assertTrue((Boolean)opcItemValues.get("opc.item.boolean.1"));
		assertTrue((Boolean)opcItemValues.get("opc.item.boolean.2"));
		assertTrue((Boolean)opcItemValues.get("opc.item.boolean.3"));
	}
	
	@Test
	public void testWriteFloat() throws OPCException
	{
		Map<String, Object> opcItemValues = mockOpcApi.getOpcItemValues();
		opcItemValues.put("opc.item.float.1", (float)1.0);
		opcItemValues.put("opc.item.float.2", (float)2.0);
		opcItemValues.put("opc.item.float.3", (float)3.0);
		
		testee.writeFloat("opc.item.float.1", "some float type", (float)101.0);
		testee.writeFloat("opc.item.float.2", "some float type", (float)102.0);
		testee.writeFloat("opc.item.float.3", "some float type", (float)103.0);
		
		float f1 = ((Float) opcItemValues.get("opc.item.float.1")).floatValue();
		assertEquals((float)101.0, f1, 0.001);

		float f2 = ((Float) opcItemValues.get("opc.item.float.2")).floatValue();
		assertEquals((float)102.0, f2, 0.001);

		float f3 = ((Float) opcItemValues.get("opc.item.float.3")).floatValue();
		assertEquals((float)103.0, f3, 0.001);
	}
	
	@Test
	public void testWriteInt() throws OPCException
	{
		Map<String, Object> opcItemValues = mockOpcApi.getOpcItemValues();
		opcItemValues.put("opc.item.int.1", 1);
		opcItemValues.put("opc.item.int.2", 2);
		opcItemValues.put("opc.item.int.3", 3);
		
		testee.writeInt("opc.item.int.1", "some int type", 101);
		testee.writeInt("opc.item.int.2", "some int type", 102);
		testee.writeInt("opc.item.int.3", "some int type", 103);

		assertEquals(101, ((Integer) opcItemValues.get("opc.item.int.1")).intValue());
		assertEquals(102, ((Integer) opcItemValues.get("opc.item.int.2")).intValue());
		assertEquals(103, ((Integer) opcItemValues.get("opc.item.int.3")).intValue());
	}
	
	@Test
	public void testWriteString() throws OPCException
	{
		Map<String, Object> opcItemValues = mockOpcApi.getOpcItemValues();
		opcItemValues.put("opc.item.string.1", "one");
		opcItemValues.put("opc.item.string.2", "two");
		opcItemValues.put("opc.item.string.3", "three");
		
		testee.writeString("opc.item.string.1", "a");
		testee.writeString("opc.item.string.2", "b");
		testee.writeString("opc.item.string.3", "c");

		assertEquals("a", opcItemValues.get("opc.item.string.1"));
		assertEquals("b", opcItemValues.get("opc.item.string.2"));
		assertEquals("c", opcItemValues.get("opc.item.string.3"));
		}
	
	@Test
	public void testExceptionStoredInResultIfThrown()
	{
		final String invalidItemAddress = "this.item.does.not.exist"; 
		try
		{
			testee.writeBoolean(invalidItemAddress, false);
			fail("expected OPCException to be thrown");
		}
		catch(OPCException e)
		{
			assertEquals("failed to find opc item ["+invalidItemAddress+"]", e.getMessage());
		}
	}
}
