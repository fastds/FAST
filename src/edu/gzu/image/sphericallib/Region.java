package edu.gzu.image.sphericallib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.gzu.image.sphericallib.Patch;
public class Region implements Iterable<Region>
{
    // Fields
    private Double area;
    private List<Convex> convexList;
    public static final String Revision = "$Revision: 1.56 $";

    // Methods
    public Region() 
    {
    	 this(1);
    }

    public Region(Halfspace halfspace)
    {
    	this(new Convex(halfspace), false);
    }

    public Region(Region region)
    {
        Collections.copy(this.convexList, region.convexList);
        this.area = region.area;
    }

//    public Region(List<Convex> collection)
//    {
//        this.convexList = new List<Convex>(collection);
//        this.area = null;
//    }

    public Region(List<Convex> list)
    {
        this.convexList = list;
        this.area = null;
    }

    public Region(int capacity)
    {
        this.convexList = new ArrayList<Convex>(capacity);
        this.area = null;
    }

    public Region(Convex convex, boolean clone) 
    {
    	 this(1);
        if (clone)
        {
            this.convexList.add(new Convex(convex));
        }
        else
        {
            this.convexList.add(convex);
        }
    }

    public Region(double ra, double dec, double arcmin) 
    {
    	 this(new Convex(ra, dec, arcmin), false);
    }

    public void Add(Convex c)
    {
        this.area = null;
        this.convexList.add(c);
    }

//    public void AddRange(Enumeration<Convex> collection)
//    {
//        this.area = null;
//        this.convexList.AddRange(collection);
//    }

    public void AddRange(List<Convex> collection)
    {
        this.area = null;
//        int num = this.convexList.size() + collection.size();
//        if (this.convexList.Capacity < num)
//        {
//            this.convexList.Capacity = num;
//        }
        this.convexList.addAll(collection);
    }

    public void Clear()
    {
        this.area = null;
        this.convexList.clear();
    }

    public boolean Contains(Cartesian p)
    {
        return this.Contains(p, Constant.CosTolerance, Constant.SinTolerance);
    }

    public boolean Contains(Cartesian p, double costol, double sintol)
    {
        for (Convex convex : this.convexList)
        {
            if (convex.Contains(p, costol, sintol))
            {
                return true;
            }
        }
        return false;
    }

    public Region Difference(Convex C)
    {
        Region region = new Region();
        for (int i = 0; i < C.getHalfspaceList().size(); i++)
        {
            Region region2 = new Region(this);
            for (int j = 0; j < i; j++)
            {
                region2.Intersect(C.getHalfspaceList().get(j));
            }
            region2.Intersect(C.getHalfspaceList().get(i).Inverse());
            region.Union(region2);
        }
        return region;
    }

    public void Difference(Halfspace h)
    {
        this.Intersect(h.Inverse());
    }

    public boolean DoesCollide(Convex c)
    {
        for (Convex convex : this.convexList)
        {
            if (convex.DoesCollide(c))
            {
                return true;
            }
        }
        return false;
    }

    private void EliminateConvexes()
    {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < this.convexList.size(); i++)
        {
            if (!list.contains(i))
            {
                Convex that = this.convexList.get(i);
                for (int j = 0; j < i; j++)
                {
                    Convex convex2 = this.convexList.get(j);
                    if (!list.contains(j))
                    {
                        if (that.getHalfspaceList().size() >= convex2.getHalfspaceList().size())
                        {
                            if (convex2.ElimCovers(that))
                            {
                                list.add(i);
                            }
                        }
                        else if (that.ElimCovers(convex2))
                        {
                            list.add(j);
                        }
                    }
                }
            }
        }
        Collections.sort(list);
        Collections.reverse(list);
        for (int num3 : list)
        {
            this.convexList.remove(num3);
        }
    }

    private void EliminateConvexes(/*ref*/ DynSymMatrix<Boolean> collision)
    {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < this.convexList.size(); i++)
        {
            if (!list.contains(i))
            {
                Convex that = this.convexList.get(i);
                for (int j = 0; j < i; j++)
                {
                    if (collision.getThis(i, j))
                    {
                        Convex convex2 = this.convexList.get(j);
                        if (!list.contains(j))
                        {
                            if (that.getHalfspaceList().size() >= convex2.getHalfspaceList().size())
                            {
                                if (convex2.ElimCovers(that))
                                {
                                    list.add(i);
                                }
                            }
                            else if (that.ElimCovers(convex2))
                            {
                                list.add(j);
                            }
                        }
                    }
                }
            }
        }
        Collections.sort(list);
        Collections.reverse(list);
        for (int num3 : list)
        {
            this.convexList.remove(num3);
            collision.RemoveAt(num3);
        }
    }

    /*public Iterator<IPatch> EnumIPatches()
    {
        Iterator<Convex> enumerator = this.convexList.iterator();
        while (enumerator.hasNext())
        {
            Convex current = enumerator.next();
            Iterator<Patch> iteratorVariable3 = current.getPatchList().iterator();
            while (iteratorVariable3.hasNext())
            {
                Patch iteratorVariable1 = iteratorVariable3.next();
                yield return iteratorVariable1;
            }
            iteratorVariable3 = null;
        }
        enumerator = null;
    }*/

   /* public Iterator<Patch> EnumPatches()
    {
        Iterator<Convex> enumerator = this.convexList.iterator();
        while (enumerator.hasNext())
        {
            Convex current = enumerator.next();
            Iterator<Patch> iteratorVariable3 = current.getPatchList().iterator();
            while (iteratorVariable3.hasNext())
            {
                Patch iteratorVariable1 = iteratorVariable3.next();
                yield return iteratorVariable1;
            }
            iteratorVariable3.Dispose();
        }
        enumerator.Dispose();
    }*/
    public String Format()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("REGION ");
        for (Convex convex : this.convexList)
        {
            builder.append("CONVEX CARTESIAN ");
            for (Halfspace halfspace : convex.getHalfspaceList())
            {
//                builder.AppendFormat("{0,20:0.000000000000000} {1,20:0.000000000000000} {2,20:0.000000000000000} {3,20:0.000000000000000} ",  halfspace.getVector().getX(), halfspace.getVector().getY(), halfspace.getVector().getZ(), halfspace.getCos0());
                builder.append(halfspace.getVector().getX()+" "+halfspace.getVector().getY()+" "+halfspace.getVector().getZ()+" "+halfspace.getCos0()+" ");
            }
        }
        return builder.toString();
    }

    public int GetNumberOfHalfspaces()
    {
        int num = 0;
        for (Convex convex : this.convexList)
        {
            num += convex.getHalfspaceList().size();
        }
        return num;
    }

    public int GetNumberOfPatches()
    {
        int num = 0;
        for (Convex convex : this.convexList)
        {
            num += convex.getPatchList().size();
        }
        return num;
    }

    public void Grow(double arcmin)
    {
        for (Convex convex : this.convexList)
        {
            convex.Grow(arcmin);
        }
    }

    public void Intersect(Convex c)
    {
        this.area = null;
        for (Convex convex : this.convexList)
        {
            convex.AddRange(c.getHalfspaceList());
        }
    }

    public void Intersect(Halfspace h)
    {
        this.area = null;
        for (Convex convex : this.convexList)
        {
            convex.Add(h);
        }
    }

    public Region Intersect(Region region)
    {
        Region region2 = new Region(this.convexList.size() * region.convexList.size());
        for (Convex convex : this.convexList)
        {
            for (Convex convex2 : region.convexList)
            {
                Convex item = new Convex(convex.getHalfspaceList().size() + convex2.getHalfspaceList().size());
                item.AddRange(convex.getHalfspaceList());
                item.AddRange(convex2.getHalfspaceList());
                region2.convexList.add(item);
            }
        }
        return region2;
    }

    private void MakeDisjoint(/*ref*/ DynSymMatrix<Boolean> collision)
    {
        int maximumIteration = Global.getParameter().getMaximumIteration();
        int num = 0;
        while (num < maximumIteration)
        {
        	/////////Initiallize//////////
            int[] num4 = new int[1];
            int[] num5 = new int[1];
            boolean[] flag2 = new boolean[1];
            double[] keys = new double[this.convexList.size()];
            Integer[] items = new Integer[this.convexList.size()];
            Map<Double,Integer> map = new TreeMap<Double,Integer>();
            for (int i = 0; i < this.convexList.size(); i++)
            {
                items[i] = i;
                keys[i] = this.convexList.get(i).getArea().doubleValue();
                map.put(keys[i], items[i]);
            }
//            Array.Sort<double, int>(keys, items);
//            Array.Reverse(items);
            int i = map.size()-1;
            for(Double key : map.keySet())
            {
            	int val = map.get(key);
            	items[i] = val;
            	i--;
            }
            if (!collision.FindValue(true, Arrays.asList(items), /*out*/ num4, /*out*/ num5))
            {
                return;
            }
            Convex convex = this.convexList.get(num4[0]);
            Region region = this.convexList.get(num5[0]).SmartDifference(convex, /*out*/ flag2);
            if (!(flag2[0]))
            {
                collision.setThis(false,num4[0], num5[0]);
            }
            else
            {
                List<Boolean> list = collision.Row(num5[0]);
                list.remove(num5);
                collision.RemoveAt(num5[0]);
                this.convexList.remove(num5[0]);
                if (num4[0] > num5[0])
                {
                    num4[0]--;
                }
                int count = this.convexList.size();
                for (Convex convex3 : region.convexList)
                {
                    if ((convex3.getArea().doubleValue() > Constant.TolArea) && (convex3.getArea().doubleValue() < (Constant.WholeSphereInSquareDegree - Constant.TolArea)))
                    {
                        this.convexList.add(convex3);
                    }
                }
                collision.setDim(this.convexList.size());
                for (int j = count; j < this.convexList.size(); j++)
                {
                    Convex convex4 = this.convexList.get(j);
                    for (int k = 0; k < count; k++)
                    {
                        if (k != num4[0])
                        {
                            Convex c = this.convexList.get(k);
                            if (list.get(k) && convex4.DoesCollide(c))
                            {
                                collision.setThis(true,j,k);
                            }
                        }
                    }
                }
            }
            num++;
        }
        if (num == maximumIteration)
        {
            throw new RuntimeException("Region.MakeDisjoint(): exceeded maximum number of iterations!");
        }
    }

//    public boolean MecContains(Cartesian x)
//    {
//        double cosTolerance = Constant.CosTolerance;
//        double sinTolerance = Constant.SinTolerance;
//        for (Convex convex : this.convexList)
//        {
//            for (Patch patch : convex.PatchList)
//            {
//                if (patch.Mec.Contains(x, cosTolerance, sinTolerance))
//                {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    public static Region Parse(String repr, boolean normalize)
//    {
//        char[] separator = new char[] { ' ' };
//        char[] chArray2 = new char[] { ',' };
//        String str = repr.Trim().Replace(',', ' ').Replace("\r\n", " ").Replace('\n', ' ').Replace('\t', ' ');
//        String str2 = "";
//        while (str2 != str)
//        {
//            str2 = str;
//            str = str2.Replace("  ", " ");
//        }
//        String[] strArray = str2.Split(separator, 2);
//        if (strArray[0] != Constant.KeywordRegion)
//        {
//            throw new ArgumentException("Region.Parse(): wrong keyword");
//        }
//        strArray = strArray[1].Replace(Constant.KeywordConvex, "," + Constant.KeywordConvex).Split(chArray2);
//        Region region = new Region();
//        for (int i = 1; i < strArray.Length; i++)
//        {
//            region.convexList.Add(Convex.Parse(strArray[i], normalize));
//        }
//        return region;
//    }
//
//    public void Remove(Convex item)
//    {
//        this.area = null;
//        this.convexList.Remove(item);
//    }
//
//    public void RemoveAt(int index)
//    {
//        this.area = null;
//        this.convexList.RemoveAt(index);
//    }
//
//    public void RemoveRange(int index, int count)
//    {
//        this.area = null;
//        this.convexList.RemoveRange(index, count);
//    }
//
    public void Simplify()
    {
        this.Simplify(true, true, true, true, null);
    }

//    public void Simplify(boolean simple_simplify, boolean eliminate, boolean make_disjoint, boolean unify)
//    {
//        this.Simplify(simple_simplify, eliminate, make_disjoint, unify, null);
//    }

    public void Simplify(boolean simple_simplify, boolean eliminate, boolean make_disjoint, boolean unify, DynSymMatrix<Boolean> collision)
    {
        List<Convex> list = new ArrayList<Convex>();
        for (Convex convex : this.convexList)
        {
            if (!convex.Simplify(simple_simplify))
            {
                list.add(convex);
            }
        }
        for (Convex convex2 : list)
        {
            this.convexList.remove(convex2);
        }
        if (eliminate)
        {
            this.EliminateConvexes();
        }
        if (make_disjoint)
        {
        	Collections.sort(this.convexList, new AreaDescendingComparator());
            boolean collisions = false;
            if (collision == null)
            {
                collisions = Convex.GetCollisions(this.convexList, collision);
            }
            if (!collisions)
            {
                this.MakeDisjoint( collision);
            }
        }
        if (unify)
        {
            this.StitchConvexes();
        }
        this.UpdateArea();
    }

    public boolean SmartContains(Cartesian x)
    {
        return this.SmartContains(x, Constant.CosTolerance, Constant.SinTolerance);
    }

    public boolean SmartContains(Cartesian x, double costol, double sintol)
    {
        for (Convex convex : this.convexList)
        {
            if (convex.SmartContains(x, costol, sintol))
            {
                return true;
            }
        }
        return false;
    }
//
//    public void SmartIntersect(Convex convex)
//    {
//        List<Convex> list = new List<Convex>();
//        for (Convex convex2 : this.convexList)
//        {
//            if (!convex2.SmartIntersect(convex))
//            {
//                list.Add(convex2);
//            }
//        }
//        for (Convex convex3 : list)
//        {
//            this.Remove(convex3);
//        }
//        this.UpdateArea();
//    }
//
//    public Region SmartIntersect(Region region, boolean unify)
//    {
//        Region region2 = new Region();
//        for (Convex convex : this.convexList)
//        {
//            for (Convex convex2 : region.convexList)
//            {
//                Convex item = new Convex(convex);
//                if (item.SmartIntersect(convex2))
//                {
//                    region2.convexList.Add(item);
//                }
//            }
//        }
//        if (unify)
//        {
//            region2.StitchConvexes();
//        }
//        region2.UpdateArea();
//        return region2;
//    }
//
//    public void SmartUnion(Region region, boolean unify)
//    {
//        int count = this.convexList.Count;
//        this.convexList.AddRange(region.convexList.ConvertAll<Convex>(new Converter<Convex, Convex>(Convex.Clone)));
//        DynSymMatrix<boolean> collision = new DynSymMatrix<boolean>(this.convexList.Count);
//        for (int i = count; i < this.convexList.Count; i++)
//        {
//            Convex convex = this.convexList[i];
//            for (int j = 0; j < count; j++)
//            {
//                Convex c = this.convexList[j];
//                if (convex.DoesCollide(c))
//                {
//                    collision[i, j] = true;
//                }
//            }
//        }
//        this.EliminateConvexes(ref collision);
//        this.MakeDisjoint(ref collision);
//        if (unify)
//        {
//            this.StitchConvexes();
//        }
//        this.UpdateArea();
//    }
//
//    public void Sort()
//    {
//        this.convexList.Sort(new Comparison<Convex>(Convex.ComparisonAreaDescending));
//    }
//
//    public void Sort(Comparison<Convex> comparison)
//    {
//        this.convexList.Sort(comparison);
//    }
//
    private void StitchConvexes()
    {
        List<Convex> list = new ArrayList<Convex>();
        boolean flag = true;
        while (flag)
        {
        	Collections.sort(this.convexList, new AreaDescendingComparator());
//            this.convexList.Sort(new Comparison<Convex>(Convex.ComparisonAreaDescending));
            flag = false;
            for (int i = 0; i < (this.convexList.size() - 1); i++)
            {
                Convex item = this.convexList.get(i);
                if (!list.contains(item))
                {
                    for (int j = i + 1; j < this.convexList.size(); j++)
                    {
                        Convex convex2 = this.convexList.get(j);
                        if ((!list.contains(convex2) && ((item.getESign() != ESign.Positive) || (convex2.getESign() != ESign.Positive))) && (!item.MecDisjoint(convex2) && item.HasCommonHalfspace(convex2)))
                        {
                            Convex c = new Convex(convex2);
                            c.InvertHalfspaces();
                            c.Sort();
                            if (item.HasOnlyOneCommonHalfspace(c))
                            {
                            	/////////initialize///////////////
                                int num4 = 0;
                                int index = item.IndexFirstCommonHalfspacePair(c, /*out*/ num4);
                                int num3 = (c.getHalfspaceList().size() - 1) - num4;
                                Convex convex4 = new Convex(convex2);
                                convex4.RemoveAt(num3);
                                convex4.AddRange(item.getHalfspaceList());
                                convex4.Simplify(true);
                                if (item.Same(convex4))
                                {
                                    Convex convex5 = new Convex(item);
                                    convex5.RemoveAt(index);
                                    convex5.AddRange(convex2.getHalfspaceList());
                                    convex5.Simplify(true);
                                    if (convex2.Same(convex5))
                                    {
                                        item.RemoveAt(index);
                                        convex2.RemoveAt(num3);
                                        item.AddRange(convex2.getHalfspaceList());
                                        item.Simplify(true);
                                        list.add(convex2);
                                        flag = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            for (Convex convex6 : list)
            {
                this.convexList.remove(convex6);
            }
            list.clear();
        }
    }
//
//    public override String ToString()
//    {
//        return this.ToString("\r\n\t");
//    }
//
//    public String ToString(String sep)
//    {
//        String keywordRegion = Constant.KeywordRegion;
//        for (Convex convex : this.convexList)
//        {
//            keywordRegion = keywordRegion + sep + convex.ToString(sep);
//        }
//        return keywordRegion;
//    }
//
    public void Union(Convex convex)
    {
        this.convexList.add(new Convex(convex));
        this.UpdateArea();
    }

    public void Union(Region region)
    {
//        List<Convex> collection = region.convexList.ConvertAll<Convex>(new Converter<Convex, Convex>(Convex.Clone));
    	List<Convex> collection = null;
    	Collections.copy(collection, region.convexList);
    	this.convexList.addAll(collection);
        this.UpdateArea();
    }

    public void UpdateArea()
    {
        this.area = 0.0;
        for (Convex convex : this.convexList)
        {
            this.area += convex.getArea();
        }
    }
//
//    // Properties
//    public double? Area
//    {
//        get
//        {
//            return this.area;
//        }
//        set
//        {
//            this.area = value;
//        }
//    }
//
//    [XmlIgnore]
//    public int Capacity
//    {
//        get
//        {
//            return this.convexList.Capacity;
//        }
//        set
//        {
//            this.convexList.Capacity = value;
//        }
//    }
//
//    [XmlIgnore]
//    public ReadOnlyCollection<Convex> ConvexList
//    {
//        get
//        {
//            return this.convexList.AsReadOnly();
//        }
//    }
    public List<Convex> getConvexList()
    {
        return this.convexList;
    }

//    public boolean Simplified
//    {
//        get
//        {
//            for (Convex convex : this.ConvexList)
//            {
//                if (!convex.Simplified)
//                {
//                    return false;
//                }
//            }
//            return true;
//        }
//    }
//
//    [XmlArray(ElementName="ConvexList", Namespace="ivo://voservices.org/spherical")]
//    public List<Convex> XmlSerialization
//    {
//        get
//        {
//            return this.convexList;
//        }
//        set
//        {
//            this.convexList = value;
//        }
//    }

    // Nested Types
//    [CompilerGenerated]
//	private class d__7 <IPatch> implements Iterator<IPatch>,Iterable<IPatch>
//    {
//        // Fields
//        private int __state;
//        private IPatch __current;
//        public Region __this;
//        public Iterator<Convex> __wrapa;
//        public Iterator<Patch> __wrapb;
//        public Convex c5__8;
//        public Patch p5__9;
//
//        // Methods
////        [DebuggerHidden]
//        public <IPatch> d__7(int __state,Region thisParam)
//        {
//            this.__state = __state;
//            this.__this = thisParam;
//        }
//
////        [DebuggerHidden]   该方法替换为iterator()
////        Iterator<IPatch> GetEnumerator()
////        {
////            if (Interlocked.CompareExchange(/*ref*/ this.__state, 0, -2) == -2)
////            {
////                return this;
////            }
////            return new Region.d__7<IPatch>(0,this.__this) /*{ __this = this.__this }*/;
////        }
//        public Iterator<IPatch> iterator() {
////        	if (Interlocked.CompareExchange(/*ref*/ this.__state, 0, -2) == -2)
////            {
////                return this;
////            }
//        	if(this.__state == -2)
//        	{
//        		this.__state = 0;
//        		return this;
//        	}
//            return new Region.d__7<IPatch>(0,this.__this);
//		}
//
////        [DebuggerHidden]
////        Enumerator IEnumerable.GetEnumerator()
////        {
////            return this.System.Collections.Generic.IEnumerable<IPatch>.GetEnumerator();
////        }
//
////        [DebuggerHidden]
////        void Enumerator.Reset()
////        {
////            throw new NotSupportedException();
////        }
//
//		public boolean hasNext() {
//			try
//          {
//              switch (this.__state)
//              {
//                  case 0:
//                      this.__state = -1;
//                      this.__wrapa = this.__this.convexList.iterator();
//                      this.__state = 1;
//                      while (this.__wrapa.hasNext())
//                      {
//                          this.c5__8 = this.__wrapa.next();
//                          this.__wrapb = this.c5__8.getPatchList().iterator();
//                          this.__state = 2;
//                          while (this.__wrapb.hasNext())
//                          {
//                              this.p5__9 = this.__wrapb.next();
//                              this.__current = (IPatch) this.p5__9;
//                              this.__state = 3;
//                              return true;
////                          Label_0098:
////                              this.__state = 2;
//                          }
//                          this.__state = 1;
//                          this.__wrapb = null;
//                      }
//                      this.__state = -1;
//                      this.__wrapa = null;
//                      break;
//
//                  case 3:
////       old               goto Label_0098;
//                	  this.__state = 2;
//                      while(this.__wrapb.hasNext())
//                      {
//                    	  this.p5__9 = this.__wrapb.next();
//                          this.__current = (IPatch) this.p5__9;
//                          this.__state = 3;
//                          return true;
//                      }
//                      this.__state = 1;
//                      while (this.__wrapa.hasNext()){
//                    	  this.c5__8 = this.__wrapa.next();
//                          this.__wrapb = this.c5__8.getPatchList().iterator();
//                          this.__state = 2;
//                          while (this.__wrapb.hasNext())
//                          {
//                              this.p5__9 = this.__wrapb.next();
//                              this.__current = (IPatch) this.p5__9;
//                              this.__state = 3;
//                              return true;
//                          }
//                          this.__state = 1;
//                          this.__wrapb = null;
//                      }
//                      this.__state = -1;
//                      this.__wrapa = null;
//                      break;
//              }
//              return false;
//          }catch(Exception e){e.printStackTrace();}
//			return false;
//		}
//
//		public IPatch next() {
//			return this.__current;
//		}
//
//		public void remove() {
//			// TODO Auto-generated method stub
//		}
//
//        public void Dispose()
//        {
//            switch (this.__state)
//            {
//                case 1:
//                case 2:
//                case 3:
//                    try
//                    {
//                        switch (this.__state)
//                        {
//                            case 2:
//                            case 3:
//                                try
//                                {
//                                }
//                                finally
//                                {
//                                    this.__state = 1;
//                                    this.__wrapb = null;
//                                }
//                                return;
//                        }
//                    }
//                    finally
//                    {
//                        this.__state = -1;
//                        this.__wrapa = null;
//                    }
//                    break;
//
//                default:
//                    return;
//            }
//        }
//
//		
//
//    }
////
//////    [CompilerGenerated]
//    private  class  d__0 <Patch> implements Iterator<Patch>
//    {
//        // Fields
//        private int __state;
//        private Patch __current;
//        public Region __this;
//        public Iterator<Convex> __wrap3;
//        public Iterator<Patch> __wrap4;
//        public Convex c5__1;
//        public Patch p5__2;
//
//        // Methods
////        [DebuggerHidden]
//        public <EnumPatches>d__0(int __state)
//        {
//            this.__state = __state;
//        }
//
//
//		public boolean hasNext() {
//			try
//            {
//                switch (this.__state)
//                {
//                    case 0:
//                        this.__state = -1;
//                        this.__wrap3 = this.__this.convexList.iterator();
//                        this.__state = 1;
//                        while (this.__wrap3.hasNext())
//                        {
//                            this.c5__1 = this.__wrap3.next();
//                            this.__wrap4 = (Iterator<Patch>) this.c5__1.getPatchList().iterator();
//                            this.__state = 2;
//                            while (this.__wrap4.hasNext())
//                            {
//                                this.p5__2 = this.__wrap4.next();
//                                this.__current = this.p5__2;
//                                this.__state = 3;
//                                return true;
//                            }
//                            this.__state = 1;
//                            this.__wrap4 = null;
//                        }
//                        this.__state = -1;
//                        this.__wrap3 = null;
//                        break;
//
//                    case 3:
//                    	this.__state = 2;
//                    	while (this.__wrap4.hasNext())
//                        {
//                            this.p5__2 = this.__wrap4.next();
//                            this.__current = this.p5__2;
//                            this.__state = 3;
//                            return true;
//                        }
//                    	this.__state = 1;
//                        this.__wrap4 = null;
//                        while (this.__wrap3.hasNext())
//                        {
//                            this.c5__1 = this.__wrap3.next();
//                            this.__wrap4 = (Iterator<Patch>) this.c5__1.getPatchList().iterator();
//                            this.__state = 2;
//                            while (this.__wrap4.hasNext())
//                            {
//                                this.p5__2 = this.__wrap4.next();
//                                this.__current = this.p5__2;
//                                this.__state = 3;
//                                return true;
//                            }
//                            this.__state = 1;
//                            this.__wrap4 = null;
//                        }
//                        this.__state = -1;
//                        this.__wrap3 = null;
//                        break;
//                }
//                return false;
//            }catch(Exception e)
//            {
//            	e.printStackTrace();
//            	return false;
//            }
//		}
//
//		public Patch next() {
//			return this.__current;
//		}
//
//		public void remove() {
//			
//		}
//
//
////        [DebuggerHidden]
////        IEnumerator<Patch> IEnumerable<Patch>.GetEnumerator()
////        {
////            if (Interlocked.CompareExchange(/*ref*/ this.__state, 0, -2) == -2)
////            {
////                return this;
////            }
////            return new Region.<EnumPatches>d__0(0) { __this = this.__this };
////        }
//
////        [DebuggerHidden]
////        IEnumerator IEnumerable.GetEnumerator()
////        {
////            return this.System.Collections.Generic.IEnumerable<Spherical.Patch>.GetEnumerator();
////        }
//
////        [DebuggerHidden]
////        void IEnumerator.Reset()
////        {
////            throw new NotSupportedException();
////        }
//
//        public void Dispose()
//        {
//            switch (this.__state)
//            {
//                case 1:
//                case 2:
//                case 3:
//                    try
//                    {
//                        switch (this.__state)
//                        {
//                            case 2:
//                            case 3:
//                                try
//                                {
//                                }
//                                finally
//                                {
//                                    this.__state = 1;
//                                    this.__wrap4 = null;
//                                }
//                                return;
//                        }
//                    }
//                    finally
//                    {
//                        this.__state = -1;
//                        this.__wrap3 = null;
//                    }
//                    break;
//
//                default:
//                    return;
//            }
//        }
//
//    }
	public Iterator<Region> iterator() {
		return null;
	}

	public List<Convex> EnumPatchesByConvexList() {
		return this.getConvexList();
	}
}
