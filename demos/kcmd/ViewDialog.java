import java.io.*;
import java.awt.*;
import java.awt.event.*;

import de.kawt.*;
import de.kawt.shell.*;

public class ViewDialog extends Dialog implements ActionListener {

    List list = new List ();
    Button close = new Button ("close");

    public ViewDialog (KCmd kcmd, AbstractShell shell, 
		       String file) throws IOException {

	super (kcmd.frame, file);

	InputStream is = shell.getInputStream (file);
	int length = (int) shell.getTransferSize ();

	kcmd.progress.setMax (length);

	if (file.endsWith (".gif")) {
	    
	    byte [] buf = new byte [length];
	    
	    int sum = 0;

	    //progress.setVisible (true);

	    while (sum < length) {

		sum += is.read (buf, sum, length-sum);
		kcmd.progress.setValue (sum/2);
	    }

	    Image img = Toolkit.getDefaultToolkit ().createImage (buf);
	    
	    kcmd.progress.setValue (sum);

	    add ("Center", new ImageCanvas (img));
	}
	else {
	    
	    add ("Center", list);

	    StringBuffer buf = new StringBuffer ();
	    int pos = 0;

	    while (true) {
		int i = is.read ();
		pos++;
		if (i == -1) break;
		else if (i == '\n') {
		    kcmd.progress.setValue (pos);
		    list.add (buf.toString ());
		    buf.setLength (0);
		}
		else if (i >= 32) buf.append ((char) i);
	    }
	    list.add (buf.toString ());
	    
	}
	is.close ();
	shell.closeStream ();
	addWindowListener (new Closer (this));

	Panel buttonPanel = new Panel ();
	buttonPanel.add (close);
	add ("South", buttonPanel);

	close.addActionListener (this);

	pack ();
	show ();

	kcmd.progress.setValue (0);
    }


    public void actionPerformed (ActionEvent e) {
	dispose ();
    }
}

