package java.util;

public class Timer {

    class TimerThread extends Thread {

	boolean cancelled;
	
	public void run () {
	    
	    synchronized (thread) {

		while (!cancelled) {
		    long time = System.currentTimeMillis ();
		    long min = 0x0ffffffff;
		    for (int i = tasks.size ()-1; i >= 0; i--) {
			TimerTask tt = (TimerTask) tasks.elementAt (i);
			long delay = tt.scheduledExecutionTime () - time;
			if (delay < min) { 
			    min = delay;
			    if (delay <= 0) break;
			}
		    }
		    

		    try {
			thread.wait (Math.max (1, min));
		    }
		    catch (InterruptedException e) {
			throw new RuntimeException (""+e);
		    }
		    
		    for (int i = tasks.size ()-1; i >= 0 && !cancelled; i--) {
			TimerTask tt = (TimerTask) tasks.elementAt (i);
			if (tt.scheduledExecutionTime () 
			    - System.currentTimeMillis () <= 0) {
			    
			    tt.run ();
			    if (tt.fixed) 
				tt.scheduledExecutionTime += tt.period;
			    else
				tt.scheduledExecutionTime = 
				    System.currentTimeMillis () + tt.period;
			}
		    }
		}
	    }
	}
    }

    Vector tasks = new Vector ();
    TimerThread thread = new TimerThread ();
    
    public Timer () {
	thread.start ();
    }


    public void schedule (TimerTask task, long delay, long period) {
	synchronized (thread) {
	    task.scheduledExecutionTime = System.currentTimeMillis () + delay;
	    task.period = period;
	    tasks.addElement (task);
	    thread.notify ();
	}
    }


    public void cancel () {
	thread.cancelled = true;
    }
}








