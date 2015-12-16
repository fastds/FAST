package edu.gzu.image.sphericallib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Patch implements IPatch{

	// Fields
    private Arc[] arcList;
    private double area;
    private double length;
    private Halfspace mec;
    public static final String Revision = "$Revision: 1.31 $";

    // Methods
    public Patch()
    {
    }

    public Patch(Arc[] list)
    {
        if (list.length < 1)
        {
            throw new RuntimeException("Patch(list): empty arclist(argument)");
        }
        this.arcList = list;
        this.mec = this.GetMinimalEnclosingCircle();
        double[] tempArea = new double[1];
        double[] tempLength = new double[1];
        this.CalcAreaLength(this.mec.getVector(), /*out*/ tempArea, /*out*/ tempLength);
        this.area = tempArea[0];
        this.length = tempLength[0];
    }

    public Patch(Patch p)
    {
        this.mec = p.mec;
        this.area = p.area;
        this.length = p.length;
        this.arcList = new Arc[p.arcList.length];
        for (int i = 0; i < this.arcList.length; i++)
        {
            this.arcList[i] = new Arc(p.arcList[i]);
        }
    }

    private void CalcAreaLength(Cartesian c, /*out*/ double[] area, /*out*/ double[] length)
    {
        area[0] = length[0] = 0.0;
        for (Arc arc : this.arcList)
        {
            area[0] += arc.CalcTriangleArea(c);
            length[0] += arc.getLength();
        }
    }

    public boolean ContainsOnEdge(Cartesian p)
    {
        for (Arc arc : this.getArcList())
        {
            if (arc.ContainsOnEdge(p))
            {
                return true;
            }
        }
        return false;
    }

    private Halfspace GetMinimalEnclosingCircle()
    {
        Arc arc = this.arcList[0];
        if (this.arcList.length == 1)
        {
            return arc.getCircle();
        }
        List<Cartesian> plist = new ArrayList<Cartesian>(this.arcList.length);
        List<Cartesian> list2 = new ArrayList<Cartesian>(1);
        Halfspace unitSphere = Halfspace.UnitSphere;
        boolean flag = true;
        for (Arc arc2 : this.arcList)
        {
            if (!plist.contains(arc2.getPoint1()))
            {
                plist.add(arc2.getPoint1());
            }
            if (!plist.contains(arc2.getPoint2()))
            {
                plist.add(arc2.getPoint2());
            }
            if (arc2.getCircle().getCos0() > -4.94065645841247E-324)
            {
                plist.add(arc2.getMiddle());
                flag = false;
            }
            else if (flag)
            {
                if (unitSphere.getCos0() < arc2.getCircle().getCos0())
                {
                    unitSphere = arc2.getCircle();
                }
                if (list2.size() < 1)
                {
                    list2.add(arc2.getMiddle());
                }
            }
        }
        Halfspace halfspace = Cartesian.MinimalEnclosingCircle(plist);
        if (flag)
        {
            if (!halfspace.Contains(list2.get(0), 1.0, 0.0))
            {
                halfspace.Invert();
            }
            if (unitSphere.getCos0() > halfspace.getCos0())
            {
                halfspace = unitSphere;
            }
        }
        return halfspace;
    }

    public double TestArea(Cartesian c)
    {
    	///////initialize/////////////////////////////////
        double[] num = new double[1];
        double[] num2 = new double[1];
        this.CalcAreaLength(c, /*out*/ num, /*out*/ num2);
        return num[0];
    }

    public  String toString()
    {
        String str = "# "+this.arcList.length+"\n";
        String[] strArray = new String[this.arcList.length];
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        for (int i = 0; i < this.arcList.length; i++)
        {
            strArray[i] = this.arcList[i].toString();
            if(i!=this.arcList.length-1)
            	sb.append(strArray[i]+"\n");
        }
        return (str + sb.toString());
        
    }

    // Properties

    public List<Arc> getArcList() {
		return Arrays.asList(arcList);
	}

	public void setArcList(Arc[] arcList) {
		this.arcList = arcList;
	}
	
	public double getArea() {
		return area;
	}

	public void setArea(double area) {
		this.area = area;
	}

	
    public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}


//    [XmlElement("BoundingCircle")]
	
    public Halfspace getMec() {
		return mec;
	}

	public void setMec(Halfspace mec) {
		this.mec = mec;
	}

//    List<Arc> IPatch.ArcList
//    {
//        get
//        {
//            return this.ArcList;
//        }
//    }


}
