package java.awt.event;

import java.awt.AWTEvent;

public class InvocationEvent extends AWTEvent implements ActiveEvent {

    public static final int INVOCATION_DEFAULT = 1200;

    Runnable runnable;
    Object notifier;
    boolean catchExceptions;
    Exception exception;

    protected InvocationEvent (Object source, int id,
			       Runnable runnable,
			       Object notifier,
			       boolean catchExceptions) {
	
	super (source, id);

	this.runnable = runnable;
	this.notifier = notifier;
	this.catchExceptions = catchExceptions;
    }
    

    public InvocationEvent (Object source,
			    Runnable runnable,
			    Object notifier,
			    boolean catchExceptions) {

	this (source, INVOCATION_DEFAULT, runnable, notifier,
	      catchExceptions);
    }


    public void dispatch () {
	
	if (catchExceptions) {
	    try {
		runnable.run ();
	    }
	    catch (Exception e) {
		exception = e;
	    }
	}
	else runnable.run ();


	if (notifier != null) {
	    synchronized (notifier) {
		notifier.notifyAll ();
	    }
	}
    }


    public Exception getException () {
	return exception;
    }
}
