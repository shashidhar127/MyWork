package com.project.ohwr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ClassInfo extends Activity {

	static private final String TAG = "OHWR";

	private Button mokButton;
	private EditText mclassName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG,"Creating ClassInfo activity");
		setContentView(R.layout.class_info);
		Log.i(TAG,"ClassInfo activity created");

		// Get a reference to the EditText field
		mclassName = (EditText) findViewById(R.id.class_name);
		
		// setup "ok" button
		mokButton = (Button) findViewById(R.id.ok_button);
		mokButton.setOnClickListener(new OnClickListener() {

			// Call enterClicked() when pressed

			@Override
			public void onClick(View v) {

				okClicked();
			
			}
		});
	}


	// Sets result to send back to calling Activity and finishes
	
	private void okClicked() {
		
		String text = null;
        Intent classInfoIntent = new Intent();
		Log.i(TAG,"ok enterClicked()");
				
		text = mclassName.getText().toString();
		Log.i(TAG,"Class name Entered : "+text);
		classInfoIntent.putExtra("com.project.ohwr.class_name", text);
			
		setResult(RESULT_OK, classInfoIntent);
		finish();
	}
}

