import java.awt.*;
import java.awt.event.*;
import de.kawt.*;


class PenDemo extends KAWTlet {

    int x;
    int y;
    StringBuffer coords = new StringBuffer ();
    Frame frame;
    
    class PenCanvas extends Canvas  implements MouseListener, 
					  MouseMotionListener  {
	
	public void paint (Graphics g) {
	    
	    int len = coords.length ();
	    for (int i = 0; i < len; i+=4) 
		g.drawLine (coords.charAt (i), coords.charAt (i+1),
			    coords.charAt (i+2), coords.charAt (i+3));
	}


	public void mouseDragged (MouseEvent me) {
	    Graphics g = getGraphics ();
	    
	    if (g != null) 
		g.drawLine (x, y, me.getX (), me.getY ());
	
	    coords.append ((char) x);
	    coords.append ((char) y);
	    
	    x = me.getX ();
	    y = me.getY ();
	    
	    coords.append ((char) x);
	    coords.append ((char) y);
	}
	

	public void mouseEntered (MouseEvent me) {}
	public void mouseExited (MouseEvent me) {}
	public void mouseMoved (MouseEvent me) {}
	
	
	public void mousePressed (MouseEvent me) {
	    x = me.getX ();
	    y = me.getY ();
	}


	public void mouseReleased (MouseEvent me) {}	
	public void mouseClicked (MouseEvent me) {}    
    }
	

    public PenDemo () {
	frame = new Frame ("PenTest");
	frame.addWindowListener (new Closer (this));
	frame.setSize (Toolkit.getDefaultToolkit ().getScreenSize ());
	
	PenCanvas pc = new PenCanvas ();
	frame.add ("Center", pc);
	pc.addMouseListener (pc);
	pc.addMouseMotionListener (pc);
    }


    public void startApp () {
	frame.show ();
    }
	

    public void destroyApp (boolean unconditional) {
	frame.setVisible (false);
    }
   
	
    public static void main (String [] argv) {
	new PenDemo ().startApp ();
    }
    
}
