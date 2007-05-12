import java.util.*;
import java.awt.*;
//import javax.swing.*;
import java.io.*;

class Orectangle extends Rectangle
{
    boolean isselected, highlighted;
    String name, object_text;
    boolean edited;
    ArrayList variables, methods;
    static int MINIMUMWIDTH = 10;
    static int MINIMUMHEIGHT = 10;
    // The new method rectangle highlights when user wants to insert new method by moving
    // mouse pointer to the bottom of the existing methods
    Rectangle new_method_rectangle;
    boolean new_m_rectangle_highlighted = false;
    int font_height;
    Vector associationsFrom, associationsTo;

    Orectangle(int f, int x, int y, int width, int height)
    {
        // TODO: We don't want to give font_height f this way
        // because it's not that dynamic
        // Changing font in the drawpane implies changing font height of each rectangle
        // :-(
        boolean isselected = false;
        boolean highlighted = false;
        edited = false;
        font_height = f;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        methods = new ArrayList();
        variables = new ArrayList();
        name = new String();
        new_method_rectangle = new Rectangle();
        //constructor_name = new String();
        associationsFrom = new Vector();
        associationsTo = new Vector();
    }

    public Line upperLine()
    {
		return new Line(new Point(x, y), new Point(x + width, y));
    }

    public Line rightLine()
    {
		return new Line(new Point(x + width, y), new Point(x + width, y + height));
    }

    public Line bottomLine()
    {
		return new Line(new Point(x, y + height), new Point(x + width, y + height));
    }

    public Line leftLine()
    {
		return new Line(new Point(x, y), new Point(x, y + height));
    }

    public void select()
    {
        isselected = true;
    }

    public void unselect()
    {
        isselected = false;
    }

    public void highlight()
    {
        highlighted = true;
    }

    public void dehighlight()
    {
        highlighted = false;
    }

    public void setLocation (double x, double y)
    {
        setLocation(x, y);
    }

    public Point getLocation()
    {
        return(new Point(x, y));
    }

    public void setName(String name)
    {
        //String old_constructor
        if (methods.isEmpty())
        {
            Method constructor = new Method(new String(name + " ( )"));
            constructor.setMethodText("public " + name + " ()" + "\n" +
                                "{" + "\n" +
                                "}");
            addMethod(constructor);
            //methods.add("hallo");
        }
        if (this.name == null)
        {
            object_text = new String("class " + name);
        }
        this.name = name;
		System.out.println("Set name in Orectangle: " + this.name);
    }

    public String getName()
    {
        return name;
    }

    public ArrayList getMethods()
    {
        return methods;
    }

    public Method getMethodByName(String method_name)
    {
        int i = 0;
        while (i < methods.size())
        {
            if (((Method) methods.get(i)).method_name.equals(method_name))
                return (Method) methods.get(i);
            i++;
        }
        return null;
    }

    public void addMethod(Method method)
    {
        System.out.println("Adding method");

        method.setMethodRectangle(new Rectangle(new_method_rectangle.x
                                                , new_method_rectangle.y
                                                , new_method_rectangle.width
                                                , new_method_rectangle.height));
        methods.add(method);
        System.out.println("Font height: " + font_height);
        new_method_rectangle.y += font_height;
    }

    public void addMethods(ArrayList methods)
    {
        // TODO: set location of new_method_rectangle
        this.methods = methods;
    }

    public int isatcorner(Point p)
    {
        int ALLOWED_DISTANCE = 26;

        //System.out.println("Location: " + getLocation());
        //System.out.println("Point: " + p);
        int distance_left_upper_corner = (int) (((int) (p.getX() - getLocation().getX()))*(((int) p.getX() - getLocation().getX())) +
            (((int) p.getY() - getLocation().getY()))*(((int) p.getY() - getLocation().getY())));
        //System.out.println("distance_left_corner: " + distance_left_corner);
        if ( distance_left_upper_corner < ALLOWED_DISTANCE)
            return 1;
        int distance_right_upper_corner = (int) (((int) (p.getX() - width - getLocation().getX()))*(((int) p.getX() - width - getLocation().getX())) +
            (((int) p.getY() - getLocation().getY()))*(((int) p.getY() - getLocation().getY())));
        //System.out.println("distance_left_corner: " + distance_left_corner);
        if ( distance_right_upper_corner < ALLOWED_DISTANCE)
            return 2;
        int distance_right_lower_corner = (int) (((int) (p.getX() - width - getLocation().getX()))*(((int) p.getX() - width - getLocation().getX())) +
            (((int) p.getY() - height - getLocation().getY()))*(((int) p.getY() - height - getLocation().getY())));
        //System.out.println("distance_left_corner: " + distance_left_corner);
        if ( distance_right_lower_corner < ALLOWED_DISTANCE)
            return 3;
        int distance_left_lower_corner = (int) (((int) (p.getX() - getLocation().getX()))*(((int) p.getX() - getLocation().getX())) +
            (((int) p.getY() - height - getLocation().getY()))*(((int) p.getY() - height - getLocation().getY())));
        //System.out.println("distance_left_corner: " + distance_left_corner);
        if ( distance_left_lower_corner < ALLOWED_DISTANCE)
            return 4;

        return 0;
    }

    public Point hits(Point p0, Point p1)
    {
        Point leftUpperCorner = new Point(x, y);
        Point rightUpperCorner = new Point(x + width, y);
        Point rightBottomCorner = new Point(x + width, y + height);
        Point leftBottomCorner = new Point(x, y + height);

        System.out.println("Method Orectangle.hits (x, y, width, height)" + x + "," + y + "," + width + ","+ height);

        Line theLine = new Line(p0, p1);
        Line side = new Line(leftUpperCorner, rightUpperCorner);
        Point hitPoint = side.hits(theLine);
        /* Does the line hit the upper face of the (fromClass) rectangle? */
        if (hitPoint != null)
        {
        System.out.println("Got hitpoint from upper face");
        return hitPoint;
        }
        else
        {
        side = new Line(rightUpperCorner, rightBottomCorner);
        hitPoint = side.hits(theLine);
        /* Does the line hit the right face of the (fromClass) rectangle? */
        if (hitPoint != null)
        {
            System.out.println("Got hitpoint from right face");
            return hitPoint;
        }
        else
        {
            side = new Line(leftBottomCorner, rightBottomCorner);
            hitPoint = side.hits(theLine);
            /* Does the line hit the bottom of the (fromClass) rectangle? */
            if (hitPoint != null)
            {
            System.out.println("Got hitpoint from bottom face");
            return hitPoint;
            }
            else
            {
            side = new Line(leftUpperCorner, leftBottomCorner);
            hitPoint = side.hits(theLine);
            /* Does the line hit the left side of the (fromClass) rectangle? */
            if (hitPoint != null)
            {
                System.out.println("Got hitpoint from left face");
                return hitPoint;
            }
            }
        }
        }
        return null;
    }

    public boolean isAtHorizontalSide(Point p)
    {
	if (((p.getY() == y) || (p.getY() == y + height))
	      && p.getX() >= x && p.getX() <= x + width)
	    return true;
	
	return false;
    }

    public boolean isAtVerticalSide(Point p)
    {
	if (((p.getX() == x) || (p.getX() == x + width))
	      && p.getY() >= y && p.getY() <= y + height)
	    return true;
	
	return false;
    }

    public boolean isAtLeftSide(Point p)
    {
		if (p.getX() == x
			&& p.getY() >= y && p.getY() <= y + height)
			return true;
					
		return false;
    }

    public boolean isAtRightSide(Point p)
    {
		if (p.getX() == x + width
			&& p.getY() >= y && p.getY() <= y + height)
			return true;
					
		return false;
    }

    public boolean isAtTopSide(Point p)
    {
		if (p.getY() == y
			&& p.getX() >= x && p.getX() <= x + width)
			return true;
					
		return false;
    }
				
    public boolean isAtBottomSide(Point p)
    {
		if (p.getY() == y + height
			&& p.getX() >= x && p.getX() <= x + width)
			return true;
					
		return false;
    }

    public void delete_selected_method()
    {
        int j = 0;
        while (j < methods.size())
        {
            if (((Method) methods.get(j)).selected)
            {
                methods.remove(j);
                // TODO: En verplaats de new_method_rectangle naar boven
                if (methods.size() != 0)
                {
                    new_method_rectangle.y -= font_height;
                }
            }
            j++;
        }
    }

    public void addAssociationFrom(Association a)
    {
        associationsFrom.add(a);
    }

    public Vector getAssociationsFrom()
    {
		return associationsFrom;
    }

    public void addAssociationTo(Association a)
    {
        associationsTo.add(a);
    }

    public Vector getAssociationsTo()
    {
		return associationsTo;
    }
}

class Method implements Serializable
{
    String method_name, method_text;
    Rectangle method_rectangle; // absolute position
    boolean highlighted = false;
    boolean selected = false;

    public Method(String method_name)
    {
        this.method_name = method_name;
        method_rectangle = new Rectangle();
    }

    public void setMethodText(String method_text)
    {
        this.method_text = method_text;
    }

    public void setMethodName(String method_name)
    {
        this.method_name = method_name;
    }

    public String getMethodName()
    {
        return method_name;
    }

    public void setMethodRectangle(Rectangle r)
    {
        method_rectangle = r;
    }
}
