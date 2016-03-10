package edu.gzu.image.sphericalhtm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hidranges{
	  // Fields
    public List<Pair> pairList;

    // Methods
    public Hidranges()
    {
        this.pairList = new ArrayList<Pair>();
    }

    Hidranges(List<ISmartTrixel> nodeList, boolean sortandcheck)
    {
        this.pairList = new ArrayList<Pair>();
        if (nodeList != null)
        {
            for (ISmartTrixel trixel : nodeList)
            {
                Pair pair = Trixel.Extend(trixel.GetHid(), 20);
                this.AddRange(pair);
            }
            if (sortandcheck)
            {
                this.Sort();
                this.Compact();
                this.Check();
            }
        }
    }

    Hidranges(List<ISmartTrixel> nodeList, boolean sortandcheck, int deepest)
    {
        this.pairList = new ArrayList<Pair>();
        if (nodeList != null)
        {
            for (ISmartTrixel trixel : nodeList)
            {
                if (trixel.getLevel() > deepest)
                {
                    break;
                }
                Pair pair = Trixel.Extend(trixel.GetHid(), 20);
                this.AddRange(pair);
            }
            if (sortandcheck)
            {
                this.Sort();
                this.Compact();
                this.Check();
            }
        }
    }

    public void AddRange(Pair pair)
    {
        this.pairList.add(pair);
    }

    public void AddRange(long lo, long hi)
    {
        Pair pair = new Pair(lo,hi);
        this.pairList.add(pair);
    }

    public void Check()
    {
        for (int i = 1; i < this.pairList.size(); i++)
        {
            if (this.pairList.get(i-1).getHi() > this.pairList.get(i).getLo())
            {
                StringBuilder builder = new StringBuilder();
                builder.append("Range.check i="+i+": ("+this.pairList.get(i-1).getLo()+", "+this.pairList.get(i-1).getHi()+"), ("+this.pairList.get(i).getLo()+", "+this.pairList.get(i).getHi()+")");
//                throw new RuntimeException(builder.toString());???????
            }
        }
    }

    public void Clear()
    {
        this.pairList.clear();
    }

    public static List<Pair> Combine(List<Pair> ranges, List<Pair> newranges)
    {
        Pair pair = new Pair();
        List<Pair> list = new ArrayList<Pair>(ranges.size());
        boolean flag = false;
        boolean flag2 = false;
        if (newranges.size() < 1)
        {
            for (Pair pair2 : ranges)
            {
                list.add(pair2);
            }
            return list;
        }
        int num = newranges.size() + ranges.size();
        int num2 = 0;
        int num3 = 0;
        if ((ranges.size() > 0) && (ranges.get(0).getLo() <= newranges.get(0).getLo()))
        {
        	pair.setLo(ranges.get(0).getLo());
        	pair.setHi(ranges.get(0).getHi());
            flag = true;
            flag2 = true;
            num2++;
        }
        else
        {
        	pair.setLo(newranges.get(0).getLo());
        	pair.setHi(newranges.get(0).getHi());
            flag = true;
            flag2 = true;
            num3++;
        }
        num--;
        while (num > 0)
        {
            flag = false;
            if (!flag2)
            {
                if (num2 >= ranges.size())
                {
                    pair.setLo(newranges.get(num3).getLo());
                    pair.setHi(newranges.get(num3).getHi());
                    flag = true;
                    flag2 = true;
                    num3++;
                }
                else if (num3 >= newranges.size())
                {
                    pair.setLo(ranges.get(num2).getLo());
                    pair.setHi(ranges.get(num2).getHi());
                    flag = true;
                    flag2 = true;
                    num2++;
                }
                else if ((ranges.size() > 0) && (ranges.get(num2).getLo() <= newranges.get(num3).getLo()))
                {
                    pair.setLo(ranges.get(num2).getLo());
                    pair.setHi(ranges.get(num2).getHi());
                    flag = true;
                    flag2 = true;
                    num2++;
                }
                else
                {
                	pair.setLo(newranges.get(num3).getLo());
                    pair.setHi(newranges.get(num3).getHi());
                    flag = true;
                    flag2 = true;
                    num3++;
                }
                num--;
            }
            if (((num2 < ranges.size()) && flag2) && !isDisjoint(pair, ranges.get(num2)))
            {
            	pair.setLo(ranges.get(num2).getLo());
                pair.setHi(ranges.get(num2).getHi());
                num2++;
                num--;
                flag = true;
            }
            if (((num3 < newranges.size()) && flag2) && !isDisjoint(pair, newranges.get(num3)))
            {
            	pair.setLo(Math.min(newranges.get(num3).getLo(), pair.getLo()));
                pair.setHi(Math.max(newranges.get(num3).getHi(), pair.getHi()));
                num3++;
                num--;
                flag = true;
            }
            if (!flag)
            {
                list.add(pair);
                flag = false;
                flag2 = false;
            }
        }
        if (flag2)
        {
            list.add(pair);
            flag2 = false;
        }
        return list;
    }

    public void Compact()
    {
        int num = this.pairList.size() - 1;
        int index = 0;
        while (index < num-1)//   old : num
        {
            if (((this.pairList.get(index).getHi() + 1L) == this.pairList.get(index+1).getLo()) || (this.pairList.get(index).getHi() == this.pairList.get(index+1).getLo()))
            {
                Pair pair = new Pair(this.pairList.get(index).getLo(),this.pairList.get(index+1).getHi());
                this.pairList.remove(index + 1);
                this.pairList.remove(index);
//                this.pairList.set(index, pair); old have
                this.pairList.add(index, pair);// old none
                num--;
            }
            else
            {
                index++;
            }
        }
    }

    static void Compact(List<Pair> sortedPairs)
    {
        int num = sortedPairs.size() - 1;
        int index = 0;
        while (index < num)
        {
            if ((sortedPairs.get(index).getHi() + 1L) == sortedPairs.get(index+1).getLo())
            {
                Pair pair = new Pair(sortedPairs.get(index).getLo(),sortedPairs.get(index+1).getHi());
                sortedPairs.remove(index + 1);
                sortedPairs.remove(index);
                sortedPairs.set(index, pair);
                num--;
            }
            else
            {
                index++;
            }
        }
    }
//    static int CompareTo(Pair a, Pair b)
//    {
//        return new Long(a.getLo()).compareTo(b.getLo());
//    }

    private static boolean isDisjoint(Pair a, Pair b)
    {
        if (a.getHi() >= b.getLo())
        {
            return (b.getHi() < a.getLo());
        }
        return true;
    }

    static boolean IsWellFormed(List<Pair> sortedpairs)
    {
        for (int i = 1; i < sortedpairs.size(); i++)
        {
            if (sortedpairs.get(i-1).getLo() > sortedpairs.get(i).getLo())
            {
                throw new RuntimeException("sorted pair is not sorted");
            }
            if ((sortedpairs.get(i-1).getHi() + 1L) >= sortedpairs.get(i).getLo())
            {
                throw new RuntimeException("sorted pair is not disjoint from neighbor");
            }
        }
        return true;
    }

    void Merge(List<Pair> newranges)
    {
        this.pairList = Combine(this.pairList, newranges);
    }

    public void Sort()
    {
    	Collections.sort(this.pairList);
//   old     this.pairList.Sort(new Comparison<Pair>(Hidranges.CompareTo));
    }

    public String toString()
    {
        String str = "";
        for (Pair pair : this.pairList)
        {
            str += pair.getLo()+" - "+pair.getHi()+"\n";
        }
        return str;
    }
	
    

}
