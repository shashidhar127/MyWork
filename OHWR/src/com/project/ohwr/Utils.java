package com.project.ohwr;

import java.io.File;

import android.os.Environment;

/**
 * @author jshashi
 *
 */
public class Utils {
    
	public static String ROOT = Environment.getExternalStorageDirectory()+"/OHWR";
	
	public static String TELUGU_ROOT = ROOT + "/TELUGU";
	public static String HINDI_ROOT = ROOT + "/HINDI";
	public static String ENGLISH_NUMERALS_ROOT = ROOT + "/ENGLISH_NUMERALS";
	public static String KANNADA_ROOT = ROOT + "/KANNADA";
	public static String ENGLISH_ROOT = ROOT + "/ENGLISH";

	public static String ROOT_DIR[] = {ROOT, TELUGU_ROOT, HINDI_ROOT, ENGLISH_NUMERALS_ROOT, KANNADA_ROOT, ENGLISH_ROOT};
	static public final String LANGUAGES[] = {"user", "TELUGU", "HINDI", "ENGLISH_NUMERALS", "KANNADA", "ENGLISH"};
	
	static public final int NUM_STROKE_GRPOUPS = 250;
	static public final int STROKE_GROUP_INCREAMENT = 50;
	static public final short USER = 0;
	static public final short TELUGU = 1;
	static public final short HINDI = 2;
	static public final short ENGLISH_NUMERALS = 3;
	static public final short KANNADA = 4;
	static public final short ENGLISH = 5;
	
	static public final int CLASS = 1;
	static public final int DATA = 2;
	
	static public final int CURRENT_CLASS = 1;
	static public final int PREVIOUS_CLASS = 2;
	static public final int NEXT_CLASS = 3;
	static public final int CURRENT_DATA = 4;
	static public final int PREVIOUS_DATA = 5;
	static public final int NEXT_DATA = 6;

	static public final int CURRENT_SG_CLASS = 7;
	static public final int CURRENT_SG_DATA = 8;

	
	public static short MAX(short a, short b)
	{
		return a >= b ? a : b;
	}
	public static int MAX(int a, int b)
	{
		return a >= b ? a : b;
	}
	public static float MAX(float a, float b)
	{
		return a >= b ? a : b;
	}
	
	public static short MIN(short a, short b)
	{
		return a <= b ? a : b;
	}
	public static int MIN(int a, int b)
	{
		return a <= b ? a : b;
	}
	public static float MIN(float a, float b)
	{
		return a <= b ? a : b;
	}
	
	public static void CheckDir(short language)
	{
		File rootPath;
		String Dname = Utils.ROOT_DIR[language];
		
		if(isExternalStorageWritable())
		{
			rootPath = new File(Dname);
			if(!rootPath.exists())
			{
				rootPath.mkdirs();
			}
		}
		return;
	}
	
	/* Checks if external storage is available for read and write */
	public static boolean isExternalStorageWritable()
	{
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state))
		{
		    return true;
		}
		return false;
	}

	/* Checks if external storage is available to at least read */
	public static boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
}
