// Color.java 0996
//
// 2000-09-08 MK Added new Licensetext
// 2000-06-01 MK added AWT color constants
// 2000-05-22 MK added red, green and blue for
//            AWT compatibility
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

public class Color {
    
    public final static Color white 	= new Color (0x0ffffff);
    
    /** @deprecated */
    public final static Color lightGray = new Color (0x0c0c0c0);
    /** @deprecated */
    public final static Color gray 	= new Color (0x0808080);
    /** @deprecated */
    public final static Color darkGray 	= new Color (0x0404040);
    /** @deprecated */
    public final static Color black 	= new Color (0x0000000);
    /** @deprecated */
    public final static Color red 	= new Color (0x0ff0000);
    /** @deprecated */
    public final static Color pink 	= new Color (0x0ffafaf);
    /** @deprecated */
    public final static Color orange 	= new Color (0x0ffc800);
    /** @deprecated */
    public final static Color yellow 	= new Color (0x0ffff00);
    /** @deprecated */
    public final static Color green 	= new Color (0x000ff00);
    /** @deprecated */
    public final static Color magenta	= new Color (0x0ff00ff);
    /** @deprecated */
    public final static Color cyan 	= new Color (0x000ffff);
    /** @deprecated */
    public final static Color blue 	= new Color (0x00000ff);
   
    int rgb;
    
    /** Intitalizes the new color object with the given RGB value */

    public Color (int rgb) {
	this.rgb = rgb;
    }

    /** Intitalizes the new color object with the given R, G, and B values */

    public Color(int r, int g, int b) {
	rgb = (r<<16) + (g<<8) + (b);
    }
    
    
    /** Returns the blue component of this color object. */

    public int getBlue () {
	return rgb & 255;
    }

    /** Returns the green component of this color object. */

    public int getGreen () {
	return (rgb >> 8) & 255;
    }

    /** Returns the red component of this color object. */
    
    public int getRed () {
	return (rgb >> 16) & 255;
    }
    

    /** Returns the RGB value of this color object. */

    public int getRGB () {
	return rgb;
    }




    public boolean equals (Object c2) {
	if (!(c2 instanceof Color)) return false;
	return rgb == ((Color) c2).rgb;
    }
}	



