import java.awt.*;

public class TextTest {

    public static void main (String [] argv) {

	Frame f = new Frame ();

	TextField t = new TextField ();
	
	f.add ("Center", t);
	f.pack ();
	f.show ();
 	t.setFont (t.getFont ().deriveFont (Font.BOLD));
    }

}
