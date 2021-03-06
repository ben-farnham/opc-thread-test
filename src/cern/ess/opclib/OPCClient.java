package cern.ess.opclib;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import cern.ess.opclib.clientThread.OPCCommand;
import cern.ess.opclib.clientThread.OPCCommandResult;
import cern.ess.opclib.clientThread.OpcCommandFactory;

/**
 * Multithreaded class - starts an internal thread to handle processing
 * commands and also exposes (to external class client threads) methods
 * for scheduling commands for the central processor thread.
 *  
 * @author bfarnham
 *
 */
class OPCClient implements OpcApi
{
	private int numberOfCommandsProcessed;
	private final SynchronousQueue<OPCCommand> requestQueue;
	private final ExecutorService threadRunner;
	private Future<Integer> threadResult;
	private final OpcCommandFactory commandFactory;
	
	private boolean initialised = false;
	
	private final static int MAX_THREAD_SHUTDOWN_WAIT_MS = 250;
	
	/**
	 * Each thread calling the OPC client has its own response queue (embedded
	 * in a thread local)
	 */
	private static ThreadLocal<SynchronousQueue<OPCCommandResult>> threadResponseQueue = new ThreadLocal<SynchronousQueue<OPCCommandResult>>()
	{
		@Override
		protected SynchronousQueue<OPCCommandResult> initialValue() 
		{
			System.out.println("Creating a response queue for thread ["+Thread.currentThread().getId()+"]");
			return new SynchronousQueue<OPCCommandResult>();
		};		
	};
	
	public OPCClient(OpcApi opcInterface) 
	{
		System.out.println("Creating OPCClient instance");
		this.requestQueue = new SynchronousQueue<OPCCommand>();
		this.commandFactory = new OpcCommandFactory(opcInterface, requestQueue);
		this.threadRunner = Executors.newFixedThreadPool(1);
		this.numberOfCommandsProcessed = 0;
	}
	
	public void start()
	{
		threadResult = threadRunner.submit(new CommandExecutor());
	}
	
	public int stop() throws InterruptedException, ExecutionException, TimeoutException
	{	
		System.out.println("Stopping OPC Client thread...");
		threadRunner.shutdownNow();
		requestQueue.clear();
	
		int result = -1;
		Integer objectResult = threadResult.get(MAX_THREAD_SHUTDOWN_WAIT_MS, TimeUnit.MILLISECONDS);
		if(objectResult != null)
		{
			result = objectResult.intValue();
		}
		
		System.out.println("command queue consumer task stopped - processed ["+result+"] commands");
		
		return result;
	}
	
	/**
	 * Called by external threads to schedule a command for the OPC client.
	 * The OPC client runs the command when it is ready.
	 * 
	 * @param command
	 * @throws InterruptedException
	 */
	public boolean submitCommand(OPCCommand command) 
	{
		try 
		{
			requestQueue.put(command);
			return true;
		} 
		catch (InterruptedException e) 
		{
			return false;
		}
	}
	
	private class CommandExecutor implements Callable<Integer>
	{
		/**
		 * task loops taking commands from the command queue and processing them.
		 * Loop is killed by a thread interrupt.
		 */
		@Override
		public Integer call() {
			System.out.println("OPC Client task for executing commands started");
			

			try 
			{
				processCommands();
			} 
			catch (InterruptedException e) 
			{
				System.out.println("Task interrupted - exiting");
			} 

			System.out.println("OPC Client thread stopped");
			
			return Integer.valueOf(numberOfCommandsProcessed);
		}

		private int processCommands() throws InterruptedException 
		{
			while(true)
			{
				System.out.println("waiting for command...");
				OPCCommand command = requestQueue.take();
				System.out.println("command received, "+command+", processing");
				
				if(!initialised && !command.isInitCommand())
				{
					command.reportError(new OPCException("init must be called before any other operation"));
				}
				else
				{
					processCommand(command);					
				}
				
			}
		}

		private void processCommand(OPCCommand command) throws InterruptedException 
		{
			try 
			{
				Object executionResult = command.execute();
				command.reportSuccess(executionResult);
				numberOfCommandsProcessed++;
			} 
			catch (OPCException e) 
			{
				command.reportError(e);
			}
		}		
	}
	
	public void init(String host, String server) throws OPCException
	{
		if(!initialised)
		{
			OPCCommand command = commandFactory.createInitCommand(host, server, threadResponseQueue.get());
			command.scheduleAndWaitForResponse();
			initialised = true;
		}
		else
		{
			System.out.println("OPCClient thread already initialised - ignoring initialise request");
		}
	}
	
	public String[] getItemNames() throws OPCException
	{
		OPCCommand command = commandFactory.createGetItemNamesCommand(threadResponseQueue.get());	
		Object result = command.scheduleAndWaitForResponse();		
		return (String[]) result;
	}
	
	public boolean readBoolean(final String opcItemAddress) throws OPCException
	{
		OPCCommand command = 
			commandFactory.createReadBooleanCommand(
					opcItemAddress, 
					threadResponseQueue.get()); 

		Object result = command.scheduleAndWaitForResponse();		
		return ((Boolean)result).booleanValue();			
	}

	public String[] getLocalServerList() throws OPCException
	{
		OPCCommand command = commandFactory.createGetLocalServerList(threadResponseQueue.get());
		Object result = command.scheduleAndWaitForResponse();
		return (String[]) result;
	}

	public float readFloat(String opcItemAddress) throws OPCException
	{
		OPCCommand command = commandFactory.createReadFloatCommand(opcItemAddress, threadResponseQueue.get());
		Object result = command.scheduleAndWaitForResponse();
		return ((Float)result).floatValue();
	}

	public int readInt(String opcItemAddress) throws OPCException
	{
		OPCCommand command = commandFactory.createReadIntCommand(opcItemAddress, threadResponseQueue.get());
		Object result = command.scheduleAndWaitForResponse();
		return ((Integer)result).intValue();
	}

	public String readString(String opcItemAddress) throws OPCException
	{
		OPCCommand command = commandFactory.createReadStringCommand(opcItemAddress, threadResponseQueue.get());
		Object result = command.scheduleAndWaitForResponse();
		return (String)result;
	}

	public void writeBoolean(String opcItemAddress, boolean value) throws OPCException 
	{
		OPCCommand command = commandFactory.createWriteBooleanCommand(opcItemAddress, threadResponseQueue.get(), value);
		command.scheduleAndWaitForResponse();
	}

	public void writeFloat(String opcItemAddress, String floatType, float value) throws OPCException 
	{
		OPCCommand command = commandFactory.createWriteFloatCommand(opcItemAddress, threadResponseQueue.get(), value, floatType);
		command.scheduleAndWaitForResponse();
	}

	public void writeInt(String opcItemAddress, String intType, int value) throws OPCException
	{
		OPCCommand command = commandFactory.createWriteIntCommand(opcItemAddress, threadResponseQueue.get(), value, intType);
		command.scheduleAndWaitForResponse();
	}

	public void writeString(String opcItemAddress, String value) throws OPCException
	{
		OPCCommand command = commandFactory.createWriteStringCommand(opcItemAddress, threadResponseQueue.get(), value);
		command.scheduleAndWaitForResponse();
	}	
}
