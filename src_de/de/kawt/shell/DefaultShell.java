// DefaultShell.java
//
// 2000-09-08 MK Added new Licensetext
// 2000-09-06 MK added changes for IBM J9 support
//            replaced java.io.File.listFiles with list()
//            in public Vector ls ()
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

import java.util.*;
import java.io.*;


public class DefaultShell extends AbstractShell {

    File path = new File (File.separator);
    
    public Vector ls () {

//#ifdef J9	
//# 	String [] list = path.list ();
//# 	Vector result = new Vector ();
//# 
//# 	result.addElement (new FileInfo ("..", FileInfo.DIRECTORY));
//# 
//# 	for (int i = 0; i < list.length; i++) {
//# 	    File f = new File(list[i]);
//# 	    if (f.isDirectory ())
//# 		result.addElement (new FileInfo (f));
//# 	}
//# 
//# 	for (int i = 0; i < list.length; i++) {
//# 	    File f = new File(list[i]);
//# 	    if (!f.isDirectory ())
//# 		result.addElement (new FileInfo (f));
//# 	}
//# 
//# 	return result;
//#else	
	//File [] list = path.listFiles ();
	String[] list = path.list();
	Vector result = new Vector ();

	result.addElement (new FileInfo ("..", FileInfo.DIRECTORY));
	/*
	  for (int i = 0; i < list.length; i++)
	  if (list [i].isDirectory ()) 
	  result.addElement (new FileInfo (list [i]));
	  
	  for (int i = 0; i < list.length; i++)
	  if (!list [i].isDirectory ())
	  result.addElement (new FileInfo (list [i]));
	*/

	for (int i = 0; i < list.length; i++) {
	    File file = new File (list[i]);
	    if (file.isDirectory ()) 
		result.addElement (new FileInfo (file));
	}

	for (int i = 0; i < list.length; i++) {
	    File file = new File (list[i]);
	    if (!file.isDirectory ()) 
		result.addElement (new FileInfo (file));
	    
	}
	

	return result;
//#endif
    }


    public boolean cd (String newDir) {
	File newpath = new File (path.toString (), newDir);
	
	if (newpath.isDirectory ()) {
	    path = newpath;
	    return true;
	}
	return false;
    }

    
    public void mkdir (String newDir) {
	new File (path.toString (), newDir).mkdir ();
    }


    public String pwd () {
	try {
	    return path.getCanonicalPath ();
	}
	catch (IOException e) {
	    throw new RuntimeException (e.toString ());
	}
    }


    public InputStream getInputStream (String localName) throws IOException {
	File f = new File (path, localName);
	transferSize = f.length ();
	return new FileInputStream (f);
    } 


    public OutputStream getOutputStream (String localName) throws IOException {
	return new FileOutputStream (new File (path, localName));
    }


    public void rm (String name) {
	new File (path, name).delete ();
    }
} 

