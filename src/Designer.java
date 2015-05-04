import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.print.*;
import java.util.*;
import java.io.*;
import javax.swing.tree.*;

// XML
import javax.xml.parsers.*;
import org.w3c.dom.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

// JPG
import java.awt.image.*;
/* Uitgesterd vanwege migratie naar Java 7 */
//import com.sun.image.codec.jpeg.JPEGImageEncoder;
//import com.sun.image.codec.jpeg.JPEGCodec;
/* Added line below for migration to Java 7 */
import javax.imageio.ImageIO;

public class Designer extends JFrame
{
    // Design mode
    public static final int DM_CLASS = 1;
    public static final int DM_COMPOSITION = 2;
    public static final int DM_INHERITANCE = 3;
	public static final int DM_RELATIONAL = 4;
    
    int key_pressed;
    public static int delete_key = 127;
    boolean showJPEGSize = false;
    int jpegWidth = 400;
    int jpegHeight = 400;

    int design_mode = DM_CLASS;

    DrawPane drawpane;
    JPanel contentPane; // This will contain both the buttonbar and the drawpane
    JSplitPane classpane;
	JScrollPane scrollDrawpane;
    PropertyWindow propertywindow;
	OptionsWindow  optionsWindow;
    JPanel classbuttonpane;
    JButton okbutton, cancelbutton, savetextbutton;
    JMenuBar menubar;
    ButtonBar buttonBar;
    JMenu filemenu, editmenu, showMenu, toolsMenu;
    JMenuItem fileopenMenuItem
			, fileopenXMLMenuItem
		    , filesaveMenuItem
		    , filesaveasMenuItem
		    , filesavexmlMenuItem
		    , filesavejavaMenuItem
		    , filesavejpgMenuItem
		    , fileprintMenuItem
		    , quitMenuItem
		    , deleteMenuItem
		    , clearallMenuItem
		    , propertiesMenuItem
		    , popupdeleteMenuItem
		    , popuppropertiesMenuItem
		    , fileopen1MenuItem
		    , showJPEGSizeMenuItem	  
				, optionsMenuItem;
    JPopupMenu propertymenu;
    Rectangles rectangles = new Rectangles();
    String filename;
    ArrayList recent_files;

    JTree classtree;
    DefaultTreeModel classtm;
    //JEditorPane editorpane;
    JTextArea editorpane;
    Method current_method;
    boolean is_method_selected = false;

    Orectangle current_rectangle;
    String current_class_name;
    String current_directory = "c:\\home\\java\\Designer3\\";
    DefaultMutableTreeNode rootnode, classnode;

    public Designer()
    {
		fileopenMenuItem = new JMenuItem("Open");
		fileopenMenuItem.setMnemonic(KeyEvent.VK_O);
		fileopenXMLMenuItem = new JMenuItem("Open as XML");
		fileopenXMLMenuItem.setMnemonic(KeyEvent.VK_L);
		filesaveMenuItem = new JMenuItem("Save");
		filesaveMenuItem.setMnemonic(KeyEvent.VK_S);
		filesaveasMenuItem = new JMenuItem("Save as...");
		filesaveasMenuItem.setMnemonic(KeyEvent.VK_A);
		filesavexmlMenuItem = new JMenuItem("Save as XML");
		filesavexmlMenuItem.setMnemonic(KeyEvent.VK_X);
		filesavejavaMenuItem = new JMenuItem("Save java");
		filesavejavaMenuItem.setMnemonic(KeyEvent.VK_J);
		filesavejpgMenuItem = new JMenuItem("Save jpg");
		fileprintMenuItem = new JMenuItem("Print");
		fileprintMenuItem.setMnemonic(KeyEvent.VK_P);
		quitMenuItem = new JMenuItem("Quit");
		quitMenuItem.setMnemonic(KeyEvent.VK_Q);
		fileopen1MenuItem = new JMenuItem("1 ");
		fileopen1MenuItem.setMnemonic(KeyEvent.VK_1);
		fileopen1MenuItem.setVisible(false);

		deleteMenuItem = new JMenuItem("Delete");
		deleteMenuItem.setMnemonic(KeyEvent.VK_D);
		clearallMenuItem = new JMenuItem("Clear all");
		clearallMenuItem.setMnemonic(KeyEvent.VK_A);
		propertiesMenuItem = new JMenuItem("Properties");
		propertiesMenuItem.setMnemonic(KeyEvent.VK_P);
		popupdeleteMenuItem = new JMenuItem("Delete");
		popupdeleteMenuItem.setMnemonic(KeyEvent.VK_D);
		popuppropertiesMenuItem = new JMenuItem("Properties");
		popuppropertiesMenuItem.setMnemonic(KeyEvent.VK_P);

		showJPEGSizeMenuItem = new JMenuItem("JPEG Size");

		optionsMenuItem = new JMenuItem("Options");
		optionsMenuItem.setMnemonic(KeyEvent.VK_O);

		fileopenMenuItem.addActionListener(new FileopenMenuHandler());
		fileopenXMLMenuItem.addActionListener(new FileopenXMLMenuHandler());
		filesaveMenuItem.addActionListener(new FilesaveMenuHandler());
		filesaveasMenuItem.addActionListener(new FilesaveasMenuHandler());
		filesavexmlMenuItem.addActionListener(new FilesavexmlMenuHandler());
		filesavejavaMenuItem.addActionListener(new FilesavejavaMenuHandler());
		filesavejpgMenuItem.addActionListener(new FilesavejpgMenuHandler());
		fileprintMenuItem.addActionListener(new FileprintMenuHandler());
		fileopen1MenuItem.addActionListener(new Fileopen1MenuHandler());
		quitMenuItem.addActionListener(new QuitMenuHandler());
		deleteMenuItem.addActionListener(new DeleteMenuHandler());
		clearallMenuItem.addActionListener(new ClearAllMenuHandler());
		propertiesMenuItem.addActionListener(new PropertiesMenuHandler());
		popupdeleteMenuItem.addActionListener(new DeleteMenuHandler());
		popuppropertiesMenuItem.addActionListener(new PropertiesMenuHandler());
		showJPEGSizeMenuItem.addActionListener(new ShowJPEGSizeMenuHandler());
		optionsMenuItem.addActionListener(new OptionsMenuHandler());

		fileopenMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
		filesaveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
		fileprintMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK));
		quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.CTRL_MASK));

		filemenu = new JMenu("File");
		filemenu.setMnemonic(KeyEvent.VK_F);
		filemenu.add(fileopenMenuItem);
		filemenu.add(fileopenXMLMenuItem);
		filemenu.add(filesaveMenuItem);
		filemenu.add(filesaveasMenuItem);
		filemenu.add(filesavejavaMenuItem);
		filemenu.add(filesavexmlMenuItem);
		filemenu.add(filesavejpgMenuItem);
		filemenu.add(fileprintMenuItem);
		filemenu.add(quitMenuItem);
		filemenu.addSeparator();
		filemenu.add(fileopen1MenuItem);


		editmenu = new JMenu("Edit");
		editmenu.setMnemonic(KeyEvent.VK_E);
		editmenu.add(deleteMenuItem);
		editmenu.add(clearallMenuItem);
		editmenu.addSeparator();
		editmenu.add(propertiesMenuItem);

		showMenu = new JMenu("Show");
		showMenu.setMnemonic(KeyEvent.VK_S);
		showMenu.add(showJPEGSizeMenuItem);

		toolsMenu = new JMenu("Tools");
		toolsMenu.setMnemonic(KeyEvent.VK_T);
		toolsMenu.add(optionsMenuItem);

		propertymenu = new JPopupMenu();
		propertymenu.add(popupdeleteMenuItem);
		propertymenu.add(popuppropertiesMenuItem);

		menubar = new JMenuBar();
		menubar.add(filemenu);
		menubar.add(editmenu);
		menubar.add(showMenu);
		menubar.add(toolsMenu);
		setBackground(Color.white);

		getContentPane().add(menubar, BorderLayout.NORTH);
		
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		getContentPane().add(contentPane, BorderLayout.CENTER);

		buttonBar = new ButtonBar(this);
		contentPane.add(buttonBar, BorderLayout.NORTH);

		drawpane = new DrawPane(this);
		// Add scrollbars 2004-10-11, Scrollbars not working yet
		scrollDrawpane = new JScrollPane(drawpane
//				, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
				, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
				, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//				, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//		scrollDrawpane.setPreferredSize(new Dimension(70, 70));
//		setPreferredSize(new Dimension(70, 70));
		contentPane.add(scrollDrawpane);
		// End of code for scrollbars
		drawpane.requestFocus();
		propertywindow = new PropertyWindow(this);
		optionsWindow = new OptionsWindow(this);
		
// This is disabled, because we want to see the scrollpane
//		contentPane.add(drawpane);

		editorpane = new JTextArea();
		editorpane.setTabSize(2);

		rootnode = new DefaultMutableTreeNode();
		classtm = new DefaultTreeModel(rootnode);

		classtree = new JTree(classtm);
		classtree.addTreeSelectionListener(new MyTreeSelectionListener());
	//	classpane.add(classtree);
		classpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, classtree, editorpane);
		classpane.setBounds(100,100,300,300);
		classpane.setDividerLocation(150);
		//Provide minimum sizes for the two components in the split pane
		Dimension minimumSize = new Dimension(100, 50);
		classtree.setMinimumSize(minimumSize);
		editorpane.setMinimumSize(minimumSize);

		//classpane.setOneTouchExpandable(true);
		//classpane.show();
		classbuttonpane = new JPanel();

		MyKeyListener myKeyListener = new MyKeyListener();
		addWindowListener(new WinClosing());
	//	addKeyListener(myKeyListener);

		okbutton = new JButton("OK");
		savetextbutton = new JButton("Save");
		cancelbutton = new JButton("Cancel");

	//	propertywindow.setModal(true);
	//	classpane.setOpaque(true);
	//	propertywindow.addWindowListener(new CloseProperties());

		classbuttonpane.add(okbutton);
		classbuttonpane.add(savetextbutton);
		classbuttonpane.add(cancelbutton);

		classbuttonpane.setLayout(new FlowLayout());

		// settings for propertywindow
		propertywindow.setSize(200, 200);

		propertywindow.getContentPane().add(classpane, BorderLayout.CENTER);
		propertywindow.getContentPane().add(classbuttonpane, BorderLayout.SOUTH);
		okbutton.addActionListener(new OKPressed());
		savetextbutton.addActionListener(new SaveTextPressed());
		cancelbutton.addActionListener(new CancelPressed());

		// settings for optionswindow
//		optionsWindow.setSize(200, 200);

	//      AWTEventListener 
	/*
			getToolkit().addAWTEventListener(
			new AWTEventListener() {
				public void eventDispatched(AWTEvent e) {
				System.out.println(e+"\n");
				}
			},
			AWTEvent.MOUSE_EVENT_MASK | 
				AWTEvent.FOCUS_EVENT_MASK
		);
	*/    
	//		propertywindow.repaint();
		//pack();
		//classtm = new JDefaultTreeModel();
		setTitle("Designer");
		setBounds(100, 100, 300, 300);
		setVisible(true);
    }

    public Rectangles getRectangles()
    {
		return rectangles;
    }

	public Dimension getJPEGSize()
	{
		return new Dimension(jpegWidth, jpegHeight);
	}

	public void setJPEGSize(Dimension d)
	{
		jpegWidth = new Double(d.getWidth()).intValue();
		jpegHeight = new Double(d.getHeight()).intValue();
	}

    public void showpropertywindow()
    {
//	current_class_name = new String(drawpane.getActiveClass());
//	if (current_class_name == "")
	if (drawpane.getActiveClass().equals(""))
	{
	    Association selectedAssociation = drawpane.getActiveAssociation();
	    if (selectedAssociation != null)
	    {
		drawpane.showPropertyWindow(selectedAssociation);
		return;
	    }
	    else
		System.out.println("No class or association selected\n");
	}
	else
	{
		current_class_name = drawpane.getActiveClass();
	    System.out.println("Classname: " + current_class_name + "\n");
	    rootnode = new DefaultMutableTreeNode(current_class_name);
	    classtm.setRoot(rootnode);
	    int i = 0;
	    while (i < drawpane.getActiveRectangle().methods.size())
	    {
		classnode = new DefaultMutableTreeNode(((Method) drawpane.getActiveRectangle().methods.get(i)).getMethodName());
		rootnode.add(classnode);
		i++;
	    }
	    classtree.expandRow(0);
	}
	editorpane.setText("Class " + current_class_name + "\nMethods" + drawpane.getActiveRectangle().getMethods() + " \n");
//	System.out.println("Going to repaint\n");
//	propertywindow.repaint();
//	System.out.println("Issued repaint\n");
	editorpane.setVisible(true);
	propertywindow.setVisible(true);
	//repaint();
	//propertywindow.getContentPane().setVisible(true);
    }
/*
	public void paint(Graphics g)
	{
		//propertywindow.update(g);
		//getContentPane().repaint();
		//super.paint(g);
	}
*/
	public void showOptionsWindow()
	{
		optionsWindow.setData(drawpane.getPreferredSize(), this.getJPEGSize());

		optionsWindow.setVisible(true);
	}

	public void setDrawPaneSize(Dimension d)
	{
		drawpane.setPreferredSize(d);
		// not yet quite the commands, sometimes repaint does not occur until window is resized
		scrollDrawpane.revalidate();
		scrollDrawpane.repaint();
	}

	public void hideOptionsWindow()
	{
		optionsWindow.setVisible(false);
	}
		

    class FileopenMenuHandler implements ActionListener
    {
		public void actionPerformed (ActionEvent ae)
		{
			System.out.println("Opening file\n");
			openfile();
		}
    }

	class FileopenXMLMenuHandler implements ActionListener
	{
		public void actionPerformed (ActionEvent ae)
		{
			openxmlfile();
		}
	}

    class FilesaveMenuHandler implements ActionListener
    {
		public void actionPerformed (ActionEvent ae)
		{
			System.out.println("Saving file\n");
			savefile();
		}
    }

    class FilesaveasMenuHandler implements ActionListener
    {
		public void actionPerformed (ActionEvent ae)
		{
			System.out.println("Saving file\n");
			savefileas();
		}
    }
    class FilesavexmlMenuHandler implements ActionListener
    {
		public void actionPerformed (ActionEvent ae)
		{
			try
			{
				savexmlfile();
			}
			catch (Exception e)
			{
//				System.err.println("Caught exception while writint xmlfile: " + e.getMessage());
				e.printStackTrace();
			}
		}
    }

    class FilesavejavaMenuHandler implements ActionListener
    {
		public void actionPerformed (ActionEvent ae)
		{
			savejavafile();
		}
    }

    class FilesavejpgMenuHandler implements ActionListener
    {
	public void actionPerformed (ActionEvent ae)
	{
	    savejpgfile();
	}
    }

    class FileprintMenuHandler implements ActionListener
    {
	public void actionPerformed (ActionEvent ae)
	{
	    printfile();
	}
    }

    class QuitMenuHandler implements ActionListener
    {
	public void actionPerformed (ActionEvent ae)
	{
	    System.exit(0);
	}
    }

    class Fileopen1MenuHandler implements ActionListener
    {
	public void actionPerformed (ActionEvent ae)
	{
	    openfile(filename);
	}
    }

    class DeleteMenuHandler implements ActionListener
    {
	public void actionPerformed (ActionEvent ae)
	{
	    drawpane.delete_rectangles();
	}
    }

    class ClearAllMenuHandler implements ActionListener
    {
	public void actionPerformed (ActionEvent ae)
	{
	    rectangles.clear();
	    drawpane.associations.clear();
	    drawpane.old_rectangle = null;
	    drawpane.rectangle = null;
	    drawpane.object_name_field.setVisible(false);
	    drawpane.currentLine = null;
	    drawpane.requestFocus();
	    drawpane.repaint();
	}
    }
    
    class ShowJPEGSizeMenuHandler implements ActionListener
    {
	public void actionPerformed (ActionEvent ae)
	{
	    if (showJPEGSize == true)
	    {
		showJPEGSize = false;
	    }
	    else
	    {
		showJPEGSize = true;
	    }
	    drawpane.repaint();

	}
    }

	class OptionsMenuHandler implements ActionListener
	{
		public void actionPerformed (ActionEvent ae)
		{
			showOptionsWindow();
		}
	}

    class PropertiesMenuHandler implements ActionListener
    {
	    public void actionPerformed (ActionEvent ae)
	    {
		    showpropertywindow();
		    //repaint();
	    }
    }

    class OKPressed implements ActionListener
    {
	public void actionPerformed (ActionEvent ae)
	{
	    propertywindow.setVisible(false);
//	    repaint();
	//propertywindow.dispose();
	//drawpane.setVisible(true);
	}
    }

    class SaveTextPressed implements ActionListener
    {
	public void actionPerformed (ActionEvent ae)
	{
	    // save method text
	    if (is_method_selected)
	    {
		    current_method.setMethodText(editorpane.getText());
	    }
	    else
	    {
		    // save object_text
		    current_rectangle.object_text = new String(editorpane.getText());
	    }
	}
    }

    class CancelPressed implements ActionListener
    {
	public void actionPerformed (ActionEvent ae)
	{
	    propertywindow.setVisible(false);
	    //repaint();
	    //propertywindow.dispose();
	    //drawpane.setVisible(true);
	}
    }

    public void savefile()
    {
	if (filename != null)
	{
	    try
	    {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(current_directory + filename));
		oos.writeObject(rectangles);
		oos.writeObject(drawpane.getAssociations());
		oos.close();
	    }
	    catch (IOException ioe)
	    {
		System.out.println("Caught IOException: " + ioe);
	    }
	    drawpane.repaint();
	}
	else
	{
	    savefileas();
	}
    }

    public void savefileas()
    {
		//System.out.println("Saving file in drawpane\n");
		FileDialog fd = new FileDialog(this,"Save");
		fd.setMode(FileDialog.SAVE);
		fd.setSize(400, 300);
		fd.setDirectory(current_directory);
		fd.show();

		filename = fd.getFile();
		current_directory = fd.getDirectory();
		//System.out.println("current directory: " + current_directory);
		if (filename != null)
		{
			try
			{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(current_directory + filename));
			oos.writeObject(rectangles);
			oos.writeObject(drawpane.getAssociations());
			oos.close();
			}
			catch (IOException ioe)
			{
			System.out.println("Caught IOException: " + ioe);
			}
			fileopen1MenuItem.setText("1 " + current_directory + filename);
			fileopen1MenuItem.setVisible(true);
		}
		drawpane.repaint();
    }

    public void savexmlfile() throws Exception
    {
		FileDialog fd = new FileDialog(this, "Save as XML");
		fd.setMode(FileDialog.SAVE);
		fd.setSize(400, 300);
		fd.setDirectory(current_directory);
		fd.show();

		String xmlfilename = fd.getFile();
		String xmlfileDirectory = fd.getDirectory();

		if (xmlfilename != null)
		{
			System.out.println("Writing file: " + xmlfileDirectory + xmlfilename);
			// build XML
			//
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();

//			Node rootNode = document.getDocumentElement();
			Node designerNode = document.createElement("designer");
			document.appendChild(designerNode);

			Node rectanglesNode = document.createElement("rectangle_set");
			designerNode.appendChild(rectanglesNode);

			for (int i = 0; i < rectangles.size(); i++)
			{
				Element rectangleElement = document.createElement("rectangle");
				rectangleElement.setAttribute("name", ((Orectangle) rectangles.get(i)).name );
				rectanglesNode.appendChild(rectangleElement);

				Element xposition = document.createElement("xposition");
				Element yposition = document.createElement("yposition");
				Element width = document.createElement("width");
				Element height = document.createElement("height");

				Text xpositionText = document.createTextNode(String.valueOf( rectangles.get(i).x));
				Text ypositionText = document.createTextNode(String.valueOf( rectangles.get(i).y));
				Text widthText = document.createTextNode(String.valueOf( rectangles.get(i).width));
				Text heightText = document.createTextNode(String.valueOf( rectangles.get(i).height));

				xposition.appendChild(xpositionText);
				yposition.appendChild(ypositionText);
				width.appendChild(widthText);
				height.appendChild(heightText);

				rectangleElement.appendChild(xposition);
				rectangleElement.appendChild(yposition);
				rectangleElement.appendChild(height);
				rectangleElement.appendChild(width);
			}

			Node associationsNode = document.createElement("association_set");
			designerNode.appendChild(associationsNode);

			Associations associations = drawpane.getAssociations();
			System.out.println("number of associations: " + associations.size());

			for (int i = 0; i < associations.size(); i++)
			{
				Association association = associations.get(i);
				Element associationElement = document.createElement("association");
				associationElement.setAttribute("fromClass", association.getFromClass().getName());
				associationElement.setAttribute("toClass", association.getToClass().getName());
				associationElement.setAttribute("type", new Integer(association.type).toString());
				associationsNode.appendChild(associationElement);

				Node pointsNode = document.createElement("point_set");
				associationElement.appendChild(pointsNode);
				
				for (int j = 0; j < association.size(); j++)
				{
					Element pointElement = document.createElement("point");
					pointsNode.appendChild(pointElement);
					
					Element xElement = document.createElement("x");
					Text xText = document.createTextNode(new Double(association.get(j).getX()).toString());
					xElement.appendChild(xText);
					Element yElement = document.createElement("y");
					Text yText = document.createTextNode(new Double(association.get(j).getY()).toString());
					yElement.appendChild(yText);
					System.out.println("Found point: (" + association.get(j).getX() + ", " + association.get(j).getY() + ")");

					pointElement.appendChild(xElement);
					pointElement.appendChild(yElement);
				}
			}
			
			// write file
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			transformer.setOutputProperty("method", "xml");
			transformer.setOutputProperty("indent", "yes");
 
			DOMSource source = new DOMSource(document);
			
			File xmlfile = new File(xmlfileDirectory + xmlfilename);
			FileOutputStream os = new FileOutputStream(xmlfile);
			StreamResult result = new StreamResult(os);
			transformer.transform(source, result);
		}
	}

/*
    public void savexmlfile()
    {
	FileDialog fd = new FileDialog(this,"Save as XML");
	fd.setMode(FileDialog.SAVE);
	fd.setSize(400, 300);
	fd.setDirectory(current_directory);
	fd.show();

	String xmlfilename = fd.getFile();
	if (xmlfilename != null)
	{
	    try
	    {
		FileWriter fw = new FileWriter(xmlfilename);
		int i = 0;
		fw.write("<XML version=\"1.0\">\n");
		while (i < rectangles.size())
		{
		    if ((((Orectangle) rectangles.get(i)).name) != null)
		    {
			fw.write("<CLASS name=\"" + ((Orectangle) rectangles.get(i)).name + "\">\n");
			fw.write("\t" + ((Orectangle) rectangles.get(i)).name + "\n");
			int j = 0;
			while (j < ((Orectangle) rectangles.get(i)).getMethods().size())
			{
				fw.write("<METHOD name=\"" + ((Method) ((Orectangle) rectangles.get(i)).getMethods().get(j)).method_name + "\">\n");
				fw.write("\t" + ((Method) ((Orectangle) rectangles.get(i)).getMethods().get(j)).method_text + "\n");
				fw.write("</METHOD>\n");
				j++;
			}
			    fw.write("</CLASS>\n");
		    }
//		else
//			{
//				System.out.println("Name was null\n");
//			}
//
		    i++;
		}
		fw.write("</XML>");
		fw.close();
	    }
	    catch (IOException ioe)
	    {
		    System.out.println("Caught IOException: " + ioe);
	    }
	    drawpane.repaint();
	}
    }
*/
    public void savejavafile()
    {
		int i = 0; int j = 0;
		while (i < rectangles.size())
		{
			try
			{
			FileWriter fw = new FileWriter(((Orectangle) rectangles.get(i)).name + ".java");
			fw.write(((Orectangle) rectangles.get(i)).object_text + "\n{");
			j = 0;
			while (j < ((Orectangle) rectangles.get(i)).methods.size())
			{
				// TODO: Put a \t in front of every line of method_text
				fw.write("\n" + ((Method) ((Orectangle) rectangles.get(i)).methods.get(j)).method_text + "\n");
				j++;
			}
			fw.write("}\n");

			fw.close();
			}
			catch (IOException ioe)
			{
			System.out.println("Caught IOException: " + ioe);
			}
			i++;
		}
    }

    public void savejpgfile()
    {
		try
		{
			// TODO: show file dialog and let user choose name and path for file
			OutputStream outputStream = new FileOutputStream("sample.jpg");
			BufferedImage outImage = new BufferedImage(jpegWidth, jpegHeight,
							 BufferedImage.TYPE_INT_RGB);

			// Debug, show properties
			System.out.println("BufferedImage properties: ");
			String prop[] = outImage.getPropertyNames();
			if (prop != null) for (int i = 0; i < prop.length; i++)
			{
			System.out.println(prop[i] + ": " + outImage.getProperty(prop[i]));
			}
			System.out.println("End of properties.");
			
			// Paint image.
				Graphics2D g2d = outImage.createGraphics();
	//            g2d.draw(new Rectangle(100, 100, 50, 50));
	//            g2d.dispose();
			g2d.setColor(Color.white);
				g2d.fillRect(0, 0, outImage.getWidth(), outImage.getHeight());
		
			drawpane.paintdrawpane(g2d);
			g2d.dispose();
/*		 Deze onderste drie regels zijn vanwege migratie naar Java 7 uitgesterd. 4 mei 2015 */
//			JPEGImageEncoder imageEncoder;
//			imageEncoder = JPEGCodec.createJPEGEncoder(outputStream);
//			imageEncoder.encode(outImage);
			ImageIO.write(outImage, "jpeg", outputStream);			
			outputStream.close();
		}
		catch (IOException ioe)
		{
			System.out.println("Caught IOException: " + ioe);
		}
	}

	public void openfile()
	{
//		System.out.println("Opening file in drawpane\n");
		FileDialog fd = new FileDialog(this, "Open file");
		fd.setMode(FileDialog.LOAD);
		fd.setSize(400, 300);
		fd.setDirectory(current_directory);
		fd.show();

		filename = fd.getFile();
		current_directory = fd.getDirectory();
		if (filename != null)
		{
			try
			{
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(current_directory + filename));
				try
				{
					rectangles.clear();
					drawpane.rectangle = null;
					drawpane.old_rectangle = null;
					rectangles = (Rectangles) ois.readObject();
					drawpane.setRectangles(rectangles);
					drawpane.setAssociations((Associations) ois.readObject());
					drawpane.file_opened = true;
					drawpane.object_name_field.setVisible(false);
					drawpane.requestFocus();
					// Check if some rectangle/method selected and set booleans accordingly
					int i = 0;
					while (i < rectangles.size())
					{
						if (((Orectangle) rectangles.get(i)).isselected)
						{
							drawpane.rectangle_selected = true;
							int j = 0;
							while (j < ((Orectangle) rectangles.get(i)).methods.size())
							{
								if (((Method) ((Orectangle) rectangles.get(i)).methods.get(j)).selected)
								{
									drawpane.is_method_selected = true;
									break;
								}
							j++;

							}
							break;
						}
						i++;
					}
				}
				catch (ClassNotFoundException cnfe)
				{
					System.out.println("Error: " + cnfe);
				}

				ois.close();
			}
			catch (IOException ioe)
			{
				System.out.println("Caught ioexception: " + ioe);
			}
			fileopen1MenuItem.setText("1 " + current_directory + filename);
			fileopen1MenuItem.setVisible(true);
			drawpane.repaint();
		}
    }

    public void openfile(String fname)
    {
        if (fname != null)
        {
            try
            {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(current_directory + fname));
                try
                {
                    rectangles.clear();
                    drawpane.rectangle = null;
                    drawpane.old_rectangle = null;
                    rectangles = (Rectangles) ois.readObject();
                    drawpane.setRectangles(rectangles);
					drawpane.setAssociations((Associations) ois.readObject());
                    drawpane.file_opened = true;
                    drawpane.object_name_field.setVisible(false);
                    drawpane.requestFocus();
                }
                catch (ClassNotFoundException cnfe)
                {
                    System.out.println("Error: " + cnfe);
                }

                ois.close();
            }
            catch (IOException ioe)
            {
                System.out.println("Caught ioexception: " + ioe);
            }
            drawpane.repaint();
        }
    }

	public void openxmlfile()
	{
		FileDialog fd = new FileDialog(this, "Open XML-file");
		fd.setMode(FileDialog.LOAD);
		fd.setSize(400, 300);
		fd.setDirectory(current_directory);
		fd.show();

		filename = fd.getFile();
		current_directory = fd.getDirectory();
		
		Associations associations = null;
		
		if (filename != null)
		{
			try
			{
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(current_directory + filename);

				Element rootElement = document.getDocumentElement();

				System.out.println("Root element name: " + rootElement.getNodeName());

				Node rectanglesetNode = rootElement.getFirstChild();
				System.out.println("Child node: " + rectanglesetNode.getNodeName());
				NodeList nodeList = rootElement.getElementsByTagName("rectangle");
				System.out.println("Found nodes: " + nodeList.getLength());
//				Rectangles rectangles = new Rectangles(); 
				rectangles.clear();
				for (int j = 0; j < nodeList.getLength(); j++)
				{
					Element rectangleElement = (Element) nodeList.item(j);
					
					System.out.println("Node: " + rectangleElement.getNodeName());
					System.out.println("name: " + rectangleElement.getAttribute("name"));
					System.out.println("xposition: " + ((Element) rectangleElement.getElementsByTagName("xposition").item(0)).getFirstChild());
					Orectangle orectangle = new Orectangle(16
							, new Integer(((Text) ((Element) rectangleElement.getElementsByTagName("xposition").item(0)).getFirstChild()).getData()).intValue()
							, new Integer(((Text) ((Element) rectangleElement.getElementsByTagName("yposition").item(0)).getFirstChild()).getData()).intValue()
							, new Integer(((Text) ((Element) rectangleElement.getElementsByTagName("width").item(0)).getFirstChild()).getData()).intValue()
							, new Integer(((Text) ((Element) rectangleElement.getElementsByTagName("height").item(0)).getFirstChild()).getData()).intValue());
					orectangle.setName(rectangleElement.getAttribute("name"));
					rectangles.add(orectangle);
				}
				
				Element associationsetElement = (Element) rootElement.getElementsByTagName("association_set").item(0);
				if (associationsetElement != null)
				{
					System.out.println("Associationset node: " + associationsetElement.getNodeName());
					// Process associations
					associations = new Associations(); 
					NodeList associationNodeList = associationsetElement.getElementsByTagName("association");
					for (int j = 0; j < associationNodeList.getLength(); j++)
					{
						Element associationElement = (Element) associationNodeList.item(j);
						String fromClassName = associationElement.getAttribute("fromClass");
						String toClassName = associationElement.getAttribute("toClass");
						System.out.println("Found association from " + fromClassName
								+ " to " + toClassName);
						Association association = new Association();
						association.type = new Integer(associationElement.getAttribute("type")).intValue();
						association.setFromClass(rectangles.getOrectangleByName(fromClassName));
						association.setToClass(rectangles.getOrectangleByName(toClassName));
						rectangles.getOrectangleByName(fromClassName).addAssociationFrom(association);
						rectangles.getOrectangleByName(toClassName).addAssociationTo(association);

						Element pointsElement = (Element) associationElement.getElementsByTagName("point_set").item(0);
						NodeList pointNodeList = pointsElement.getElementsByTagName("point");
						System.out.println("Found " + pointNodeList.getLength() + " points");

						for (int i = 0; i < pointNodeList.getLength(); i++)
						{
							Element pointElement = (Element) pointNodeList.item(i);
							Double x = new Double(((Text) ((Element) pointElement.getElementsByTagName("x").item(0)).getFirstChild()).getData());
							Double y = new Double(((Text) ((Element) pointElement.getElementsByTagName("y").item(0)).getFirstChild()).getData());

							// Add point to association
							association.add(new Point(x.intValue(), y.intValue()));
							System.out.println("Found point: (" + x.intValue() + ", " + y.intValue() + ")");
						}
								
						associations.add(association);
					} // for-loop over associationNodeList
				}
			}
			catch (Exception e)
			{
				System.out.println("Caught exception: " + e.getMessage());
				e.printStackTrace();
			}
			drawpane.rectangle = null;
			drawpane.old_rectangle = null;
			drawpane.setRectangles(rectangles);
			if (associations != null)
			{
				drawpane.setAssociations(associations);
			}
			drawpane.file_opened = true;

			drawpane.rectangle_selected = false;
			drawpane.is_method_selected = false;

			drawpane.object_name_field.setVisible(false);
			drawpane.requestFocus();
			drawpane.repaint();
		}
	}

    public void printfile()
    {
        System.out.println("Printing file");

		PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setPrintable(drawpane);
        if (printJob.printDialog())
        {
            try
            {
                printJob.print();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }


    public static void main(String args[])
    {
//        System.out.println("Hello Designer\n");
        Designer designer = new Designer();
		System.out.println("Returning from the main loop");
    }

    class WinClosing extends WindowAdapter
    {
        public void windowClosing(WindowEvent e)
        {
            System.exit(0);
        }
    }

    class CloseProperties extends WindowAdapter
    {
        public void CloseProperties(WindowEvent e)
        {
            propertywindow.setVisible(false);
            System.out.println("Cosing properties");
//            propertywindow.dispose();
//            drawpane.repaint();
        }
    }

    class MyKeyListener extends KeyAdapter
    {
        public void keyPressed (KeyEvent ke)
        {
            //key_pressed = ke.getKeyChar();
//            System.out.println("keyPressed\n");
            key_pressed = ke.getKeyCode();
            drawpane.setKeyPressed(key_pressed);
            System.out.println("Pressed key: " + key_pressed);
            if (key_pressed == delete_key)
            {
                drawpane.delete_rectangles();
                drawpane.repaint();
            }
//            System.out.println("Pressed key: " + ke.KEY_TYPED);
        }

        public void keyReleased (KeyEvent ke)
        {
//            System.out.println("Released key: " + ke.getKeyCode());
            key_pressed = 0;
            drawpane.setKeyPressed(key_pressed);
        }
/*
        public void keyTyped (KeyEvent ke)
        {
            key_pressed = ke.getKeyCode();
            System.out.println("Typed key: " + key_pressed);
        }
*/
    }

    class MyTreeSelectionListener implements TreeSelectionListener
    {
        public void valueChanged(TreeSelectionEvent e)
        {
//            System.out.println("TreeSelectionEvent: " + e);

        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
               classtree.getLastSelectedPathComponent();
//            System.out.println("Selected node: " + node);

        if (node == null) return;

        String selected_name = (String) node.getUserObject();
//            System.out.println("Selected method: " + selected_name);
        // get method text and put in the editor pane
//            System.out.println("Current_class_name: " + current_class_name);
        current_rectangle = rectangles.getOrectangleByName(current_class_name);
//            System.out.println("Current rectangle: " + current_rectangle);
            if (current_class_name.equals(selected_name))
            {
                System.out.println("Classname selected: " + current_class_name);
                editorpane.setText(current_rectangle.object_text);
                is_method_selected = false;
            }
            else
            {
            current_method = current_rectangle.getMethodByName(selected_name);
//                System.out.println("Selected method: " + current_method);
            if (current_method != null)
            {
                editorpane.setText(current_method.method_text);
                is_method_selected = true;
                }
            }
        }
    }

    public void setDesignMode(int design_mode)
    {
		this.design_mode = design_mode;
		System.out.println("Design mode set to: "  + this.design_mode);
    }

    public int getDesignMode()
    {
		return design_mode;
    }

}

class PropertyWindow extends JDialog
{
    Designer designer;

    public PropertyWindow(Designer d)
    {
        //super();
        this.designer = d;
        setResizable(true);
		setTitle("Properties for Object: ");
    }

/*    protected void paintComponent(Graphics g)
    {
        System.out.println("PropertyWindow.paintComponent()\n");
        this.designer.paintComponents(g);
        paintComponents(g);
        super.paintComponents(g);
    }
*/
    public void paint(Graphics g)
    {
//        Graphics2D g2 = (Graphics2D) g;
//        System.out.println("PropertyWindow.paint()\n");
        //this.designer.paintComponents(g);
        //this.designer.paintComponents(g2);
//        paintComponents(g);
//        paintComponents(g2);
//        super.paintComponents(g);
//        super.paintComponents(g2);
        super.paint(g);
    }
}

class OptionsWindow extends JDialog
{
	Designer designer;

	JPanel field1Panel = new JPanel();
	JPanel field2Panel = new JPanel();
	JPanel buttonPanel = new JPanel();

	JLabel sizeDrawpaneLabel = new JLabel("Size drawpane");
	JLabel xlabel = new JLabel("width: ");
	JLabel ylabel = new JLabel("height: ");
	JTextField drawpaneSizeXField = new JTextField(10);
	JTextField drawpaneSizeYField = new JTextField(10);

	JLabel jpegsizeLabel = new JLabel("Size JPEG");
	JPanel field3Panel = new JPanel();
	JPanel field4Panel = new JPanel();
	JLabel jpegxlabel = new JLabel("width jpeg: ");
	JLabel jpegylabel = new JLabel("height jpeg: ");
	JTextField jpegxField = new JTextField(10);
	JTextField jpegyField = new JTextField(10);

	JButton okButton = new JButton("OK");
	JButton cancelButton = new JButton("Cancel");

	public OptionsWindow(Designer d)
	{
		this.designer = d;
		drawpaneSizeXField.setHorizontalAlignment(JTextField.TRAILING);
		drawpaneSizeYField.setHorizontalAlignment(JTextField.TRAILING);
		jpegxField.setHorizontalAlignment(JTextField.TRAILING);
		jpegyField.setHorizontalAlignment(JTextField.TRAILING);
		field1Panel.setMaximumSize(new Dimension(400, 25));
		field1Panel.setLayout(new FlowLayout());
		field1Panel.add(xlabel);
		field1Panel.add(drawpaneSizeXField);
		field2Panel.setMaximumSize(new Dimension(400, 25));
		field2Panel.setLayout(new FlowLayout());
		field2Panel.add(ylabel);
		field2Panel.add(drawpaneSizeYField);
		field3Panel.setMaximumSize(new Dimension(400, 25));
		field3Panel.setLayout(new FlowLayout());
		field3Panel.add(jpegxlabel);
		field3Panel.add(jpegxField);
		field4Panel.setMaximumSize(new Dimension(400, 25));
		field4Panel.setLayout(new FlowLayout());
		field4Panel.add(jpegylabel);
		field4Panel.add(jpegyField);
		buttonPanel.setMaximumSize(new Dimension(400, 50));
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		add(sizeDrawpaneLabel);
		add(field1Panel);
		add(field2Panel);
		add(jpegsizeLabel);
		add(field3Panel);
		add(field4Panel);
		add(buttonPanel);
		setSize(400, 400);
		setResizable(false);
		setTitle("Options for Designer");

		okButton.addActionListener(new OKPressed());
		cancelButton.addActionListener(new CancelPressed());
	}

	public void paint(Graphics g)
	{
		super.paint(g);
	}

	public void setData(Dimension sizeDrawpane, Dimension jpegSize)
	{
		drawpaneSizeXField.setText(new Double(sizeDrawpane.getWidth()).toString());
		drawpaneSizeYField.setText(new Double(sizeDrawpane.getHeight()).toString());
		jpegxField.setText(new Double(jpegSize.getWidth()).toString());
		jpegyField.setText(new Double(jpegSize.getHeight()).toString());
	}
		

	class OKPressed implements ActionListener
	{
		public void actionPerformed(ActionEvent ae)
		{
			designer.setDrawPaneSize(new Dimension(new Double(drawpaneSizeXField.getText()).intValue()
												, new Double(drawpaneSizeYField.getText()).intValue() ) );
			designer.setJPEGSize(new Dimension(new Double(jpegxField.getText()).intValue()
												, new Double(jpegyField.getText()).intValue() ) );
			System.out.println("OK Pressed");
		}
	}

	class CancelPressed implements ActionListener
	{
		public void actionPerformed(ActionEvent ae)
		{
			designer.hideOptionsWindow();
			System.out.println("Cancel Pressed");
		}
	}
}
