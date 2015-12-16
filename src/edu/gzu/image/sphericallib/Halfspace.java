package edu.gzu.image.sphericallib;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Halfspace {
	private Cartesian vector;
    private double cos0;
    private double sin0;
    public static /*final*/ Halfspace UnitSphere;
    public static /*final*/ String Revision;
    public Cartesian getVector()
    {
        return this.vector;
    }
    public void setVector(Cartesian value)
    {
    	 this.vector = value;
    }
//    [XmlAttribute("C")]
    public double getCos0()
    {
        return this.cos0;
    }
    public void setCos0(double value)
    {
    	 this.cos0 = value;
    }
//    [XmlAttribute("S")]
    public double getSin0()
    {
        return this.sin0;
    }
    public void setSin0(double value)
    {
    	this.sin0 = value;
    }
    public ESign getESign()
    {
        double doublePrecision = Constant.DoublePrecision;
        if (this.cos0 > doublePrecision)
        {
            return ESign.Positive;
        }
        if (this.cos0 < -doublePrecision)
        {
            return ESign.Negative;
        }
        return ESign.Zero;
    }
//    [XmlIgnore]
    public double getRadiusInRadian()
    {
        if (this.cos0 >= 1.0)
        {
            return 0.0;
        }
        if (this.cos0 <= -1.0)
        {
            return 3.1415926535897931;
        }
        return Math.acos(this.cos0);
    }
//    [XmlIgnore]
    public double getRadiusInDegree()
    {
        return (this.getRadiusInRadian() * Constant.Radian2Degree);
    }
//    [XmlIgnore]
    public double getRadiusInArcmin()
    {
        return (this.getRadiusInRadian() * Constant.Radian2Arcmin);
    }
    public double getArea()
    {
        return ((6.2831853071795862 * (1.0 - this.cos0)) * Constant.SquareRadian2SquareDegree);
    }
    public Halfspace(Cartesian center, double cos0, double sin0)
    {
        this.vector = center;
        this.cos0 = cos0;
        this.sin0 = sin0;
    }

    public Halfspace(double x, double y, double z, boolean normalize, double cos0, double sin0)
    {
    	this(new Cartesian(x, y, z, normalize), cos0, sin0);
    }

    public Halfspace(double x, double y, double z, boolean normalize, double cos0)
    {
    	this(new Cartesian(x, y, z, normalize), cos0, Math.sqrt(1.0 - (cos0 * cos0)));
    }

    public Halfspace(Cartesian center, double cos0) 
    {
    	 this(center, cos0, Math.sqrt(1.0 - (cos0 * cos0)));
    }

    public Halfspace(double ra, double dec, double arcmin)
    {
        this.vector = new Cartesian(ra, dec);
        double d = arcmin * Constant.Arcmin2Radian;
        this.cos0 = Math.cos(d);
        this.sin0 = Math.sin(d);
    }

    public Halfspace(Cartesian p0, Cartesian p1, Cartesian p2)
    {
        Cartesian cartesian = new Cartesian(p1.getX() - p0.getX(), p1.getY() - p0.getY(), p1.getZ() - p0.getZ(), false);
        Cartesian p = new Cartesian(p2.getX() - p0.getX(), p2.getY() - p0.getY(), p2.getZ() - p0.getZ(), false);
        this.vector = cartesian.Cross(p, true);
        this.cos0 = this.vector.Dot(p0);
        this.sin0 = Math.sqrt(1.0 - (this.cos0 * this.cos0));
        if (this.cos0 < 0.0)
        {
            this.Invert();
        }
    }

    public void Invert()
    {
        this.vector.Mirror();
        this.cos0 *= -1.0;
    }

    public static Halfspace Invert(Halfspace h)
    {
        return h.Inverse();
    }

    public Halfspace Inverse()
    {
        return new Halfspace(this.vector.Mirrored(), -this.cos0, this.sin0);
    }

    public Halfspace Grow(double arcmin)
    {
        double d = Math.acos(this.cos0) + (arcmin * Constant.Arcmin2Radian);
        if (d > 3.1415926535897931)
        {
            throw new RuntimeException("Halfspace.Grow(): radius bigger than Pi  (Argument)");
        }
        if (d < 0.0)
        {
            throw new RuntimeException("Halfspace.Grow(): radius less than 0(Argument)");
        }
        this.cos0 = Math.cos(d);
        this.sin0 = Math.sin(d);
        return this;
    }

    public Topo GetTopo(Halfspace h)
    {
    	/////////////initialize/////////////////////////////////////
        Cartesian[] cartesian = new Cartesian[1];
        Cartesian[] cartesian2 = new Cartesian[1];
        return this.GetTopo(h, /*out*/ cartesian, /*out*/ cartesian2);
    }

    public Topo GetTopo(Halfspace h, /*out*/ Cartesian[] pos, /*out*/ Cartesian[] neg)
    {
    	//////initialize//////////////////////////////
        int[] num = new int[1];
        return this.GetTopo(h, /*out*/ pos, /*out*/ neg, /*out*/ num);
    }

    public Topo GetTopo(Halfspace h, /*out*/ Cartesian[] pos, /*out*/ Cartesian[] neg, /*out*/ int[] flag)
    {
        pos = new Cartesian[]{Cartesian.NaN};
        neg = new Cartesian[]{Cartesian.NaN};
        flag[0] = -99;
        if (this == h)
        {
            return Topo.Same;
        }
        double num = this.getVector().AngleInRadian(h.getVector());
        double radiusInRadian = this.getRadiusInRadian();
        double num3 = h.getRadiusInRadian();
        double safeLimit = Constant.SafeLimit;
        double num5 = (num - radiusInRadian) - num3;
        double num6 = (radiusInRadian - num) - num3;
        double num7 = (num3 - num) - radiusInRadian;
        if (num5 > safeLimit)
        {
            return Topo.Disjoint;
        }
        if (num6 > safeLimit)
        {
            return Topo.Outer;
        }
        if (num7 > safeLimit)
        {
            return Topo.Inner;
        }
        flag[0] = this.Roots(h, /*out*/ pos, /*out*/ neg);
        if (flag[0] == 0)
        {
            boolean flag2 = this.vector.Dot(h.vector) > 0.0;
            double num8 = this.cos0 - h.cos0;
            if ((flag2 && (num8 < Constant.DoublePrecision2x)) && (num8 > -Constant.DoublePrecision2x))
            {
                return Topo.Same;
            }
            num8 = this.cos0 + h.cos0;
            if ((!flag2 && (num8 < Constant.DoublePrecision2x)) && (num8 > -Constant.DoublePrecision2x))
            {
                return Topo.Inverse;
            }
        }
        if (flag[0] == 2)
        {
            return Topo.Intersect;
        }
        if (num5 > 0.0)
        {
            return Topo.Disjoint;
        }
        if (num6 > 0.0)
        {
            return Topo.Outer;
        }
        if (num7 > 0.0)
        {
            return Topo.Inner;
        }
        return Topo.Overlap;
    }

    public Topo GetTopo(Cartesian p)
    {
        return this.GetTopo(p, Constant.SinTolerance);
    }

    public Topo GetTopo(Cartesian p, double sintol)
    {
        double num = this.vector.Dot(p);
        if (num > (this.cos0 + Constant.SafeLimit))
        {
            return Topo.Inner;
        }
        if (num >= (this.cos0 - Constant.SafeLimit))
        {
            double num4 = (Math.sin(this.vector.AngleInRadian(p)) * this.cos0) - (num * this.sin0);
            if ((num4 > -sintol) && (num4 < sintol))
            {
                return Topo.Same;
            }
            if (num > this.cos0)
            {
                return Topo.Inner;
            }
        }
        return Topo.Outer;
    }

     boolean IsEmpty(double sinhalf)
    {
        return ((this.cos0 > 0.9999) && (this.sin0 < sinhalf));
    }

    public boolean IsEmpty()
    {
        return this.IsEmpty(Constant.SinHalf);
    }

     boolean IsAll(double sinhalf)
    {
        return ((this.cos0 < -0.9999) && (this.sin0 < sinhalf));
    }

    public boolean IsAll()
    {
        return this.IsAll(Constant.SinHalf);
    }

    public boolean Contains(Cartesian p, double costol, double sintol)
    {
        if (this.IsAll())
        {
            return true;
        }
        boolean flag = false;
        Cartesian cartesian = this.vector.Cross(p, false);
        double num = this.vector.Dot(p);
        double num3 = (cartesian.getNorm() * this.cos0) - (num * this.sin0);
        if (num3 < sintol)
        {
            flag = true;
        }
        return flag;
    }

     boolean Contains(List<Cartesian> list, double costol, double sintol)
    {
        for (Cartesian cartesian : list)
        {
            if (!this.Contains(cartesian, costol, sintol))
            {
                return false;
            }
        }
        return true;
    }

    public Cartesian GetPointWest()
    {
    	////////initialize//////////////////////////
        double[] num3 = new double[1];
        double num = Math.sin(this.getRadiusInRadian());
        double[] ra = new double[1];
        Cartesian.Xyz2Radec(this.vector.getX(), this.vector.getY(), this.vector.getZ(), /*out*/ ra, /*out*/ num3);
        double num4 = Math.sin(ra[0] * Constant.Degree2Radian);
        double num5 = Math.cos(ra[0] * Constant.Degree2Radian);
        double x = (this.vector.getX() * this.cos0) + (num4 * num);
        double y = (this.vector.getY() * this.cos0) - (num5 * num);
        return new Cartesian(x, y, this.vector.getZ() * this.cos0, false);
    }

    public int Roots(Halfspace that, /*out*/ Cartesian[] pos, /*out*/ Cartesian[] neg)
    {
    	///////////initialize////////////////////////////////////////
        Cartesian[] cartesian = new Cartesian[1];
        Cartesian[] cartesian2 = new Cartesian[1];
        double num = this.vector.Dot(that.vector);
        if (((num - 1.0) > -Constant.DoublePrecision2x) || ((num + 1.0) < Constant.DoublePrecision2x))
        {
            pos = neg = new Cartesian[]{Cartesian.NaN};
            return 0;
        }
        double num2 = 1.0 - (num * num);
        double num3 = ((this.cos0 * this.cos0) + (that.cos0 * that.cos0)) - (((2.0 * num) * this.cos0) * that.cos0);
        if ((num2 - num3) < (2.0 * Constant.DoublePrecision2x))
        {
            pos = neg = new Cartesian[]{Cartesian.NaN};
            return -3;
        }
        if (!this.GetXLine(that, /*out*/ cartesian, /*out*/ cartesian2))
        {
            pos = neg =new Cartesian[]{ Cartesian.NaN};
            return 0;
        }
        double num4 = cartesian[0].Dot(cartesian[0]);
        double num5 = 2.0 * cartesian2[0].Dot(cartesian[0]);
        double num6 = ((2.0 + ((cartesian2[0].getX() - 1.0) * (cartesian2[0].getX() + 1.0))) + ((cartesian2[0].getY() - 1.0) * (cartesian2[0].getY() + 1.0))) + ((cartesian2[0].getZ() - 1.0) * (cartesian2[0].getZ() + 1.0));
        double d = (num5 * num5) - ((4.0 * num4) * num6);
        if (d < 0.0)
        {
            pos = neg = new Cartesian[]{Cartesian.NaN};
            return -1;
        }
        double num8 = (num5 < 0.0) ? -1.0 : 1.0;
        d = Math.sqrt(d);
        double num9 = -0.5 * (num5 + (num8 * d));
        if (num4 == 0.0)
        {
            pos = neg = new Cartesian[]{Cartesian.NaN};
            return -2;
        }
        if (num9 == 0.0)
        {
            pos = neg = new Cartesian[]{Cartesian.NaN};
            return -3;
        }
        pos[0] = cartesian2[0].Add(cartesian[0].Scaled(num9 / num4));
        neg[0] = cartesian2[0].Add(cartesian[0].Scaled(num6 / num9));
        if (cartesian[0].Dot(pos[0]) < 0.0)
        {
            Cartesian cartesian3 = pos[0];
            pos = neg;
            neg[0] = cartesian3;
        }
        return 2;
    }

    public boolean GetXLine(Halfspace that, /*out*/ Cartesian[] xdir, /*out*/ Cartesian[] xpt)
    {
        double num;
        xdir[0] = this.vector.Cross(that.vector, false);
        Cartesian cartesian = new Cartesian(xdir[0].getX() * xdir[0].getX(), xdir[0].getY() * xdir[0].getY(), xdir[0].getZ() * xdir[0].getZ(), false);
        double num2 = Constant.DoublePrecision2x;
        if (((cartesian.getZ() > cartesian.getY()) && (cartesian.getZ() > cartesian.getX())) && (cartesian.getZ() > num2))
        {
            num = 1.0 / xdir[0].getZ();
            xpt[0] = new Cartesian((-this.vector.getY() * that.cos0) + (that.vector.getY() * this.cos0), (-that.vector.getX() * this.cos0) + (this.vector.getX() * that.cos0), 0.0, false);
        }
        else if ((cartesian.getY() > cartesian.getX()) && (cartesian.getY() > num2))
        {
            num = -1.0 / xdir[0].getY();
            xpt[0] = new Cartesian((-this.vector.getZ() * that.cos0) + (that.vector.getZ() * this.cos0), 0.0, (-that.vector.getX() * this.cos0) + (this.vector.getX() * that.cos0), false);
        }
        else if (cartesian.getX() > num2)
        {
            num = 1.0 / xdir[0].getX();
            xpt[0] = new Cartesian(0.0, (-this.vector.getZ() * that.cos0) + (that.vector.getZ() * this.cos0), (-that.vector.getY() * this.cos0) + (this.vector.getY() * that.cos0), false);
        }
        else
        {
            xpt[0] = Cartesian.NaN;
            return false;
        }
        xpt[0].Scale(num);
        num = 1.0 / Math.sqrt((cartesian.getX() + cartesian.getY()) + cartesian.getZ());
        xdir[0].Scale(num);
        return true;
    }

//    public static boolean operator ==(Halfspace left, Halfspace right)
//    {
//        return (((left.cos0 == right.cos0) && (left.sin0 == right.sin0)) && (left.vector == right.vector));
//    }
//
//    public static boolean operator !=(Halfspace left, Halfspace right)
//    {
//        if (!(left.vector != right.vector) && (left.cos0 == right.cos0))
//        {
//            return !(left.sin0 == right.sin0);
//        }
//        return true;
//    }

    public boolean equals(Object right)
    {
        if (right == null)
        {
            return false;
        }
        if (Objects.equals(this, right))
        {
            return true;
        }
        if (super.getClass() != right.getClass())
        {
            return false;
        }
        Halfspace halfspace = (Halfspace) right;
        return this.Equals(halfspace);
    }

    public boolean Equals(Halfspace right)
    {
        return (this.vector.equals(right.vector) &&new Double( this.cos0).equals(right.cos0));
    }

    public int hashCode()
    {
        return this.toString().hashCode();
    }

    public String toString()
    {
        return this.ToString(false);
    }

    public String ToString(boolean outsin)
    {
        String str = this.vector+" "+this.cos0;
        if (outsin)
        {
            str = str + " "+this.sin0;
        }
        return str;
    }

    public static Halfspace Parse(String repr, boolean normalize)
    {
        double num4;
        double num5;
        char[] separator = new char[] { ' ' };
        String str = repr.trim().replace(',', ' ').replace("\r\n", " ").replace('\n', ' ').replace('\t', ' ');
        String str2 = "";
        while (str2 != str)
        {
            str2 = str;
            str = str2.replace("  ", " ");
        }
        String[] strArray = str2.split(" "/*separator*/, 4);
        double x = Double.parseDouble(strArray[0]);
        double y = Double.parseDouble(strArray[1]);
        double z = Double.parseDouble(strArray[2]);
        try
        {
            num4 = Double.parseDouble(strArray[3]);
            num5 = Math.sqrt(1.0 - (num4 * num4));
        }
        catch(Exception e)
        {
            String[] strArray2 = strArray[3].split(" "/*separator*/, 2);
            num4 = Double.parseDouble(strArray2[0]);
            num5 = Double.parseDouble(strArray2[1]);
        }
        return new Halfspace(new Cartesian(x, y, z, normalize), num4, num5);
    }

    public static int ComparisonRadiusXYZ(Halfspace h, Halfspace g)
    {
        int num =new Double(g.getCos0()).compareTo(h.getCos0());
        if (num != 0)
        {
            return num;
        }
        num = new Double(h.getVector().getX()).compareTo(g.getVector().getX());
        if (num != 0)
        {
            return num;
        }
        num = new Double(h.getVector().getY()).compareTo(g.getVector().getY());
        if (num != 0)
        {
            return num;
        }
        return new Double(h.getVector().getZ()).compareTo(g.getVector().getZ());
    }

    /*static*/ Halfspace()
    {
        UnitSphere = new Halfspace(new Cartesian(0.0, 0.0, 1.0, false), -1.0, 0.0);
        Revision = "$Revision: 1.57 $";
    }

}
class ComparisonRadiusXYZComparator implements Comparator<Halfspace>
{

	public int compare(Halfspace h, Halfspace g) {
		  int num =new Double(g.getCos0()).compareTo(h.getCos0());
	        if (num != 0)
	        {
	            return num;
	        }
	        num = new Double(h.getVector().getX()).compareTo(g.getVector().getX());
	        if (num != 0)
	        {
	            return num;
	        }
	        num = new Double(h.getVector().getY()).compareTo(g.getVector().getY());
	        if (num != 0)
	        {
	            return num;
	        }
	        return new Double(h.getVector().getZ()).compareTo(g.getVector().getZ());
	}

}
