package edu.gzu.image.sphericalhtm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import edu.gzu.image.sphericallib.Arc;
import edu.gzu.image.sphericallib.Constant;
import edu.gzu.image.sphericallib.Convex;
import edu.gzu.image.sphericallib.IPatch;
import edu.gzu.image.sphericallib.Outline;
import edu.gzu.image.sphericallib.Patch;
import edu.gzu.image.sphericallib.PatchPart;
import edu.gzu.image.sphericallib.Region;

public class Cover {
	 // Fields
    int currentLevel;
    final int DefaultMaxRanges = 20;
    final int DefaultMinRanges = 12;
    List<ISmartTrixel> listOfInners;
    int maxLevel;
    int maxRanges;
    int minLevel;
    int minRanges;
    Outline outline;
    boolean patched;
    private SmartVertex[] points;
    private int previousLevel;
    Region reg;
    private RunStatus RunStatus;
    private List<ISmartTrixel> savedListOfPartials;
//    private Dictionary<Arc, SmartArc> smartArcTable;
    private Map<Arc, SmartArc> smartArcTable;
//    Queue<ISmartTrixel> smartQue;
    List<ISmartTrixel> smartQue;
    Stack<List<ISmartTrixel>> stackOfPartialLists;

    // Methods
    public Cover(Region reg)
    {
//        this.smartQue = new Queue<ISmartTrixel>(100);
        this.smartQue = new ArrayList<ISmartTrixel>();
        this.Init(reg, true);
    }

    public Cover(Region reg, boolean haspatches)
    {
//        this.smartQue = new Queue<ISmartTrixel>(100);
        this.smartQue = new LinkedList<ISmartTrixel>();
        this.Init(reg, haspatches);
    }

	private void buildArcTable(Outline ol)
    {
        this.smartArcTable = new HashMap<Arc, SmartArc>();
        //////////////这里自己做过改动
        for (PatchPart part : ol.getPartList())
        {
        	for (Arc arc : part.getArcList())
            {
                this.smartArcTable.put(arc, new SmartArc(arc));
            }
        }
    }

    public int Cost()
    {
        return this.Cost(Markup.Outer);
    }

    int Cost(Markup kind)
    {
        switch (kind)
        {
        case Inner:
            return RowCost(null, this.listOfInners);

        case Partial:
            return RowCost(this.stackOfPartialLists.peek(), null);

        case Outer:
            return RowCost(this.stackOfPartialLists.peek(), this.listOfInners);
		default:
			break;
        }
        return -1;
    }

    void estimateMinMaxLevels()
    {
        double radius = 1.0;
        double wholeSphereInSquareDegree = Constant.WholeSphereInSquareDegree;
        double num4 = 0.0;
        for (Convex convex : this.reg.getConvexList())
        {
            for (Patch patch : convex.getPatchList())
            {
                double num2 = patch.getArea();
                if (num2 < (0.0 + Trixel.DblTolerance))
                {
                    num2 += Constant.WholeSphereInSquareDegree;
                }
                if (wholeSphereInSquareDegree > num2)
                {
                    wholeSphereInSquareDegree = num2;
                }
                if (num4 < num2)
                {
                    num4 = num2;
                }
            }
        }
        radius = Math.sqrt(num4 * 2.0) * Constant.Degree2Radian;
        this.minLevel = HtmState.getInstance().getLevel(radius);
        radius = Math.sqrt(wholeSphereInSquareDegree / 2.0) * Constant.Degree2Radian;
        this.maxLevel = HtmState.getInstance().getLevel(radius);
        if (this.maxLevel < (this.minLevel + HtmState.getInstance().getHdelta()))
        {
            this.maxLevel += HtmState.getInstance().getHdelta() / 2;
            this.minLevel -= HtmState.getInstance().getHdelta() / 2;
        }
        int num5 = this.minLevel - 3;
        if (num5 < 0)
        {
            this.minLevel -= num5;
            this.maxLevel -= num5;
        }
        this.maxLevel += HtmState.getInstance().getDeltalevel();
        this.minLevel += HtmState.getInstance().getDeltalevel();
    }

    private RunStatus evaluateCurrentLevel()
    {
        int num = this.Cost(Markup.Outer);
        RunStatus hold = edu.gzu.image.sphericalhtm.RunStatus.Continue;
        if (num > this.maxRanges)
        {
            return edu.gzu.image.sphericalhtm.RunStatus.Backup;
        }
        if (this.currentLevel >= this.maxLevel)
        {
            return edu.gzu.image.sphericalhtm.RunStatus.Hold;
        }
        if ((num > this.minRanges) && (num <= this.maxRanges))
        {
            hold = edu.gzu.image.sphericalhtm.RunStatus.Hold;
        }
        return hold;
    }

//    static List<Int64AugPair> GetAdvancedRange(Region reg)
//    {
//        Cover cover = new Cover(reg);
//        cover.Run();
//        List<Int64Pair> pairs = cover.GetPairs(Markup.Outer);
//        List<Int64Pair> list2 = cover.GetPairs(Markup.Inner);
//        List<Int64AugPair> list3 = new List<Int64AugPair>();
//        foreach (Int64Pair pair in pairs)
//        {
//            list3.Add(new Int64AugPair(pair, false));
//        }
//        foreach (Int64Pair pair2 in list2)
//        {
//            list3.Add(new Int64AugPair(pair2, true));
//        }
//        return list3;
//    }

    public List<Pair> GetPairs(Markup kind)
    {
        switch (kind)
        {
            case Inner:
                return this.NodesToPairs(null, this.listOfInners);

            case Partial:
                return this.NodesToPairs(this.stackOfPartialLists.peek(), null);

            case Outer:
            	//////////////////////////////////////////////////////
            	for(int i = 0;i<this.stackOfPartialLists.size()-1;i++)
            	{
            		this.NodesToPairs(this.stackOfPartialLists.peek(), this.listOfInners);
            		this.stackOfPartialLists.pop();
            	}
                return this.NodesToPairs(this.stackOfPartialLists.peek(), this.listOfInners);
		default:
			break;
        }
        return new ArrayList<Pair>();
    }

//    public long GetPseudoArea(Markup kind)
//    {
//        long num = 0L;
//        if ((kind == Markup.Inner) || (kind == Markup.Outer))
//        {
//            foreach (SmartTrixel trixel in this.listOfInners)
//            {
//                num += PseudoArea(trixel.Hid);
//            }
//        }
//        if ((kind == Markup.Outer) || (kind == Markup.Partial))
//        {
//            foreach (SmartTrixel trixel2 in this.stackOfPartialLists.Peek())
//            {
//                num += PseudoArea(trixel2.Hid);
//            }
//        }
//        return num;
//    }

    SmartArc GetSmartArc(Arc arc)
    {
        if (this.smartArcTable.containsKey(arc))
        {
            return this.smartArcTable.get(arc);
        }
        return null;
    }

//    public List<Int64AugPair> GetTriples(Markup kind)
//    {
//        switch (kind)
//        {
//            case Markup.Inner:
//                return this.NodesToTriples(null, this.listOfInners);
//
//            case Markup.Partial:
//                return this.NodesToTriples(this.stackOfPartialLists.peek(), null);
//
//            case Markup.Outer:
//                return this.NodesToTriples(this.stackOfPartialLists.peek(), this.listOfInners);
//        }
//        return new List<Int64AugPair>();
//    }
//
//    public List<long> GetTrixels(Markup kind)
//    {
//        switch (kind)
//        {
//            case Markup.Inner:
//                return this.NodesToTrixels(null, this.listOfInners);
//
//            case Markup.Partial:
//                return this.NodesToTrixels(this.stackOfPartialLists.peek(), null);
//
//            case Markup.Outer:
//                return this.NodesToTrixels(this.stackOfPartialLists.peek(), this.listOfInners);
//        }
//        return new List<long>();
//    }
//
//    internal static List<Int64AugPair> HidAugRange(Region reg)
//    {
//        Cover cover = new Cover(reg);
//        cover.Run();
//        return cover.GetTriples(Markup.Outer);
//    }
//
////    public static List<long> HidList(Region reg)
////    {
////        Cover cover = new Cover(reg);
////        cover.Run();
////        return cover.GetTrixels(Markup.Outer);
////    }

    public static List<Pair> HidRange(Region reg)
    {
        Cover cover = new Cover(reg);
        cover.Run();
        return cover.GetPairs(Markup.Outer);
    }

    private void Init(Region r, boolean haspathces)
    {
        this.reg = r;
        this.listOfInners = new ArrayList<ISmartTrixel>();
        this.stackOfPartialLists = new Stack<List<ISmartTrixel>>();
        this.stackOfPartialLists.push(new ArrayList<ISmartTrixel>());
        this.savedListOfPartials = null;
        this.currentLevel = 0;
        this.previousLevel = -1;
        this.minRanges = 12;
        this.maxRanges = 20;
        if (haspathces)
        {
            this.estimateMinMaxLevels();
            ///这里自己改动过，通过getConvexList得到convex.getPatchList,然后初始化outline，详见其构造函数
            this.outline = new Outline(this.reg.getConvexList());
            this.buildArcTable(this.outline);
            List<IPatch> plist = new ArrayList<IPatch>();
            /*这里有过改动 */
            for (IPatch patch : this.outline.getPartList())
            {
                plist.add(patch);
            }
            this.points = new SmartVertex[6];
            HtmState instance = HtmState.getInstance();
            for (int i = 0; i < 6; i++)
            {
                this.points[i] = new SmartVertex(instance.getOriginalpoint(i), true);
                this.points[i].SetParentArcsAndTopo(plist, this.reg);
            }
            SmartTrixel parent = new SmartTrixel(this);
            for (int j = 0; j < 8; j++)
            {
                int[] num4 = new int[1];//////////////////
                int[] num5 = new int[1];////////////////////
                int[] num6 = new int[1];/////////////////
                long hid = instance.Face(j, /*out*/ num4, /*out*/ num5, /*out*/ num6);
//                this.smartQue.Enqueue(new SmartTrixel(parent, hid, this.points[num4], this.points[num5], this.points[num6]));
                this.smartQue.add(new SmartTrixel(parent, hid, this.points[num4[0]], this.points[num5[0]], this.points[num6[0]]));
            }
        }
        else
        {
            this.outline = null;
        }
    }

    private List<Pair> NodesToPairs(List<ISmartTrixel> partialNodes, List<ISmartTrixel> innerNodes)
    {
        Hidranges hidranges = new Hidranges(partialNodes, true);
        Hidranges hidranges2 = new Hidranges(innerNodes, true, this.currentLevel);
        List<Pair> sortedPairs = Hidranges.Combine(hidranges.pairList, hidranges2.pairList);
        Hidranges.Compact(sortedPairs);
        Hidranges.IsWellFormed(sortedPairs);
        return sortedPairs;
    }

//    private List<Int64AugPair> NodesToTriples(List<ISmartTrixel> partialNodes, List<ISmartTrixel> innerNodes)
//    {
//        List<Int64AugPair> list = new List<Int64AugPair>();
//        Hidranges hidranges = new Hidranges(partialNodes, true);
//        Hidranges hidranges2 = new Hidranges(innerNodes, true, this.currentLevel);
//        foreach (Int64Pair pair in hidranges.pairList)
//        {
//            list.Add(new Int64AugPair(pair, false));
//        }
//        foreach (Int64Pair pair2 in hidranges2.pairList)
//        {
//            list.Add(new Int64AugPair(pair2, true));
//        }
//        return list;
//    }
//
//    private List<long> NodesToTrixels(List<ISmartTrixel> partialNodes, List<ISmartTrixel> innerNodes)
//    {
//        List<long> list = new List<long>();
//        if (partialNodes != null)
//        {
//            foreach (ISmartTrixel trixel in partialNodes)
//            {
//                list.Add(trixel.GetHid());
//            }
//        }
//        if (innerNodes != null)
//        {
//            foreach (SmartTrixel trixel2 in innerNodes)
//            {
//                if (trixel2.Level <= this.currentLevel)
//                {
//                    list.Add(trixel2.Hid);
//                }
//            }
//        }
//        return list;
//    }
//
//    public static long PseudoArea(Hidranges it)
//    {
//        long num = 0L;
//        foreach (Int64Pair pair in it.pairList)
//        {
//            num += (pair.hi - pair.lo) + 1L;
//        }
//        return num;
//    }

    public static long PseudoArea(long hid)
    {
        int num = Trixel.LevelOfHid(hid);
        int num3 = 20 - num;
        long num2 = 1L;
        for (num3 = 20 - num; num3 > 0; num3--)
        {
            num2 = num2 << 2;
        }
        return num2;
    }

    private static int RowCost(List<ISmartTrixel> partials, List<ISmartTrixel> inners)
    {
        Hidranges hidranges = new Hidranges(partials, true); 
        Hidranges hidranges2 = new Hidranges(inners, true);
        hidranges2.Merge(hidranges.pairList);
        hidranges2.Sort();
        hidranges2.Compact();
        hidranges2.Check();
        return hidranges2.pairList.size();
    }

    public void Run()
    {
        if (this.reg != null)
        {
            this.RunStatus = edu.gzu.image.sphericalhtm.RunStatus.Continue;
            while ((this.RunStatus == edu.gzu.image.sphericalhtm.RunStatus.Continue) && (this.smartQue.size() > 0))
            {
                this.Step();
                this.RunStatus = this.evaluateCurrentLevel();
            }
            if (this.RunStatus == edu.gzu.image.sphericalhtm.RunStatus.Backup)
            {
                this.savedListOfPartials = this.stackOfPartialLists.pop();
                this.currentLevel--;
            }
            else
            {
                this.savedListOfPartials = null;
            }
        }
    }

    public void SetTunables(int minr, int maxr, int maxl)
    {
        if (minr > 0)
        {
            this.minRanges = minr;
        }
        if (maxr > 0)
        {
            this.maxRanges = maxr;
        }
        this.maxLevel = maxl;
    }

    public void Step()
    {
        if (this.reg != null)
        {
            boolean flag = false;
            if (this.savedListOfPartials != null)
            {
                this.stackOfPartialLists.push(this.savedListOfPartials);
                this.savedListOfPartials = null;
                this.currentLevel++;
            }
            else
            {
                if (this.smartQue.size() > 0)
                {
                    this.stackOfPartialLists.push(new ArrayList<ISmartTrixel>());
                    this.currentLevel++;
                }
                while (!flag && (this.smartQue.size() > 0))
                {
//                    ISmartTrixel sq = this.smartQue.peek();
                	
                    ISmartTrixel sq = this.smartQue.get(0);
                    if (sq.getLevel() != this.previousLevel)
                    {
                        if (this.previousLevel < 0)
                        {
                            this.currentLevel = 0;
                            this.previousLevel = sq.getLevel();
                        }
                        else
                        {
                            flag = true;
                        }
                        this.previousLevel = sq.getLevel();
                    }
                    if (!flag)
                    {
//                        this.smartQue.Dequeue();
                        this.smartQue.remove(0);
                        this.Visit(sq);
                        if (!sq.isTerminal())
                        {
                            sq.Expand();
                        }
                    }
                }
            }
        }
    }

    private void Visit(ISmartTrixel sq)
    {
        switch (sq.GetMarkup())
        {
        case Inner:
            this.listOfInners.add(sq);
            sq.setTerminal(true);
            return;

        case Partial:
            this.stackOfPartialLists.peek().add(sq);
            return;

        case Reject:
            sq.setTerminal(true);
            return;
		default:
			break;
        }
    }

    // Properties
    public int getGetLevel()
    {
        return this.currentLevel;
    }

    public int getGetMaxLevel()
    {
        return this.maxLevel;
    }

    public Region getRegion()
    {
        return this.reg;
    }
}


