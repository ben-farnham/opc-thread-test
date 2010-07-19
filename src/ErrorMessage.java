

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

public class ErrorMessage 
{
	private final Exception exception;
	private final Date timeRaised;
	
	public ErrorMessage(Exception exception)
	{
		this.exception = exception;
		this.timeRaised = new Date();
	}
	
	@Override
	public String toString()
	{
		String result =  timeRaised.toString() + " " + exception.getMessage() + "\n";
		
		final Writer sw = new StringWriter();
	    final PrintWriter pw = new PrintWriter(sw);
	    exception.printStackTrace(pw);
	    return result + sw.toString();
	}
}
