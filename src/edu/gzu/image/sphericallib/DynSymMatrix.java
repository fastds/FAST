package edu.gzu.image.sphericallib;

import java.util.ArrayList;
import java.util.List;

public class DynSymMatrix<Boolean>{
	// Fields
    private List<List<Boolean>> data;
    public static final String Revision = "$Revision: 1.5 $";

    // Methods 单例
    DynSymMatrix()
    {
    }

    public DynSymMatrix(int dim)
    {
        this.data = new ArrayList<List<Boolean>>(dim);
        for (int i = 0; i < dim; i++)
        {
            this.data.add(new ArrayList<Boolean>(i + 1));
            for (int j = 0; j <= i; j++)
            {
//                T item = default(T);
            	Boolean item = (Boolean) new java.lang.Boolean(false);
                this.data.get(i).add(item);
            }
        }
    }

    public int CountValue(Boolean t)
    {
        int num = 0;
        for (int i = 0; i < this.data.size(); i++)
        {
            for (int j = 0; j <= i; j++)
            {
                Boolean local = this.data.get(i).get(j);
                if (local.equals(t))
                {
                    if (j == i)
                    {
                        num++;
                    }
                    else
                    {
                        num += 2;
                    }
                }
            }
        }
        return num;
    }

    public boolean FindValue(Boolean t, /*out*/ int i, /*out*/ int j)
    {
        i = j = -1;
        j = 0;
        while (j < this.data.size())
        {
            i = 0;
            while (i <= j)
            {
                Boolean local = this.data.get(j).get(i);
                if (local.equals(t))
                {
                    return true;
                }
                i++;
            }
            j++;
        }
        return false;
    }

    public boolean FindValue(Boolean t, List<Integer> index, /*out*/ int[] i, /*out*/ int[] j)
    {
        i[0] = j[0] = -1;
        for (int k = 0; k < index.size(); k++)
        {
            i[0] = index.get(k);
            for (int m = k; m < index.size(); m++)
            {
                j[0] = index.get(m);
                Boolean local = getThis(i[0], j[0]);
                if ( local.equals(t))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public void RemoveAt(int index)
    {
        for (int i = index; i < this.data.size(); i++)
        {
            this.data.get(i).remove(index);
        }
        this.data.remove(index);
    }

    public List<Boolean> Row(int index)
    {
        List<Boolean> list = new ArrayList<Boolean>(this.data.size());
        for (int i = 0; i < this.data.size(); i++)
        {
            list.add(getThis(index, i));
        }
        return list;
    }

    public String toString()
    {
        String str = "";
        for (int i = 0; i < this.data.size(); i++)
        {
            for (int j = 0; j < this.data.get(i).size(); j++)
            {
                str = str + " " + this.data.get(i).get(j).toString();
            }
            str = str + "\n";
        }
        return str;
    }

    // Properties
    public int getDim()
    {
        return this.data.size();
    }
    public void setDim(int value)
    {
    	if (this.data.size() < value)
        {
    		
//            this.data.Capacity = value;
            for (int i = this.data.size(); i < value; i++)
            {
                this.data.add(new ArrayList<Boolean>(i));
                for (int j = 0; j <= i; j++)
                {
                	Boolean item = (Boolean) new java.lang.Boolean(false);
                    this.data.get(i).add(item);
                }
            }
        }
        else if (this.data.size() > value)
        {
            throw new RuntimeException("Dim_set(): Count smaller than existing!");
        }
    }

    public Boolean getThis(int i, int j)
    {
            if (i > j)
            {
                return this.data.get(i).get(j);
            }
            return this.data.get(j).get(i);
    }
    public void setThis(Boolean value,int i,int j)
    {
    	if (i > j)
        {
            this.data.get(i).set(j, value);
        }
        else
        {
            this.data.get(j).set(i, value);
        }
    }



}
