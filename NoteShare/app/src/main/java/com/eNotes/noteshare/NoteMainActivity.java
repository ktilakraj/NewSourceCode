package com.eNotes.noteshare;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.eNotes.Drawing.NewDrawingActivity;
import com.eNotes.Utlity.CameraUtill;
import com.eNotes.adpters.NotesListAdapter;
import com.eNotes.dataAccess.DataManager;
import com.eNotes.datamodels.NOTETYPE;
import com.eNotes.datamodels.NoteListDataModel;
import com.eNotes.notesharedatabase.DBNoteItemElement;
import com.eNotes.notesharedatabase.DBNoteItems;
import com.eNotes.notesharedatabase.NoteshareDatabaseHelper;
import com.mobiapp.ventures.eNotes.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


//import android.support.annotation.Keep;

public class NoteMainActivity extends DrawerActivity implements OnClickListener  {

	public ImageButton imageButtonHamburg, imageButtoncalander,
			imageButtonsquence;

	public ImageButton imageButtonTextMode, imageButtonImageMode,
			imageButtonPaintMode, imageButtonAudioMode, imageButtonShareMode,
			imageButtonMoreMode;
	MediaPlayer mediaPlayer;// = new MediaPlayer();
	public TextView textViewheaderTitle, progressRecordtext;
	public RelativeLayout layoutHeader;
	public int currentAudioIndex = 0;
	ImageButton buttonPlay;
	ImageButton buttonStop;
	ImageButton buttonRecord, buttonPause, buttonRecordPause;
	SeekBar progressRecord1;
	RelativeLayout LayoutAudioRecording;
	public LinearLayout  layout_note_more_Info ,layoutBlankView;
	public  RelativeLayout LayoutNoNoteElement;
	public TextView textViewDuration;
	final Context context = this;
	public ArrayList<NoteListDataModel> arrNoteListData;
	public ArrayList<DBNoteItemElement> arrDBNoteListData;
	public ListView listviewNotes;
	public NotesListAdapter adapter;
	private static final int SELECT_PICTURE = 1;
	private static final int REQUEST_CAMERA = 2;
	boolean isRecordingAudio = false;
	private MediaRecorder myAudioRecorder;
	View contentView;
	private String outputFile = null;

	// /Drawing Controls








	public boolean isMoreShown = false;
	public boolean isFirstTime = true;
	public boolean isTextmodeSelected = false;


	public String[] fonts_sizeName, fonts_Name_Display;
	public String[] fontSizes;
	ImageView background_bg;

	public DBNoteItems selectedDBNoteItem;
	public NoteshareDatabaseHelper androidOpenDbHelperObj;
	SQLiteDatabase sqliteDatabase;



	// 8b241b selected bg
	
	
	static  String imageFilePath="/eNote/Images/";
	static  String imageFolderPath="/eNote/Images";
	static  String audioFolderPath="/eNote/Audio";
	static  String notetypeText="TEXT";
	static  String notetypeAudio="AUDIO";
	static  String notetypeImage="IMAGE";
	static  String notetypeImageCAMRA="CAMERAIMAGE";
	static  String notetypeScribble="DRAWING";

	public DBNoteItemElement dbNoteItemElement;

	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID = 1;

	static final int Reminder_TIME_DIALOG_ID = 2;
	static final int Reminder_DATE_DIALOG_ID = 3;


	private int mYear, mMonth, mDay, mHour, mMinute, mSeconds;
	public int year, month, day, hour, minute, seconds;

	public String selecteddate, selectedTime, timeZone;
	public Calendar c;
	SharedPreferences preference;
	static String SAVELOCK = "LOCK";
	String appendPath;

    @Override
    protected void onResume() {

        onResumeCall();
    }

    @Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// inflate your activity layout here!
		contentView = inflater
				.inflate(R.layout.note_activity_main, null, false);
		mDrawerLayout.addView(contentView, 0);
		

		selectedDBNoteItem = DataManager.sharedDataManager().getSeletedDBNoteItem();
		initlizeUIElement(contentView);
		audioRecording(contentView);




		DataManager.sharedDataManager().setUpUserPermission(this,3);//READ


	}

    void  onResumeCall() {

		super.onResume();
        databaseInitlization();


        try {
			textViewheaderTitle.setText(""+selectedDBNoteItem.getNote_Title());
            getNoteFromDB();
			updateButtonUI(-1);

			if (layoutBlankView.getVisibility() == View.VISIBLE){
				layoutBlankView.setVisibility(View.VISIBLE);
			} else {
				layoutBlankView.setVisibility(View.GONE);
			}

        }catch (Exception exception)
        {
            exception.printStackTrace();
        }

    }

	void databaseInitlization()
	{
		// Data base initilization

		try
		{
			androidOpenDbHelperObj = new NoteshareDatabaseHelper(context);
			sqliteDatabase = androidOpenDbHelperObj.getWritableDatabase();


		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	// #PRAGA-MARK GET NOTE DATA IN DATABASE
	void getNoteFromDB()
	{

		try {


			if (arrDBNoteListData == null) {
				arrDBNoteListData=new ArrayList<DBNoteItemElement>();
			}
			if ( arrNoteListData == null) {
				arrNoteListData = new ArrayList<NoteListDataModel>();
			}

			arrNoteListData.clear();
			arrDBNoteListData.clear();




			ArrayList<DBNoteItemElement> arrNoteListItem = androidOpenDbHelperObj
                    .getAllNotesElementWithNote_Id(selectedDBNoteItem.getNote_Id());


			for (DBNoteItemElement elemet :arrNoteListItem)
            {

                NoteListDataModel model = new NoteListDataModel();

                if (elemet.getNOTE_ELEMENT_TYPE().equalsIgnoreCase(notetypeText))
                {
                    model.noteType = NOTETYPE.TEXTMODE;
                    model.stringtext= elemet.getNOTE_ELEMENT_CONTENT();
                    model.setNoteElmentId(elemet.getNOTE_ELEMENT_ID());
					model.setNoteElementDateTime(elemet.getNOTE_ELEMENT_DATE_TIME());

					if (model.stringtext.length()>0) {
						arrNoteListData.add(model);
						arrDBNoteListData.add(elemet);
					} else {

						androidOpenDbHelperObj.deleteNoteElement(elemet.getNOTE_ELEMENT_ID());
					}
                }
				else if (elemet.getNOTE_ELEMENT_TYPE().equalsIgnoreCase(notetypeImage))
                {
                    String filename=elemet.getNOTE_ELEMENT_CONTENT();

                   // Bitmap imageBitMap=getImageFromfilePath(filename);
					String imageBitMap=getDrawImageFromfilePath(filename);


                    if (imageBitMap!=null)
                    {
                        NoteListDataModel noteListdatamodel = new NoteListDataModel();
                        noteListdatamodel.noteType = NOTETYPE.IMAGEMODE;
						model.setNoteElementDateTime(elemet.getNOTE_ELEMENT_DATE_TIME());
						model.setNoteElmentId(elemet.getNOTE_ELEMENT_ID());
                        //noteListdatamodel.setBitmap(imageBitMap);
						noteListdatamodel.setBitmapPath(imageBitMap);
                        arrNoteListData.add(noteListdatamodel);
						arrDBNoteListData.add(elemet);
                    }
                    else
                    {
                        //blank image will be delete here
                        androidOpenDbHelperObj.deleteNoteElement(elemet.getNOTE_ELEMENT_ID());
                    }
                }
				else if (elemet.getNOTE_ELEMENT_TYPE().equalsIgnoreCase(notetypeImageCAMRA))
				{
					String filename=elemet.getNOTE_ELEMENT_CONTENT();

					// Bitmap imageBitMap=getImageFromfilePath(filename);
					String imageBitMap=filename;//getDrawImageFromfilePath(filename);


					if (imageBitMap!=null)
					{

						Bitmap imageBitMap1 = CameraUtill.sharedDataUtill().compressImage(imageBitMap,this);

						NoteListDataModel noteListdatamodel = new NoteListDataModel();
						noteListdatamodel.noteType = NOTETYPE.CAMERAIMAGEMODE;
						model.setNoteElementDateTime(elemet.getNOTE_ELEMENT_DATE_TIME());
						model.setNoteElmentId(elemet.getNOTE_ELEMENT_ID());
						noteListdatamodel.setBitmap(imageBitMap1);
						noteListdatamodel.setBitmapPath(imageBitMap);
						arrNoteListData.add(noteListdatamodel);
						arrDBNoteListData.add(elemet);
					}
					else
					{
						//blank image will be delete here
						androidOpenDbHelperObj.deleteNoteElement(elemet.getNOTE_ELEMENT_ID());
					}
				}
				else if (elemet.getNOTE_ELEMENT_TYPE().equalsIgnoreCase(notetypeScribble))
				{
					String filename=elemet.getNOTE_ELEMENT_CONTENT();

					String imageBitMap=getDrawImageFromfilePath(filename);

					if (imageBitMap!=null)
					{
						NoteListDataModel noteListdatamodel = new NoteListDataModel();
						noteListdatamodel.noteType = NOTETYPE.SCRIBBLEMODE;
						//noteListdatamodel.setBitmap(imageBitMap);
						noteListdatamodel.setBitmapPath(imageBitMap);
						model.setNoteElmentId(elemet.getNOTE_ELEMENT_ID());
						model.setNoteElementDateTime(elemet.getNOTE_ELEMENT_DATE_TIME());
						arrNoteListData.add(noteListdatamodel);
						arrDBNoteListData.add(elemet);
					}
					else
					{
						//blank image will be delete here
						androidOpenDbHelperObj.deleteNoteElement(elemet.getNOTE_ELEMENT_ID());
					}
				}
				else  if (elemet.getNOTE_ELEMENT_TYPE().equalsIgnoreCase(notetypeAudio))
				{
					model.noteType = NOTETYPE.AUDIOMODE;
					model.strAudioFilePath = elemet.getNOTE_ELEMENT_CONTENT();
					model.setNoteElmentId(elemet.getNOTE_ELEMENT_ID());
					model.setNoteElementDateTime(elemet.getNOTE_ELEMENT_DATE_TIME());
					arrNoteListData.add(model);
					arrDBNoteListData.add(elemet);

				}

            }

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					adapter.notifyDataSetChanged();
					if (isFirstTime) {
						//listviewNotes.smoothScrollToPosition(arrNoteListData.size() - 1);
						isFirstTime=false;
					} else {
						listviewNotes.smoothScrollToPosition(arrNoteListData.size() - 1);
					}


					if (arrNoteListData.size() >0 )
					{

						LayoutNoNoteElement.setVisibility(View.GONE);
					}
					else {

						LayoutNoNoteElement.setVisibility(View.VISIBLE);
					}

				}
			});

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	String getDrawImageFromfilePath(String filename) {

		String iconsStoragePath = Environment.getExternalStorageDirectory() + imageFolderPath;
		File destination = new File(iconsStoragePath);

		if(destination.exists())
		{
			String filePath = destination.toString() +File.separator+filename;
			//Bitmap myBitmap1 = BitmapFactory.decodeFile(filePath);
			return  filePath;
		}


		return  null;
	}



	private boolean storeCamraImage(Bitmap imageData,String imageType) {
		//get path to external storage (SD card)

		Log.d("Camra Path:",""+DataManager.sharedDataManager().getCamraAppendPath());
		String uri = DataManager.sharedDataManager().camraURI.toString();
		Log.e("uri-:", uri);
		///Toast.makeText(this, DataManager.sharedDataManager().camraURI.toString(),Toast.LENGTH_SHORT).show();


		try {

			androidOpenDbHelperObj.noteElementdatabaseInsertion(selectedDBNoteItem.getNote_Id(), uri, imageType, "", "");

		}  catch (Exception e) {
			Log.w("TAG", "Error saving image file: " + e.getMessage());
			return false;
		}
		return true;
	}

	void createImageFolder()
	{
		String iconsStoragePath = Environment.getExternalStorageDirectory() + imageFolderPath;

		File sdIconStorageDir = new File(iconsStoragePath);

		//create storage directories, if they don't exist
		if (!sdIconStorageDir.exists()) {

			sdIconStorageDir.mkdirs();
		}

	}

	private boolean storeImage(Bitmap imageData,String imageType) {
		//get path to external storage (SD card)

		if (imageData == null) {
			return false;
		}
		String filename;

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		filename =  sdf.format(date);
		String appendPath= filename+".jpg";

		// String iconsStoragePath = Environment.getExternalStorageDirectory() + "/eNotes/Images";
		String iconsStoragePath = Environment.getExternalStorageDirectory() + imageFolderPath;

		File sdIconStorageDir = new File(iconsStoragePath);

		//create storage directories, if they don't exist
		sdIconStorageDir.mkdirs();

		try {
			String filePath = sdIconStorageDir.toString() +File.separator+appendPath;
			FileOutputStream fileOutputStream = new FileOutputStream(filePath);

			BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
			//choose another format if PNG doesn't suit you
			imageData.compress(Bitmap.CompressFormat.JPEG, 100, bos);

			bos.flush();
			bos.close();

			androidOpenDbHelperObj.noteElementdatabaseInsertion(selectedDBNoteItem.getNote_Id(), appendPath, imageType, "", "");

		}  catch (Exception e) {
			Log.w("TAG", "Error saving image file: " + e.getMessage());
			return false;
		}
		return true;
	}

	// #PRAGA-MARK UPDATE NOTE DATA IN DATABASE



	void initlizeUIElement(View contentview) {

		/* Default Initlization */


		background_bg = (ImageView) contentview
				.findViewById(R.id.background_bg);
		layoutHeader = (RelativeLayout) contentview
				.findViewById(R.id.mainHeadermenue);
		imageButtoncalander = (ImageButton) layoutHeader

		.findViewById(R.id.imageButtoncalander);
		textViewheaderTitle = (TextView) layoutHeader
				.findViewById(R.id.textViewheaderTitle);
		imageButtonHamburg = (ImageButton) layoutHeader
				.findViewById(R.id.imageButtonHamburg);
		imageButtonsquence = (ImageButton) layoutHeader
				.findViewById(R.id.imageButtonsquence);

		imageButtonsquence.setImageResource(R.drawable.color_header_icon);
		imageButtonHamburg.setImageResource(R.drawable.back_icon_1);
		imageButtoncalander.setImageResource(R.drawable.done_icon);
		imageButtonsquence.setVisibility(View.GONE);






		initlizesMoreInfoView(contentview);





		// Main controls

		imageButtonAudioMode = (ImageButton) contentview
				.findViewById(R.id.imageButtonAudioMode);
		imageButtonImageMode = (ImageButton) contentview
				.findViewById(R.id.imageButtonImageMode);
		imageButtonPaintMode = (ImageButton) contentview
				.findViewById(R.id.imageButtonPaintMode);
		imageButtonShareMode = (ImageButton) contentview
				.findViewById(R.id.imageButtonShareMode);
		imageButtonTextMode = (ImageButton) contentview
				.findViewById(R.id.imageButtonTextMode);
		imageButtonMoreMode = (ImageButton) contentview
				.findViewById(R.id.imageButtonMoreMode);
		textViewheaderTitle.setText("Note");



		layoutBlankView=(LinearLayout) contentview.findViewById(R.id.layoutBlankView);
		layoutBlankView.setVisibility(View.GONE);

		arrNoteListData = new ArrayList<NoteListDataModel>();
		
		
		adapter = new NotesListAdapter(NoteMainActivity.this, arrNoteListData);
		listviewNotes = (ListView) contentview.findViewById(R.id.listviewNotes);
		listviewNotes.setAdapter(adapter);
		listviewNotes.setDivider(null);
		listviewNotes.setDividerHeight(0);

		/** Layout Audio Recording **/

		addlistners();


		updateHeaderControls(R.id.imageButtonHamburg);
		imageButtonsquence.setVisibility(View.GONE);

		fonts_sizeName = getResources().getStringArray(R.array.Font_Size_px);
		fontSizes = getResources().getStringArray(R.array.Font_Size);
		fonts_Name_Display = getResources().getStringArray(
				R.array.Font_Name_Display);

		initilizesDateAndTime();

		LayoutNoNoteElement = (RelativeLayout) findViewById(R.id.LayoutNoNoteElement);

	}

	void initilizesDateAndTime() {

		c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
		mSeconds = c.get(Calendar.SECOND);

		mMonth = mMonth + 1;

		TimeZone tz = c.getTimeZone();
		Log.d("Time zone", "=" + tz.getDisplayName());

		String tempMonth = "";
		String tempDay = "";

		if (mMonth > 9) {
			tempMonth = mMonth + "";
		} else {
			tempMonth = "0" + mMonth;
		}

		if (mDay > 9) {
			tempDay = "" + mDay;
		} else {
			tempDay = "0" + mDay;
		}

		String tempHours = "";
		String tempMiutes = "";
		String tempSeconds = "";

		if (mHour > 9) {
			tempHours = mHour + "";
		} else {
			tempHours = "0" + mHour;
		}

		if (mMinute > 9) {
			tempMiutes = "" + mMinute;
		} else {
			tempMiutes = "0" + mMinute;
		}

		if (mSeconds > 9) {
			tempSeconds = "" + mSeconds;
		} else {
			tempSeconds = "0" + mSeconds;
		}

		selecteddate = tempDay + "/" + tempMonth + "/" + mYear;
		selectedTime = tempHours + ":" + tempMiutes + ":" + tempSeconds;

	}

	private void updateItemAtPosition(int position) {

		int visiblePosition = listviewNotes.getFirstVisiblePosition();
		View view = listviewNotes.getChildAt(position - visiblePosition);
		listviewNotes.getAdapter().getView(position, view, listviewNotes);

	}

	void initlizeAudiorecoder() {

		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHH_mm_ss");
		String date = sdf.format(new Date(System.currentTimeMillis()));

		String sep = File.separator; // Use this instead of hardcoding the "/"
		String newFolder = audioFolderPath;

		try {
			outputFile=getAudioFilename();

			Log.d("the Audio Path:",""+outputFile);

			myAudioRecorder = new MediaRecorder();
			myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
			myAudioRecorder.setOutputFile(outputFile);

			androidOpenDbHelperObj.noteElementdatabaseInsertion(selectedDBNoteItem.getNote_Id(), "", notetypeAudio, "", "");

			ArrayList<DBNoteItemElement> lastInsertedElement=androidOpenDbHelperObj.getLastNotesElementWithNote_Id(selectedDBNoteItem.getNote_Id());

			if (lastInsertedElement.size()>0)
			{
				dbNoteItemElement=lastInsertedElement.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getAudioFilename()
	{
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath,audioFolderPath);

		if(!file.exists()){
			file.mkdirs();
		}

		return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".mp3");
	}

	void audioRecording(View contentview) {

		LayoutAudioRecording = (RelativeLayout) contentview
				.findViewById(R.id.LayoutAudioRecording);
		buttonPlay = (ImageButton) LayoutAudioRecording
				.findViewById(R.id.buttonPlay);
		buttonStop = (ImageButton) LayoutAudioRecording
				.findViewById(R.id.buttonStop);
		buttonRecord = (ImageButton) LayoutAudioRecording
				.findViewById(R.id.buttonRecord);
		buttonPause = (ImageButton) LayoutAudioRecording
				.findViewById(R.id.buttonPause);

		buttonRecordPause = (ImageButton) LayoutAudioRecording
				.findViewById(R.id.buttonRecordPause);

		progressRecord1 = (SeekBar) LayoutAudioRecording
				.findViewById(R.id.progressRecord1);

		textViewDuration = (TextView) LayoutAudioRecording
				.findViewById(R.id.textViewDuration);

		progressRecordtext = (TextView) LayoutAudioRecording
				.findViewById(R.id.progressRecordtext);

		textViewDuration.setVisibility(View.GONE);
		LayoutAudioRecording.setVisibility(View.GONE);
		progressRecord1.setVisibility(View.GONE);
		progressRecordtext.setVisibility(View.GONE);
		buttonStop.setVisibility(View.GONE);

		ImageButton audioClose = (ImageButton) LayoutAudioRecording.findViewById(R.id.audioClose);

		audioClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				isMoreShown=false;
				layout_note_more_Info.setVisibility(View.GONE);
				LayoutAudioRecording.setVisibility(View.GONE);
				layoutBlankView.setVisibility(View.GONE);

				updateButtonUI(-1);

			}
			});

		buttonRecordPause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				buttonRecord.setVisibility(View.VISIBLE);
				buttonRecordPause.setVisibility(View.GONE);
				myAudioRecorder.stop();
				progressRecordtext.setText("");
				isMoreShown=false;
				layout_note_more_Info.setVisibility(View.GONE);


			}
		});

		buttonPause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				isMoreShown=false;
				layout_note_more_Info.setVisibility(View.GONE);
				buttonPause.setVisibility(View.VISIBLE);
				buttonPlay.setVisibility(View.VISIBLE);

				buttonPlay.setEnabled(true);
				buttonPause.setEnabled(false);
				progressRecordtext.setVisibility(View.GONE);

				buttonRecord.setVisibility(View.GONE);
				buttonStop.setVisibility(View.GONE);

				try {

					mediaPlayer.pause();
					Toast.makeText(NoteMainActivity.this, "Recording pause",
							Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					Toast.makeText(NoteMainActivity.this, "pause error",
							Toast.LENGTH_SHORT).show();
				}
				progressRecordtext.setText("");

			}
		});

		buttonPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) throws IllegalArgumentException,
					SecurityException, IllegalStateException {
				// TODO Auto-generated method stub
				// Button play Click
				isMoreShown=false;
				layout_note_more_Info.setVisibility(View.GONE);
				progressRecord1.setProgress(0);

				mediaPlayer = new MediaPlayer();
				progressRecordtext.setVisibility(View.GONE);

				System.out.println("get total duration"
						+ mediaPlayer.getDuration());


				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer arg0) {

						buttonPlay.setEnabled(true);
						buttonPause.setEnabled(false);
						// progressRecord1.setProgress(0);

					}
				});
				textViewDuration.setVisibility(View.VISIBLE);

				buttonPause.setVisibility(View.VISIBLE);
				buttonPlay.setVisibility(View.VISIBLE);

				buttonPlay.setEnabled(false);
				buttonPause.setEnabled(true);

				buttonRecord.setVisibility(View.GONE);
				buttonStop.setVisibility(View.GONE);

				try {

					System.out.println("file playing path:" + outputFile);
					mediaPlayer.setDataSource(outputFile);
				} catch (IOException e) {
					e.printStackTrace();
				}

				try {
					mediaPlayer.prepare();

				} catch (IOException e) {

					e.printStackTrace();
				}

				progressRecord1.setMax(mediaPlayer.getDuration() / 1000);
				mediaPlayer.start();

				final Handler mHandler = new Handler();
				// Make sure you update Seekbar on UI thread
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (mediaPlayer != null)
						{
							int mCurrentPosition = mediaPlayer
									.getCurrentPosition() / 1000;
							String currentduration = getDurationBreakdown(mediaPlayer
									.getCurrentPosition());
							String currentduration1 = getDurationBreakdown(mediaPlayer
									.getDuration());

							if (mCurrentPosition <= mediaPlayer.getDuration() / 1000) {
								System.out.println("CurrentDuration:"
										+ currentduration);
								progressRecord1.setProgress(mCurrentPosition);

								textViewDuration.setText(currentduration + "/"
										+ currentduration1);

							}

						}
						mHandler.postDelayed(this, 1000);
					}
				});

				Toast.makeText(NoteMainActivity.this, "Recording playing",
						Toast.LENGTH_SHORT).show();
				progressRecordtext.setText("");


			}
		});

		
		
		buttonStop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			try {
				LayoutAudioRecording.setVisibility(View.GONE);
				layoutBlankView.setVisibility(View.GONE);
				isMoreShown=false;
				layout_note_more_Info.setVisibility(View.GONE);
				progressRecord1.setVisibility(View.GONE);

				if (myAudioRecorder==null) {

					return;
				}
				myAudioRecorder.stop();
				myAudioRecorder.release();
				myAudioRecorder = null;

				buttonPause.setVisibility(View.VISIBLE);
				buttonPlay.setVisibility(View.VISIBLE);

				buttonPlay.setEnabled(true);
				buttonPause.setEnabled(false);
				layoutBlankView.setVisibility(View.GONE);



				buttonRecord.setVisibility(View.GONE);
				buttonStop.setVisibility(View.GONE);

				Toast.makeText(NoteMainActivity.this, "Recording stopped",
						Toast.LENGTH_SHORT).show();

				System.out.println("Current Index:" + currentAudioIndex);

				NoteListDataModel noteListdatamodel = new NoteListDataModel();
				noteListdatamodel.noteType = NOTETYPE.AUDIOMODE;
				noteListdatamodel.setStrAudioFilePath(outputFile);
				arrNoteListData.add(currentAudioIndex, noteListdatamodel);


				adapter.notifyDataSetChanged();
				listviewNotes.smoothScrollToPosition(currentAudioIndex);




				progressRecordtext.setText("");

				if (dbNoteItemElement!=null)
				{
					androidOpenDbHelperObj.updateNoteElementText(dbNoteItemElement.getNOTE_ELEMENT_ID(),outputFile);
				}
				blinkAnimation(progressRecordtext,false);
				getNoteFromDB();

				updateButtonUI(-1);
			}
			catch (Exception e) {
				LayoutAudioRecording.setVisibility(View.GONE);
				layoutBlankView.setVisibility(View.GONE);
				e.printStackTrace();
			}

			}
		});

		buttonRecord.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				isMoreShown=false;
				layout_note_more_Info.setVisibility(View.GONE);

				isRecordingAudio = true;
				progressRecordtext.setVisibility(View.VISIBLE);

				// TODO Auto-generated method stub
				try {

					myAudioRecorder.prepare();
					myAudioRecorder.start();

				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				buttonPause.setVisibility(View.GONE);
				buttonPlay.setVisibility(View.GONE);
				buttonRecordPause.setVisibility(View.GONE);
				textViewDuration.setVisibility(View.GONE);

				buttonStop.setVisibility(View.VISIBLE);
				buttonRecord.setVisibility(View.GONE);

				buttonStop.setEnabled(true);
				buttonRecord.setEnabled(false);

				Toast.makeText(NoteMainActivity.this, "Recording started",
						Toast.LENGTH_SHORT).show();
				progressRecordtext.setText("REC.");
				progressRecordtext.setTextColor(Color.RED);


				if (arrNoteListData.size() > 0) {
					currentAudioIndex = arrNoteListData.size();
				} else {
					currentAudioIndex = 0;
				}

				blinkAnimation(progressRecordtext,true);
			}
		});
	}




	void updateHeaderControls(int itemId) {
		imageButtonHamburg.setBackgroundColor(getResources().getColor(
				R.color.header_bg));
		imageButtoncalander.setBackgroundColor(getResources().getColor(
				R.color.header_bg));
		imageButtonsquence.setBackgroundColor(getResources().getColor(
				R.color.header_bg));

		switch (itemId) {
		case R.id.imageButtonHamburg:
			imageButtonHamburg.setBackgroundColor(getResources().getColor(
					R.color.header_bg));
			break;
		case R.id.imageButtoncalander:
			imageButtoncalander.setBackgroundColor(getResources().getColor(
					R.color.A8b241b));
			break;
		case R.id.imageButtonsquence:
			imageButtonsquence.setBackgroundColor(getResources().getColor(
					R.color.A8b241b));
			break;

		default:
			break;
		}

	}

	/************* moreinfo control Here ************/
	void initlizesMoreInfoView(View contentView) {
		layout_note_more_Info = (LinearLayout) contentView
				.findViewById(R.id.layout_note_more_Info);
		layout_note_more_Info.setVisibility(View.GONE);

		Button buttonLock = (Button) layout_note_more_Info
				.findViewById(R.id.buttonLock);
		Button buttonDelete = (Button) layout_note_more_Info
				.findViewById(R.id.buttonDelete);
		Button buttonRemind = (Button) layout_note_more_Info
				.findViewById(R.id.buttonRemind);
		Button buttonTimeBomb = (Button) layout_note_more_Info
				.findViewById(R.id.buttonTimeBomb);
		Button buttonAttach = (Button) layout_note_more_Info
				.findViewById(R.id.buttonAttach);

		buttonLock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				System.out.println("button lock");
				showAlertToLockNUnlockNote(selectedDBNoteItem, NoteMainActivity.this, true, false);
				layout_note_more_Info.setVisibility(View.GONE);
				isMoreShown=false;
				updateButtonUI(-1);

			}
		});
		buttonDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				System.out.println("button delete");

				showAlertWithDeleteMessage("Do you want to delete this Note",NoteMainActivity.super.getApplicationContext(),selectedDBNoteItem);

			}
		});
		buttonRemind.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				System.out.println("button remind");
				showDialog(Reminder_DATE_DIALOG_ID);
				layout_note_more_Info.setVisibility(View.GONE);
				isMoreShown=false;
				updateButtonUI(-1);
			}
		});
		buttonTimeBomb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				System.out.println("button time bomb");
				showDialog(DATE_DIALOG_ID);
				layout_note_more_Info.setVisibility(View.GONE);
				isMoreShown=false;
				updateButtonUI(-1);

			}
		});
		buttonAttach.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				System.out.println("button attached");
				layout_note_more_Info.setVisibility(View.GONE);
				isMoreShown=false;
				updateButtonUI(-1);

			}
		});


	}

	/************* main list control Here ************/
	void addlistners() {

		imageButtoncalander.setVisibility(View.GONE);
		imageButtonShareMode.setVisibility(View.GONE);


		// #NOTE ITEM CLCIK

		listviewNotes.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				// TODO Auto-generated method stub
				NoteListDataModel notelistitem = arrNoteListData.get(arg2);
				DBNoteItemElement dbNoteItemElement = arrDBNoteListData.get(arg2);
				notelistitem.setNoteElmentId(dbNoteItemElement.getNOTE_ELEMENT_ID());


				layout_note_more_Info.setVisibility(View.GONE);
				imageButtonsquence.setVisibility(View.GONE);
				isMoreShown = false;
				updateButtonUI(-1);

				switch (notelistitem.noteType) {
					case TEXTMODE: {

						DataManager.sharedDataManager().setNotelistData(
								notelistitem);

						//startActivity(new Intent(NoteMainActivity.this,NoteTextIItemFullScreen.class));

					}
					break;
					case IMAGEMODE: {
						//Toast.makeText(getApplicationContext(), "Image mode",Toast.LENGTH_SHORT).show();
						DataManager.sharedDataManager().setNotelistData(notelistitem);
						startActivity(new Intent(NoteMainActivity.this,NoteItemFullScreen.class));
					}
					break;
					case SCRIBBLEMODE: {
						//Toast.makeText(getApplicationContext(), "scribble mode",Toast.LENGTH_SHORT).show();
						DataManager.sharedDataManager().setNotelistData(notelistitem);
						startActivity(new Intent(NoteMainActivity.this,NoteItemFullScreen.class));
					}
					break;
					case AUDIOMODE: {
						//Toast.makeText(getApplicationContext(), "audio mode",Toast.LENGTH_SHORT).show();

					}
					break;

					default:
						break;
				}

			}
		});

		// #BUTTON CHECKED CLICK TEXTSAVE

		imageButtoncalander.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (layoutBlankView.getVisibility() == View.VISIBLE){
					layoutBlankView.setVisibility(View.VISIBLE);
					LayoutAudioRecording.setVisibility(View.VISIBLE);
				} else {
					layoutBlankView.setVisibility(View.GONE);
					LayoutAudioRecording.setVisibility(View.GONE);
				}
				layout_note_more_Info.setVisibility(View.GONE);
				imageButtonsquence.setVisibility(View.GONE);
				isMoreShown = false;


				updateHeaderControls(-1);

				isTextmodeSelected = false;

			}
		});

		imageButtonHamburg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// openSlideMenu();
				if (layoutBlankView.getVisibility() == View.VISIBLE){
					layoutBlankView.setVisibility(View.VISIBLE);
					LayoutAudioRecording.setVisibility(View.VISIBLE);
				} else {
					layoutBlankView.setVisibility(View.GONE);
					LayoutAudioRecording.setVisibility(View.GONE);
				}
				isTextmodeSelected = false;
				layout_note_more_Info.setVisibility(View.GONE);
				imageButtonsquence.setVisibility(View.GONE);
				imageButtoncalander.setVisibility(View.GONE);
				isMoreShown = false;
				//finish();

				onBackPressed();

			}
		});

		imageButtonsquence.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				if (layoutBlankView.getVisibility() == View.VISIBLE){
					layoutBlankView.setVisibility(View.VISIBLE);
					LayoutAudioRecording.setVisibility(View.VISIBLE);
				} else {
					layoutBlankView.setVisibility(View.GONE);
					LayoutAudioRecording.setVisibility(View.GONE);
				}
				imageButtoncalander.setVisibility(View.GONE);
				layout_note_more_Info.setVisibility(View.GONE);

				isMoreShown = false;


				updateHeaderControls(v.getId());



			}
		});

		
		
		
		imageButtonAudioMode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub

				LayoutNoNoteElement.setVisibility(View.GONE);

				DataManager.sharedDataManager().setUpUserPermission(NoteMainActivity.this,1);//RECORD
				

				System.out.println("audio mode");
				updatenoteList(NOTETYPE.AUDIOMODE);


				imageButtonsquence.setVisibility(View.GONE);
				imageButtoncalander.setVisibility(View.GONE);
				layout_note_more_Info.setVisibility(View.GONE);
				isMoreShown = false;


				layoutBlankView.setVisibility(View.VISIBLE);
				LayoutAudioRecording.setVisibility(View.VISIBLE);
				buttonPlay.setVisibility(View.GONE);
				buttonRecordPause.setVisibility(View.GONE);
				buttonPause.setVisibility(View.GONE);
				buttonRecord.setVisibility(View.VISIBLE);
				buttonStop.setVisibility(View.GONE);
				progressRecord1.setVisibility(View.GONE);
				textViewDuration.setVisibility(View.GONE);
				buttonRecord.setEnabled(true);
				buttonStop.setEnabled(true);
				updateButtonUI(R.id.imageButtonAudioMode);
				initlizeAudiorecoder();

			}
		});
		imageButtonImageMode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (layoutBlankView.getVisibility() == View.VISIBLE){
					layoutBlankView.setVisibility(View.VISIBLE);
					LayoutAudioRecording.setVisibility(View.VISIBLE);
				} else {
					layoutBlankView.setVisibility(View.GONE);
					LayoutAudioRecording.setVisibility(View.GONE);
				}

				System.out.println("image  mode");
				LayoutNoNoteElement.setVisibility(View.GONE);
				updateButtonUI(R.id.imageButtonImageMode);

				DataManager.sharedDataManager().setUpUserPermission(NoteMainActivity.this,3);//WRITE


				showImageChooserAlertWith("", NoteMainActivity.this);
				imageButtonsquence.setVisibility(View.GONE);
				layout_note_more_Info.setVisibility(View.GONE);
				isMoreShown = false;

				imageButtoncalander.setVisibility(View.GONE);
			}
		});
		imageButtonPaintMode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				DataManager.sharedDataManager().setUpUserPermission(NoteMainActivity.this,2);//WRITE
				if (layoutBlankView.getVisibility() == View.VISIBLE){
					layoutBlankView.setVisibility(View.VISIBLE);
					LayoutAudioRecording.setVisibility(View.VISIBLE);
				} else {
					layoutBlankView.setVisibility(View.GONE);
					LayoutAudioRecording.setVisibility(View.GONE);
				}
				updateButtonUI(-1);
				Intent intent =new Intent(NoteMainActivity.this,NewDrawingActivity.class);
				startActivity(intent);
			}
		});
		imageButtonShareMode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (layoutBlankView.getVisibility() == View.VISIBLE){
					layoutBlankView.setVisibility(View.VISIBLE);
					LayoutAudioRecording.setVisibility(View.VISIBLE);
				} else {
					layoutBlankView.setVisibility(View.GONE);
					LayoutAudioRecording.setVisibility(View.GONE);
				}
				LayoutNoNoteElement.setVisibility(View.GONE);

				updateButtonUI(R.id.imageButtonShareMode);
				System.out.println("share mode");

				imageButtonsquence.setVisibility(View.GONE);
				layout_note_more_Info.setVisibility(View.GONE);
				isMoreShown = false;

			}
		});

		imageButtonTextMode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (layoutBlankView.getVisibility() == View.VISIBLE){
					layoutBlankView.setVisibility(View.VISIBLE);
					LayoutAudioRecording.setVisibility(View.VISIBLE);
				} else {
					layoutBlankView.setVisibility(View.GONE);
					LayoutAudioRecording.setVisibility(View.GONE);
				}
				LayoutNoNoteElement.setVisibility(View.GONE);
				updateButtonUI(R.id.imageButtonTextMode);
				System.out.println("text mode");

				isTextmodeSelected = true;


				imageButtonsquence.setVisibility(View.GONE);
				layout_note_more_Info.setVisibility(View.GONE);
				isMoreShown = false;

				imageButtoncalander.setVisibility(View.GONE);
                startActivity(new Intent(NoteMainActivity.this,
                        TextEditorActivity.class));
			}
		});
		imageButtonMoreMode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (layoutBlankView.getVisibility() == View.VISIBLE){
					layoutBlankView.setVisibility(View.VISIBLE);
					LayoutAudioRecording.setVisibility(View.VISIBLE);
				} else {
					layoutBlankView.setVisibility(View.GONE);
					LayoutAudioRecording.setVisibility(View.GONE);
				}
				LayoutNoNoteElement.setVisibility(View.GONE);
				imageButtonsquence.setVisibility(View.GONE);

				System.out.println("more mode");

				updateButtonUI(R.id.imageButtonMoreMode);



				if (isMoreShown == false) {
					isMoreShown = true;
					layout_note_more_Info.setVisibility(View.VISIBLE);
				} else {
					isMoreShown = false;
					layout_note_more_Info.setVisibility(View.GONE);
					updateButtonUI(-1);
				}
				imageButtoncalander.setVisibility(View.GONE);

				Button buttonLock = (Button) layout_note_more_Info
						.findViewById(R.id.buttonLock);

				if (selectedDBNoteItem.getNote_Lock_Status() !=null){
					if (selectedDBNoteItem.getNote_Lock_Status().equalsIgnoreCase("1")) {
						buttonLock.setVisibility(View.GONE);
					}
				}
			}
		});

	}

	void blinkAnimation(TextView  obj ,boolean isStart)
	{
		Animation anim = new AlphaAnimation(0.0f, 1.0f);
		anim.setDuration(90); //You can manage the blinking time with this parameter
		anim.setStartOffset(50);
		anim.setRepeatMode(Animation.REVERSE);
		anim.setRepeatCount(Animation.INFINITE);
		if (isStart == true) {
			obj.startAnimation(anim);
		} else {
			obj.clearAnimation();
		}

	}

	void insertTextInDb() {


		androidOpenDbHelperObj.noteElementdatabaseInsertion(selectedDBNoteItem.getNote_Id(),"",notetypeText,"","");


		ArrayList<DBNoteItemElement> lastdbItem=androidOpenDbHelperObj.getLastNotesElementWithNote_Id(selectedDBNoteItem.getNote_Id());

        if (lastdbItem!=null)
			{
				if (lastdbItem.size()>0)
				{
					DBNoteItemElement lastinsertedElementDetail=lastdbItem.get(0);
					NoteListDataModel model = new NoteListDataModel();

					if (lastinsertedElementDetail.getNOTE_ELEMENT_TYPE().equalsIgnoreCase(notetypeText))
					{
						model.noteType = NOTETYPE.TEXTMODE;
						model.stringtext = lastinsertedElementDetail.getNOTE_ELEMENT_CONTENT();
						model.setNoteElmentId(lastinsertedElementDetail.getNOTE_ELEMENT_ID());
						arrNoteListData.add(model);
						adapter.notifyDataSetChanged();
						listviewNotes.smoothScrollToPosition(arrNoteListData.size() - 1);

						if (arrNoteListData.size()>0)
						{
							try {
								View convertView = listviewNotes.getChildAt(listviewNotes.getFirstVisiblePosition());
								EditText textViewSlideMenuName=(EditText)convertView.findViewById(R.id.textViewSlideMenuName);
								InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.showSoftInput(textViewSlideMenuName, InputMethodManager.SHOW_IMPLICIT);
							} catch (Exception e)
							{
								e.printStackTrace();
							}

						}

					}

				}
			}


	}

	void updatenoteList(NOTETYPE notetype) {
		NoteListDataModel noteListdatamodel = new NoteListDataModel();
		noteListdatamodel.noteType = notetype;

		if (notetype == NOTETYPE.AUDIOMODE) {
			noteListdatamodel.setStrAudioFilePath("");
			if (arrNoteListData.size() > 0) {
				currentAudioIndex = arrNoteListData.size() - 1;
			} else 
			{
				currentAudioIndex = 0;
			}

		} else 
		{
			arrNoteListData.add(noteListdatamodel);
		}

		adapter.notifyDataSetChanged();
		listviewNotes.smoothScrollToPosition(arrNoteListData.size() - 1);
	}

	void updateButtonUI(int id) {


		if (LayoutAudioRecording.getVisibility() == View.VISIBLE){
			layoutBlankView.setVisibility(View.VISIBLE);
			LayoutAudioRecording.setVisibility(View.VISIBLE);
		} else  {
			layoutBlankView.setVisibility(View.GONE);
			LayoutAudioRecording.setVisibility(View.GONE);
		}
		imageButtonAudioMode.setBackgroundColor(getResources().getColor(
				R.color.header_bg));
		imageButtonTextMode.setBackgroundColor(getResources().getColor(
				R.color.header_bg));
		imageButtonShareMode.setBackgroundColor(getResources().getColor(
				R.color.header_bg));
		imageButtonPaintMode.setBackgroundColor(getResources().getColor(
				R.color.header_bg));
		imageButtonMoreMode.setBackgroundColor(getResources().getColor(
				R.color.header_bg));
		imageButtonImageMode.setBackgroundColor(getResources().getColor(
				R.color.header_bg));

		switch (id) {

		case R.id.imageButtonMoreMode: {

			imageButtonMoreMode.setBackgroundColor(getResources().getColor(
					R.color.A8b241b));
		}
			break;
		case R.id.imageButtonTextMode: {

			imageButtonTextMode.setBackgroundColor(getResources().getColor(
					R.color.A8b241b));

		}
			break;
		case R.id.imageButtonPaintMode: {

			imageButtonPaintMode.setBackgroundColor(getResources().getColor(
					R.color.A8b241b));

		}
			break;
		case R.id.imageButtonShareMode: {

			imageButtonShareMode.setBackgroundColor(getResources().getColor(
					R.color.A8b241b));

		}
			break;
		case R.id.imageButtonAudioMode: {
			imageButtonAudioMode.setBackgroundColor(getResources().getColor(
					R.color.A8b241b));

		}
			break;
		case R.id.imageButtonImageMode: {
			imageButtonImageMode.setBackgroundColor(getResources().getColor(
					R.color.A8b241b));

		}
			break;

		default:
			break;
		}

	}

	public void getImage() {
		final CharSequence[] items = { "Take Photo", "Choose from Gallary",
				"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(
				NoteMainActivity.this);
		builder.setTitle("Add Photo");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {




					openCamera();


				} else if (items[item].equals("Choose from Gallary")) {

					Intent intent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					startActivityForResult(
							Intent.createChooser(intent, "Select File"),
							SELECT_PICTURE);

				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
		
	}

	void  openCamera()
	{
		String filename;

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		filename =  sdf.format(date);
		appendPath= filename+".jpg";

		DataManager.sharedDataManager().setCamraAppendPath(appendPath);

		String iconsStoragePath1 = Environment.getExternalStorageDirectory() + imageFolderPath;
		String iconsStoragePath = iconsStoragePath1+File.separator+appendPath;
		File sdIconStorageDir = new File(iconsStoragePath);
		Uri tempUri=Uri.fromFile(sdIconStorageDir);
		DataManager.sharedDataManager().setCamraURI(tempUri);

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,tempUri);
		startActivityForResult(intent, REQUEST_CAMERA);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK) 
		{
			imageButtoncalander.setVisibility(View.GONE);

			if (requestCode == REQUEST_CAMERA)
			{
				//Bitmap thumbnail = (Bitmap) data.getExtras().get("data");


				try {


					//storeImage(thumbnail,notetypeImage);

					storeCamraImage(null,notetypeImageCAMRA);

				} catch (Exception e)
				{
					e.printStackTrace();
				}

			} 
			else if (requestCode == SELECT_PICTURE)
			{

				String filename="image_"+System.currentTimeMillis()+".jpg";
				try
				{
					Uri selectedImageUri = data.getData();

					String[] projection = { MediaStore.MediaColumns.DATA };
					Cursor cursor = managedQuery(selectedImageUri, projection,
							null, null, null);
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
					cursor.moveToFirst();

					String selectedImagePath = cursor.getString(column_index);

					System.out.println("Gallery Image Selected path:"
							+ selectedImagePath);

					Log.d("The Gallary image Path:", selectedImagePath);

					Bitmap bm;
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeFile(selectedImagePath, options);
					final int REQUIRED_SIZE = 320;
					int scale = 1;
					while (options.outWidth / scale / 2 >= REQUIRED_SIZE
							&& options.outHeight / scale / 2 >= REQUIRED_SIZE)
						scale *= 2;
					options.inSampleSize = scale;
					options.inJustDecodeBounds = false;

					bm = BitmapFactory.decodeFile(selectedImagePath, options);

					//ByteArrayOutputStream bytes = new ByteArrayOutputStream();
					//bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

					try {
						//createImageFile(bm,notetypeImage);
						storeImage(bm,notetypeImage);

					} catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				 catch (Exception e) {

					 e.printStackTrace();
				 }

			}
		}

	}



	void showImageChooserAlertWith(String message, Context context) {

		final Dialog dialog = new Dialog(context);

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// inflate your activity layout here!
		View contentView = inflater.inflate(R.layout.chooseimagealertview,
				null, false);

		TextView textViewTitleAlert = (TextView) contentView
				.findViewById(R.id.textViewTitleAlert);
		textViewTitleAlert.setText("SELECT IMAGE");
		textViewTitleAlert.setTextColor(Color.WHITE);

		TextView buttonAlertCancel = (TextView) contentView
				.findViewById(R.id.buttonAlertCancel);

		TextView textViewTitleTakePicture = (TextView) contentView
				.findViewById(R.id.textViewTitleTakePicture);

		TextView textViewTitlechosserfromgallary = (TextView) contentView
				.findViewById(R.id.textViewTitlechosserfromgallary);

		buttonAlertCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				updateButtonUI(-1);
				dialog.dismiss();

			}
		});
		textViewTitleTakePicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// System.exit(0);
				//Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				//startActivityForResult(intent, REQUEST_CAMERA);

				createImageFolder();

				/*String filename;

				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
				filename =  sdf.format(date);
				appendPath= filename+".jpg";

				DataManager.sharedDataManager().setCamraAppendPath(appendPath);

				// String iconsStoragePath = Environment.getExternalStorageDirectory() + "/eNotes/Images";
				String iconsStoragePath = Environment.getExternalStorageDirectory() + imageFolderPath+File.separator+appendPath;

				File sdIconStorageDir = new File(iconsStoragePath);
				//create storage directories, if they don't exist


				Uri tempUri=Uri.fromFile(sdIconStorageDir);

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT,tempUri);
				startActivityForResult(intent, REQUEST_CAMERA);*/

				openCamera();

				dialog.dismiss();

			}
		});

		textViewTitlechosserfromgallary
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						// System.exit(0);

						Intent intent = new Intent(
								Intent.ACTION_PICK,
								android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						intent.setType("image/*");
						startActivityForResult(
								Intent.createChooser(intent, "Select File"),
								SELECT_PICTURE);

						dialog.dismiss();

					}
				});

		dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {

				updateButtonUI(-1);
			}
		});

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(contentView);
		dialog.show();

	}

	void showAlertWith(String message, Context context) {

		final Dialog dialog = new Dialog(context);

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View contentView = inflater.inflate(R.layout.alert_view, null, false);

		TextView textViewTitleAlert = (TextView) contentView
				.findViewById(R.id.textViewTitleAlert);
		textViewTitleAlert.setText("ALERT");
		textViewTitleAlert.setTextColor(Color.WHITE);
		TextView textViewTitleAlertMessage = (TextView) contentView
				.findViewById(R.id.textViewTitleAlertMessage);
		textViewTitleAlertMessage.setText(message);

		Button buttonAlertCancel = (Button) contentView
				.findViewById(R.id.buttonAlertCancel);
		Button buttonAlertOk = (Button) contentView
				.findViewById(R.id.buttonAlertOk);
		buttonAlertCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				dialog.dismiss();

			}
		});
		buttonAlertOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				dialog.dismiss();
			}
		});

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(contentView);
		dialog.show();

	}

	/************* show brush control Here ************/




	/************* text control Here ************/


	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		DataManager.sharedDataManager().setArrNoteListData(
				new ArrayList<NoteListDataModel>());


	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub



	}















	public static String getDurationBreakdown(long millis) {
		if (millis < 0) {
			throw new IllegalArgumentException(
					"Duration must be greater than zero!");
		}

		// long days = TimeUnit.MILLISECONDS.toDays(millis);
		// millis -= TimeUnit.DAYS.toMillis(days);
		// long hours = TimeUnit.MILLISECONDS.toHours(millis);
		// millis -= TimeUnit.HOURS.toMillis(hours);
		// long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		// millis -= TimeUnit.MINUTES.toMillis(minutes);
		//
		// long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

		// StringBuilder sb = new StringBuilder(64);

		StringBuffer sb = new StringBuffer();

		int hours = (int) (millis / (1000 * 60 * 60));
		int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
		int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

		sb.append(String.format("%02d", hours)).append(":")
				.append(String.format("%02d", minutes)).append(":")
				.append(String.format("%02d", seconds));

		System.out.println("time is:" + sb.toString());

		return sb.toString();

	}



	/**********************  on list view text edited ****************/
	public void didEditedTextField(NoteListDataModel notelistData, int index,String text)
	{

			Log.d("The TextChange: ","ID: "+notelistData.getNoteElmentId()+"\n");
			Log.d("The TextChange: ","Text: "+text+"\n");
			Log.d("The TextChange: ", "Index: " + index + "\n");
			androidOpenDbHelperObj.updateNoteElementText(notelistData.getNoteElmentId(), text);
			NoteListDataModel editedDatamodel=arrNoteListData.get(position);
			editedDatamodel.setStringtext(text);
	}



/***********************Utility methods ***********************/

void showAlertWithDeleteMessage(String message, Context context, final DBNoteItems dbNoteItems) {

	final Dialog dialogdelete = new Dialog(NoteMainActivity.this);

	LayoutInflater inflater = (LayoutInflater) this
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	// inflate your activity layout here!
	View contentView = inflater.inflate(R.layout.alert_view, null, false);

	TextView textViewTitleAlert = (TextView) contentView
			.findViewById(R.id.textViewTitleAlert);
	textViewTitleAlert.setText("" + dbNoteItems.getNote_Title());
	textViewTitleAlert.setTextColor(Color.WHITE);
	TextView textViewTitleAlertMessage = (TextView) contentView
			.findViewById(R.id.textViewTitleAlertMessage);
	textViewTitleAlertMessage.setText(message);

	Button buttonAlertCancel = (Button) contentView
			.findViewById(R.id.buttonAlertCancel);
	Button buttonAlertOk = (Button) contentView
			.findViewById(R.id.buttonAlertOk);
	buttonAlertCancel.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			dialogdelete.dismiss();

		}
	});
	buttonAlertOk.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub

			boolean status = androidOpenDbHelperObj.deleteNote(selectedDBNoteItem.getNote_Id());
			if (status == true) {



				Toast.makeText(NoteMainActivity.this, "Note deleted successfully", Toast.LENGTH_SHORT).show();
				finish();

			} else {
				Toast.makeText(NoteMainActivity.this, "Note deleted unsuccessfully", Toast.LENGTH_SHORT).show();

			}


			dialogdelete.dismiss();

		}
	});

	dialogdelete.setOnDismissListener(new DialogInterface.OnDismissListener() {
		@Override
		public void onDismiss(DialogInterface dialog) {

			layout_note_more_Info.setVisibility(View.GONE);
			isMoreShown=false;
			updateButtonUI(-1);
		}
	});

	dialogdelete.requestWindowFeature(Window.FEATURE_NO_TITLE);
	dialogdelete.setCancelable(false);
	dialogdelete.setContentView(contentView);
	dialogdelete.show();

}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DATE_DIALOG_ID:
				// create a new DatePickerDialog with values you want to show
				DatePickerDialog datePickerDialog = new DatePickerDialog(this,
						mDateSetListener,
						mYear, mMonth - 1, mDay);

				datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
				datePickerDialog.setOnDismissListener(dissmissDatePickerDialog);


				return datePickerDialog;

			// create a new TimePickerDialog with values you want to show
			case TIME_DIALOG_ID: {

				TimePickerDialog timePickerDialog = new TimePickerDialog(this,
						mTimeSetListener, mHour, mMinute, true);
				timePickerDialog.setOnDismissListener(dissmissTimePickerDialog);

				return timePickerDialog;

			}

			case Reminder_DATE_DIALOG_ID: {

				// create a new DatePickerDialog with values you want to show
				DatePickerDialog datePickerDialog1 = new DatePickerDialog(this,
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker datePicker, int yearSelected,
												  int monthOfYear, int dayOfMonth) {

								year = yearSelected;
								month = monthOfYear + 1;
								day = dayOfMonth;
								// Set the Selected Date in Select date Button
								//btnSelectDate.setText("Date selected : "+day+"-"+month+"-"+year);

								String tempMonth = "";
								String tempDay = "";

								if (month > 9) {
									tempMonth = month + "";
								} else {
									tempMonth = "0" + month;
								}

								if (day > 9) {
									tempDay = "" + day;
								} else {
									tempDay = "0" + day;
								}


								selecteddate = tempDay + "/" + tempMonth + "/" + year;

								Toast.makeText(NoteMainActivity.this, "Selected date:" + selecteddate, Toast.LENGTH_SHORT).show();

								showDialog(Reminder_TIME_DIALOG_ID);

							}
						},
						mYear, mMonth - 1, mDay);


				datePickerDialog1.getDatePicker().setMinDate(c.getTimeInMillis());
				datePickerDialog1.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialogInterface) {

					}
				});


				return datePickerDialog1;
			}
			case Reminder_TIME_DIALOG_ID: {

				TimePickerDialog timePickerDialog = new TimePickerDialog(this,
						new TimePickerDialog.OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker view, int hourOfDay, int min) {

								mHour = hourOfDay;
								mMinute = min;
								mSeconds = c.get(Calendar.SECOND);
								TimeZone tz = c.getTimeZone();
								Log.d("Time zone", "=" + tz.getDisplayName());
								String tempHours = "";
								String tempMiutes = "";
								String tempSeconds = "";

								if (mHour > 9) {
									tempHours = mHour + "";
								} else {
									tempHours = "0" + mHour;
								}

								if (mMinute > 9) {
									tempMiutes = "" + mMinute;
								} else {
									tempMiutes = "0" + mMinute;
								}

								if (mSeconds > 9) {
									tempSeconds = "" + mSeconds;
								} else {
									tempSeconds = "0" + mSeconds;
								}


								selectedTime = tempHours + ":" + tempMiutes + ":" + tempSeconds;
								Toast.makeText(NoteMainActivity.this, "Selected Time:" + selectedTime, Toast.LENGTH_SHORT).show();


								selectedReminderdateAndTime();

							}
						}, mHour, mMinute, true);
				timePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialogInterface) {

					}
				});

				return timePickerDialog;

			}


		}
		return null;
	}

	// Register  TimePickerDialog listener
	private TimePickerDialog.OnTimeSetListener mTimeSetListener =
			new TimePickerDialog.OnTimeSetListener() {

				// the callback received when the user "sets" the TimePickerDialog in the dialog
				public void onTimeSet(TimePicker view, int hourOfDay, int min) {

					mHour = hourOfDay;
					mMinute = min;
					mSeconds = c.get(Calendar.SECOND);
					TimeZone tz = c.getTimeZone();
					Log.d("Time zone", "=" + tz.getDisplayName());
					String tempHours = "";
					String tempMiutes = "";
					String tempSeconds = "";

					if (mHour > 9) {
						tempHours = mHour + "";
					} else {
						tempHours = "0" + mHour;
					}

					if (mMinute > 9) {
						tempMiutes = "" + mMinute;
					} else {
						tempMiutes = "0" + mMinute;
					}

					if (mSeconds > 9) {
						tempSeconds = "" + mSeconds;
					} else {
						tempSeconds = "0" + mSeconds;
					}


					selectedTime = tempHours + ":" + tempMiutes + ":" + tempSeconds;
					Toast.makeText(NoteMainActivity.this, "Selected Time:" + selectedTime, Toast.LENGTH_SHORT).show();
					selecteddateAndTime();

				}
			};

	// Register  DatePickerDialog listener
	private DatePickerDialog.OnDateSetListener mDateSetListener =

			new DatePickerDialog.OnDateSetListener() {
				// the callback received when the user "sets" the Date in the DatePickerDialog
				public void onDateSet(DatePicker view, int yearSelected,
									  int monthOfYear, int dayOfMonth) {
					year = yearSelected;
					month = monthOfYear + 1;
					day = dayOfMonth;
					// Set the Selected Date in Select date Button
					//btnSelectDate.setText("Date selected : "+day+"-"+month+"-"+year);

					String tempMonth = "";
					String tempDay = "";

					if (month > 9) {
						tempMonth = month + "";
					} else {
						tempMonth = "0" + month;
					}

					if (day > 9) {
						tempDay = "" + day;
					} else {
						tempDay = "0" + day;
					}


					selecteddate = tempDay + "/" + tempMonth + "/" + year;

					Toast.makeText(NoteMainActivity.this, "Selected date:" + selecteddate, Toast.LENGTH_SHORT).show();

					showDialog(TIME_DIALOG_ID);

				}
			};
	private DatePickerDialog.OnDismissListener dissmissDatePickerDialog = new DatePickerDialog.OnDismissListener() {

		@Override
		public void onDismiss(DialogInterface dialogInterface) {
			//showDialog(TIME_DIALOG_ID);
		}
	};

	private TimePickerDialog.OnDismissListener dissmissTimePickerDialog = new TimePickerDialog.OnDismissListener() {

		@Override
		public void onDismiss(DialogInterface dialogInterface) {
			//selecteddateAndTime();
		}
	};

	void selectedReminderdateAndTime() {

		String seletecdFinal = selecteddate + " " + selectedTime;
		//Log.d("the selected dat", "" + seletecdFinal);


		SimpleDateFormat formatter = new SimpleDateFormat(
				"dd/MM/yyyy HH:mm:ss", Locale.US);

		//formatter.setTimeZone(TimeZone.getDefault());
		//formatter.setTimeZone(TimeZone.getTimeZone("GMT"));


		try {

			Date timebombdate = formatter.parse(seletecdFinal);
			String strDate = formatter.format(timebombdate);

			Toast.makeText(NoteMainActivity.this, "the final time bomb :" + strDate, Toast.LENGTH_LONG).show();

			selectedDBNoteItem.setNote_Reminder_Time(strDate);
			boolean status = androidOpenDbHelperObj.updateNoteitems_ReminderTime(selectedDBNoteItem);


			if (status == true) {
				Toast.makeText(NoteMainActivity.this,
						"Reminder  set successfully",
						Toast.LENGTH_SHORT).show();
				//getallNotes();

			} else {

				Toast.makeText(NoteMainActivity.this,
						"Reminder not set successfully",
						Toast.LENGTH_SHORT).show();
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void selecteddateAndTime() {

		String seletecdFinal = selecteddate + " " + selectedTime;
		//Log.d("the selected dat", "" + seletecdFinal);


		SimpleDateFormat formatter = new SimpleDateFormat(
				"dd/MM/yyyy HH:mm:ss", Locale.US);

		//formatter.setTimeZone(TimeZone.getDefault());
		//formatter.setTimeZone(TimeZone.getTimeZone("GMT"));


		try {

			Date timebombdate = formatter.parse(seletecdFinal);
			String strDate = formatter.format(timebombdate);

			Toast.makeText(NoteMainActivity.this, "the final time bomb :" + strDate, Toast.LENGTH_LONG).show();

			selectedDBNoteItem.setNote_TimeBomb(strDate);
			boolean status = androidOpenDbHelperObj.updateNoteitems_timeBomb(selectedDBNoteItem);


			if (status == true) {
				Toast.makeText(NoteMainActivity.this,
						"time bomb set successfully",
						Toast.LENGTH_SHORT).show();
				//getallNotes();

			} else {

				Toast.makeText(NoteMainActivity.this,
						"timebomb not set successfully",
						Toast.LENGTH_SHORT).show();
			}


		} catch (ParseException e) {
			e.printStackTrace();
		}
	}



	void showAlertToLockNUnlockNote(final DBNoteItems items, Context context, final boolean islock, final boolean isOpenLockedNote) {

		final Dialog dialog = new Dialog(context);

		final StringBuilder lock = new StringBuilder(4);

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// inflate your activity layout here!
		View contentView = inflater.inflate(R.layout.notesharelockactivity, null, false);


		final TextView textLock = (TextView) contentView.findViewById(R.id.textViewLock);
		TextView headerText = (TextView) contentView.findViewById(R.id.headerText);

		final Button btn1 = (Button) contentView.findViewById(R.id.button);
		final Button btn2 = (Button) contentView.findViewById(R.id.button1);
		final Button btn3 = (Button) contentView.findViewById(R.id.button2);
		final Button btn4 = (Button) contentView.findViewById(R.id.button3);
		final Button btn5 = (Button) contentView.findViewById(R.id.button4);
		final Button btn6 = (Button) contentView.findViewById(R.id.button5);
		final Button btn7 = (Button) contentView.findViewById(R.id.button6);
		final Button btn8 = (Button) contentView.findViewById(R.id.button7);
		final Button btn9 = (Button) contentView.findViewById(R.id.button8);
		final Button btn10 = (Button) contentView.findViewById(R.id.button9);
		final Button btn11 = (Button) contentView.findViewById(R.id.button10);
		final Button btn12 = (Button) contentView.findViewById(R.id.button11);

		preference = PreferenceManager.getDefaultSharedPreferences(NoteMainActivity.this);
		if (!preference.getString(SAVELOCK, "").equalsIgnoreCase("")) {

			textLock.setHint("Unlock eNote");

		} else {

			textLock.setHint("Create password");
		}

		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				lock.append(btn1.getText());

				//textLock.setText(lock.toString());
				updateLockLabel(textLock, lock.toString(), dialog, islock, isOpenLockedNote);
			}
		});
		btn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				lock.append(btn2.getText());

				//textLock.setText(lock.toString());

				updateLockLabel(textLock, lock.toString(), dialog, islock, isOpenLockedNote);
			}
		});
		btn3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				lock.append(btn3.getText());

				//textLock.setText(lock.toString());

				updateLockLabel(textLock, lock.toString(), dialog, islock, isOpenLockedNote);
			}
		});

		btn4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				lock.append(btn4.getText());

				//textLock.setText(lock.toString());

				updateLockLabel(textLock, lock.toString(), dialog, islock, isOpenLockedNote);
			}
		});
		btn5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				lock.append(btn5.getText());

				//textLock.setText(lock.toString());

				updateLockLabel(textLock, lock.toString(), dialog, islock, isOpenLockedNote);
			}
		});

		btn6.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				lock.append(btn6.getText());

				//textLock.setText(lock.toString());

				updateLockLabel(textLock, lock.toString(), dialog, islock, isOpenLockedNote);
			}
		});

		btn7.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				lock.append(btn7.getText());

				//textLock.setText(lock.toString());

				updateLockLabel(textLock, lock.toString(), dialog, islock, isOpenLockedNote);
			}
		});
		btn8.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				lock.append(btn8.getText());

				//textLock.setText(lock.toString());

				updateLockLabel(textLock, lock.toString(), dialog, islock, isOpenLockedNote);
			}
		});
		btn9.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				lock.append(btn9.getText());

				//textLock.setText(lock.toString());

				updateLockLabel(textLock, lock.toString(), dialog, islock, isOpenLockedNote);
			}
		});
		btn11.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				lock.append(btn11.getText());

				//textLock.setText(lock.toString());

				updateLockLabel(textLock, lock.toString(), dialog, islock, isOpenLockedNote);
			}
		});

		btn10.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {


				dialog.dismiss();


			}
		});

		btn12.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {



				textLock.setText("");

				lock.delete(0, lock.length());

			}
		});


		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(true);

		dialog.setContentView(contentView);
		dialog.show();

	}

	void updateLockLabel(TextView textLock, String strLock, Dialog dialog, final boolean islock, boolean isOpenLockedNote) {

		textLock.setText(strLock);
		if (textLock.getText().length() == 4) {
			preference = PreferenceManager.getDefaultSharedPreferences(NoteMainActivity.this);
			if (!preference.getString(SAVELOCK, "").equalsIgnoreCase("")) {
				if (preference.getString(SAVELOCK, "").equalsIgnoreCase(textLock.getText().toString())) {

					Toast.makeText(NoteMainActivity.this, "Password matched,note Open.", Toast.LENGTH_SHORT).show();
					dialog.dismiss();

					if (isOpenLockedNote == false) {

						if (islock == true) {
							selectedDBNoteItem.setNote_Lock_Status("1");
							boolean status = androidOpenDbHelperObj.updateNoteitems_Notelock(selectedDBNoteItem);


							if (status == true) {

								Toast.makeText(NoteMainActivity.this, "Note locked", Toast.LENGTH_SHORT).show();

							} else {

								Toast.makeText(NoteMainActivity.this, "Note not locked", Toast.LENGTH_SHORT).show();
							}


							//getallNotes();

						} else {

							selectedDBNoteItem.setNote_Lock_Status("0");
							boolean status = androidOpenDbHelperObj.updateNoteitems_Notelock(selectedDBNoteItem);


							if (status == true) {

								Toast.makeText(NoteMainActivity.this, "Note unlocked", Toast.LENGTH_SHORT).show();

							} else {

								Toast.makeText(NoteMainActivity.this, "Note not unlocked", Toast.LENGTH_SHORT).show();
							}

							//getallNotes();
						}
					} else {

						DataManager.sharedDataManager().setSeletedDBNoteItem(selectedDBNoteItem);
						startActivity(new Intent(NoteMainActivity.this, NoteMainActivity.class));
					}

				} else {
					//textLock.setText("");

					Toast.makeText(NoteMainActivity.this, "Password not matched", Toast.LENGTH_SHORT).show();
					dialog.dismiss();
				}

			} else {
				SharedPreferences.Editor editor = preference.edit();
				editor.putString(SAVELOCK, textLock.getText() + "");
				editor.commit();

				if (islock == true) {
					selectedDBNoteItem.setNote_Lock_Status("1");
					boolean status = androidOpenDbHelperObj.updateNoteitems_Notelock(selectedDBNoteItem);


					if (status == true) {

						Toast.makeText(NoteMainActivity.this, "Note locked", Toast.LENGTH_SHORT).show();

					} else {

						Toast.makeText(NoteMainActivity.this, "Note not locked", Toast.LENGTH_SHORT).show();
					}

				} else {

					selectedDBNoteItem.setNote_Lock_Status("0");
					boolean status = androidOpenDbHelperObj.updateNoteitems_Notelock(selectedDBNoteItem);


					if (status == true) {

						Toast.makeText(NoteMainActivity.this, "Note unlocked", Toast.LENGTH_SHORT).show();

					} else {

						Toast.makeText(NoteMainActivity.this, "Note not unlocked", Toast.LENGTH_SHORT).show();
					}
				}
				dialog.dismiss();

			}
		}
	}
}
