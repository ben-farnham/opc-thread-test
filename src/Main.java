import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import static cern.ess.opclib.OPCClientInstance.theOPCClient;

public class Main {
	
	public static final String LOCAL_HOST_IP = ""; //empty is localhost 
	public static final String MATRIKON_OPC_SERVER = "Matrikon.OPC.Simulation";
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws InterruptedException 
	 * @throws ExecutionException 
	 * @throws ExecutionException 
	 * @throws TimeoutException 
	 */
	public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException 
	{
		System.out.println("App started");

		final int nNumThreads = 20;		
		
		Collection<Callable<List<ErrorMessage>>> threads = new ArrayList<Callable<List<ErrorMessage>>>();
		for(int i = 0; i < nNumThreads; i++)
		{
			// old skool
			//threads.add(new OPCThread(LOCAL_HOST_IP, MATRIKON_OPC_SERVER, i));

			// nu skool
			threads.add(new ImprovedOpcThread(LOCAL_HOST_IP, MATRIKON_OPC_SERVER, i));
		}
		
		ExecutorService executor = Executors.newFixedThreadPool(nNumThreads);
		List<Future<List<ErrorMessage>>> results = executor.invokeAll(threads);
		
		for(int i=0; i<nNumThreads; i++)
		{
			Future<List<ErrorMessage>> threadResult = results.get(i);
			List<ErrorMessage> errorMessages = threadResult.get();
			System.out.println("Thread id ["+i+"] is done ["+(threadResult.isDone()?"Y":"N")+"] exception count ["+errorMessages.size()+"]");

			for(Iterator<ErrorMessage> iter = errorMessages.iterator(); iter.hasNext();)
			{
				System.out.println(iter.next());
			}
		}
		// stop the OPCClient command handler thread
		theOPCClient.stop();
		
		// stop the command producer threads in pool
		executor.shutdown();
		
		System.out.println("Exiting.");
	}
}
