package com.project.ohwr;
/**
 * @author jshashi
 *
 */


public class Coordinate {
	
	final int DEFAULT_VALUE = 0;
    short x;
    short y;
    
    Coordinate()
    {
    	x = DEFAULT_VALUE;
    	y = DEFAULT_VALUE;
    	System.out.println("In Coordinate constructor");
    }
    
    short getX()
    {
    	return x;
    }
    short getY()
    {
    	return y;
    }
    void putX(short x)
    {
    	this.x = x;
    }
    void putY(short y)
    {
    	this.y = y;
    }
}