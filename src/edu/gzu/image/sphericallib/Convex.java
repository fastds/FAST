package edu.gzu.image.sphericallib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Convex
{
    // Fields
    private double area;
    private ESign eSign;
    private List<Halfspace> halfspaceList;
    private List<Patch> patchList;
    public static final String Revision = "$Revision: 1.48 $";
    private boolean simplified;
    private boolean sorted;

    // Methods
    public Convex()
    {
    	 this(4);
    }

    public Convex(Convex convex) 
    {
    	this(convex.halfspaceList.size());
        this.area = convex.area;
        this.sorted = convex.sorted;
        this.eSign = convex.eSign;
        this.simplified = convex.simplified;
        this.halfspaceList.addAll(convex.halfspaceList);
        if (convex.patchList == null)
        {
            this.patchList = null;
        }
        else
        {
            this.patchList = new ArrayList<Patch>(convex.patchList.size());
            for (Patch patch : convex.patchList)
            {
                this.patchList.add(new Patch(patch));
            }
        }
    }

    public Convex(Halfspace halfspace) 
    {
    	this(halfspace, false);
    }

    public Convex(ArrayList<Halfspace> halfspaces) 
    {
    	this(4);
        this.halfspaceList.addAll(halfspaces);
    }

    public Convex(List<Halfspace> halfspaces) 
    {
    	 this(halfspaces.size());
        this.halfspaceList.addAll(halfspaces);
    }

    public Convex(int capacity)
    {
        this.halfspaceList = new ArrayList<Halfspace>(capacity);
        this.simplified = false;
        this.sorted = false;
        this.eSign = ESign.Unknown;
    }

    public Convex(Halfspace halfspace, boolean simplify) 
    {
    	 this(1);
        this.halfspaceList.add(halfspace);
        this.sorted = true;
        if (simplify)
        {
            this.simplified = true;
            if (halfspace.IsEmpty())
            {
                this.patchList = new ArrayList<Patch>();
                this.area = 0.0;
            }
            else
            {
                Patch patch = new Patch(new Arc[] { new Arc(halfspace, halfspace.GetPointWest()) });
                this.patchList = new ArrayList<Patch>();
                this.patchList.add(patch);
                if (halfspace.IsAll())
                {
                    this.area = new Double(Constant.WholeSphereInSquareDegree);
                }
                else
                {
                    this.area = new Double(patch.getArea());
                }
            }
        }
    }

    public Convex(List<Cartesian> list, PointOrder order)  
    {
    	this(list.size());
        Cartesian cartesian;
        Cartesian cartesian2;
        Halfspace halfspace2;
        if (list.size() < 3)
        {
            throw new RuntimeException("Convex..ctor(list,order): Not enough points in list for a polygon!");
        }
        if (order == PointOrder.CW)
        {
        	Collections.reverse(list);
        }
        else if (order == PointOrder.Safe)
        {
            Cartesian cartesian3 = list.get(0);
            Halfspace halfspace = new Halfspace(cartesian3.Cross(list.get(1), true), 0.0, 1.0);
            if (!halfspace.Contains(list.get(2), Constant.CosTolerance, Constant.SinTolerance))
            {
            	Collections.reverse(list);
            }
        }
        else if (order == PointOrder.Random)
        {
            throw new RuntimeException("Convex..ctor(list,order): random order not implemented");
        }
        Arc[] arcArray = new Arc[list.size()];
        for (int i = 1; i < list.size(); i++)
        {
            cartesian = list.get(i - 1);
            cartesian2 = list.get(i);
            halfspace2 = new Halfspace(cartesian.Cross(cartesian2, true), 0.0, 1.0);
            arcArray[i - 1] = new Arc(halfspace2, cartesian, cartesian2);
            this.halfspaceList.add(halfspace2);
        }
        cartesian = list.get(list.size() - 1);
        cartesian2 = list.get(0);
        halfspace2 = new Halfspace(cartesian.Cross(cartesian2, true), 0.0, 1.0);
        arcArray[list.size() - 1] = new Arc(halfspace2, cartesian, cartesian2);
        this.halfspaceList.add(halfspace2);
        this.patchList = new ArrayList<Patch>(1);
        Patch item = new Patch(arcArray);
        this.patchList.add(item);
        this.simplified = true;
        this.area = new Double(item.getArea());
        this.Sort();
    }

    public Convex(Cartesian p1, Cartesian p2, Cartesian p3) 
    {
    	this(3);
        Arc[] list = new Arc[] { new Arc(p1, p2), new Arc(p2, p3), new Arc(p3, p1) };
        Halfspace halfspace = new Halfspace(p1.Cross(p2, true), 0.0, 1.0);
        if (!halfspace.Contains(p3, Constant.CosTolerance, Constant.SinTolerance))
        {
            throw new RuntimeException("Convex..ctor(p1,p2,p3): incorrect winding!");
        }
        this.sorted = true;
        for (Arc arc : list)
        {
            this.halfspaceList.add(arc.getCircle());
        }
        this.patchList = new ArrayList<Patch>(1);
        this.patchList.add(new Patch(list));
        this.simplified = true;
        this.area = new Double(Cartesian.SphericalTriangleArea(p1, p2, p3));
    }

    public Convex(double ra, double dec, double arcmin) 
    {
    	 this(new Halfspace(ra, dec, arcmin));
    }

    public void Add(Halfspace h)
    {
        this.sorted = false;
        this.setSimplified(false);
        this.halfspaceList.add(h);
        if (this.eSign != ESign.Mixed)
        {
            this.eSign = ESign.Unknown;
        }
    }

    public void AddRange(List<Halfspace> collection)
    {
        this.sorted = false;
        this.setSimplified(false);
        this.halfspaceList.addAll(collection);
        if (this.eSign != ESign.Mixed)
        {
            this.eSign = ESign.Unknown;
        }
    }

    public void Clear()
    {
        this.eSign = ESign.Unknown;
        this.sorted = false;
        this.setSimplified(false);
        this.halfspaceList.clear();
    }

    public static Convex Clone(Convex c)
    {
        return new Convex(c);
    }

    public static int ComparisonAreaAscending(Convex c, Convex k)
    {
        return c.getArea().compareTo(k.getArea());
    }

    public static int ComparisonAreaDescending(Convex c, Convex k)
    {
        return k.getArea().compareTo(c.getArea());
    }

    public boolean Contains(Cartesian p)
    {
        return this.Contains(p, Constant.CosTolerance, Constant.SinTolerance);
    }

    public boolean Contains(Cartesian p, double costol, double sintol)
    {
        if (this.halfspaceList.size() == 0)
        {
            return false;
        }
        for (Halfspace halfspace : this.halfspaceList)
        {
            if (!halfspace.Contains(p, costol, sintol))
            {
                return false;
            }
        }
        return true;
    }

    public Region Difference(Convex convex)
    {
        Region region = new Region();
        for (int i = 0; i < convex.halfspaceList.size(); i++)
        {
            Convex convex2 = new Convex(this);
            for (int j = 0; j < i; j++)
            {
                convex2.Intersect(convex.halfspaceList.get(j));
            }
            convex2.Intersect(convex.halfspaceList.get(i).Inverse());
            region.Union(convex2);
        }
        return region;
    }

    public boolean DoesCollide(Convex c)
    {
        if (!this.MecDisjoint(c))
        {
            double cosTolerance = Constant.CosTolerance;
            double sinTolerance = Constant.SinTolerance;
            for (Patch patch : this.getPatchList())
            {
                for (Arc arc : patch.getArcList())
                {
                    if (c.Contains(arc.getPoint1(), cosTolerance, sinTolerance))
                    {
                        return true;
                    }
                }
            }
            for (Patch patch2 : c.getPatchList())
            {
                for (Arc arc2 : patch2.getArcList())
                {
                    if (this.Contains(arc2.getPoint1(), cosTolerance, sinTolerance))
                    {
                        return true;
                    }
                }
            }
            double tolerance = Constant.Tolerance;
            for (Patch patch3 : this.getPatchList())
            {
                for (Patch patch4 : c.getPatchList())
                {
                    for (Arc arc3 : patch3.getArcList())
                    {
                        for (Arc arc4 : patch4.getArcList())
                        {
                        	//////////initialize//////////////////////
                            Cartesian[] cartesian = new Cartesian[1];
                            Cartesian[] cartesian2 = new Cartesian[1];
                            int[] num4 = new int[1];
                            switch (arc3.getCircle().GetTopo(arc4.getCircle(), /*out*/ cartesian, /*out*/ cartesian2, /*out*/ num4))
                            {
                                case Intersect:
                                {
                                    double angle = arc3.GetAngle(cartesian[0]);
                                    double num6 = arc4.GetAngle(cartesian[0]);
                                    if (((angle < (arc3.getAngle() + tolerance)) && (num6 < (arc4.getAngle() + tolerance))) && ((angle > -tolerance) && (num6 > -tolerance)))
                                    {
                                        return true;
                                    }
                                    double num7 = arc3.GetAngle(cartesian2[0]);
                                    double num8 = arc4.GetAngle(cartesian2[0]);
                                    if (((num7 < (arc3.getAngle() + tolerance)) && (num8 < (arc4.getAngle() + tolerance))) && ((num7 > -tolerance) && (num8 > -tolerance)))
                                    {
                                        return true;
                                    }
                                    break;
                                }
                                case Same:
                                {
                                    double num9 = arc3.GetAngle(arc4.getPoint1());
                                    double num10 = arc3.GetAngle(arc4.getPoint2());
                                    if (((num9 < (arc3.getAngle() + tolerance)) && (num9 > -tolerance)) || ((num10 < (arc3.getAngle() + tolerance)) && (num10 > -tolerance)))
                                    {
                                        return true;
                                    }
                                    num9 = arc4.GetAngle(arc3.getPoint1());
                                    num10 = arc4.GetAngle(arc3.getPoint2());
                                    if (((num9 < (arc4.getAngle() + tolerance)) && (num9 > -tolerance)) || ((num10 < (arc4.getAngle() + tolerance)) && (num10 > -tolerance)))
                                    {
                                        return true;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    boolean ElimCovers(Convex that)
    {
        if (!this.simplified || !this.sorted)
        {
            throw new RuntimeException("Convex.ElimCovers(): this should be simplified and sorted");
        }
        if (!that.simplified || !that.sorted)
        {
            throw new RuntimeException("Convex.ElimCovers(): that should be simplified and sorted");
        }
        if (this.halfspaceList.size() > that.halfspaceList.size())
        {
            return false;
        }
        Halfspace h = new Halfspace();
        Halfspace g = new Halfspace();
        int count = this.halfspaceList.size();
        int num4 = that.halfspaceList.size();
        int num2 = 0;
        for (int i = 0; i < this.halfspaceList.size(); i++)
        {
            h = this.halfspaceList.get(i);
            while (true)
            {
                if ((count - i) > (num4 - num2))
                {
                    return false;
                }
                g = that.halfspaceList.get(num2);
                if ((Halfspace.ComparisonRadiusXYZ(h, g) == 0) || (h.GetTopo(g) == Topo.Same))
                {
                    num2++;
                    break;
                }
                num2++;
            }
        }
        return true;
    }

//    public boolean Exists(Predicate<Halfspace> match)
//    {
//        return this.halfspaceList.Exists(match);
//    }
//
    public static boolean GetCollisions(List<Convex> convexList, /*out*/ DynSymMatrix<Boolean> mat)
    {
        mat = new DynSymMatrix<Boolean>(convexList.size());
        boolean flag = true;
        for (int i = 0; i < convexList.size(); i++)
        {
            Convex convex = convexList.get(i);
            for (int j = 0; j < i; j++)
            {
                Convex c = convexList.get(j);
                if (convex.DoesCollide(c))
                {
                    mat.setThis(true,i, j);
                    flag = false;
                }
            }
        }
        return flag;
    }

    public Convex Grow(double arcmin)
    {
        this.setSimplified(false);
        for (int i = 0; i < this.halfspaceList.size(); i++)
        {
            this.halfspaceList.set(i, this.halfspaceList.get(i).Grow(arcmin));
        }
        return this;
    }

    public boolean HasCommonHalfspace(Convex c)
    {
        Convex convex = new Convex(this);
        convex.AddRange(c.halfspaceList);
        convex.Sort();
        Halfspace h = new Halfspace();
        for (Halfspace halfspace2 : convex.halfspaceList)
        {
            if (halfspace2.GetTopo(h) == Topo.Same)
            {
                return true;
            }
            h = halfspace2;
        }
        return false;
    }

    public boolean HasESignNegative()
    {
        if (!this.sorted)
        {
            this.Sort();
        }
        Halfspace halfspace = this.halfspaceList.get(this.halfspaceList.size() - 1);
        return (halfspace.getESign() == ESign.Negative);
    }

    public boolean HasOnlyOneCommonHalfspace(Convex c)
    {
        Convex convex = new Convex(this);
        convex.AddRange(c.halfspaceList);
        convex.Sort();
        Halfspace h = new Halfspace();
        int num = 0;
        for (Halfspace halfspace2 : convex.halfspaceList)
        {
            if ((halfspace2.GetTopo(h) == Topo.Same) && (num++ > 1))
            {
                return false;
            }
            h = halfspace2;
        }
        return (num == 1);
    }

//    public boolean HasOnlyOneESignNegative()
//    {
//        boolean flag = this.HasESignNegative();
//        if (!flag || (this.getHalfspaceList().size() != 1))
//        {
//            if (!flag)
//            {
//                goto Label_0040;
//            }
//            Halfspace halfspace = this.halfspaceList[this.halfspaceList.Count - 2];
//            if (halfspace.ESign == ESign.Negative)
//            {
//                goto Label_0040;
//            }
//        }
//        return true;
//    Label_0040:
//        return false;
//    }

    public int IndexFirstCommonHalfspacePair(Convex c, /*out*/ int idx)
    {
        if (!this.sorted)
        {
            this.Sort();
        }
        if (!c.sorted)
        {
            c.Sort();
        }
        int num = 0;
        int num2 = 0;
        Halfspace h = this.halfspaceList.get(num);
        Halfspace g = c.halfspaceList.get(num2);
        boolean flag = false;
        while (!flag)
        {
            int num3 = Halfspace.ComparisonRadiusXYZ(h, g);
            Topo topo = h.GetTopo(g);
            if ((num3 == 0) || (topo == Topo.Same))
            {
                flag = true;
            }
            else
            {
                if (num3 == 1)
                {
                    if (num2 == (c.halfspaceList.size() - 1))
                    {
                        throw new RuntimeException("Convex.IndexFirstCommonHalfspacePair(): no common");
                    }
                    num2++;
                    g = c.halfspaceList.get(num2);
                    continue;
                }
                if (num == (this.halfspaceList.size() - 1))
                {
                    throw new RuntimeException("Convex.IndexFirstCommonHalfspacePair(): no common");
                }
                num++;
                h = this.halfspaceList.get(num);
            }
        }
        idx = num2;
        return num;
    }

    public void Intersect(Convex c)
    {
        this.AddRange(c.halfspaceList);
    }

    public void Intersect(Halfspace h)
    {
        this.Add(h);
    }

    void InvertHalfspaces()
    {
        this.setSimplified(false);
        this.sorted = false;
//        this.halfspaceList = this.halfspaceList.ConvertAll<Halfspace>(new Converter<Halfspace, Halfspace>(Halfspace.Invert));
        for(int i = 0; i < this.halfspaceList.size(); i++)
        {
        	Halfspace var = this.halfspaceList.get(i);
        	var.Invert();
        	this.halfspaceList.set(i, var);
        }
    }

    public boolean MecContains(Cartesian x)
    {
        return this.MecContains(x, Constant.CosTolerance, Constant.SinTolerance);
    }

    public boolean MecContains(Cartesian x, double costol, double sintol)
    {
        for (Patch patch : this.patchList)
        {
            if (patch.getMec().Contains(x, costol, sintol))
            {
                return true;
            }
        }
        return false;
    }

    public boolean MecDisjoint(Convex c)
    {
        for (Patch patch : this.patchList)
        {
            for (Patch patch2 : c.patchList)
            {
                Topo topo = patch.getMec().GetTopo(patch2.getMec());
                if ((topo != Topo.Disjoint) && (topo != Topo.Inverse))
                {
                    return false;
                }
            }
        }
        return true;
    }

    public static Convex Parse(String repr, boolean normalize)
    {
        char[] separator = new char[] { ' ' };
        String str = repr.trim().replace(',', ' ').replace("\r\n", " ").replace('\n', ' ').replace('\t', ' ');
        String str2 = "";
        while (str2 != str)
        {
            str2 = str;
            str = str2.replace("  ", " ");
        }
        String[] strArray = str2.split(separator[0]+"");
        if (strArray[0] != Constant.KeywordConvex)
        {
            throw new RuntimeException("Convex.Parse(repr,norm): keyword does not match");
        }
        if ((strArray.length % 4) != 1)
        {
            throw new RuntimeException("Convex.Parse(): # of input values in String are wrong: "+strArray.length );
        }
        Convex convex = new Convex();
        for (int i = 1; i < strArray.length; i += 4)
        {
            double x = Double.parseDouble(strArray[i]);
            double y = Double.parseDouble(strArray[i + 1]);
            double z = Double.parseDouble(strArray[i + 2]);
            double num4 = Double.parseDouble(strArray[i + 3]);
            convex.halfspaceList.add(new Halfspace(new Cartesian(x, y, z, normalize), num4, Math.sqrt(1.0 - (num4 * num4))));
        }
        return convex;
    }

    public void RemoveAt(int index)
    {
        this.eSign = ESign.Unknown;
        this.setSimplified(false);
        this.halfspaceList.remove(index);
    }

    public void RemoveRange(int index, int count)
    {
        this.eSign = ESign.Unknown;
        this.setSimplified(false) ;
//        this.halfspaceList.RemoveRange(index, count);
        for(int i =0;i<count;i++)
        {
        	this.halfspaceList.remove(index);
        }
        
    }
    public boolean Same(Convex c)
    {
        if (c.halfspaceList.size() != this.halfspaceList.size())
        {
            return false;
        }
        if (!this.getSorted())
        {
            this.Sort();
        }
        if (!c.getSorted())
        {
            c.Sort();
        }
        for (int i = 0; i < this.halfspaceList.size(); i++)
        {
            Halfspace halfspace = c.halfspaceList.get(i);
            if (halfspace.GetTopo(this.halfspaceList.get(i)) != Topo.Same)
            {
                return false;
            }
        }
        return true;
    }

    boolean SimpleSimplify()
    {
        List<Integer> list = new ArrayList<Integer>();
        boolean flag = false;
        if (this.halfspaceList.size() == 0)
        {
            return false;
        }
        if (!this.sorted)
        {
            this.Sort();
        }
        Halfspace halfspace3 = this.halfspaceList.get(0);
        if (halfspace3.IsEmpty())
        {
            this.halfspaceList.clear();
            this.sorted = false;
            return false;
        }
        for (int i = 0; (i < this.halfspaceList.size()) && !flag; i++)
        {
            if (!list.contains(i))
            {
                Halfspace h = this.halfspaceList.get(i);
                for (int j = 0; j < i; j++)
                {
                    if (!list.contains(j))
                    {
                    	///////initialize///////////////////////////////////////////////////
                        Cartesian[] cartesian = new Cartesian[1];
                        Cartesian[] cartesian2 = new Cartesian[1];
                        Halfspace halfspace2 = this.halfspaceList.get(j);
                        switch (halfspace2.GetTopo(h, /*out*/ cartesian, /*out*/ cartesian2))
                        {
                            case Same:
                            case Inner:
                            {
                                list.add(i);
                                continue;
                            }
                            case Inverse:
                            case Disjoint:
                            {
                                flag = true;
                                continue;
                            }
                        }
                    }
                }
            }
        }
        if (flag)
        {
            this.halfspaceList.clear();
            this.sorted = false;
        }
        else
        {
        	Collections.sort(list);
        	Collections.reverse(list);
            for (int num3 : list)
            {
                this.halfspaceList.remove(num3);
            }
            if (this.halfspaceList.size() == 0)
            {
                throw new RuntimeException("Convex.SimpleSimplify(): no halfspaces left? shouldn't happen");
            }
        }
        return !flag;
    }

    public boolean Simplify()
    {
        return this.Simplify(true);
    }

    public boolean Simplify(boolean simple_simplify)
    {
        if (!this.simplified)
        {
            if (simple_simplify && !this.SimpleSimplify())
            {
                this.simplified = true;
                this.area = 0.0;
                return false;
            }
            if (!this.sorted)
            {
                this.Sort();
            }
            this.patchList = new PatchFactory(this).DerivePatches(true);
            this.simplified = true;
            this.area = 0.0;
            if (this.halfspaceList == null)
            {
                throw new RuntimeException("Convex.Simplify(): HalfspaceList is NULL");
            }
            if (this.halfspaceList.size() == 0)
            {
                return false;
            }
            for (Patch patch : this.patchList)
            {
                Double area = this.area;
                double num = patch.getArea();
                this.area = area !=null ? new Double(area+num) : null;
//                this.area = area.HasValue ? new Double(area.GetValueOrDefault() + num) : null;
            }
            Halfspace halfspace2 = this.halfspaceList.get(0);
            if (halfspace2.IsAll())
            {
                Double nullable3 = this.area;
                double tolArea = Constant.TolArea;
                if ( (nullable3!=null) && (nullable3 < tolArea))
                {
                    Double nullable4 = this.area;
                    double wholeSphereInSquareDegree = Constant.WholeSphereInSquareDegree;
                    this.area = nullable4!=null ? new Double(nullable4+wholeSphereInSquareDegree) : null;
                }
            }
            if (this.area < 0.0)
            {
                boolean flag2 = false;
                for (Halfspace halfspace : this.halfspaceList)
                {
                    if (halfspace.getESign() != ESign.Negative)
                    {
                        flag2 = true;
                        break;
                    }
                }
                if (flag2)
                {
                    this.area = 0.0;
                    return false;
                }
                Double nullable7 = this.area;
                double num4 = Constant.WholeSphereInSquareDegree;
                this.area = nullable7!=null ? new Double(nullable7 + num4) : null;
            }
        }
        return true;
    }

    public boolean SmartContains(Cartesian x)
    {
        return this.SmartContains(x, Constant.CosTolerance, Constant.SinTolerance);
    }

    public boolean SmartContains(Cartesian x, double costol, double sintol)
    {
        return (this.MecContains(x, costol, sintol) && this.Contains(x, costol, sintol));
    }

    public Region SmartDifference(Convex convex, /*out*/ boolean[] flag)
    {
        Convex c = new Convex(this);
        flag[0] = c.SmartIntersect(convex);
        Region region = new Region();
        if (!flag[0])
        {
            region.Add(new Convex(this));
            region.UpdateArea();
            return region;
        }
        if (!this.Same(c))
        {
            region = this.Difference(c);
            region.Simplify(true, false, false, false, null);
        }
        return region;
    }

    public boolean SmartIntersect(Convex c)
    {
        if (this.MecDisjoint(c))
        {
            this.halfspaceList.clear();
            this.patchList = null;
            this.area = 0.0;
            this.simplified = true;
            return false;
        }
        double local1 = this.getArea();
        this.Intersect(c);
        return this.Simplify(true);
    }

    public void Sort()
    {
//        this.halfspaceList.Sort(new Comparison<Halfspace>(Halfspace.ComparisonRadiusXYZ));
        Collections.sort(this.halfspaceList, new ComparisonRadiusXYZComparator());
        this.sorted = true;
    }

    public  String toString()
    {
        return this.ToString("\r\n\t");
    }

    public String ToString(String sep)
    {
        String keywordConvex = Constant.KeywordConvex;
        for (Halfspace halfspace : this.halfspaceList)
        {
            keywordConvex = keywordConvex + sep + halfspace.toString();
        }
        return keywordConvex;
    }

//    public boolean TrueForAll(Predicate<Halfspace> match)
//    {
//        return this.halfspaceList.TrueForAll(match);
//    }

    // Properties
    
    public Double getArea()
    {
        return this.area;
    }
    public void setArea(Double value)
    {
    	this.area = value;
    }
//    [XmlIgnore]
//    public int getCapacity()
//    {
//        return this.halfspaceList.Capacity;
//    }
//    public void setCapacity(int value)
//    {
//    	 this.halfspaceList.Capacity = value;
//    }

    public int getCount()
    {
        return this.halfspaceList.size();
    }

//    [XmlAttribute]
    public ESign getESign()
    {
            if (this.eSign == ESign.Unknown)
            {
                Halfspace halfspace = this.halfspaceList.get(0);
                this.eSign = halfspace.getESign();
                for (int i = 1; i < this.halfspaceList.size(); i++)
                {
                    Halfspace halfspace2 = this.getHalfspaceList().get(i);
                    if (this.eSign != halfspace2.getESign())
                    {
                        this.eSign = ESign.Mixed;
                        break;
                    }
                }
            }
            return this.eSign;
    }
    public void setESign(ESign value)
    {
    	this.eSign = value;
    }

//    [XmlIgnore] ReadOnlyCollection
    public List<Halfspace> getHalfspaceList()
    {
        return this.halfspaceList;
    }

    public List<Patch> getPatchList()
    {
        return this.patchList;
    }
    public void setPatchList(List<Patch> value)
    {
    	 this.patchList = value;
    }

//    [XmlAttribute]
    public boolean getSimplified()
    {
        return this.simplified;
    }
    public void setSimplified(boolean value)
    {
    	if (this.simplified != value)
        {
            if (!value)
            {
                this.simplified = false;
                this.patchList = null;
                this.area = 0;
            }
            else
            {
                this.simplified = true;
            }
        }
    }

//    [XmlAttribute]
    public boolean getSorted()
    {
        return this.sorted;
    }
    public void setSorted(boolean value)
    {
    	this.sorted = true;
    }

//    [XmlArray(ElementName="HalfspaceList", Namespace="ivo://voservices.org/spherical")]
    public List<Halfspace> getXmlSerialization()
    {
        return this.halfspaceList;
    }
    public void setXmlSerialization(List<Halfspace> value)
    {
    	this.halfspaceList = value;
    }
}
class AreaDescendingComparator implements Comparator<Convex>
{
	public int compare(Convex c, Convex k) {
		 return k.getArea().compareTo(c.getArea());
	}
}
