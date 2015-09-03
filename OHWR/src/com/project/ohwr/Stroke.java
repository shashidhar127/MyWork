package com.project.ohwr;
/**
 * @author jshashi
 *
 */


import java.util.*;


public class Stroke {
	
	final int NUM_COORDS = 60;
	final int COORDINATES_INCREAMENT = 60; 
	
	Vector<Coordinate> coordinates;
	int coordinatesCount;
	int length;
	short xMin;
	short xMax;
	short yMin;
	short yMax;
	int xMean;
	int yMean;
	
	Stroke()
	{
		coordinates = new Vector<Coordinate>(NUM_COORDS, COORDINATES_INCREAMENT);
		System.out.println("In Stroke constructor");
		coordinatesCount = 0;
		length = 0;
		xMin = 0;
		xMax = 0;
		yMin = 0;
		yMax = 0;
		xMean = 0;
		yMean = 0;
	}
	
	boolean addCoordinate(Coordinate coord)
	{
		boolean result;
		short xCoord;
		short yCoord;
		xCoord = coord.getX();
		yCoord = coord.getY();
		
		result = coordinates.add(coord);
		
		if(true == result)
		{
			coordinatesCount++;
			if(1 == coordinatesCount)
			{
				xMin = xCoord;
				yMin = yCoord;
				xMax = xCoord;
				yMax = yCoord;
				xMean = xCoord;
				yMean = yCoord;
			}
			else
			{
				if(xCoord <= xMin )
					xMin = xCoord;
				if(yCoord <= yMin)
					yMin = yCoord;
				if(xCoord >= xMax)
					xMax = xCoord;
				if(yCoord >= yMax)
					yMax = yCoord;
				xMean += xCoord;
				yMean += yCoord;
			}
			return true;
		}
		else
			return false;
	}
	
	public Coordinate getCoord(int i)
	{
		return this.coordinates.get(i);
	}
	
	
	short getXMean()
	{
		return (short) (xMean / coordinatesCount);
	}
	
	short getYMean()
	{
		return (short) (yMean / coordinatesCount);
	}
	
	short getXMin()
	{
		return xMin;
	}
	
	short getXMax()
	{
		return xMax;
	}
	
	short getYMin()
	{
		return yMin;
	}
	
	short getYMax()
	{
		return yMax;
	}
	
	int getCoordCount()
	{
		return this.coordinatesCount;
	}
	
}
