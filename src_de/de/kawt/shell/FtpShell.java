// FtpShell.java
//
// 2000-09-21 SH Changed indexOf ('(') to lastIndexOf ('(')
// 2000-09-08 MK Added new Licensetext
//
//#include ..\..\license.txt
//
// kAWT version 0.95
//
// Copyright (C) 1999-2000 by Michael Kroll & Stefan Haustein GbR, Essen
//
// Contact: kawt@kawt.de
// General Information about kAWT is available at: http://www.kawt.de
//
// Using kAWT for private and educational and in GPLed open source
// projects is free. For other purposes, a commercial license must be
// obtained. There is absolutely no warranty for non-commercial use.
//
//
// 1. BECAUSE THE PROGRAM IS LICENSED FREE OF CHARGE, THERE IS NO
//    WARRANTY FOR THE PROGRAM, TO THE EXTENT PERMITTED BY APPLICABLE
//    LAW.  EXCEPT WHEN OTHERWISE STATED IN WRITING THE COPYRIGHT
//    HOLDERS AND/OR OTHER PARTIES PROVIDE THE PROGRAM "AS IS" WITHOUT
//    WARRANTY OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING, BUT
//    NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
//    FITNESS FOR A PARTICULAR PURPOSE.  THE ENTIRE RISK AS TO THE
//    QUALITY AND PERFORMANCE OF THE PROGRAM IS WITH YOU.  SHOULD THE
//    PROGRAM PROVE DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY
//    SERVICING, REPAIR OR CORRECTION.
//   
// 2. IN NO EVENT UNLESS REQUIRED BY APPLICABLE LAW OR AGREED TO IN
//    WRITING WILL ANY COPYRIGHT HOLDER, OR ANY OTHER PARTY WHO MAY
//    MODIFY AND/OR REDISTRIBUTE THE PROGRAM AS PERMITTED ABOVE, BE
//    LIABLE TO YOU FOR DAMAGES, INCLUDING ANY GENERAL, SPECIAL,
//    INCIDENTAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE USE OR
//    INABILITY TO USE THE PROGRAM (INCLUDING BUT NOT LIMITED TO LOSS OF
//    DATA OR DATA BEING RENDERED INACCURATE OR LOSSES SUSTAINED BY YOU
//    OR THIRD PARTIES OR A FAILURE OF THE PROGRAM TO OPERATE WITH ANY
//    OTHER PROGRAMS), EVEN IF SUCH HOLDER OR OTHER PARTY HAS BEEN
//    ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
//   
//    END OF TERMS AND CONDITIONS
// 
//#endinclude


package de.kawt.shell;

import java.net.*;
import java.io.*;
import java.util.*;

public class FtpShell extends AbstractShell {
    
    Socket controlConnection;
    InputStream controlInputStream;
    OutputStream controlOutputStream;

    Socket dataConnection;
    InputStream dataInputStream;
    OutputStream dataOutputStream;

    String host;
    String user;
    String pass;
    String path = "/";
    Vector currentDir;


    String exec (String in) throws IOException {
	for (int i=0; i < in.length (); i++) 
	    controlOutputStream.write ((byte) in.charAt (i));

	controlOutputStream.write (13);
	controlOutputStream.write (10);

	return readControlLine ();
    }


    public boolean cd (String path) {
	try {
	    exec ("cwd "+path);
	    currentDir = null;
	    return true;
	}
	catch (IOException e) {
	    return false;
	}
    }
    

    public Vector ls () {

	try {
	    Vector currentDir = new Vector ();

	    pasv (false);
	    exec ("list");
	
	    StringBuffer s = new StringBuffer ();
	    
	    boolean ignore = true;
	    
	    while (true) {
		int i = dataInputStream.read ();
		if (i == -1) break;
		if (i == 13) {
		    if (ignore) ignore = false;
		    else {
			String line = s.toString ();
			
			int cut = line.lastIndexOf (' ');
			
			currentDir.addElement 
			    (new FileInfo (line.substring (cut+1), 
					   line.charAt (0) == 'd' 
					   ? FileInfo.DIRECTORY 
					   : 0));
		    }
		    s = new StringBuffer ();
		}
		else if (i > 31) s.append ((char) i);
	    }
	    
	    closeStream ();
	    
	    return currentDir;
	}
	catch (IOException e) {
	    return null;
	}
    }

    
    public void closeStream () throws IOException {
	dataInputStream.close ();
	dataOutputStream.close ();
	dataConnection.close ();
	readControlLine ();
    }


    public void close () {
	try {
	    controlInputStream.close ();
	    controlOutputStream.close ();
	    controlConnection.close ();
	}
	catch (IOException e) {
	    throw new RuntimeException (e.toString ());
	}
    }

    public void open (String host, String user, String pass) 
	throws IOException {

	this.host = host;
	this.user = user;
	this.pass = pass;

	controlConnection = new Socket (host, 21);
	controlInputStream = controlConnection.getInputStream ();
	controlOutputStream = controlConnection.getOutputStream ();
	
	readControlLine ();
	
	exec ("user "+user);
	exec ("pass "+pass);
    }


    void pasv (boolean binary) throws IOException {
	exec (binary ? "type i" : "type a");
	String passive = exec ("pasv");
	// changes for J9 compatibility
	passive = passive.substring 
	    (passive.indexOf ('(', 0)+1, 
	     passive.indexOf (')', 0));
	// end J9 chnages
	
	//before J9 changes	
	//passive = passive.substring 
	//(passive.indexOf ('(')+1,
	// passive.indexOf (')'));

//#ifdef j9
//# 	int len = passive.length();
//# 	StringBuffer newPassive = new StringBuffer(len);
//# 	for (int i = 0; i < len; i++) {
//# 	    char ch = passive.charAt(i);
//# 	    if (ch == ',')
//# 		newPassive.append('.');
//# 	    else
//# 		newPassive.append(ch);
//# 	}
//# 	passive = newPassive.toString();
//#else
passive = passive.replace (',', '.');
//#endif

	int cut = passive.lastIndexOf ('.');
		
	int port = Integer.parseInt (passive.substring (cut+1));
		
	passive = passive.substring (0, cut);

	cut = passive.lastIndexOf ('.');

	port = port + 256*Integer.parseInt (passive.substring (cut+1));
		
	passive = passive.substring (0, cut);

	dataConnection = new Socket (passive, port);
	dataInputStream = dataConnection.getInputStream ();
	dataOutputStream = dataConnection.getOutputStream ();
    }


    public String pwd () {
	return path;
    }


    public InputStream getInputStream (String name) throws IOException {
	pasv (true);
	String ret = exec ("retr "+name);
	// changes for J9 compatibility
 	int i0 = ret.lastIndexOf ('(');
	
	// before J9 changes
	//int i0 = ret.indexOf ('(');

	int i1 = ret.indexOf (' ', i0);

	transferSize = Integer.parseInt (ret.substring (i0+1, i1)); 

	return dataInputStream;
    }


    public OutputStream getOutputStream (String name) throws IOException {
	pasv (true);
	String ret = exec ("stor "+name);
	return dataOutputStream;
    }


    public void mkdir (String name) {
	try {
	    exec ("mkd "+name);
	}
	catch (IOException e) {}
    }


    public void rm (String name) {
	try {
	    exec ("dele "+name);
	    exec ("rmd "+name);
	}
	catch (IOException e) {}
    }


    String readControlLine () throws IOException {
	StringBuffer s;
	
	do {
	    s = new StringBuffer ();

	    while (true) {
		int i = controlInputStream.read ();
		if (i == -1 || i == 13) break;
		if (i > 31) s.append ((char) i);
	    }
	}
	while (s.length () < 4 || s.charAt (3) == '-');
	return s.toString ();
    }
}
