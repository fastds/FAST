package edu.gzu.image.sphericallib;
public class Arc
{
    // Fields
    private double angle;
    private double area;
    private Halfspace circle;
    private Cartesian middle;
    private Cartesian point1;
    private Cartesian point2;
    public static final String Revision = "$Revision: 1.39 $";
    private Cartesian up;
    private Cartesian wp;

    // Methods
    public Arc()
    {
    }

    public Arc(Arc a)
    {
        this.circle = a.circle;
        this.point1 = a.point1;
        this.point2 = a.point2;
        this.middle = a.middle;
        this.area = a.area;
        this.angle = a.angle;
        this.up = a.up;
        this.wp = a.wp;
    }

    public Arc(Cartesian p1, Cartesian p2)
    {
        Cartesian center = p1.Cross(p2, false);
        Cartesian cartesian2 = new Cartesian(center.getX() * center.getX(), center.getY() * center.getY(), center.getZ() * center.getZ(), false);
        if (Math.max(cartesian2.getX(), Math.max(cartesian2.getY(), cartesian2.getZ())) <= Constant.DoublePrecision2x)
        {
            throw new RuntimeException("Arc..ctor(p1,p2): p1 and p2 are co-linear");
        }
        center.Scale(1.0 / Math.sqrt((cartesian2.getX() + cartesian2.getY()) + cartesian2.getZ()));
        this.circle = new Halfspace(center, 0.0, 1.0);
        this.point1 = p1;
        this.point2 = p2;
        this.middle = p1.GetMiddlePoint(p2, true);
        this.area = 0.0;
        this.up = p1;
        this.wp = this.circle.getVector().Cross(this.up, true);
        this.angle = p1.AngleInRadian(p2);
    }

    public Arc(Halfspace circle, Cartesian p)
    {
        this.circle = circle;
        this.point1 = p;
        this.point2 = p;
        this.angle = 6.2831853071795862;
        this.up = p.Sub(circle.getVector().Scaled(circle.getCos0()));
        this.up.Scale(1.0 / circle.getSin0());
        this.wp = circle.getVector().Cross(this.up, true);
        Cartesian cartesian = this.up.Scaled(-circle.getSin0());
        Cartesian that = circle.getVector().Scaled(circle.getCos0());
        this.middle = cartesian.Add(that);
        double num = (circle.getCos0() < 0.0) ? ((double) (-1)) : ((double) 1);
        this.area = ((6.2831853071795862 * (1.0 - (circle.getCos0() * num))) * num) * Constant.SquareRadian2SquareDegree;
    }

    public Arc(Halfspace circle, Cartesian p1, Cartesian p2)
    {
        this.circle = circle;
        this.point1 = p1;
        this.point2 = p2;
        this.up = p1.Sub(circle.getVector().Scaled(circle.getCos0()));
        this.up.Scale(1.0 / circle.getSin0());
        this.wp = circle.getVector().Cross(this.up, true);
        if (p1.Same(p2))
        {
            throw new RuntimeException("Arc..ctor(c,p1,p2): p1 and p2 are the same");
        }
        this.area = this.GetAreaSemilune();
        this.angle = this.GetAngle(p2);
        this.middle = this.GetPoint(this.angle / 2.0);
    }

    public double CalcTriangleArea(Cartesian p)
    {
        if (this.point2 == this.point1)
        {
            return this.area;
        }
        if (p.Same(this.point1) || p.Same(this.point2))
        {
            return this.area;
        }
        double num = Cartesian.SphericalTriangleArea(p, this.point1, this.point2);
        if (this.point1.Same(this.point2.Mirrored()))
        {
            Cartesian cartesian = p.Cross(this.point1, true);
            num += (2.0 * (3.1415926535897931 - cartesian.AngleInRadian(this.circle.getVector()))) * Constant.SquareRadian2SquareDegree;
            if (Cartesian.TripleProduct(cartesian, this.circle.getVector(), this.point1) < 0.0)
            {
                num *= -1.0;
            }
        }
        return (num + this.area);
    }

    public boolean ContainsOnEdge(Cartesian p)
    {
        if ((this.circle.GetTopo(p, Constant.SinTolerance) != Topo.Same) || (((this.getAngle() < this.GetAngle(p)) && !this.point1.Same(p)) && !this.point2.Same(p)))
        {
            return false;
        }
        return true;
    }

    public double GetAngle(Cartesian x)
    {
        double num = Math.atan2(this.wp.Dot(x), this.up.Dot(x));
        if (num < 0.0)
        {
            num += 6.2831853071795862;
        }
        return num;
    }

    private double GetAreaSemilune()
    {
        double num8 = 1.0;
        if (this.circle.getESign() == ESign.Zero)
        {
            return 0.0;
        }
        Cartesian vector = this.circle.getVector();
        double num5 = Cartesian.TripleProduct(vector, this.point1, this.point2);
        if (this.circle.getCos0() < 0.0)
        {
            vector.Mirror();
            num8 = -1.0;
        }
        double d = 0.5 * this.point1.Distance(this.point2);
        if (d > 1.0)
        {
            d = 1.0;
        }
        double num7 = ((Math.tan(Math.asin(d)) * this.circle.getCos0()) / this.circle.getSin0()) * num8;
        if (num7 > 1.0)
        {
            num7 = 1.0;
        }
        double num = Math.asin(num7);
        num7 = d / this.circle.getSin0();
        if (num7 > 1.0)
        {
            num7 = 1.0;
        }
        double num2 = 2.0 * Math.asin(num7);
        double num6 = (2.0 * num) - ((num2 * this.circle.getCos0()) * num8);
        if (num5 < 0.0)
        {
            num6 = (6.2831853071795862 * (1.0 - (this.circle.getCos0() * num8))) - num6;
        }
        num6 *= Constant.SquareRadian2SquareDegree;
        if (this.circle.getCos0() < 0.0)
        {
            num6 *= -1.0;
        }
        return num6;
    }

    public Cartesian GetPoint(double phi)
    {
        Cartesian up = this.up;
        up.Scale(Math.cos(phi) * this.circle.getSin0());
        Cartesian wp = this.wp;
        wp.Scale(Math.sin(phi) * this.circle.getSin0());
        Cartesian vector = this.circle.getVector();
        vector.Scale(this.circle.getCos0());
        return up.Add(wp).Add(vector);
    }

    public Convex GetWideConvex(double arcmin)
    {
        Convex convex = new Convex(4);
        arcmin /= 2.0;
        Halfspace circle = this.circle;
        circle.Grow(arcmin);
        convex.Add(circle);
        Halfspace h = this.circle;
        h.Invert();
        h.Grow(arcmin);
        convex.Add(h);
        Cartesian center = this.circle.getVector().Cross(this.point1, true);
        convex.Add(new Halfspace(center, 0.0, 1.0));
        center = this.point2.Cross(this.circle.getVector(), true);
        convex.Add(new Halfspace(center, 0.0, 1.0));
        return convex;
    }

    public String toString()
    {
        return this.circle+"  "+this.point1+"  "+this.point2;
    }

    // Properties
    
    public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public double getArea() {
		return area;
	}

	public void setArea(double area) {
		this.area = area;
	}

	public Halfspace getCircle() {
		return circle;
	}

	public void setCircle(Halfspace circle) {
		this.circle = circle;
	}
	
    public boolean IsFull()
    {
        return ((this.getPoint1() == this.getPoint2()) && !Double.isNaN(this.getAngle()));
    }
    public double getLength()
    {
        return (this.angle * this.circle.getSin0());
    }

    public Cartesian getMiddle() {
		return middle;
	}

	public void setMiddle(Cartesian middle) {
		this.middle = middle;
	}

	public Cartesian getPoint1() {
		return point1;
	}

	public void setPoint1(Cartesian point1) {
		this.point1 = point1;
	}

	public Cartesian getPoint2() {
		return point2;
	}

	public void setPoint2(Cartesian point2) {
		this.point2 = point2;
	}

    public Cartesian getPointU() {
		return this.up;
	}

	public void setPointU(Cartesian pointU) {
		this.up = pointU;
	}

	public Cartesian getPointW() {
		return this.wp;
	}

	public void setPointW(Cartesian pointW) {
		this.wp = pointW;
	}

}

 