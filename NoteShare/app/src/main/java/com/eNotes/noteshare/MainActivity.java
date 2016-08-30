package com.eNotes.noteshare;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.eNotes.adpters.FolderListingAdapter;
import com.eNotes.adpters.NoteFolderGridAdapter;
import com.eNotes.adpters.NoteListAdapter_1;
import com.eNotes.dataAccess.DataManager;
import com.eNotes.datamodels.SideMenuitems;
import com.eNotes.notesharedatabase.DBNoteItemElement;
import com.eNotes.notesharedatabase.DBNoteItems;
import com.eNotes.notesharedatabase.NoteshareDatabaseHelper;
import com.mobiapp.ventures.eNotes.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


enum SORTTYPE {
	ALPHABET, COLOURS, CREATED_TIME, MODIFIED_TIME, REMINDER_TIME, TIME_BOMB
};

public class MainActivity extends DrawerActivity  {

	public ImageButton imageButtonHamburg, imageButtoncalander,
			imageButtonsquence;
	public TextView textViewheaderTitle;
	public RelativeLayout layoutHeader;

	NoteshareDatabaseHelper androidOpenDbHelperObj;
	SQLiteDatabase sqliteDatabase;

	public ImageButton textViewAdd;
	public ListView notefoleserList;
	public GridView notefoleserGridList;
	public ScrollView notefoleserPintrestList;

	public LinearLayout Layout1;
	public LinearLayout Layout2;

	//public NoteFolderAdapter adapter;

	public NoteListAdapter_1 adapter;
	public NoteFolderGridAdapter gridAdapter;
	
	public ArrayList<SideMenuitems> arrDataNote;
	public ArrayList<DBNoteItems> arrDataFolder;;
	public ArrayList<DBNoteItems> arrDBDataNote;

	
	final Context context = this;
	public TextView textNoteSort, textNoteView;

	public SORTTYPE sortType;

	public  DBNoteItems selectedDbItem;

	public FolderListingAdapter folderListingAdapter;

	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID=1;

	private int mYear, mMonth, mDay,mHour,mMinute,mSeconds;
	public  int year,month,day,hour,minute,seconds;

	public  String selecteddate,selectedTime,timeZone;
	public  Calendar c;

	SharedPreferences preference;
	private android.widget.RelativeLayout.LayoutParams layoutParams;

	static String SAVELOCK="LOCK";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);

		preference=getSharedPreferences(DataManager.Mylock,Context.MODE_PRIVATE);

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// inflate your activity layout here!
		View contentView = inflater
				.inflate(R.layout.activity_main_1, null, false);
		mDrawerLayout.addView(contentView, 0);

		androidOpenDbHelperObj = new NoteshareDatabaseHelper(context);
		sqliteDatabase = androidOpenDbHelperObj.getWritableDatabase();

		DataManager.sharedDataManager().setSelectedIndex(-1);
		initlizeUIElement(contentView);



		initilizesDateAndTime();

		getallNotes();
		getAllFolder();

	}


void  initilizesDateAndTime()
{

	c = Calendar.getInstance();
	mYear = c.get(Calendar.YEAR);
	mMonth = c.get(Calendar.MONTH);
	mDay = c.get(Calendar.DAY_OF_MONTH);
	mHour = c.get(Calendar.HOUR_OF_DAY);
	mMinute = c.get(Calendar.MINUTE);
	mSeconds = c.get(Calendar.SECOND);

	mMonth=mMonth+1;

	TimeZone tz = c.getTimeZone();
	Log.d("Time zone","="+tz.getDisplayName());

	String tempMonth="";
	String tempDay="";

	if (mMonth>9)
	{
		tempMonth=mMonth+"";
	}else
	{
		tempMonth="0"+mMonth;
	}

	if (mDay>9)
	{
		tempDay=""+mDay;
	}
	else
	{
		tempDay="0"+mDay;
	}

	String tempHours="";
	String tempMiutes="";
	String tempSeconds="";

	if (mHour>9)
	{
		tempHours=mHour+"";
	}else
	{
		tempHours="0"+mHour;
	}

	if (mMinute>9)
	{
		tempMiutes=""+mMinute;
	}
	else
	{
		tempMiutes="0"+mMinute;
	}

	if (mSeconds>9)
	{
		tempSeconds=""+mSeconds;
	}
	else
	{
		tempSeconds="0"+mSeconds;
	}


	selecteddate=tempDay+"/"+tempMonth+"/"+mYear;

	selectedTime=tempHours+":"+tempMiutes+":"+tempSeconds;


}


	// Method automatically gets Called when you call showDialog()  method
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DATE_DIALOG_ID:
				// create a new DatePickerDialog with values you want to show
				DatePickerDialog datePickerDialog=new DatePickerDialog(this,
						mDateSetListener,
						mYear, mMonth-1, mDay);

				datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
				datePickerDialog.setOnDismissListener(dissmissDatePickerDialog);



				return datePickerDialog;

			// create a new TimePickerDialog with values you want to show
			case TIME_DIALOG_ID:
			{

				TimePickerDialog timePickerDialog=new TimePickerDialog(this,
						mTimeSetListener, mHour, mMinute, true);
				timePickerDialog.setOnDismissListener(dissmissTimePickerDialog);

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
					mSeconds=c.get(Calendar.SECOND);




					TimeZone tz = c.getTimeZone();
					Log.d("Time zone","="+tz.getDisplayName());



					String tempHours="";
					String tempMiutes="";
					String tempSeconds="";

					if (mHour>9)
					{
						tempHours=mHour+"";
					}else
					{
						tempHours="0"+mHour;
					}

					if (mMinute>9)
					{
						tempMiutes=""+mMinute;
					}
					else
					{
						tempMiutes="0"+mMinute;
					}

					if (mSeconds>9)
					{
						tempSeconds=""+mSeconds;
					}
					else
					{
						tempSeconds="0"+mSeconds;
					}


					selectedTime=tempHours+":"+tempMiutes+":"+tempSeconds;
					Toast.makeText(MainActivity.this,"Selected Time:"+selectedTime,Toast.LENGTH_SHORT).show();

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
					month = monthOfYear+1;
					day = dayOfMonth;
					// Set the Selected Date in Select date Button
					//btnSelectDate.setText("Date selected : "+day+"-"+month+"-"+year);

					String tempMonth="";
					String tempDay="";

					if (month>9)
					{
						tempMonth=month+"";
					}else
					{
						tempMonth="0"+month;
					}

					if (day>9)
					{
						tempDay=""+day;
					}
					else
					{
						tempDay="0"+day;
					}


					selecteddate=tempDay+"/"+tempMonth+"/"+year;

					Toast.makeText(MainActivity.this,"Selected date:"+selecteddate,Toast.LENGTH_SHORT).show();

					showDialog(TIME_DIALOG_ID);

				}
			};
	private DatePickerDialog.OnDismissListener dissmissDatePickerDialog=new DatePickerDialog.OnDismissListener()
	{

		@Override
		public void onDismiss(DialogInterface dialogInterface)
		{
			//showDialog(TIME_DIALOG_ID);
		}
	};

	private TimePickerDialog.OnDismissListener dissmissTimePickerDialog=new TimePickerDialog.OnDismissListener()
	{

		@Override
		public void onDismiss(DialogInterface dialogInterface)
		{
				//selecteddateAndTime();
		}
	};

	void  selecteddateAndTime() {

		String seletecdFinal = selecteddate + " " + selectedTime;
		//Log.d("the selected dat", "" + seletecdFinal);



		SimpleDateFormat formatter = new SimpleDateFormat(
				"dd/MM/yyyy HH:mm:ss", Locale.US);

		//formatter.setTimeZone(TimeZone.getDefault());
		//formatter.setTimeZone(TimeZone.getTimeZone("GMT"));



		try {

			Date timebombdate = formatter.parse(seletecdFinal);
			String strDate = formatter.format(timebombdate);

			Toast.makeText(MainActivity.this, "the final time bomb :" + strDate, Toast.LENGTH_LONG).show();

			selectedDbItem.setNote_TimeBomb(strDate);
			boolean status=androidOpenDbHelperObj.updateNoteitems_timeBomb(selectedDbItem);


			if (status == true)
			{
				Toast.makeText(MainActivity.this,
						"time bomb set successfully",
						Toast.LENGTH_SHORT).show();
				getallNotes();

			} else {

				Toast.makeText(MainActivity.this,
						"timebomb not set successfully",
						Toast.LENGTH_SHORT).show();
			}


		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void btnCallbacks(Object data)
	{
		System.out.println("the tag us" + data);
		DataManager.sharedDataManager().setSelectedIndex(-1);
		adapter.notifyDataSetChanged();

	}

	void initlizeUIElement(View contentview) {

		DataManager.sharedDataManager().setTypeofListView(false);

		layoutHeader = (RelativeLayout) contentview
				.findViewById(R.id.mainHeadermenue);
		textViewheaderTitle = (TextView) layoutHeader
				.findViewById(R.id.textViewheaderTitle);
		// textViewheaderTitle.setTypeface(NoteShareFonts.asTypeface(MainActivity.this,
		// NoteShareFonts.arial));

		imageButtoncalander = (ImageButton) layoutHeader
				.findViewById(R.id.imageButtoncalander);
		imageButtonHamburg = (ImageButton) layoutHeader
				.findViewById(R.id.imageButtonHamburg);
		imageButtonsquence = (ImageButton) layoutHeader
				.findViewById(R.id.imageButtonsquence);

		textNoteSort = (TextView) findViewById(R.id.textNoteSort);
		textNoteView = (TextView) findViewById(R.id.textNoteView);

		textViewAdd = (ImageButton) findViewById(R.id.textViewAdd);
		notefoleserList = (ListView) findViewById(R.id.notefoleserList);
		arrDataNote = new ArrayList<SideMenuitems>();
		arrDBDataNote= new ArrayList<DBNoteItems>();

		notefoleserGridList = (GridView) findViewById(R.id.notefoleserGridList);
		notefoleserPintrestList = (ScrollView) findViewById(R.id.notefoleserPintrestList);
		Layout1 = (LinearLayout) findViewById(R.id.Layout1);
		Layout2 = (LinearLayout) findViewById(R.id.Layout2);

		// Grid adapter

		gridAdapter = new NoteFolderGridAdapter(this, arrDataNote);
		notefoleserGridList.setAdapter(gridAdapter);

		// list adapter

		adapter = new NoteListAdapter_1(this, arrDataNote);
		notefoleserList.setAdapter(adapter);

		addlistners();


		textViewAdd.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view)
			{


				ClipData.Item item = new ClipData.Item((CharSequence)view.getTag());
				String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

				ClipData dragData = new ClipData(view.getTag().toString(),mimeTypes, item);
				View.DragShadowBuilder myShadow = new View.DragShadowBuilder(textViewAdd);

				view.startDrag(dragData,myShadow,null,0);
				return true;


			}
		});


		textViewAdd.setOnDragListener(new View.OnDragListener() {
			@Override
			public boolean onDrag(View v, DragEvent event) {
				switch(event.getAction())
				{
					case DragEvent.ACTION_DRAG_STARTED:
						layoutParams = (RelativeLayout.LayoutParams)v.getLayoutParams();
						Log.d("DRAG", "Action is DragEvent.ACTION_DRAG_STARTED");

						// Do nothing
						break;

					case DragEvent.ACTION_DRAG_ENTERED:
						Log.d("DRAG", "Action is DragEvent.ACTION_DRAG_ENTERED");
						int x_cord = (int) event.getX();
						int y_cord = (int) event.getY();
						break;

					case DragEvent.ACTION_DRAG_EXITED :
						Log.d("DRAG", "Action is DragEvent.ACTION_DRAG_EXITED");
						x_cord = (int) event.getX();
						y_cord = (int) event.getY();
						layoutParams.leftMargin = x_cord;
						layoutParams.topMargin = y_cord;
						v.setLayoutParams(layoutParams);
						break;

					case DragEvent.ACTION_DRAG_LOCATION  :
						Log.d("DRAG", "Action is DragEvent.ACTION_DRAG_LOCATION");
						x_cord = (int) event.getX();
						y_cord = (int) event.getY();
						break;

					case DragEvent.ACTION_DRAG_ENDED   :
						Log.d("DRAG", "Action is DragEvent.ACTION_DRAG_ENDED");

						// Do nothing
						break;

					case DragEvent.ACTION_DROP:
						Log.d("DRAG", "ACTION_DROP event");

						// Do nothing
						break;
					default: break;
				}
				return true;
			}
		});

		/*textViewAdd.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					ClipData data = ClipData.newPlainText("", "");
					View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(textViewAdd);

					textViewAdd.startDrag(data, shadowBuilder, textViewAdd, 0);
					textViewAdd.setVisibility(View.INVISIBLE);
					return true;
				}
				else
				{
					return false;
				}
			}
		});*/




		// getDeafultNote();
	}

	void updatePintrestView() {
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// inflate your activity layout here!

		sortingArray();
		Layout1.removeAllViews();
		Layout2.removeAllViews();

		for (int i = 0; i < arrDataNote.size(); i++) {

			View contentView = inflater.inflate(R.layout.notefoldepintrestrow,
					null, false);



			TextView textViewSlideMenuName = (TextView) contentView
					.findViewById(R.id.textViewSlideMenuName);
			TextView textViewSlideMenuNameSubTitle = (TextView) contentView
					.findViewById(R.id.textViewSlideMenuNameSubTitle);
			View layoutsepreter = (View) contentView
					.findViewById(R.id.layoutsepreter);
			layoutsepreter.setVisibility(View.VISIBLE);

			SideMenuitems model = arrDataNote.get(i);
			textViewSlideMenuName.setText(model.getMenuName());
			textViewSlideMenuNameSubTitle.setText(model.getMenuNameDetail());

			if (i % 2 == 0) {
				Layout1.addView(contentView);
				contentView.setBackgroundColor(Color.parseColor(model
						.getColours()));
			} else {
				Layout2.addView(contentView);
				contentView.setBackgroundColor(Color.parseColor(model
						.getColours()));
			}

		}

	}

	void sortingArray() {
		switch (sortType) {
		case ALPHABET: {
			
			//DB note element sorting
			
			Collections.sort(arrDBDataNote, new Comparator<DBNoteItems>() {

				@Override
				public int compare(DBNoteItems lhs, DBNoteItems rhs) {
					// TODO Auto-generated method stub
					return lhs.getNote_Title().compareToIgnoreCase(
							rhs.getNote_Title());
				}
			});
			
			
			
			Collections.sort(arrDataNote, new Comparator<SideMenuitems>() {

				@Override
				public int compare(SideMenuitems lhs, SideMenuitems rhs) {
					// TODO Auto-generated method stub
					return lhs.getMenuName().compareToIgnoreCase(
							rhs.getMenuName());
				}
			});

		}
			break;
		case COLOURS: {
			
			
	//DB note element sorting
			
			Collections.sort(arrDBDataNote, new Comparator<DBNoteItems>() {

				@Override
				public int compare(DBNoteItems lhs, DBNoteItems rhs) {
					// TODO Auto-generated method stub
					return lhs.getNote_Color().compareToIgnoreCase(
							rhs.getNote_Color());
				}
			});
			
			Collections.sort(arrDataNote, new Comparator<SideMenuitems>() {

				@Override
				public int compare(SideMenuitems lhs, SideMenuitems rhs) {
					// TODO Auto-generated method stub
					return lhs.getColours().compareToIgnoreCase(
							rhs.getColours());
				}
			});
		}
			break;
		case CREATED_TIME: 
		{

		}
			break;
		case MODIFIED_TIME: 
		{

		}
			break;
		case REMINDER_TIME: 
		{

		}
			break;
		case TIME_BOMB: {

		}
			break;

		default:
			break;
		}
	}

	void updateGridView() {

		gridAdapter.notifyDataSetChanged();
	}

	//void getDeafultNote()

	void addlistners() {

		textNoteSort.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showActionSheet_sort(arg0);

			}
		});
		textNoteView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showActionSheet(v);

			}
		});

		imageButtoncalander.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		imageButtonHamburg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openSlideMenu();

			}
		});
		imageButtonsquence.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		textViewAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// startActivity(new Intent(context, NoteMainActivity.class));

				showAlertWithEditText(MainActivity.this);

			}
		});

		notefoleserList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			SideMenuitems menuItem=arrDataNote.get(position);
			
			
			
			if (menuItem.isIsdefaultNote()!=true)
			{
				DBNoteItems noteItems=arrDBDataNote.get(position);
				
				DataManager.sharedDataManager().setSeletedListNoteItem(menuItem);
				DataManager.sharedDataManager().setSeletedDBNoteItem(noteItems);
				startActivity(new Intent(context, NoteMainActivity.class));
			}
			else
			{
				Toast.makeText(MainActivity.this, "Default Note can't be open", Toast.LENGTH_SHORT).show();
			}
			
				
				
			}
		});

		notefoleserList
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						// TODO Auto-generated method stub
						if (DataManager.sharedDataManager().getSelectedIndex() == arg2) {
							DataManager.sharedDataManager()
									.setSelectedIndex(-1);
						} else {
							DataManager.sharedDataManager().setSelectedIndex(
									arg2);
						}

						adapter.notifyDataSetChanged();
						return true;
					}
				});
	}

	@Override
	public void onBackPressed() {

		showAlertWith("Are you sure,Do you want to quit the app?",
				MainActivity.this);

	}


	void showAlertWithDeleteMessage(String message, Context context, final DBNoteItems dbNoteItems)
	{

		final Dialog dialog = new Dialog(context);

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// inflate your activity layout here!
		View contentView = inflater.inflate(R.layout.alert_view, null, false);

		TextView textViewTitleAlert = (TextView) contentView
				.findViewById(R.id.textViewTitleAlert);
		textViewTitleAlert.setText(""+dbNoteItems.getNote_Title());
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
				// TODO Auto-generated method stub
				dialog.dismiss();

			}
		});
		buttonAlertOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			boolean status=androidOpenDbHelperObj.deleteNote(dbNoteItems.getNote_Id());
				if (status==true)
				{

					Toast.makeText(MainActivity.this,"Note deleted successfully",Toast.LENGTH_SHORT).show();
					getallNotes();
				}
				else
				{
					Toast.makeText(MainActivity.this,"Note deleted unsuccessfully",Toast.LENGTH_SHORT).show();

				}
				dialog.dismiss();

			}
		});

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(contentView);
		dialog.show();

	}



	void showMoveToFolderDialog(String message, Context context,final  DBNoteItems items)
	{


		folderListingAdapter=new FolderListingAdapter(MainActivity.this,arrDataFolder);
		final Dialog dialog = new Dialog(context);

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// inflate your activity layout here!
		View contentView = inflater.inflate(R.layout.folderlisting_view, null, false);

		TextView textViewTitleAlert = (TextView) contentView
				.findViewById(R.id.textViewTitleAlert);
		textViewTitleAlert.setText("MOVE TO FOLDER");
		textViewTitleAlert.setTextColor(Color.WHITE);

		ListView listfolderView=(ListView)contentView.findViewById(R.id.listViewFolderList);
		listfolderView.setAdapter(folderListingAdapter);
		folderListingAdapter.notifyDataSetChanged();


		listfolderView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

				DBNoteItems itemSelected = arrDataFolder.get(i);
				items.setNote_Folder_Id(itemSelected.getNote_Id());

				boolean status = androidOpenDbHelperObj.updateNoteitems_MoveToFolder(items);

				if (status == true) {

					Toast.makeText(MainActivity.this, "Note moved successfully", Toast.LENGTH_SHORT).show();
					getallNotes();
				} else {
					Toast.makeText(MainActivity.this, " move unsuccessfully", Toast.LENGTH_SHORT).show();

				}
				dialog.dismiss();

			}
		});



		Button buttonAlertCancel = (Button) contentView
				.findViewById(R.id.buttonAlertCancel);
		Button buttonAlertOk = (Button) contentView
				.findViewById(R.id.buttonAlertOk);
		buttonAlertCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
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


	void showAlertToLockNUnlockNote(final DBNoteItems items, Context context, final boolean islock, final boolean isOpenLockedNote)
	{




	/*
		dialog = new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.loading_screen);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams wlp = window.getAttributes();

		wlp.gravity = Gravity.CENTER;
		wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
		window.setAttributes(wlp);
		dialog.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.MATCH_PARENT);
		dialog.show();*/

		//final Dialog dialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar);



		final Dialog dialog = new Dialog(context);

		final StringBuilder lock = new StringBuilder(4);

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// inflate your activity layout here!
		View contentView = inflater.inflate(R.layout.notesharelockactivity, null, false);


		final TextView textLock=(TextView)contentView.findViewById(R.id.textViewLock);
		TextView headerText=(TextView)contentView.findViewById(R.id.headerText);

		final Button btn1=(Button)contentView.findViewById(R.id.button);
		final Button btn2=(Button)contentView.findViewById(R.id.button1);
		final Button btn3=(Button)contentView.findViewById(R.id.button2);
		final Button btn4=(Button)contentView.findViewById(R.id.button3);
		final Button btn5=(Button)contentView.findViewById(R.id.button4);
		final Button btn6=(Button)contentView.findViewById(R.id.button5);
		final Button btn7=(Button)contentView.findViewById(R.id.button6);
		final Button btn8=(Button)contentView.findViewById(R.id.button7);
		final Button btn9=(Button)contentView.findViewById(R.id.button8);
		final Button btn10=(Button)contentView.findViewById(R.id.button9);
		final Button btn11=(Button)contentView.findViewById(R.id.button10);
		final Button btn12=(Button)contentView.findViewById(R.id.button11);



		preference= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
		if (!preference.getString(SAVELOCK,"").equalsIgnoreCase(""))
		{


			textLock.setHint("Password to Unlock Note");

		}else
		{

			textLock.setHint("Create password to Lock Note");
		}

		btn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				lock.append(btn1.getText());

				//textLock.setText(lock.toString());
				updateLockLabel(textLock,lock.toString(),dialog,islock,isOpenLockedNote);
			}
		});
		btn2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				lock.append(btn2.getText());

				//textLock.setText(lock.toString());

				updateLockLabel(textLock, lock.toString(),dialog,islock,isOpenLockedNote);
			}
		});
		btn3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				lock.append(btn3.getText());

				//textLock.setText(lock.toString());

				updateLockLabel(textLock,lock.toString(),dialog,islock,isOpenLockedNote);
			}
		});

		btn4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				lock.append(btn4.getText());

				//textLock.setText(lock.toString());

				updateLockLabel(textLock,lock.toString(),dialog,islock,isOpenLockedNote);
			}
		});
		btn5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				lock.append(btn5.getText());

				//textLock.setText(lock.toString());

				updateLockLabel(textLock,lock.toString(),dialog,islock,isOpenLockedNote);
			}
		});

		btn6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				lock.append(btn6.getText());

				//textLock.setText(lock.toString());

				updateLockLabel(textLock,lock.toString(),dialog,islock,isOpenLockedNote);
			}
		});

		btn7.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				lock.append(btn7.getText());

				//textLock.setText(lock.toString());

				updateLockLabel(textLock,lock.toString(),dialog,islock,isOpenLockedNote);
			}
		});
		btn8.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				lock.append(btn8.getText());

				//textLock.setText(lock.toString());

				updateLockLabel(textLock,lock.toString(),dialog,islock,isOpenLockedNote);
			}
		});
		btn9.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				lock.append(btn9.getText());

				//textLock.setText(lock.toString());

				updateLockLabel(textLock,lock.toString(),dialog,islock,isOpenLockedNote);
			}
		});
		btn11.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				lock.append(btn11.getText());

				//textLock.setText(lock.toString());

				updateLockLabel(textLock,lock.toString(),dialog,islock,isOpenLockedNote);
			}
		});

		btn10.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				//lock.append(btn2.getText());

				//textLock.setText(lock.toString());

				//updateLockLabel(textLock,lock.toString());
				//Exit

				dialog.dismiss();



			}
		});

		btn12.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				//lock.append(btn2.getText());

				//textLock.setText(lock.toString());

				//updateLockLabel(textLock,lock.toString());
				//clear


				//dialog.dismiss();

				textLock.setText("");

				lock.delete(0,lock.length());

			}
		});







		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		/*Window window = dialog.getWindow();
		WindowManager.LayoutParams wlp = window.getAttributes();

		wlp.gravity = Gravity.CENTER;
		wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
		window.setAttributes(wlp);
		dialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);*/



		dialog.setCancelable(true);

		dialog.setContentView(contentView);
		dialog.show();

	}
	void updateLockLabel(TextView textLock,String strLock,Dialog dialog,final boolean islock,boolean isOpenLockedNote)
	{


		textLock.setText(strLock);;

		if (textLock.getText().length()==4)
		{



			preference= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
			if (!preference.getString(SAVELOCK,"").equalsIgnoreCase(""))
			{
				if (preference.getString(SAVELOCK,"").equalsIgnoreCase(textLock.getText().toString()))
				{

					Toast.makeText(MainActivity.this, "Password matched,note Open.", Toast.LENGTH_SHORT).show();
					dialog.dismiss();

					if (isOpenLockedNote==false)
					{

						if (islock==true)
						{
							selectedDbItem.setNote_Lock_Status("1");
							boolean status=androidOpenDbHelperObj.updateNoteitems_Notelock(selectedDbItem);


							if (status==true)
							{

								Toast.makeText(MainActivity.this, "Note locked", Toast.LENGTH_SHORT).show();

							}else
							{

								Toast.makeText(MainActivity.this, "Note Not locked", Toast.LENGTH_SHORT).show();
							}


							getallNotes();

						}else
						{

							selectedDbItem.setNote_Lock_Status("0");
							boolean status=androidOpenDbHelperObj.updateNoteitems_Notelock(selectedDbItem);


							if (status==true)
							{

								Toast.makeText(MainActivity.this, "Note unlocked", Toast.LENGTH_SHORT).show();

							}else
							{

								Toast.makeText(MainActivity.this, "Note Not Unlocked", Toast.LENGTH_SHORT).show();
							}

							getallNotes();
						}
					}
					else {

						DataManager.sharedDataManager().setSeletedDBNoteItem(selectedDbItem);
						startActivity(new Intent(context, NoteMainActivity.class));
					}

				}
				else
				{
					//textLock.setText("");

					Toast.makeText(MainActivity.this,"Password not matched",Toast.LENGTH_SHORT).show();
					dialog.dismiss();
				}

			}else
			{
				SharedPreferences.Editor editor=preference.edit();
				editor.putString(SAVELOCK, textLock.getText() + "");
				editor.commit();

//				selectedDbItem.setNote_Lock_Status("1");
//				boolean status=androidOpenDbHelperObj.updateNoteitems_Notelock(selectedDbItem);
//
//
//				if (status==true)
//				{
//
//					Toast.makeText(MainActivity.this, "Password Created,Note locked", Toast.LENGTH_SHORT).show();
//
//				}else
//				{
//
//					Toast.makeText(MainActivity.this, "Password Created,Note Not locked", Toast.LENGTH_SHORT).show();
//				}


				if (islock==true)
				{
					selectedDbItem.setNote_Lock_Status("1");
					boolean status=androidOpenDbHelperObj.updateNoteitems_Notelock(selectedDbItem);


					if (status==true)
					{

						Toast.makeText(MainActivity.this, "Note locked", Toast.LENGTH_SHORT).show();

					}else
					{

						Toast.makeText(MainActivity.this, "Note Not locked", Toast.LENGTH_SHORT).show();
					}

				}else
				{

					selectedDbItem.setNote_Lock_Status("0");
					boolean status=androidOpenDbHelperObj.updateNoteitems_Notelock(selectedDbItem);


					if (status==true)
					{

						Toast.makeText(MainActivity.this, "Note unlocked", Toast.LENGTH_SHORT).show();

					}else
					{

						Toast.makeText(MainActivity.this, "Note Not Unlocked", Toast.LENGTH_SHORT).show();
					}
				}



				dialog.dismiss();

			}




		}
	}




	void showAlertWith(String message, Context context) {

		final Dialog dialog = new Dialog(context);

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// inflate your activity layout here!
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
				// TODO Auto-generated method stub
				dialog.dismiss();

			}
		});
		buttonAlertOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				System.exit(0);

			}
		});

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);

		dialog.setContentView(contentView);
		dialog.show();

	}

	// showActionSheet
	// ---------------------------------------------------------------------------------------

	public void showActionSheet(View v) {

		final Dialog myDialog = new Dialog(MainActivity.this,
				R.style.CustomTheme);

		myDialog.setContentView(R.layout.actionsheet);
		Button buttonDissmiss = (Button) myDialog
				.findViewById(R.id.buttonDissmiss);

		LinearLayout layoutList = (LinearLayout) myDialog
				.findViewById(R.id.layoutList);
		TextView layoutListTextView = (TextView) layoutList
				.findViewById(R.id.textViewSlideMenuName);
		ImageView layoutListImageView = (ImageView) layoutList
				.findViewById(R.id.imageViewSlidemenu);
		layoutListImageView.setImageResource(R.drawable.list_view_logo);
		layoutListTextView.setText("List");

		LinearLayout layoutDetail = (LinearLayout) myDialog
				.findViewById(R.id.layoutDetail);
		TextView layoutDetailTextView = (TextView) layoutDetail
				.findViewById(R.id.textViewSlideMenuName);
		ImageView layoutDetailImageView = (ImageView) layoutDetail
				.findViewById(R.id.imageViewSlidemenu);
		layoutDetailImageView.setImageResource(R.drawable.detail_view_logo);
		layoutDetailTextView.setText("Details");

		LinearLayout layoutPintrest = (LinearLayout) myDialog
				.findViewById(R.id.layoutPintrest);
		TextView layoutPintrestTextView = (TextView) layoutPintrest
				.findViewById(R.id.textViewSlideMenuName);
		ImageView layoutPintrestImageView = (ImageView) layoutPintrest
				.findViewById(R.id.imageViewSlidemenu);
		layoutPintrestImageView.setImageResource(R.drawable.pintrest_view_logo);
		layoutPintrestTextView.setText("Shuffle");

		LinearLayout layoutGrid = (LinearLayout) myDialog
				.findViewById(R.id.layoutGrid);
		TextView layoutGridTextView = (TextView) layoutGrid
				.findViewById(R.id.textViewSlideMenuName);
		ImageView layoutGridImageView = (ImageView) layoutGrid
				.findViewById(R.id.imageViewSlidemenu);
		layoutGridImageView.setImageResource(R.drawable.grid_view_logo);
		layoutGridTextView.setText("Tiles");

		layoutGridTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				notefoleserGridList.setVisibility(View.VISIBLE);
				notefoleserList.setVisibility(View.GONE);
				notefoleserPintrestList.setVisibility(View.GONE);
				myDialog.dismiss();

			}
		});

		layoutPintrestTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				notefoleserGridList.setVisibility(View.GONE);
				notefoleserList.setVisibility(View.GONE);
				notefoleserPintrestList.setVisibility(View.VISIBLE);

				updatePintrestView();
				myDialog.dismiss();

			}
		});

		layoutDetailTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				DataManager.sharedDataManager().setTypeofListView(false);
				adapter.notifyDataSetChanged();
				// TODO Auto-generated method stub
				notefoleserGridList.setVisibility(View.GONE);
				notefoleserList.setVisibility(View.VISIBLE);
				notefoleserPintrestList.setVisibility(View.GONE);

				DataManager.sharedDataManager().setSelectedIndex(-1);
				adapter.notifyDataSetChanged();
				myDialog.dismiss();

			}
		});

		layoutListTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DataManager.sharedDataManager().setTypeofListView(true);
				adapter.notifyDataSetChanged();
				notefoleserGridList.setVisibility(View.GONE);
				notefoleserList.setVisibility(View.VISIBLE);
				notefoleserPintrestList.setVisibility(View.GONE);
				DataManager.sharedDataManager().setSelectedIndex(-1);
				adapter.notifyDataSetChanged();
				myDialog.dismiss();

			}
		});

		buttonDissmiss.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				myDialog.dismiss();
			}
		});

		myDialog.getWindow().getAttributes().windowAnimations = R.anim.slide_up_1;
		myDialog.show();

		myDialog.getWindow().setGravity(Gravity.BOTTOM);

	}

	public void showActionSheet_sort(View v) {

		final Dialog myDialog = new Dialog(MainActivity.this,
				R.style.CustomTheme);

		// layoutReminderTime
		// layouttimebomb

		myDialog.setContentView(R.layout.actionsheet_sort);
		Button buttonDissmiss = (Button) myDialog
				.findViewById(R.id.buttonDissmiss);

		LinearLayout layoutList = (LinearLayout) myDialog
				.findViewById(R.id.layoutList);
		TextView layoutListTextView = (TextView) layoutList
				.findViewById(R.id.textViewSlideMenuName);
		ImageView layoutListImageView = (ImageView) layoutList
				.findViewById(R.id.imageViewSlidemenu);
		layoutListImageView.setImageResource(R.drawable.alphabet_sort_view);

		layoutListTextView.setText("A-Z alphabetical");

		LinearLayout layoutDetail = (LinearLayout) myDialog
				.findViewById(R.id.layoutDetail);
		TextView layoutDetailTextView = (TextView) layoutDetail
				.findViewById(R.id.textViewSlideMenuName);
		ImageView layoutDetailImageView = (ImageView) layoutDetail
				.findViewById(R.id.imageViewSlidemenu);
		layoutDetailImageView.setImageResource(R.drawable.color_sort_view);
		layoutDetailTextView.setText("Colours");

		LinearLayout layoutPintrest = (LinearLayout) myDialog
				.findViewById(R.id.layoutPintrest);
		TextView layoutPintrestTextView = (TextView) layoutPintrest
				.findViewById(R.id.textViewSlideMenuName);
		ImageView layoutPintrestImageView = (ImageView) layoutPintrest
				.findViewById(R.id.imageViewSlidemenu);
		layoutPintrestImageView
				.setImageResource(R.drawable.modifiedtime_sort_view);
		layoutPintrestTextView.setText("Modified Time");

		LinearLayout layoutGrid = (LinearLayout) myDialog
				.findViewById(R.id.layoutGrid);
		TextView layoutGridTextView = (TextView) layoutGrid
				.findViewById(R.id.textViewSlideMenuName);
		ImageView layoutGridImageView = (ImageView) layoutGrid
				.findViewById(R.id.imageViewSlidemenu);
		layoutGridImageView.setImageResource(R.drawable.createdtime_sort_view);
		layoutGridTextView.setText("Created Time");

		LinearLayout layoutListReminderTime = (LinearLayout) myDialog
				.findViewById(R.id.layoutReminderTime);
		TextView layoutListTextViewReminderTime = (TextView) layoutListReminderTime
				.findViewById(R.id.textViewSlideMenuName);
		ImageView layoutListImageViewReminderTime = (ImageView) layoutListReminderTime
				.findViewById(R.id.imageViewSlidemenu);
		layoutListImageViewReminderTime
				.setImageResource(R.drawable.reminder_sort_view);
		layoutListTextViewReminderTime.setText("Reminder Time");

		LinearLayout layoutListTimeBomb = (LinearLayout) myDialog
				.findViewById(R.id.layouttimebomb);
		TextView layoutListTextViewTimeBomb = (TextView) layoutListTimeBomb
				.findViewById(R.id.textViewSlideMenuName);
		layoutListTimeBomb.findViewById(R.id.imageViewSlidemenu);
		layoutListTextViewTimeBomb.setText("Time Bomb");

		layoutGridTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(getApplicationContext(), "Created Time",
						Toast.LENGTH_SHORT).show();
				sortType = SORTTYPE.CREATED_TIME;
				sortingArray();

				myDialog.dismiss();

			}
		});

		layoutPintrestTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Modified Time",
						Toast.LENGTH_SHORT).show();
				sortType = SORTTYPE.MODIFIED_TIME;
				sortingArray();

				myDialog.dismiss();

			}
		});

		layoutDetailTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Toast.makeText(getApplicationContext(), "Colours",
						Toast.LENGTH_SHORT).show();

				sortType = SORTTYPE.COLOURS;
				sortingArray();

				myDialog.dismiss();

			}
		});

		layoutListTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				adapter.notifyDataSetChanged();

				sortType = SORTTYPE.ALPHABET;
				sortingArray();

				Toast.makeText(getApplicationContext(), "Alphabetical",
						Toast.LENGTH_SHORT).show();

				myDialog.dismiss();

			}
		});

		layoutListTextViewTimeBomb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Toast.makeText(getApplicationContext(), "Time Bomb",
						Toast.LENGTH_SHORT).show();

				myDialog.dismiss();

			}
		});

		layoutListTextViewReminderTime
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Toast.makeText(getApplicationContext(),
								"Reminder Time", Toast.LENGTH_SHORT).show();

						myDialog.dismiss();
					}
				});

		buttonDissmiss.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				myDialog.dismiss();
			}
		});

		myDialog.getWindow().getAttributes().windowAnimations = R.anim.slide_up_1;

		myDialog.show();

		myDialog.getWindow().setGravity(Gravity.BOTTOM);

	}

	// ---------------------------------------------------------------------------------------
	// showActionSheet end

	void showAlertWithEditText(Context context)
	{

		final Dialog dialog = new Dialog(context);

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// inflate your activity layout here!

		// LinearLayout layoutAlertbox1=

		View contentView = inflater.inflate(R.layout.edit_alert_view, null,
				false);
		TextView textViewTitleAlert = (TextView) contentView
				.findViewById(R.id.textViewTitleAlert);
		textViewTitleAlert.setText("CRATE A NOTE");
		textViewTitleAlert.setTextColor(Color.WHITE);
		final EditText textViewTitleAlertMessage = (EditText) contentView
				.findViewById(R.id.textViewTitleAlertMessage);
		// textViewTitleAlertMessage.setText(message);

		Button buttonAlertCancel = (Button) contentView
				.findViewById(R.id.buttonAlertCancel);
		Button buttonAlertOk = (Button) contentView
				.findViewById(R.id.buttonAlertOk);
		buttonAlertCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (textViewTitleAlertMessage.getText().toString().length() > 0) {

					// Call insert db call

					SimpleDateFormat dateFormatGmt = new SimpleDateFormat(
							"dd/MM/yyyy HH:mm:ss");
					dateFormatGmt.setTimeZone(TimeZone.getDefault());
					//dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
					Date d = new Date();
					String strDate = dateFormatGmt.format(d);


					System.out.println("The note created date: " + strDate
							+ "note title: "
							+ textViewTitleAlertMessage.getText().toString());

					boolean status = androidOpenDbHelperObj.databaseInsertion(
							textViewTitleAlertMessage.getText().toString(),
							"ffffff", strDate, strDate, "0", "0", "1234", "",
							"", "", "0");
					
					
					

					if (status == true)
					{
						Toast.makeText(MainActivity.this,
								"data inserted successfully",
								Toast.LENGTH_SHORT).show();

						getNoteWithTitle(textViewTitleAlertMessage.getText()
								.toString());
						
						SideMenuitems item=arrDataNote.get(arrDataNote.size()-1);
						
					ArrayList<DBNoteItems>	selectecitem_list=androidOpenDbHelperObj.getAllNotesWithNote_Id(item.getMenuid());

					if (selectecitem_list.size()>0)
					{
						DataManager.sharedDataManager().setSeletedListNoteItem(item);
						DataManager.sharedDataManager().setSeletedDBNoteItem(selectecitem_list.get(0));
						startActivity(new Intent(MainActivity.this, NoteMainActivity.class));
						
					}
					
					
					} else {

						Toast.makeText(MainActivity.this,
								"data not inserted successfully",
								Toast.LENGTH_SHORT).show();
					}

					dialog.dismiss();
				}

			}
		});
		buttonAlertOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				dialog.dismiss();

				// System.exit(0);

			}
		});

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(true);

		dialog.setContentView(contentView);
		dialog.show();

	}

	void showAlertWithUpdateTitleEditText(Context context,final DBNoteItems selectedItem,String message)
	{

		final Dialog dialog = new Dialog(context);

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// inflate your activity layout here!

		// LinearLayout layoutAlertbox1=

		View contentView = inflater.inflate(R.layout.edit_alert_view, null,
				false);
		TextView textViewTitleAlert = (TextView) contentView
				.findViewById(R.id.textViewTitleAlert);
		textViewTitleAlert.setText("CRATE A NOTE");
		textViewTitleAlert.setTextColor(Color.WHITE);
		final EditText textViewTitleAlertMessage = (EditText) contentView
				.findViewById(R.id.textViewTitleAlertMessage);
		 textViewTitleAlertMessage.setText(message);

		Button buttonAlertCancel = (Button) contentView
				.findViewById(R.id.buttonAlertCancel);
		Button buttonAlertOk = (Button) contentView
				.findViewById(R.id.buttonAlertOk);
		buttonAlertCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (textViewTitleAlertMessage.getText().toString().length() > 0) {

					// Call insert db call

					SimpleDateFormat dateFormatGmt = new SimpleDateFormat(
							"dd/MM/yyyy HH:mm:ss");
					dateFormatGmt.setTimeZone(TimeZone.getDefault());
					//dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
					Date d = new Date();
					String strDate = dateFormatGmt.format(d);
					System.out.println("The note created date: " + strDate
							+ "note title: "
							+ textViewTitleAlertMessage.getText().toString());

					selectedItem.setNote_Title(textViewTitleAlertMessage.getText().toString());

					boolean status=androidOpenDbHelperObj.updateNoteitems_title(selectedItem);




					if (status == true)
					{
						Toast.makeText(MainActivity.this,
								"data inserted successfully",
								Toast.LENGTH_SHORT).show();


						ArrayList<DBNoteItems>	selectecitem_list=androidOpenDbHelperObj.getAllNotesWithNote_Id(selectedItem.getNote_Id());

						getallNotes();


					} else {

						Toast.makeText(MainActivity.this,
								"data not inserted successfully",
								Toast.LENGTH_SHORT).show();
					}

					dialog.dismiss();
				}

			}
		});
		buttonAlertOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				dialog.dismiss();

				// System.exit(0);

			}
		});

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(true);

		dialog.setContentView(contentView);
		dialog.show();

	}

	/************* Fetch all note from database ******************/


	void  getAllFolder()
	{
		//SideMenuitems //getAllFolder

		//arrDataFolder=new ArrayList<SideMenuitems>();

		//ArrayList<DBNoteItems>
				arrDataFolder = androidOpenDbHelperObj
				.getAllFolder();

//		for (DBNoteItems dbNoteItems : arrinsertedNote) {
//			System.out.println("Foldr id: " + dbNoteItems.getNote_Id()
//					+ " Foldr_title: " + dbNoteItems.getNote_Title());
//
//			SideMenuitems item1 = new SideMenuitems();
//			item1.setMenuid(dbNoteItems.getNote_Id());
//			item1.setMenuName(dbNoteItems.getNote_Title());
//			arrDataFolder.add(item1);
//		}

	}

	void getallNotes()
	{
		arrDataNote.clear();
		arrDBDataNote.clear();

		ArrayList<DBNoteItems> arrinsertedNote = androidOpenDbHelperObj
				.getAllNotes();
		

		for (DBNoteItems dbNoteItems : arrinsertedNote) {
			System.out.println("note id: " + dbNoteItems.getNote_Id()
					+ " note_title: " + dbNoteItems.getNote_Title());

			SideMenuitems item1 = new SideMenuitems();
			item1.setMenuName(dbNoteItems.getNote_Title());
			ArrayList<DBNoteItemElement> dbNoteItemElements=androidOpenDbHelperObj.getLastNotesElementWithNote_Id(dbNoteItems.getNote_Id());
			item1.setMenuNameDetail("" + (dbNoteItemElements.size() > 0 ? dbNoteItemElements.get(0).getNOTE_ELEMENT_TYPE() : ""));
			item1.setMenuid(dbNoteItems.getNote_Id());
			if (dbNoteItems.getNote_Color().length() > 0
					&& dbNoteItems.getNote_Color() != null) {

				item1.setColours("#" + dbNoteItems.getNote_Color());
			}



if (dbNoteItems.getNote_TimeBomb() !=null)

{
	if (dbNoteItems.getNote_TimeBomb().equalsIgnoreCase("0"))
	{

		Log.d("time bomb","not set for this note");
		arrDataNote.add(item1);
		arrDBDataNote.add(dbNoteItems);
	}
	else
	{
		SimpleDateFormat formatter = new SimpleDateFormat(
				"dd/MM/yyyy HH:mm:ss",Locale.US);


		Date currentdate= new Date();
		String strDate = formatter.format(currentdate);


		try {

			Date timebombdate = formatter.parse(dbNoteItems.getNote_TimeBomb());

			Date dateCurrent = formatter.parse(strDate);

			System.out.println(timebombdate);
			System.out.println(formatter.format(timebombdate));

			if(dateCurrent.compareTo(timebombdate)>0)
			{
				System.out.println("Date1 is after Date2 --Delete note here");

				androidOpenDbHelperObj.deleteNote(dbNoteItems.getNote_Id());
			}
			else if(dateCurrent.compareTo(timebombdate)<0)
			{
				System.out.println("Date1 is before Date2");

				arrDataNote.add(item1);
				arrDBDataNote.add(dbNoteItems);
			}
			else if(dateCurrent.compareTo(timebombdate)==0)
			{
				System.out.println("Date1 is equal to Date2");
				//arrDataNote.add(item1);
				//arrDBDataNote.add(dbNoteItems);
			}


		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}
}


		}
		adapter.notifyDataSetChanged();

	}

//#pragma mark-the note update
	
	void getNoteWithTitle(String note_title)
	{
		ArrayList<DBNoteItems> arrinsertedNote = androidOpenDbHelperObj
				.getAllNotesWithTitle(note_title);

		for (DBNoteItems dbNoteItems : arrinsertedNote) {
			System.out.println("note id: " + dbNoteItems.getNote_Id()
					+ " note_title: " + dbNoteItems.getNote_Title());

			SideMenuitems item1 = new SideMenuitems();
			item1.setMenuName(dbNoteItems.getNote_Title());
			item1.setMenuNameDetail("");
			item1.setMenuid(dbNoteItems.getNote_Id());
			item1.setColours("#" + dbNoteItems.getNote_Color());

			arrDataNote.add(item1);
			arrDBDataNote.add(dbNoteItems);
		}
		adapter.notifyDataSetChanged();

	}


	public void didMoreSelected(SideMenuitems item1,View selectedbutton, int selectedindex)
	{


		selectedDbItem=arrDBDataNote.get(selectedindex);

		Log.d("The TextChange: ", "ID: " + item1.getMenuid() + "\n");
		Log.d("The TextChange: ","Text: "+selectedbutton.getTag()+"\n");
		Log.d("The TextChange: ", "Index: " + selectedindex + "\n");

		//androidOpenDbHelperObj.updateNoteElementText(notelistData.getNoteElmentId(), text);
		//NoteListDataModel editedDatamodel=arrNoteListData.get(position);
		//editedDatamodel.setStringtext(text);


		PopupMenu popup=new PopupMenu(MainActivity.this,selectedbutton);

		if (selectedDbItem.getNote_Lock_Status()!=null)
		{
			if (selectedDbItem.getNote_Lock_Status().equalsIgnoreCase("0"))
			{
				popup.getMenuInflater().inflate(R.menu.poupup_menu, popup.getMenu());
			}else
			{
				popup.getMenuInflater().inflate(R.menu.poupup_menu_1, popup.getMenu());
			}
		}else
		{

			popup.getMenuInflater().inflate(R.menu.poupup_menu, popup.getMenu());
		}




		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item)
			{



				Toast.makeText(MainActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();

				switch (item.getItemId())
				{
					case R.id.EditTitle:
					{
						showAlertWithUpdateTitleEditText(MainActivity.this,selectedDbItem,selectedDbItem.getNote_Title());
					}
						break;
					case R.id.Delete:
					{
						showAlertWithDeleteMessage("Are you sure you want delete this note", MainActivity.this, selectedDbItem);
					}
						break;
					case R.id.timebomb:
					{


						showDialog(DATE_DIALOG_ID);


					/*	SimpleDateFormat dateFormatGmt = new SimpleDateFormat(
								"dd/MM/yyyy HH:mm:ss");
						dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
						Date d = new Date();
						String strDate = dateFormatGmt.format(d);
						selectedDbItem.setNote_TimeBomb(strDate);

						boolean status=androidOpenDbHelperObj.updateNoteitems_timeBomb(selectedDbItem);

						if (status==true)
						{

							Toast.makeText(MainActivity.this,"Time Bomb added successfully",Toast.LENGTH_SHORT).show();
							getallNotes();
						}
						else
						{
							Toast.makeText(MainActivity.this,"Time Bomb added unsuccessfully",Toast.LENGTH_SHORT).show();

						}*/


					}
						break;
					case R.id.Lock:
					{

						showAlertToLockNUnlockNote(selectedDbItem, MainActivity.this,true,false);

					}
					break;
					case R.id.Unlock:
					{
						showAlertToLockNUnlockNote(selectedDbItem, MainActivity.this,false,false);
					}
					break;
					case R.id.Share:
					{

						Intent sharingIntent = new Intent(Intent.ACTION_SEND);
						sharingIntent.setType("text/html");
						sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("<p>" + selectedDbItem.getNote_Title().toString()+"</p>"));
						startActivity(Intent.createChooser(sharingIntent, "Share using"));

					}
					break;

					case R.id.Move:
					{

						if (arrDataFolder!=null)

						{
							if (arrDataFolder.size()>0)
							{
								showMoveToFolderDialog("",MainActivity.this,selectedDbItem);
							}
							else
							{
								showAlertWith("Please create at least one folder",MainActivity.this);
							}
						}



					}
						break;

					case R.id.Color:
					{

						/*ColorPickerDialog colorPickerDialog = new ColorPickerDialog(MainActivity.this,Color.WHITE,new  ColorPickerDialog.OnColorChangedListener()
						{


							public void onColorSelected(int color) {
								// do action
							}



						});
						colorPickerDialog.show();*/



						new ColorPickerDialog(MainActivity.this, new ColorPickerDialog.OnColorChangedListener() {
							@Override
							public void colorChanged(int color) {

								Log.d("selected Color",""+color);

							}
						},Color.WHITE).show();


					}
						break;
					default:
						break;
				}
				return true;
			}
		});

		popup.show();//showing popup menu


	}
	 public  void  didlistItemClick(SideMenuitems item1,View selectedbutton, int selectedindex)
	 {
		 Log.d("The listitem : ", "ID: " + item1.getMenuid() + "\n");
		 Log.d("The listitem: ","Text: "+selectedbutton.getTag()+"\n");
		 Log.d("The listitem: ", "Index: " + selectedindex + "\n");


		 SideMenuitems menuItem=arrDataNote.get(selectedindex);



		 if (menuItem.isIsdefaultNote()!=true)
		 {
			 selectedDbItem=arrDBDataNote.get(selectedindex);
			 if (selectedDbItem.getNote_Lock_Status()!=null)
			 {
				 if (selectedDbItem.getNote_Lock_Status().equalsIgnoreCase("1"))
				 {

					 showAlertToLockNUnlockNote(selectedDbItem, MainActivity.this,false,true);
				 }
				 else
				 {
					 DataManager.sharedDataManager().setSeletedListNoteItem(menuItem);
					 DataManager.sharedDataManager().setSeletedDBNoteItem(selectedDbItem);
					 startActivity(new Intent(context, NoteMainActivity.class));
				 }

			 }else
			 {
				 DataManager.sharedDataManager().setSeletedListNoteItem(menuItem);
				 DataManager.sharedDataManager().setSeletedDBNoteItem(selectedDbItem);
				 startActivity(new Intent(context, NoteMainActivity.class));
			 }



		 }
		 else
		 {
			 Toast.makeText(MainActivity.this, "Default Note can't be open", Toast.LENGTH_SHORT).show();
		 }
	 }


	@Override
	protected void onResume()
	{
		super.onResume();

		getallNotes();


	}
}
