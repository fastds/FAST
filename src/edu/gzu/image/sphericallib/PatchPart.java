package edu.gzu.image.sphericallib;

import java.util.ArrayList;
import java.util.List;

public class PatchPart implements IPatch{
	 // Fields
    private List<Arc> arcList = new ArrayList<Arc>();
    private double length;
    private Halfspace mec;

    // Methods
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

    public String toString()
    {
        String str = "# "+this.arcList.size()+"\n";
        String[] strArray = new String[this.arcList.size()];
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        for (int i = 0; i < this.arcList.size(); i++)
        {
            strArray[i] = this.arcList.get(i).toString();
            sb.append(strArray[i]+"\n");
        }
        return (str + sb.toString().substring(0,sb.toString().length()-1));
    }

    // Properties
    public List<Arc> getArcList() {
		return arcList;
	}

	public void setArcList(List<Arc> arcList) {
		this.arcList = arcList;
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
//            return this.arcList;
//        }
//    }

}
