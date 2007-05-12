import java.io.*;

import java.util.Vector;
import java.awt.Point;

public class Association implements Serializable 
{

    public static final int NORTH = 0;
    public static final int EAST = 1;
    public static final int SOUTH = 2;
    public static final int WEST = 3;
    //
    // An association can be made between two classes
    // It is constituted from 
    //
    Vector pointArray;
    int type;    
    Orectangle fromClass, toClass;
    boolean isSelected = false;
    int pointSelected = 0;
    int pointHighLighted = -1;
    private String name;
    int pointToAddIndex = -1;

    public Association()
    {
		pointArray = new Vector();
		name = new String();
    }

    public Association(Point point)
    {
		this();
		add(point);
    }

    public Association(Point point, int type)
    {
		this();
		add(point);
		this.type = type;
    }

    public void add(Point point)
    {
		pointArray.add(point);
    }

    public Point get(int i)
    {
		return (Point) pointArray.get(i);
    }

    public void set(int i, Point p)
    {
		pointArray.set(i, p);
    }

    public void insertElementAt(int i, Point p)
    {
//	pointArray.insertElementAt (p , i);
		pointArray.add (i, p);
    }

    public void remove(int i)
    {
		pointArray.remove(i);
    }

    public void setFromClass(Orectangle fromClass)
    {
		this.fromClass = fromClass;
    }

    public void setToClass(Orectangle toClass)
    {
		this.toClass = toClass;
    }

    public Orectangle getFromClass()
    {
		return fromClass;
    }

    public Orectangle getToClass()
    {
		return toClass;
    }

    public int size()
    {
		return pointArray.size();
    }

    public int getType()
    {
		return type;
    }

    public void select()
    {
		isSelected = true;
    }

    public void unselect()
    {
		isSelected = false;
    }

    public void highlight(int pointHighLighted)
    {
		this.pointHighLighted = pointHighLighted;
    }

    public int getHighlightedPoint()
    {
		return pointHighLighted;
    }

    public void dehighlight()
    {
        pointHighLighted = -1;
    }

    public boolean highlighted()
    {
		return (pointHighLighted != -1);
    }

    public boolean isSelected()
    {
		return isSelected;
    }

    public int isAtPoint(Point p)
    {
		int ALLOWED_DISTANCE = 26;
		pointSelected = -1;
		
		for (int i = 0; i < pointArray.size(); i++)
		{
			if ((p.getX() - ((Point) pointArray.get(i)).getX())*(p.getX() - ((Point) pointArray.get(i)).getX())
			+ (p.getY() - ((Point) pointArray.get(i)).getY())*(p.getY() - ((Point) pointArray.get(i)).getY()) < ALLOWED_DISTANCE)
			{
			pointSelected = i;
			break;
			}
		}
		
		return pointSelected;
    }

    public Point isNearLine(Point p)
    {
		int ALLOWED_DISTANCE = 100;
		
		for (int i = 0; i < pointArray.size() - 1; i++)
		{
			Point projectionPoint = new Line((Point) pointArray.get(i), (Point) pointArray.get(i + 1)).getProjectionPoint(p);

			if (projectionPoint != null)
			{
				int distance = (projectionPoint.x - p.x)*(projectionPoint.x - p.x) 
					+ (projectionPoint.y - p.y)*(projectionPoint.y - p.y);
//		System.out.println("Found projection point: (" + projectionPoint.x + "," + projectionPoint.y + ")");
//		System.out.println("   Distance: " + distance);
				if ( (projectionPoint.x - p.x)*(projectionPoint.x - p.x) 
					+ (projectionPoint.y - p.y)*(projectionPoint.y - p.y)< ALLOWED_DISTANCE)
				{
					pointToAddIndex = i + 1;
					return projectionPoint;
				}
			}
		}

		return null;
    }

    public int getDirection()
    {
		// Handle vertical lines
		if (get(0).getX() == get(1).getX())
		{
			if (get(0).getY() > get(1).getY())
			return NORTH;
			else
			return SOUTH;
		}
		// Handle horizontal lines
		if (get(0).getY() == get(1).getY())
		{
			if (get(0).getX() > get(1).getX())
			return WEST;
			else
				return EAST;
		}
		// No vertical, no horizontal line, do some calculations
		double rico = (get(1).getY() - get(0).getY())/(get(1).getX() - get(0).getX());

		Point relativePoint = new Point(new Double(get(1).getX() - get(0).getX()).intValue()
					, new Double(get(1).getY() - get(0).getY()).intValue());

		if (rico >= 1 || rico <= -1)
			if (relativePoint.getY() > 0) 
			return SOUTH;
			else
			return NORTH;
		if (rico < 1 || rico > -1)
			if (relativePoint.getX() > 0)
			return EAST;
			else
			return WEST;

		// Default, to prevent compile errors :)
		return NORTH;
	}

	public String getName()
    {
		return this.name;
    }

    public String toString()
    {
//	System.out.println("toString method called on Association");
		return getName();
    }

    public void setName(String name)
    {
		this.name = name;
    }

    public void setPointToAddIndex(int index)
    {
		pointToAddIndex = index;
    }

    public int getPointToAddIndex()
    {
		return pointToAddIndex;
    }
}
