package edu.gzu.image.sphericallib;

import java.util.List;

public interface IPatch {
	 boolean ContainsOnEdge(Cartesian p);

    // Properties
    Halfspace getMec();
	List<Arc> getArcList();
}
