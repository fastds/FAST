package edu.gzu.image.sphericalhtm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.gzu.image.sphericallib.Arc;
import edu.gzu.image.sphericallib.Cartesian;
import edu.gzu.image.sphericallib.Constant;
import edu.gzu.image.sphericallib.IPatch;
import edu.gzu.image.sphericallib.Region;
import edu.gzu.image.sphericallib.Topo;

public class SmartVertex {
	// Fields
    private static final double costol = Constant.CosTolerance;
    private static final double sintol = Constant.SinTolerance;
    private Topo topo;
    private Cartesian up;
    private Cartesian v;
    List<Wedge> wedgelist;
    private Cartesian west;

    // Methods
//    [CLSCompliant(false)]
    public SmartVertex(Cartesian v)
    {
        this.v = v;
//        this.topo = 3;
        this.topo = Topo.values()[3];
        this.v.Tangent( this.west,  this.up);
    }

//    [CLSCompliant(true)]
    public SmartVertex(Cartesian v, boolean inside)
    {
    	this(v);
        if (inside)
        {
//            this.topo = 4;
            this.topo = Topo.values()[4];
        }
        else
        {
//            this.topo = 5;
        	 this.topo = Topo.values()[5];
        }
    }

//    public double AngleInRadian(SmartVertex right)
//    {
//        return this.v.AngleInRadian(right.v);
//    }
//
//    public boolean Equals(SmartVertex right)
//    {
//        return this.v.Equals(right.v);
//    }

    private double GetArcPosAngle(Arc a)
    {
        double y = -a.getCircle().getVector().Dot(this.up);
        double x = a.getCircle().getVector().Dot(this.west);
        return Math.atan2(y, x);
    }

    private List<PositionAngle> GetParentAngles(List<Arc> parentArcs)
    {
        List<PositionAngle> list = new ArrayList<PositionAngle>(2 * parentArcs.size());
        for (Arc arc : parentArcs)
        {
            double angle = arc.GetAngle(this.getVertex());
            double arcPosAngle = this.GetArcPosAngle(arc);
            if (arc.IsFull())
            {
                if (arc.getCircle().getCos0() < (-1.0 + Trixel.DblTolerance))
                {
                    list.add(new PositionAngle(0.0, PositionAngle.Direction.End));
                    list.add(new PositionAngle(6.2831853071795862, PositionAngle.Direction.Begin));
                }
                else
                {
                    list.add(new PositionAngle(arcPosAngle, PositionAngle.Direction.End));
                    arcPosAngle += 3.1415926535897931;
                    list.add(new PositionAngle(arcPosAngle, PositionAngle.Direction.Begin));
                }
            }
            else if (angle < Trixel.Epsilon2)
            {
                list.add(new PositionAngle(arcPosAngle, PositionAngle.Direction.End));
            }
            else if (angle > (arc.getAngle() - Trixel.Epsilon2))
            {
                arcPosAngle += 3.1415926535897931;
                list.add(new PositionAngle(arcPosAngle, PositionAngle.Direction.Begin));
            }
            else
            {
                list.add(new PositionAngle(arcPosAngle, PositionAngle.Direction.End));
                arcPosAngle += 3.1415926535897931;
                list.add(new PositionAngle(arcPosAngle, PositionAngle.Direction.Begin));
            }
        }
        Collections.sort(list);
//        list.Sort(new Comparison<PositionAngle>(PositionAngle.CompareTo));
        return list;
    }

     Wedge makeOneWedge(Arc arcin, Arc arcout)
    {
        double num = this.GetArcPosAngle(arcin) + 3.1415926535897931;
        PositionAngle angle = new PositionAngle(num, PositionAngle.Direction.Begin);
        PositionAngle angle2 = new PositionAngle(this.GetArcPosAngle(arcout), PositionAngle.Direction.End);
        return new Wedge(angle2.Angle, angle.Angle);
    }

    private void makeWedgeList(List<Arc> parentArcs)
    {
        this.wedgelist = new ArrayList<Wedge>();
        List<PositionAngle> parentAngles = this.GetParentAngles(parentArcs);
        int count = parentAngles.size();
        if (count >= 2)
        {
            int num2;
            int num3 = -1;
            for (num2 = 0; num2 < parentAngles.size(); num2++)
            {
                if (parentAngles.get(num2).State == PositionAngle.Direction.End)
                {
                    num3 = num2;
                    break;
                }
            }
            if (num3 >= 0)
            {
                num2 = num3;
//     old           int num1 = count % 2;
                for (int i = 0; i < count; i += 2)
                {
                    int num5 = (num2 + 1) % count;
                    this.wedgelist.add(new Wedge(parentAngles.get(num2).Angle, parentAngles.get(num5).Angle));
                    num2 += 2;
                }
            }
        }
    }

//    [CLSCompliant(true)]
    public void SetParentArcsAndTopo(List<IPatch> plist, Region reg)
    {
        this.topo = reg.Contains(this.getVertex(), costol, sintol) ? Topo.values()[4] : Topo.values()[5];
        if (this.topo == Topo.values()[4])
        {
            List<Arc> parentArcs = new ArrayList<Arc>();
            for (IPatch patch : plist)
            {
                for (Arc arc : patch.getArcList())
                {
                    if (arc.ContainsOnEdge(this.getVertex()))
                    {
                        parentArcs.add(arc);
                        this.topo = /*0*/Topo.values()[0];
                    }
                }
            }
            this.makeWedgeList(parentArcs);
        }
    }
//
//    // Properties
//    public boolean IsInside
//    {
//        get
//        {
//            if (this.topo != 4)
//            {
//                return (this.topo == 0);
//            }
//            return true;
//        }
//        set
//        {
//            if (value)
//            {
//                this.topo = 4;
//            }
//            else
//            {
//                this.topo = 5;
//            }
//        }
//    }
//
//    [CLSCompliant(true)]
    public Topo getTopo()
    {
        return this.topo;
    }
    public void setTopo(Topo value)
    {
    	this.topo = value;
    }

    public Cartesian getVertex()
    {
        return this.v;
    }

}
