package java.util;


public class TimerTask implements Runnable {
    
    long scheduledExecutionTime;
    long period;
    boolean fixed;

    public boolean cancel () {

	return false;
    }

    
    public void run () {
    }
    

    public long scheduledExecutionTime () {
	return scheduledExecutionTime;
    }
}
