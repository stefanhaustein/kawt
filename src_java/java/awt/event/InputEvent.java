// InputEvent.java - R0996
//	
//#include ..\..\..\license.txt
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


package java.awt.event;

import java.awt.*;



public class InputEvent extends ComponentEvent {

    public final static int ALT_MASK = 8;
    public final static int BUTTON1_MASK = 16;
    public final static int BUTTON2_MASK = 8;
    public final static int BUTTON3_MASK = 4;
    public final static int CTRL_MASK = 2;
    public final static int META_MASK = 4;
    public final static int SHIFT_MASK = 1;

    
    long when;
    int modifiers;

    InputEvent(Component source, int id, long when, int modifiers) {
        super(source, id);
        this.when = when;
        this.modifiers = modifiers;
    }


    /** "Consumes" the event. If an event is consumed by the
	application, kAWT will not perform any default action
	associated with this event. */


    public void consume() {
        consumed = true;
    }
  
    public long getWhen() {
        return when;
    }


    public int getModifiers() {
        return modifiers;
    }



    public boolean isConsumed() {
        return consumed;
    }


    public boolean isControlDown() {
        return (modifiers & CTRL_MASK) != 0;
    }


    public boolean isMetaDown() {
        return (modifiers & META_MASK) != 0;
    }

    public boolean isShiftDown() {
        return (modifiers & SHIFT_MASK) != 0;
    }


    public boolean isAltDown() {
        return (modifiers & ALT_MASK) != 0;
    }

/*
 * $Log: InputEvent.java,v $
 * Revision 1.3  2001/08/27 23:31:17  haustein
 * keyevent constants added, log added
 *
 */


}
