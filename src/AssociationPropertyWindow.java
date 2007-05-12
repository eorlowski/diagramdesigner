import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AssociationPropertyWindow extends JDialog implements ActionListener
{
    JComboBox associationList = new JComboBox();
    JTextField associationName;
    JTextField fromClass, toClass;
    
    public AssociationPropertyWindow()
    {
	associationName = new JTextField();
	fromClass = new JTextField();
	toClass = new JTextField();
	fromClass.setEditable(false);
	toClass.setEditable(false);
	
	getContentPane().setLayout(new GridLayout(4, 2));
	getContentPane().add(new JLabel("Association"));
        getContentPane().add(associationList);

	getContentPane().add(new JLabel("Name"));
	getContentPane().add(associationName);
	getContentPane().add(new JLabel("From class"));
	getContentPane().add(fromClass);
	getContentPane().add(new JLabel("To class"));
	getContentPane().add(toClass);
	
	associationList.addActionListener(this);
        associationList.setEditable(false);
	associationName.addActionListener(this);
	setTitle("Association properties");
	setSize(200, 200);
        setResizable(true);
	setModal(true);
    }

    public void setData(Rectangles rectangles, Associations associations)
    {
	associationList.removeAllItems();
	for (int i= 0; i < associations.size(); i++)
	{
	    associationList.addItem(associations.get(i));
	}
    }

    public void actionPerformed(ActionEvent ae)
    {
//	System.out.println("Action source: " + ae.getSource());
	if (ae.getSource() instanceof JComboBox)
	{
	    Association a = ((Association) ((JComboBox) ae.getSource()).getSelectedItem());
	    associationName.setText(a.getName());
	    fromClass.setText(a.getFromClass().getName());
	    toClass.setText(a.getToClass().getName());
	}
	else if (ae.getSource() instanceof JTextField)
	{
	    Association a = (Association) associationList.getSelectedItem();
	    a.setName(associationName.getText());
	}
    }
}
