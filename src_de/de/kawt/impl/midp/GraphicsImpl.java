// kAWT
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


package de.kawt.impl.midp;

import java.awt.*;
import java.util.Vector;
import java.awt.image.ImageObserver;

public class GraphicsImpl extends Graphics {

    static Graphics current;
    static boolean dirty;
    javax.microedition.lcdui.Graphics graphics = 
	ToolkitImpl.canvas.graphics;

    int orgX;
    int orgY;

    Color color = Color.black;
    
    FontMetricsImpl fontMetrics = FontMetricsImpl.defaultFontMetrics;
    int clipX;
    int clipY;
    int clipW;
    int clipH;

    static int minX;
    static int maxX;
    static int minY;
    static int maxY;
    static boolean reclip;

    public GraphicsImpl () {
	clipW = ToolkitImpl.canvas.getWidth ();
	clipH = ToolkitImpl.canvas.getHeight ();
    }


    public Graphics create () {
	GraphicsImpl g = new GraphicsImpl ();

	g.orgX = orgX;
	g.orgY = orgY;

	g.clipX = clipX;
	g.clipY = clipY;
	g.clipW = clipW;
	g.clipH = clipH;

	g.color = color;

	return g;
    } 


    void checkContext () {
	if (!dirty) {
	    dirty = true;
	    minX = clipX;
	    minY = clipY;
	    maxX = clipX + clipW;
	    maxY = clipY + clipH;
	}

	if (current != this) {
	    if (clipX < minX) minX = clipX;
	    if (clipY < minY) minY = clipY;
	    if (clipX + clipW > maxX) maxX = clipX + clipW;
	    if (clipY + clipH > maxY) maxY = clipY + clipH;

	    graphics.setClip (clipX, clipY, clipW, clipH);
	    graphics.setColor (color.getRGB ());
	    graphics.setFont (fontMetrics.midpFont);
	    current = this;
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
	graphics.setColor (0x0ffffff);
	graphics.fillRect (orgX+x, orgY+y, w, h);
	graphics.setColor (color.getRGB ());
    }
    
    
    public boolean drawImage (Image image, int x, int y, 
			   ImageObserver observer) {
	
	checkContext ();
	graphics.drawImage
	    (((ImageImpl) image).image, x + orgX, y+orgY,
	     javax.microedition.lcdui.Graphics.TOP
	     | javax.microedition.lcdui.Graphics.LEFT);
	    
	return true;
    }


    public void dispose () {
    }

    
    public void drawLine (int x1, int y1, int x2, int y2) {

	checkContext ();
	graphics.drawLine
	    (x1+orgX, y1+orgY,
	     x2+orgX, y2+orgY);
	
    }


    public void drawPolyline (int[] xPoints, int[] yPoints, 
			      int nPoints) {

	checkContext ();

	for (int i = 0; i < nPoints - 1; i++) {
 		    graphics.drawLine
			(xPoints[i] + orgX,
 			 yPoints[i] + orgY,
  			 xPoints[i+1] + orgX,
 			 yPoints[i+1] + orgY);

	}
    }
    

    public void drawRoundRect (int x, int y, int width, int height, 
	               	         int arcWidth, int arcHeight) {

	checkContext ();
 		graphics.drawRoundRect
 		    (orgX + x, orgY + y,
 		     width, height, arcWidth, arcHeight);

    }


    public void drawString (String text, int x, int y) {

	checkContext ();
		
	// font pos = baseline in AWT!!!!

	//System.out.println ("text: "+text + " x: "+(orgX+x) + " y: "+(orgY+y));

	graphics.drawString
	    (text, orgX + x, orgY + y,
	     javax.microedition.lcdui.Graphics.BASELINE
	     | javax.microedition.lcdui.Graphics.LEFT);
    }



    public void drawOval (int x, int y, int w, int h) {
	checkContext ();
	graphics.drawArc (x + orgX, y + orgY, w, h, 0, 360);
    }


    public void drawRect (int x,  int y, int width, int height) {
	checkContext ();
	graphics.drawRect (x + orgX, y + orgY, width, height);
    }

    
    public void fillRect (int x, int y, int width, int height) {
	checkContext ();
	graphics.fillRect (orgX +x, orgY + y, width, height);

    }
    
  
    public void fillRoundRect (int x, int y, 
			       int width, int height, 
			       int arcWidth, int arcHeight) {

	checkContext ();
	graphics.fillRoundRect
	    (orgX +x, orgY + y,
	     width, height, arcWidth, arcHeight);
    }
    
    public Color getColor () {
	return color;
    }


    public Font getFont () {
	return fontMetrics.getFont ();
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


    public boolean hitClip (int x, int y, int w, int h) {
      
	x += orgX;
	y += orgY;
	
	return ((clipX <= x + w) && (clipX + clipW >= x) 
		&& (clipY <= y + h) && (clipY + clipH >= y));
    }
    

    public FontMetrics getFontMetrics () {
	return fontMetrics;
    } 


    public FontMetrics getFontMetrics (Font font) {
	return new FontMetricsImpl (font);
    } 


    public void setColor (Color c) {
 	color = c;
	graphics.setColor (color.getRGB ());
    }
    

    public void setFont (Font font) {
	fontMetrics = new FontMetricsImpl (font);
 	graphics.setFont (fontMetrics.midpFont);
    }
    

    public void setPaintMode () {
    }


    public void setXORMode (Color c2) {
    }
	

    public void translate (int x, int y) {
	orgX += x;
	orgY += y;
    }
}






