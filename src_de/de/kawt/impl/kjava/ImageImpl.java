// ImageImpl.java
//
// 2000-10-05 SH Initial implementation
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

/** currently, only wrapper for palm image with hidden constructor
 *  for kAWT internal usage only.
 *
 */  

import java.awt.*;
import java.awt.image.*;
import com.sun.kjava.Bitmap;


public class ImageImpl extends Image {

    static final int WIDTH = 0;
    static final int HEIGHT = 1;
 
    Bitmap bitmap;
    short [] data;
    int lineLength; // in WORDS!
    int depth;
 
    static final int HEADER_SIZE = 8;
 

    /** builds a palm native image from the given data */
 
    ImageImpl (byte [] bytes) {
	int cnt = bytes.length / 2;
	int size = (bytes.length + 1) / 2;
	data = new short [size];
	int bi = 0;
	
	for (int i = 0; i < cnt; i++) {
	    data [i] = (short) (((((int) bytes [bi]) & 0x0ff) << 8)
				 | (((int) bytes [bi+1]) & 0x0ff));
	    bi += 2;
	}
	
	if (size > cnt)
	    data [cnt] = (short) ((((int) bytes [bi]) & 0x0ff) << 8);

	bitmap = new Bitmap (data);
    }


    ImageImpl (int w, int h, int depth) {
 
 	this.depth = depth;
 	lineLength = (w * depth + 15) / 16;
 
	// try some defrag?
	//Runtime.getRuntime ().gc ();

 	data = new short [HEADER_SIZE + h * lineLength];
 
 	data [0] = (short) w;
 	data [1] = (short) h;
 	data [2] = (short) (lineLength * 2);
 	data [3] = 0;
 	data [4] = (short) ((depth << 8) + 1);
 	data [5] = 0;
 	data [6] = 0;
 	data [7] = 0;
 
 	bitmap = new Bitmap (data);
    }
 

    /** do not call for native bitmaps since linelength and depth are not set! */
 
    void setPixels (int x, int y, int w, int h,
 		    ColorModel model, byte[] pixels,
 		    int off, int scansize) {
	
 	int dst = HEADER_SIZE + lineLength * y;
 
 	// ignore x, model, off, scansize;
 	int i = 0;
 
 	if (depth == 1) {
 	    int val;
 	    while (i < w) {
 		val = 0;
 		for (int j = 15; j >= 0; j--)
 		    val |= pixels [i++] << j;
 		data [dst++] = (short) val;
 	    }
 	}
 	else if (depth == 4) {
 	    while (i < w) {
 		data [dst++] = (short)
 		    ((pixels [i] << 12)
 		     + (pixels [i+1] << 8)
 		     + (pixels [i+2] << 4)
 		     + (pixels [i+3]));
 
 		i += 4;
 	    }
 	}
 	else if (depth == 8) {
 	    while(i < w) {
 		data [dst++] = (short)
 		    (((((int) pixels [i]) & 255) << 8)
 		     + ((((int) pixels [i+1]) & 255)));
 
 		i += 2;
 	    }
 	}
 	else throw new RuntimeException ("depth currently unsupported!");
     }
 
 
     public int getWidth (ImageObserver observer) {
 	return data [WIDTH];
     }
 
 
     public int getHeight (ImageObserver observer) {
 	return data [HEIGHT];
     }
}

