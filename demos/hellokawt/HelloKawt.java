import java.awt.*;
import de.kawt.*;


class HelloKawt extends KAWTlet {

    Frame frame = new Frame ("HelloKawt");

    public HelloKawt () {
	frame.addWindowListener (new Closer (this));
	frame.add ("Center", new Label ("HelloKawt"));
	frame.pack ();
    }

    public void startApp () {
	frame.show (); 
    }
    
    public void destroyApp (boolean unconditional) {
    }

    public static void main (String [] argv) {
	new HelloKawt ().startApp ();
    }

} 
