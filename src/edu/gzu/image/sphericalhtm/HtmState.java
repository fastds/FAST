package edu.gzu.image.sphericalhtm;

import edu.gzu.image.sphericallib.Cartesian;

public class HtmState {
	// Fields
    private int _deltalevel;
    private double[] _featuresizes = new double[] { 
        1.5707963267949, 0.785398163397448, 0.392699081698724, 0.196349540849362, 0.098174770424681, 0.0490873852123405, 0.0245436926061703, 0.0122718463030851, 0.00613592315154256, 0.00306796157577128, 0.00153398078788564, 0.000766990393942821, 0.00038349519697141, 0.000191747598485705, 9.58737992428526E-05, 4.79368996214263E-05, 
        2.39684498107131E-05, 1.19842249053566E-05, 5.99211245267829E-06, 2.99605622633914E-06, 1.49802811316957E-06, 7.49014056584786E-07, 3.74507028292393E-07, 0.0, -1.0
     };
    private int _hdelta = 2;
    private int _maxlevel;
    private int _minlevel;
    private boolean _oktorun = true;
    private long _sid = 1L;
    private int _times;
    private long _uniqueID;
    private final String _versionstring = "Spherical.HTM 3.1.5f (Release - Built 5-1-2009)";
    private long _vid = 1L;
    HtmFace[] faces = new HtmFace[] { new HtmFace(8L, 'S', '0', 1, 5, 2), new HtmFace(9L, 'S', '1', 2, 5, 3), new HtmFace(10L, 'S', '2', 3, 5, 4), new HtmFace(11L, 'S', '3', 4, 5, 1), new HtmFace(12L, 'N', '0', 1, 0, 4), new HtmFace(13L, 'N', '1', 4, 0, 3), new HtmFace(14L, 'N', '2', 3, 0, 2), new HtmFace(15L, 'N', '3', 2, 0, 1) };
    private static final HtmState instance = new HtmState();
    Cartesian[] originalPoints = new Cartesian[] { new Cartesian(0.0, 0.0, 1.0, false), new Cartesian(1.0, 0.0, 0.0, false), new Cartesian(0.0, 1.0, 0.0, false), new Cartesian(-1.0, 0.0, 0.0, false), new Cartesian(0.0, -1.0, 0.0, false), new Cartesian(0.0, 0.0, -1.0, false) };


    // Methods
    private HtmState()
    {
    }

    long Face(int i, /*out*/ int[] ix, /*out*/ int[] iy, /*out*/ int[] iz)
    {
        ix[0] = this.faces[i].vi0;
        iy[0] = this.faces[i].vi1;
        iz[0] = this.faces[i].vi2;
        return this.faces[i].hid;
    }

     int getLevel(double radius)
    {
        int index = 0;
        while (this._featuresizes[index] > radius)
        {
            index++;
        }
        return index;
    }

    public String getVersion()
    {
        return "Spherical.HTM 3.1.5f (Release - Built 5-1-2009)";
    }

    public Cartesian getOriginalpoint(int i)
    {
        return this.originalPoints[i];
    }

     void resetVS()
    {
        this._sid = this._vid = 1L;
    }

    // Properties
    public int getDeltalevel()
    {
        return this._deltalevel;
    }
    public void setDeltalevel(int deltalevel)
    {
    	this._deltalevel = deltalevel;
    }

    public int getHdelta() 
    {
		return this._hdelta;
	}
    public void setHdelta(int hdelta)
    {
    	this._hdelta = hdelta;
    	throw new RuntimeException("Someone is changing helta");
    }
    public static HtmState getInstance()
    {
        return instance;
    }

    public int getMaxlevel()
    {
        return this._maxlevel;
    }
    
    public void setMaxlevel(int value)
    {
    	this._maxlevel = value;
    }

    public int getMinlevel()
    {
        return this._minlevel;
    }
    public void setMinlevel(int value)
    {
    	this._minlevel = value;
    }
    public long getNewID()
    {
        this._times++;
        this._uniqueID += 1L;
        return this._uniqueID;
    }

    public long getNewSid()
    {
        long num;
        this._sid = (num = this._sid) + 1L;
        return num;
    }

    public long getNewVid()
    {
        long num;
        this._vid = (num = this._vid) + 1L;
        return num;
    }

    public boolean getOkToRun()
    {
        return this._oktorun;
    }
    public void setOkToRun(boolean value)
    {
    	this._oktorun = value;
    }
    public int getTimes()
    {
        return this._times;
    }
}
