package edu.gzu.image.sphericallib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class Outline {

	  // Fields
    private Hashtable<Halfspace, Dictionary<Integer, List<DArc>>> circleArcs = new Hashtable<Halfspace, Dictionary<Integer, List<DArc>>>();
    private static final double eps = (Constant.Tolerance / 100.0);
    private double length;
    private List<PatchPart> partList;
    private List<Convex> convexList;
    private List<Patch> patchList;
    public static final String Revision = "$Revision: 1.26 $";

    // Methods
//    public Outline(List<Patch> patches)
//    {
//        this.patchList = patches;
//        this.partList = new ArrayList<PatchPart>();
//        for (Patch patch : patches)
//        {
//            PatchPart item = new PatchPart ();
//            item.setMec(patch.getMec());
//            this.partList.add(item);
//        }
//        this.Build();
//    }
    ///后面自己添加的构造函数
    public Outline(List<Convex> convexs)
    {
        this.convexList = convexs;
        this.partList = new ArrayList<PatchPart>();
        for(Convex convex : this.convexList)
        {
        	List<Patch> patches = convex.getPatchList();
        	this.patchList = patches;
        	for (Patch patch : patches)
            {
                PatchPart item = new PatchPart();
                item.setMec(patch.getMec());
                this.partList.add(item);
            }
        }
        
        this.Build();
    }

    private void Build()
    {
        this.BuildCircleArcs();
        for (Enumeration<Halfspace> en = this.circleArcs.keys();en.hasMoreElements();)
        {
        		Halfspace key = en.nextElement();
        		Dictionary<Integer, List<DArc>> dictionary = this.circleArcs.get(key);
        		List<DArc> lpos = dictionary.get(1);
                List<DArc> lneg = dictionary.get(-1);
                if (lpos.size() == 0)
                {
                    for (DArc arc : lneg)
                    {
                        arc.segment.getArcList().add(new Arc(arc.arc));
                    }
                }
                else if (lneg.size() == 0)
                {
                    for (DArc arc2 : lpos)
                    {
                        arc2.segment.getArcList().add(new Arc(arc2.arc));
                    }
                }
                else
                {
                    Cartesian p = lpos.get(0).arc.getPoint1();
                    Arc full = new Arc(key, p);
                    Arc fullinv = new Arc(key.Inverse(), p);
                    List<DArcChange> dac = BuildChangeList(full, fullinv, lpos, lneg);
                    DArc lastNeg = lneg.get(lneg.size() - 1);
                    this.Sweep(full, fullinv, dac, lastNeg);
                }
        	}
    }

    private static List<DArcChange> BuildChangeList(Arc full, Arc fullinv, List<DArc> lpos, List<DArc> lneg)
    {
        lpos.get(0).angleStart = 0.0;
        lpos.get(0).angleEnd = lpos.get(0).arc.getAngle();
        for (int i = 1; i < lpos.size(); i++)
        {
            DArc arc = lpos.get(i);
            arc.angleStart = full.GetAngle(arc.arc.getPoint1());
            arc.angleEnd = arc.angleStart + arc.arc.getAngle();
        }
        for (DArc arc2 : lneg)
        {
            if (arc2.arc.IsFull())
            {
                arc2.angleStart = 0.0;
                arc2.arc = fullinv;
                arc2.angleEnd = arc2.arc.getAngle();
            }
            else
            {
                arc2.angleStart = full.GetAngle(arc2.arc.getPoint2());
                arc2.angleEnd = arc2.angleStart + arc2.arc.getAngle();
                if (arc2.angleStart >= 6.2831853071795862)
                {
                    arc2.angleStart -= 6.2831853071795862;
                    arc2.angleEnd -= 6.2831853071795862;
                }
            }
        }
        Collections.sort(lpos,new DArcAngleComparator());
        Collections.sort(lneg,new DArcAngleComparator());
        double angleEnd = -1.0;
        double num3 = 10.0 * Constant.DoublePrecision;
        for (DArc arc3 : lpos)
        {
            if (arc3.angleStart <= angleEnd)
            {
                arc3.angleStart = angleEnd + num3;
            }
            if (arc3.angleEnd <= arc3.angleStart)
            {
                arc3.angleEnd = arc3.angleStart + num3;
            }
            angleEnd = arc3.angleEnd;
        }
        angleEnd = -1.0;
        for (DArc arc4 : lneg)
        {
            if (arc4.angleStart <= angleEnd)
            {
                arc4.angleStart = angleEnd + num3;
            }
            if (arc4.angleEnd <= arc4.angleStart)
            {
                arc4.angleEnd = arc4.angleStart + num3;
            }
            if (arc4.angleEnd > 6.2831853071795862)
            {
                arc4.angleEnd -= 6.2831853071795862;
                if (arc4.angleEnd >= lneg.get(0).angleStart)
                {
                    arc4.angleEnd = lneg.get(0).angleStart - num3;
                }
            }
            angleEnd = arc4.angleEnd;
        }
        if ((lneg.get(lneg.size() - 1).angleStart > lneg.get(lneg.size() - 1).angleEnd) && (lneg.get(0).angleStart <= lneg.get(lneg.size() - 1).angleEnd))
        {
            DArc local1 = lneg.get(lneg.size() - 1);
            local1.angleEnd -= num3;
            if (lneg.get(lneg.size() - 1).angleEnd < 0.0)
            {
                DArc local2 = lneg.get(lneg.size() - 1);
                local2.angleEnd += 6.2831853071795862;
            }
        }
        List<DArcChange> list = new ArrayList<DArcChange>(2 * (lpos.size() + lneg.size()));
        for (DArc arc5 : lpos)
        {
            list.add(new DArcChange(arc5, 1, arc5.angleStart));
            list.add(new DArcChange(arc5, -1, arc5.angleEnd));
        }
        for (DArc arc6 : lneg)
        {
            list.add(new DArcChange(arc6, -1, arc6.angleStart));
            list.add(new DArcChange(arc6, 1, arc6.angleEnd));
        }
//        list.Sort(new Comparison<DArcChange>(DArcChange.ComparisonAngle));
        Collections.sort(list, new DArcChangeAngleComparator());
        return list;
    }

    private void BuildCircleArcs()
    {
        int num = 0;
        for (Patch patch : this.patchList)
        {
            PatchPart segment = this.partList.get(num++);
            for (Arc arc : patch.getArcList())
            {
                Halfspace circle = arc.getCircle();
                Halfspace key = circle.Inverse();
                if (this.circleArcs.containsKey(circle))
                {
                    this.circleArcs.get(circle).get(1).add(new DArc(arc, 1, segment));
                }
                else if (this.circleArcs.containsKey(key))
                {
                    this.circleArcs.get(key).get(-1).add(new DArc(arc, -1, segment));
                }
                else if (circle.getESign() == ESign.Negative)
                {
                    this.circleArcs.put(key, new Hashtable<Integer, List<DArc>>());
                    this.circleArcs.get(key).put(1,  new ArrayList<DArc>());
                    this.circleArcs.get(key).put(-1,  new ArrayList<DArc>());
                    this.circleArcs.get(key).get(-1).add(new DArc(arc, -1, segment));
                }
                else
                {
                    this.circleArcs.put(circle, new Hashtable<Integer, List<DArc>>());
                    this.circleArcs.get(circle).put(1,new ArrayList<DArc>());
                    this.circleArcs.get(circle).put(-1,new ArrayList<DArc>());
                    this.circleArcs.get(circle).get(1).add(new DArc(arc, 1, segment));
                }
            }
        }
    }

//    public IEnumerable<IPatch> EnumIPatches()
//    {
//        List<PatchPart>.Enumerator enumerator = this.getPartList().GetEnumerator();
//        while (enumerator.MoveNext())
//        {
//            IPatch current = enumerator.Current;
//            yield return current;
//        }
//        enumerator.Dispose();
//    }

    private void Sweep(Arc full, Arc fullinv, List<DArcChange> dac, DArc lastNeg)
    {
        Cartesian cartesian = full.getPoint1();
        int num = 0;
        int num2 = 0;
        DArc arc = null;
        DArc arc2 = null;
        DArc arc3 = null;
        DArcChange change = null;
        DArcChange change2 = null;
        if (lastNeg.angleEnd < lastNeg.angleStart)
        {
            num--;
            num2--;
            arc2 = lastNeg;
            arc3 = arc2;
            DArc darc = new DArc(new Arc(lastNeg.arc), -1, lastNeg.segment);
            change = new DArcChange(darc, -1, 0.0);
            change.darc.arc.setPoint2(cartesian) ;
        }
        double num3 = Constant.Tolerance / 1000.0;
        DArcChange change3 = null;
        for (int i = 0; i < dac.size(); i++)
        {
            boolean flag;
            Arc arc5;
            change3 = dac.get(i);
            num += change3.change;
            switch (num)
            {
                case -1:
                {
                    if (change3.IsStart())
                    {
                        arc2 = change3.darc;
                    }
                    arc = null;
                    arc3 = arc2;
                    change = change3;
                    continue;
                }
                case 0:
                    change2 = change3;
                    flag = (change2.angle - change.angle) < num3;
                    if (arc3.dir != 1)
                    {
                        break;
                    }
                    if (!flag)
                    {
                        try
                        {
                            arc5 = new Arc(full.getCircle(), change.getPoint(), change2.getPoint());
                            arc3.segment.getArcList().add(arc5);
                        }
                        catch (Exception e)
                        {
                        }
                    }
                    if (change3.IsStart())
                    {
                        arc2 = change3.darc;
                    }
                    else
                    {
                        arc = null;
                    }
//                    goto Label_01C6;
                    arc3 = null;
                    continue;

                case 1:
                {
                    if (change3.IsStart())
                    {
                        arc = change3.darc;
                    }
                    arc2 = null;
                    arc3 = arc;
                    change = change3;
                    continue;
                }
                default:
                    throw new RuntimeException("Outline.Build(): invalid sweep value on circle\n\t "+change3.darc.arc.getCircle());
            }
            if (!flag)
            {
                try
                {
                    arc5 = new Arc(fullinv.getCircle(), change2.getPoint(), change.getPoint());
                    arc3.segment.getArcList().add(arc5);
                }
                catch (Exception e)
                {
                }
            }
            if (change3.IsStart())
            {
                arc = change3.darc;
            }
            else
            {
                arc2 = null;
            }
//        Label_01C6:
            arc3 = null;
        }
        if (num != num2)
        {
            throw new RuntimeException("Outline.Build(): sweepEnd != sweepStart");
        }
        if ((num == -1) && (Math.abs((double) (change3.angle - 6.2831853071795862)) >= num3))
        {
            try
            {
                Arc item = new Arc(fullinv.getCircle(), cartesian, change3.getPoint());
                arc3.segment.getArcList().add(item);
            }
            catch (Exception e)
            {
            }
        }
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        for (PatchPart part : this.partList)
        {
            builder.append(part.toString()+"\n");
        }
        return builder.toString();
    }

    // Properties
    public double getLength()
    {
        return this.length;
    }
    public void setLength(double value)
    {
    	this.length = value;
    }

    public List<PatchPart> getPartList() {
		return partList;
	}

	public void setPartList(List<PatchPart> partList) {
		this.partList = partList;
	}



    // Nested Types
//    [CompilerGenerated]
//    private sealed class <EnumIPatches>d__0 : IEnumerable<IPatch>, IEnumerable, IEnumerator<IPatch>, IEnumerator, IDisposable
//    {
//        // Fields
//        private int <>1__state;
//        private IPatch <>2__current;
//        public Outline <>4__this;
//        public List<PatchPart>.Enumerator <>7__wrap2;
//        public IPatch <p>5__1;

        // Methods
//        [DebuggerHidden]
//        public <EnumIPatches>d__0(int <>1__state)
//        {
//            this.<>1__state = <>1__state;
//        }

//        private boolean MoveNext()
//        {
//            boolean flag;
//            try
//            {
//                switch (this.<>1__state)
//                {
//                    case 0:
//                        this.<>1__state = -1;
//                        this.<>7__wrap2 = this.<>4__this.PartList.GetEnumerator();
//                        this.<>1__state = 1;
//                        goto Label_0070;
//
//                    case 2:
//                        this.<>1__state = 1;
//                        goto Label_0070;
//
//                    default:
//                        goto Label_0095;
//                }
//            Label_0041:
//                this.<p>5__1 = this.<>7__wrap2.Current;
//                this.<>2__current = this.<p>5__1;
//                this.<>1__state = 2;
//                return true;
//            Label_0070:
//                if (this.<>7__wrap2.MoveNext())
//                {
//                    goto Label_0041;
//                }
//                this.<>1__state = -1;
//                this.<>7__wrap2.Dispose();
//            Label_0095:
//                flag = false;
//            }
//            fault
//            {
//                ((IDisposable) this).Dispose();
//            }
//            return flag;
//        }

//        [DebuggerHidden]
//        IEnumerator<IPatch> IEnumerable<IPatch>.GetEnumerator()
//        {
//            if (Interlocked.CompareExchange(ref this.<>1__state, 0, -2) == -2)
//            {
//                return this;
//            }
//            return new Outline.<EnumIPatches>d__0(0) { <>4__this = this.<>4__this };
//        }

//        [DebuggerHidden]
//        IEnumerator IEnumerable.GetEnumerator()
//        {
//            return this.System.Collections.Generic.IEnumerable<Spherical.IPatch>.GetEnumerator();
//        }
//
////        [DebuggerHidden]
//        void IEnumerator.Reset()
//        {
//            throw new NotSupportedException();
//        }
//
//        void IDisposable.Dispose()
//        {
//            switch (this.<>1__state)
//            {
//                case 1:
//                case 2:
//                    try
//                    {
//                    }
//                    finally
//                    {
//                        this.<>1__state = -1;
//                        this.<>7__wrap2.Dispose();
//                    }
//                    return;
//            }
//        }
//
//        // Properties
//        IPatch IEnumerator<IPatch>.Current
//        {
//            [DebuggerHidden]
//            get
//            {
//                return this.<>2__current;
//            }
//        }
//
//        Object IEnumerator.Current
//        {
////            [DebuggerHidden]
//            get
//            {
//                return this.<>2__current;
//            }
//        }
//    }


}
