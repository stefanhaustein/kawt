package javax.microedition.midlet;



public abstract class MIDlet {

    protected abstract void startApp () throws MIDletStateChangeException;

    protected abstract void destroyApp (boolean unconditional) throws MIDletStateChangeException; 

    protected abstract void pauseApp ();


    public String getAppProperty (String key) {
	return null;
    }

    public void notifyDestroyed () {
	System.exit (0);
    }
}


