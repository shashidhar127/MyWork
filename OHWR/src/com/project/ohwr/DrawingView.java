/**
 * @author jshashi
 *
 */
package com.project.ohwr;

import java.io.File;
import java.io.FileInputStream;  
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.Vector;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


import org.xmlpull.v1.XmlSerializer;

import android.content.DialogInterface;
import android.content.ContentResolver;

import android.view.Display;
import android.view.View;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;	
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.view.MotionEvent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.TypedValue;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;

 class DrawingView extends View {

    static private final String TAG = "OHWR";
	//drawing path
	private Path drawPath;
	//drawing and canvas paint
	private Paint drawPaint, canvasPaint;
	//initial color
	private int paintColor = 0xFF000000;
	//canvas
	private Canvas drawCanvas;
	//canvas bitmap
	private Bitmap canvasBitmap;
	
	private float brushSize, lastBrushSize;
	private int mMaxX, mMaxY;                 // Resolution of the device in which current stroke group is being collected
	private int mDataMaxX, mDataMaxY;         // Resolution of the device in which a stroke group was already collected. 
	                                          // These values has to be part of the xml from which the stroke group is loaded.
	                                         	
	private boolean erase=false;

	public int mDataCount[] = {0, 0, 0, 0, 0, 0};
	public int mLanguage = 0;
	
	//static private final int GET_TEXT_REQUEST_CODE = 1;
	private String mName;
	private String mAge;
	private String mGender;
	private String mRegion;
	
	
	private boolean enableDrawing = false;
	
	private Coordinate mCoord = null;
	
	private Stroke mStroke = null;
	
	private Vector<StrokeGroup> mClassSGs;
	//public Vector<StrokeGroup> mClassSGs;
	private Vector<String> mClassSGName;
	private int mClassSGCount;
	//public int mClassSGCount;
	private int mClassSGindex;
	
	private Vector<StrokeGroup> mDataSGs;
	private Vector<String> mDataSGName;
	private int mDataSGCount;
	private int mDataSGindex;
	
	private StrokeGroup mCurrSG = null;
	private String mCurrSGName = null;
	private String mDisplaySGName = null;
	private String mClassFileName = null;
	private String mDataFileName = null;
	private boolean mCollectData = true;
	private boolean mLoadingClass = false;
	
	public DrawingView(Context context, AttributeSet attrs){
	    super(context, attrs);
	    setupDrawing();
	    this.mClassSGs = new Vector<StrokeGroup>(Utils.NUM_STROKE_GRPOUPS, Utils.STROKE_GROUP_INCREAMENT);
	    this.mClassSGName = new Vector<String>(Utils.NUM_STROKE_GRPOUPS, Utils.STROKE_GROUP_INCREAMENT);
	    this.mClassSGCount = 0;
	    this.mClassSGindex = 0;
	    
	    this.mDataSGs = new Vector<StrokeGroup>(Utils.NUM_STROKE_GRPOUPS, Utils.STROKE_GROUP_INCREAMENT);
	    this.mDataSGName = new Vector<String>(Utils.NUM_STROKE_GRPOUPS, Utils.STROKE_GROUP_INCREAMENT);
	    this.mDataSGCount = 0;
	    this.mDataSGindex = 0;
	}
	
	public void setMaxSize(int maxX, int maxY)
	{
		this.mMaxX = this.mDataMaxX = maxX;
		this.mMaxY = this.mDataMaxY = maxY;
	}
	
	public void setDrawing(boolean enableFlag)
	{
		enableDrawing = enableFlag;
	}
	public void reInit(boolean collectDataFlag, int type)
	{
		this.mCollectData = collectDataFlag;
        if(true == this.mCollectData)
        {
        	this.mDataSGs = null;
        	this.mDataSGCount = 0;
        	this.mDataSGName = null;
        	this.mDataCount[this.mLanguage]++;
        	
        	Log.i(TAG, "Save data statistics : "+Utils.LANGUAGES[type] + " Data count : " + this.mDataCount[this.mLanguage]);
        	SaveDataStat();
        	
    	    this.mDataSGs = new Vector<StrokeGroup>(Utils.NUM_STROKE_GRPOUPS, Utils.STROKE_GROUP_INCREAMENT);
    	    this.mDataSGName = new Vector<String>(Utils.NUM_STROKE_GRPOUPS, Utils.STROKE_GROUP_INCREAMENT);
    	    this.mDataFileName = Utils.LANGUAGES[type] + "_data_"+Integer.toString(this.mDataCount[type]+1)+".xml";
    	    this.mDataSGCount = 0;
    	    this.mDataSGindex = 0;
        }
	}
	public void intializeMode(boolean collectDataFlag, int type)
	{
		this.mCollectData = collectDataFlag;
		this.mClassFileName = Utils.LANGUAGES[type] + ".xml";
		this.mLanguage = type;
		
		Log.i(TAG, "Reading xml : "+this.mClassFileName);
		readXML(this.mClassFileName, 1, true);
		Log.i(TAG, "Read xml : "+this.mClassFileName + "No. of class : " + this.mClassSGCount + " Class index : " + this.mClassSGindex );
		// TODO
		if(this.mCollectData)
		{
			readXML("DataStat.xml", 0, false);
		}
		else
		{
			this.mLoadingClass = true;
		}
		this.mDataFileName = Utils.LANGUAGES[type] + "_data_"+Integer.toString(this.mDataCount[type]+1)+".xml";
	}

	private void setupDrawing(){
		//get drawing area setup for interaction        
		brushSize = getResources().getInteger(R.integer.medium_size);
		lastBrushSize = brushSize;
		
		drawPath = new Path();
		drawPaint = new Paint();
		
		drawPaint.setColor(paintColor);
		
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(brushSize);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		
		canvasPaint = new Paint(Paint.DITHER_FLAG);
    }
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	//view given size
		super.onSizeChanged(w, h, oldw, oldh);
		
		canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		drawCanvas = new Canvas(canvasBitmap);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
	//draw view
		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		canvas.drawPath(drawPath, drawPaint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	//detect user touch     
		float touchX = event.getX();
		float touchY = event.getY();
		if(enableDrawing)
		{
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
				{
				    drawPath.moveTo(touchX, touchY);
				    if(null == mCoord)
	    			    mCoord = new Coordinate();
				    if(null == mStroke)
				    	mStroke = new Stroke();
				    if(null == mCurrSG)
				    	mCurrSG = new StrokeGroup();
				    
				    mCurrSG.putDeviceDimensions(this.mMaxX, this.mMaxY);
				    mCoord.putX((short)touchX);
				    mCoord.putY((short)touchY);
				    mStroke.addCoordinate(mCoord);
				    mCoord = null;
				    Log.i(TAG, "In ACTION_DOWN : x = "+touchX+"  y = "+touchY+"\n");
				}
				break;
				case MotionEvent.ACTION_MOVE:
				{
				    drawPath.lineTo(touchX, touchY);
				    if(null == mCoord)
				    	mCoord = new Coordinate();
				    mCoord.putX((short)touchX);
				    mCoord.putY((short)touchY);
				    mStroke.addCoordinate(mCoord);
				    mCoord = null;
				    Log.i(TAG, "In ACTION_MOVE : x = "+touchX+"  y = "+touchY+"\n");
				}
				    break;
				case MotionEvent.ACTION_UP:
				{
				    drawCanvas.drawPath(drawPath, drawPaint);
				    drawPath.reset();
				    mCurrSG.addStroke(mStroke);
				    mStroke = null;
				    mCoord = null;
				    Log.i(TAG, "In ACTION_UP : x = "+touchX+"  y = "+touchY+"\n");
				}
				    break;
				default:
				    return false;
			}
			invalidate();
		}
		return true;
	}
	
	public void setColor(String newColor){
		//set color     
		invalidate();
		paintColor = Color.parseColor(newColor);
		drawPaint.setColor(paintColor);
	}
	
	public void setBrushSize(float newSize){
		//update size
		float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
			    newSize, getResources().getDisplayMetrics());
		brushSize=pixelAmount;
		drawPaint.setStrokeWidth(brushSize);
	}
	
	public void setLastBrushSize(float lastSize){
	    lastBrushSize=lastSize;
	}
	
	public float getLastBrushSize(){
	    return lastBrushSize;
	}
	
	public void setErase(boolean isErase){
		//set erase true or false
		erase=isErase;
		if(erase) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		else drawPaint.setXfermode(null);
	}
	public void startNew(){
	    drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
	    invalidate();
	    mCurrSG = null;
	    mStroke = null;
	    mCoord = null;
	}
	public void clear()
	{
		drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
	    invalidate();
	    mCurrSG = null;
	    mStroke = null;
	    mCoord = null;
	}
	public int getSGCount(int type)
	{
		int count = 0;
		switch (type)
		{
			case Utils.DATA :
			{
				count = this.mDataSGCount;
			}
			break;
		    case Utils.CLASS :
			{
				count = this.mClassSGCount;
			}
			break;
		}
		return count;
	}
	public boolean checkCurrSGName(String sgName)
	{
		if(false == this.mClassSGName.contains(sgName))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean setCurrSGName(String sgName)
	{
		mCurrSGName = sgName;
		return true;
	}
	
	public boolean setDisplaySGName(String sgName)
	{
		mDisplaySGName = sgName;
		return true;
	}
	
	public String getDisplaySGName()
	{
		return mDisplaySGName;
	}
	
	public void saveCurrSG()
	{
		if(null != mCurrSG)
		{
			if(mCurrSG.strokesCount >= 1)
			{
				
				if(this.mLoadingClass == true)
				{
					mCurrSG.setName(this.mCurrSGName);
					this.mClassSGs.add(mCurrSG);
					this.mClassSGName.add(mCurrSGName);
					this.mClassSGCount++;					
				}
				else
				{
					mCurrSG.setName(this.mDisplaySGName);
					this.mDataSGs.add(mCurrSG);
					this.mDataSGName.add(mDisplaySGName);
					this.mDataSGCount++;
				}

				mCurrSG = null;
				mStroke = null;
				mCoord = null;
			}
		}
	}
	public boolean editSG(String SGName)
	{
		if(this.mLoadingClass == true)
		{
			if(null != mCurrSG)
			{
				if(null != SGName)
				{
					mCurrSGName = SGName;
				}
				mCurrSG.setName(mCurrSGName);
				this.mClassSGs.add(this.mClassSGindex, mCurrSG);
				this.mClassSGName.add(this.mClassSGindex, mCurrSGName);
				
			}
			else
			{
				this.mClassSGCount--;
				if(0 == this.mClassSGCount)
					this.mClassSGindex = 0;
				if(this.mClassSGindex >= this.mClassSGCount)
					this.mClassSGindex = this.mClassSGCount-1;
			}
			mCurrSG = null;
			mStroke = null;
			mCoord = null;
			mCurrSGName = null;
		}
		else
		{
			if(null != mCurrSG)
			{
				if(null != SGName)
				{
					mCurrSGName = SGName;
				}
				mCurrSG.setName(mCurrSGName);
				this.mDataSGs.add(this.mDataSGindex, mCurrSG);
				this.mDataSGName.add(this.mDataSGindex, mCurrSGName);
			}
			else
			{
				this.mDataSGCount--;
				if(0 == this.mDataSGCount)
					this.mDataSGindex = 0;
				if(this.mDataSGindex >= this.mDataSGCount)
					this.mDataSGindex = this.mDataSGCount-1;
			}
			mCurrSG = null;
			mStroke = null;
			mCoord = null;
			mCurrSGName = null;			
		}
		return true;
	}
	
	public boolean editSGInit()
	{
		if(this.mLoadingClass == true)
		{
			mCurrSG = null;
			mCurrSG = this.mClassSGs.get(this.mClassSGindex);
			mCurrSGName = this.mClassSGName.get(this.mClassSGindex);
			this.mClassSGs.remove(this.mClassSGindex);
			this.mClassSGName.remove(this.mClassSGindex);
			dispSG(mCurrSG);
			setDisplaySGName(mCurrSGName);
			setDrawing(true);
		}
		else
		{
			mCurrSG = null;
			mCurrSG = this.mDataSGs.get(this.mDataSGindex);
			mCurrSGName = this.mDataSGName.get(this.mDataSGindex);
			this.mDataSGs.remove(this.mDataSGindex);
			this.mDataSGName.remove(this.mDataSGindex);
			dispSG(mCurrSG);
			setDisplaySGName(mCurrSGName);
			setDrawing(true);			
		}
		return true;
	}
	
	public boolean displaySG(int type)
	{
		boolean returnStatus = false;
		switch (type)
		{
			case Utils.PREVIOUS_CLASS :
			{
				if(this.mClassSGCount == 0)
					return false;
				
				if(this.mClassSGindex <= 0)
				{
					this.mClassSGindex = this.mClassSGCount - 1;
				}
				else
				{
					this.mClassSGindex --;

				}
				returnStatus = dispSG(this.mClassSGs.get(this.mClassSGindex));
				if(true == returnStatus)
				{
					setDisplaySGName(this.mClassSGs.get(this.mClassSGindex).getName());
				}
			}
			break;
			case Utils.CURRENT_CLASS :
			{
				if(this.mClassSGCount == 0)
					return false;

				returnStatus = dispSG(this.mClassSGs.get(this.mClassSGindex));
				if(true == returnStatus)
				{
					setDisplaySGName(this.mClassSGs.get(this.mClassSGindex).getName());
				}
			}
			break;
			case Utils.NEXT_CLASS :
			{
				if(this.mClassSGCount == 0)
					return false;

				if(this.mClassSGindex >= this.mClassSGCount - 1)
				{
					this.mClassSGindex = 0;
				}
				else
				{
					this.mClassSGindex ++;
				}
				returnStatus = dispSG(this.mClassSGs.get(this.mClassSGindex));
				if(true == returnStatus)
				{
					setDisplaySGName(this.mClassSGs.get(this.mClassSGindex).getName());
				}
			}
			break;
			case Utils.PREVIOUS_DATA :
			{
				if(this.mDataSGCount == 0)
					return false;
				
				if(this.mDataSGindex <= 0)
				{
					this.mDataSGindex = this.mDataSGCount - 1;
				}
				else
				{
					this.mDataSGindex --;

				}
				returnStatus = dispSG(this.mDataSGs.get(this.mDataSGindex));
				if(true == returnStatus)
				{
					setDisplaySGName(this.mDataSGs.get(this.mDataSGindex).getName());
				}
			}
			break;
			case Utils.CURRENT_DATA :
			{
				if(this.mDataSGCount == 0)
					return false;

				returnStatus = dispSG(this.mDataSGs.get(this.mDataSGindex));
				if(true == returnStatus)
				{
					setDisplaySGName(this.mDataSGs.get(this.mDataSGindex).getName());
				}
			}
			break;
			case Utils.NEXT_DATA :
			{
				if(this.mDataSGCount == 0)
					return false;

				if(this.mDataSGindex >= this.mDataSGCount - 1)
				{
					this.mDataSGindex = 0;
				}
				else
				{
					this.mDataSGindex ++;
				}
				returnStatus = dispSG(this.mDataSGs.get(this.mDataSGindex));
				if(true == returnStatus)
				{
					setDisplaySGName(this.mDataSGs.get(this.mDataSGindex).getName());
				}
			}
			break;
			case Utils.CURRENT_SG_CLASS :
			{
				returnStatus = dispSG(this.mCurrSG);
				if(true == returnStatus)
				{
					setDisplaySGName(this.mCurrSGName);
				}
			}
			break;
			case Utils.CURRENT_SG_DATA :
			{
				returnStatus = dispSG(this.mCurrSG);
				if(true == returnStatus)
				{
					setDisplaySGName(this.mClassSGs.get(this.mClassSGindex).getName());
				}
			}
			break;
		}
		return returnStatus;
	}
	
	public boolean dispSG(StrokeGroup sg)
	{
		Stroke stroke = null;
		Coordinate coord = null;
		boolean returnStatus = false;
		
		drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
	    invalidate();

	    if(null != sg && sg.getStrokeCount() >= 0)
	    {		    
		    for(int i=0; i < sg.getStrokeCount(); i++)
		    {
		    	stroke = sg.getStroke(i);
		    	if(stroke != null)
		    	{
		    		for(int j=0; j < stroke.getCoordCount(); j++)
		    		{
		    			coord = stroke.getCoord(j);
		    			if(coord != null)
		    			{
		    				if(j == 0)
		    				{
		    					this.drawPath.moveTo( ( (float)coord.getX() * this.mMaxX ) / sg.getDeviceXMax() , ( (float)coord.getY() * this.mMaxY ) / sg.getDeviceYMax() );
		    				}
		    				else if(j == stroke.getCoordCount() - 1)
		    				{
							    this.drawCanvas.drawPath(drawPath, drawPaint);
							    this.drawPath.reset();
							    returnStatus = true;
		    				}
		    				else
		    				{
		    					this.drawPath.lineTo( ( (float)coord.getX() * this.mMaxX ) / sg.getDeviceXMax() , ( (float)coord.getY() * this.mMaxY ) / sg.getDeviceYMax() );
		    				}
		    			}
		    		}
		    	}
		    }
	    }
	    return returnStatus;
	}	
	
	public void mPutName(String name)
	{
		mName = name;
	}
	public void mPutAge(String age)
	{
		mAge = age;
	}
	public void mPutGender(String gender)
	{
		mGender = gender;
	}
	public void mPutRegion(String region)
	{
		mRegion = region;
	}

	public void xmlWriteText(XmlSerializer serializer, String tag, String text)
	{
        try {
			serializer.startTag(null, tag);
	        //set an attribute called "attribute" with a "value" for <child2>
	        serializer.text(text);
	        serializer.endTag(null, tag);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	public void xmlWriteAttribute(XmlSerializer serializer,  String attribute, String value)
	{
		try {
	        //set an attribute called "attribute" with a "value" for <child2>
	        serializer.attribute(null, attribute, value);
	        
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void xmlWriteStartTag(XmlSerializer serializer, String tag)
	{
		try {
			serializer.startTag(null, tag);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void xmlWriteEndTag(XmlSerializer serializer, String tag)
	{
		try {
			serializer.endTag(null, tag);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void readXML(String classFileName, int isSG, boolean isClass)
	{
		File classFile;
		FileInputStream fclassFileIn;
		XmlPullParserFactory xmlFactoryObject;
		XmlPullParser myparser;

		if(Utils.isExternalStorageReadable())
		{
			classFile = new File(Utils.ROOT_DIR[0]+"/"+classFileName);
			
			try 
			{
				fclassFileIn = new FileInputStream(classFile);
			} 
			catch(FileNotFoundException e)
			{
                Log.e("FileNotFoundException", "can't read FileInputStream");
                return;
            }
			
			try
			{
				xmlFactoryObject = XmlPullParserFactory.newInstance();
				myparser = xmlFactoryObject.newPullParser();
				myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			    myparser.setInput(fclassFileIn, null);
			    
			    if(isSG == 1)
			    {
			    	Log.i(TAG, "Loading xml : " + classFileName);
			    	loadSGsFromClass(myparser, isClass);
			    }
			    else
			    {
			    	loadDataStat(myparser);
			    }
				fclassFileIn.close();
			}
			catch (Exception e) 
			{
	        	Log.e("Exception","error occurred while creating xml file");
	        }			
		}
		else
		{
			Log.i(TAG, "ERROR : Device is not in readable state");
		}
		
	}
	
		
   public void loadSGsFromClass(XmlPullParser myParser, boolean isClass)
   {
		StrokeGroup Sg = null;
		Stroke stroke = null;
		Coordinate coord = null;
		int event, labelDesc = 0, strokeCnt = 0, coordCnt = 0, trace = 0;
		Integer x , y;
		String text=null, className= null, x_v, y_v;
		
		try
		{
			event = myParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT)
			{
				String name=myParser.getName();
				switch (event)
				{
					case XmlPullParser.START_TAG:
					{
						if(name.equals("strokeGroup"))
						{
							Log.i(TAG, "START_TAG: strokeGroup");
							strokeCnt = 0;
							coordCnt = 0;
							labelDesc = 0;
							className = null;
							Sg = null;
							Sg = new StrokeGroup();
						}
						else if(name.equals("stroke"))
						{
							Log.i(TAG, "START_TAG: stroke");
							coordCnt = 0;
							stroke = null;
					    	stroke = new Stroke();
						}
						else if(name.equals("hwTrace"))
						{
							Log.i(TAG, "START_TAG: hwTrace");
							trace = 1;
						}
						else if(name.equals("trace") && trace == 1)
						{
							Log.i(TAG, "START_TAG: trace");
							coord = null;
							coord = new Coordinate();
						}
						else if(name.equals("labelDesc"))
						{
							Log.i(TAG, "START_TAG: labelDesc");
							labelDesc = 1;
							
						}
						else if(name.equals("DeviceMaxDimensions"))
						{
							Log.i(TAG, "START_TAG: DeviceMaxDimensions");
						}
						
					}
					break;

					case XmlPullParser.TEXT:
					{
						text = myParser.getText();
						Log.i(TAG, "TEXT : " + text);
					}
					break;
					
					case XmlPullParser.END_TAG:
					{
						if(name.equals("strokeGroup"))
						{
							Log.i(TAG, "END_TAG: strokeGroup");
							if(null != Sg && 0 != strokeCnt)
							{
								Sg.setName(className);
								Sg.putDeviceDimensions(this.mDataMaxX, this.mDataMaxY);
								if(isClass == true)
								{
									this.mClassSGName.add(className);
									this.mClassSGs.add(Sg);
									this.mClassSGCount++;
									Log.i(TAG, "Adding a class stroke group : "+this.mClassSGCount );
								}
								else
								{
									this.mDataSGName.add(className);
									this.mDataSGs.add(Sg);
									this.mDataSGCount++;
									Log.i(TAG, "Adding a data stroke group : "+this.mDataSGCount );
								}
							}
							Sg = null;
							className = null;
							coord = null;
							stroke = null;
							strokeCnt = 0;
							coordCnt = 0;

							break;
						}
						else if(name.equals("stroke"))
						{
							Log.i(TAG, "END_TAG: stroke");
							if(null == Sg)
							{
								Sg = new StrokeGroup();
								strokeCnt = 0;
							}
							if(null != stroke && 0 != coordCnt)
							{
								Sg.addStroke(stroke);
								strokeCnt ++;
								Log.i(TAG, "Adding stroke : "+strokeCnt + " to stroke group");
							}
							stroke = null;
							coord = null;
							coordCnt = 0;

							break;
						}
						else if(name.equals("trace"))
						{
							Log.i(TAG, "END_TAG: trace");
							x_v = myParser.getAttributeValue(null,"x");
							x = Integer.parseInt(x_v);
							y_v = myParser.getAttributeValue(null,"y");
							y = Integer.parseInt(y_v);
							if(null != x && null != y)
							{
								if(null == stroke)
								{
									coordCnt = 0;
									stroke = new Stroke();
								}
								if(null == coord)
							    	coord = new Coordinate();
								coord.putX(x.shortValue());
								coord.putY(y.shortValue());
								stroke.addCoordinate(coord);
								coord = null;
								coordCnt ++;
								Log.i(TAG, "X : "+x.shortValue() + " Y : " + y.shortValue() + " coord count : " + coordCnt);
							}
							Log.i(TAG, "X : "+x.shortValue() + " Y : " + y.shortValue() + " coord count : " + coordCnt);
							break;
						}
						else if(name.equals("desc") && 1 == labelDesc)
						{
							Log.i(TAG, "END_TAG: desc");
							if(1 == labelDesc)
							{
								className = text;
								Log.i(TAG, "Class name : "+text);
								labelDesc = 0;
							}
							break;
						}
						else if(name.equals("DeviceMaxDimensions"))
						{
							this.mDataMaxX = Integer.parseInt(myParser.getAttributeValue(null, "XMax"));
							this.mDataMaxY = Integer.parseInt(myParser.getAttributeValue(null, "YMax"));
						}
					}
					break;
				}		 
				event = myParser.next(); 			
			}
		}
		catch (Exception e)
		{
		     e.printStackTrace();
		}
	
   }
		
	
	public boolean saveSG()
	{
		File newxmlfile;
		StrokeGroup Sg;
		Stroke stroke;
		Coordinate coord;
		String fileName;
		int SGCount = 0;
		Vector<StrokeGroup> SGVector = null;
		
		if(this.mCollectData == true && this.mDataSGCount <=0 )
		{
			return false;
		}
		if(this.mCollectData == false && this.mClassSGCount <=0 )
		{
			return false;
		}
		
		if(this.mCollectData == true)
		{
			SGCount = this.mDataSGCount;
			SGVector = this.mDataSGs;
		}
		else
		{
			SGCount = this.mClassSGCount;
			SGVector = this.mClassSGs;
		}
		
		if(this.mCollectData)
			fileName = Utils.ROOT_DIR[this.mLanguage]+ "/" + this.mDataFileName;
		else
			fileName = Utils.ROOT_DIR[0] + "/" + this.mClassFileName;
		
		if(false == Utils.isExternalStorageWritable())
		{
			return false;
		}
		newxmlfile = new File(fileName);
        try{
        	    
                newxmlfile.createNewFile();
        }catch(IOException e){
        	   Log.e("IOException", "exception in createNewFile() method");
        	   return false;
        }
        //we have to bind the new file with a FileOutputStream
        FileOutputStream fileos = null;        
        try
        {
            fileos = new FileOutputStream(newxmlfile);
        }
        catch(FileNotFoundException e)
        {
            Log.e("FileNotFoundException", "can't create FileOutputStream");
            return false;
        }
        //we create a XmlSerializer in order to write xml data
        XmlSerializer serializer = Xml.newSerializer();
        try
        {
	        //we set the FileOutputStream as output for the serializer, using UTF-8 encoding
	        serializer.setOutput(fileos, "UTF-8");
	        //Write <?xml declaration with encoding (if encoding not null) and standalone flag (if standalone not null)
	        serializer.startDocument(null, Boolean.valueOf(true));
	        //set indentation option
	        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
	        
	        if(true == this.mCollectData)
	        {
		        serializer.startTag(null, "writerDef");
		          xmlWriteText(serializer, "name", mName);
		          xmlWriteText(serializer, "age", mAge);
		          xmlWriteText(serializer, "gender", mGender);
		          xmlWriteText(serializer, "region", mRegion);
		          xmlWriteText(serializer, "dateOfCollection", "0-11-2014");
		          xmlWriteText(serializer, "educationLevel", "ME");
		          xmlWriteText(serializer, "profession", "EMPLOYED");
		          xmlWriteText(serializer, "hand", "Right");
		          xmlWriteText(serializer, "deviceType", android.os.Build.MANUFACTURER + "-" + android.os.Build.MODEL);
		          xmlWriteStartTag(serializer, "DeviceMaxDimensions");
		          xmlWriteAttribute(serializer, "XMax", Integer.toString(this.mMaxX));
                  xmlWriteAttribute(serializer, "YMax", Integer.toString(this.mMaxY));
                  xmlWriteEndTag(serializer, "DeviceMaxDimensions");
              	serializer.endTag(null, "writerDef");
	        }
	        else
	        {
		          xmlWriteText(serializer, "deviceType", "Nexus 4");
		          xmlWriteStartTag(serializer, "DeviceMaxDimensions");
		          xmlWriteAttribute(serializer, "XMax", Integer.toString(this.mMaxX));
            	  xmlWriteAttribute(serializer, "YMax", Integer.toString(this.mMaxY));
            	  xmlWriteEndTag(serializer, "DeviceMaxDimensions");
	        }
	        
	        for(int k=0; k<SGCount; k++)
	        {
	        	Log.i(TAG, "Saving SG number : "+ (k+1));
	        	Sg = SGVector.get(k);
	            xmlWriteStartTag(serializer, "strokeGroup");
	              xmlWriteStartTag(serializer, "labelDesc");
	                xmlWriteText(serializer, "desc", Sg.getName());
	              xmlWriteEndTag(serializer, "labelDesc");
	              
	              xmlWriteText(serializer, "strkCount", Integer.toString(Sg.strokesCount));
	              for(int i=1; i<=Sg.strokesCount; i++)
	              {
	            	  xmlWriteStartTag(serializer, "stroke");
	                    xmlWriteText(serializer, "strokeNumber", Integer.toString(i));
	                    xmlWriteStartTag(serializer, "hwTrace");
	                      xmlWriteText(serializer, "dimension", Integer.toString(2));
	                      stroke = Sg.strokes.get(i-1);
	                      xmlWriteText(serializer, "NoOfPoints", Integer.toString(stroke.coordinatesCount));
	                        for(int j=1; j <= stroke.coordinatesCount; j++)
	                        {
	                        	xmlWriteStartTag(serializer, "trace");
	                        	coord = stroke.coordinates.get(j-1);
	                        	xmlWriteAttribute(serializer, "x", Integer.toString(coord.getX()));
	                        	xmlWriteAttribute(serializer, "y", Integer.toString(coord.getY()));
	                            xmlWriteEndTag(serializer, "trace");                                	
	                        }
	                    xmlWriteEndTag(serializer, "hwTrace");
	                  xmlWriteEndTag(serializer, "stroke");
	            	  
	              }
	            xmlWriteEndTag(serializer, "strokeGroup");
	        }
	        serializer.endDocument();
	        //write xml data into the FileOutputStream
	        serializer.flush();
	        
        	this.mCurrSG = null;
        	this.mStroke = null;
        	this.mCoord = null;             
        }
        catch (Exception e)
        {
        	Log.e("Exception","error occurred while creating xml file");
        	e.printStackTrace();
        	return false;
        }
        //finally we close the file stream
        try 
        {
			fileos.close();
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
        return true;
	}
	
	public boolean SaveDataStat()
	{
		File newxmlfile= null;
		
		if(false == Utils.isExternalStorageWritable())
		{
			return false;
		}
		newxmlfile = new File(Utils.ROOT+"/"+"DataStat.xml");
        try{
                newxmlfile.createNewFile();
        }catch(IOException e){
                Log.e("IOException", "exception in createNewFile() method");
                return false;
        }
        //we have to bind the new file with a FileOutputStream
        FileOutputStream fileos = null;        
        try
        {
            fileos = new FileOutputStream(newxmlfile);
        }
        catch(FileNotFoundException e)
        {
            Log.e("FileNotFoundException", "can't create FileOutputStream");
            return false;
        }
        //we create a XmlSerializer in order to write xml data
        XmlSerializer serializer = Xml.newSerializer();
        
        try
        {
	        //we set the FileOutputStream as output for the serializer, using UTF-8 encoding
	        serializer.setOutput(fileos, "UTF-8");
	        //Write <?xml declaration with encoding (if encoding not null) and standalone flag (if standalone not null)
	        serializer.startDocument(null, Boolean.valueOf(true));
	        //set indentation option
	        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
	        
	        //start a tag called "root"
        	xmlWriteStartTag(serializer, "TELUGU");
        	xmlWriteAttribute(serializer, "count", Integer.toString(this.mDataCount[Utils.TELUGU]));
            xmlWriteEndTag(serializer, "TELUGU");
            
        	xmlWriteStartTag(serializer, "HINDI");
        	xmlWriteAttribute(serializer, "count", Integer.toString(this.mDataCount[Utils.HINDI]));
            xmlWriteEndTag(serializer, "HINDI"); 

        	xmlWriteStartTag(serializer, "ENGLISH");
        	xmlWriteAttribute(serializer, "count", Integer.toString(this.mDataCount[Utils.ENGLISH]));
            xmlWriteEndTag(serializer, "ENGLISH"); 

        	xmlWriteStartTag(serializer, "ENGLISH_NUMERALS");
        	xmlWriteAttribute(serializer, "count", Integer.toString(this.mDataCount[Utils.ENGLISH_NUMERALS]));
            xmlWriteEndTag(serializer, "ENGLISH_NUMERALS"); 

        	xmlWriteStartTag(serializer, "user");
        	xmlWriteAttribute(serializer, "count", Integer.toString(this.mDataCount[Utils.USER]));
            xmlWriteEndTag(serializer, "user"); 

            serializer.endDocument();
            //write xml data into the FileOutputStream
            serializer.flush();

        }
        catch (Exception e)
	    {
	    	Log.e("Exception","error occurred while creating xml file");
	    	e.printStackTrace();
	    	return false;
	    }
        //finally we close the file stream
	    try 
	    {
			fileos.close();
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	    return true;
	}
	
	public void loadDataStat(XmlPullParser myParser)
	{
		int event;
		String text;
		try
		{
			event = myParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT)
			{
				String name=myParser.getName();
				switch (event)
				{
					case XmlPullParser.START_TAG:
					{
						
					}
					break;

					case XmlPullParser.TEXT:
					{
						text = myParser.getText();
					}
					break;
					
					case XmlPullParser.END_TAG:
					{
						if(name.equals("TELUGU"))
						{
							this.mDataCount[Utils.TELUGU] = Integer.parseInt(myParser.getAttributeValue(null,"count"));
						}
						else if(name.equals("HINDI"))
						{
							this.mDataCount[Utils.HINDI] = Integer.parseInt(myParser.getAttributeValue(null,"count"));
						}
						else if(name.equals("KANADA"))
						{
							this.mDataCount[Utils.KANNADA] = Integer.parseInt(myParser.getAttributeValue(null,"count"));
						}
						else if(name.equals("ENGLISH"))
						{
							this.mDataCount[Utils.ENGLISH] = Integer.parseInt(myParser.getAttributeValue(null,"count"));
						}
						else if(name.equals("ENGLISH_NUMERALS"))
						{
							this.mDataCount[Utils.ENGLISH_NUMERALS] = Integer.parseInt(myParser.getAttributeValue(null,"count"));
						}
						else if(name.equals("user"))
						{
							this.mDataCount[Utils.USER] = Integer.parseInt(myParser.getAttributeValue(null,"count"));
						}
					}
					break;
				}		 
				event = myParser.next(); 			
			}
		}
		catch (Exception e)
		{
		     e.printStackTrace();
		}
		
	}
	

}
