package de.kawt.impl;

import java.awt.*;

public abstract class KawtToolkit extends Toolkit {

    public abstract Image createImage (int w, int h);


    public static Component findComponentAt (Component c, int x, int y) {
	while (true) {
	    Component c2 = c.getComponentAt (x, y);
	    if (c == c2) return c;
	    c = c2;
	}
	    
    }

} 
