// FileList.java
//
// 2000-09-08 MK Added new Licensetext
//
//#include ..\license.txt
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


package de.kawt;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.io.*;

import de.kawt.shell.*;


public class FileList extends Panel implements MouseListener, ItemListener  {

    boolean active;
    List list = new List ();
    Label title = new Label ();
    AbstractShell shell;
    FileListActivationListener activationListener;
    
    public FileList (FileListActivationListener activationListener) {
	super (new BorderLayout ());
	
	add ("North", title);	
	add ("Center", list); 

	title.addMouseListener (this);
	list.addItemListener (this);

	this.activationListener = activationListener;
	
	setShell (new DefaultShell ());
	
	active = true;
	setActive (false);
    }

    

    public void setShell (AbstractShell shell) {
	this.shell = shell;
	cd (shell.pwd ());
    }

    public AbstractShell getShell () {
	return shell;
    }


    public String getSelectedFile () {
	return list.getSelectedItem ();
    }

    public void cd (String path) {

	shell.cd (path);
	//title.repaint ();
	title.setText (shell.pwd ());

	Vector l = shell.ls ();

	list.removeAll ();
	for (int i= 0; i < l.size (); i++) {
	    list.add (l.elementAt (i).toString ());
	}
    }

    public void itemStateChanged (ItemEvent e) {
	if (e.getStateChange () == e.SELECTED) 
	    setActive (true);
    }

    
    public void mouseClicked(MouseEvent e) {
	setActive (true);
    } 

    public void mouseEntered(MouseEvent e) {} 
    public void mouseExited(MouseEvent e) {} 
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {} 
   

    public void setActive (boolean active) {

	if (this.active == active) return;

	this.active = active;
	
	if (active) { 
	    title.setBackground (SystemColor.activeCaption);
	    title.setForeground (SystemColor.activeCaptionText);
	    if (activationListener != null)
		activationListener.fileListActivated (this);
	}
	else {
	    title.setBackground (SystemColor.inactiveCaption);
	    title.setForeground (SystemColor.inactiveCaptionText);
	    list.deselect (list.getSelectedIndex ());
	}
    }
}

