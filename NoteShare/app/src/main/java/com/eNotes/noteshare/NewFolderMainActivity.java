package com.eNotes.noteshare;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.eNotes.Utlity.DialogUtill;
import com.eNotes.adpters.NewNoteFolderAdapter;
import com.eNotes.adpters.NewNoteFolderGridAdapter;
import com.eNotes.dataAccess.DataManager;
import com.eNotes.datamodels.SideMenuitems;
import com.eNotes.notesharedatabase.DBNoteItems;
import com.eNotes.notesharedatabase.NoteshareDatabaseHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.mobiapp.ventures.eNotes.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;


enum SORTTYPE_NEW {
	ALPHABET, COLOURS, CREATED_TIME, MODIFIED_TIME, REMINDER_TIME, TIME_BOMB
};

public class NewFolderMainActivity extends DrawerActivity {



	NoteshareDatabaseHelper androidOpenDbHelperObj;
	SQLiteDatabase sqliteDatabase;

	public ImageButton imageButtonHamburg, imageButtoncalander,
			imageButtonsquence;
	public TextView textViewheaderTitle;
	public RelativeLayout layoutHeader;

	public ImageButton textViewAdd;
	public ListView notefoleserList;
	public GridView notefoleserGridList;
	public ScrollView notefoleserPintrestList;

	public LinearLayout Layout1;
	public LinearLayout Layout2,LayoutNoFolder;

	public NewNoteFolderAdapter adapter;
	public NewNoteFolderGridAdapter gridAdapter;
	public ArrayList<SideMenuitems> arrDataNote;
	public ArrayList<DBNoteItems> arrDBDataNote;

	final Context context = this;
	public TextView textNoteSort, textNoteView;

	public SORTTYPE_NEW sortType;

	public Dialog dialogColor;
	public ImageButton searchbuttonclick;
	public EditText editTextsearchNote;
	private AdView mAdView;
	private Button btnFullscreenAd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);

		androidOpenDbHelperObj = new NoteshareDatabaseHelper(context);
		sqliteDatabase = androidOpenDbHelperObj.getWritableDatabase();
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// inflate your activity layout here!
		View contentView = inflater.inflate(R.layout.folder_activity_main,
				null, false);
		mDrawerLayout.addView(contentView, 0);
		DataManager.sharedDataManager().setSelectedFolderItemIndex(-1);
		initlizeUIElement(contentView);
		loadAds();

	}

	@Override
	protected void onResume() {
		super.onResume();
		getDeafultNote();
	}

	void  loadAds()
	{
		MobileAds.initialize(getApplicationContext(), "ca-app-pub-4042620180347128~4456337699");
		mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder()
				.build();
		mAdView.loadAd(adRequest);
	}

	void initlizeUIElement(View contentview) {

		DataManager.sharedDataManager().setTypeofListView(true);

		layoutHeader = (RelativeLayout) contentview
				.findViewById(R.id.mainHeadermenue);
		textViewheaderTitle = (TextView) layoutHeader
				.findViewById(R.id.textViewheaderTitle);

		imageButtoncalander = (ImageButton) layoutHeader
				.findViewById(R.id.imageButtoncalander);
		imageButtonHamburg = (ImageButton) layoutHeader
				.findViewById(R.id.imageButtonHamburg);
		imageButtonsquence = (ImageButton) layoutHeader
				.findViewById(R.id.imageButtonsquence);
		imageButtoncalander.setVisibility(View.GONE);
		imageButtonsquence.setVisibility(View.GONE);

		textNoteSort = (TextView) findViewById(R.id.textNoteSort);
		textNoteView = (TextView) findViewById(R.id.textNoteView);

		textViewAdd = (ImageButton) findViewById(R.id.textViewAdd);
		notefoleserList = (ListView) findViewById(R.id.notefoleserList);

		LayoutNoFolder = (LinearLayout) findViewById(R.id.LayoutNoFolder);


		arrDataNote = new ArrayList<SideMenuitems>();
		arrDBDataNote= new ArrayList<DBNoteItems>();


		notefoleserGridList = (GridView) findViewById(R.id.notefoleserGridList);
		notefoleserPintrestList = (ScrollView) findViewById(R.id.notefoleserPintrestList);
		Layout1 = (LinearLayout) findViewById(R.id.Layout1);
		Layout2 = (LinearLayout) findViewById(R.id.Layout2);



		// Grid adapter

		gridAdapter = new NewNoteFolderGridAdapter(this, arrDataNote);
		notefoleserGridList.setAdapter(gridAdapter);
		notefoleserGridList.setLongClickable(true);

		// list adapter

		adapter = new NewNoteFolderAdapter(this, arrDataNote, new NewNoteFolderAdapter.NewNoteFolderAdapter_Listner() {
			@Override
			public void didFolderMoreSelected(SideMenuitems item1, int selectedindex) {
				DataManager.sharedDataManager().setSelectedFolderItemIndex(selectedindex);
				System.out.println("the more Oprtion is Tapped: "+item1.getMenuName());
				adapter.notifyDataSetChanged();
			}

			@Override
			public void didFolderExpandViewAtIndex(SideMenuitems item1, View selectedbutton, int selectedindex) {


				System.out.println("the Expanded is Tapped: "+item1.getMenuName() + "Action"+selectedbutton.getTag());
				DataManager.sharedDataManager().setSelectedFolderItemIndex(-1);
				adapter.notifyDataSetChanged();

				showAlertWithDeleteMessage("Deletion of folder will Delete inside Note,Are you sure?",NewFolderMainActivity.this,item1);


			}

			@Override
			public void didFolderlistItemClick(SideMenuitems item1, View selectedbutton, int selectedindex) {

				System.out.println("the listItem is Tapped: "+item1.getMenuName() + "Action"+":Tapped");

				SideMenuitems selectedfolder=arrDataNote.get(selectedindex);

				DataManager.sharedDataManager().setSelectedFolderId(selectedfolder.getMenuid());

				//startActivity(new Intent(NewFolderMainActivity.this,FolderNoteListMainActivity.class));
				startActivity(new Intent(NewFolderMainActivity.this,NoteListingActvity.class));
			}
		});
		notefoleserList.setAdapter(adapter);
		notefoleserList.setLongClickable(true);
		
		//search
		searchbuttonclick=(ImageButton) contentview.findViewById(R.id.searchbuttonclick);
		editTextsearchNote=(EditText) contentview.findViewById(R.id.editTextsearchNote);
		
		
		addlistners();
		// getDeafultNote();
		addClickListneres();

		getDeafultNote();
	}

	void showAlertWithDeleteMessage(String message, Context context, final SideMenuitems dbNoteItems) {

		final Dialog dialog = new Dialog(context);

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// inflate your activity layout here!
		View contentView = inflater.inflate(R.layout.alert_view, null, false);

		TextView textViewTitleAlert = (TextView) contentView
				.findViewById(R.id.textViewTitleAlert);
		textViewTitleAlert.setText("" + dbNoteItems.getMenuName());
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
				dialog.dismiss();

			}
		});
		buttonAlertOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				ArrayList<DBNoteItems> arrinsertedNote = androidOpenDbHelperObj
						.getAllNotesWithFolder_Id(dbNoteItems.getMenuid());
				for (DBNoteItems items:arrinsertedNote) {
					deleteNoteInsideFolder(items.getNote_Id());
				}

				boolean status = androidOpenDbHelperObj.updateFolder_DeleteStatus(dbNoteItems.getMenuid());


				if (status == true) {

					Toast.makeText(NewFolderMainActivity.this, "Folder deleted successfully", Toast.LENGTH_SHORT).show();
					getDeafultNote();
				} else {
					Toast.makeText(NewFolderMainActivity.this, "Folder deleted failed", Toast.LENGTH_SHORT).show();

				}
				dialog.dismiss();

			}
		});

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(contentView);
		dialog.show();

	}

	void  deleteNoteInsideFolder (String note_Id) {

		 androidOpenDbHelperObj.updateNoteitems_Delete(note_Id,"-2");

	}
	void addClickListneres() {

		notefoleserList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub


				SideMenuitems selectedfolder=arrDataNote.get(arg2);

				DataManager.sharedDataManager().setSelectedFolderId(selectedfolder.getMenuid());

				//startActivity(new Intent(NewFolderMainActivity.this,FolderNoteListMainActivity.class));
				startActivity(new Intent(NewFolderMainActivity.this,NoteListingActvity.class));

				//Toast.makeText(NewFolderMainActivity.this,"The position CLicked:"+position +"\n folder Id"+selectedfolder.getMenuid(),Toast.LENGTH_SHORT).show();
				



				// ((SwipeLayout)(notefoleserList.getChildAt(position - notefoleserList.getFirstVisiblePosition()))).open(true);
			}
		});

		notefoleserGridList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Log.v("Grid  clicked", "pos: " + arg2);




			}
		});

		notefoleserList
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int pos, long id) {
						// TODO Auto-generated method stub

						Log.v("List long clicked", "pos: " + pos);

						showColorAlert(pos, NewFolderMainActivity.this);

						return true;
					}
				});

		notefoleserGridList
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int pos, long id) {
						// TODO Auto-generated method stub

						Log.v("Grid long clicked", "pos: " + pos);
						showColorAlert(pos, NewFolderMainActivity.this);

						return true;
					}
				});
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
			layoutsepreter.setVisibility(View.GONE);

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
		case CREATED_TIME: {

		}
			break;
		case MODIFIED_TIME: {

		}
			break;
		case REMINDER_TIME: {

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

	void getDeafultNote() {
		arrDataNote.clear();
		arrDBDataNote.clear();

		ArrayList<DBNoteItems> arrinsertedNote = androidOpenDbHelperObj
				.getAllFolder();


		for (DBNoteItems dbNoteItems : arrinsertedNote) {
			System.out.println("FolderId id: " + dbNoteItems.getNote_Id()
					+ " FolderTitle_title: " + dbNoteItems.getNote_Title());

			SideMenuitems item1 = new SideMenuitems();
			item1.setMenuName(dbNoteItems.getNote_Title());
			item1.setMenuNameDetail(dbNoteItems.getNote_Created_Time());
			item1.setMenuid(dbNoteItems.getNote_Id());
			if (dbNoteItems.getNote_Color().length() > 0
					&& dbNoteItems.getNote_Color() != null) {

				item1.setColours("#" + dbNoteItems.getNote_Color());
			}

			if (dbNoteItems.getNote_Deleted_Status()!= null) {

				if (dbNoteItems.getNote_Deleted_Status().equalsIgnoreCase("-1")) {

				} else {

					arrDataNote.add(item1);
					arrDBDataNote.add(dbNoteItems);
				}
			} else {
				arrDataNote.add(item1);
				arrDBDataNote.add(dbNoteItems);
			}
		}



		adapter.notifyDataSetChanged();
		String strCout = "(" + arrDataNote.size() + ")";
		textViewheaderTitle.setText("Folder" + strCout);
		sortType = SORTTYPE_NEW.ALPHABET;
		updateGridView();
		updatePintrestView();

		if (arrDataNote.size()>0) {

			LayoutNoFolder.setVisibility(View.GONE);
		}
		else {
			LayoutNoFolder.setVisibility(View.VISIBLE);
		}

	}

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

				// create folder here

				showAlertWithEditText(NewFolderMainActivity.this);

			}
		});


		
		
		searchbuttonclick.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//search Click
				if (editTextsearchNote.getText().toString().length()>0)
				{
					filterWithSearchString(editTextsearchNote.getText().toString());
				}
				
			}
		});
		
		
		// Add Text Change Listener to EditText
	    editTextsearchNote.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				// adapter.getFilter().filter(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
	}

	@Override
	public void onBackPressed() {

		DialogUtill.backDialog(this);

	}

	void showAlertWith(String message, Context context) {

		final Dialog dialog = new Dialog(context);

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// inflate your activity layout here!
		View contentView = inflater.inflate(R.layout.alert_view, null, false);

		TextView textViewTitleAlert = (TextView) contentView
				.findViewById(R.id.textViewTitleAlert);
		textViewTitleAlert.setText("Alert");
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
		dialog.setCancelable(true);

		dialog.setContentView(contentView);
		dialog.show();

	}

	void showAlertWithEditText(Context context) {

		final Dialog dialog = new Dialog(context);

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// inflate your activity layout here!

		// LinearLayout layoutAlertbox1=

		View contentView = inflater.inflate(R.layout.edit_alert_view, null,
				false);
		TextView textViewTitleAlert = (TextView) contentView
				.findViewById(R.id.textViewTitleAlert);
		textViewTitleAlert.setText("Create a folder");
		textViewTitleAlert.setTextColor(Color.WHITE);
		final EditText textViewTitleAlertMessage = (EditText) contentView
				.findViewById(R.id.textViewTitleAlertMessage);
		textViewTitleAlertMessage.setHint("Please enter folder title");
		// textViewTitleAlertMessage.setText(message);

		Button buttonAlertCancel = (Button) contentView
				.findViewById(R.id.buttonAlertCancel);
		Button buttonAlertOk = (Button) contentView
				.findViewById(R.id.buttonAlertOk);
		buttonAlertCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (textViewTitleAlertMessage.getText().toString().length()>0)
				{
					updateFolder(textViewTitleAlertMessage.getText().toString());
					dialog.dismiss();
				} else {

					Toast.makeText(NewFolderMainActivity.this, "Please create a folder to submit", Toast.LENGTH_SHORT).show();
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

	// showActionSheet
	// ---------------------------------------------------------------------------------------

	public void showActionSheet(View v) {

		final Dialog myDialog = new Dialog(NewFolderMainActivity.this,
				R.style.CustomTheme);

		myDialog.setContentView(R.layout.folder_view_actionsheet);
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
		layoutPintrest.setVisibility(View.GONE);
		TextView layoutPintrestTextView = (TextView) layoutPintrest
				.findViewById(R.id.textViewSlideMenuName);
		ImageView layoutPintrestImageView = (ImageView) layoutPintrest
				.findViewById(R.id.imageViewSlidemenu);
		layoutPintrestImageView.setImageResource(R.drawable.pintrest_view_logo);
		layoutPintrestTextView.setText("Tiles");

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

		final Dialog myDialog = new Dialog(NewFolderMainActivity.this,
				R.style.CustomTheme);

		myDialog.setContentView(R.layout.folder_view_actionsheet_sort);
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
		layoutDetail.setVisibility(View.GONE);

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
				// Toast.makeText(getApplicationContext(), "Created Time",
				// Toast.LENGTH_SHORT).show();
				sortType = SORTTYPE_NEW.CREATED_TIME;
				sortingArray();

				myDialog.dismiss();

			}
		});

		layoutPintrestTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// Toast.makeText(getApplicationContext(), "Modified Time",
				// Toast.LENGTH_SHORT).show();

				sortType = SORTTYPE_NEW.MODIFIED_TIME;
				sortingArray();

				myDialog.dismiss();

			}
		});

		layoutDetailTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// Toast.makeText(getApplicationContext(), "Colours",
				// Toast.LENGTH_SHORT).show();

				sortType = SORTTYPE_NEW.COLOURS;
				sortingArray();

				myDialog.dismiss();

			}
		});

		layoutListTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				adapter.notifyDataSetChanged();

				sortType = SORTTYPE_NEW.ALPHABET;
				sortingArray();

				// Toast.makeText(getApplicationContext(), "Alphabetical",
				// Toast.LENGTH_SHORT).show();

				myDialog.dismiss();

			}
		});

		layoutListTextViewTimeBomb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				// Toast.makeText(getApplicationContext(), "Time Bomb",
				// Toast.LENGTH_SHORT).show();

				myDialog.dismiss();

			}
		});

		layoutListTextViewReminderTime
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						// Toast.makeText(getApplicationContext(),
						// "Reminder Time", Toast.LENGTH_SHORT).show();

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

	void updateFolder(String folderName)
	{

		SimpleDateFormat dateFormatGmt = new SimpleDateFormat(
				"dd/MM/yyyy HH:mm:ss");
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date d = new Date();
		String strDate = dateFormatGmt.format(d);
		System.out.println("The note created date: " + strDate
				+ "note title: "
				+ folderName.toString());

		boolean status = false;

		status=androidOpenDbHelperObj.folderdatabaseInsertion(folderName,"ffffff",strDate,strDate,"0","0","1234","","0");

		if (status==true)
		{

			getDeafultNote();
		}


	}

	void showColorAlert(final int position, Context context) {

		dialogColor = new Dialog(context);

		dialogColor.setCanceledOnTouchOutside(true);

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// inflate your activity layout here!
		View contentView = inflater.inflate(R.layout.paintcolor, null, false);
		LinearLayout paintLayout = (LinearLayout) contentView
				.findViewById(R.id.paint_colors);
		LinearLayout paintLayout1 = (LinearLayout) contentView
				.findViewById(R.id.paint_colors1);
		// currPaint = (ImageButton) paintLayout.getChildAt(0);
		// currPaint.setImageDrawable(getResources().getDrawable(
		// R.drawable.paint_pressed));

		ImageButton colorbutton1 = (ImageButton) paintLayout
				.findViewById(R.id.colorbutton1);
		ImageButton colorbutton2 = (ImageButton) paintLayout
				.findViewById(R.id.colorbutton2);
		ImageButton colorbutton3 = (ImageButton) paintLayout
				.findViewById(R.id.colorbutton3);
		ImageButton colorbutton4 = (ImageButton) paintLayout
				.findViewById(R.id.colorbutton4);
		ImageButton colorbutton5 = (ImageButton) paintLayout
				.findViewById(R.id.colorbutton5);
		ImageButton colorbutton6 = (ImageButton) paintLayout
				.findViewById(R.id.colorbutton6);
		ImageButton colorbutton7 = (ImageButton) paintLayout
				.findViewById(R.id.colorbutton7);
		ImageButton colorbutton8 = (ImageButton) paintLayout1
				.findViewById(R.id.colorbutton8);
		ImageButton colorbutton9 = (ImageButton) paintLayout1
				.findViewById(R.id.colorbutton9);
		ImageButton colorbutton10 = (ImageButton) paintLayout1
				.findViewById(R.id.colorbutton10);
		ImageButton colorbutton11 = (ImageButton) paintLayout1
				.findViewById(R.id.colorbutton11);
		ImageButton colorbutton12 = (ImageButton) paintLayout1
				.findViewById(R.id.colorbutton12);
		ImageButton colorbutton13 = (ImageButton) paintLayout1
				.findViewById(R.id.colorbutton13);
		ImageButton colorbutton14 = (ImageButton) paintLayout1
				.findViewById(R.id.colorbutton14);
		colorbutton1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				paintClicked(position, v);

			}
		});
		colorbutton2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				paintClicked(position, v);

			}
		});
		colorbutton3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				paintClicked(position, v);

			}
		});
		colorbutton4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				paintClicked(position, v);
			}
		});
		colorbutton5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				paintClicked(position, v);
			}
		});
		colorbutton6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				paintClicked(position, v);
			}
		});
		colorbutton7.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				paintClicked(position, v);
			}
		});

		colorbutton8.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				paintClicked(position, v);
			}
		});

		colorbutton9.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				paintClicked(position, v);
			}
		});
		colorbutton10.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				paintClicked(position, v);
			}
		});
		colorbutton11.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				paintClicked(position, v);
			}
		});
		colorbutton12.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				paintClicked(position, v);
			}
		});
		colorbutton13.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				paintClicked(position, v);
			}
		});
		colorbutton14.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				paintClicked(position, v);
			}
		});

		TextView textViewTitleAlert = (TextView) contentView
				.findViewById(R.id.textViewTitleAlert);
		textViewTitleAlert.setText("SELECT TAG COLOR");
		textViewTitleAlert.setTextColor(Color.WHITE);

		dialogColor.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogColor.setCancelable(true);
		dialogColor.setCanceledOnTouchOutside(true);

		dialogColor.setContentView(contentView);
		dialogColor.show();
	}

	public void paintClicked(int position, View view) {

		// update color
		dialogColor.dismiss();
		String color = view.getTag().toString();
		System.out.println("selected color:" + color);
		updatedFolderItem(position, color);


	}

	void updatedFolderItem(int pos, String coloString)
	{

		DBNoteItems itemSelcted=arrDBDataNote.get(pos);
		itemSelcted.setNote_Color(coloString);
		androidOpenDbHelperObj.updateFolder_Color(itemSelcted);


		Log.v("the folder color:","Color:"+coloString);


		SideMenuitems item1 = arrDataNote.get(pos);
		item1.setColours("#"+coloString);
		adapter.notifyDataSetChanged();
		gridAdapter.notifyDataSetChanged();



	}


	void filterWithSearchString(String filterString)
	{
		
	}



	
}
