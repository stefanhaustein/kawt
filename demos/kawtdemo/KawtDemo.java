// Authors: Stefan Haustein (SH), Michael Kroll (MK)
//

import java.awt.*;
import java.awt.event.*;
import de.kawt.*;


public class KawtDemo extends KAWTlet implements ActionListener {

    class LayoutPanel extends Panel implements ActionListener {
        
        LayoutManager saveBorder;
        KAWTlet kawtlet;
        
        LayoutPanel () {
            
            super (new BorderLayout ());
            
            addButton ("North", "grid");
            addButton ("West", "exit");
            addButton ("Center", "dialog");
            addButton ("East", "flow");
            addButton ("South", "border");
            saveBorder = getLayout ();
            validate ();
        }
        
        
        void addButton (String where, String label) {
            Button b = new Button (label);
            add (where, b);
            b.addActionListener (this);
            b.setActionCommand (label);
        }
        

    
        public void actionPerformed (ActionEvent e) {
            String cmd = e.getActionCommand ();
            if (cmd.equals ("grid")) 
                setLayout (new GridLayout (3, 2));
            else if (cmd.equals ("flow"))
                setLayout (new FlowLayout ());
            else if (cmd.equals ("border"))
                setLayout (saveBorder);
            else if (cmd.equals ("dialog")) {
                if (OptionDialog.showConfirmDialog 
                    (frame, "Do you like kAWT?", "Sample Dialog") 
                    == OptionDialog.NO_OPTION)
                    OptionDialog.showInputDialog 
                        (frame, "Why don't you like it?");
            }
            else if (cmd.equals ("exit")) { 
                notifyDestroyed ();
	    }
            validate ();
        }
    } 
    

    class MiscPanel extends Panel implements ActionListener, ItemListener { 
        
        Checkbox checkBox1;
        Checkbox checkBox2;
        Checkbox titlebox;
        TextField tf;

        MiscPanel () {
            super (new GridLayout (0, 1));

            CheckboxGroup group = new CheckboxGroup ();
            
            checkBox1 = new Checkbox ("Public text", group, false);
            checkBox1.addItemListener (this);
            checkBox2 = new Checkbox ("Hidden text", group, false);
            checkBox2.addItemListener (this);
            
            add (checkBox1); 
            add (checkBox2);
            
            titlebox = new Checkbox ("Show Frame Title", true);
            titlebox.addItemListener (this);
            add (titlebox);
        
            tf = new TextField ("Test");
            add (tf);
            
            Choice choice = new Choice ();
            choice.add ("red");
            choice.add ("green");
            choice.add ("blue");
            add (choice);
            addButton ("beep");
            addButton ("exit");
        }
    
        void addButton (String label) {
            Button b = new Button (label);
            add (b);
            b.addActionListener (this);
            b.setActionCommand (label);
        }
        
        public void itemStateChanged (ItemEvent ev) {
        
            if (ev.getSource () == titlebox) {
                
                if (ev.getStateChange () == ev.SELECTED) 
                    frame.setTitle ("kAWT Demo v0.9903");
                else 
                    frame.setTitle (null);

                //getFrame ().validate ();	
            }
            else if (ev.getStateChange () == ev.SELECTED) {
                tf.setEchoChar (ev.getSource () == checkBox2 ? '*' : '\0');
                tf.setText (tf.getText ());
            }
        }
    
    
        public void actionPerformed (ActionEvent ev) {
            if (ev.getActionCommand ().equals ("beep")) {
                Toolkit.getDefaultToolkit ().beep ();
            }
            else {
		destroyApp (true);
		notifyDestroyed ();
	    }
	}
    }

    class ScrollbarPanel extends Panel implements ActionListener {

        ScrollbarPanel () {
            super (new BorderLayout ());
            
            Panel center = new Panel (new GridLayout (0, 2));
            
            java.awt.List list = new java.awt.List ();
            
            for (int i= 0; i < 16; i++)	
                list.add ("item "+i);
            
            center.add (list);

            TextArea ta = new TextArea ();
            
            ta.setText ("This is an example of a simple TextArea with automatic word wrapping in this text block and some hard line breaks below.\n01\n02\n03\n04\n05\n06\n07\n08\n09\n10\n11\n12\n13\n14\n15\n16\n17\n18\n19\n20\n");
        
            center.add (ta);
            
            Button exitB = new Button ("exit");
            exitB.addActionListener (this);
            exitB.setActionCommand ("exit");
            add ("Center", center);
            add ("South", exitB);
            validate ();
        }
    

        public void actionPerformed (ActionEvent e) {
            String cmd = e.getActionCommand ();
            if (cmd.equals ("exit")) {
		destroyApp (true);
                notifyDestroyed ();
	    }
        }
    }
    

    Frame frame = new Frame ("kAWT Demo v1.0");;
    Dialog dialog;
    TabbedPane tabPane;

    public KawtDemo () {
        MenuBar menuBar = new MenuBar ();
        Menu menu = new Menu ("Info");
        MenuItem mi = new MenuItem ("About kAWT");
        mi.addActionListener (this);
        menu.add (mi);

        mi = new MenuItem ("Memory");
        mi.addActionListener (this);
        menu.add (mi);

	menuBar.add (menu);

        menu = new Menu ("Tabs");

	for (int i = 1; i <= 3; i++) {
	    mi = new MenuItem ("Tab "+i);
	    mi.addActionListener (this);
	    menu.add (mi);
	}
	menuBar.add (menu);
        frame.setMenuBar (menuBar);

        
        tabPane = new TabbedPane();
        tabPane.addTab ("Layout", new LayoutPanel ());
        //tabPane.addTab ("Color", new MemPanel ());
        tabPane.addTab ("Scroll", new ScrollbarPanel ());
        tabPane.addTab ("Misc", new MiscPanel ());
        frame.add ("Center", tabPane);
        
        //getRootPane ().add ("Center", new LayoutPanel ());

	frame.addWindowListener (new Closer (this));

        frame.pack ();
    }

    public void startApp () {
	frame.show ();
    }


    public void destroyApp (boolean uncond) {
	frame.dispose ();
    }
    


    public void actionPerformed (ActionEvent e) {
	String cmd = e.getActionCommand ();
	if (cmd.equals ("About kAWT")) {
	    OptionDialog.showMessageDialog 
		(frame, "For information about kAWT, please visit\nhttp://www.kawt.de");
	}
	else if (cmd.equals ("Memory")) {
	    int count = 0;
	    long free0 = Runtime.getRuntime().freeMemory ();
	    long free = free0;
	    while (true) {
		Runtime.getRuntime().gc();
		long newFree = Runtime.getRuntime().freeMemory ();
		if (newFree <= free) break;
		count ++;
		free = newFree;
	    }
	    OptionDialog.showMessageDialog 
		(frame, "ini: "+free0+"\ngc"+count+": "+free);
	}
	else {
	    int nr = Integer.parseInt (cmd.substring (4));
	    tabPane.setSelectedIndex (nr-1);
	}
    }

    public static void main (String [] argv) {
        new KawtDemo ().startApp ();
    }
    
    
}






