// Graphics.java
//
// 2000-10-06 SH split into abstract class and implementations
// 2000-09-28 SH Synchronization removed since kAWT is not thread
//               safe anyway. Removed complex clipping suppport,
//               too.
// 2000-09-08 MK Added new Licensetext
// 2000-08-25 SH DrawOval ifdef for MIDP added, some MIDP offset fixes
// 2000-08-06 SH Changes for MIDP compatibility 
// 2000-06-25 Draw*Rect changes for AWT compatibility 
//            (1 pixel larger than one would expect)
// 2000-05-26 Color support added
// 2000-05-20 SH hitClip added
// 2000-05-01 SH create from graphics bug fixed
// 2000-04-09 SH Preparation for improved repaint: multiple clip rects 
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
import java.awt.image.ImageObserver;

public abstract class Graphics {

    public abstract Graphics create ();
        
    public Graphics create (int x, int y, int width, int height) {
	Graphics g = create ();
	g.translate (x, y);
	g.clipRect (0, 0, width, height);
	return g;
    }
    

    public  Rectangle getClipBounds () {
	return getClipBounds (new Rectangle (0,0,0,0));
    }

    public abstract Rectangle getClipBounds (Rectangle r);

    public abstract void clipRect (int x, int y, int w, int h);


    public abstract void setClip (int x, int y, int w, int h);
    
    public abstract void clearRect (int x, int y, int w, int h);
    
    public abstract boolean drawImage (Image image, int x, int y, 
				       ImageObserver observer);
	

    public abstract void setPaintMode ();
    public abstract void setXORMode (Color c);

    public void dispose () {
    }

    
    public abstract void drawLine (int x1, int y1, int x2, int y2);


    public abstract void drawPolyline (int[] xPoints, int[] yPoints, 
				       int nPoints);

    

    public abstract void drawRoundRect (int x, int y, int width, int height, 
					int arcWidth, int arcHeight);

    public abstract void drawString (String text, int x, int y);



    /** Since there is no drawOval in com.sun.kjava.Graphics, 
	this method draw a roundRect */

    public abstract void drawOval (int x, int y, int w, int h);


    public abstract void drawRect (int x,  int y, int width, int height);
    
    public abstract void fillRect (int x, int y, int width, int height);
    
  
    public abstract void fillRoundRect (int x, int y, 
					int width, int height, 
					int arcWidth, int arcHeight);
    
    public abstract Font getFont ();
    
    public abstract Color getColor ();

    public abstract boolean hitClip (int x, int y, int w, int h);
    

    public abstract FontMetrics getFontMetrics ();

    public abstract FontMetrics getFontMetrics (Font font);

    public abstract void setColor (Color c);
    
    public abstract void setFont (Font font);
    
    public abstract void translate (int x, int y);
}






