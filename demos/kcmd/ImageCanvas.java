import java.io.*;
import java.awt.*;
import java.awt.event.*;

import de.kawt.*;
import de.kawt.shell.*;

class ImageCanvas extends Canvas {

    Image image;

    ImageCanvas (Image image) {
	this.image = image;
    }

	
    public void paint (Graphics g) {
	
	Dimension d = getSize ();

	int w = image.getWidth (this);
	int h = image.getHeight (this);

	g.drawImage (image, (d.width - w) / 2, (d.height - h) / 2, this);
    }


    public Dimension getMinimumSize () {
	return new Dimension (image.getWidth (this), image.getHeight (this));
    }
}
