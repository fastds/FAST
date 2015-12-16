package edu.gzu.image.sphericalhtm;

public interface ISmartTrixel {
	 void Expand();
    long GetHid();
    int getLevel();
    Markup GetMarkup();
    boolean isTerminal();
    void setTerminal(boolean value);

}
