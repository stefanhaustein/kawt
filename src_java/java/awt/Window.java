// Window.java
//
// 2000-09-08 MK Added new Licensetext
// 2000-05-25 SH Immediate repaint implemented (because of modal windows)
// 2000-04-10 SH Fixed repaint problems for tiled windows   
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


public class Window extends Container {

    Frame owner;
    Component focus = this;
    boolean isMenu = false; // close if clicking elsewhere
    Vector dirty = new Vector ();

    public Window (Frame f) {
        super (new BorderLayout ());
        
        owner = f;
        visible = false;
        /*
        background = Color.white;
        foreground = Color.black;
        */
    }

    
    /** This method does not do anything and is included for
        compatibility only */

    public void addWindowListener (WindowListener l) {
    }
    

    synchronized void addRepaint (Rectangle r) {
        
        int i = dirty.size () - 1; 

        while (i >= 0) {
            Rectangle old = (Rectangle) dirty.elementAt (i);
            
            if ((old.x <= r.x + r.width + 4) 
                && (old.x + old.width + 4 >= r.x) 
                && (old.y <= r.y + r.height + 4) 
                && (old.y + old.height + 4 >= r.y)) {

                r.add (old);
                dirty.removeElementAt (i);
                i = dirty.size () - 1;
            }
            else i--;
        }
        
        dirty.addElement (r);
    }



    public void dispose () {
        // remove child windows (?!) 
        setVisible (false);
    }


    synchronized void flushRepaint () {
        for (int i = 0; i < dirty.size (); i++) {
            Rectangle r = (Rectangle) dirty.elementAt (i);
            Graphics g = getGraphics ();
            g.clipRect (r.x, r.y, r.width, r.height);
            update (g);
        }
        dirty.removeAllElements ();
    }


    public Component getFocusOwner () {
        return focus;
    }

    /*
    public Window getOwner () {
        return owner != this ? owner : null;
    }
    */
    /* 

    public Point getLocationOnScreen () {

        return (owner.getTopWindow () == this) ? new Point (x, y) : null;
    }
    
    */

    public Toolkit getToolkit () {
        return Toolkit.defaultToolkit;
    }


    public void pack () {
        setSize (getPreferredSize ());
        validate ();
        x = 0;
        y = owner == null ? 0 : owner.h - h;
    }
    
 
    void proxyRequestFocus (Component fc) {
        if (focus != null) {
            focus.hasFocus = false;

            //  if (focus instanceof TextField)
                //Caret.caret.setVisible (false);

	    Toolkit.eventQueue.postEvent 
		(new FocusEvent (focus, FocusEvent.FOCUS_LOST)); 
        }
        
        focus = fc;
	focus.hasFocus = true;

	Toolkit.eventQueue.postEvent 
	    (new FocusEvent (focus, FocusEvent.FOCUS_GAINED)); 

        //if (focus instanceof TextField)
            //Caret.caret.setVisible (true);
	
	//	focus.focusChanged ();
    }
    
  
    public void setVisible (boolean vis) {
        if (vis == visible) return;

        if (vis) {
            Laf.windows.addElement (this);

            visible = true;
            repaint ();
        }
        else {
            visible = false;

            // go down from top until a window complete covering 
            // the dirty area is found

            // if the covering window is above, boil out

            // go upwards and repaint

            int ownI = -1;
            int target = -1;
            
            for (int i = Laf.windows.size () - 1; i >= 0; i--) {
                Window win = (Window) Laf.windows.elementAt (i);
                if (win == this) ownI = i;
                else if (x >= win.x && y >= win.y 
                         && x + w <= win.x + win.w
                         && y + h <= win.y + win.h) {
                    target = i;
                    break;
                }
            }

            if (ownI == -1) {
                Laf.windows.removeElement (this);
                return;
            }

            Laf.windows.removeElementAt (ownI);

            if (target == -1) {
                // paint screen background here!!!!!!!!!!
                target = 0;
            }

            int cnt = Laf.windows.size () - target;
            Window [] buf = new Window [cnt];
            for (int i = Laf.windows.size () - 1; i >= target; i--) {
                buf [--cnt] = (Window) Laf.windows.elementAt (i);
                Laf.windows.removeElementAt (i);
            }

            for (int i = 0; i < buf.length; i++) {
                Window win = buf [i];
                Laf.windows.addElement (win);
                Graphics g = win.getGraphics ();
                g.clipRect (x - win.x, y - win.y, w, h);
                buf [i].update (g);
            }
        }
    }   
    

    public void show () {
        setVisible (true);
    }
}
