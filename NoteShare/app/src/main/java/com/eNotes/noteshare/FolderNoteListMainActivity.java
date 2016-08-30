package com.eNotes.noteshare;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;

;

public class FolderNoteListMainActivity extends DrawerActivity  {

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
	
	public ArrayList<SideMenuitems> arrDataNote;;
	public ArrayList<DBNoteItems> arrDBDataNote,arrDataFolder;

	
	final Context context = this;
	public TextView textNoteSort, textNoteView;

	public SORTTYPE sortType;

	public  DBNoteItems selectedDbItem;

	public FolderListingAdapter folderListingAdapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// inflate your activity layout here!
		View contentView = inflater
				.inflate(R.layout.folder_list_activity_main, null, false);
		mDrawerLayout.addView(contentView, 0);

		androidOpenDbHelperObj = new NoteshareDatabaseHelper(context);
		sqliteDatabase = androidOpenDbHelperObj.getWritableDatabase();

		DataManager.sharedDataManager().setSelectedIndex(-1);


		initlizeUIElement(contentView);

		//getDeafultNote();

		getallNotes();
		//getAllFolder();

	}



	public void btnCallbacks(Object data) {
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

				showAlertWithEditText(FolderNoteListMainActivity.this);

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
				Toast.makeText(FolderNoteListMainActivity.this, "Default Note can't be open", Toast.LENGTH_SHORT).show();
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
				FolderNoteListMainActivity.this);

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

					Toast.makeText(FolderNoteListMainActivity.this,"Note deleted successfully",Toast.LENGTH_SHORT).show();
					getallNotes();
				}
				else
				{
					Toast.makeText(FolderNoteListMainActivity.this,"Note deleted unsuccessfully",Toast.LENGTH_SHORT).show();

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


		folderListingAdapter=new FolderListingAdapter(FolderNoteListMainActivity.this,arrDataFolder);
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

				DBNoteItems itemSelected=arrDataFolder.get(i);
				items.setNote_Folder_Id(itemSelected.getNote_Id());

			boolean status=androidOpenDbHelperObj.updateNoteitems_MoveToFolder(items);

				if (status==true)
				{

					Toast.makeText(FolderNoteListMainActivity.this,"Note moved successfully",Toast.LENGTH_SHORT).show();
					getallNotes();
				}
				else
				{
					Toast.makeText(FolderNoteListMainActivity.this," move unsuccessfully",Toast.LENGTH_SHORT).show();

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

		final Dialog myDialog = new Dialog(FolderNoteListMainActivity.this,
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

		final Dialog myDialog = new Dialog(FolderNoteListMainActivity.this,
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
					dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
					Date d = new Date();
					String strDate = dateFormatGmt.format(d);


					System.out.println("The note created date: " + strDate
							+ "note title: "
							+ textViewTitleAlertMessage.getText().toString());

					boolean status = androidOpenDbHelperObj.databaseInsertion(
							textViewTitleAlertMessage.getText().toString(),
							"ffffff", strDate, strDate, "0", "0", "1234", DataManager.sharedDataManager().getSelectedFolderId(),
							"", "", "0");
					
					
					

					if (status == true)
					{
						Toast.makeText(FolderNoteListMainActivity.this,
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
						startActivity(new Intent(FolderNoteListMainActivity.this, NoteMainActivity.class));
						
					}
					
					
					} else {

						Toast.makeText(FolderNoteListMainActivity.this,
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
					dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
					Date d = new Date();
					String strDate = dateFormatGmt.format(d);
					System.out.println("The note created date: " + strDate
							+ "note title: "
							+ textViewTitleAlertMessage.getText().toString());

					selectedItem.setNote_Title(textViewTitleAlertMessage.getText().toString());

					boolean status=androidOpenDbHelperObj.updateNoteitems_title(selectedItem);




					if (status == true)
					{
						Toast.makeText(FolderNoteListMainActivity.this,
								"data inserted successfully",
								Toast.LENGTH_SHORT).show();


						ArrayList<DBNoteItems>	selectecitem_list=androidOpenDbHelperObj.getAllNotesWithNote_Id(selectedItem.getNote_Id());

						getallNotes();


					} else {

						Toast.makeText(FolderNoteListMainActivity.this,
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


	void getallNotes()
	{
		arrDataNote.clear();
		arrDBDataNote.clear();

		ArrayList<DBNoteItems> arrinsertedNote = androidOpenDbHelperObj
				.getAllNotesWithFolder_Id(DataManager.sharedDataManager().getSelectedFolderId());
		

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

			arrDataNote.add(item1);
			arrDBDataNote.add(dbNoteItems);

if (item1.getTimeBomb()!=null)

{
	if (!item1.getTimeBomb().equalsIgnoreCase("0"))
	{
		SimpleDateFormat formatter = new SimpleDateFormat(
				"dd/MM/yyyy HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

		Date currentdate= new Date();
		String strDate = formatter.format(currentdate);


		try {

			Date timebombdate = formatter.parse(item1.getTimeBomb().toString());

			Date dateCurrent = formatter.parse(strDate);

			System.out.println(timebombdate);
			System.out.println(formatter.format(timebombdate));

			if(dateCurrent.compareTo(timebombdate)>0)
			{
				System.out.println("Date1 is after Date2 --Delete note here");
			}
			else if(dateCurrent.compareTo(timebombdate)<0)
			{
				System.out.println("Date1 is before Date2");
			}
			else if(dateCurrent.compareTo(timebombdate)==0)
			{
				System.out.println("Date1 is equal to Date2");
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


		PopupMenu popup=new PopupMenu(FolderNoteListMainActivity.this,selectedbutton);

		popup.getMenuInflater().inflate(R.menu.poupup_menu, popup.getMenu());
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item)
			{



				Toast.makeText(FolderNoteListMainActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();

				switch (item.getItemId())
				{
					case R.id.EditTitle:
					{
						showAlertWithUpdateTitleEditText(FolderNoteListMainActivity.this,selectedDbItem,selectedDbItem.getNote_Title());
					}
						break;
					case R.id.Delete:
					{
						showAlertWithDeleteMessage("Are you sure you want delete this note",FolderNoteListMainActivity.this,selectedDbItem);
					}
						break;
					case R.id.timebomb:
					{


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



					}
					break;
					case R.id.Share:
					{

					}
					break;

					case R.id.Move:
					{

						if (arrDataFolder!=null)

						{
							if (arrDataFolder.size()>0)
							{
								showMoveToFolderDialog("",FolderNoteListMainActivity.this,selectedDbItem);
							}
							else
							{
								showAlertWith("Please create at least one folder",FolderNoteListMainActivity.this);
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



						new ColorPickerDialog(FolderNoteListMainActivity.this, new ColorPickerDialog.OnColorChangedListener() {
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
			 DBNoteItems noteItems=arrDBDataNote.get(selectedindex);

			 DataManager.sharedDataManager().setSeletedListNoteItem(menuItem);
			 DataManager.sharedDataManager().setSeletedDBNoteItem(noteItems);
			 startActivity(new Intent(context, NoteMainActivity.class));
		 }
		 else
		 {
			 Toast.makeText(FolderNoteListMainActivity.this, "Default Note can't be open", Toast.LENGTH_SHORT).show();
		 }
	 }


}
