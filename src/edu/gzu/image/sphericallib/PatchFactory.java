package edu.gzu.image.sphericallib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;
class PatchFactory {
	  // Fields
    private List<Arclet> arclets;
    private Convex convex;
    private int[] ego;
    private boolean hasDegeneracy;
    private boolean[] hasRoot;
    public static final String Revision = "$Revision: 1.21 $";
    private List<Root> roots;
    private Cartesian[] west;

    // Methods
    public PatchFactory(Convex convex)
    {
        int count = convex.getHalfspaceList().size();
        this.west = new Cartesian[count];
        this.hasRoot = new boolean[count];
        this.convex = convex;
        this.hasDegeneracy = false;
        this.roots = new ArrayList<Root>(count * (count - 1));
        for (int i = 0; i < count; i++)
        {
            Halfspace halfspace = convex.getHalfspaceList().get(i);
            this.west[i] = halfspace.GetPointWest();
            for (int n = 0; n < i; n++)
            {
                Cartesian[] cartesian = new Cartesian[1];
                Cartesian[] cartesian2 = new Cartesian[1];
                Halfspace that = convex.getHalfspaceList().get(n);
                if (halfspace.Roots(that, /*out*/ cartesian, /*out*/ cartesian2) == 2)
                {
                    this.roots.add(new Root(cartesian[0], convex, i, n));
                    this.roots.add(new Root(cartesian2[0], convex, n, i));
                    this.hasRoot[i] = true;
                    this.hasRoot[n] = true;
                }
            }
        }
        for (int j = 0; j < this.hasRoot.length; j++)
        {
            if (!this.hasRoot[j])
            {
                this.roots.add(new Root(this.west[j], convex, j, j));
            }
        }
        this.ego = new int[this.roots.size()];
        for (int k = 0; k < this.ego.length; k++)
        {
            this.ego[k] = k;
        }
        for (int m = 0; m < this.roots.size(); m++)
        {
            Root root = this.roots.get(m);
            for (int num8 = 0; num8 < m; num8++)
            {
                Root root2 = this.roots.get(num8);
                if (root.getPoint().Same(root2.getPoint()))
                {
                    if (root.getStatus() == RootStatus.Inside)
                    {
                        root2.setStatus(root.getStatus());
                    }
                    else if (root2.getStatus() == RootStatus.Inside)
                    {
                        root.setStatus(root2.getStatus());
                    }
                    root.setPoint(root2.getPoint());
                    this.ego[m] = this.ego[num8];
                    this.hasDegeneracy = true;
                }
            }
        }
        this.arclets = new ArrayList<Arclet>(2 * this.roots.size());
    }

    private void BuildArclets()
    {
        for (int i = 0; i < this.convex.getHalfspaceList().size(); i++)
        {
            int[] numArray = this.SelectRootsOnCircle(i);
            if (numArray.length != 0)
            {
                for (int j = 1; j < numArray.length; j++)
                {
                    this.arclets.add(new Arclet(i, numArray[j - 1], numArray[j]));
                }
                this.arclets.add(new Arclet(i, numArray[numArray.length - 1], numArray[0]));
            }
        }
    }

    private List<Integer> ConnectArclets(List<Integer> iArclet)
    {
        List<Integer> list = new ArrayList<Integer>();
        int num = iArclet.get(0);
        Arclet arclet = this.arclets.get(num);
        int index = arclet.iRoot1;
        List<Integer> list2 = new ArrayList<Integer>(1);
        list2.add(num);
        iArclet.remove(0);
        int num3 = arclet.iRoot2;
        boolean flag = false;
        if (this.ego[num3] != this.ego[index])
        {
            while (!flag)
            {
                boolean flag2 = false;
                for (int num4 : iArclet)
                {
                    if (!list.contains(num4))
                    {
                        Arclet arclet2 = this.arclets.get(num4);
                        if (this.ego[arclet2.iRoot1] == this.ego[num3])
                        {
                            flag2 = true;
                            if (this.roots.get(arclet2.iRoot1).getParent1() == arclet2.iCircle)
                            {
                                list.add(num4);
                            }
                            else
                            {
                                list2.add(num4);
                                num3 = this.arclets.get(num4).iRoot2;
                                list.add(num4);
                            }
                            break;
                        }
                    }
                }
                if (!flag2)
                {
                    throw new RuntimeException("PatchFactory.ConnectArclets(): no matching arclet found");
                }
                if (this.ego[num3] == this.ego[index])
                {
                    flag = true;
                }
                for (int num5 : list)
                {
                    iArclet.remove(num5);
                }
                list.clear();
            }
            return list2;
        }
        return list2;
    }

     List<Patch> DerivePatches(boolean simplify_convex)
    {
        List<Integer> list3 = null;
        this.BuildArclets();
        List<Integer> iArclet = this.SelectArcletsVisible(RootStatus.Inside);
        if (this.hasDegeneracy)
        {
            this.PruneGoodArclets(iArclet);
        }
        List<Integer> s = this.SelectCirclesWithArclets(iArclet);
        this.SelectCompleteCircles(s, list3);
        List<Patch> list4 = new ArrayList<Patch>();
        while (iArclet.size() > 0)
        {
            List<Integer> list5 = this.ConnectArclets(iArclet);
            Arc[] list = new Arc[list5.size()];
            for (int i = 0; i < list5.size(); i++)
            {
                int num2 = list5.get(i);
                list[i] = this.MakeArc(this.arclets.get(num2));
            }
            Patch item = new Patch(list);
            if ((list.length == 2) && list[0].getMiddle().Same(list[1].getMiddle()))
            {
                item.setArea(new Double(0.0));
            }
            list4.add(item);
        }
        if (simplify_convex)
        {
            for (int j = this.convex.getHalfspaceList().size() - 1; j >= 0; j--)
            {
                if (!s.contains(j))
                {
                    this.convex.RemoveAt(j);
                }
            }
        }
        return list4;
    }

    private void InitRootGoodness(List<Integer> S)
    {
        for (Root root : this.roots)
        {
            if (root.getStatus() == RootStatus.Inside)
            {
                root.setGood(1);
            }
            else if (!S.contains(root.getParent1()) || !S.contains(root.getParent2()))
            {
                root.setGood(-1);
            }
            else if (root.MaskedByExists(S))
            {
                root.setGood(0);
            }
            else
            {
                root.setGood(-2);
            }
        }
    }
    private Arc MakeArc(Arclet arclet)
    {
        if (arclet.iRoot1 == arclet.iRoot2)
        {
            return new Arc(this.convex.getHalfspaceList().get(arclet.iCircle), this.roots.get(arclet.iRoot1).getPoint());
        }
        return new Arc(this.convex.getHalfspaceList().get(arclet.iCircle), this.roots.get(arclet.iRoot1).getPoint(), this.roots.get(arclet.iRoot2).getPoint());
    }

    private void PruneGoodArclets(List<Integer> iArclet)
    {
        List<Integer> list = new ArrayList<Integer>(iArclet.size());
        for (int num : iArclet)
        {
            if (!list.contains(num))
            {
                for (int num2 : iArclet)
                {
                    if (((num != num2) && !list.contains(num2)) && ((this.ego[this.arclets.get(num).iRoot1] == this.ego[this.arclets.get(num2).iRoot1]) && (this.ego[this.arclets.get(num).iRoot2] == this.ego[this.arclets.get(num2).iRoot2])))
                    {
                        Arc arc = this.MakeArc(this.arclets.get(num));
                        Arc arc2 = this.MakeArc(this.arclets.get(num2));
                        Cartesian middle = arc.getMiddle();
                        Cartesian cartesian2 = arc2.getMiddle();
                        double num3 = middle.Dot(arc2.getCircle().getVector()) - arc2.getCircle().getCos0();
                        double num4 = cartesian2.Dot(arc.getCircle().getVector()) - arc.getCircle().getCos0();
                        if (num3 < num4)
                        {
                            list.add(num);
                        }
                        else
                        {
                            list.add(num2);
                        }
                    }
                }
            }
        }
        for (int num5 : list)
        {
            iArclet.remove(num5);
        }
    }

    private List<Integer> SelectArcletsVisible(RootStatus status)
    {
        List<Integer> list = new ArrayList<Integer>();
        double cosTolerance = Constant.CosTolerance;
        double sinTolerance = Constant.SinTolerance;
        for (int i = 0; i < this.arclets.size(); i++)
        {
            Arclet arclet = this.arclets.get(i);
            if (((status == RootStatus.Any) || ((this.roots.get(arclet.iRoot1).getStatus() == status) && (this.roots.get(arclet.iRoot2).getStatus() == status))) && (((arclet.iCircle == this.roots.get(arclet.iRoot1).getParent2()) && (arclet.iCircle == this.roots.get(arclet.iRoot2).getParent1())) && ((arclet.iRoot1 == arclet.iRoot2) || (this.ego[arclet.iRoot1] != this.ego[arclet.iRoot2]))))
            {
                Cartesian middle = this.MakeArc(arclet).getMiddle();
                boolean flag = true;
                for (int j = 0; j < this.convex.getHalfspaceList().size(); j++)
                {
                    if (j != arclet.iCircle)
                    {
                        Halfspace halfspace = this.convex.getHalfspaceList().get(j);
                        if (!halfspace.Contains(middle, cosTolerance, sinTolerance))
                        {
                            flag = false;
                            break;
                        }
                    }
                }
                if (flag)
                {
                    list.add(i);
                }
            }
        }
        return list;
    }

    private List<Integer> SelectCirclesWithArclets(List<Integer> iGoodArclets)
    {
        List<Integer> list = new ArrayList<Integer>(iGoodArclets.size());
        for (int num2 : iGoodArclets)
        {
            int iCircle = this.arclets.get(num2).iCircle;
            if (!list.contains(iCircle))
            {
                list.add(iCircle);
            }
        }
        return list;
    }

    private void SelectCompleteCircles(List<Integer> S, /*out*/ List<Integer> Visible)
    {
        Visible = new ArrayList<Integer>(S);
        int maximumIteration = Global.getParameter().getMaximumIteration();
        double cosTolerance = Constant.CosTolerance;
        double sinTolerance = Constant.SinTolerance;
        int num2 = 0;
        while (num2 < maximumIteration)
        {
            this.InitRootGoodness(S);
            boolean flag = false;
            for (Root root : this.roots)
            {
                if (root.getGood() == -2)
                {
                    flag = true;
                    break;
                }
            }
            if (!flag)
            {
                break;
            }
            int num5 = -1;
            int item = 0x5f5e0ff;
            for (int i = 0; i < this.convex.getHalfspaceList().size(); i++)
            {
                if (!S.contains(i))
                {
                    int num7 = 0;
                    for (Root root2 : this.roots)
                    {
                        if (root2.getGood() == -2)
                        {
                            Halfspace halfspace = this.convex.getHalfspaceList().get(i);
                            if (!halfspace.Contains(root2.getPoint(), cosTolerance, sinTolerance))
                            {
                                num7++;
                            }
                        }
                    }
                    if (num7 > num5)
                    {
                        num5 = num7;
                        item = i;
                    }
                }
            }
            if (num5 == -1)
            {
                throw new RuntimeException("PatchFactory.SelectCompleteCircles(): nmax=-1 should never happen!");
            }
            S.add(item);
            num2++;
        }
        if (num2 == maximumIteration)
        {
            throw new RuntimeException("PatchFactory.SelectCompleteCircles(): exceeded maximum number of iterations ("+maximumIteration+")");
        }
    }

    private int[] SelectRootsOnCircle(int c)
    {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < this.roots.size(); i++)
        {
            if ((c == this.roots.get(i).getParent1()) || (c == this.roots.get(i).getParent2()))
            {
                list.add(i);
            }
        }
        Arc arc = new Arc(this.convex.getHalfspaceList().get(c), this.west[c]);
        double[] keys = new double[list.size()];
        Map<Double,Integer> map = new TreeMap<Double,Integer>(new KeyComparator());
        for (int j = 0; j < list.size(); j++)
        {
            Cartesian point = this.roots.get(list.get(j)).getPoint();
            keys[j] = arc.GetAngle(point);
            map.put(keys[j], list.get(j));
        }
        
//  old     int[] items = list.toArray();
//  old     Array.Sort<Double, Integer>(keys, items);
        int[] items = new int[map.size()];
        int i = 0;
        for(Double key : map.keySet())
        {
        	int value = map.get(key);
        	items[i] = value;
        }
        return items;
    }

    public String toString()
    {
        return this.ToString(RootStatus.Any);
    }

    public String ToString(RootStatus status)
    {
        String str = "Roots:";
        for (int i = 0; i < this.roots.size(); i++)
        {
            if ((status == RootStatus.Any) || (this.roots.get(i).getStatus() == status))
            {
                str = str + "\n\t"+i+". "+this.ego[i]+"  "+ this.roots.get(i);
            }
        }
        if (this.arclets.size() > 0)
        {
            str = str + "\nArclets:";
            for (int j = 0; j < this.arclets.size(); j++)
            {
                str = str + "\n\t"+j+". "+this.arclets.get(j)+" " ;
            }
        }
        return str;
    }

    // Nested Types
//    [StructLayout(LayoutKind.Sequential)]
    private class Arclet
    {
        public int iCircle;
        public int iRoot1;
        public int iRoot2;
        public Arclet(int iCircle, int iRoot1, int iRoot2)
        {
            this.iCircle = iCircle;
            this.iRoot1 = iRoot1;
            this.iRoot2 = iRoot2;
        }

        public String toString()
        {
            return  this.iCircle+" -> "+this.iRoot1+"  on "+this.iRoot2 ;
        }
    }
}
class KeyComparator implements Comparator<Double>
{

	public int compare(Double o1, Double o2) {
		return o1.compareTo(o2);
	}
	
}
