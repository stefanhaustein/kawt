// TextArea.java
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
import de.kawt.WordWrap;

/** A simple TextArea, now with support for editing and word wrap. */

public class TextArea extends TextComponent {

    //StringBuffer starts = new StringBuffer ();

    int [] starts = new int [10];
    int startsSize = 0;

    //    Vector content = new Vector ();
    int maxWidth = 50;
    int maxHeight = 50;

    { w = 50; h = 50; }


    ScrollManager scrollManager = new ScrollManager 
	(this, 
	 Laf.laf.getBorderInsets (Laf.BORDER_DOUBLE));



    void modified (int position, boolean simple) {

	if (simple) {
	    //	    StringBuffer newStarts = new StringBuffer ();
	    int len = startsSize;
	    int i = 0;

	    while (i < len-1 && position >= starts [i]) 
		i++;

	    int line = i-1;
	    
	    while (i < len) 
		starts [i] = starts [i++]+1;

	    String l = getLine (line);
	    int lineH = fontMetrics.getHeight ();
	    int lineW = fontMetrics.stringWidth (getLine (line));

	    /*
	    if (lineW > maxWidth){
		maxWidth = (lineW + 15) & ~15;
		doLayout ();
	    }
	    */
	    // is shortcut really allowed (if not, just continue 
	    // with "not simple" modified branch

	    if (lineW <= maxWidth) { 
		if (!scrollTo (caretPos0)) {
		    //	    System.out.println ("!scrto");
		    repaint (scrollManager.insets.left, 
			     scrollManager.insets.top 
			     + line * lineH - scrollManager.yOffset,
			     scrollManager.viewPortW, lineH);
		}
		//    else System.out.println ("scrto");

		return;
	    }
	}

	// reset starts and rebuild completely
	startsSize = 1;  // first entry is always 0
	
	int i0 = 0;
	int oldMaxHeight = maxHeight;
	int oldMaxWidth = maxWidth;
	maxWidth = scrollManager.viewPortW;
	    
	de.kawt.WordWrap wrap = 
	    new de.kawt.WordWrap (fontMetrics, text, maxWidth);

	while (true) {
	    int i = wrap.next ();
	    if (i == -1) break;
	    addStart (i);
	}

	if (text.length () == 0 || text.charAt (text.length ()-1) == '\n')
	    addStart (text.length ());

	if (wrap.getMaxWidth () > maxWidth)
	    maxWidth = (wrap.getMaxWidth () + 15) & ~15;
	
	maxHeight = fontMetrics.getHeight () * (startsSize-1);
	
	doLayout ();
	
	if (visible && (oldMaxWidth != maxWidth || oldMaxHeight != maxHeight) 
	    && visible)
	    
	    repaint ();
	
	super.modified (position, false);
    }

    
    public void setBounds (int x, int y, int w, int h) {
	super.setBounds (x, y, w, h);
	doLayout (); // set ViewPort 
	modified (0, false);
    }
   
    
    public void addStart (int i) {
	if (starts.length <= startsSize) {
	    int [] save = starts;
	    starts = new int [startsSize + 10];
	    System.arraycopy (save, 0, starts, 0, save.length);
	}
	
	starts [startsSize++] = i;
    }
    
    
    public void paint (Graphics g) {
	
	if (!visible) return;

	//System.out.println ("paintall beg");
	
	Rectangle clip = scrollManager.paint 
	    (g, hasFocus && !inside 
	     ? Laf.BORDER_FOCUSSED : Laf.BORDER_FOCUSABLE );

	int lineH = fontMetrics.getHeight ();
	int i = clip.y / lineH;
	int cy = i * lineH;

	for (; cy < clip.height + clip.y; cy += lineH) 
	    drawLine (g, 0, cy, i++);


	//System.out.println ("paintall end");
    }


    public void dispatchEvent (AWTEvent event) {
	super.dispatchEvent (event);

	if (event.consumed  || event.id != KeyEvent.KEY_PRESSED) return;

	KeyEvent ke = (KeyEvent) event;

	switch (ke.getKeyCode ()) {

	case KeyEvent.VK_CANCEL:
	    if (!inside) break;
	case KeyEvent.VK_ACCEPT:
	    inside = !inside;
	    ke.consumed = true;
	    repaint ();
	    break;

	case KeyEvent.VK_DOWN:
	    if (inside) {
		int nr = getLineNr (caretPos0, 0);
		int np = caretPos0 - getLineStart (nr) + getLineStart (nr+1);
		int mx = getLineStart (nr+2);
		if (np >= mx) np = mx-1;
		moveCursor (np, np);
		ke.consumed = true;
	    }
	    break;

	case KeyEvent.VK_UP:
	    if (inside) {
		int nr = getLineNr (caretPos0, 0);
		int np = caretPos0 - getLineStart (nr) + getLineStart (nr-1);
		int mn = getLineStart (nr-2);
		if (np < mn) np = mn;
		moveCursor (np, np);
		ke.consumed = true;
	    }
	    break;

	case KeyEvent.VK_ENTER: // move this to textArea.processKeyEvent

	    if (editable) {
		text = text.substring (0, caretPos0)
		    + '\n'
		    + text.substring (caretPos, text.length ());
		caretPos = ++caretPos0;
		modified (caretPos0-1, false);
	    }
	    break;
	}
    }

    
    public void doLayout () {  
	scrollManager.doLayout 
	    (maxWidth, maxHeight);
    }

    
    public Component getComponentAt (int px, int py) {
	return scrollManager.findComponentAt (px, py);
    }

    int getLineCount () {
	return startsSize-1;
    }


    int getLineStart (int nr) {
	
	return nr < startsSize 
	    ? (nr < 0 ? 0 : starts [nr])
	    : text.length ();
    }


    int getLineNr (int cp, int nr0) {

	for (int i = nr0 + 1; i < startsSize; i++) 
	    if (cp < starts [i]) return i-1;
	
	return getLineCount ()-1;
    }

    public Dimension getMinimumSize () {
	return new Dimension (50, 50);
    }

    
    int getTextPosition (int sx, int sy) {
	return super.getTextPosition 
	    (sx - scrollManager.insets.left + scrollManager.xOffset,
	     sy - scrollManager.insets.top + scrollManager.yOffset); 
    }

    // replace by getScreenPos (Point p)


    Point getScreenPos (int cp) {
	Point p = super.getScreenPos (cp);
	p.x += scrollManager.insets.left - scrollManager.xOffset;
	p.y += scrollManager.insets.top - scrollManager.yOffset;
	return p;
    }
    

    public Dimension getPreferredSize () {
	return new Dimension 
	    (maxWidth + scrollManager.insets.left 
	     + scrollManager.insets.right, 
	     (startsSize - 1) * fontMetrics.getHeight () 
	     + scrollManager.insets.top 
	     + scrollManager.insets.bottom);
    }


    void repaint (int minCp, int maxCp) {
	//System.out.println ("l0");
	int l0 = getLineNr (minCp, 0);
	//System.out.println ("l1");
	int l1 = getLineNr (maxCp, l0); 
	//System.out.println ("lchk "+l0+"="+l1);

	int lineH = fontMetrics.getHeight ();
	repaint (scrollManager.insets.left, 
		 l0 * lineH + scrollManager.insets.top 
		 - scrollManager.yOffset, 
		 scrollManager.viewPortW,
		 (l1 - l0 + 1) * lineH);
	//System.out.println ("rpnt srv.");
    }


    boolean scrollTo (int cp) {
	if (!visible) return false;

	if (scrollManager.viewPortW < 2 
	    || scrollManager.viewPortH < 2) return false;

	Point p = getScreenPos (cp);
	p.x -= scrollManager.insets.left;
	p.y -= scrollManager.insets.top;
	boolean scroll = false;

	while (p.x < 0) {
	    scrollManager.xOffset -= scrollManager.viewPortW / 2;
	    p.x += scrollManager.viewPortW / 2;
	    scroll = true;
	}

	while (p.x > scrollManager.viewPortW) {
	    scrollManager.xOffset += scrollManager.viewPortW / 2;
	    p.x -= scrollManager.viewPortW / 2;
	    scroll = true;
	}
    
	while (p.y < 0) { 
	    scrollManager.yOffset -= scrollManager.viewPortH / 2;
	    p.y += scrollManager.viewPortH / 2;
	    scroll = true;
	}

	while (p.y + fontMetrics.getHeight () 
		 > scrollManager.viewPortH) {
	    
	    scrollManager.yOffset += scrollManager.viewPortH / 2;
	    p.y -= scrollManager.viewPortH / 2;
	    scroll = true;
	}

	if (scroll) {
	    if (scrollManager.hBar != null) {
	        if (scrollManager.xOffset 
		    + scrollManager.viewPortW > maxWidth)
		    scrollManager.xOffset = maxWidth - scrollManager.viewPortW;
		if (scrollManager.xOffset < 0) scrollManager.xOffset = 0;
		scrollManager.hBar.currValue = scrollManager.xOffset;
	    }
		
	    if (scrollManager.vBar != null) {
 
		if (scrollManager.yOffset 
		    + scrollManager.viewPortH > maxHeight)
		    scrollManager.yOffset = maxHeight 
			- scrollManager.viewPortH;

		if (scrollManager.yOffset < 0) 
		    scrollManager.yOffset = 0;

		scrollManager.vBar.currValue = scrollManager.yOffset;
	    }
	    repaint ();
	}

	return scroll;
    }


    public void update (Graphics g) {
	paintAll (g);
    }
}

