package com.project.ohwr;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ImageView;

public class VowelChooser extends Activity implements OnClickListener {

	static private final String TAG = "OHWR";
	private int vowelChoosen = 0;
	
	private ImageButton drawBtn, eraseBtn, newBtn, saveBtn, charBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.i(TAG, "Launching vowel chooser activity : \n");
		
		setContentView(R.layout.vowel_chooser);

				
		drawBtn = (ImageButton)findViewById(R.id.draw_btn1);
		drawBtn.setOnClickListener(this);

		eraseBtn = (ImageButton)findViewById(R.id.erase_btn1);
		eraseBtn.setOnClickListener(this);
		
		newBtn = (ImageButton)findViewById(R.id.new_btn1);
		newBtn.setOnClickListener(this);
		
		saveBtn = (ImageButton)findViewById(R.id.save_btn1);
		saveBtn.setOnClickListener(this);
		
		charBtn = (ImageButton) findViewById(R.id.character_btn1);
		charBtn.setOnClickListener(this);

		
	}
	
	
	@Override
	public void onClick(View view)
	{
		int viewId = view.getId(); 
		vowelChoosen = 0;
		String text = null;

		Log.i(TAG,"selected a character");
		
		//respond to clicks     
		switch (view.getId()) 
		{
			case R.id.draw_btn1 :
			case R.id.draw_btn2 :
			{
				vowelChoosen = 1;
			}
			break;
			case R.id.erase_btn1 :
			case R.id.erase_btn2 :
			{
				vowelChoosen = 2;
			}
			break;
			case R.id.new_btn1 :
			case R.id.new_btn2 :
			{
				vowelChoosen = 3;
			}
			case R.id.save_btn1 :
			case R.id.save_btn2 :
			{
				vowelChoosen = 4;
			}
		}
		if(0 != vowelChoosen)
		{
			returnVowel(vowelChoosen);
		}
	}
	// Sets result to send back to calling Activity and finishes
	
	private void returnVowel(int vowel) 
	{
        
		Log.i(TAG,"Going to display character");
		
		setContentView(R.layout.display_character);
		
		Log.i(TAG,"Change the view");

		ImageView character_pic = (ImageView)findViewById(R.id.char_Photo);
		
		character_pic.setImageResource(R.drawable.eraser);
		
		
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
	public void enterClicked()
	{
		Intent VowelInfoIntent = new Intent();
		VowelInfoIntent.putExtra("com.project.ohwr.vowel", 1);
		
		setResult(RESULT_OK, VowelInfoIntent);
		finish();
		Log.i(TAG,"Activity finished");		
	}
}
