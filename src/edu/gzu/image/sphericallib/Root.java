package edu.gzu.image.sphericallib;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Root {
	 // Fields
    private List<Integer> coveredBy;
    private int good;
    private List<Integer> maskedBy;
    private int parent1;
    private int parent2;
    private Cartesian point;
    public static final String Revision = "$Revision: 1.14 $";
    private RootStatus status;

    // Methods
    public Root(Cartesian point, Convex c, int parent1, int parent2)
    {
        this.point = point;
        this.parent1 = parent1;
        this.parent2 = parent2;
        this.good = 0;
        double cosTolerance = Constant.CosTolerance;
        double sinTolerance = Constant.SinTolerance;
        this.coveredBy = new ArrayList<Integer>(c.getCount());
        this.maskedBy = new ArrayList<Integer>(c.getCount());
        this.status = RootStatus.Inside;
        for (int i = 0; i < c.getCount(); i++)
        {
            Halfspace halfspace = c.getHalfspaceList().get(i);
            if ((i != parent1) && (i != parent2))
            {
                if (halfspace.Contains(point, 1.0, Constant.DoublePrecision * 10.0))
                {
                    this.coveredBy.add(i);
                }
                else
                {
                    this.maskedBy.add(i);
                    this.status = RootStatus.Outside;
                }
            }
        }
    }

//    public boolean MaskedByExists(Predicate<Integer> match)
//    {
//        return this.maskedBy.Exists(match);
//    }
    public boolean MaskedByExists(List<Integer> match)
    {
    	return this.maskedBy.retainAll(match);
    }

    public String toString()
    {
        return "("+this.parent1+","+this.parent2+") "+this.status+" "+this.point+" ";
    }

    // Properties
    
    public int getGood() {
		return good;
	}

	public void setGood(int good) {
		this.good = good;
	}

    public Cartesian getPoint() {
		return point;
	}

	public void setPoint(Cartesian point) {
		this.point = point;
	}

	public RootStatus getStatus() {
		return status;
	}

	public void setStatus(RootStatus status) {
		this.status = status;
	}

	public int getParent1() {
		return parent1;
	}

	public int getParent2() {
		return parent2;
	}

}
