import java.util.*;
import java.io.*;
import java.awt.Point;

public class Rectangles implements Serializable
{
    int UPPER_MARGIN = 5, LEFT_MARGIN = 10;
    private Vector rectangles;

    public Rectangles()
    {
		rectangles = new Vector();
    }

    public Orectangle getOrectangleByName(String classname)
    {
        int i = 0;
        while (i < rectangles.size())
        {
            if (((Orectangle) rectangles.get(i)).name.equals(classname))
                return ((Orectangle) rectangles.get(i));
            i++;
        }
        return null;
    }

    public Orectangle contains(Point point)
    {
		for (int i = 0; i < rectangles.size(); i++)
		{
			if (((Orectangle) rectangles.get(i)).contains(point))
			{
				return (Orectangle) rectangles.get(i);
			}
		}

		return null;
    }

    public void add(Orectangle oRectangle)
    {
		rectangles.add(oRectangle);
    }

    public Orectangle get(int index)
    {
		return (Orectangle) rectangles.get(index);
    }

    public void remove(int index)
    {
		rectangles.remove(index);
    }

    public void clear()
    {
		rectangles.clear();
    }

    public int size()
    {
		return rectangles.size();
    }

    public int indexOf(Orectangle rectangle)
    {
		return rectangles.indexOf(rectangle);
    }

    public Vector getVector()
    {
		return rectangles;
    }
}
