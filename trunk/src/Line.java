import java.awt.Point;

public class Line
{
    Point startPoint, endPoint;

    public Line(Point startPoint, Point endPoint)
    {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
    }

    public Point getStartPoint()
    {
		return startPoint;
    }

    public Point getEndPoint()
    {
		return endPoint;
    }

    public void setEndPoint(Point endPoint)
    {
		this.endPoint = endPoint;
    }

    public Point hits(Line line2)
    {
		double alpha, beta;
		Point t0 = line2.getStartPoint()
			, t1 = line2.getEndPoint()
			, s0 = this.getStartPoint()
	    , s1 = this.getEndPoint();
	
//	System.out.println("Going to calculate if the lines touch each other, points: (" + t0.x + "," + t0.y + ") and (" + t1.x + "," + t1.y + ")");
	    
		// Check for t0 == s0
		if ((t0.x == s0.x) && (t0.y == s0.y))
			return s0;
		// Check for t1 == s0
		if ((t1.x == s0.x) && (t1.y == s0.y))
			return s0;
		// Check for t0 == s1
		if ((t0.x == s1.x) && (t0.y == s1.y))
			return t0;
		// Check for t1 == s1
		if ((t1.x == s1.x) && (t1.y == s1.y))
			return s1;

		// Check for t0 == t1 (and of course the point is not ON some of the lines as above checked
		if ((t0.x == t1.x) && (t0.y == t1.y))
			return null;

		// And of course for the case so == s1
		if ((s0.x == s1.x) && (s0.y == s1.y))
			return null;

		double xs0 = s0.x;
		double ys0 = s0.y;
		double xs1 = s1.x;
		double ys1 = s1.y;
		double xt0 = t0.x;
		double yt0 = t0.y;
		double xt1 = t1.x;
		double yt1 = t1.y;
		
		double xA, yA; // Hitpoint

		if ((xs0 == xs1) && !(xt0 == xt1))
		{
			xA = xs0;
			yA = (xt1*yt0 - xt0*yt1)/(xt1 - xt0) + xs0*(yt1 - yt0)/(xt1 - xt0);
		}
		else if ((ys0 == ys1) && !(xt0 == xt1))
		{
			yA = ys0;
			xA = (yA - (xt1*yt0 - xt0*yt1)/(xt1 - xt0))*(xt1 - xt0)/(yt1 - yt0);
		}
		else if (xt0 == xt1)
		{
			System.out.println("Vertical line detected: to be implemented yet");

			if (xs0 == xs1)
			{
				System.out.println("2 vertical lines, no hitpoint");
				return null;
			}
			else
			{
				xA = xt0;
				yA = xA*(ys1 - ys0)/(xs1 - xs0) + ys0 - xs0*(ys1 - ys0)/(xs1 - xs0);
			}
		}
		/* Horizontal line gives no problems
		else if (yt0 == yt1)
		{
			System.out.println("Horizontal line detected: to be implemented yet");
			return null;
		}
		*/
		else
		{
			yA = (xs1*ys0 - xs0*ys1)/(xs1 - xs0) +((ys1-ys0)/(xs1 - xs0))
				*((xs1*ys0 - xs0*ys1)*(xt1 - xt0) - (xt1*yt0 - xt0*yt1)*(xs1 - xs0))
				/((yt1 - yt0)*(xs1 - xs0) - (ys1 - ys0)*(xt1 - xt0));
		
			xA = ((xs1*ys0 - xs0*ys1)*(xt1 - xt0) - (xt1*yt0 - xt0*yt1)*(xs1 - xs0))
					/((yt1 - yt0)*(xs1 -xs0) - (ys1 - ys0)*(xt1 - xt0));
		}
			    
		double noemer1 = (yt1 - yt0)*(xs1 - xs0) - (ys1 - ys0)*(xt1 - xt0);
		double noemer2 = (yt1 - yt0)*(xs1 -xs0) - (ys1 - ys0)*(xt1 - xt0);
		double noemer3 = (xs1 - xs0);

//	System.out.println("Noemer 1: " + noemer1);
//	System.out.println("Noemer 2: " + noemer2);
//	System.out.println("Noemer 3: " + noemer3);

//	System.out.println("Hitpoint is: (" + xA + "," + yA + ")");
	
	// TODO: Check for if one or more of the lines is actually a point, i.e. startpoint == endpoint
		    
	// Watch out for division by zero !
	/*
	 * Watch out. This is a mirrored x,y coordinate-system, so write -x for x!
	 */
//	System.out.println("The points: t0 (" + t0.x + "," + t0.y + "), t1 (" + t1.x + "," + t1.y + "), " +
//			"s0 (" + s0.x + "," + s0.y + "), s1 (" + s1.x + "," + s1.y + ")");
/*	alpha = (double) (t0.x - s0.x + (t1.x - t0.x)*((double) s0.y - t0.y)/(t1.y - t0.y))/(s1.x - s0.x - ((double)s1.y -s0.y)*(t1.x - t0.x));
//	alpha = (double) (-t0.x + s0.x + (-t1.x + t0.x)*(s0.y - t0.y)/(t1.y - t0.y))/(-s1.x + s0.x - (s1.y -s0.y)*(-t1.x + t0.x));
	System.out.println("Alpha: " + alpha);
	// Calculate beta here for debugging only
	beta = (double) ((s0.y -t0.y) + alpha*(s1.y - s0.y))/((double)t1.y - t0.y);
	System.out.println("Beta: " + beta);
	System.out.println("HitPoint is: (" + ((double) t0.x + (double) beta*(t1.x - t0.x)) + "," 
			    + ((double) t0.y + (double) beta*(t1.y - t0.y)) + ")");
	
	if (alpha >= 0 && alpha <= 1)
	{
    	    beta = (double) ((s0.y -t0.y) + alpha*(s1.y - s0.y))/(t1.y - t0.y);
	    System.out.println("Beta: " + beta);
	    if (beta >= 0 && beta <= 1)
	    {
		Point p = new Point();
		// Revert x to get back to this weird mirrored x,y coordinate system
		p.setLocation((t0.x + beta*(t1.x - t0.x)), t0.y + beta*(t1.y - t0.y));
		return p;
	    }
	}
*/
		// Is the hitpoint between the other two points?
		Point p = new Point();
		// inproduct of s0A and As1
		if (((xA - xs0)*(xs1 - xA) + (yA - ys0)*(ys1 - yA) > 0)
			// inproduct of t0A and At1
			&& ((xA - xt0)*(xt1 - xA) + (yA -yt0)*(yt1 - yA) > 0))
		{
			p.setLocation (xA, yA);
			System.out.println("Got hitpoint: (" + xA + "," + yA + ")");
		}
		else
		{
			p = null;
		}

		return p;
    }

    public boolean intersects(Point p)
    {
		int xs0 = getStartPoint().x;
		int ys0 = getStartPoint().y;
		int xs1 = getEndPoint().x;
		int ys1 = getEndPoint().y;
		int xA = p.x;
		int yA = p.y;
		
		if ((xA - xs0)*(xs1 - xA) + (yA - ys0)*(ys1 - yA) > 0)
			return true;

		return false;
    }

    public Point getProjectionPoint(Point p)
    {
		Point a = new Point(p.x - getStartPoint().x, p.y - getStartPoint().y);
		Point b = new Point(getEndPoint().x - getStartPoint().x, getEndPoint().y - getStartPoint().y);
		int adotb = a.x * b.x + a.y * b.y;
		int bsquared = b.x * b.x + b.y * b.y;

		// a parallel
		Point apar = new Point(adotb*b.x/bsquared, adotb*b.y/bsquared);
		// a perpendicular
		Point aperp = new Point(a.x - apar.x, a.y - apar.x);
		int absaperp = aperp.x * aperp.x + aperp.y * aperp.y;
		if ( ((apar.x * apar.x + apar.y * apar.y) < (b.x * b.x + b.y * b.y))
			&& ((apar.x * b.x + apar.y * b.y) > 0) )
			return new Point(getStartPoint().x + apar.x, getStartPoint().y + apar.y);

		return null;
    }
}

