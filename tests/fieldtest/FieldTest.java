import java.awt.*;
import java.awt.event.*;

public class FieldTest extends Frame implements TextListener{
    
    TextField field1;
    TextField field2;

    public FieldTest(){
	super("FieldTest");
	setLayout(new BorderLayout());
	Panel panel1 = new Panel();	
	field1 = new TextField(15);
	panel1.add(new Label("input :"));
	panel1.add(field1);

	field1.addTextListener(this);
	
	Panel panel2 = new Panel();
	field2 = new TextField(15);
	panel2.add(new Label("output:"));
	panel2.add(field2);

	add(panel1, BorderLayout.NORTH);
	
	add(new Label("Shouldn't they be the same??"), BorderLayout.CENTER);
	
	add(panel2, BorderLayout.SOUTH);
	pack();
	validate();
    }
    
    public void textValueChanged(TextEvent ev){
	TextField field = (TextField) ev.getSource();
	field2.setText(field.getText());
    }
    
    public static void main(String[] args){
	try{
	    FieldTest test = new FieldTest();
	    test.show();
	}catch(Exception ex){}
    }
}
