// GridLayout.java
//
// 2000-09-08 MK Added new Licensetext
// 2000-07-23 Bugfix
// 1999-11-26 SH added removeLayoutComponent for V 0.2     
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

import java.util.Vector;

public class GridLayout implements LayoutManager {
    
  int rows;
  int cols;
  int hGap;
  int vGap;

  public void addLayoutComponent (String where, Component component) {}

  public GridLayout (int rows, int cols) {
	this (rows, cols, 1, 1);
  }


  public GridLayout (int rows, int cols, int hGap, int vGap) {
	this.rows = rows;
	this.cols = cols;
	this.hGap = hGap;
	this.vGap = vGap;

	if (rows == 0 && cols == 0) 
	    throw new RuntimeException 
		("rows and cols must not both be 0");
  }
    
  public Dimension minimumLayoutSize (Container parent) {
	int cw = 0;
	int ch = 0;
	int count = parent.childCount;

	for (int i = 0; i < count; i++) {

	    Component c = parent.children [i];
	    Dimension d = c.getMinimumSize ();

	    if (d.width > cw) cw = d.width;
	    if (d.height > ch) ch = d.height;
	}

	int rowCount = (rows == 0) ? ((count + cols - 1) / cols) : rows;
	int colCount = (rowCount * cols < count) 
	    ? ((count + rows - 1) / rows) : cols;

	Insets insets = parent.getInsets ();

	return new Dimension 
	    (colCount * (cw + hGap) - hGap 
	     + insets.left + insets.right, 
	     rowCount * (ch + vGap) - vGap 
	     + insets.top + insets.bottom); 
  }
    

    public void layoutContainer (Container parent) {

	int count = parent.childCount;

	if (count == 0) return;
	
	Insets insets = parent.getInsets ();
	
	int x0 = insets.left;
	int y0 = insets.top;
	
	int w0 = parent.w - x0 - insets.right;
	int h0 = parent.h - y0 - insets.bottom;



	int rowCount = (rows == 0) ? ((count + cols - 1) / cols) : rows;
	int colCount = (rowCount * cols < count) 
	    ? ((count + rows - 1) / rows) : cols;

	int cellW = (w0 + hGap) / colCount;
	int cellH = (h0 + vGap) / rowCount;

	int idx = 0;
	int currY = y0;

	for (int idxY = 0; idxY < rowCount; idxY++) {

	    int currX = x0;
	    
	    for (int idxX = 0; idxX < colCount; idxX++) {
		
		if (idx >= count) return;
		
		Component c = parent.children [idx++];
		
		c.setBounds (currX, currY, cellW - hGap, cellH - vGap);
		
		c.valid = false;
	      
		currX += cellW;
	    }
	    currY += cellH;
	}
  }

    public void removeLayoutComponent (Component component) {}
} 






























