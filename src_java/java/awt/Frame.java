// Frame.java
//
// 2000-09-08 MK Added new Licensetext
// 2000-09-06 SH Changed indexOf for J9 compatibility
// 2000-08-13 SH Title line Bug resulting from MIDP port fixed
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
import java.util.Vector;
import de.kawt.impl.Laf;

public class Frame extends Window {

    String title;
    MenuBar menuBar;

    public Frame () {
        this (null);
        /*
          x = 0; 0 by default!
          y = 0;
          title = null;
        */
    }

    
    public Frame (String t) {
        super (null);
        //      owner = this;

        setSize (Toolkit.defaultToolkit.getScreenSize ());

        setTitle (t);
    }
    

    public Insets getInsets () {
	return title != null 
	    ? Laf.laf.getWindowInsets (Laf.WINDOW_FRAME)
            : new Insets (0, 0, 0, 0);
    }

   
    public Dimension getMinimumSize () {
        return Toolkit.defaultToolkit.getScreenSize ();
    }
    
    public String getTitle () {
	return title;
    }


    public void setMenuBar (MenuBar menuBar) {
        this.menuBar = menuBar;
    }

    
    public void setTitle (String title) {

        //if (Toolkit.defaultToolkit.getScreenSize ().height < 160) title = null;

        if ((title == null) != (this.title == null)) {
            valid = false;

            this.title = title;
	    validate ();
	    repaint ();
        }
        else if (title != null && !title.equals (this.title)) {
            this.title = title;
            Graphics g = getGraphics ();
            paint (g);
            g.dispose ();
        }
    }
     

    public boolean isFocusTraversable () {
        return menuBar != null;
    }


    public void paintAll (Graphics g) {

        Laf.laf.drawWindow (g, w, h, Laf.WINDOW_FRAME, title, hasFocus);
	super.paintAll (g);
    }


    void action (InputEvent ev) {
	if (menuBar != null && ((!(ev instanceof MouseEvent)) || ((MouseEvent)ev).getY () <= getInsets().top)) 
	    menuBar.show ();
    }
}



