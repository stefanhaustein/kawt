// TextComponent.java
//
//  2000-09-06 EchoChar added, moved some functionality here from InputLine
//  2000-08-17 Event Bug fixed	
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


/** common abstract base class for TextArea and TextField */

public class TextComponent extends Component {

    int caretPos;
    int caretPos0;
    int org; // ankerpunkt for mouse-selection
    char echoChar;

    boolean editable = true;
    TextListener textListener;
    boolean inside = false;  // needed at TextArea only, but set with mouse

    /** changed back from stringbuffer to string because of incomplete
	StringBuffer implementations in J9 and RIM. */
  
    String text = "";

    /** internal method, fires an event and forces a repaint */

    void modified (int position, boolean simple) {

	if (caretPos > text.length ()) caretPos = text.length ();
	if (caretPos0 > text.length ()) caretPos0 = text.length (); 

	if (textListener != null)
	    textListener.textValueChanged 
		(new TextEvent 
		    (this, TextEvent.TEXT_VALUE_CHANGED));
    
	if (!scrollTo (caretPos)) {
	    int y0 = getScreenPos (position).y;
	    repaint (0, y0, w, h-y0);
	}
    }

    
    /** sets the cursor / marked area to the given position.
	cares about ordering, painting and scrolling */
    
    void moveCursor (int origin, int current) {
	int oldCp0 = caretPos0;
	int oldCp = caretPos;

	if (current < 0) current = 0;
	else if (current > text.length ()) current = text.length ();

	if (origin < 0) origin = 0;
	else if (origin > text.length ()) origin = text.length ();

	if (origin < current) {
	    caretPos0 = origin;
	    caretPos = current;
	}
	else {
	    caretPos0 = current;
	    caretPos = origin;
	}

	if (!scrollTo (current)) 
	    repaint (Math.min (oldCp0, caretPos0),
		     Math.max (oldCp, caretPos));
    }


    /** Sets the textListener for this TextComponent. The TextListener
	is notified when the text content changes. Caution: In
	contrast to the AWT, KAWT supports * only one TextListener!  */

    public void addTextListener (TextListener tl) {
	if (textListener != null) 
	    throw new TooManyListenersException ();
	
	textListener = tl;
    }


    /** returns the scrren x-position for the given cursor position */

    Point getScreenPos (int cp) {
	Point p = new Point (0, 0);
	int lnr = getLineNr (cp, 0);
	String line = getLine (lnr);
	cp -= getLineStart (lnr);
	if (cp > line.length ()) cp = line.length ();
	p.x = fontMetrics.stringWidth (line.substring (0, cp));
	p.y = lnr * fontMetrics.getHeight ();
	return p;
    }


    /** returns the line nr for the given cursor position */

    int getLineNr (int cp, int nr0) {
	return 0;
    }


    /** returns the current line. performs echoChar replacement (!) if
	set. */

    String getLine (int nr) {
	
	int start = getLineStart (nr);
	int end = getLineStart (nr+1);
	if (end > start) {
	    int c = text.charAt (end-1);
	    if (c == '\n' 
	        || (c == ' ' && nr < getLineCount ()-1)) end--; 
	}      

	if (echoChar == 0) 
	    return text.substring (start, end);
	else { 
	    StringBuffer sb = new StringBuffer ();
	    while (end > start) {
		sb.append (echoChar); 
		end--;
	    }
	    return sb.toString ();
	}  
    }


    int getLineCount () {
	return 1;
    }


    /** returns the global index of the first character in the 
	n-th line */

    int getLineStart (int n) {
	return n == 0 ? 0 : text.length ();
    }

    
    int getTextPosition (int sx, int sy) {

	int nr = sy / fontMetrics.getHeight ();
	int w = 0;
	int cp = 0;
	String tmp = getLine (nr);

	while (cp < tmp.length ()) {
	    w += fontMetrics.stringWidth 
		(tmp.substring (cp, cp+1));
	    
	    if (sx < w) break;
	    cp++;
	}
	return cp + getLineStart (nr);
    }

    /** Returns the current caret position */

    public int getCaretPosition () {
	return caretPos;
    }


    /** Performs the action associated with the given KeyEvent */

    public void dispatchEvent (AWTEvent event) {

	if (event instanceof MouseEvent) inside = true;
	
	super.dispatchEvent (event);
	if (event.consumed) return;

	if (event instanceof KeyEvent) {
	    KeyEvent ke = (KeyEvent) event;

	    if (ke.getID () == KeyEvent.KEY_TYPED) {

		if (!editable) return;  // shortcut for lineW < "virtualW"
		
		boolean simple = caretPos0 == caretPos;
		int p0 = caretPos0;
		
		text = text.substring (0, caretPos0)
		    + ke.getKeyChar ()
		    + text.substring (caretPos, text.length ());  
		
		ke.consumed = true;
		
		caretPos0++;
		caretPos = caretPos0;
		
		modified (p0, simple);
	    }
	    else if (ke.getID () == KeyEvent.KEY_PRESSED) {
		int kc = ke.getKeyCode ();
		switch (kc) {
		    

		case KeyEvent.VK_LEFT:
		    moveCursor (caretPos0-1, caretPos0-1);
		    ke.consumed = true;
		    break;
		    
		case KeyEvent.VK_RIGHT:
		    moveCursor (caretPos+1, caretPos+1);
		    ke.consumed = true;
		    break;
		    
		case KeyEvent.VK_BACK_SPACE:
		    if (editable) {
			int p0 = caretPos0 > 0 ? caretPos0 - 1 : 0; 
			text = text.substring (0, p0)
			    + text.substring (caretPos, text.length ());  
			
			ke.consumed = true;
			caretPos = p0;
			caretPos0 = p0;
			modified (p0, false);
		    }
		    break;
		
		    /*   default:
			 text.append (ke.getKeyCode ());
			 changed ();
		    */
		}
	    }
	}
	else if (event instanceof MouseEvent) {

	    if (event.id == MouseEvent.MOUSE_RELEASED) return;

	    MouseEvent me = (MouseEvent) event;

	    if (!hasFocus) requestFocus ();
	    
	    int cp = getTextPosition (me.getX (), me.getY ());

	    if (cp != org || me.getID () == MouseEvent.MOUSE_PRESSED) {
		if (me.getID () == MouseEvent.MOUSE_PRESSED) org = cp;
		moveCursor (org, cp);
	    }
	}
    }
    

    /** returns if scrolling was necessary. If so, a repaint is 
	already scheduled by scrollTo. */

    boolean scrollTo (int cp) {
	return false;
    }

    /** returns the selected substring of the text content. If no text
        is selected, an empty String is returned. */

    public String getSelectedText () {
	return getText ().substring (caretPos0, caretPos);
    }


    /** returns the start position of the current selection. */ 

    public int getSelectionStart () {
	return caretPos0;
    }
	
    /** returns the end position of the current selection. */ 
	
    public int getSelectionEnd () {
	return caretPos;
    }
		

    /** Internal method. Draws a text line at the given screen
	position. The line content is determined from the given cursor
	position */

    void drawLine (Graphics g, int x0, int y0, int nr) {
	
	int x00 = x0;
	int p0 = getLineStart (nr);
	int p1 = getLineStart (nr+1); // bei substr ist p1 excl. 

	String line = getLine (nr);
	int len = line.length ();

	boolean cursor = false;
	int lh = fontMetrics.getHeight ();
	int ly = y0+lh-1;
	int ty = y0 + fontMetrics.getAscent ();
	String s;
	int cursorX = 0;

	g.setColor (Color.white);
	    
	if (hasFocus) {	
	    int toBlack = getSelectionStart () - p0;
	    int toWhite = getSelectionEnd () - p0;
	    if (toBlack < 0) toBlack = 0;
	    else if (toBlack > len) toBlack = len;
	    else cursor = nr < getLineCount ();
	    
	    if (toWhite < 0) toWhite = 0;
	    else if (toWhite > len) toWhite = len;
	    
	    // white
	
	    s = line.substring (0, toBlack);
	    int tw = fontMetrics.stringWidth (s);

	    g.fillRect (x0, y0, tw, lh-1);
	    if (editable) g.setColor (Color.gray);
	    g.drawLine (x0, ly, x0 + tw, ly);
	    g.setColor (Color.black);
	    g.drawString (s, x0, ty);

	    // black
	    
	    x0 += tw;
	    s = line.substring (toBlack, toWhite);
	    tw = fontMetrics.stringWidth (s);
	    cursorX = x0;
	    cursor &= tw == 0;
	    
	    g.fillRect (x0, y0, tw, lh);
	    g.setColor (Color.white);
	    g.drawString (s, x0, ty);
	    
	    // white

	    x0 += tw;
	    s = line.substring (toWhite, len);
	}
	else
	    s = line;

	//	int tw = fontMetrics.stringWidth (s);

	g.fillRect (x0, y0, 999, lh-1);  // dont sub x0 since x0 may be < 0
	if (editable) g.setColor (Color.gray);
	g.drawLine (x0, ly, 999, ly);
	g.setColor (Color.black);
	g.drawString (s, x0, ty);

	if (cursor) 
	    g.fillRect (cursorX == 0 ? 0 : cursorX-1, y0, 2, lh);

	
    }


    /** Returns the String content of this text component */

    public String getText () {
	return text;
    }


    /** Returns true if the Textfield is editable, false otherwise. */

    public boolean isEditable () {
	return editable;
    }


    /** Returns if the textfield can be reached by focus
	traversal. True if the Textfield is editable, false
	otherwise. */

    public boolean isFocusTraversable () {
        return editable;
    }


    /*
    public void processTextEvent (TextEvent te) {
	if (textListener != null) 
	    textListener.textValueChanged (te);
    }
    */

    /** Removes the given TextListener from this TextArea. */
    
    public void removeTextListener (TextListener tl) {
	if (tl == textListener) 
	    textListener = null;
    }


    /** Selects the text at the given positon */

    public void select (int start, int end) {
	caretPos0 = start;
	caretPos = end;
	repaint ();
    }


    /** Selects the whole text content. */

    public void selectAll () {
	caretPos0 = 0;
	caretPos = text.length ();
	repaint ();
    }
	

    /** Sets the text content to editable or not editable. */ 

    public void setEditable (boolean ed) {
	if (ed != editable) {
	    editable = ed;
	    repaint ();
	}
    }


    /** Sets the current cursor position */

    public void setCaretPosition (int cp) {
	moveCursor (cp, cp);
    }

  

    void repaint (int minCp, int maxCp) {
	//System.out.println ("repaint "+minCp+", "+maxCp);

	Point p = getScreenPos (minCp);
	Point p2 = getScreenPos (maxCp);

	if (Toolkit.classbase.equals ("de.kawt.impl.kjava") 
	    && fontMetrics.font.isBold ()) p2.x = 160; 

	repaint (p.x-1, p.y, 
		 p2.x - p.x + 2, 
		 fontMetrics.getHeight ()); 
    }


    /** Sets the start position of the current selection. */

    public void setSelectionStart (int i) {
	caretPos0 = i;
	repaint ();
    }


    /** Sets the end position of the current selection. */
    
    public void setSelectionEnd (int i) {
	caretPos = i;
	repaint ();
    }
    

    /** Sets the text content to the given String */

    public void setText (String newText) {
	text = newText == null ? "" : newText;
	modified (0, false);
    }   
}

    
