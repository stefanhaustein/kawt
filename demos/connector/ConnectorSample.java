
import java.io.*;
import javax.microedition.io.*;

import java.awt.*;
import java.awt.event.*;

import de.kawt.*;

public class ConnectorSample extends KAWTlet implements ActionListener {

    TextField urlField = new TextField ("socket://kiew.cs.uni-dortmund.de:23");
    TextField sendField = new TextField ();
    List incoming = new List ();
    Frame frame = new Frame ();

    StreamConnection conn;
    InputStream in;
    OutputStream out;

    class Appender implements Runnable {
	String data;
	Appender (String data) {
	    this.data = data;
	}

	public void run () {
	    int i0 = data.indexOf ('\r');
	    if (i0 == -1) i0 = data.length ();
	    incoming.replaceItem 
		(incoming.getItem (incoming.getItemCount () - 1) 
		 + data.substring (0, i0), incoming.getItemCount ()-1);
	    i0++;
	    while (i0 < data.length ()) {
		int i = data.indexOf ('\r', i0);
		if (i == -1) i = data.length ();
		incoming.add (data.substring (i0, i));
		i0 = i+1;
	    }
	}
    }	    
	    
    class ReadThread extends Thread {

	public void run () {
	    
	    StringBuffer buf = new StringBuffer ();
	    
	    try {
		while (true) {

		    do {//System.out.println ("waiting");
			int i = in.read ();

			//System.out.println ("read: "+i+ "("+(char) i+")");
			if (i == -1) return;
			if (i == 255) {
			    int cmd = in.read ();
			    System.out.println ("cmd: "+cmd);
			    if (cmd == 255)
				buf.append ((char) 255);
			    else if (cmd == 0xfd || cmd == 0x0fb) {
				int opt = in.read ();
				
				out.write (255);
				out.write (cmd == 0xfd ? 252 : 254);
				out.write (opt);
				out.flush ();
				System.out.println ("done");
			    }
			}
			else if (i == '\r' || i >= ' ')   
			    buf.append ((char) i);
		    }
		    while (in.available () > 0 && buf.length () < 1024);

		    Toolkit.getDefaultToolkit ().getSystemEventQueue ()
			.invokeAndWait (new Appender 
			    (buf.toString ()));
		    buf.setLength (0);
		}
	    }
	    catch (Exception e) {
		incoming.add (e.toString ());
		try {
		    in.close ();
		    out.close ();
		    conn.close ();
		} 
		catch (Exception e2) {}
		conn = null;
	    }
	}
    }


    public ConnectorSample () {
	frame = new Frame ("ConnectorSample");
	
	frame.addWindowListener (new de.kawt.Closer (this));
	
	Button connectButton = new Button ("connect");
	connectButton.addActionListener (this);
	
	Panel topPanel = new Panel (new BorderLayout ());
	//topPanel.add ("West", protocolChoice);
	topPanel.add ("Center", urlField);
	topPanel.add ("East", connectButton);
	
	Button sendButton = new Button ("send");
	sendButton.addActionListener (this);
	
	Panel bottomPanel = new Panel (new BorderLayout ());
	bottomPanel.add ("Center", sendField);
	bottomPanel.add ("East", sendButton);

	frame.add ("North", topPanel);
	frame.add ("Center", incoming);
	frame.add ("South", bottomPanel);
	
	//new ReadThread ().start ();
	
	frame.pack ();
    }


    public void startApp () {
	frame.show ();
    }


    public void actionPerformed (ActionEvent ae) {

	String cmd = ae.getActionCommand ();

	try {
	    if (cmd.equals ("send")) {
		out.write (sendField.getText ().getBytes ());
		out.write (13);
		out.write (10);
		out.flush ();
		sendField.setText ("");
	    }
	    else if (cmd.equals ("connect")) {
		if (conn != null) { 
		    in.close ();
		    out.close ();
		    conn.close ();
		    conn = null;
		}
		
	        conn = (StreamConnection) Connector.open 
		    (urlField.getText (), Connector.READ_WRITE, true);

		out = conn.openOutputStream ();
		in = conn.openInputStream ();

		incoming.add ("opened: "+urlField.getText ());

		new ReadThread ().start ();
	    }
	}
	catch (Exception e) {
	    incoming.add (e.toString ());
	}
    }

    public void destroyApp (boolean unconditional) {
	frame.setVisible (false);
    }


    public static void main (String [] args) {
	new ConnectorSample ().startApp ();
    }
}
