import java.awt.*;
import de.kawt.date.*;

public class DateDemo extends Frame {

    public DateDemo () {
    }
    

    public static void main (String [] argv) {

	Frame f = new Frame ();
	f.add ("Center", new CalendarComponent ());

	f.pack ();
	f.show ();
    }

}



