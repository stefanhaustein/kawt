package de.kawt.impl.kjava;

import java.awt.*;
import java.io.*;
import de.kawt.impl.*;


public class ToolkitImpl extends KawtToolkit {

    static int scrW = 160;
    static int scrH = 160;

    // static, damit aus dem constr. auf die eventqueue zugegr wd. kann (?)
    public static SpotletImpl spotlet = new de.kawt.impl.kjava.SpotletImpl ();


    public ToolkitImpl () {
	adjustScreenSize ();
    }

    public static void adjustScreenSize () {
	if (System.getProperty ("kawt.screenwidth") != null) {
	    scrW = Integer.parseInt (System.getProperty ("kawt.screenwidth"));
	    scrH = Integer.parseInt (System.getProperty ("kawt.screenheight"));
	}
    }


    public void beep () {
	com.sun.kjava.Graphics.playSound
	    (com.sun.kjava.Graphics.SOUND_ALARM);
    }

    /** Creates a gif image if the gif image signature
	is found in the first bytes. Otherwise, a 
	palm native image is created */

    public Image createImage (byte [] data) {

	if (data [0] == 'G' && data [1] == 'I' && data [2] == 'F') {

	    de.kawt.impl.kjava.GifLoader loader = 
		new de.kawt.impl.kjava.GifLoader (data);

	    return loader.load ();
	}
	else return new ImageImpl (data);
    }

    public Image createImage (int w, int h) {
	throw new RuntimeException ("Not yet implemented");
    }


    public FontMetrics getFontMetrics (Font font) {
	return new FontMetricsImpl (font);
    }

    public Dimension getScreenSize () {
	return new Dimension (scrW, scrH);
    }
}


