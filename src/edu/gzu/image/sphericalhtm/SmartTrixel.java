package edu.gzu.image.sphericalhtm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import edu.gzu.image.sphericalhtm.SortableRoot;
import edu.gzu.image.sphericallib.Arc;
import edu.gzu.image.sphericallib.Cartesian;
import edu.gzu.image.sphericallib.Constant;
import edu.gzu.image.sphericallib.Halfspace;
import edu.gzu.image.sphericallib.IPatch;
import edu.gzu.image.sphericallib.PatchPart;
import edu.gzu.image.sphericallib.Region;
import edu.gzu.image.sphericallib.Topo;
class SmartTrixel implements ISmartTrixel
{
    // Fields
    private Halfspace bc;
    private static final double costol = Constant.CosTolerance;
    private Cover covmachine;
    private Arc[] edges;
    private List<SortableRoot>[] fractionals;
    private long hid;
    final int Level;
    private Topo[] localTopo;
    private SmartTrixel parent;
    private List<IPatch> plist;
    private static final double sintol = Constant.SinTolerance;
    boolean Terminal;
    private SmartVertex[] v;

    // Methods
//    private SmartTrixel()
//    {
//        this.localTopo = new Topo[3];
//        this.edges = new Arc[3];
//        this.v = new SmartVertex[3];
//        this.fractionals = new ArrayList[3];
//    }

    SmartTrixel(Cover machine)
    {
        this.localTopo = new Topo[3];
        this.edges = new Arc[3];
        this.v = new SmartVertex[3];
        this.fractionals = new ArrayList[3];
        this.parent = null;
        this.covmachine = machine;
        this.hid = 0L;
        this.Level = -1;
    }

    SmartTrixel(SmartTrixel parent, long hid, SmartVertex iv0, SmartVertex iv1, SmartVertex iv2)
    {
        this.localTopo = new Topo[3];
        this.edges = new Arc[3];
        this.v = new SmartVertex[3];
        this.fractionals = new ArrayList[3];
        this.hid = hid;
        this.parent = parent;
        this.covmachine = parent.covmachine;
        this.Level = parent.Level + 1;
        this.v[0] = iv0;
        this.v[1] = iv1;
        this.v[2] = iv2;
        this.initBCandPList();
        this.localTopo[0] = this.v[0].getTopo();
        this.localTopo[1] = this.v[1].getTopo();
        this.localTopo[2] = this.v[2].getTopo();
        this.edges[0] = new Arc(this.v[1].getVertex(), this.v[2].getVertex());
        this.edges[1] = new Arc(this.v[2].getVertex(), this.v[0].getVertex());
        this.edges[2] = new Arc(this.v[0].getVertex(), this.v[1].getVertex());
        if (this.Level == 0)
        {
            this.updateFractionals();
        }
    }

    private static int CompareTo(SortableRoot a, SortableRoot b)
    {
        return new Double(a.Lower).compareTo(b.Lower);
    }

//    [CLSCompliant(true)]
    public void Expand()
    {
        SmartVertex vertex = new SmartVertex(this.v[1].getVertex().GetMiddlePoint(this.v[2].getVertex(), true));
        SmartVertex vertex2 = new SmartVertex(this.v[2].getVertex().GetMiddlePoint(this.v[0].getVertex(), true));
        SmartVertex vertex3 = new SmartVertex(this.v[0].getVertex().GetMiddlePoint(this.v[1].getVertex(), true));
        Region reg = this.covmachine.reg;
        vertex.SetParentArcsAndTopo(this.plist, reg);
        vertex2.SetParentArcsAndTopo(this.plist, reg);
        vertex3.SetParentArcsAndTopo(this.plist, reg);
        for (int i = 0; i < 3; i++)
        {
            if ((this.fractionals[i] != null) && (this.fractionals[i].size() > 1))
            {
            	Collections.sort(this.fractionals[i]);
//       old         this.fractionals[i].Sort(new Comparison<SortableRoot>(SmartTrixel.CompareTo));
            }
        }
        long hid = this.hid << 2;
        List<ISmartTrixel> smartQue =  this.covmachine.smartQue;
        hid += 1L;
//   old     smartQue.Enqueue(new SmartTrixel(this, hid, this.v[0], vertex3, vertex2));
       smartQue.add(new SmartTrixel(this, hid, this.v[0], vertex3, vertex2));
        hid += 1L;
//  old      smartQue.Enqueue(new SmartTrixel(this, hid, this.v[1], vertex, vertex3));
        smartQue.add(new SmartTrixel(this, hid, this.v[1], vertex, vertex3));
        hid += 1L;
// old       smartQue.Enqueue(new SmartTrixel(this, hid, this.v[2], vertex2, vertex));
       smartQue.add(new SmartTrixel(this, hid, this.v[2], vertex2, vertex));
        hid += 1L;
//  old      smartQue.Enqueue(new SmartTrixel(this, hid, vertex, vertex2, vertex3));
       smartQue.add(new SmartTrixel(this, hid, vertex, vertex2, vertex3));
    }

    private List<IPatch> FilterByBC(List<? extends IPatch> patches)
    {
        List<IPatch> list = new ArrayList<IPatch>();
        for (IPatch patch : patches)
        {
            try
            {
                Topo topo =  patch.getMec().GetTopo(this.bc);
                if ((topo.ordinal() != 6) && (topo.ordinal() != 1))
                {
                    list.add(patch);
                }
            }
            catch (Exception e)
            {
            	e.printStackTrace();
            }
        }
        return list;
    }

     static List<SortableRoot> GetFractionalRoots(Arc edge, List<IPatch> in_plist, boolean simplify)
    {
        List<SortableRoot> sortableRoots = new ArrayList<SortableRoot>();
        Cartesian[] naN = {Cartesian.NaN};
        Cartesian[] root = {Cartesian.NaN};
        for (IPatch patch : in_plist)
        {
            for (Arc arc : patch.getArcList())
            {
            	//////////initialize/////////////
                int[] num = new int[1];
                Topo top = edge.getCircle().GetTopo(arc.getCircle(), /*ref*/ naN, /*ref*/ root, /*ref*/ num);
                if ((top.ordinal() == 1) || (top == null))
                {
                	////////////////////////initialize/////////////////
                    double[] num2 = new double[1];
                    double[] num3 = new double[1];
                    if (arc.IsFull())
                    {
                        num2[0] = 0.0;
                        num3[0] = 1.0;
                        sortableRoots.add(new SortableRoot(num2, num3, arc, top));
                    }
                    else if (haveCommonInterval(edge, arc, /*out*/ num2, /*out*/ num3))
                    {
                        sortableRoots.add(new SortableRoot(num2, num3, arc, top));
                    }
                }
                if (top.ordinal() == 3)
                {
                	///////////////initialize/////////////////////
                    double[] num4 = new double[1];
                    if (haveIntersect(edge, arc, naN[0], /*out*/ num4))
                    {
                        sortableRoots.add(new SortableRoot(num4, arc));
                    }
                    if (haveIntersect(edge, arc, root[0], /*out*/ num4))
                    {
                        sortableRoots.add(new SortableRoot(num4, arc));
                    }
                }
            }
        }
        if (sortableRoots.size() > 1)
        {
        	Collections.sort(sortableRoots);
//            sortableRoots.Sort(new Comparison<SortableRoot>(SmartTrixel.CompareTo));
        }
        if (simplify)
        {
            simplifySortableRoots(sortableRoots);
        }
        return sortableRoots;
    }

    public long GetHid()
    {
        return this.hid;
    }

    public int getLevel()
    {
        return this.Level;
    }

    public Markup GetMarkup()
    {
        Markup undefined = Markup.Undefined;
        for (int i = 0; i < 3; i++)
        {
            if (this.v[i].getTopo() == null)
            {
                undefined = this.setLocalTopo(i);
                if (undefined != Markup.Undefined)
                {
                    return undefined;
                }
            }
        }
        int num2 = this.nrVerticesInner();
        switch (num2)
        {
            case 1:
            case 2:
                return Markup.Partial;
        }
        if (this.plist.size() == 0)
        {
            return Markup.Reject;
        }
        for (IPatch item : this.plist)
        {
        	PatchPart part = (PatchPart)item;
            for (Arc arc : part.getArcList())
            {
                if (this.IsArcInTrixel(this.covmachine.GetSmartArc(arc), false))
                {
                    return Markup.Partial;
                }
            }
        }
        this.updateFractionals();
        if (num2 == 0)
        {
            for (int j = 0; j < 3; j++)
            {
                simplifySortableRoots(this.fractionals[j]);
            }
        }
        if (this.intersectingFractionals())
        {
            return Markup.Partial;
        }
        if (num2 == 0)
        {
            return Markup.Reject;
        }
        return Markup.Inner;
    }

    private static boolean haveCommonInterval(Arc edge, Arc a, /*out*/ double[] arclo, /*out*/ double[] archi)
    {
        double num3;
        double num4;
        arclo[0] = archi[0] = -1.0;
        double num = edge.GetAngle(a.getPoint1()) / edge.getAngle();
        double num2 = edge.GetAngle(a.getPoint2()) / edge.getAngle();
        if (num <= num2)
        {
            num3 = num;
            num4 = num2;
        }
        else
        {
            num4 = num;
            num3 = num2;
        }
        if (num4 < 0.0)
        {
            return false;
        }
        if (num3 > 1.0)
        {
            return false;
        }
        arclo[0] = (num3 < 0.0) ? 0.0 : num3;
        archi[0] = (num4 > 1.0) ? 1.0 : num4;
        return true;
    }

    private static boolean haveIntersect(Arc edge, Arc a, Cartesian root, /*out*/ double[] arcint)
    {
        boolean flag = false;
        arcint[0] = 0.0;
        double angle = edge.GetAngle(root);
        angle = (a.IsFull() && (angle >= (6.2831853071795862 - Trixel.DblTolerance))) ? 0.0 : angle;
        if ((angle <= (edge.getAngle() + Trixel.DblTolerance)) && (a.GetAngle(root) <= (a.getAngle() + Trixel.DblTolerance)))
        {
            flag = true;
            arcint[0] = angle / edge.getAngle();
        }
        return (((arcint[0] > Trixel.DblTolerance) && (arcint[0] < (1.0 - Trixel.DblTolerance))) && flag);
    }

    private void initBCandPList()
    {
        this.bc = new Halfspace(this.v[0].getVertex(), this.v[1].getVertex(), this.v[2].getVertex());
        if ((this.parent != null) && (this.parent.plist != null))
        {
            this.plist = this.FilterByBC(this.parent.plist);
        }
        if (this.plist == null)
        {
        	///////////这里调用的方法名和方法体做过改动
            this.plist = this.FilterByBC(this.covmachine.outline.getPartList());
        }
    }

    private boolean intersectingFractionals()
    {
        for (int i = 0; i < 3; i++)
        {
            for (SortableRoot root : this.fractionals[i])
            {
                if (((root.topo.ordinal() == 3) && (root.Upper > Trixel.Epsilon)) && (root.Upper < (1.0 - Trixel.Epsilon)))
                {
                    return true;
                }
            }
        }
        return false;
    }

     boolean IsArcInTrixel(SmartArc sa, boolean both)
    {
        boolean flag2;
        for (int i = 0; i < 3; i++)
        {
            if (this.v[i].getTopo() == null)
            {
                return false;
            }
        }
        if (sa.Arc.IsFull())
        {
            if (sa.Arc.getCircle().getCos0() >= -costol)
            {
                if (this.bc.GetTopo(sa.Arc.getCircle()).ordinal() == 6)
                {
                    return false;
                }
                if (Trixel.IsAncestor(this.hid, sa.Hid1))
                {
                    return true;
                }
                for (int j = 0; j < 3; j++)
                {
                    Arc parentArc = null;
                    if (this.fractionals[j] != null)
                    {
                        for (SortableRoot root : this.fractionals[j])
                        {
                            if (parentArc == null)
                            {
                                parentArc = root.ParentArc;
                            }
                            else if (root.ParentArc == parentArc)
                            {
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
        boolean flag = Trixel.IsAncestor(this.hid, sa.Hid1);
        if (sa.Hid2 == 0L)
        {
            flag2 = both;
        }
        else
        {
            flag2 = Trixel.IsAncestor(this.hid, sa.Hid2);
        }
        if (both)
        {
            return (flag && flag2);
        }
        if (!flag)
        {
            return flag2;
        }
        return true;
    }

    public boolean isTerminal()
    {
        return this.Terminal;
    }

    private int nrVerticesInner()
    {
        int num = 0;
        num += (this.localTopo[0].ordinal() == 4) ? 1 : 0;
        num += (this.localTopo[1].ordinal() == 4) ? 1 : 0;
        return (num + ((this.localTopo[2].ordinal() == 4) ? 1 : 0));
    }

    private RootError rootCheck(SortableRoot root)
    {
        if (root.Lower > root.Upper)
        {
            return RootError.LowerGTUpper;
        }
        double num = -Trixel.Epsilon;
        double num2 = 1.0 + Trixel.Epsilon;
        if (root.Lower < num)
        {
            return RootError.LowerLTZero;
        }
        if (root.Upper < num)
        {
            return RootError.UpperLTZero;
        }
        if (root.Lower > num2)
        {
            return RootError.LowerGTOne;
        }
        if (root.Upper > num2)
        {
            return RootError.UpperGTOne;
        }
        return RootError.NoError;
    }

    private Markup setLocalTopo(int ix)
    {
        Markup undefined = Markup.Undefined;
        this.localTopo[ix] = Topo.values()[5];
        int index = (ix + 1) % 3;
        int num2 = (ix + 2) % 3;
        Arc arcin = this.edges[index];
        Arc arcout = this.edges[num2];
        Wedge other = this.v[ix].makeOneWedge(arcin, arcout);
        for (Wedge wedge2 : this.v[ix].wedgelist)
        {
            switch (wedge2.Compare(other))
            {
                case Partial:
                    return Markup.Partial;

                case Inner:
                    this.localTopo[ix] = Topo.values()[4];
                    break;
            }
        }
        return undefined;
    }

    public void setTerminal(boolean value)
    {
        this.Terminal = value;
    }
//
//     static void simplifySortableRoots(List<SortableRoot> sortableRoots)
//    {
//        if ((sortableRoots != null) && (sortableRoots.size() > 0))
//        {
//            int index = 0;
//            while (index < sortableRoots.size())
//            {
//                SortableRoot root3;
//                boolean flag = false;
//                SortableRoot root = sortableRoots.get(index);
//                if (root.topo.ordinal() != 3)
//                {//满足条件的话走 Label_00A8（特点：）
//                    goto Label_00A8;
//                }
//                if (index < (sortableRoots.size() - 1))
//                {
//                    SortableRoot root2 = sortableRoots.get(index + 1);
//                    if ((root2.Lower - root.Upper) < Trixel.Epsilon)
//                    {
//                        flag = true;
//                    }
//                }
//                goto Label_00ED;
//            Label_0060:
//                root3 = sortableRoots.get(index + 1);
//                if (((root3.topo.ordinal() != 3) || (root.Lower > (root3.Lower + Trixel.Epsilon))) || (root3.Lower > (root.Upper + Trixel.Epsilon)))
//                {
//                    goto Label_00B3;
//                }
//                sortableRoots.remove(index + 1);
//            Label_00A8:
//                if (index < (sortableRoots.size() - 1))
//                {
//                    goto Label_0060;
//                	root3 = sortableRoots.get(index + 1);
//                    if (((root3.topo.ordinal() != 3) || (root.Lower > (root3.Lower + Trixel.Epsilon))) || (root3.Lower > (root.Upper + Trixel.Epsilon)))
//                    {
//                        goto Label_00B3;
//                    }
//                    sortableRoots.remove(index + 1);
//                }
//            Label_00B3:
//                if (((root.Upper - root.Lower) < Trixel.Epsilon) && ((root.Lower < Trixel.Epsilon) || ((1.0 - root.Lower) < Trixel.Epsilon)))
//                {
//                    flag = true;
//                }
//            Label_00ED:
//                if (flag)
//                {
//                    sortableRoots.remove(index);
//                }
//                else
//                {
//                    index++;
//                }
//            }
//        }
//    }
    static void simplifySortableRoots(List<SortableRoot> sortableRoots)
    {
        if ((sortableRoots != null) && (sortableRoots.size() > 0))
        {
            int index = 0;
            while (index < sortableRoots.size())
            {
                SortableRoot root3;
                boolean flag = false;
                SortableRoot root = sortableRoots.get(index);
                if (root.topo.ordinal() != 3)
                {
                	  while (index < (sortableRoots.size() - 1))
                      {
                      	root3 = sortableRoots.get(index + 1);
                          if (!(((root3.topo.ordinal() != 3) || (root.Lower > (root3.Lower + Trixel.Epsilon))) || (root3.Lower > (root.Upper + Trixel.Epsilon))))
                          {
                          	sortableRoots.remove(index + 1);
                          	continue;
                          }
                          break;
                      }
                      if (((root.Upper - root.Lower) < Trixel.Epsilon) && ((root.Lower < Trixel.Epsilon) || ((1.0 - root.Lower) < Trixel.Epsilon)))
                      {
                          flag = true;
                      }
                      if (flag)
                      {
                          sortableRoots.remove(index);
                      }
                      else
                      {
                          index++;
                      }
                      continue;
                }
                if (index < (sortableRoots.size() - 1))
                {
                    SortableRoot root2 = sortableRoots.get(index + 1);
                    if ((root2.Lower - root.Upper) < Trixel.Epsilon)
                    {
                        flag = true;
                    }
                }
                if (flag)
                {
                    sortableRoots.remove(index);
                }
                else
                {
                    index++;
                }
              
            }//while
        }
    }

    private List<SortableRoot> splitFractionals(int edgenr, boolean bottom)
    {
        List<SortableRoot> list = new ArrayList<SortableRoot>();
        for (SortableRoot root : this.parent.fractionals[edgenr])
        {
            if (this.rootCheck(root) != RootError.NoError)
            {
                throw new RuntimeException("spliFractionals starts with bad root");
            }
            double[] upper = {root.Upper};
            double[] lower = {root.Lower};
            if (bottom)
            {
                lower[0] *= 2.0;
                upper[0] *= 2.0;
                if (lower[0] < 1.0)
                {
                    upper[0] = (upper[0] > 1.0) ? 1.0 : upper[0];
                    list.add(new SortableRoot(lower, upper, root.ParentArc, root.topo));
                }
            }
            else
            {
                lower[0] = 2.0 * (lower[0] - 0.5);
                upper[0] = 2.0 * (upper[0] - 0.5);
                if (upper[0] > 0.0)
                {
                    lower[0] = (lower[0] < 0.0) ? 0.0 : lower[0];
                    list.add(new SortableRoot(lower, upper, root.ParentArc, root.topo));
                }
            }
        }
        return list;
    }

    public  String toString()
    {
        return Trixel.ToString(this.GetHid());
    }

    private void updateFractionals()
    {
        if (this.Level == 0)
        {
            for (int i = 0; i < 3; i++)
            {
                this.fractionals[i] = GetFractionalRoots(this.edges[i], this.plist, false);
            }
        }
        else
        {
            int num2 = (int) (this.hid & 3L);
            Trixel.LevelOfHid(this.hid);
            for (int j = 0; j < 3; j++)
            {
                if (this.parent.fractionals[j] == null)
                {
                    this.parent.fractionals[j] = GetFractionalRoots(this.parent.edges[j], this.parent.plist, true);
                }
            }
            switch (num2)
            {
                case 0:
                    this.fractionals[0] = GetFractionalRoots(this.edges[0], this.plist, false);
                    this.fractionals[1] = this.splitFractionals(1, false);
                    this.fractionals[2] = this.splitFractionals(2, true);
                    return;

                case 1:
                    this.fractionals[0] = GetFractionalRoots(this.edges[0], this.plist, false);
                    this.fractionals[1] = this.splitFractionals(2, false);
                    this.fractionals[2] = this.splitFractionals(0, true);
                    return;

                case 2:
                    this.fractionals[0] = GetFractionalRoots(this.edges[0], this.plist, false);
                    this.fractionals[1] = this.splitFractionals(0, false);
                    this.fractionals[2] = this.splitFractionals(1, true);
                    return;

                case 3:
                    this.fractionals[0] = GetFractionalRoots(this.edges[0], this.plist, false);
                    this.fractionals[1] = GetFractionalRoots(this.edges[1], this.plist, false);
                    this.fractionals[2] = GetFractionalRoots(this.edges[2], this.plist, false);
                    return;
            }
        }
    }

    // Properties
//    public long Hid
//    {
//        get
//        {
//            return this.hid;
//        }
//    }

    // Nested Types
    public enum RootError
    {
        NoError,
        LowerGTUpper,
        LowerLTZero,
        UpperLTZero,
        LowerGTOne,
        UpperGTOne
    }

}
