// TextField.java
//
//  2000-09-07 Repaint problem fixed
//  2000-08-17 Event Bug fixed
//  2000-07-24 Bugfix: changed ke. to KeyEvent. in processEvent
//  2000-06-23 _setText added, moved some methods 
//             to/from TextComponent (for TextArea impl. simplification)
//
//include ..\..\license.txt
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

/** Current Limitations
    <ul>
    <li>The caret blinking is currently disabled because of often system 
    crashes when running threads
    <li>ActionEvents are not sent (but TextEvents)
    </ul>

    The colum count of the text field determines the minimal display width.
*/

public class TextField extends TextComponent {
    
    int scrollOffset = 0;
    int cols;
    
    /** Creates a new text field */

    public TextField () {
	this ("", 10);
    }
    

    /** Creates a new text field with the given colum count. */

    public TextField (int cols) {
	this ("", cols);
    }
    

    /** Creates a new text field that is initialized with the given String */

    public TextField (String s) {
	this (s, 10);
    }


    /** Creates a new text field that is initialized with the given String
	and colum count. */

    public TextField (String text, int cols) {
	setText (text);
	this.cols = cols;
    }


    /** query if an echo char is set */
    
    public boolean echoCharIsSet () {
	return echoChar != 0; 
    }


    /** Returns the echo char or '\0' if none set. */

    public char getEchoChar () {
	return echoChar;
    }

    
    public Dimension getMinimumSize () {
	return new Dimension 
	    (fontMetrics.stringWidth ("W") * cols, 
	     fontMetrics.getHeight ());
    }


    public void paint (Graphics g) {
	Point p = getScreenPos (0);
	drawLine (g, -scrollOffset, p.y, 0);
    }


    Point getScreenPos (int cp) {
	Point p = super.getScreenPos (cp);
	p.x -= scrollOffset;
	return p;
    }


    int getTextPosition (int x, int y) {
	return super.getTextPosition (x + scrollOffset, y); 
    }



    public void setEchoChar (char c) {
	echoChar = c;
	repaint ();
    }


    boolean scrollTo (int cp) {
	int scrCaretPos = getScreenPos (caretPos).x;
	int old = scrollOffset;

	if (w < 2) return false;

	while (scrCaretPos > w) {
	    scrollOffset +=  w / 2;
	    scrCaretPos -= w/2;
	}
	
	while (scrCaretPos < 0) { 
	    scrollOffset -=  w / 2;
	    scrCaretPos += w/2;
	}
	
	if (scrollOffset < 0) 
	    scrollOffset = 0;

	if (old != scrollOffset) {
	    repaint ();
	    return true;
	}
	return false;
    }
}






