package com.project.ohwr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class UserInfo extends Activity {

	static private final String TAG = "OHWR";

	private EditText mName, mAge, mGender, mRegion;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG,"Creating UserInfo activity");
		setContentView(R.layout.user_info);
		Log.i(TAG,"UserInfo activity created");

		// Get a reference to the EditText field
		mName = (EditText) findViewById(R.id.name);
		mAge = (EditText) findViewById(R.id.age);
		mGender = (EditText) findViewById(R.id.gender);
		mRegion = (EditText) findViewById(R.id.region);

		// Declare and setup "Enter" button
		Button enterButton = (Button) findViewById(R.id.enter_button);
		enterButton.setOnClickListener(new OnClickListener() {

			// Call enterClicked() when pressed

			@Override
			public void onClick(View v) {

				enterClicked();
			
			}
		});

	}

	// Sets result to send back to calling Activity and finishes
	
	private void enterClicked() {
		

		String text = null;
        Intent userInfoIntent = new Intent();
		Log.i(TAG,"Entered enterClicked()");
		
		// TODO - Save user provided input from the EditText field

		// TODO - Create a new intent and save the input from the EditText field as an extra
		
		// TODO - Set Activity's result with result code RESULT_OK
		
		// TODO - Finish the Activity
		
		text = mName.getText().toString();
		Log.i(TAG,"Text Entered : "+text);
		userInfoIntent.putExtra("com.project.ohwr.name", text);
		
		text = mAge.getText().toString();
		Log.i(TAG,"Text Entered : "+text);
		userInfoIntent.putExtra("com.project.ohwr.age", text);
		
		text = mGender.getText().toString();
		Log.i(TAG,"Text Entered : "+text);
		userInfoIntent.putExtra("com.project.ohwr.gender", text);
		
		text = mRegion.getText().toString();
		Log.i(TAG,"Text Entered : "+text);
		userInfoIntent.putExtra("com.project.ohwr.region", text);
		
		setResult(RESULT_OK, userInfoIntent);
		finish();

	}
}
