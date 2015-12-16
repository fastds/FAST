package edu.gzu.image.sphericalhtm;

import edu.gzu.image.sphericallib.Arc;
import edu.gzu.image.sphericallib.Topo;

class SortableRoot implements Comparable<SortableRoot>{
	  // Fields
     double Lower;
    public Arc ParentArc;
    public Topo topo;
     double Upper;

    // Methods
    public SortableRoot(double[] angle, Arc arc)
    {
    	this(angle, angle, arc, Topo.values()[3]);
    }

    public SortableRoot(double[] low, double[] high, Arc arc, Topo top)
    {
        this.Lower = low[0];
        this.Upper = high[0];
        this.ParentArc = arc;
        this.topo = top;
    }

    public boolean isOne()
    {
        return (this.Upper >= (1.0 - Trixel.DblTolerance));
    }

    public boolean isZero()
    {
        return (this.Lower <= Trixel.DblTolerance);
    }

    public String toString()
    {
        char ch;
        if (this.topo == Topo.values()[3])
        {
            ch = 'X';
        }
        else if (this.topo == null)
        {
            ch = 'S';
        }
        else if (this.topo == Topo.values()[1])
        {
            ch = 'I';
        }
        else
        {
            ch = '?';
        }
        char ch2 = ' ';
        if ((this.Upper - this.Lower) < Trixel.DblTolerance)
        {
            return ch+","+this.Lower+":("+ch2+")";
        }
        return ch+","+ch2+":(_"+this.Lower+"  _"+this.Upper+")";
    }

	public int compareTo(SortableRoot o) {
		return new Double(this.Lower).compareTo(o.Lower);
	}


}
