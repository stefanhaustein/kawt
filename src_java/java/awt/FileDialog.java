// FileDialog.java
//
// 2000-09-08 MK Added new Licensetext
// 2000-09-06 SH Changed endsWith to charAt for J9 compatibility
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


package java.awt;

import java.awt.event.*;
import de.kawt.*;
import de.kawt.shell.*;

public class FileDialog extends Dialog implements ActionListener {

    public static final int LOAD = 0;
    public static final int SAVE = 1;

    int mode;
    FileList fileList;
    TextField textField = new TextField ();

    String directory;
    String file;


    public FileDialog (Frame frame) {
	this (frame, null, LOAD);
    }

    public FileDialog (Frame frame, String title) {
	this (frame, title, LOAD);
    }

    public FileDialog (Frame frame, String title, int mode) {

	super (frame, title == null ? "Select File" : title, true);
	
	this.mode = mode;

	fileList = new FileList (null);
	fileList.setActive (true);
	
	add ("Center", fileList);

	Panel south = new Panel (new BorderLayout ());
	south.add ("West", new Label ("File:"));
	south.add ("Center", textField);

	Panel buttons = new Panel ();

	Button open = new Button (mode == LOAD ? "Open" : "Save");
	open.addActionListener (this);
	buttons.add (open);

	Button cancel = new Button ("Cancel");
	cancel.addActionListener (this);
	buttons.add (cancel);
	
	south.add ("East", buttons);

	add ("South", south);
	pack ();
    }


    public void actionPerformed (ActionEvent e) {
	if (!e.getActionCommand ().equals ("Cancel")) {

	    file = textField.getText ();
	    if (file == null || file.equals ("")) {
		file = fileList.getSelectedFile ();
		// changes for J9 compatibility
		int len = file.length ();
 		if (len > 0 && file.charAt (len-1) == '/') {
		// end of J9 changes
		
		// before J9 changes
		//if (file.endsWith ("/")) {    
		    fileList.cd (file);
		    file = null;
		    return;
		}
	    }
	    directory = fileList.getShell ().pwd ();
	}
	setVisible (false);
    }


    public String getFile () {
	return file;
    }


    public String getDirectory () {
	return directory;
    }
}
    
