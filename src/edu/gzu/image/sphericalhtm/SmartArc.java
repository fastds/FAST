package edu.gzu.image.sphericalhtm;

import edu.gzu.image.sphericallib.Arc;

class SmartArc {
	 // Fields
    public Arc Arc;
    long Hid1;
    long Hid2;

    // Methods
    SmartArc(Arc a)
    {
        this.Arc = a;
        this.Hid1 = Trixel.CartesianToHid20(a.getPoint1());
        this.Hid2 = Trixel.CartesianToHid20(a.getPoint2());
    }


}
