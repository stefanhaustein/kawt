// SpotletImpl.java
//
//  2000-10-06 SH Initial Version
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


package de.kawt.impl.kjava;

import de.kawt.impl.Laf;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class SpotletImpl extends com.sun.kjava.Spotlet {

    static boolean jogRepeated;
    BeamListener beamListener;
    Point mouseOrg = new Point (0,0);
    Component mouseSource;

    public SpotletImpl () {
	register (NO_EVENT_OPTIONS);

	// die eventqueue kann hier nicht zugegr werden, da dies
	// aus dem constr. von Toolkit aufger. wird...
    }


    public void keyDown (int palmCode) {

	int keyCode = KeyEvent.VK_UNDEFINED;
	char charCode = KeyEvent.CHAR_UNDEFINED;
	int type = KeyEvent.KEY_PRESSED;

	Component source = Laf.laf.getKeyEventSource ();
	if (source == null) return;

	switch (palmCode) {
        case 0: 
	    ToolkitImpl.adjustScreenSize();
	    Laf.laf.paintAll ();
	    return; 
	case 8: keyCode = KeyEvent.VK_BACK_SPACE; break;
	case 10: keyCode = KeyEvent.VK_ENTER; break;
	case 28: keyCode = KeyEvent.VK_LEFT; break;
	case 29: keyCode = KeyEvent.VK_RIGHT; break;
	case 30: keyCode = KeyEvent.VK_DOWN; break;
	case 31: keyCode = KeyEvent.VK_UP; break;
	case 259: keyCode = KeyEvent.VK_UP; break;
	case 268: keyCode = KeyEvent.VK_DOWN; break;
	case 0x1704: 
	case PAGEDOWN: keyCode = KeyEvent.VK_PAGE_DOWN; break;
	case 0x1703:
	case PAGEUP: keyCode = KeyEvent.VK_PAGE_UP; break;
	case MENUICON: keyCode = KeyEvent.VK_PROPS; break;
	case 0x1700: keyCode = KeyEvent.VK_UP; break;
	case 0x1701: keyCode = KeyEvent.VK_DOWN; break;
	case 0x1702: jogRepeated = true; return;
        case 0x1705: jogRepeated = false; return;
        case 0x1706: keyCode = jogRepeated ? KeyEvent.VK_CANCEL : KeyEvent.VK_ACCEPT; break; 
	default: 
	    charCode = (char) (palmCode < ' ' ? palmCode + '@' : palmCode);
	    type = KeyEvent.KEY_TYPED;
	}
	
	Toolkit.getDefaultToolkit ().getSystemEventQueue ().postEvent
	    (new KeyEvent 
		(source,
		 type, 0, 0,
		 keyCode, charCode));
    }
    
    
    public void penDown (int x, int y) {

	mouseSource = Laf.laf.getMouseEventSource (x, y, mouseOrg);
	if (mouseSource == null) return;

	Toolkit.getDefaultToolkit ().getSystemEventQueue ().postEvent 
	    (new MouseEvent (mouseSource,
			     MouseEvent.MOUSE_PRESSED, 0, 0,
			     x-mouseOrg.x, y-mouseOrg.y, 0, false));
    }
    

    public void penUp (int x, int y) {
	if (mouseSource == null) return;

	EventQueue eventQueue = 
	    Toolkit.getDefaultToolkit ().getSystemEventQueue ();

	x -= mouseOrg.x;
	y -= mouseOrg.y;

	eventQueue.postEvent 
	    (new MouseEvent (mouseSource,
			     MouseEvent.MOUSE_RELEASED, 0, 0, x, y,
 			     0, false));
	
 
 	eventQueue.postEvent
 	    (new MouseEvent (mouseSource,
 			     MouseEvent.MOUSE_CLICKED, 0, 0, x, y,
 			     0, false));
    }
    
    
    public void penMove (int x, int y) {
	if (mouseSource == null) return;

	Toolkit.getDefaultToolkit ().getSystemEventQueue ().postEvent 
	    (new MouseEvent (mouseSource, MouseEvent.MOUSE_DRAGGED, 0, 0,
			     x-mouseOrg.x, y-mouseOrg.y, 0, false));
     }

    
    public void setBeamListener (BeamListener bl) {
	beamListener = bl;
    }

    public void beamReceive (byte [] data) {
	if (beamListener != null) {
	    beamListener.beamReceive (data);
	}
    }
}
