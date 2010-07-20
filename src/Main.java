import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Main {
	
	public static final String LOCAL_HOST_IP = ""; /*empty is localhost*/
	public static final String MATRIKON_OPC_SERVER = "Matrikon.OPC.Simulation";
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws ExecutionException 
	 */
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		System.out.println("App started");
		
		final int nNumThreads = 5;		
		
		Collection<Callable<List<ErrorMessage>>> threads = new ArrayList<Callable<List<ErrorMessage>>>();
		for(int i = 0; i < nNumThreads; i++)
		{
			/* Old skool - not working
			threads.add(new OPCThread(LOCAL_HOST_IP, MATRIKON_OPC_SERVER, i));
			*/
			
			threads.add(new ImprovedOpcThread(LOCAL_HOST_IP, MATRIKON_OPC_SERVER, i));
		}
		
		ExecutorService executor = Executors.newFixedThreadPool(1);
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
		
		System.out.println("Exiting.");
	}	
}
