package com.project.ohwr;


import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;


import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	
	static private final String TAG = "OHWR";
	static private final int GET_USER_INFO_REQUEST_CODE = 1;
	static private final int GET_VOWEL_INFO_REQUEST_CODE = 2;
	static private final int GET_MODE_INFO_REQUEST_CODE = 3;
	static private final int GET_CLASS_INFO_REQUEST_CODE = 4;
	static private final int DISPLAY_ON = 1;
	static private final int DISPLAY_OFF = 2;
	
	static private final boolean mNotNeeded = false;
	
	
	private int mDrawingStatus = DISPLAY_OFF;
	private DrawingView mDrawView;
	private ImageButton mCurrPaint, mDrawBtn, mEraseBtn, mNewBtn, mSaveBtn;
	private Button mClearBtn, mCharBtn, mOkBtn, mPrevBtn, mCurrBtn, mNextBtn, mEditBtn, mEditOkBtn, mClassBtn;
	private TextView mClassBox;
	private EditText mEditName;
	
	private float mSmallBrush, mMediumBrush, mLargeBrush;
	private boolean mCollectDataFlag = false;
	private boolean mEditClassFlag = true;
	private String mClassName;
	private short mLanguage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mDrawView = (DrawingView)findViewById(R.id.drawing);
				
		mSmallBrush = getResources().getInteger(R.integer.small_size);
		mMediumBrush = getResources().getInteger(R.integer.medium_size);
		mLargeBrush = getResources().getInteger(R.integer.large_size);
		
		mDrawView.setBrushSize(mSmallBrush);

if(mNotNeeded)		
{
//		LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
//		mCurrPaint = (ImageButton)paintLayout.getChildAt(0);
		mCurrPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
		
//		mDrawBtn = (ImageButton)findViewById(R.id.draw_btn);
		mDrawBtn.setOnClickListener(this);

//		mEraseBtn = (ImageButton)findViewById(R.id.erase_btn);
		mEraseBtn.setOnClickListener(this);
		
//		this.mCharBtn = (Button) findViewById(R.id.select_character_button);
		this.mCharBtn.setOnClickListener(this);
}		
		this.mNewBtn = (ImageButton)findViewById(R.id.new_btn);
		this.mNewBtn.setOnClickListener(this);
		
		this.mSaveBtn = (ImageButton)findViewById(R.id.save_btn);
		this.mSaveBtn.setOnClickListener(this);
		
		this.mClearBtn = (Button) findViewById(R.id.clear_button);
		this.mClearBtn.setOnClickListener(this);
				
		this.mOkBtn = (Button) findViewById(R.id.ok_button);
		this.mOkBtn.setOnClickListener(this);
		
		this.mPrevBtn = (Button) findViewById(R.id.prev_button_class);
		this.mPrevBtn.setOnClickListener(this);
				
		this.mCurrBtn = (Button) findViewById(R.id.curr_button_class);
		this.mCurrBtn.setOnClickListener(this);
		
		this.mNextBtn = (Button) findViewById(R.id.next_button_class);
		this.mNextBtn.setOnClickListener(this);
		
		this.mClassBox = (TextView) findViewById(R.id.text);
		mClassBox.setText("Class name");
		
		this.mEditBtn = (Button) findViewById(R.id.edit_button);
		this.mEditBtn.setOnClickListener(this);
		this.mEditBtn.setVisibility(View.INVISIBLE);
		
		this.mEditName = (EditText) findViewById(R.id.edit_name);
		this.mEditName.setVisibility(View.INVISIBLE);
		
		this.mEditOkBtn = (Button) findViewById(R.id.editOk_button);
		this.mEditOkBtn.setOnClickListener(this);
		this.mEditOkBtn.setVisibility(View.INVISIBLE);
		
		this.mClassBtn = (Button) findViewById(R.id.class_button);
		this.mClassBtn.setOnClickListener(this);
		this.mClassBtn.setVisibility(View.VISIBLE);
		
			
	    Display mdisp = getWindowManager().getDefaultDisplay();
	    Point mdispSize = new Point();
	    mdisp.getSize(mdispSize);
	    int maxX = mdispSize.x; 
	    int maxY = mdispSize.y;
	    
	    this.mDrawView.setMaxSize(maxX, maxY);
	    
		
		Log.i(TAG, "Launching user info activity : \n");
		
		getModeInfo();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onClick(View view)
	{
		int viewId = view.getId(); 
	    //respond to clicks     
		switch (view.getId()) 
		{		
/*
		case R.id.draw_btn :
			{
	     	    //draw button clicked
				final Dialog brushDialog = new Dialog(this);
				brushDialog.setTitle("Brush size:");
				
				brushDialog.setContentView(R.layout.brush_chooser);
				
				ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
				smallBtn.setOnClickListener(new OnClickListener(){
				    @Override
				    public void onClick(View v) {
				        drawView.setBrushSize(smallBrush);
				        drawView.setLastBrushSize(smallBrush);
				        drawView.setErase(false);
				        brushDialog.dismiss();
				    }
				});
				
				ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
				mediumBtn.setOnClickListener(new OnClickListener(){
				    @Override
				    public void onClick(View v) {
				        drawView.setBrushSize(mediumBrush);
				        drawView.setLastBrushSize(mediumBrush);
				        drawView.setErase(false);
				        brushDialog.dismiss();
				    }
				});
				 
				ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
				largeBtn.setOnClickListener(new OnClickListener(){
				    @Override
				    public void onClick(View v) {
				        drawView.setBrushSize(largeBrush);
				        drawView.setLastBrushSize(largeBrush);
				        drawView.setErase(false);
				        brushDialog.dismiss();
				    }
				});
				
				brushDialog.show();
	
			}
			break;
*/			
/*		
			case R.id.erase_btn :
			{
			    //switch to erase - choose size
				final Dialog brushDialog = new Dialog(this);
				brushDialog.setTitle("Eraser size:");
				brushDialog.setContentView(R.layout.brush_chooser);
				
				ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
				smallBtn.setOnClickListener(new OnClickListener(){
				    @Override
				    public void onClick(View v) {
				        drawView.setErase(true);
				        drawView.setBrushSize(smallBrush);
				        brushDialog.dismiss();
				    }
				});
				ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
				mediumBtn.setOnClickListener(new OnClickListener(){
				    @Override
				    public void onClick(View v) {
				        drawView.setErase(true);
				        drawView.setBrushSize(mediumBrush);
				        brushDialog.dismiss();
				    }
				});
				ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
				largeBtn.setOnClickListener(new OnClickListener(){
				    @Override
				    public void onClick(View v) {
				        drawView.setErase(true);
				        drawView.setBrushSize(largeBrush);
				        brushDialog.dismiss();
				    }
				});
				
				brushDialog.show();
			}
			break;
*/
			case R.id.new_btn :
			{
			    //new button
				AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
				if(true == mCollectDataFlag)
				{
			        newDialog.setTitle("New data");
					newDialog.setMessage("Collect data from new user");
				}
				else
				{
					newDialog.setTitle("New class");
					newDialog.setMessage("Add new class ");					
				}
				newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
				    public void onClick(DialogInterface dialog, int which){
				        mDrawView.startNew();
				        dialog.dismiss();
				        if(true == mCollectDataFlag)
				        	collectUserInformation();
				        else
				        	collectClassInformation();
				    }
				});
				newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
				    public void onClick(DialogInterface dialog, int which){
				        dialog.cancel();
				    }
				});
				newDialog.show();
			}
			break;
			case R.id.save_btn :
	        {
	            //save drawing
				AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
				saveDialog.setTitle("Save data");
				saveDialog.setMessage("Save data to device ?");
				saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
				    public void onClick(DialogInterface dialog, int which){
				        //save drawing
				    	//mDrawView.saveCurrSG();
				    	if( true == mDrawView.saveSG())
				    	{
				    		
					    	mDrawView.reInit(mCollectDataFlag, mLanguage);				    		
				    	}
                        
//				    	saveClassImage();
				    	
				    	mDrawView.setDrawingCacheEnabled(true);
if(mNotNeeded)
{
						String imgSaved = MediaStore.Images.Media.insertImage(
							    getContentResolver(), mDrawView.getDrawingCache(),
							    UUID.randomUUID().toString()+".png", "drawing");
						if(imgSaved!=null){
						    Toast savedToast = Toast.makeText(getApplicationContext(), 
						        "Data saved to device!", Toast.LENGTH_SHORT);
						    savedToast.show();
						}
						else{
						    Toast unsavedToast = Toast.makeText(getApplicationContext(), 
						        "Oops! Data could not be saved.", Toast.LENGTH_SHORT);
						    unsavedToast.show();
						}
}
				    	mDrawView.destroyDrawingCache();
				    	mDrawView.clear();
				    }
				});
				saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
				    public void onClick(DialogInterface dialog, int which){
				        dialog.cancel();
				    }
				});
				saveDialog.show();
	        }
	        break;
			case R.id.clear_button :
			{
				// Clear screen
			    //new button
				AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
				newDialog.setTitle("Clear writing");
				newDialog.setMessage("Clear writing (you will lose the current writing)?");
				newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
				    public void onClick(DialogInterface dialog, int which){
				        mDrawView.clear();
				        dialog.dismiss();
				    }
				});
				newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
				    public void onClick(DialogInterface dialog, int which){
				        dialog.cancel();
				    }
				});
				//newDialog.show();
		        mDrawView.clear();
			}
			break;
/*
			case R.id.select_character_button :
			{
			    //switch to Choosing vowel/consonants
				final Dialog charDialog = new Dialog(this);
				charDialog.setTitle("Chose character type");
				charDialog.setContentView(R.layout.select_character_type);
				
				selectCharacterType(charDialog);
				
                charDialog.show();

			}
			break;
*/
			case R.id.ok_button :
			{
				if(MainActivity.DISPLAY_OFF == this.mDrawingStatus)
				{
					mDrawView.saveCurrSG();					
					mDrawView.clear();
					
					if(true == this.mCollectDataFlag)
					{
						if(0 == mDrawView.getSGCount(Utils.DATA))
						{
							mDrawView.setDrawing(false);
						}
						else
						{
							
						}
						mDrawView.setDrawing(true);
						this.mPrevBtn.setVisibility(View.VISIBLE);
						this.mNextBtn.setVisibility(View.VISIBLE);
					}
					else
					{
						mDrawView.setDrawing(false);
					}
				}
				else if(MainActivity.DISPLAY_ON == this.mDrawingStatus)
				{
					this.mEditBtn.setVisibility(View.INVISIBLE);
					this.mDrawingStatus = MainActivity.DISPLAY_OFF;
					if(true == this.mCollectDataFlag)
					{
						mDrawView.setDrawing(false);
						if(true == mDrawView.displaySG(Utils.CURRENT_SG_DATA))
						{
							mClassBox.setText(mDrawView.getDisplaySGName());
						}
						mDrawView.setDrawing(true);
						this.mPrevBtn.setVisibility(View.INVISIBLE);
						this.mNextBtn.setVisibility(View.INVISIBLE);
//						mDrawView.setDrawing(true);
//						mDrawView.clear();
					}
					else
					{
						mDrawView.setDrawing(false);
						if(true == mDrawView.displaySG(Utils.CURRENT_SG_CLASS))
						{
							mDrawView.setDrawing(true);
							mClassBox.setText(mDrawView.getDisplaySGName());
						}
					}
				}
			}
			break;
			case R.id.prev_button_class :
			{
				if(true == mDrawView.displaySG(Utils.PREVIOUS_CLASS))
				{
					this.mDrawingStatus = MainActivity.DISPLAY_ON;
					mClassBox.setText(mDrawView.getDisplaySGName());
					if(true == this.mCollectDataFlag)
						this.mEditBtn.setVisibility(View.INVISIBLE);
					else
						this.mEditBtn.setVisibility(View.VISIBLE);
				}
				else
				{
					this.mDrawingStatus = MainActivity.DISPLAY_OFF;
					mClassBox.setText("Class name");
				}
				mDrawView.setDrawing(false);
			}
			break;
			case R.id.next_button_class :
			{
				if( true == mDrawView.displaySG(Utils.NEXT_CLASS))
				{
					this.mDrawingStatus = MainActivity.DISPLAY_ON;
					mClassBox.setText(mDrawView.getDisplaySGName());
					if(true == this.mCollectDataFlag)
						this.mEditBtn.setVisibility(View.INVISIBLE);
					else
						this.mEditBtn.setVisibility(View.VISIBLE);
				}
				else
				{
					this.mDrawingStatus = MainActivity.DISPLAY_OFF;
					mClassBox.setText("Class name");
				}
				mDrawView.setDrawing(false);
			}
			break;
			case R.id.curr_button_class :
			{
				if( true == mDrawView.displaySG(Utils.CURRENT_CLASS))
				{
					this.mDrawingStatus = MainActivity.DISPLAY_ON;
					mClassBox.setText(mDrawView.getDisplaySGName());
					if(true == this.mCollectDataFlag)
						this.mEditBtn.setVisibility(View.INVISIBLE);
					else
						this.mEditBtn.setVisibility(View.VISIBLE);
				}
				else
				{
					this.mDrawingStatus = MainActivity.DISPLAY_OFF;
					mClassBox.setText("Class name");
				}
				mDrawView.setDrawing(false);
			}
			break;
			case R.id.prev_button_data :
			{
				if(true == mDrawView.displaySG(Utils.PREVIOUS_DATA))
				{
					this.mDrawingStatus = MainActivity.DISPLAY_ON;
					mClassBox.setText(mDrawView.getDisplaySGName());
					this.mEditBtn.setVisibility(View.VISIBLE);
				}
				else
				{
					this.mDrawingStatus = MainActivity.DISPLAY_OFF;
					mClassBox.setText("Class name");
				}
				mDrawView.setDrawing(false);
			}
			break;
			case R.id.next_button_data :
			{
				if( true == mDrawView.displaySG(Utils.NEXT_DATA))
				{
					this.mDrawingStatus = MainActivity.DISPLAY_ON;
					mClassBox.setText(mDrawView.getDisplaySGName());
					this.mEditBtn.setVisibility(View.VISIBLE);
				}
				else
				{
					this.mDrawingStatus = MainActivity.DISPLAY_OFF;
					mClassBox.setText("Class name");
				}
				mDrawView.setDrawing(false);
			}
			break;
			case R.id.curr_button_data :
			{
				if( true == mDrawView.displaySG(Utils.CURRENT_DATA))
				{
					this.mDrawingStatus = MainActivity.DISPLAY_ON;
					mClassBox.setText(mDrawView.getDisplaySGName());
					this.mEditBtn.setVisibility(View.VISIBLE);
				}
				else
				{
					this.mDrawingStatus = MainActivity.DISPLAY_OFF;
					mClassBox.setText("Class name");
				}
				mDrawView.setDrawing(false);
			}
			break;

			
			
			case R.id.edit_button :
			{
				this.mEditName.setVisibility(View.VISIBLE);
				this.mEditOkBtn.setVisibility(View.VISIBLE);
				
				this.mEditBtn.setVisibility(View.INVISIBLE);
				
				this.mNewBtn.setVisibility(View.INVISIBLE);
				this.mSaveBtn.setVisibility(View.INVISIBLE);
				this.mPrevBtn.setVisibility(View.INVISIBLE);
				this.mCurrBtn.setVisibility(View.INVISIBLE);
				this.mNextBtn.setVisibility(View.INVISIBLE);
				this.mOkBtn.setVisibility(View.INVISIBLE);
				this.mClassBtn.setVisibility(View.INVISIBLE);
				
				mDrawView.clear();
				mDrawView.editSGInit();
			}
			break;
			case R.id.editOk_button :
			{
				String text;
				text = mEditName.getText().toString();
				text.trim();
				if(true == text.isEmpty())
					text = null;
				
				mDrawView.editSG(text);
				mDrawView.clear();
				mDrawView.setDrawing(false);
				
				this.mEditName.setVisibility(View.INVISIBLE);
				this.mEditOkBtn.setVisibility(View.INVISIBLE);
				this.mEditBtn.setVisibility(View.INVISIBLE);
				
				this.mNewBtn.setVisibility(View.VISIBLE);
				this.mSaveBtn.setVisibility(View.VISIBLE);
				this.mPrevBtn.setVisibility(View.VISIBLE);
				this.mCurrBtn.setVisibility(View.VISIBLE);
				this.mNextBtn.setVisibility(View.VISIBLE);
				this.mOkBtn.setVisibility(View.VISIBLE);
				this.mClassBtn.setVisibility(View.VISIBLE);
			}
			break;
			case R.id.class_button :
			{
				if(true == this.mCollectDataFlag)
				{
					if(true == this.mEditClassFlag)
					{
						this.mClassBtn.setText(R.string.data_symbol);
						this.mEditClassFlag = false;
						this.mCurrBtn.setId(R.id.curr_button_data);
						this.mPrevBtn.setId(R.id.prev_button_data);
						this.mNextBtn.setId(R.id.next_button_data);
 					}
					else
					{
						this.mClassBtn.setText(R.string.class_symbol);
						this.mEditClassFlag = true;						
						this.mCurrBtn.setId(R.id.curr_button_class);
						this.mPrevBtn.setId(R.id.prev_button_class);
						this.mNextBtn.setId(R.id.next_button_class);
						this.mEditBtn.setVisibility(View.INVISIBLE);
					}
					mDrawView.setDrawing(false);
					mDrawView.clear();
				}
			}
			break;
			default :
			{
				Log.i(TAG, "Unknown view in main activity : \n"+viewId);
			}
		}
	}
	
	private void selectCharacterType(final Dialog charDialog)
	{
		// Declare and setup "Vowel" button
		Button vowelButton = (Button) charDialog.findViewById(R.id.vowel_button);
		vowelButton.setOnClickListener(new OnClickListener() {

			// Call enterClicked() when pressed

			@Override
			public void onClick(View v) {

				vowelClicked();
				charDialog.dismiss();
			
			}
		});
		
		// Declare and setup "Vowel" button
		Button consonantButton = (Button) charDialog.findViewById(R.id.consonant_button);
		consonantButton.setOnClickListener(new OnClickListener() {

			// Call enterClicked() when pressed

			@Override
			public void onClick(View v) {

				vowelClicked();
				charDialog.dismiss();
			
			}
		});
	}
	
	public void vowelClicked()
	{
		Log.i(TAG,"Launching new activity to collect vowel");
		
		Intent vowelChooserIntent = new Intent(this, VowelChooser.class);
		Log.i(TAG,"created vowelChooserIntent");
        startActivityForResult(vowelChooserIntent, GET_VOWEL_INFO_REQUEST_CODE);
		Log.i(TAG,"Launched new activity to collect vowel");
	}

	public void consonantClicked()
	{
		Log.i(TAG,"Launching new activity to collect vowel");
		
		Intent consonantChooserIntent = new Intent(this, ConsonantChooser.class);
		Log.i(TAG,"created vowelChooserIntent");
        startActivityForResult(consonantChooserIntent, GET_VOWEL_INFO_REQUEST_CODE);
		Log.i(TAG,"Launched new activity to collect vowel");
	}	
	
	public void paintClicked(View view){
	    //use chosen color
		mDrawView.setErase(false);
		mDrawView.setBrushSize(mDrawView.getLastBrushSize());
		
		if(view!=mCurrPaint){
			//update color
			ImageButton imgView = (ImageButton)view;
			String color = view.getTag().toString();
			
			mDrawView.setColor(color);
			
			imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
			mCurrPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
			mCurrPaint=(ImageButton)view;
		}
	}
	
	private void collectUserInformation()
	{
		Log.i(TAG,"Launching new activity to collect writer data");
		
		Intent explicitIntent = new Intent(this, UserInfo.class);
		
        startActivityForResult(explicitIntent, GET_USER_INFO_REQUEST_CODE);
		
		// Create a new intent to launch the ExplicitlyLoadedActivity class
		
		// Start an Activity using that intent and the request code defined above
	}
	
	private void collectClassInformation()
	{
		Log.i(TAG,"Launching new activity to collect class information");
		
		Intent explicitIntent = new Intent(this, ClassInfo.class);
        startActivityForResult(explicitIntent, GET_CLASS_INFO_REQUEST_CODE);
        Log.i(TAG,"Launched new activity to collect class information");
	}
	
	private void getModeInfo()
	{
		Log.i(TAG,"Launching new activity to find the mode of collection");
		
		Intent explicitIntent = new Intent(this, GetMode.class);
        startActivityForResult(explicitIntent, GET_MODE_INFO_REQUEST_CODE);
		
		// Create a new intent to launch the ExplicitlyLoadedActivity class
		
		// Start an Activity using that intent and the request code defined above
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent expIntent) 
	{
		Log.i(TAG, "Entered onActivityResult()");
		
		// Process the result only if this method received both a
		// RESULT_OK result code and a recognized request code
		// If so, update the Textview showing the user-entered text.
		if(GET_USER_INFO_REQUEST_CODE == requestCode && RESULT_OK == resultCode)
		{
			Log.i(TAG, "Got the user info");
			mDrawView.mPutName(expIntent.getStringExtra("com.project.ohwr.name"));
			mDrawView.mPutAge(expIntent.getStringExtra("com.project.ohwr.age"));
			mDrawView.mPutGender(expIntent.getStringExtra("com.project.ohwr.gender"));
			mDrawView.mPutRegion(expIntent.getStringExtra("com.project.ohwr.region"));
		}
		else if(GET_VOWEL_INFO_REQUEST_CODE == requestCode && RESULT_OK == resultCode)
		{
			Log.i(TAG, "VOWEL INFO");
			mDrawView.setDrawing(true);
		}
		else if(GET_MODE_INFO_REQUEST_CODE == requestCode && RESULT_OK == resultCode)
		{
			this.mCollectDataFlag = expIntent.getBooleanExtra("com.project.ohwr.modeinfo", true);
			this.mLanguage = expIntent.getShortExtra("com.project.ohwr.language", Utils.TELUGU);
			Log.i(TAG, "Got the mode info"+"collect data : "+this.mCollectDataFlag + "language : " + this.mLanguage);
			Utils.CheckDir(this.mLanguage);
			if(true == mCollectDataFlag)
			{
				collectUserInformation();
			}
			mDrawView.intializeMode(this.mCollectDataFlag, this.mLanguage);
		}
		else if(GET_CLASS_INFO_REQUEST_CODE == requestCode && RESULT_OK == resultCode)
		{
            mClassName = expIntent.getStringExtra("com.project.ohwr.class_name");
            mClassName.trim();
            if(0 != mClassName.length())
            {           	
            	if(true == mDrawView.checkCurrSGName(mClassName))
            	{
            		mDrawView.setCurrSGName(mClassName);
            		mDrawView.setDrawing(true);
            		mClassBox.setText(mClassName);
            	}
            	else
            	{
            		mDrawView.setDrawing(false);
            		mClassBox.setText("Class name");
    			    //Alert button
    				AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
    				alertDialog.setTitle("Error :");
    				alertDialog.setMessage(mClassName + " is already allocated ");
    				alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){
    				    public void onClick(DialogInterface dialog, int which){
    				        mDrawView.startNew();
    				        dialog.dismiss();
    				    }
    				});
    				alertDialog.show();
            	}
            }
            else
            {
            	Log.i(TAG, "ERROR : Null class name recieved");
            	// TODO - Put an error box here
            }
		}
	}

/*	
	void saveClassImage()
	{
		int i;
		String name;
		Bitmap bitmap;
		File file;
		boolean result;
		
		for(i=0 ; i < mDrawView.mClassSGCount; i++)
		{

			mDrawView.dispSG(mDrawView.mClassSGs.get(i));
			mDrawView.setDrawingCacheEnabled(true);
			name = mDrawView.mClassSGs.get(i).getName();
			bitmap = mDrawView.getDrawingCache();

			String dir_name = Utils.ROOT_DIR[this.mLanguage]+"/CLASS";
			File dir = new File(dir_name);
			if(!dir.exists())
			{
				dir.mkdirs();
			}
			
			file = new File(dir, name+".jpg");
	        try{
	        	file.createNewFile();
	        }catch(IOException e){
	                Log.e("IOException", "exception in createNewFile() method");
	                
	        }
	        //we have to bind the new file with a FileOutputStream
	        FileOutputStream fileos = null;        
	        try
	        {
	            fileos = new FileOutputStream(file);
	        }
	        catch(FileNotFoundException e)
	        {
	            Log.e("FileNotFoundException", "can't create FileOutputStream");
	            
	        }

	        result = bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fileos);
	        
			//String imgSaved = MediaStore.Images.Media.insertImage(
			//		this.getContentResolver(), mDrawView.getDrawingCache(),
			//		name+".png", "drawing");
			Log.i(TAG, "Class name : " + name + "  :  " + result);
			if(result!=false){

			}
			else{
			    Toast unsavedToast = Toast.makeText(this.getApplicationContext(), 
			        "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
			    unsavedToast.show();
			}
	    	mDrawView.destroyDrawingCache();
	    	mDrawView.clear();

			
		}
	}
*/	
}
