import java.io.*;

import java.util.Vector;
import java.awt.Point;

public class Associations implements Serializable
{
    private Vector associations;

    public Associations()
    {
	associations = new Vector();
    }

    public Association get(int index)
    {
	return (Association) associations.get(index);
    }

    public void add(Association a)
    {
	associations.add(a);
    }

    public int size()
    {
	return associations.size();
    }

    public void clear()
    {
	associations.clear();
    }

    public void remove(int i)
    {
	associations.remove(i);
    }

    public void unSelectAll()
    {
	for (int i = 0; i < associations.size(); i++)
	    ((Association) associations.get(i)).unselect();
    }

    public Vector getVector()
    {
	return associations;
    }
								    
/*
    public int isAtPoint(Point p)
    {
	return 0;
    }
*/

}
    
