// ScrollPane.java
//
// 2000-09-08 MK Added new Licensetext
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
import de.kawt.impl.Laf;

public class ScrollPane extends Container {
    
    Component child;
    ScrollManager scrollManager;

    public ScrollPane () {
	scrollManager = new ScrollManager 
	    (this, 
	     Laf.laf.getBorderInsets (Laf.BORDER_DOUBLE));
    }


    public void addImpl (Component c, Object constraints, int index) {
	child = c;
	invalidate ();
    
	super.addImpl (c, constraints, index);
    }


    public void paintAll (Graphics g) {

	g.setColor (foreground);

	if (child == null || !visible) return;

	scrollManager.paint (g, hasFocus 
			     ? Laf.BORDER_DOUBLE 
			     : Laf.BORDER_SINGLE);

       	child.x = -scrollManager.xOffset + scrollManager.insets.left;
	child.y = -scrollManager.yOffset + scrollManager.insets.top;
	
	//Graphics g2 = g.create (child.x, child.y, child.w, child.h);

	child.update (g);
    }
    
    public void update (Graphics g) {
	g.setColor (background);
	g.fillRect (0, 0, w, h);
	paintAll (g);
    }

    public synchronized void doLayout () {  

	if (child == null) return;

	Dimension d = child.getPreferredSize ();
	scrollManager.doLayout (d.width, d.height);

	if (d.width < scrollManager.viewPortW) 
	    d.width = scrollManager.viewPortW;

	if (d.height < scrollManager.viewPortH) 
	    d.height = scrollManager.viewPortH;

	child.setSize (d);	
	child.doLayout ();

	if (scrollManager.xOffset > d.width - scrollManager.viewPortW) 
	    scrollManager.xOffset = d.width - scrollManager.viewPortW;
	else if (scrollManager.yOffset > d.height - scrollManager.viewPortH)
	    scrollManager.yOffset = d.height - scrollManager.viewPortH;
    }

    

    public Component getComponentAt (int px, int py) {
	//System.out.println ("fca "+px+", "+py);
	Component target = scrollManager.findComponentAt (px, py);
	if (target != this) return target;
	return child.contains (px-child.x, py-child.y) ? child : null;
    }

    
    /** Returns a minimum size of 50x50 pixel (1/2 AWT def. Value) */

    public Dimension getMinimumSize () {
	return new Dimension (50, 50);
    }


    public Dimension getViewportSize () {
	return new Dimension (scrollManager.viewPortW, 
			      scrollManager.viewPortH);
    }

    public Point getScrollPosition () {
	
	return new Point 
	    (scrollManager.hBar != null ? scrollManager.hBar.currValue : 0,
	     scrollManager.vBar != null ? scrollManager.vBar.currValue : 0);
    }
}
