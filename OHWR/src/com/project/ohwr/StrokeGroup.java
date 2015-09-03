package com.project.ohwr;
import java.util.Vector;

/**
 * @author jshashi
 *
 */
public class StrokeGroup {
	final int NUM_STROKES = 4;
	final int STROKE_INCREAMENT = 4;
	String mName;
	
	Vector<Stroke> strokes;
	short strokesCount;
	int length;
	short xMin;
	short xMax;
	short yMin;
	short yMax;
	int xMean;
	int yMean;
	int deviceXMax;    // Maximum X coordinate of the device in which this stroke group is collected
	int deviceYMax;    // Maximum Y coordinate of the device in which this stroke group is collected
	
	StrokeGroup()
	{
		strokes = new Vector<Stroke>(NUM_STROKES, STROKE_INCREAMENT);
		System.out.println("In StrokeGroup constructor");
		strokesCount = 0;
		length = 0;
		xMin = 0;
		xMax = 0;
		yMin = 0;
		yMax = 0;
		xMean = 0;
		yMean = 0;
		deviceXMax = 0;
		deviceYMax = 0;
	}
	
	boolean addStroke(Stroke stroke)
	{
		boolean result = false;
		if(null != stroke)
		{
			result = this.strokes.add(stroke);
			
			if(result)
			{
				this.strokesCount++;
				if(1 == this.strokesCount)
				{
					xMin = stroke.getXMin();
					yMin = stroke.getYMin();
					xMax = stroke.getXMax();
					yMax = stroke.getYMax();
					xMean = stroke.getXMean();
					yMean = stroke.getYMean();
				}
				else
				{
					if(stroke.getXMin() <= xMin )
						xMin = stroke.getXMin();
					if(stroke.getYMin() <= yMin)
						yMin = stroke.getYMin();
					if(stroke.getXMax() >= xMax)
						xMax = stroke.getXMax();
					if(stroke.getYMax() >= yMax)
						yMax = stroke.getYMax();
					xMean += stroke.getXMean();
					yMean += stroke.getYMean();
				}
			}
			
		}
		return result;
	}
	
	short getXMean()
	{
		return (short) (xMean / strokesCount);
	}
	
	short getYMean()
	{
		return (short) (yMean / strokesCount);
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
	
	void setName(String name)
	{
		this.mName = name;
	}
	
	Stroke getStroke(int i)
	{
		if(i < this.strokesCount)
			return this.strokes.get(i);
		else
			return null;
	}

	String getName()
	{
		return this.mName;
	}
	
	int getStrokeCount()
	{
		return (int) this.strokesCount;
	}
	
	int getDeviceXMax()
	{
		return Utils.MAX(this.deviceXMax, this.xMax);
	}
	int getDeviceYMax()
	{
		return Utils.MAX(this.deviceYMax, this.yMax);
	}
	
	void putDeviceDimensions(int xMax, int yMax)
	{
		this.deviceXMax = xMax;
		this.deviceYMax = yMax;
	}
}
