package de.kawt.impl.rim;

import java.awt.*;
import java.io.*;

public class ToolkitImpl extends Toolkit {

    // static, damit aus dem constr. auf die eventqueue zugegr wd. kann (?)

    static UiApplicationImpl app;
    

    public ToolkitImpl () {
        app = new UiApplicationImpl ();
        new Thread (app).start ();
    }

    public void beep () {
    }


    public Image createImage (byte [] data) {
        return new ImageImpl ();
    }


    public FontMetrics getFontMetrics (Font font) {
        return new FontMetricsImpl (font);
    }

    public Dimension getScreenSize () {

        while (GraphicsImpl.rimGraphics == null) {
            Thread.yield ();
         }
         return new Dimension (app.screen.getWidth (), 
                              app.screen.getHeight ());
    }
}


