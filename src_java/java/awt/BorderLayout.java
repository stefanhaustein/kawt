// BorderLayout.java
//
// 2000-09-30 SH removeLayoutComponent fixed
// 2000-09-08 MK Added new Licensetext
// 1999-11-26 SH added removeayoutComponent for 0.2      
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

public class BorderLayout implements LayoutManager {

    public static final String CENTER = "Center";
    public static final String SOUTH = "South";
    public static final String EAST = "East";
    public static final String NORTH = "North";
    public static final String WEST = "West";

    Component center;
    Component south;
    Component east;
    Component north;
    Component west;

    public void addLayoutComponent (String where, Component component) {
	if (where == null || where.equals ("Center")) 
	    center = component;

	else if (where.equals ("South")) south = component;
	else if (where.equals ("East")) east = component;
	else if (where.equals ("North")) north = component;
	else if (where.equals ("West")) west = component;
    }


    public void layoutContainer (Container parent) {
	int wWest = -1;
	int wEast = -1;
	int hNorth = -1;
	int hSouth = -1;

	Insets insets = parent.getInsets ();
	    
	int x0 = insets.left;
	int y0 = insets.top;
	int w0 = parent.w - insets.left - insets.right;
	int h0 = parent.h - insets.top - insets.bottom;
	
	if (north != null) {
	    hNorth = north.getMinimumSize ().height;
	    north.setBounds (x0, y0, w0, hNorth);
	}
	
	if (south != null) {
	    hSouth = south.getMinimumSize ().height;
	    south.setBounds 
		(x0,  parent.h - hSouth - insets.bottom, w0, hSouth);
	}
	
	if (west != null) {
	    wWest = west.getMinimumSize ().width;
	    west.setBounds 
		(x0, y0 + hNorth + 1, wWest, h0 - hNorth - hSouth - 2);
	} 
	
	if (east != null) {
	    wEast = east.getMinimumSize ().width;
	    east.setBounds 
		(w0 - wEast, y0 + hNorth + 1, 
		 wEast, h0 - hNorth - hSouth - 2); 
	}
	    
	if (center != null) {
	    center.setBounds 
		(x0 + wWest + 1, y0 + hNorth + 1,
		 w0 - wEast - wWest - 2,
		 h0 - hNorth - hSouth - 2);
	}
    }
	

    public Dimension minimumLayoutSize (Container parent) {
	int wMin = 0;
	int hMin = 0;
	
	if (west != null) {
	    Dimension d = west.getMinimumSize ();
	    wMin += d.width + 1;
	    hMin = d.height;
	} 
	if (east != null) { 
	    Dimension d = east.getMinimumSize ();
	    wMin += d.width + 1;
	    if (hMin < d.height) hMin = d.height;
	}
	
	if (center != null) {
	    Dimension d = center.getMinimumSize ();
	    wMin += d.width;
	    if (hMin < d.height) hMin = d.height;
	}
	    
	if (north != null) {
	    Dimension d = north.getMinimumSize ();
	    hMin += d.height + 1;
	    if (wMin < d.width) wMin = d.width;
	}
	if (south != null) {
	    Dimension d = south.getMinimumSize ();
	    hMin += d.height + 1;
	    if (wMin < d.width) wMin = d.width;
	}

	Insets insets = parent.getInsets ();

	return new Dimension 
	    (wMin + insets.left + insets.right, 
	     hMin + insets.top + insets.bottom); 
    }
    
    public void removeLayoutComponent (Component component) {
	if (component == south) south = null;
	else if (component == east) east = null;
	else if (component == north) north = null;
	else if (component == west) west = null;
	else if (component == center) center = null;
    } 
}



