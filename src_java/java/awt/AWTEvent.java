// AWTEvent.java - 0996
//
// 2000-09-08 MK Added new Licensetext
// 2000-08-25 EventObject superclass added
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

import java.util.EventObject;

/** This is the base class for all awt events. */

public class AWTEvent extends EventObject {

    public static final long FOCUS_EVENT_MASK = 0x04;
    public static final long KEY_EVENT_MASK = 0x08;
    public static final long MOUSE_EVENT_MASK = 0x10;
    public static final long MOUSE_MOTION_EVENT_MASK = 0x20;
    public static final long WINDOW_EVENT_MASK = 0x40;
    public static final long ACTION_EVENT_MASK = 0x80;
    public static final long ADJUSTMENT_EVENT_MASK = 0x100;
    public static final long ITEM_EVENT_MASK = 0x200;
    public static final long TEXT_EVENT_MASK = 0x400;
    public static final int RESERVED_ID_MAX = 1999;

    /** The id determines the type of the event: MOUSE_PRESSED,
	KEY_TYPED, MOUSE_CLICKED... */

    protected int id;

    /** The consumed status of the event. If this is set to true
	by the application, kAWT will not perform any default
	action associated with this event. */

    protected boolean consumed;
    
    public AWTEvent (Object source, int id) {
	super (source);
	this.id = id;
	consumed = false;
    }

    
    /** "Consumes" the event. If an event is consumed by the
	application, kAWT will not perform any default action
	associated with this event. */

    protected void consume () {
	consumed = true;
    }


    /** Queries the consumed state. */

    protected boolean isConsumed () {
	return consumed;
    }


    /* A private method that allows the event queue to adjust the
        source.

    void setSource (Object source) {
	this.source = source;
    } 

 */
    
    /** Returns the type of the event: MOUSE_PRESSED, KEY_TYPED,
	MOUSE_CLICKED... */

    public int getID () {
	return id;
    }
}
