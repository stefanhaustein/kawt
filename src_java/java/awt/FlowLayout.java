// FlowLayout.java
//
// 2000-09-08 MK Added new Licensetext
// 1999-11-26 SH added removeLayoutComponent for 0.2     
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

public class FlowLayout implements LayoutManager {

    public static final int LEFT = 0;
    public static final int CENTER = 1;
    public static final int RIGHT = 2;

    static LayoutManager defaultLayout = new FlowLayout ();

    int orientation = CENTER;

    public FlowLayout () {
    }

    public FlowLayout (int orientation) {
	this.orientation = orientation;
    } 

    public void addLayoutComponent (String where, Component component) {}

    public void layoutContainer (Container parent) {

	Insets insets = parent.getInsets ();

	int x0 = insets.left;
	int y0 = insets.top;
	int maxW = parent.w - insets.right - x0;
	int maxH = 0;

	int i0 = 0;
	for (int i = 0; i < parent.childCount; i++) {
	    
	    Component c = parent.children [i];
	    Dimension d = c.getMinimumSize ();

	    if (d.width + 2 > maxW) d.width = maxW - 2; 
	    
	    c.setBounds (c.x, c.y, d.width, d.height);
	    c.valid = false;
	    
	    // newline?

	    if (x0 != 0 && x0 + d.width + 2 > maxW) {
		shift (parent, i0, i, maxW-x0);
		i0 = i;
		x0 = 0;
		y0 += maxH + 1;
		maxH = 0;
	    }
	    
	    c.x = x0;
	    c.y = y0;
	    x0 = x0 + d.width + 2;
	    
	    if (d.height > maxH) 
		maxH = d.height;
	}
	
	shift (parent, i0, parent.childCount, maxW-x0);
    }
    
    void shift (Container parent, int i0, int i1, int delta) {
	if (orientation == LEFT) return;
	if (orientation == CENTER) delta >>= 1;
	for (int i = i0; i < i1; i++) {
	    Component c = parent.children [i];
	    c.setBounds 
		(c.x + delta, c.y, c.w, c.h);
	}
    }

  public Dimension minimumLayoutSize (Container parent) {
	int minW = 0;
	int minH = 0;

	for (int i = 0; i < parent.childCount; i++) {
	    Dimension d = parent.children [i].getMinimumSize ();
	    
	    minW = minW + d.width + 2;
	    
	    if (d.height > minH) 
		minH = d.height;
	}
	
	Insets insets = parent.getInsets ();

	return new Dimension 
	    (minW + insets.left + insets.right, 
	     minH + insets.top + insets.bottom);
  }


    public void removeLayoutComponent (Component c) {}

}
