import java.awt.*;
import java.awt.event.*;
import java.io.*;

import de.kawt.*;
import de.kawt.shell.*;


class FtpDialog extends Dialog implements ActionListener {
	
    TextField server = new TextField ("ftp.leo.org");
    TextField user = new TextField ("anonymous");
    TextField password = new TextField ("abc@yahoo.com");
    
    Button ok = new Button ("ok");
    Button cancel = new Button ("cancel");
    
    KCmd kcmd;

    FtpDialog (KCmd kcmd) {
	super (kcmd.frame, "open FTP", true);

	this.kcmd = kcmd;
	Panel buttons = new Panel ();
	Panel left = new Panel (new GridLayout (0, 1));
	Panel right = new Panel (new GridLayout (0, 1));
	
	left.add (new Label ("Server:", Label.RIGHT));
	left.add (new Label ("User:", Label.RIGHT));
	left.add (new Label ("Password:", Label.RIGHT));

	password.setEchoChar ('*');
	
	right.add (server);
	right.add (user);
	right.add (password);
	
	buttons.add (ok);
	ok.addActionListener (this);
	buttons.add (cancel);
	cancel.addActionListener (this);
		
	add ("West", left);
	add ("Center", right);
	
	add ("South", buttons);

	pack ();

	//System.out.println ("fc x="+getX ()+" y="+getY ()+" w="+ getWidth ()+" h="+getHeight ());
    }
    
    
    public void actionPerformed (ActionEvent ae) {
	setVisible (false);
	
	if (ae.getSource () == ok) {
	    
	    try {
		FtpShell ftp = new FtpShell ();
		ftp.open (server.getText (), user.getText (), password.getText ());
		kcmd.active.setShell (ftp);
	    }
	    catch (IOException e) {
		OptionDialog.showMessageDialog (kcmd.frame, "Connection failed!");
	    }
	}		
    }
}

