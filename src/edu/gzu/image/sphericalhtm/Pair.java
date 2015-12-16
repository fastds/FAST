package edu.gzu.image.sphericalhtm;

public class Pair implements Comparable<Pair>{
	private long hi;
	private long lo;
	public Pair()
	{}
	public Pair(long lo,long hi)
	{
		this.lo = lo;
		this.hi = hi;
	}
	public long getHi() {
		return hi;
	}
	public void setHi(long hi) {
		this.hi = hi;
	}
	public long getLo() {
		return lo;
	}
	public void setLo(long lo) {
		this.lo = lo;
	}
	public int compareTo(Pair o) {
		return new Long(this.getLo()).compareTo(o.getLo());
	}
	

}
