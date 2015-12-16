package edu.gzu.image.sphericallib;

import java.util.Comparator;

class DArcChange implements Comparable<DArcChange>{
	 // Fields
    public double angle;
    public int change;
    public DArc darc;

    // Methods
    public DArcChange(DArc darc, int change, double angle)
    {
        this.darc = darc;
        this.change = change;
        this.angle = angle;
    }

    static int ComparisonAngle(DArcChange a, DArcChange b)
    {
        return new Double(a.angle).compareTo(b.angle);
    }

    public  String ToString()
    {
        return this.change+" "+ this.angle;
    }

    // Properties
    public boolean IsStart()
    {
            switch ((this.change * this.darc.dir))
            {
                case -1:
                    return false;

                case 1:
                    return true;
            }
            throw new RuntimeException("change not +/- 1?");
    }

    public Cartesian getPoint()
    {
            switch (this.change)
            {
                case -1:
                    return this.darc.arc.getPoint2();

                case 1:
                    return this.darc.arc.getPoint1();
            }
            throw new RuntimeException("change not +/- 1?");
    }


	public int compareTo(DArcChange b) {
		return new Double(this.angle).compareTo(b.angle);
	}

}
class DArcChangeAngleComparator implements Comparator<DArcChange>
{

	public int compare(DArcChange a, DArcChange b) {
		return new Double(a.angle).compareTo(b.angle);
	}
}