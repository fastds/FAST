package edu.gzu.image.sphericallib;

public final class Global {
	  // Fields
    private static final Global instance = new Global();
    private int maxiter = 0x4e20;
    public static final String Revision = "$Revision: 1.8 $";

    // Methods
    private Global()
    {
    }

    public String toString()
    {
        return "Maximum number of iterations: "+ this.maxiter;
    }

    // Properties
    public int getMaximumIteration()
    {
        return this.maxiter;
    }
    public void setMaximumIteration(int value)
    {
        this.maxiter = value;
    }

    public static Global getParameter()
    {
        return instance;
    }


}
