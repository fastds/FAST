package edu.gzu.image.sphericallib;

import java.util.Comparator;

class DArc implements Comparable<DArc>{
	// Fields
    public double angleEnd;
    public double angleStart;
    public Arc arc;
    public int dir;
    public PatchPart segment;

    // Methods
    public DArc(Arc arc, int dir, PatchPart segment)
    {
        this.arc = arc;
        this.dir = dir;
        this.segment = segment;
    }

     static int ComparisonAngle(DArc a, DArc b)
    {
        int num = new Double(a.angleStart).compareTo(b.angleStart);
        if (num == 0)
        {
            num = new Double(a.angleEnd).compareTo(b.angleEnd);
        }
        return num;
    }

    public String toString()
    {
        return this.dir+" : "+this.angleStart+" "+this.angleEnd ;
    }

    // Properties
    public Cartesian getPointEnd()
    {
        if (this.dir == -1)
        {
            return this.arc.getPoint1();
        }
        return this.arc.getPoint2();
    }

    public Cartesian getPointStart()
    {
        if (this.dir == -1)
        {
            return this.arc.getPoint2();
        }
        return this.arc.getPoint1();
    }

	public int compareTo(DArc b) {
		int num = new Double(this.angleStart).compareTo(b.angleStart);
        if (num == 0)
        {
            num = new Double(this.angleEnd).compareTo(b.angleEnd);
        }
        return num;
	}
}
class DArcAngleComparator implements Comparator<DArc>
{

	public int compare(DArc a, DArc b) {
		 int num = new Double(a.angleStart).compareTo(b.angleStart);
	        if (num == 0)
	        {
	            num = new Double(a.angleEnd).compareTo(b.angleEnd);
	        }
	        return num;
	}
}
