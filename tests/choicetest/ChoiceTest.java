import java.awt.*;
import java.awt.event.*;

public class ChoiceTest extends Frame implements ActionListener {

    Choice strings;

    TextField string = new TextField ();

    ChoiceTest () {
	setTitle ("kAWT Choice Test");
	strings = new Choice ();
	
	Button delI = new Button ("rmByItem");
	delI.addActionListener (this);
	
	Button delX = new Button ("rmByIndex");
	delX.addActionListener (this);

	Button add = new Button ("add");
	add.addActionListener (this);


	Panel btnPanel = new Panel (new FlowLayout ());
	
	btnPanel.add (add);
	btnPanel.add (delI);
	btnPanel.add (delX);

	strings.add ("String 1");
	strings.add ("String 2");
	strings.add ("String 3");

	add ("Center", strings);

	Panel p = new Panel (new BorderLayout ());
	
	p.add ("West", new Label ("Text"));
	p.add ("Center", string);

	add ("North", p);


	add ("South", btnPanel);

        pack();
    }
    
    public void actionPerformed (ActionEvent e) { 
	String cmd = e.getActionCommand ();
	if (cmd.equals ("rmByItem")) {
	    if (strings.getSelectedIndex () > -1)
		strings.remove (strings.getSelectedItem ()); 
	} 
	else if (cmd.equals ("rmByIndex")) {
	    if (strings.getSelectedIndex () > -1)
		strings.remove (strings.getSelectedIndex ()); 
	} 
	else if (cmd.equals ("add")) {
	    strings.add (string.getText ());
	}
    }
    
    public static void main (String [] argv) {
	new ChoiceTest ().show ();
    }
}

