import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import java.awt.Rectangle;

public class ButtonBar extends JPanel implements ActionListener
{
    JToggleButton classButton, compositionButton, inheritanceButton, relationalButton;
    ImageIcon classIcon, compositionIcon, inheritanceIcon, relationalIcon;
    Designer designer;

    public ButtonBar(Designer d)
    {
		this.designer = d;
		classIcon = new ImageIcon("images/class.gif");
		classButton = new JToggleButton(classIcon);
		classButton.setToolTipText("Draw class");
		classButton.setActionCommand("class");
		
		compositionIcon = new ImageIcon("images/composition.gif");
		compositionButton = new JToggleButton(compositionIcon);
		compositionButton.setToolTipText("Draw composition");
		compositionButton.setActionCommand("composition");

		inheritanceIcon = new ImageIcon("images/inheritance.gif");
		inheritanceButton = new JToggleButton(inheritanceIcon);
		inheritanceButton.setToolTipText("Draw inheritance");
		inheritanceButton.setActionCommand("inheritance");

		relationalIcon = new ImageIcon("images/relation.gif");
		relationalButton = new JToggleButton(relationalIcon);
		relationalButton.setToolTipText("Draw ER-relationans");
		relationalButton.setActionCommand("relational");

		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(classButton);
		add(compositionButton);
		add(inheritanceButton);
		add(relationalButton);

//	setBounds(new Rectangle(0, 32, 400, 20));
		setOpaque(true);

		classButton.addActionListener(this);
		classButton.setSelected(true);
		compositionButton.addActionListener(this);
		inheritanceButton.addActionListener(this);
		relationalButton.addActionListener(this);
		setVisible(true);
/*	
		JFrame frame = new JFrame();
		frame.getContentPane().add(this);
		frame.setVisible(true);
		frame.pack();
*/
    }

    public void actionPerformed (ActionEvent e)
    {
		String actionCommand = e.getActionCommand();
		// Why does a button become deselected if it is clicked twice?
		if (!actionCommand.equals(classButton.getActionCommand()))
		{
//	    if (!classButton.isSelected())
				classButton.setSelected(false);
		}
		if (!actionCommand.equals(compositionButton.getActionCommand()))
		{
//	    if (!compositionButton.isSelected())
			compositionButton.setSelected(false);
		}
		if (!actionCommand.equals(inheritanceButton.getActionCommand()))
		{
//	    if (!inheritanceButton.isSelected())
			inheritanceButton.setSelected(false);
		}
		if (!actionCommand.equals(relationalButton.getActionCommand()))
		{
			relationalButton.setSelected(false);
		}
		if (actionCommand.equals(classButton.getActionCommand()))
		{
			designer.setDesignMode(Designer.DM_CLASS);
			classButton.setSelected(true);
		}
		else if (actionCommand.equals(compositionButton.getActionCommand()))
		{
			designer.setDesignMode(Designer.DM_COMPOSITION);
			compositionButton.setSelected(true);
		}
		else if (actionCommand.equals(inheritanceButton.getActionCommand()))
		{
			designer.setDesignMode(Designer.DM_INHERITANCE);
			inheritanceButton.setSelected(true);
		}
		else if (actionCommand.equals(relationalButton.getActionCommand()))
		{
			designer.setDesignMode(Designer.DM_RELATIONAL);
			relationalButton.setSelected(true);
		}
    }
/*
    public static void main (String args[])
    {
	ButtonBar b = new ButtonBar();
    }
*/
}

    
