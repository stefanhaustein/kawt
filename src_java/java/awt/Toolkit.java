// Toolkit.java
//
// 2000-10-06 SH Splitted into abstract and implementation classes
// 2000-09-08 MK Added new Licensetext
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

import java.io.*;
import java.util.*;
import java.awt.image.ColorModel;
import de.kawt.impl.*;

public abstract class Toolkit {

    // some package-internal constants / shortcuts 


    static int pixelBits = System.getProperty ("kawt.colordepth") == null 
        ? 1 : Integer.parseInt (System.getProperty ("kawt.colordepth"));
    static boolean colorKvm = System.getProperty ("kawt.colordepth") != null;

    //   static int scrW = 160;
    //    static int scrH = 160;

    static EventQueue eventQueue = new EventQueue ();
    static KawtThread kawtThread = new KawtThread ();
    
    static Object kawtLock = new Object ();


    static String platform = System.getProperty  ("microedition.platform");
    static String classbase = initclassbase ();

    static Class graphicsImpl = null;
  
    // last call, others must have been init.ed already!
    static KawtToolkit defaultToolkit = init ();  

    static FontMetrics defaultFontMetrics = 
        defaultToolkit.getFontMetrics (new Font ("plain", 8, 0)); 
        

    static String initclassbase () {

	// don't ask me why this shortcut is necessary....!
	//System.out.println ("icb0:"+platform);

	if ("Jbed".equals(platform))
	    //return "de.kawt.impl.jbed";
	return "de.kawt.impl.kjava";

	if ("palm".equals (platform))
	    return "de.kawt.impl.kjava";
	
	
	if (System.getProperty ("de.kawt.classbase") != null) 
	    return System.getProperty ("de.kawt.classbase");

	try {
            Class.forName ("com.sun.kjava.Graphics");
	    if (platform == null) platform = "palm";
            return "de.kawt.impl.kjava";
        }
        catch (Exception e) {}
        try {
            Class.forName ("javax.microedition.lcdui.Graphics");
	    if (platform == null) platform = "midp";
            return "de.kawt.impl.midp";
        }
        catch (Exception e) {}
        try {
            Class.forName ("net.rim.device.api.system.Graphics");
	    if (platform == null) platform = "rim";
            return "de.kawt.impl.rim";
        }
        catch (Exception e) {}
        
        throw new RuntimeException 
	    ("unknown base lib and property de.kawt.classbase not set!");
	
    }

   /** creates default toolkit and fills static shortcut variables */
    
    static KawtToolkit init () {

	//System.out.println ("ti0");

	Runtime.getRuntime ().gc (); // dont ask why this is neccessary...

	//System.out.println ("ti1");

	String laf = classbase+".LafImpl";

	try { 
	    Class.forName("com.sun.midp.palm.Info"); 
	    laf = "de.kawt.impl.kjava.LafImpl";
	}
	catch (Throwable x) {}

	try {
	    Laf.laf = (Laf) Class.forName (laf).newInstance ();
	}
	catch (Exception e) {
	    Laf.laf = new Laf ();
	}

	//System.out.println ("*** ti2/cb: "+classbase);

        try {
	    //System.out.println ("*** ti3");
            graphicsImpl = Class.forName (classbase+".GraphicsImpl");
	    //System.out.println ("*** ti4");
            return (KawtToolkit) Class.forName 
		(classbase+".ToolkitImpl").newInstance ();
        }
        catch (Exception e) {
	    //System.out.println ("*** ti5: "+e);
            throw new RuntimeException ("kawt init failure: "+e.toString ());
        }
    }

    /** starts the kawtThread */

    protected Toolkit () {
        kawtThread.start ();
    }

    /** not abstract, just does nothing by default */

    public void beep () {
    }
    

    public ColorModel getColorModel () {
        return new ColorModel (pixelBits);
    }
    
    
    static public Toolkit getDefaultToolkit () {
        return defaultToolkit;
    }
    

    public EventQueue getSystemEventQueue () {
        return eventQueue;
    }


    public abstract Dimension getScreenSize ();


    static void flushRepaint () {
	synchronized (kawtLock) {

	    Window top = Laf.getTopWindow ();
	    if (top != null) {
		top.flushRepaint ();
		defaultToolkit.sync ();
	    }
	}
    }
    

    public void sync () {
    }

    static Graphics createGraphics () {
        try {
            return (Graphics) graphicsImpl.newInstance ();
        }
        catch (Exception e) {
            throw new RuntimeException ("createGraphics failed: "+e.toString ());
        }
    }


    public Image createImage (String ressourceName) {
	throw new RuntimeException ("jar ressources not yet supported");
    }

    public abstract Image createImage (byte [] data);


    public abstract FontMetrics getFontMetrics (Font font);

    
    public static String getProperty (String key, String dflt) {
	if (key.equals ("kawt.classbase")) 
	    return classbase;
	else 
	    return dflt;
    }

}



