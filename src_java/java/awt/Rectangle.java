// Rectangle.java
//
// 2000-29-09 SH removed intersection, added intersects
// 2000-09-08 MK Added new Licensetext
// 2000-08-06 SH contains added
// 2000-04-09 SH Intitial Version
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


public class Rectangle {

    public int x;
    public int y;
    public int width;
    public int height;


    public Rectangle (int x, int y, int width, int height) {
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
    }

    
    public void add (Rectangle r) {
	
	int x2 = Math.max (r.x + r.width, x + width);
	int y2 = Math.max (r.y + r.height, y + height);

	if (r.x < x) x = r.x;
	if (r.y < y) y = r.y;

	width = x2 - x;
	height = y2 - y;
    }

    
    public boolean contains (Point p) {
	return contains (p.x, p.y);
    }


    public boolean contains (int px, int py) {
	return px >= x && px < x + width 
	    && py >= y && py < y + height;
    }

    public boolean intersects (Rectangle r) {
	return ((r.x <= x + width) && (r.x + r.width >= x) 
		&& (r.y <= y + height) && (r.y + r.height >= y));
    }	


    /*
    public Rectangle intersection (Rectangle r) {

	Rectangle result = new Rectangle (x, y, width, height);

	if (r.x > result.x) {
	    result.width -= r.x - result.x;
	    result.x = r.x;
	}

	if (r.y > result.y) {
	    result.height -= r.y - result.y;
	    result.y = r.y;
	}

	if (r.x + r.width < result.x + result.width)
	    result.width = r.x + r.width - result.x;

	if (r.y + r.height < result.y + result.height)
	    result.height = r.y + r.height - result.y;

	return result;
    } 
    */
}
