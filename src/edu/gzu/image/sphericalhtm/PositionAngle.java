package edu.gzu.image.sphericalhtm;

class PositionAngle implements Comparable<PositionAngle>{
	// Fields
     double Angle;
     Direction State;

    // Methods
     PositionAngle(double angle, Direction state)
    {
        if (angle < 0.0)
        {
            this.Angle = angle + 6.2831853071795862;
        }
        else
        {
            this.Angle = angle;
        }
        this.State = state;
    }

    public static int CompareTo(PositionAngle a, PositionAngle b)
    {
        return new Double(a.Angle).compareTo(b.Angle);
    }

    public  String ToString()
    {
        return "("+this.State+","+this.Angle+")" ;
    }

    // Nested Types
     enum Direction
    {
        Begin,
        End,
        Undefined
    }

	public int compareTo(PositionAngle o) {
		return new Double(this.Angle).compareTo(o.Angle);
	}


}
