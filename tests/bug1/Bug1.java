import java.awt.*;
import java.awt.event.*;

public class Bug1 extends Frame implements ActionListener {
    
    Bug1 () {
        setTitle ("No Textfield in 'North'");
        Button exit = new Button("Exit");
        exit.addActionListener (this);
        exit.setActionCommand ("Exit");
	  TextField tf = new TextField ();
        add("North", tf);
        add("South", exit);
        pack ();
    }
    
    public void actionPerformed (ActionEvent e) { 
        String cmd = e.getActionCommand ();
        if (cmd.equals ("Exit")) 
       System.exit (0);
    }
    
    public static void main (String [] argv) {
        new Bug1 ().show ();
    }
}

