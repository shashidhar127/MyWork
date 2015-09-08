package com.project.ohwr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class GetMode extends Activity implements OnClickListener{

	static private final String TAG = "OHWR";
	static private boolean collectDataFlag = true;
	static private final boolean ONLY_DATA_COLLECTION = false;
	private Button addClass, collectData, mEnglish, mTelugu, mHindi, mEnglishNumerals, mKannada;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG,"Creating GetMode activity");
		setContentView(R.layout.get_mode);
		Log.i(TAG,"GetMode activity created");
        
		// setup "addClass" button
		addClass = (Button) findViewById(R.id.class_button);
		addClass.setOnClickListener(this);
		if(true == ONLY_DATA_COLLECTION)
			this.addClass.setVisibility(View.INVISIBLE);
		else
			this.addClass.setVisibility(View.VISIBLE);
		// setup "collectData" button
		collectData = (Button) findViewById(R.id.data_button);
		collectData.setOnClickListener(this);
	}

	// Sets result to send back to calling Activity and finishes
	@Override
	public void onClick(View view)
	{
		int viewId = view.getId(); 
		Intent modeInfoIntent = new Intent();
	    //respond to clicks     
		switch (view.getId()) 
		{	
			case R.id.class_button :
			{
				Log.i(TAG, "class_button view in GetMode activity : \n"+viewId);
				collectDataFlag = false;
				selectLanguage();
				break;
			}
			case R.id.data_button :
			{
				Log.i(TAG, "data_button view in GetMode activity : \n"+viewId);
				collectDataFlag = true;
				selectLanguage();
				break;
			}
			case R.id.telugu_button :
			{
				modeInfoIntent.putExtra("com.project.ohwr.language", Utils.TELUGU);
				modeInfoIntent.putExtra("com.project.ohwr.modeinfo", collectDataFlag);
				setResult(RESULT_OK, modeInfoIntent);
				finish();
				break;
			}
			case R.id.hindi_button :
			{
				modeInfoIntent.putExtra("com.project.ohwr.language", Utils.HINDI);
				modeInfoIntent.putExtra("com.project.ohwr.modeinfo", collectDataFlag);
				setResult(RESULT_OK, modeInfoIntent);
				finish();				
				break;
			}
			case R.id.english_button :
			{
				modeInfoIntent.putExtra("com.project.ohwr.language", Utils.ENGLISH);
				modeInfoIntent.putExtra("com.project.ohwr.modeinfo", collectDataFlag);
				setResult(RESULT_OK, modeInfoIntent);
				finish();
				break;
			}
			case R.id.english_numerals_button :
			{
				modeInfoIntent.putExtra("com.project.ohwr.language", Utils.ENGLISH_NUMERALS);
				modeInfoIntent.putExtra("com.project.ohwr.modeinfo", collectDataFlag);
				setResult(RESULT_OK, modeInfoIntent);
				finish();
				break;
			}
			case R.id.kannada_button :
			{
				modeInfoIntent.putExtra("com.project.ohwr.language", Utils.KANNADA);
				modeInfoIntent.putExtra("com.project.ohwr.modeinfo", collectDataFlag);
				setResult(RESULT_OK, modeInfoIntent);
				finish();
				break;
			}
			default :
			{
				Log.i(TAG, "Unknown view in GetMode activity : \n"+viewId);
			}
		}
		
	}
	
	void selectLanguage()
	{
		setContentView(R.layout.get_language);
		Log.i(TAG,"GetMode activity created");

		// setup "English" button
		this.mEnglish = (Button) findViewById(R.id.english_button);
		this.mEnglish.setOnClickListener(this);

		// setup "Telugu" button
		this.mTelugu = (Button) findViewById(R.id.telugu_button);
		this.mTelugu.setOnClickListener(this);

		// setup Hindi button
		this.mHindi = (Button) findViewById(R.id.hindi_button);
		this.mHindi.setOnClickListener(this);

		// setup English numerals button
		this.mEnglishNumerals = (Button) findViewById(R.id.english_numerals_button);
		this.mEnglishNumerals.setOnClickListener(this);

		// setup Kannada button
		this.mKannada = (Button) findViewById(R.id.kannada_button);
		this.mKannada.setOnClickListener(this);
	}
}

