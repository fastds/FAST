package edu.gzu.image.sphericallib;

import java.util.List;
import java.util.Objects;

public class Cartesian {
	  private double x;
	    private double y;
	    private double z;
	    
	    public static final Cartesian NaN = new Cartesian(Double.NaN, Double.NaN, Double.NaN, false);;
	    public static final Cartesian Xaxis = new Cartesian(1.0, 0.0, 0.0, false);
	    public static final Cartesian Yaxis = new Cartesian(0.0, 1.0, 0.0, false);
	    public static final Cartesian Zaxis  = new Cartesian(0.0, 0.0, 1.0, false);
	    public static final String Revision = "$Revision: 1.46 $";
//	    [XmlAttribute]
	    public double getX()
	    {
            return this.x;
	    }
	    
	    public void setX(double x) {
			this.x = x;
		}
	    
	    public double getY() {
			return y;
		}

		public void setY(double y) {
			this.y = y;
		}

		public double getZ() {
			return z;
		}

		public void setZ(double z) {
			this.z = z;
		}

		
	    public double getNorm()
	    {
            return Math.sqrt(this.getNorm2());
	    }
	    public double getNorm2()
	    {
            return (((this.x * this.x) + (this.y * this.y)) + (this.z * this.z));
	    }
	    public double getRA()
	    {
	    	////////////initialize///////////////////////////////
	            double[] num = new double[1];
	            double[] num2 = new double[1];
	            Xyz2Radec(this.x, this.y, this.z, /*out*/ num, /*out*/ num2);
	            return num[0];
	    }
	    public double getDec()
	    {
            if (Math.abs(this.z) < 1.0)
            {
                return (Math.asin(this.z) * Constant.Radian2Degree);
            }
            return (90.0 * Math.signum(this.z));
	    }
	    public Cartesian(double x, double y, double z, boolean normalize)
	    {
	        this.x = x;
	        this.y = y;
	        this.z = z;
	        if (normalize)
	        {
	            this.Normalize();
	        }
	    }

	    public Cartesian(Cartesian p, boolean normalize)
	    {
	    	 this(p.x, p.y, p.z, normalize);
	    }

	    public Cartesian(double ra, double dec)
	    {
	    	double[] tempX = new double[1];
	    	double[] tempY = new double[1];
	    	double[] tempZ = new double[1];
	        Radec2Xyz(ra, dec, /*out*/ tempX, /*out*/ tempY, /*out*/ tempZ);
	        this.x = tempX[0];
	        this.y = tempY[0];
	        this.z = tempZ[0];
	    }

	    public double Get(int i) throws Exception
	    {
	        switch (i)
	        {
	            case 0:
	                return this.x;

	            case 1:
	                return this.y;

	            case 2:
	                return this.z;
	        }
	        throw new Exception("Cartesian IndexOutOfRange");
	    }

	    public void Set(Cartesian v, boolean normalize)
	    {
	        this.Set(v.x, v.y, v.z, normalize);
	    }

	    public void Set(double x, double y, double z, boolean normalize)
	    {
	        this.x = x;
	        this.y = y;
	        this.z = z;
	        if (normalize)
	        {
	            this.Normalize();
	        }
	    }

	    public void Set(double ra, double dec)
	    {
	    	double[] tempX = new double[1];
	    	double[] tempY = new double[1];
	    	double[] tempZ = new double[1];
	        Radec2Xyz(ra, dec, /*out*/ tempX, /*out*/ tempY, /*out*/ tempZ);
	        this.x = tempX[0];
	        this.y = tempY[0];
	        this.z = tempZ[0];
	    }

	    public void SetMiddlePoint(Cartesian p, Cartesian q, boolean normalize)
	    {
	        this.x = p.x + q.x;
	        this.y = p.y + q.y;
	        this.z = p.z + q.z;
	        if (normalize)
	        {
	            this.Normalize();
	        }
	    }

	    public double Normalize()
	    {
	        double d = ((this.x * this.x) + (this.y * this.y)) + (this.z * this.z);
	        double num2 = d - 1.0;
	        double num3 = 1.0;
	        if ((num2 > Constant.DoublePrecision4x) || (num2 < -Constant.DoublePrecision4x))
	        {
	            num3 = Math.sqrt(d);
	            this.x /= num3;
	            this.y /= num3;
	            this.z /= num3;
	        }
	        return num3;
	    }

	    public void Mirror()
	    {
	        this.x *= -1.0;
	        this.y *= -1.0;
	        this.z *= -1.0;
	    }

	    public Cartesian Mirrored()
	    {
	        return new Cartesian(-this.x, -this.y, -this.z, false);
	    }

	    public void Scale(double s)
	    {
	        this.x *= s;
	        this.y *= s;
	        this.z *= s;
	    }

	    public Cartesian Scaled(double s)
	    {
	        Cartesian cartesian = this;
	        cartesian.Scale(s);
	        return cartesian;
	    }

	    public void Tangent(/*out*/ Cartesian west, /*out*/ Cartesian north)
	    {
	    	/////////////initialize/////////////
	        double num = 0;
	        double num2 = 0;
	        Cartesian2RadecRadian(this, /*out*/ num, /*out*/ num2);
	        double x = Math.sin(num);
	        double num4 = Math.cos(num);
	        double num5 = Math.sin(num2);
	        double z = Math.cos(num2);
	        west = new Cartesian(x, -num4, 0.0, false);
	        north = new Cartesian(-num5 * num4, -num5 * x, z, false);
	    }

	    public double Dot(Cartesian p)
	    {
	        return (((this.x * p.x) + (this.y * p.y)) + (this.z * p.z));
	    }

	    public Cartesian GetMiddlePoint(Cartesian that, boolean normalize)
	    {
	        return new Cartesian(this.x + that.x, this.y + that.y, this.z + that.z, normalize);
	    }

	    public Cartesian Add(Cartesian that)
	    {
	        return new Cartesian(this.x + that.x, this.y + that.y, this.z + that.z, false);
	    }

	    public Cartesian Sub(Cartesian that)
	    {
	        return new Cartesian(this.x - that.x, this.y - that.y, this.z - that.z, false);
	    }

	    public Cartesian Cross(Cartesian p, boolean normalize)
	    {
	        return new Cartesian((this.y * p.z) - (this.z * p.y), (this.z * p.x) - (this.x * p.z), (this.x * p.y) - (this.y * p.x), normalize);
	    }

	    public double AngleInRadian(Cartesian p)
	    {
	        double d = 0.5 * this.Distance(p);
	        if (d < 1.0)
	        {
	            return (2.0 * Math.asin(d));
	        }
	        return 3.1415926535897931;
	    }

	    public double Distance(Cartesian p)
	    {
	        double num = this.x - p.x;
	        double num2 = this.y - p.y;
	        double num3 = this.z - p.z;
	        return Math.sqrt(((num * num) + (num2 * num2)) + (num3 * num3));
	    }

	    public double AngleInDegree(Cartesian p)
	    {
	        return (this.AngleInRadian(p) * Constant.Radian2Degree);
	    }

	    public double AngleInArcmin(Cartesian p)
	    {
	        return (this.AngleInRadian(p) * Constant.Radian2Arcmin);
	    }

	    public boolean Same(Cartesian that)
	    {
	        if (this == that)
	        {
	            return true;
	        }
	        if (this.Dot(that) < Constant.CosSafe)
	        {
	            return false;
	        }
	        return (this.AngleInRadian(that) < Constant.Tolerance);
	    }

	    public static double TripleProduct(Cartesian p1, Cartesian p2, Cartesian p3)
	    {
	        double num = (p1.y * p2.z) - (p1.z * p2.y);
	        double num2 = (p1.z * p2.x) - (p1.x * p2.z);
	        double num3 = (p1.x * p2.y) - (p1.y * p2.x);
	        return (((num * p3.x) + (num2 * p3.y)) + (num3 * p3.z));
	    }

	    public static void Xyz2Radec(double x, double y, double z, /*out*/ double[] ra, /*out*/ double[] dec)
	    {
	    	System.out.println("Cartesian.Xyz2Radec()------run");
	        double num2;
	        double num = Constant.DoublePrecision2x;
	        if (z >= 1.0)
	        {
	            num2 = 1.5707963267948966;
	        }
	        else if (z <= -1.0)
	        {
	            num2 = -1.5707963267948966;
	        }
	        else
	        {
	            num2 = Math.asin(z);
	        }
	        dec[0] = num2 * Constant.Radian2Degree;
	        double num3 = Math.cos(num2);
	        if ((num3 > num) || (num3 < -num))
	        {
	            if ((y > num) || (y < -num))
	            {
	                double num5;
	                double d = x / num3;
	                if (d <= -1.0)
	                {
	                    num5 = 180.0;
	                }
	                else if (d >= 1.0)
	                {
	                    num5 = 0.0;
	                }
	                else
	                {
	                    num5 = Math.acos(d) * Constant.Radian2Degree;
	                }
	                if (y < 0.0)
	                {
	                    ra[0] = 360.0 - num5;
	                }
	                else
	                {
	                    ra[0] = num5;
	                }
	            }
	            else
	            {
	                ra[0] = (x < 0.0) ? 180.0 : 0.0;
	            }
	        }
	        else
	        {
	            ra[0] = 0.0;
	        }
	        System.out.println("Cartesian.Xyz2Radec()------finish");
	    }

	    public static void Xyz2RadecRadian(double x, double y, double z, /*out*/ double ra, /*out*/ double dec)
	    {
	        double num2;
	        double num = Constant.DoublePrecision2x;
	        if (z >= 1.0)
	        {
	            num2 = 1.5707963267948966;
	        }
	        else if (z <= -1.0)
	        {
	            num2 = -1.5707963267948966;
	        }
	        else
	        {
	            num2 = Math.asin(z);
	        }
	        dec = num2;
	        double num3 = Math.cos(num2);
	        if ((num3 > num) || (num3 < -num))
	        {
	            if ((y > num) || (y < -num))
	            {
	                double num5;
	                double d = x / num3;
	                if (d <= -1.0)
	                {
	                    num5 = 3.1415926535897931;
	                }
	                else if (d >= 1.0)
	                {
	                    num5 = 0.0;
	                }
	                else
	                {
	                    num5 = Math.acos(d);
	                }
	                if (y < 0.0)
	                {
	                    ra = 6.2831853071795862 - num5;
	                }
	                else
	                {
	                    ra = num5;
	                }
	            }
	            else
	            {
	                ra = (x < 0.0) ? 3.1415926535897931 : 0.0;
	            }
	        }
	        else
	        {
	            ra = 0.0;
	        }
	    }

	    public static void Radec2Xyz(double ra, double dec, /*out*/ double[] x, /*out*/ double[] y, /*out*/ double[] z)
	    {
	        double num = Constant.DoublePrecision2x;
	        double num3 = Math.cos(dec * Constant.Degree2Radian);
	        double num2 = 90.0 - dec;
	        if ((num2 < num) && (num2 > -num))
	        {
	            x[0] = 0.0;
	            y[0] = 0.0;
	            z[0] = 1.0;
	        }
	        else
	        {
	            num2 = -90.0 - dec;
	            if ((num2 < num) && (num2 > -num))
	            {
	                x[0] = 0.0;
	                y[0] = 0.0;
	                z[0] = -1.0;
	            }
	            else
	            {
	                z[0] = Math.sin(dec * Constant.Degree2Radian);
	                double num4 = ra / 90.0;
	                double num5 = (int) num4;
	                if (Math.abs((double) (num5 - num4)) >= num)
	                {
	                    x[0] = Math.cos(ra * Constant.Degree2Radian) * num3;
	                    y[0] = Math.sin(ra * Constant.Degree2Radian) * num3;
	                }
	                else
	                {
	                    int num6 = (int) num5;
	                    num6 = num6 % 4;
	                    if (num6 < 0)
	                    {
	                        num6 += 4;
	                    }
	                    switch (num6)
	                    {
	                        case 0:
	                            x[0] = num3;
	                            y[0] = 0.0;
	                            return;

	                        case 1:
	                            x[0] = 0.0;
	                            y[0] = num3;
	                            return;

	                        case 2:
	                            x[0] = -num3;
	                            y[0] = 0.0;
	                            return;
	                    }
	                    x[0] = 0.0;
	                    y[0] = -num3;
	                }
	            }
	        }
	    }

	    public static void Cartesian2Radec(Cartesian p, /*out*/ double[] ra, /*out*/ double[] dec)
	    {
	        Xyz2Radec(p.x, p.y, p.z, /*out*/ ra, /*out*/ dec);
	    }

	    public static void Cartesian2RadecRadian(Cartesian p, /*out*/ double ra, /*out*/ double dec)
	    {
	        Xyz2RadecRadian(p.x, p.y, p.z, /*out*/ ra, /*out*/ dec);
	    }

	    public static Cartesian CenterOfMass(List<Cartesian> plist, boolean normalize)
	    {
	        Cartesian cartesian = new Cartesian(0.0, 0.0, 0.0, false);
	        for (Cartesian cartesian2 : plist)
	        {
	            cartesian.x += cartesian2.x;
	            cartesian.y += cartesian2.y;
	            cartesian.z += cartesian2.z;
	        }
	        if (normalize)
	        {
	            cartesian.Normalize();
	        }
	        return cartesian;
	    }

	    public static Halfspace MinimalEnclosingCircle(List<Cartesian> plist)
	    {
	        double radiusInRadian;
	        Halfspace halfspace = new Halfspace(new Cartesian(1.0, 0.0, 0.0, false), -1.0, 0.0);
	        boolean flag = false;
	        double num2 = 3.1415926535897931;
	        double cosTolerance = Constant.CosTolerance;
	        double sinTolerance = Constant.SinTolerance;
	        if (plist.size() == 1)
	        {
	            return new Halfspace(plist.get(0), 1.0, 0.0);
	        }
	        for (int i = 0; i < (plist.size() - 1); i++)
	        {
	            Cartesian cartesian = plist.get(i);
	            for (int k = i + 1; k < plist.size(); k++)
	            {
	                Cartesian middlePoint;
	                Cartesian p = plist.get(k);
	                if (cartesian.Same(p.Mirrored()))
	                {
	                    middlePoint = CenterOfMass(plist, true);
	                }
	                else
	                {
	                    middlePoint = plist.get(i).GetMiddlePoint(plist.get(k), true);
	                }
	                double d = middlePoint.AngleInRadian(p);
	                Halfspace halfspace2 = new Halfspace(middlePoint, Math.cos(d), Math.sin(d));
	                radiusInRadian = halfspace2.getRadiusInRadian();
	                if ((radiusInRadian < num2) && halfspace2.Contains(plist, cosTolerance, sinTolerance))
	                {
	                    flag = true;
	                    halfspace = halfspace2;
	                    num2 = radiusInRadian;
	                }
	            }
	        }
	        for (int j = 0; j < (plist.size() - 1); j++)
	        {
	            for (int m = j + 1; m < plist.size(); m++)
	            {
	                for (int n = 0; n < plist.size(); n++)
	                {
	                    if ((n != j) && (n != m))
	                    {
	                        Halfspace halfspace3;
	                        try
	                        {
	                            halfspace3 = new Halfspace(plist.get(j), plist.get(m), plist.get(n));
	                        }
	                        catch (Exception e)
	                        {
	                            continue;
	                        }
	                        radiusInRadian = halfspace3.getRadiusInRadian();
	                        if ((radiusInRadian < num2) && halfspace3.Contains(plist, cosTolerance, sinTolerance))
	                        {
	                            flag = true;
	                            halfspace = halfspace3;
	                            num2 = radiusInRadian;
	                        }
	                        else
	                        {
	                            halfspace3.Invert();
	                            radiusInRadian = halfspace3.getRadiusInRadian();
	                            if ((radiusInRadian < num2) && halfspace3.Contains(plist, cosTolerance, sinTolerance))
	                            {
	                                flag = true;
	                                halfspace = halfspace3;
	                                num2 = radiusInRadian;
	                            }
	                        }
	                    }
	                }
	            }
	        }
	        if (!flag)
	        {
	            throw new RuntimeException("MinimalEnclosingCircle(): not found! invalid input?");
	        }
	        return halfspace;
	    }

	    public static double GirardArea(double a, double b, double c)
	    {
	        if (((a < 0.0) || (b < 0.0)) || (c < 0.0))
	        {
	            throw new RuntimeException("Cartesian.GirardArea(): negative arc length?");
	        }
	        double num = 0.5 * ((a + b) + c);
	        double num2 = num - a;
	        double num3 = num - b;
	        double num4 = num - c;
	        if ((((num2 <= 0.0) || (num3 <= 0.0)) || ((num4 <= 0.0) || ((num2 / num) < Constant.DoublePrecision2x))) || (((num3 / num) < Constant.DoublePrecision2x) || ((num4 / num) < Constant.DoublePrecision2x)))
	        {
	            return 0.0;
	        }
	        double d = ((Math.tan(num / 2.0) * Math.tan(num2 / 2.0)) * Math.tan(num3 / 2.0)) * Math.tan(num4 / 2.0);
	        return (4.0 * Math.atan(Math.sqrt(d)));
	    }

	    public static double SphericalTriangleArea(Cartesian p1, Cartesian p2, Cartesian p3)
	    {
	        Cartesian cartesian = new Cartesian(p3.x - p2.x, p3.y - p2.y, p3.z - p2.z, false);
	        Cartesian cartesian2 = new Cartesian(p1.x - p3.x, p1.y - p3.y, p1.z - p3.z, false);
	        Cartesian cartesian3 = new Cartesian(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z, false);
	        double a = 2.0 * Math.asin(0.5 * Math.sqrt(((cartesian.x * cartesian.x) + (cartesian.y * cartesian.y)) + (cartesian.z * cartesian.z)));
	        double b = 2.0 * Math.asin(0.5 * Math.sqrt(((cartesian2.x * cartesian2.x) + (cartesian2.y * cartesian2.y)) + (cartesian2.z * cartesian2.z)));
	        double c = 2.0 * Math.asin(0.5 * Math.sqrt(((cartesian3.x * cartesian3.x) + (cartesian3.y * cartesian3.y)) + (cartesian3.z * cartesian3.z)));
	        double num5 = TripleProduct(cartesian, cartesian2, p3);
	        if (Math.abs(num5) < Double.MIN_NORMAL)
	        {
	            return 0.0;
	        }
	        double num4 = GirardArea(a, b, c) * Constant.SquareRadian2SquareDegree;
	        if (num5 < 0.0)
	        {
	            num4 *= -1.0;
	        }
	        return num4;
	    }

	    public boolean Equals(Cartesian right)
	    {
	        return ((new Double(this.x).equals(right.x) && new Double(this.y).equals(right.y)) && new Double(this.z).equals(right.z));
	    }

	    public boolean Equals(Object right)
	    {
	        if (right == null)
	        {
	            return false;
	        }
	        if (Objects.equals(this, right))
	        {
	            return true;
	        }
	        if (super.getClass()/*GetType()*/ != right.getClass()/*GetType()*/)
	        {
	            return false;
	        }
	        Cartesian cartesian = (Cartesian) right;
	        return this.Equals(cartesian);
	    }

	    public int hashCode()
	    {
	        return this.toString().hashCode();
	    }

//	    public static boolean operator ==(Cartesian left, Cartesian right)
//	    {
//	        return (((left.x == right.x) && (left.y == right.y)) && (left.z == right.z));
//	    }
//
//	    public static boolean operator !=(Cartesian left, Cartesian right)
//	    {
//	        if ((left.x == right.x) && (left.y == right.y))
//	        {
//	            return !(left.z == right.z);
//	        }
//	        return true;
//	    }

	    public static boolean IsNaN(Cartesian p)
	    {
	        return ((Double.isNaN(p.x) && Double.isNaN(p.y)) && Double.isNaN(p.z));
	    }

	    public static Cartesian Parse(String repr, boolean normalize)
	    {
	        char[] separator = new char[] { ' ' };
	        String[] strArray = repr.split(" "/*separator*/, 3);
	        double x = Double.parseDouble(strArray[0]);
	        double y = Double.parseDouble(strArray[1]);
	        return new Cartesian(x, y, Double.parseDouble(strArray[2]), normalize);
	    }

	    public String toString()
	    {
	        return this.x+" "+this.y+" "+this.z;
	    }

//	    /*static*/ Cartesian()
//	    {
//	        NaN = new Cartesian(Double.NaN, Double.NaN, Double.NaN, false);
//	        Xaxis = new Cartesian(1.0, 0.0, 0.0, false);
//	        Yaxis = new Cartesian(0.0, 1.0, 0.0, false);
//	        Zaxis = new Cartesian(0.0, 0.0, 1.0, false);
//	        Revision = "$Revision: 1.46 $";
//	    }


}
