// Button.java - R0996
//
//#include ..\..\license.txt
//
// kAWT
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

import java.util.*;
import java.awt.event.*;
import de.kawt.impl.Laf;

public class Button extends Component {  

    private Chain actionListeners;
    
    String label;
    String command;
    
    boolean pressing = false;

    /** Constructs a new button without label */
    
    public Button () {
        this ("");
    }


    /** Constructs a new button with the given label */

    public Button (String s) {
        setLabel (s);
    }
  

    /** Adds an action listener to the button. The listener will be notified 
	when the button is "pressed". <b>Attention:</b> kAWT supports only
        a single action listener for each button */

    public void addActionListener(ActionListener al) {
        actionListeners = new Chain (al, actionListeners);
	eventMask |= AWTEvent.ACTION_EVENT_MASK; 
    }
    
    
    /** kAWT internal action handling method */

    void action (InputEvent e) {
        if (e instanceof MouseEvent || actionListeners == null) return;
        

	Chain c = actionListeners;
	while (c != null) {
	    ((ActionListener) c.element).actionPerformed 
                ((new ActionEvent 
                  (this, ActionEvent.ACTION_PERFORMED, 
                   command != null ? command : label)));
	    c = c.next;
	}
    }




    public void dispatchEvent (AWTEvent event) {
        super.dispatchEvent (event);

        if (event.consumed || !(event instanceof MouseEvent)) return;

	MouseEvent me = (MouseEvent) event;

        boolean inRange =  me.getX () >= 0 && me.getX () <= w 
            && me.getY () >= 0 && me.getY () <= h;
        
        boolean isPressing = me.getID () == MouseEvent.MOUSE_PRESSED 
            || (me.getID () == me.MOUSE_DRAGGED && inRange);

        if (isPressing != pressing) {
            Graphics g = getGraphics ();
            pressing = isPressing;
            if (g != null) {
                paint (g);
            }
        }

        if (inRange && me.getID () == MouseEvent.MOUSE_RELEASED)
            action (null);
    }


    /** returns the action command assigned to this button. If null,
        the label is returned.  */

    public String getActionCommand () {
	return command == null ? label : command;
    }

    /** Returns the label of the button. */

    public String getLabel () {
	return label;
    }


    public Dimension getMinimumSize () {
	return Laf.laf.getMinimumButtonSize (fontMetrics, label);
    }



    /** Draws the button on the given graphics context. */
    
    public void paint (Graphics g) {
	Laf.laf.drawButton (g, background, w, h, label, hasFocus, pressing); 
    }
    
    
    protected void processActionEvent (ActionEvent ev) {
	Chain c = actionListeners;
	while (c != null) {
	    ((ActionListener) c.element).actionPerformed (ev);
	    c = c.next;
	}      
   }


    protected void processEvent (AWTEvent e) {
        if (e instanceof ActionEvent) 
            processActionEvent ((ActionEvent) e);
	else super.processEvent(e);
    }

  
    /** Removes the given action listener. */

    public void removeActionListener (ActionListener al) {
	if (actionListeners != null) 
	    actionListeners = actionListeners.remove (al);
    }
    

    /** Sets the action command for action events to the given
	string. The default action command string is the label of the
	button. */

    public void setActionCommand (String s) {
        command = s;
    }

    
    /** Sets the label of the button. The button will be
	repainted automatically as soon as the event handling
	routine returns. */

    public void setLabel (String s) {
        this.label = s;
        repaint ();
    }

/*
 * $Log: Button.java,v $
 * Revision 1.8  2001/09/22 20:54:06  haustein
 * Button actionlistener changed to multicast model
 *
 * Revision 1.7  2001/08/28 23:18:41  haustein
 * getLabel was missing
 *
 * Revision 1.6  2001/08/28 23:09:02  haustein
 * ActionEvent issue fixed, Button fixess for 0996.
 *
 *
 */
}








