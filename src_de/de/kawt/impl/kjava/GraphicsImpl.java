// Graphics.java
//
// 2000-10-06 SH dividet into abstract graphics and implementation
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


package de.kawt.impl.kjava;

import java.awt.*;
import java.util.Vector;
import java.awt.image.ImageObserver;


public class GraphicsImpl extends Graphics {

    static GraphicsImpl current;
    static boolean monochrome = System.getProperty ("kawt.colordepth") == null 
	|| Integer.parseInt (System.getProperty ("kawt.colordepth")) == 1;

    static {
	LafImpl.monochrome = monochrome;
    }

    static boolean colorKvm = System.getProperty ("kawt.colordepth") != null;

    int nativeDrawMode = com.sun.kjava.Graphics.PLAIN;

    int orgX;
    int orgY;

    Color color = Color.black;
    boolean xorMode = false;
    FontMetrics fontMetrics = FontMetricsImpl.defaultFontMetrics; 
    int clipX;
    int clipY;
    int clipW;
    int clipH;

    public GraphicsImpl () {
	clipW = ToolkitImpl.scrW;
	clipH = ToolkitImpl.scrH;
    }


    public Graphics create () {
	GraphicsImpl g = new GraphicsImpl ();

	g.orgX = orgX;
	g.orgY = orgY;
	g.xorMode = xorMode;

	g.clipX = clipX;
	g.clipY = clipY;
	g.clipW = clipW;
	g.clipH = clipH;

	g.color = color;

	g.nativeDrawMode = nativeDrawMode;

	return g;
    }
    
    
    public void checkContext () {

	if (current != this) {
	    com.sun.kjava.Graphics.setDrawRegion
		(clipX, clipY, clipW, clipH);
	}
    }
	

    public void clipRect (int x, int y, int w, int h) {
	current = null; // force setDrawRegion

	x += orgX;
	y += orgY;

	if (x > clipX) {
	    clipW -= x - clipX;
	    clipX = x;
	}

	if (y > clipY) {
	    clipH -= y - clipY;
	    clipY = y;
	}

	if (x + w < clipX + clipW)
	    clipW = x + w - clipX;

	if (y + h < clipY + clipH)
	    clipH = y + h - clipY;
    }


    public void setClip (int x, int y, int w, int h) {
	current = null;
	clipX = x + orgX;
	clipY = y + orgY;
	clipW = w;
	clipH = h;
    }
    
    
    public void clearRect (int x, int y, int w, int h) {
	checkContext ();

	com.sun.kjava.Graphics.drawRectangle
	    (orgX+x, orgY+y, w, h,
	     com.sun.kjava.Graphics.ERASE, 0);
    }
    
    
    public boolean drawImage (Image image, int x, int y, 
			   ImageObserver observer) {
	
	checkContext ();

	com.sun.kjava.Graphics.drawBitmap
	    (x + orgX, y+orgY, ((de.kawt.impl.kjava.ImageImpl) image).bitmap);

	return true;
    }


    
    public void drawLine (int x1, int y1, int x2, int y2) {

	checkContext ();

	com.sun.kjava.Graphics.drawLine
	    (x1+orgX, y1+orgY,
	     x2+orgX, y2+orgY, nativeDrawMode);
    }


    public void drawPolyline (int[] xPoints, int[] yPoints, 
			      int nPoints) {

	checkContext ();

	for (int i = 0; i < nPoints - 1; i++) {
	    com.sun.kjava.Graphics.drawLine
		(xPoints[i] + orgX,
		 yPoints[i] + orgY,
		 xPoints[i+1] + orgX,
		 yPoints[i+1] + orgY,
		 nativeDrawMode);
	}
    }
    

    public void drawRoundRect (int x, int y, int width, int height, 
	               	         int arcWidth, int arcHeight) {

	int arc =  (arcWidth+arcHeight) / 2;

	if (arc > 0x0fff) arc = 0x0fff;

	checkContext ();

	com.sun.kjava.Graphics.drawBorder
	    (orgX + x + 1, orgY + y + 1,
	     width - 1, height - 1,
	     nativeDrawMode, arc << 8 | 1);
    }


    public void drawString (String text, int x, int y) {

	checkContext ();
		
	// font pos = baseline in AWT!!!!

	y += orgY - fontMetrics.getAscent ();
	x += orgX;

	if (!fontMetrics.getFont ().isBold () 
	    || (!colorKvm && nativeDrawMode != com.sun.kjava.Graphics.ERASE))
	    com.sun.kjava.Graphics.drawString
		(text, x, y, nativeDrawMode);
	else {
	    for (int i = 0; i < text.length (); i++) {
		String cc = ""+text.charAt (i);
		com.sun.kjava.Graphics.drawString
		    (cc, x++, y, nativeDrawMode);
		com.sun.kjava.Graphics.drawString
		    (cc, x, y, nativeDrawMode);
		x += com.sun.kjava.Graphics.getWidth (cc);
	    }
	}
    }


    /** Since there is no drawOval in com.sun.kjava.Graphics, 
	this method draw a roundRect */

    public void drawOval (int x, int y, int w, int h) {

	int d = (w < h ? w : h) / 2;
	drawRoundRect (x, y, w, h, d, d);
     }


    public void drawRect (int x,  int y, int width, int height) {
	checkContext ();

	com.sun.kjava.Graphics.drawBorder
	    (orgX + x + 1, orgY + y+1,
	     width - 1, height - 1, nativeDrawMode, 1);
    }

    
    public void fillRect (int x, int y, int width, int height) {
	checkContext ();

	com.sun.kjava.Graphics.drawRectangle
	    (orgX + x, orgY + y,
	     width, height, nativeDrawMode, 0);
    }
    
  
    public void fillRoundRect (int x, int y, 
			       int width, int height, 
			       int arcWidth, int arcHeight) {
	checkContext ();

	com.sun.kjava.Graphics.drawRectangle
	    (orgX + x, orgY + y,
	     width, height, nativeDrawMode,
	     (arcWidth+arcHeight) >> 1);
    }
    

    public Color getColor () {
	return color;
    }


    public Font getFont () {
	return fontMetrics.getFont ();
    }
    
    
    public boolean hitClip (int x, int y, int w, int h) {
      
	x += orgX;
	y += orgY;
	
	return ((clipX <= x + w) && (clipX + clipW >= x) 
		&& (clipY <= y + h) && (clipY + clipH >= y));
    }
    

    public Rectangle getClipBounds () {
	return new Rectangle (clipX - orgX, clipY - orgY, clipW, clipH);
    }

    public Rectangle getClipBounds (Rectangle r) {
	r.x = clipX - orgX;
	r.y = clipY - orgY;
	r.width = clipW;
	r.height = clipH;
	return r;
    }



    public FontMetrics getFontMetrics () {
	return fontMetrics;
    } 


    public FontMetrics getFontMetrics (Font font) {
	return new FontMetricsImpl (font);
    } 


    public void setColor (Color c) {
 	color = c;

	if (xorMode) return;

	if (monochrome) {
	    int rgb = c.getRGB ();

	    int sum = ((rgb & 0x0ff0000) >> 16)
		+ ((rgb & 0x0ff00) >> 8)
		+ (rgb & 0x0ff);

	    if (sum > 256)
		if (sum > 512) nativeDrawMode =
				   com.sun.kjava.Graphics.ERASE;

		else nativeDrawMode = com.sun.kjava.Graphics.GRAY;
	    else nativeDrawMode = com.sun.kjava.Graphics.PLAIN;
	}
	else nativeDrawMode = (c.getRGB () << 8) + 5;
    }
    

    public void setFont (Font font) {
	fontMetrics = new FontMetricsImpl (font);
    }
    

    public void setPaintMode () {
	xorMode = false;
	setColor (color);
    }


    public void setXORMode (Color c2) {
	xorMode = true;
	nativeDrawMode = com.sun.kjava.Graphics.INVERT;
    }
	

    public void translate (int x, int y) {
	orgX += x;
	orgY += y;
    }
}






