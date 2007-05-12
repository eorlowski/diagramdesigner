import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import javax.swing.*;
import javax.swing.event.*;

import java.util.Vector;

/**
 *
 * The DrawPane is the panel where the drawing is done on.
 * It contains the rectangles and the associations
 * Events are handled and the drawing is edited.
 */
public class DrawPane extends JPanel implements Printable, Scrollable
                                            // Action Listener detects when classname
                                            // entered in object_name_field
{
    // some variables for the rectangle drawn
    boolean rectangle_selected = false;
    boolean rectangle_highlighted = false;
    boolean rectangle_moved = false;
    boolean rectangle_resized = false;
    int corner_resized = 0;
    boolean unselecting = false;
    boolean adding_method = false;
    boolean is_method_selected = false;
    // some variables for the association drawn
    boolean isAssociationDrawn = false;
    Line currentLine;
    Associations associations;
    Association associationDrawn;
    boolean isAssociationHighlighted = false;
    boolean isAssociationSelected = false;
    // "radius" of the diamond in a composition
    int diamondSize = 5;
    private AssociationPropertyWindow associationPropertyWindow;
    // The projection point is the point, visualized by the blue rectangle, and is the point to add if the mouse is released
    Association activeAssociation = null;
    Point projectionPoint = null;
    Point pointToAdd = null;
    Line mergeLine = null;
    int pointToRemoveIndex = -1;
	int preferredSizeX = 640;
	int preferredSizeY = 480;

    int key_pressed;
    int ctrl_key = 17;
    Point startingPoint, end_point, last_point;
    Orectangle rectangle, old_rectangle, rectangle_to_resize;
    Rectangles rectangles = new Rectangles();
    //rectangle_to_resized = new Orectangle();
    Method selected_method;
    Graphics g;
    int font_height;

    String object_name;
    JTextField object_name_field = new JTextField();
    JTextField method_name_field = new JTextField();
    boolean mousepressed = false;
    Designer designer;
    boolean file_opened = false;
    
    JLabel dummyTextField;

//    ArrayList rectangles = new ArrayList();
//    int index = 0;

    public void requestFocus()
    {
//	System.out.println("Requested focus");
	dummyTextField.requestFocus();
    }

    public Orectangle getActiveRectangle()
    {
        int i = 0;
        while (i < rectangles.size())
        {
            if (((Orectangle) rectangles.get(i)).isselected)
            {
                return ((Orectangle) rectangles.get(i));
            }
            i++;
        }
        return null;
    }

    public Association getActiveAssociation()
    {
	    for (int i = 0; i < associations.size(); i++)
	    {
		    if (associations.get(i).isSelected())
			    return associations.get(i);
	    }

	    return null;
    }
    
    public Associations getAssociations()
    {
	    return associations;
    }

    public String getActiveClass()
    {
        int i = 0;
        while (i < rectangles.size())
        {
            if (((Orectangle) rectangles.get(i)).isselected)
            {
                return ((Orectangle) rectangles.get(i)).getName();
            }
            i++;
        }
        return "";
    }

    class ObjectNameEntered implements ActionListener
    {
        public void actionPerformed(ActionEvent ae)
        {
            object_name = object_name_field.getText();
            System.out.println("Text typed: " + object_name);
            int i = 0;
            while (i < rectangles.size())
            {
                if (((Orectangle) rectangles.get(i)).edited)
                {
                    //String old_constructor_name = new String(((Orectangle) rectangles.get(i)).constructor_name);
                    System.out.println("About to setting object name");
                    ((Orectangle) rectangles.get(i)).setName(object_name);
                    System.out.println("Object name set");
                    //
                    // TODO: dit werkt hier niet !!!
                    //
                    /*
                    if (!((Orectangle) rectangles.get(i)).methods.isEmpty())
                    {
                        ((Orectangle) rectangles.get(i)).getMethodByName(old_constructor_name).method_name = object_name;
                    }
                    */
                    //((Orectangle) rectangles.get(i)).constructor_name = object_name + " ()";
                    ((Orectangle) rectangles.get(i)).edited = false;
                    object_name_field.setText("");
                    object_name_field.setVisible(false);
                    //requestFocus();
                }
                i++;
            }
            repaint();
        }
    }

    class MethodNameEntered implements ActionListener
    {
        public void actionPerformed(ActionEvent ae)
        {
            String method_name = method_name_field.getText();
            System.out.println("Text typed: " + object_name);
            int i = 0;
            while (i < rectangles.size())
            {
                if (((Orectangle) rectangles.get(i)).edited)
                {
                    if (!adding_method)
                    {
                        System.out.println("Editing method");
                        // Editing existing method

                        //((Orectangle) rectangles.get(i)).setName(object_name);
                        //((Orectangle) rectangles.get(i)).addMethod(new Method(method_name));
                        //
                        // TODO: Dit blijkt niet helemaal goed te gaan
                        // de 'rectangle' wordt niet meegenomen
                        //
                        ((Orectangle) rectangles.get(i)).getMethodByName(selected_method.method_name).method_name = method_name;
                        // TODO: set coordinates of method_rectangle
                        //
    //                    System.out.println("Size of methods " + ((Orectangle) rectangles.get(i)).getMethods().size());
                        int size_methods = ((Orectangle) rectangles.get(i)).getMethods().size();
    //                    System.out.println("size methods: " + size_methods);
    //                    g = getGraphics();
    //                    font_height = g.getFontMetrics().getHeight();
    //                    System.out.println("font height: " + font_height);
    //                    int text_width = g.getFontMetrics().stringWidth(method_name);
                        //
                        // TODO: calculate position of methodname
                        //
                        // Set method rectangle
                        /*
                        ((Orectangle) rectangles.get(i)).getMethodByName(method_name).setMethodRectangle((Rectangle) new Rectangle(((Orectangle) rectangles.get(i)).x
                                            , ((Orectangle) rectangles.get(i)).y + font_height*(size_methods + 1)
                                            , ((Orectangle) rectangles.get(i)).width
                                            , font_height));
                        */
                        ((Orectangle) rectangles.get(i)).edited = false;
                        method_name_field.setText("");
                        method_name_field.setVisible(false);
                        //requestFocus();
                    }
                    else
                    {
                        System.out.println("Adding method");
                        // Adding new method
                        ((Orectangle) rectangles.get(i)).addMethod(new Method(method_name));

                        // Set method rectangle
                        int size_methods = ((Orectangle) rectangles.get(i)).getMethods().size();
                        //font_height = g.getFontMetrics().getHeight();
                        ((Orectangle) rectangles.get(i)).getMethodByName(method_name).setMethodRectangle((Rectangle) new Rectangle(((Orectangle) rectangles.get(i)).x
                                            , ((Orectangle) rectangles.get(i)).y + font_height*(size_methods + 1)
                                            , ((Orectangle) rectangles.get(i)).width
                                            , font_height));
                        ((Orectangle) rectangles.get(i)).edited = false;
                        method_name_field.setText("");
                        method_name_field.setVisible(false);
                        adding_method = false;
                    }
                }
                i++;
            }
            repaint();
        }
    }

    public void setRectangles(Rectangles rectangles)
    {
        this.rectangles = rectangles;
        file_opened = false;
    }

    public Rectangles getRectangles()
    {
	    return rectangles;
    }

    public void setAssociations(Associations associations)
    {
		this.associations = associations;
    }

    public void delete_rectangles()
    {
        int i = 0;
        object_name_field.setVisible(false);
        requestFocus();
        while (i < rectangles.size())
        {
            if (rectangles.get(i).isselected)
            {
                if (!is_method_selected)
                {
                    rectangles.remove(i);
                    rectangle = null;
                    old_rectangle = null;
                    rectangle_selected = false;
                }
                else
                {
            //        System.out.println("To delete method");
                    // Search for selected method and delete it
                    ((Orectangle) rectangles.get(i)).delete_selected_method();
                    rectangle = null;
                    old_rectangle = null;
                    rectangle_selected = false;
                    repaint();
                }
            }
/*            else
            {
            }
*/
            i++;
        }

	for (i = 0; i < associations.size(); i++)
	{
	    if (associations.get(i).isSelected())
		associations.remove(i);
	}
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        repaint();
    }

    //boolean dragged_rectangle_painted = false;

    public void setKeyPressed(int key)
    {
        designer.key_pressed = key;

    }

    public DrawPane(Designer designer)
    {
//        super();
        this.designer = designer;
        rectangles = designer.getRectangles();
		associations = new Associations();
        selected_method = new Method(null);
        startingPoint = new Point(0, 0);
        end_point = new Point(0, 0);
        last_point = startingPoint;
        MyMouseListener myMouseListener = new MyMouseListener();
		dummyTextField = new JLabel();
		add(BorderLayout.SOUTH, dummyTextField);
	// Enable key events to be handled
        MyKeyListener myKeyListener = new MyKeyListener();
        dummyTextField.addKeyListener(new MyKeyListener());
//	dummyTextField.setVisible(false);
	
        addMouseListener(myMouseListener);
        addMouseMotionListener(myMouseListener);
        setBackground(Color.white);
//        setOpaque(false);
        setOpaque(true);
//        updateUI();
        add(object_name_field);
        object_name_field.addActionListener(new ObjectNameEntered());
        object_name_field.setVisible(false);
        object_name_field.setOpaque(true);
        add(method_name_field);
        method_name_field.addActionListener(new MethodNameEntered());
        method_name_field.setVisible(false);
		associationPropertyWindow = new AssociationPropertyWindow();

        g = getGraphics();
        setBounds(0, 0, 100, 100);
				setPreferredSize(new Dimension(preferredSizeX,preferredSizeY));
				System.out.println(" preferred size drawpane: " + getPreferredSize().getWidth() + ", height:" + getPreferredSize().getHeight()); 
        //font_height = g.getFontMetrics().getHeight();
		requestFocus();
    }

    public DrawPane getPanel()
    {
        return this;
    }

    class MyKeyListener extends KeyAdapter
    {
        public void keyPressed (KeyEvent ke)
        {
            //key_pressed = ke.getKeyChar();
//            System.out.println("keyPressed\n");
            key_pressed = ke.getKeyCode();
            setKeyPressed(key_pressed);
            System.out.println("Pressed key: " + key_pressed);
            if (key_pressed == Designer.delete_key)
            {
                delete_rectangles();
                repaint();
            }
//            System.out.println("Pressed key: " + ke.KEY_TYPED);
        }
        
	public void keyReleased (KeyEvent ke)
        {
            System.out.println("Released key: " + ke.getKeyCode());
            key_pressed = 0;
            setKeyPressed(key_pressed);
        }
    }

    class MyMouseListener extends MouseInputAdapter
    {
/*        public void mouseClicked(MouseEvent me)
        {
            System.out.println("Mouse clicked\n");
        }
*/
        public void mousePressed(MouseEvent me)
        {
//            System.out.println("Mouse pressed from mousePressed\n");
            /* System.out.println("BUTTON1_MASK: " + me.BUTTON1_MASK);
            System.out.println("BUTTON2_MASK: " + me.BUTTON2_MASK);
            System.out.println("BUTTON3_MASK: " + me.BUTTON3_MASK);
            System.out.println("MOUSE_PRESSED: " + me.MOUSE_PRESSED);
            System.out.println("getModifiers(): " + me.getModifiers());
            */
//            System.out.println("Mouse modifiers: " + me.getModifiers());
            // If button 1 pressed (left button)
            if ((me.getModifiers() & me.BUTTON1_MASK) == me.BUTTON1_MASK)
            {
                //System.out.println("Button 1 pressed\n");
				switch (designer.getDesignMode())
				{
					case (Designer.DM_CLASS):
			
					if ((rectangle_selected) && (!rectangle_highlighted) && (!rectangle_resized))
					{
						System.out.println("Unselecting all\n");
						int i = 0;
						int j = 0;
						while (i < rectangles.size())
						{
							((Orectangle) rectangles.get(i)).unselect();
							// Unselect methods
							j = 0;
							while (j < ((Orectangle) rectangles.get(i)).methods.size())
							{
								((Method) ((Orectangle) rectangles.get(i)).methods.get(j)).selected = false;
								j++;
							}
							i++;
						}
						//rectangle_selected = !rectangle_selected;
						rectangle_selected = false;
						startingPoint = new Point(me.getPoint());
						unselecting = true;
						object_name_field.setVisible(false);
						method_name_field.setVisible(false);
						requestFocus();
						repaint();
					}
					else if ((!rectangle_highlighted) && (!rectangle_resized))
					{
						//System.out.println("Button 1 pressed\n");
						startingPoint = new Point(me.getPoint());
				//                    System.out.println(startingPoint);
						mousepressed = true;
					}
					else if (rectangle_resized)
					{
						if (corner_resized == 1)
						{
							startingPoint = new Point(rectangle_to_resize.x + rectangle_to_resize.width
										, rectangle_to_resize.y + rectangle_to_resize.height);
						}
						else if (corner_resized == 2)
						{
							startingPoint = new Point(rectangle_to_resize.x
										, rectangle_to_resize.y + rectangle_to_resize.height);
						}
						else if (corner_resized == 3)
						{
							startingPoint = new Point(rectangle_to_resize.x
										, rectangle_to_resize.y);
						}
						else if (corner_resized == 4)
						{
							startingPoint = new Point(rectangle_to_resize.x + rectangle_to_resize.width
										, rectangle_to_resize.y);
						}

						mousepressed = true;
					}
					else if (rectangle_highlighted)
					{
						setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
					}
					break;
				case (Designer.DM_COMPOSITION):
				case Designer.DM_RELATIONAL:
					// Pseudo-code
					//
					//
					// if not line highlighted
					if (!isAssociationHighlighted)
					{
						//
						// if not already drawing association
						if (!isAssociationDrawn)
						{
							if (projectionPoint != null)
							{
								pointToAdd = projectionPoint;
							}
							else // projectionPoint = null
							{
								// Now starting to draw composition line
								//System.out.println("Starting to draw composition line\n");

								Orectangle fromClass;
								fromClass = rectangles.contains(me.getPoint());
								//
								//   if mouse pointer in rectangle
								if (fromClass != null)
								{
									//   set starting point
									//
									startingPoint = me.getPoint();
									associationDrawn = new Association(startingPoint, designer.getDesignMode());
									isAssociationDrawn = true;
									associationDrawn.select();
									associationDrawn.setFromClass(fromClass);
									associationDrawn.setName("noname");
									fromClass.addAssociationFrom(associationDrawn);
									currentLine = new Line(startingPoint, startingPoint);
									associations.add(associationDrawn);
									System.out.println("From class : " + fromClass.getName());
								}
								else 
								{
									//System.out.println("No class selected to start association from");
									//Unselect all associations
									associations.unSelectAll();
									isAssociationSelected = false;
									repaint();
								}
							} // After either prepare for adding point to line or start to draw association or unselect
						}
						else /* Drawing association  (isAssociationDrawn = true) */
						{
							System.out.println("Association drawn! Size: " + associationDrawn.size());
							if (associationDrawn.size() == 2)
							{
								/* The first line part of the association will be drawn */
								/* Let out the part that's inside a rectangle */
								//
								// this method will calculate the new startingPoint
								startingPoint = associationDrawn.getFromClass().hits(associationDrawn.get(0), associationDrawn.get(1));
								if (startingPoint != null)
									associationDrawn.set(0, startingPoint);
							}
							Orectangle toClass;
							toClass = rectangles.contains(me.getPoint());
							//   if mouse pointer in rectangle
							if (toClass != null)
							{
								/* The last line part of the association will be drawn */
								/* Let out the part that's inside a rectangle */
								//
								associationDrawn.setToClass(toClass);
								startingPoint = me.getPoint();
									
								associationDrawn.add(startingPoint);
								startingPoint = associationDrawn.getToClass().hits(associationDrawn.get(associationDrawn.size() - 1), associationDrawn.get(associationDrawn.size() - 2));
								if (startingPoint != null)
									associationDrawn.set( associationDrawn.size() - 1, startingPoint);
								//      take this rectangle as an end relationship
								isAssociationDrawn = false;
								associationDrawn.unselect();
								currentLine = null;
								toClass.addAssociationTo(associationDrawn);
								System.out.println("To Class is: " + toClass.getName());
								
								//      Let's try to NOT wait for mouse release to commit this relationship
							}
							else /* mouse not in rectangle */
							//      set a point, store the first part of the line 
							//                   and continue drawing a line from the last set point
							//
							{
								startingPoint = me.getPoint();
								associationDrawn.add(startingPoint);
								currentLine = new Line(startingPoint, startingPoint);
							} 
							repaint();
						}// if drawing association (isAssociationDrawn = true)
					} // if not association highlighted
					else // some association has been highlighted: select the association
					{
						System.out.println("Selecting association!");
						System.out.println("Key pressed: "  + designer.key_pressed);
						for (int i = 0; i < associations.size(); i++)
						{
							if (associations.get(i).highlighted())
							{
								associations.get(i).select();
								isAssociationSelected = true;
							}
							else
							{
								if (!(designer.key_pressed == ctrl_key))
								{
										associations.get(i).unselect();
								}
							}
						} // for loop
						repaint();
					} // else: select highlighted association
					break;
				} // switch statement
				
			}
			else if (me.getModifiers() == me.BUTTON2_MASK)
			{
				//System.out.println("Button 2 pressed\n");
			}
			else if (me.getModifiers() == me.BUTTON3_MASK)
			{
				//System.out.println("Button 3 pressed\n");
				// Show popupmenu if rectangle highlighted or selected
				int i = 0;
				while (i < rectangles.size())
				{
					if ((((Orectangle) rectangles.get(i)).isselected)
					|| (((Orectangle) rectangles.get(i)).highlighted))
					{
					// TODO: Move this code to the point where the rectangle is
					// selected and enable/disable other menu items like deleteMenuItem
					//
					if (!((Orectangle) rectangles.get(i)).isselected)
					{
						//designer.popupdeleteMenuItem.setEnabled(false);
						((Orectangle) rectangles.get(i)).select();
						//repaint();
					}
					else
					{
						designer.popupdeleteMenuItem.setEnabled(true);
					}
					if (((Orectangle) rectangles.get(i)).name != null)
					{
						designer.propertywindow.setTitle(((Orectangle) rectangles.get(i)).name);
					}
					designer.propertymenu.show(designer.drawpane, me.getPoint().x, me.getPoint().y);
					}
					i++;
				} // while loop 
				repaint();
			}
			last_point = me.getPoint();
		}

		public void mouseReleased(MouseEvent me)
		{
			if ((me.getModifiers() & me.BUTTON1_MASK) == me.BUTTON1_MASK)
			{
				// Left mouse button released
//		System.out.println("LB Mouse released");
//		System.out.println("rectangle_highlighted: " + rectangle_highlighted);
//		System.out.println("rectangle_selected: "  + rectangle_selected);
				switch(designer.getDesignMode())
				{
				case (Designer.DM_CLASS):
//		    System.out.println("design mode DM_CLASS");
					if (!rectangle_highlighted)
					{
//			System.out.println("rectangle_highlighted is false");
						if (!rectangle_selected)
						{
			//			    System.out.println("rectangle_selected is false");
							if(!file_opened && startingPoint != null && !unselecting)
							{
								// Some rectangle has been drawn
			//				System.out.println("Mouse released");
								end_point = new Point(me.getPoint());
								g = getGraphics();
								font_height = g.getFontMetrics().getHeight();
			//				System.out.println("font height: " + font_height);
								rectangle = new Orectangle(font_height, (startingPoint.x < end_point.x) ? startingPoint.x : end_point.x
											, (startingPoint.y < end_point.y) ? startingPoint.y : end_point.y
											, (end_point.x > startingPoint.x) ? end_point.x - startingPoint.x : startingPoint.x - end_point.x
											, (end_point.y > startingPoint.y) ? end_point.y - startingPoint.y : startingPoint.y - end_point.y);
							//System.out.println("Rectangle created");

							/*rectangles.add(new Rectangle((startingPoint.x < end_point.x) ? startingPoint.x : end_point.x
											, (startingPoint.y < end_point.y) ? startingPoint.y : end_point.y
											, (end_point.x > startingPoint.x) ? end_point.x - startingPoint.x : startingPoint.x - end_point.x
											, (end_point.y > startingPoint.y) ? end_point.y - startingPoint.y : startingPoint.y - end_point.y));
							*/
					//                System.out.println(end_point);
					//                System.out.println(rectangle);
					//                System.out.println("Index: " + index++);
								if ((((end_point.x > startingPoint.x) ? end_point.x - startingPoint.x : startingPoint.x - end_point.x) >= Orectangle.MINIMUMWIDTH)
									&& (((end_point.y > startingPoint.y) ? end_point.y - startingPoint.y : startingPoint.y - end_point.y) >= Orectangle.MINIMUMHEIGHT))
								{
									// Rectangle has minimum required size: add to list
									rectangles.add(rectangle);
			//				    System.out.println("Rectangle added");
									//
									// TODO: set new_method_rectangle
									//
									int i = rectangles.size() - 1;
			//				    System.out.println("i: " + i);
									int size_methods = rectangles.get(i).getMethods().size();
			//				    System.out.println("size methods: " + size_methods);
			//				    System.out.println("font height: " + font_height);
								//int text_width = g.getFontMetrics().stringWidth(method_name);
								//
				/*                                ((Orectangle) rectangles.get(i)).getMethodByName(method_name).setMethodRectangle((Rectangle) new Rectangle(((Orectangle) rectangles.get(i)).x
													, ((Orectangle) rectangles.get(i)).y + font_height*(size_methods + 1)
													, ((Orectangle) rectangles.get(i)).width
													, font_height));

				*/
			//				    System.out.println("Got font height: " + font_height);
									rectangles.get(i).new_method_rectangle.x = rectangles.get(i).x;
									rectangles.get(i).new_method_rectangle.y = rectangles.get(i).y + 2*font_height;
									rectangles.get(i).new_method_rectangle.width = rectangles.get(i).width;
									rectangles.get(i).new_method_rectangle.height = font_height;
								}
								else {rectangle = null;}

					//                System.out.println("Size of rectangles: " + rectangles.size());
							//System.out.println("Repainting from...");
								repaint();
								mousepressed = false;
							}
							else
							{
								file_opened = false;
							}
						} // if !rectangle_selected
						if (rectangle_resized)
						{
							//
							// Resize Orectangle
							//
							// TODO: Hier moet even kritisch naar gekeken worden
							// Kan het rectangle niet gewoon geresized worden i.p.v. opnieuw aangemaakt te worden?
							//
							int index_resized_rectangle = rectangles.indexOf(rectangle_to_resize);
				/*                        rectangle.select();
							rectangle.setName(((Orectangle) rectangles.get(index_resized_rectangle)).getName());
							rectangle.addMethods(((Orectangle) rectangles.get(index_resized_rectangle)).getMethods());
							rectangles.remove(index_resized_rectangle);
							rectangles.add(rectangle);
				*/
							int old_x = rectangles.get(index_resized_rectangle).x;
							int old_y = rectangles.get(index_resized_rectangle).y;
							rectangles.get(index_resized_rectangle).x = rectangle.x;
							rectangles.get(index_resized_rectangle).y = rectangle.y;
							rectangles.get(index_resized_rectangle).width = rectangle.width;
							rectangles.get(index_resized_rectangle).height = rectangle.height;
							int j = 0;
							Point current_location;
							//
							// Adjust position and size of method rectangles
							//
							while (j < ((Orectangle) rectangles.get(index_resized_rectangle)).getMethods().size())
							{
								current_location = new Point(((Method) rectangles.get(index_resized_rectangle).getMethods().get(j)).method_rectangle.getLocation());
								// Set position of method rectangle
								((Method) rectangles.get(index_resized_rectangle).getMethods().get(j)).method_rectangle.setLocation (
									(int) current_location.getX() + rectangle.x - old_x
									, (int) current_location.getY() + rectangle.y - old_y);
								// And set width
								((Method) rectangles.get(index_resized_rectangle).getMethods().get(j)).method_rectangle.width = rectangle.width;
								j++;
							}

							// Adjust position of new_method_rectangle
							current_location = new Point(rectangles.get(index_resized_rectangle).new_method_rectangle.getLocation());
							rectangles.get(index_resized_rectangle).new_method_rectangle.setLocation((int) current_location.getX() + rectangle.x - old_x
								, (int) current_location.getY() + rectangle.y - old_y);
							// and set width
							rectangles.get(index_resized_rectangle).new_method_rectangle.width = rectangle.width;


							corner_resized = 0;
							rectangle_resized = false;
							repaint();
						}
					} // if !rectangle_highlighted
					else
					{
						// Rectangle is highlighted
						//
						setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						System.out.println("Rectagle highlighted, setting default cursor");

						//  Select or deselect some rectangle
						int i = 0;
						while (i < rectangles.size())
						{
							//
							// If the mouse pointer is in some rectangle
							//
							if (rectangles.get(i).contains(me.getPoint()))
							{
								System.out.println("Rectangle " + i + " contains point");
								System.out.println("rectangle_moved is: " + rectangle_moved);
								if (!rectangle_moved)
									if (!rectangles.get(i).isselected)
								
			//				if (!(rectangles.get(i).isselected && !rectangle_moved))
								{
									System.out.println("Selecting rectangle " + i);
									rectangles.get(i).select();
									rectangle_selected = true;
									getPanel().object_name_field.setVisible(false);
									getPanel().method_name_field.setVisible(false);
									requestFocus();
									//System.out.println("Sent focus request");
									// Set cursor to normal

									// TODO: if cursor is in some method, select that method
									int j = 0;
									while (j < rectangles.get(i).getMethods().size())
									{
										if (((Method) rectangles.get(i).getMethods().get(j)).method_rectangle.contains(me.getPoint()))
										{
											((Method) rectangles.get(i).getMethods().get(j)).selected = true;
											is_method_selected = true;
											selected_method = (Method) rectangles.get(i).getMethods().get(j);
											System.out.println("Selected method: " + selected_method.method_name);
										}
										j++;
									}
								}
				//				else if (rectangles.get(i).isselected && !rectangle_moved)
								else
								// Trying to edit name or add method
								//
								// TODO: iets minder strakke voorwaarde anders kunnen we nooit de objectname invullen
								//
								{
									int j = 0;
									// TODO: Toch moet deze boolean ergens op false worden gezet, anders kan de rectangle
									// niet meer worden verwijderd.
									is_method_selected = false;
									System.out.println("Trying to edit name or add method");
									Insets insets = getInsets();
									// Are we trying to add a new method?
									if (((Orectangle) rectangles.get(i)).new_method_rectangle.contains(me.getPoint()))
									{
										//
										// Adding new method
										//
										adding_method = true;
										g = getGraphics();
										font_height = g.getFontMetrics().getHeight();
										method_name_field.setBounds(rectangles.get(i).new_method_rectangle.x + insets.left + rectangles.LEFT_MARGIN + 1,
											rectangles.get(i).new_method_rectangle.y + insets.top,
											(rectangles.get(i).width < 20) ? 20 : rectangles.get(i).width - 2*rectangles.LEFT_MARGIN,
											20);
										getPanel().method_name_field.setVisible(true);
										getPanel().method_name_field.requestFocus();
										rectangles.get(i).edited = true;

										System.out.println("This is the moment to add a new method");
									}
									else
									{
										// Select method, if it wasn't selected, otherwise edit it
										// Editing existing method

										while (j < rectangles.get(i).getMethods().size())
										{
											if (((Method) rectangles.get(i).getMethods().get(j)).method_rectangle.contains(me.getPoint()))
											{
												if (((Method) rectangles.get(i).getMethods().get(j)).selected)
												{
													// Method is selected, so edit method name
													is_method_selected = true;
													System.out.println("Trying to edit method, is_method_selected: " + is_method_selected);
													g = getGraphics();
													font_height = g.getFontMetrics().getHeight();

													System.out.println("Font height: " + font_height);
													getPanel().object_name_field.setVisible(false);
													//
													// Alternative: look at position of the method_rectangle !! instead of this complicated calc.
													method_name_field.setBounds(rectangles.get(i).x + insets.left + rectangles.LEFT_MARGIN + 1,
																rectangles.get(i).y + insets.top + (j + 2)*font_height,
																(rectangles.get(i).width < 20 ? 20 :rectangles.get(i).width - 2*rectangles.LEFT_MARGIN),
																20);
													//getPanel().add(object_name_field);
													//
													//
													if (rectangles.get(i).methods.size() != 0)
														getPanel().method_name_field.setText(selected_method.method_name);
														//System.out.println("Got name: " + ((Orectangle) rectangles.get(i)).name);
													getPanel().method_name_field.setVisible(true);
													getPanel().method_name_field.requestFocus();
													getPanel().method_name_field.repaint();
													rectangles.get(i).edited = true;

												}
												else
												{
													// Select method
													((Method) rectangles.get(i).getMethods().get(j)).selected = true;
													is_method_selected = true;
													selected_method = (Method) rectangles.get(i).getMethods().get(j);
													System.out.println("Selected method: " + selected_method.method_name);
													method_name_field.setVisible(false);
													object_name_field.setVisible(false);
												}
											}
											else
											{
												if (designer.key_pressed != ctrl_key)
													((Method) rectangles.get(i).getMethods().get(j)).selected = false;
											}
											j++;
										} //while-loop

										if (!is_method_selected)
										{
											System.out.println("Going to edit object name");
											getPanel().method_name_field.setVisible(false);
											// Edit object name
											// TODO: iets zit hier niet goed
											// ben niet zeker van de 75 en 20
											//System.out.println("rectangles.UPPER_MARGIN: " + rectangles.UPPER_MARGIN);
											if (me.getPoint().y <  rectangles.get(i).y + rectangles.UPPER_MARGIN + 20)
											{
												object_name_field.setBounds(rectangles.get(i).x + insets.left + rectangles.LEFT_MARGIN + 1,
														rectangles.get(i).y + insets.top + rectangles.UPPER_MARGIN + 1,
														(rectangles.get(i).width) < 20 ? 20 :rectangles.get(i).width - 2*rectangles.LEFT_MARGIN,  // breedte van rectangle - 2*marges
														20); // Deze 20 moet worden: font_height + marges
												//getPanel().add(object_name_field);
												getPanel().object_name_field.setText(((Orectangle) rectangles.get(i)).name);
												//System.out.println("Got name: " + ((Orectangle) rectangles.get(i)).name);
												getPanel().object_name_field.setVisible(true);
												getPanel().object_name_field.requestFocus();
												rectangles.get(i).edited = true;
												System.out.println("Editing object name");
											}
										} // if !is_method_selected
									}
								}
								else // Rectangle moved
								{
									System.out.println("Rectangle moved");
									System.out.println("Rectangle_selected: " + rectangle_selected);
									rectangles.get(i).edited = false;
									getPanel().object_name_field.setVisible(false);
									getPanel().method_name_field.setVisible(false);
									requestFocus();
									System.out.println("Setting rectangle_moved to false");
									getPanel().rectangle_moved = false;
								}
							}
							else // The mouse pointer is not in any rectangle
								if (designer.key_pressed != ctrl_key)
								{
									// deselect other rectangles
									rectangles.get(i).unselect();
									rectangles.get(i).edited = false;
								}

							i++;
						} // while-loop ?
//                    System.out.println("Repainting from MouseReleased");
						repaint();
					} 
					/*
					else
					{
					System.out.println("From the else statement, rectangle_highlighted: " + rectangle_highlighted);
					}
					*/
					break;
				case (Designer.DM_COMPOSITION):
				case Designer.DM_RELATIONAL:
					if (isAssociationDrawn)
					{
					
			//			System.out.println("mouse released while association.size: " + associationDrawn.size());
						Orectangle toClass;
						toClass = rectangles.contains(me.getPoint());
						//   if mouse pointer in other rectangle than fromClass rectangle
						if (toClass != null)
						{
							if (!(toClass.equals(associationDrawn.getFromClass())))
							{
								associationDrawn.setToClass(toClass);
								startingPoint = me.getPoint();
								associationDrawn.add(startingPoint);
								//      take this rectangle as an end relationship
								// If the association consists of only 2 points, calculate projection point, because
								// it hasn't been done yet. 
								if (associationDrawn.size() == 2)
								{
									startingPoint = associationDrawn.getFromClass().hits(associationDrawn.get(0), associationDrawn.get(1));
									associationDrawn.set(0, startingPoint);
									toClass.addAssociationTo(associationDrawn);
								}
								// Project end point on toClass rectangle too
								startingPoint = toClass.hits(associationDrawn.get(0),associationDrawn.get(associationDrawn.size() - 1));
								associationDrawn.set(associationDrawn.size() - 1, startingPoint);

								isAssociationDrawn = false;
								currentLine = null;
								System.out.println("To Class is: " + toClass.getName());
								repaint();
								
								//      Let's try to NOT wait for mouse release to commit this relationship
							}
							else // (toClass.equals(associationDrawn.getFromClass()))
							{
								System.out.println("Relation to itself: ignore");
							}
						}
						else /* mouse not in other rectangle, i.e. toClass = null */
						//      set a point, store the first part of the line 
						//                   and continue drawing a line from the last set point
						//
						{
							System.out.println("User releases mouse outside rectangle, association.size: " + associationDrawn.size());
							//
							// If user releases mouse at the starting point, obviously don't add any line
							//
							if (!startingPoint.equals(me.getPoint()))
							{
								startingPoint = me.getPoint();
								associationDrawn.add(startingPoint);
								currentLine = new Line(startingPoint, startingPoint);
								System.out.println("Association drawn! Size: " + associationDrawn.size());
								if (associationDrawn.size() == 2)
								{
									/* The first line part of the association will be drawn */
									/* Let out the part that's inside a rectangle */
									//
									// this method will calculate the new startingPoint
									System.out.println("New starting point will be calculated for: (" + associationDrawn.get(0).x + "," + associationDrawn.get(0).y + ") and (" + associationDrawn.get(1).x + "," + associationDrawn.get(1).y + ").");
									startingPoint = associationDrawn.getFromClass().hits(associationDrawn.get(0), associationDrawn.get(1));
									associationDrawn.set(0, startingPoint);

								}
								repaint();
							}
						}
					}
					else if (pointToAdd != null)
					// Add point to line
					{
						System.out.println("Association before adding point:");
						for (int k = 0; k < activeAssociation.size(); k++)
							System.out.println("  point: " + activeAssociation.get(k));
						activeAssociation.insertElementAt(activeAssociation.getPointToAddIndex(), pointToAdd);
						System.out.println("Association after adding point");
						for (int k = 0; k < activeAssociation.size(); k++)
							System.out.println("  point: " + activeAssociation.get(k));

						pointToAdd = null;
						projectionPoint = null;
					} // end or adding point
					else if (pointToRemoveIndex != -1)
					{
						activeAssociation.remove(pointToRemoveIndex);
					}
					break;
				} // switch statement
		
            }
            else if (me.getModifiers() == me.BUTTON2_MASK)
            {
                //System.out.println("Button 2 pressed\n");
            }
            else if (me.getModifiers() == me.BUTTON3_MASK)
            {
                //System.out.println("Button 3 pressed\n");
                // Show popupmenu if rectangle highlighted or selected
                int i = 0;
                while (i < rectangles.size())
                {
                    if ((rectangles.get(i).isselected)
                        || (rectangles.get(i).highlighted))
                    {
                        // TODO: Move this code to the point where the rectangle is
                        // selected and enable/disable other menu items like deleteMenuItem
                        //
                        if (!(rectangles.get(i).isselected))
                        {
                            //designer.popupdeleteMenuItem.setEnabled(false);
                            rectangles.get(i).select();
                            //repaint();
                        }
                        else
                        {
                            designer.popupdeleteMenuItem.setEnabled(true);
                        }
                        if (rectangles.get(i).name != null)
                        {
                            designer.propertywindow.setTitle(rectangles.get(i).name);
                        }
                        designer.propertymenu.show(designer.drawpane, me.getPoint().x, me.getPoint().y);
                    }
                    i++;
                }
                repaint();
            }
            last_point = me.getPoint();
        }

        public void mouseEntered(MouseEvent me)
        {
//            System.out.println("Mouse entered, design mode: " + designer.getDesignMode() + "\n");
			if ((designer.getDesignMode() == Designer.DM_COMPOSITION) ||
			(designer.getDesignMode() == Designer.DM_INHERITANCE) ||
			(designer.getDesignMode() == Designer.DM_RELATIONAL))
			{
//		System.out.println("Setting cursor to crosshair");
				setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}
        }

        public void mouseExited(MouseEvent me)
        {
//            System.out.println("Mouse exited, design mode: " + designer.getDesignMode() + "\n");
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

        public void mouseDragged(MouseEvent me)
        {
            int i = 0;
            if ((me.getModifiers() & me.BUTTON1_MASK) == me.BUTTON1_MASK)
            {

				switch (designer.getDesignMode())
				{
				case (Designer.DM_CLASS):
				
					if (!rectangle_highlighted && !rectangle_resized)
					{
						// Draw new rectangle
						//System.out.println("Mouse dragged && !rectangle_highlighted\n");
						end_point = new Point(me.getPoint());
						old_rectangle = rectangle;
						rectangle = new Orectangle(font_height, (startingPoint.x < end_point.x) ? startingPoint.x : end_point.x
									, (startingPoint.y < end_point.y) ? startingPoint.y : end_point.y
									, (end_point.x > startingPoint.x) ? end_point.x - startingPoint.x : startingPoint.x - end_point.x
									, (end_point.y > startingPoint.y) ? end_point.y - startingPoint.y : startingPoint.y - end_point.y);
						repaint();
						if (unselecting)
						{
							unselecting = false;
						}
					}
					else if (rectangle_resized)
					{
						// Resize rectangle
						//System.out.println("Going to resize rectangle");
						end_point = new Point(me.getPoint());
						old_rectangle = rectangle;
						rectangle = new Orectangle(font_height, (startingPoint.x < end_point.x) ? startingPoint.x : end_point.x
									, (startingPoint.y < end_point.y) ? startingPoint.y : end_point.y
									, (end_point.x > startingPoint.x) ? end_point.x - startingPoint.x : startingPoint.x - end_point.x
									, (end_point.y > startingPoint.y) ? end_point.y - startingPoint.y : startingPoint.y - end_point.y);
						repaint();
					}
					else
					{
					// Move highlighted rectangle
					while (i < rectangles.size())
					{
						if (((Orectangle) rectangles.get(i)).highlighted)
						{
						Point current_location = new Point(((Orectangle) rectangles.get(i)).getLocation());
						//System.out.println("Current location: " + current_location);
						Point new_location = new Point();
						new_location.setLocation(current_location.getX() +
										me.getPoint().getX() - last_point.getX()
										, current_location.getY() + me.getPoint().getY() - last_point.getY());
						//System.out.println("New location: " + new_location);
						old_rectangle = new Orectangle(font_height, ((Orectangle) rectangles.get(i)).x
										, ((Orectangle) rectangles.get(i)).y
										, ((Orectangle) rectangles.get(i)).width
										, ((Orectangle) rectangles.get(i)).height);
						//System.out.println("old_rectangle: " + old_rectangle);

						//current_location.setLocation(current_location.getX() - 1, current_location.getY() - 1);
						//System.out.println("Current location: " + current_location);

						//old_rectangle.setLocation(current_location);

						((Orectangle) rectangles.get(i)).setLocation(new_location);

						//
						// Move methods rectangles, if there are any
						//
						int j = 0;
						while (j < ((Orectangle) rectangles.get(i)).getMethods().size())
						{
							current_location = new Point(((Method) ((Orectangle) rectangles.get(i)).getMethods().get(j)).method_rectangle.getLocation());
							((Method) ((Orectangle) rectangles.get(i)).getMethods().get(j)).method_rectangle.setLocation ((int) (current_location.getX() + me.getPoint().getX() - last_point.getX())
													, (int) (current_location.getY() + me.getPoint().getY() - last_point.getY()));
							j++;
						}
						// Move the new_method_rectangle
						current_location = new Point(((Orectangle) rectangles.get(i)).new_method_rectangle.getLocation());
						((Orectangle) rectangles.get(i)).new_method_rectangle.setLocation(
							(int) (current_location.getX() + me.getPoint().getX() - last_point.getX())
							, (int) (current_location.getY() + me.getPoint().getY() - last_point.getY()));

						// Move all associations
						// Move 'from associations'
						for (j = 0; j < ((Orectangle) rectangles.get(i)).getAssociationsFrom().size(); j++)
						{
							((Association) ((Orectangle) rectangles.get(i)).getAssociationsFrom().get(j)).get(0).setLocation (
							((Association) ((Orectangle) rectangles.get(i)).getAssociationsFrom().get(j)).get(0).getX() +me.getPoint().getX() - last_point.getX()
							, ((Association) ((Orectangle) rectangles.get(i)).getAssociationsFrom().get(j)).get(0).getY() +me.getPoint().getY() - last_point.getY());
						}
						// Move 'to associations'
						int size = ((Orectangle) rectangles.get(i)).getAssociationsTo().size();
						int associationSize;
						for (j = 0; j < size; j++)
						{
							associationSize = ((Association) ((Orectangle) rectangles.get(i)).getAssociationsTo().get(j)).size();
							((Association) ((Orectangle) rectangles.get(i)).getAssociationsTo().get(j)).get(associationSize - 1).setLocation (
							((Association) ((Orectangle) rectangles.get(i)).getAssociationsTo().get(j)).get(associationSize - 1).getX() +me.getPoint().getX() - last_point.getX()
							, ((Association) ((Orectangle) rectangles.get(i)).getAssociationsTo().get(j)).get(associationSize - 1).getY() +me.getPoint().getY() - last_point.getY());
						}



						rectangle = (Orectangle) rectangles.get(i);
						last_point = me.getPoint();
						rectangle_moved = true;
						//System.out.println("old_rectangle(2): " + old_rectangle);
			//                            repaint();
						//super.paintImmediately();
						//paintImmediately();
						}
						i++;
					}
					repaint();
					}
					break;
				case (Designer.DM_COMPOSITION):
				case Designer.DM_RELATIONAL:
					if (isAssociationDrawn)
					{
						currentLine.setEndPoint(me.getPoint());
						repaint();
					}
					else /* Dragging association */
					{
			//			System.out.println("Drawing association");
						for (i = 0; i < associations.size(); i++)
						{
							if (associations.get(i).isSelected())
							{
								// Association selected, move the highlighted point
								if( associations.get(i).getHighlightedPoint() != -1)
								{
									Point p = associations.get(i).get(associations.get(i).getHighlightedPoint());
									if ((associations.get(i).getHighlightedPoint() != 0) 
										&& (associations.get(i).getHighlightedPoint() != associations.get(i).size() - 1))
									{
										p.setLocation (p.getX() + me.getPoint().getX() - last_point.getX()
											, p.getY() + me.getPoint().getY() - last_point.getY());
										// Suggest line merge if near line coalesce
										mergeLine = new Line(associations.get(i).get(associations.get(i).getHighlightedPoint() - 1)
												, associations.get(i).get(associations.get(i).getHighlightedPoint() + 1));
										Point tmpProjectionPoint = mergeLine.getProjectionPoint(p);
										int ALLOWED_DISTANCE = 100;
										if (tmpProjectionPoint != null)
										{
											if (!(((tmpProjectionPoint.x - p.x)*(tmpProjectionPoint.x - p.x)
											+ (tmpProjectionPoint.y - p.y)*(tmpProjectionPoint.y - p.y)) < ALLOWED_DISTANCE))
											{
												mergeLine = null;
												pointToRemoveIndex = -1;
											}
											else
											{
												pointToRemoveIndex = associations.get(i).getHighlightedPoint();
												activeAssociation = associations.get(i);
											}
										}
										else
										{
											mergeLine = null;
											pointToRemoveIndex = -1;
										}
									}
									else
									{
										// First point or last point dragging
										// Don't let it escape from the rectangles side
										System.out.println("first or last point draggin");
										Orectangle theRectangle;
										if (associations.get(i).getHighlightedPoint() == 0)
										{
											theRectangle = associations.get(i).getFromClass();
											System.out.println("At first point: rectangle: " + theRectangle.getName());
										}
										else
										{
											theRectangle = associations.get(i).getToClass();
											System.out.println("At last point, rectangle: " + theRectangle.getName());
										}
										if (theRectangle.isAtHorizontalSide(p))
										{
											int newx = p.x + me.getPoint().x - last_point.x;
											// Don't let the point run out
											if (theRectangle.isAtHorizontalSide(new Point(newx, p.y)))
												p.setLocation(newx, p.getY());

										}
										if (theRectangle.isAtVerticalSide(p))
										{
											int newy = p.y + me.getPoint().y - last_point.y;
											// Don't let the point run out
											if (theRectangle.isAtVerticalSide(new Point(p.x, newy)))
												p.setLocation(p.getX(), newy);
										}

									}
									last_point = me.getPoint();
									repaint();

									break; //for-loop ?
								}
							}
						} //for-loop
					}
					break;
				} // switch statement
            } // if statement
        }

        public void mouseMoved(MouseEvent me)
        {
            int i = 0;
            last_point = me.getPoint();
            //System.out.println("Mouse moved\n");

			// For the line
			if (currentLine != null)
			{
				currentLine.setEndPoint(last_point);
			}

            rectangle_highlighted = false;
            boolean rectangle_changed = false; // if some rectangle changed from hilite to no-hilite
			isAssociationHighlighted = false;
            //rectangle_selected = false;
            //System.out.println("rectangle_selected: " + rectangle_selected);
            while (i < rectangles.size() + associations.size())
            {
				if (i < rectangles.size())
				{
					//System.out.println("isatcorner: " + ((Orectangle) rectangles.get(i)).isatcorner(me.getPoint()));
					if (((Orectangle) rectangles.get(i)).contains(me.getPoint()) &&
						(((Orectangle) rectangles.get(i)).isatcorner(me.getPoint()) == 0 ||
						rectangle_selected == false))
					{
						((Orectangle) rectangles.get(i)).highlight();
						// Check here if there is any method made active
						if (((Orectangle) rectangles.get(i)).new_method_rectangle.contains(me.getPoint()))
						// TODO: highlight new_method_rectangle
						{
							((Orectangle) rectangles.get(i)).new_m_rectangle_highlighted = true;
						}
						else
						{
							((Orectangle) rectangles.get(i)).new_m_rectangle_highlighted = false;
						}
						// Check if existing method rectangle highlighted
						int j = 0;
						while (j < ((Orectangle) rectangles.get(i)).getMethods().size())
						{
							if (((Method) ((Orectangle) rectangles.get(i)).getMethods().get(j)).method_rectangle.contains(me.getPoint()))
								((Method) ((Orectangle) rectangles.get(i)).getMethods().get(j)).highlighted = true;
							else
								((Method) ((Orectangle) rectangles.get(i)).getMethods().get(j)).highlighted = false;
							j++;
						}


						//System.out.println("Rectangle " + i++ + " highlighted\n");
						rectangle_highlighted = true;
				//    Don't change cursor when moving only, change it when user clicks
				//                    if (!rectangle_selected)
				//                        setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
						rectangle_resized = false;
						rectangle_changed = true;
						break;

					}
					else if ((corner_resized = ((Orectangle) rectangles.get(i)).isatcorner(me.getPoint())) > 0 && (((Orectangle) rectangles.get(i)).isselected))
					{
						if (((Orectangle) rectangles.get(i)).isatcorner(me.getPoint()) == 1)
						{
							setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
						}
						else if (((Orectangle) rectangles.get(i)).isatcorner(me.getPoint()) == 2)
						{
							setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
						}
						else if (((Orectangle) rectangles.get(i)).isatcorner(me.getPoint()) == 3)
						{
							setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
						}
						else if (((Orectangle) rectangles.get(i)).isatcorner(me.getPoint()) == 4)
						{
							setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
						}
						rectangle_resized = true;
						rectangle_to_resize = (Orectangle) rectangles.get(i);
						break;
					}
					else
					{
						((Orectangle) rectangles.get(i)).dehighlight();
						//
						// TODO: dehighlight all method rectangles
						//
						((Orectangle) rectangles.get(i)).new_m_rectangle_highlighted = false;
						int j = 0;
						while (j < ((Orectangle) rectangles.get(i)).getMethods().size())
						{
							((Method) ((Orectangle) rectangles.get(i)).getMethods().get(j)).highlighted = false;
							j++;
						}
						if (designer.getDesignMode() == Designer.DM_CLASS)
							setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

						//System.out.println("Rectangle " + i++ + " dehighlighted, setting default cursor\n");
				
						rectangle_resized = false;
						rectangle_changed = true;
					}
				}
				else /* Now check the associations */
				{
					int index = i - rectangles.size();
					
					if (index > -1 && !isAssociationDrawn)
					{
						/*
						 * isAtPoint gives the index to the point which should be highlighted
						 */
						int pointToHighLight = associations.get(index).isAtPoint(me.getPoint());
						if ( pointToHighLight != -1)
						{
							System.out.println("Association " + index + " point " + pointToHighLight);
							associations.get(index).highlight(pointToHighLight);
							isAssociationHighlighted = true;
						}
						else
						{
							// Check if pointer is near some line
							// this method also sets the pointToAddIndex 
							projectionPoint = associations.get(index).isNearLine(me.getPoint());
							if (projectionPoint != null)
							{
				//				System.out.println("Found projection Point: (" + projectionPoint.x + ","
				//					+ projectionPoint.y + ")");
								activeAssociation = associations.get(index);
								break;
							}
							associations.get(index).dehighlight();
							activeAssociation = null;
						}
					}
				}
                i++;
            } // while-loop
            if (rectangle_changed)
            {

                repaint();
            }
// Inserted this at 6-6-2001
//        repaint();
			requestFocus();
        }

    } //class MyMouseListener

    public void showPropertyWindow(Association a)
    {
		System.out.println("Going to show properties for association: " + a);
		associationPropertyWindow.setData(getRectangles(), getAssociations());
		associationPropertyWindow.show();

    }

    public void paint(Graphics g)
    {
        //System.out.println("DrawPane.paint() called");
        super.paint(g);
    }


    public void paintComponent(Graphics g)
    {
        //System.out.println("DrawPane.paintComponent() called");
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        paintdrawpane(g2);

// Cut from here
// Cut to this point

        // TODO: if edit-textfield visible, request focus
        if (getPanel().method_name_field.isVisible())
        {
            getPanel().method_name_field.requestFocus();
            getPanel().method_name_field.repaint();
            //System.out.println("repainting: Requesting focus: method_name_field");
        }
        if (getPanel().object_name_field.isVisible())
        {
            getPanel().object_name_field.requestFocus();
            getPanel().object_name_field.repaint();
            //System.out.println("Requesting focus: object_name_field");
        }

    }

    public int print(Graphics g, PageFormat pf, int pi) throws PrinterException
    {
        if (pi >= 1)
        {
            return Printable.NO_SUCH_PAGE;
        }

        paintdrawpane((Graphics2D) g);

        return Printable.PAGE_EXISTS;
    }
 
		public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize(); //new Dimension(30,30);
    }
		
		public boolean getScrollableTracksViewportHeight()
		{
				return false;
		}

		public boolean getScrollableTracksViewportWidth()
		{
				return false;
		}

		public int getScrollableUnitIncrement(Rectangle visibleRect,
                                      int orientation,
                                      int direction)
		{
				return 1;
		}


		public int getScrollableBlockIncrement(Rectangle visibleRect,
                                       int orientation,
                                       int direction)
		{
				return 20;
		}
			
    public void paintdrawpane(Graphics2D g2)
    {

        BasicStroke normalStroke, dashed_stroke, thickStroke;

        int MARK_WIDTH = 2;

        float dash_array[] = {(float) 1.0,(float) 1.0};

        normalStroke = new BasicStroke();
        dashed_stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, (float) 1.0, dash_array, (float) 0.0);
        thickStroke = new BasicStroke(2);

        int i = 0;
        g2.setBackground(Color.white);


		if (designer.showJPEGSize == true)
		{
			g2.setStroke(dashed_stroke);
			g2.drawRect(0, 0, designer.jpegWidth, designer.jpegHeight);
			g2.setStroke(normalStroke);
		}
	
		// Draw the current line

		if (currentLine != null)
		{
			g2.setColor(Color.blue);
			g2.drawLine(currentLine.getStartPoint().x, currentLine.getStartPoint().y
				, currentLine.getEndPoint().x, currentLine.getEndPoint().y);
		}

		if (projectionPoint != null)
		{
			// Hightlight projectionPoint
			g2.setColor(Color.blue);
			g2.drawRect(projectionPoint.x - 2, projectionPoint.y - 2, 4, 4);
		}

		if (mergeLine != null)
		{
			g2.setColor(Color.blue);
			g2.drawLine(mergeLine.getStartPoint().x, mergeLine.getStartPoint().y
				, mergeLine.getEndPoint().x, mergeLine.getEndPoint().y);
		}

		// Draw the associations

		for (i = 0; i < associations.size(); i++)
		{
			g2.setColor(Color.black);
			Association association = (Association) associations.get(i);
			Point associationStartPoint = new Point();
			Point diamondCenter = new Point();

			diamondCenter.setLocation(association.get(0).getX(), association.get(0).getY());

			// Draw first line
			if (association.size() > 1)
			{
				if (association.getType() == Designer.DM_COMPOSITION)
				{
		//		    if (association.getDirection() == Association.EAST)
					if (association.getFromClass().isAtRightSide(association.get(0)))
					{
						associationStartPoint.setLocation(association.get(0).getX() + 2*diamondSize
										, association.get(0).getY());
						diamondCenter.setLocation(association.get(0).getX() + diamondSize
										, association.get(0).getY());
					}
		//		    else if (association.getDirection() == Association.SOUTH)
					if (association.getFromClass().isAtBottomSide(association.get(0)))
					{
						associationStartPoint.setLocation(association.get(0).getX()
										, association.get(0).getY() + 2*diamondSize);
						diamondCenter.setLocation(association.get(0).getX()
										, association.get(0).getY() + diamondSize);
					}
		//		    else if (association.getDirection() == Association.WEST)
					else if (association.getFromClass().isAtLeftSide(association.get(0)))
					{
						associationStartPoint.setLocation(association.get(0).getX() - 2*diamondSize
										, association.get(0).getY());
						diamondCenter.setLocation(association.get(0).getX() - diamondSize
										, association.get(0).getY());
					}
		//		    else if (association.getDirection() == Association.NORTH)
					if (association.getFromClass().isAtTopSide(association.get(0)))
					{
						associationStartPoint.setLocation(association.get(0).getX()
										, association.get(0).getY() - 2*diamondSize);
						diamondCenter.setLocation(association.get(0).getX()
										, association.get(0).getY() - diamondSize);
					}
				}
				else if (association.getType() == Designer.DM_RELATIONAL)
				{
					associationStartPoint.setLocation(association.get(0).getX()
										, association.get(0).getY());
				}
				if (association.highlighted())
					g2.setStroke(thickStroke);
				g2.drawLine(associationStartPoint.x, associationStartPoint.y
					, association.get(1).x, association.get(1).y);
				g2.setStroke(normalStroke);
			} // if association.size() > 1

			if (association.getType() == Designer.DM_COMPOSITION)
			{
				// Draw the diamond
				if (association.highlighted())
					g2.setStroke(thickStroke);
				g2.draw(new Diamond(new Point[]{new Point(diamondCenter.x - diamondSize, diamondCenter.y)
								, new Point(diamondCenter.x, diamondCenter.y + diamondSize)
								, new Point(diamondCenter.x + diamondSize, diamondCenter.y)
								, new Point(diamondCenter.x, diamondCenter.y - diamondSize)}));
				g2.setStroke(normalStroke);
			}
			else if (association.getType() == Designer.DM_RELATIONAL)
			{
				// Draw the craw's foot
				if (association.getFromClass().isAtLeftSide(association.get(0)))
				{
					g2.drawLine(association.get(0).x, association.get(0).y + 2*diamondSize 
							, association.get(0).x - 2*diamondSize, association.get(0).y);
					g2.drawLine(association.get(0).x, association.get(0).y - 2*diamondSize
							, association.get(0).x - 2*diamondSize, association.get(0).y);
				}
				else if (association.getFromClass().isAtRightSide(association.get(0)))
				{
					g2.drawLine(association.get(0).x, association.get(0).y + 2*diamondSize 
							, association.get(0).x + 2*diamondSize, association.get(0).y);
					g2.drawLine(association.get(0).x, association.get(0).y - 2*diamondSize
							, association.get(0).x + 2*diamondSize, association.get(0).y);
				}
				else if (association.getFromClass().isAtTopSide(association.get(0)))
				{
					g2.drawLine(association.get(0).x - 2*diamondSize, association.get(0).y
							, association.get(0).x, association.get(0).y - 2*diamondSize);
					g2.drawLine(association.get(0).x + 2*diamondSize, association.get(0).y
							, association.get(0).x, association.get(0).y - 2*diamondSize);
				}
				else if (association.getFromClass().isAtBottomSide(association.get(0)))
				{
					g2.drawLine(association.get(0).x - 2*diamondSize, association.get(0).y
							, association.get(0).x, association.get(0).y + 2*diamondSize);
					g2.drawLine(association.get(0).x + 2*diamondSize, association.get(0).y
							, association.get(0).x, association.get(0).y + 2*diamondSize);
				}
			}
			
			for (int j = 2; j < association.size(); j++)
			{
				if (association.highlighted())
				{
					g2.setStroke(thickStroke);
				}
				else
				{
					g2.setStroke(normalStroke);
				}
				// Draw rectangles in the 'knees' if the association is selected
				if ( association.isSelected())
					g2.drawRect(((Point) association.get(j)).x - 2, ((Point) association.get(j)).y - 2, 
						4, 4);
				if (association.getHighlightedPoint() == j)
					g2.fillRect(((Point) association.get(j)).x - 2, ((Point) association.get(j)).y - 2, 
									5, 5);
					
				g2.drawLine(((Point) association.get(j - 1)).x, ((Point) association.get(j - 1)).y
					, ((Point) association.get(j)).x, ((Point) association.get(j)).y);
			}

			// Draw rectangle in the first knee, if there is one
			if (association.size() > 1)
			{
				if (association.isSelected())
					g2.drawRect(((Point) association.get(1)).x - 2, ((Point) association.get(1)).y - 2, 
						4, 4);
				if (association.getHighlightedPoint() == 1)
					g2.fillRect(((Point) association.get(1)).x - 2, ((Point) association.get(1)).y - 2, 
					5, 5);
			} // for-loop j
		
		} // for-loop i
		i = 0;
	
        if (mousepressed)
        {
            g2.setColor(Color.blue);
            //g2.setStroke(dashed_stroke);
        }
        else
        {
            //g.setColor(Color.black);
            g2.setStroke(normalStroke);
        }
        if (old_rectangle != null)
        {
            g2.clearRect(old_rectangle.x - 1
                    , old_rectangle.y - 1
                    , old_rectangle.width + 2
                    , old_rectangle.height + 2);
//            System.out.println("Cleared rectangle: " + old_rectangle);
        }
        //rectangles.add(rectangle);
        //System.out.println("rectangles: " + rectangles);
        if (rectangle != null)
        {
/*            g.drawRect(rectangle.x
                , rectangle.y
                , rectangle.width
                , rectangle.height);
*/
            g2.draw( rectangle);
//            System.out.println("Drew rectangle: " + rectangle);
        }
        //old_rectangle = rectangle;
        g2.setColor(Color.black);
        g2.setStroke(normalStroke);
        while(rectangles != null && i < rectangles.size())
        {
            g2.setStroke(normalStroke);
            g2.setClip(    ((Orectangle) rectangles.get(i)).x - 1
                        , ((Orectangle) rectangles.get(i)).y - 1
                        , ((Orectangle) rectangles.get(i)).width + 2
                        , ((Orectangle) rectangles.get(i)).height + 2)
                        ;
            //g2.drawString("label", ((Orectangle) rectangles.get(i)).x, ((Orectangle) rectangles.get(i)).y + g2.getFontMetrics().getHeight());
            if (((Orectangle) rectangles.get(i)).name != null)
            {
                //System.out.println("Name of object: " + ((Orectangle) rectangles.get(i)).name);
                // draw name of object
                g2.drawString(((Orectangle) rectangles.get(i)).name
                , ((Orectangle) rectangles.get(i)).x + rectangles.LEFT_MARGIN
                , ((Orectangle) rectangles.get(i)).y + g2.getFontMetrics().getHeight() + rectangles.UPPER_MARGIN);
                // draw methods
                g2.drawLine(((Orectangle) rectangles.get(i)).x
                            , ((Orectangle) rectangles.get(i)).y + g2.getFontMetrics().getHeight() + 2*rectangles.UPPER_MARGIN
                            , ((Orectangle) rectangles.get(i)).x + ((Orectangle) rectangles.get(i)).width
                            , ((Orectangle) rectangles.get(i)).y + g2.getFontMetrics().getHeight() + 2*rectangles.UPPER_MARGIN);
                if (((Orectangle) rectangles.get(i)).new_m_rectangle_highlighted)
                {
                    g2.setBackground(Color.yellow);
                    g2.clearRect(((Orectangle) rectangles.get(i)).new_method_rectangle.x
                                , ((Orectangle) rectangles.get(i)).new_method_rectangle.y
                                , ((Orectangle) rectangles.get(i)).new_method_rectangle.width
                                , ((Orectangle) rectangles.get(i)).new_method_rectangle.height);
                    g2.setBackground(Color.white);
                }
                int j = 0;
                // Draw methods
                while (j < ((Orectangle) rectangles.get(i)).methods.size())
                {
                    // Draw method rectangle
                    if (((Method) ((Orectangle) rectangles.get(i)).methods.get(j)).method_rectangle != null)
                    {
                        if (((Method) ((Orectangle) rectangles.get(i)).methods.get(j)).selected)
                        {
                            g2.setBackground(Color.cyan);
                        }
                        else if (((Method) ((Orectangle) rectangles.get(i)).methods.get(j)).highlighted)
                        {
                            g2.setBackground(Color.yellow);
                        }
                        else
                        {
                            g2.setBackground(Color.white);
                        }
                        //g2.setClip(((Method) ((Orectangle) rectangles.get(i)).methods.get(j)).method_rectangle);
                        g2.clearRect(((Method) ((Orectangle) rectangles.get(i)).methods.get(j)).method_rectangle.x /* + ((Orectangle) rectangles.get(i)).x */
                                    , ((Method) ((Orectangle) rectangles.get(i)).methods.get(j)).method_rectangle.y /* + ((Orectangle) rectangles.get(i)).y */
                                    , ((Method) ((Orectangle) rectangles.get(i)).methods.get(j)).method_rectangle.width
                                    , ((Method) ((Orectangle) rectangles.get(i)).methods.get(j)).method_rectangle.height);

                    }
                    g2.drawString(((Method) ((Orectangle) rectangles.get(i)).methods.get(j)).getMethodName()
                    , ((Orectangle) rectangles.get(i)).x + rectangles.LEFT_MARGIN
                    , ((Orectangle) rectangles.get(i)).y + (j + 2)*g2.getFontMetrics().getHeight() + 3*rectangles.UPPER_MARGIN);
                    j++;
                }
            }
            if (((Orectangle) rectangles.get(i)).highlighted)
            {
//                g.setColor(Color.blue);
                g2.setStroke(thickStroke);
            }
            else
            {
//                g.setColor(Color.black);
                g2.setStroke(normalStroke);
            }
            if (((Orectangle) rectangles.get(i)).isselected)
            {
                // set clip larger to display full corners
                // Mark corners of rectangle
                g2.setClip( ((Orectangle) rectangles.get(i)).x - 1 - MARK_WIDTH
                        , ((Orectangle) rectangles.get(i)).y - 1 - MARK_WIDTH
                        , ((Orectangle) rectangles.get(i)).width + 2 + 2*MARK_WIDTH
                        , ((Orectangle) rectangles.get(i)).height + 2 + 2*MARK_WIDTH);
                g2.drawRect (((Orectangle) rectangles.get(i)).x - 1
                            , ((Orectangle) rectangles.get(i)).y - 1
                            , MARK_WIDTH
                            , MARK_WIDTH);
                g2.drawRect (((Orectangle) rectangles.get(i)).x - 1 + ((Orectangle) rectangles.get(i)).width
                            , ((Orectangle) rectangles.get(i)).y - 1
                            , MARK_WIDTH
                            , MARK_WIDTH);
                g2.drawRect (((Orectangle) rectangles.get(i)).x - 1
                            , ((Orectangle) rectangles.get(i)).y - 1 + ((Orectangle) rectangles.get(i)).height
                            , MARK_WIDTH
                            , MARK_WIDTH);
                g2.drawRect (((Orectangle) rectangles.get(i)).x - 1 + ((Orectangle) rectangles.get(i)).width
                            , ((Orectangle) rectangles.get(i)).y - 1 + ((Orectangle) rectangles.get(i)).height
                            , MARK_WIDTH
                            , MARK_WIDTH);
                //System.out.println("Drew selected rectangle: " + ((Orectangle) rectangles.get(i)));
            }
/*            g.drawRect(((Orectangle) rectangles.get(i)).x
                        , ((Orectangle) rectangles.get(i)).y
                        , ((Orectangle) rectangles.get(i)).width
                        , ((Orectangle) rectangles.get(i++)).height);
*/
            g2.draw( (Orectangle) rectangles.get(i++));
        }
    }
}

