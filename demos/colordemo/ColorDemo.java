/*
 * ColorDemo version 0.95
 *
 * Copyright (C) 1999-2000 by Michael Kroll & Stefan Haustein GbR, Essen
 *
 * Contact: kawt@kawt.de
 * General Information about kAWT is available at: http://www.kawt.de
 *
 * Using kAWT for private and educational and in GPLed open source
 * projects is free. For other purposes, a commercial license must be
 * obtained. There is absolutely no warranty for non-commercial use.
 *
 *
 * 1. BECAUSE THE PROGRAM IS LICENSED FREE OF CHARGE, THERE IS NO
 *    WARRANTY FOR THE PROGRAM, TO THE EXTENT PERMITTED BY APPLICABLE
 *    LAW.  EXCEPT WHEN OTHERWISE STATED IN WRITING THE COPYRIGHT
 *    HOLDERS AND/OR OTHER PARTIES PROVIDE THE PROGRAM "AS IS" WITHOUT
 *    WARRANTY OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING, BUT
 *    NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *    FITNESS FOR A PARTICULAR PURPOSE.  THE ENTIRE RISK AS TO THE
 *    QUALITY AND PERFORMANCE OF THE PROGRAM IS WITH YOU.  SHOULD THE
 *    PROGRAM PROVE DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY
 *    SERVICING, REPAIR OR CORRECTION.
 *   
 * 2. IN NO EVENT UNLESS REQUIRED BY APPLICABLE LAW OR AGREED TO IN
 *    WRITING WILL ANY COPYRIGHT HOLDER, OR ANY OTHER PARTY WHO MAY
 *    MODIFY AND/OR REDISTRIBUTE THE PROGRAM AS PERMITTED ABOVE, BE
 *    LIABLE TO YOU FOR DAMAGES, INCLUDING ANY GENERAL, SPECIAL,
 *    INCIDENTAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE USE OR
 *    INABILITY TO USE THE PROGRAM (INCLUDING BUT NOT LIMITED TO LOSS OF
 *    DATA OR DATA BEING RENDERED INACCURATE OR LOSSES SUSTAINED BY YOU
 *    OR THIRD PARTIES OR A FAILURE OF THE PROGRAM TO OPERATE WITH ANY
 *    OTHER PROGRAMS), EVEN IF SUCH HOLDER OR OTHER PARTY HAS BEEN
 *    ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *   
 *    END OF TERMS AND CONDITIONS
 */ 

import java.awt.*;
import java.awt.event.*;
import de.kawt.*;

class MemPanel extends Canvas {
    

    public static int hueToRgb (int hue) { 
        int r = 0, g = 0, b = 0;

        int h = hue / 43;  // 0..5
        int f = (hue % 43) * 6; // 0..255 

        int p = 0;
        int q = 255 - f;
        int t = f;

        switch (h) {
        case 0:
            r = 255;
            g = f;
            b = 0;
            break;
        case 1:
            r = 255-f;
            g = 255;
            b = 0;
            break;
        case 2:
            r = 0;
            g = 255;
            b = f;
            break;
        case 3:
            r = 0;
            g = 255-f;
            b = 255;
            break;
        case 4:
            r = f;
            g = 0;
            b = 255;
            break;
        case 5:
            r = 255;
            g = 0;
            b = 255-f;
            break;
        }
        return 0xff000000 | (r << 16) | (g << 8) | (b << 0);
    }

    public void paint (Graphics g) {
        
        Dimension d = getSize ();
        
        int m = d.width > d.height ? d.height : d.width;
        int step = m / 10; 
        m = step * 10;
        
        g.setColor (new Color (127, 127, 127));
        g.fillRect (0, 0, d.width, d.height);
        
        g.setColor (Color.white);
        
        for (int i = step/2; i < d.width; i+= step)
            g.drawLine (i, 0, i, d.height);
        
        for (int i = step/2; i < d.height; i+= step)
            g.drawLine (0, i, d.width, i);
        
        g.translate ((d.width - m) / 2, (d.height - m) / 2);
        g.fillRect (step, step, 8*step, 8*step); 
        
       
        int w = 8*step;
        for (int i = 0; i < w; i++) {

           int j = (i * 255) / w; 
           g.setColor (new Color (hueToRgb (j)));
           g.drawLine (i + step, step, i + step, 3*step+step/2);

           g.setColor (new Color (j, j, j));
           g.drawLine (i + step, 3*step+step/2, i+step, 5*step);
        }
        /*
        for (int i = 0; i < 8; i++) {
            g.setColor (new Color (((i & 1) * 255) ,
                                   ((i & 2) >> 1) * 255 ,
                                   ((i & 4) >> 2) *255));
            
            g.fillRect ((i+1) * step, step, step, 2*step+step/2);
            
            int gray = 255 / 8 * i;
            g.setColor (new Color (gray, gray, gray));
            
            g.fillRect ((i+1) * step, 3*step+step/2, step, step+step/2);  
        }
        */
        
        g.setColor (Color.white);
        g.drawOval (0, 0, m, m);
        
        int ty = 6*step;
        
        g.setColor (Color.black);
        
        boolean small = m < 100;
        
        g.drawString 
            ((small ? "ttl " : "total mem: ")
             + Runtime.getRuntime().totalMemory(), 2*step, ty);
        
	long free = Runtime.getRuntime().freeMemory ();
	
        g.drawString 
            ((small ? "fr " : "free mem: ") 
             + free, 2*step, ty+step);
        
	int count = 0;
	while (true) {
	    Runtime.getRuntime().gc();
	    long newFree = Runtime.getRuntime().freeMemory ();
	    if (newFree <= free) break;
	    count ++;
	    free = newFree;
	}

        g.drawString 
            ((small ? "gc " : "post gc: ")
             + "(" + count +")"+free , 2*step, ty+2*step);
    }
    
    public Dimension getPreferredSize () {
        return new Dimension (160, 160);
    }
}

public class ColorDemo extends KAWTlet implements ActionListener {
    
    Frame frame;

    public ColorDemo () {
        frame = new Frame ("kAWT Color Demo v1.0");
	
	frame.addWindowListener (new de.kawt.Closer (this));

        Button exit = new Button ("Exit");
        exit.addActionListener (this);

        frame.add ("Center", new MemPanel ());
        frame.add ("South", exit);
        frame.pack ();
    }
    
    public void startApp () {
      frame.setVisible (true);
    }



    public void destroyApp (boolean unconditional) {
      frame.setVisible (false);
    }


    public void actionPerformed (ActionEvent e) {
        String cmd = e.getActionCommand ();
        if (cmd.equals ("Exit")) {
            destroyApp (true);
            notifyDestroyed ();
        }
    } 


    public static void main (String [] argv) {
        new ColorDemo ().startApp ();
    }

}
