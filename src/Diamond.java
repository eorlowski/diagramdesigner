import java.awt.Polygon;
import java.awt.Point;

public class Diamond extends Polygon
{
    public Diamond(Point[] points)
    {
		super();
		for (int i = 0; i < points.length; i++)
		{
			addPoint(points[i].x, points[i].y);
		}
    }
}
