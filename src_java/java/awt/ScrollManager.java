// ScrollManager.java
//
// 2000-10-12 SH Border drawing by Laf added 
// 2000-09-08 MK Added new Licensetext
// 2000-06-22 SH implemented some border-related fixes 
// 2000-06-18 SH Created in order to be able to remove redundant
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

import de.kawt.impl.Laf;


class ScrollManager {

    Component component;
    Rectangle clipBuf = new Rectangle (0,0,0,0);
    Insets insets;

    Scrollbar hBar;
    Scrollbar vBar;

    int viewPortW;
    int viewPortH;

    int xOffset;
    int yOffset;

    boolean clear;

    ScrollManager (Component component, Insets insets) {
	this.component = component;
	this.insets = insets;
    }

    /** after paint, g, xOffset and yOffset are set properly */

    Rectangle paint (Graphics g, int border) {

	g.getClipBounds (clipBuf);

	if (clipBuf.x < insets.left || clipBuf.y < insets.top
	    || clipBuf.width + clipBuf.x > insets.left + viewPortW 
	    || clipBuf.height + clipBuf.y > insets.top + viewPortH) {
	
	    Laf.laf.drawBorder (g, component.background, 
			    component.w, component.h, border);

	    if (vBar != null) {
		Graphics g2 = g.create (vBar.x, vBar.y, vBar.w, vBar.h);
		if (clear) 
		    g2.clearRect (0, 0, vBar.w, vBar.h);
		
		vBar.paint (g2);//.create (vBar.x, vBar.y, vBar.w, vBar.h));
		yOffset = vBar.currValue;
	    }
	    else yOffset = 0; // (viewPortH - child.h) / 2;
	    
	    if (hBar != null) { 
		Graphics g2 = g.create (hBar.x, hBar.y, hBar.w, hBar.h);
		if (clear)
		    g2.clearRect (0, 0, hBar.w, hBar.h);
		
		hBar.paint (g2); //.create (hBar.x, hBar.y, hBar.w, hBar.h));
		xOffset = hBar.currValue;
	    }
	    else xOffset = 0; //(viewPortW - child.w) / 2;

	    clear = false;
	    g.clipRect (insets.left, insets.top, viewPortW, viewPortH);
	}

	g.translate (insets.left - xOffset, 
		     insets.top - yOffset);

	clipBuf.x -= insets.left - xOffset;
	clipBuf.y -= insets.right - yOffset;
	//g.setColor (Color.black);
	//	Graphics g2 = g.create (child.x, child.y, child.w, child.h);

	return clipBuf;
    }


    /** creates scrollbars if necessary */
    
    void doLayout (int virtualW, int virtualH) {

	int borderW = insets.left + insets.right;
	int borderH = insets.top + insets.bottom;

	viewPortW = component.w - borderW;
	viewPortH = component.h - borderH;

	// two iterations!
	

	for (int i = 0; i < 2; i++) {
	
	    if (virtualH > viewPortH) {
		if (vBar == null) {
		    clear = true;
		    vBar = new Scrollbar (Scrollbar.VERTICAL);
		    vBar.parent = component;
		    vBar.repaintParent = true;
		    vBar.setSize (vBar.getMinimumSize ());
		}
		vBar.maxValue = virtualH;
		vBar.currVisible = viewPortH;
		vBar.blockIncrement = viewPortH;
		vBar.unitIncrement = component.fontMetrics.getHeight ();
		
		vBar.x = component.w - vBar.w - insets.right;
		vBar.y = insets.top;
		vBar.h = component.h - borderH;
		
		viewPortW = component.w - vBar.w  - borderW;

		if (vBar.currValue > vBar.maxValue-vBar.currVisible) 
		    vBar.currValue = vBar.maxValue-vBar.currVisible;
	    }
	    else {
		clear |= vBar != null;
		vBar = null;
	    }

	    if (virtualW > viewPortW) {
		if (hBar == null) {
		    clear = true;
		    hBar = new Scrollbar (hBar.HORIZONTAL);
		    hBar.parent = component;
		    hBar.repaintParent = true;
		    hBar.setSize (hBar.getMinimumSize ());
		}
		hBar.maxValue = virtualW;
		hBar.currVisible = viewPortW;
		hBar.blockIncrement = viewPortW;
		hBar.unitIncrement = component.fontMetrics.getHeight ();
	    
		hBar.y = component.h - hBar.h - insets.top;
		hBar.x = insets.left;
		hBar.w = viewPortW;
	    
		viewPortH = component.h - hBar.h - borderH;

		if (hBar.currValue > hBar.maxValue-hBar.currVisible) 
		    hBar.currValue = hBar.maxValue-hBar.currVisible;
	    }
	    else {
		clear |= hBar != null;
		hBar = null;
	    }
	}

	component.valid = true;
    }


    /** returns a scrollbar, null or the owner. By returning the
	owner, handling is returned to the owner since calling 
	componet.findComponentAt here would result in an endless loop! */

    Component findComponentAt (int px, int py) {
	
	if (!component.visible || !component.contains (px, py)) return null;
	if (vBar != null && px > viewPortW) return vBar;
	if (hBar != null && py > viewPortH) return hBar;

	return component;
    }
   
}
