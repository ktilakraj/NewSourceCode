package com.eNotes.noteshare;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.eNotes.adpters.MenuOpenInterface;
import com.eNotes.adpters.SlideMenuAdapter;
import com.eNotes.dataAccess.DataManager;
import com.eNotes.datamodels.SideMenuitems;
import com.eNotes.datamodels.SlideMenu;
import com.eNotes.notesharedatabase.NoteshareDatabaseHelper;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mobiapp.ventures.eNotes.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class DrawerActivity extends Activity implements MenuOpenInterface {
	// private static String TAG = MainActivity.class.getSimpleName();
	public String[] mNavigationDrawerItemTitles;
	public ArrayList<SideMenuitems> arrMenuTitle;
	public DrawerLayout mDrawerLayout;
	public ListView mDrawerList;
	ArrayList<SideMenuitems> arrMenu;


	public NoteshareDatabaseHelper androidOpenDbHelperObj;
	public SQLiteDatabase sqliteDatabase;

	// ArrayAdapter<String> adapter;

	public SlideMenuAdapter adapter;

	private static boolean isLaunch = true;

	// private String mActivityTitle;

	public TextView mTitleTextView;
	protected static int position;
	public SlideMenu menu;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sliding_sctivity);

		androidOpenDbHelperObj = new NoteshareDatabaseHelper(this);
		sqliteDatabase = androidOpenDbHelperObj.getWritableDatabase();

		// DataManager.sharedDataManager().printname();

		// Loading menu file

		// Do what you need for this SDK
		/*if (Build.VERSION.SDK_INT >= 21) {
			Window window = this.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(this.getResources().getColor(R.color.header_bg));
		}*/

		String strresponse = loadJSONFromAsset();
		try {
			JSONObject jsonObject = new JSONObject(strresponse);

			menu = new SlideMenu(jsonObject);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mNavigationDrawerItemTitles = getResources().getStringArray(
				R.array.navigation_drawer_items_array);

		arrMenuTitle = menu.getSideMenuitems();
		arrMenu = new ArrayList<SideMenuitems>();

		for (int i = 0; i < arrMenuTitle.size(); i++) {
			SideMenuitems items = arrMenuTitle.get(i);
			int menuid = Integer.parseInt(items.getMenuid());
			switch (menuid) {

				case 2:
					items.setResourceId(R.drawable.check_list_icon);
					break;
				case 1:
					items.setResourceId(R.drawable.note_menu_icon);
					break;
				case 3:
					items.setResourceId(R.drawable.folder_icon);
					break;
				case 4:
					items.setResourceId(R.drawable.about_us_icon);
					break;
				case 5:
					items.setResourceId(R.drawable.termsandcondition_icon);
					break;
				case 6:
					items.setResourceId(R.drawable.notification_icon);
					break;
				case 7:
					items.setResourceId(R.drawable.rate_us_icon);
					break;
				case 8:
					items.setResourceId(R.drawable.likeus_on_icon);
					break;
				case 9:
					items.setResourceId(R.drawable.send_feedback_icon);
					break;
				case 10:
					items.setResourceId(R.drawable.invite_icon);
					break;
				case 11:
					items.setResourceId(R.drawable.setting_icon);
					break;
				case 12:
					items.setResourceId(R.drawable.logout_icon);
					break;
				case 13:
					items.setResourceId(R.drawable.logout_icon);
					break;
				case 14:
					items.setResourceId(R.drawable.delete_new_gray_icon);
					break;
				default:
					break;
			}
			arrMenu.add(items);

		}

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// adapter = new
		// ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mNavigationDrawerItemTitles);
		adapter = new SlideMenuAdapter(this, mNavigationDrawerItemTitles,
				arrMenu);
		mDrawerList.setAdapter(adapter);

		mDrawerList.setSelector(android.R.color.transparent);
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
									int position, long arg3) {
				// TODO Auto-generated method stub

				mDrawerLayout.closeDrawers();
				// mTitleTextView.setText(mNavigationDrawerItemTitles[position]);

				openActivity(position);

				/*
				 * Bundle args = new Bundle(); args.putString("Menu",
				 * mNavigationDrawerItemTitles[position]); DetailFragment detail
				 * = new DetailFragment(); detail.setArguments(args);
				 * FragmentManager fragmentManager = getFragmentManager();
				 * fragmentManager
				 * .beginTransaction().replace(R.id.content_frame,
				 * detail).commit();
				 */

			}

		});

		if (isLaunch) {

			isLaunch = false;
			//openinitilaActivity(0);
		}

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	protected void openinitilaActivity(int position1) {

		System.out.println("initail launch");
		DataManager.sharedDataManager().setSelectedFolderId(null);
		startActivity(new Intent(this, NoteListingActvity.class));
		finish();
	}

	protected void openActivity(int position1) {

		position = position1;

		if (position1 == 0) {

			// Profile setting
			//startActivity(new Intent(this, ProfileSettingActivity.class));
			//System.out.println("profile setting");
			//finish();

		} else {
			SideMenuitems modeldata = arrMenu.get(position1 - 1);

			int menuid = Integer.parseInt(modeldata.getMenuid());

			switch (menuid) {
				case 2: {

					// openinitilaActivity(0);
					System.out.println("check list");
					startActivity(new Intent(this, CheckListActivity.class));
					finish();
				}

				break;

				case 1: {

					System.out.println("notes");
					openinitilaActivity(0);

				}
				break;
				case 3: {
					System.out.println("folder");

					startActivity(new Intent(this, NewFolderMainActivity.class));
					finish();
				}
				break;

				case 4: {

					System.out.println("about note share");
					startActivity(new Intent(this, AboutNoteShareActivity.class));
					finish();

				}
				break;
				case 5: {
					System.out.println("terms and conditions");
					startActivity(new Intent(this, TermsAndConditionsActivity.class));
					finish();

				}
				break;
				case 6: {

					System.out.println("notification center");
					startActivity(new Intent(this, NotificationCenterActivity.class));

					finish();
				}
				break;
				case 7: {
					System.out.println("rate us");

					startActivity(new Intent(this, RateUsActivity.class));
					finish();

				}
				break;
				case 8: {
					System.out.println("like us on facebook");
					startActivity(new Intent(this, LikeUsOnFacebookActivity.class));
					finish();

				}
				break;
				case 9: {
					System.out.println(" send feed back");
					startActivity(new Intent(this, SendFeedbackActivity.class));
					finish();

				}
				break;
				case 10: {

					System.out.println("invites friends");
					//startActivity(new Intent(this, InviteFriendsActivity.class));
					//finish();
					shareTextUrl();

				}
				break;
				case 11: {
					System.out.println("setting");
					startActivity(new Intent(this, SettingActivity.class));
					finish();

				}
				break;
				case 12: {
					System.out.println("login");
					startActivity(new Intent(this, LoginActivity.class));
					finish();

				}
				break;
				case 13: {
					System.out.println("logout");
				}
				break;

				case 14: {
					System.out.println("Trash");
					startActivity(new Intent(this, TrashActivity.class));
					finish();
				}
				break;



				default: {

				}
				break;
			}
		}

	}

	public void addListners() {

	}


	private void shareTextUrl() {
		Intent share = new Intent(Intent.ACTION_SEND);
		share.setType("text/plain");
		share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

		// Add data to the intent, the receiving app will decide
		// what to do with it.
		share.putExtra(Intent.EXTRA_SUBJECT, "eNote");
		share.putExtra(Intent.EXTRA_TEXT, "");

		startActivity(Intent.createChooser(share, "Share eNote"));
	}

	public String loadJSONFromAsset() {
		String json = null;
		try {

			InputStream is = getAssets().open("sidemenu.json");
			// InputStream is=getResources().openRawResource(R.raw.sidemenu);

			int size = is.available();

			byte[] buffer = new byte[size];

			is.read(buffer);

			is.close();

			json = new String(buffer, "UTF-8");

		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return json;

	}

	@Override
	public void openSlideMenu() {
		// TODO Auto-generated method stub

		if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			mDrawerLayout.openDrawer(mDrawerList);
		}

		//Toast.makeText(getApplicationContext(), "menu Clicked!",
		//Toast.LENGTH_LONG).show();

	}

	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	public Action getIndexApiAction() {
		Thing object = new Thing.Builder()
				.setName("Drawer Page") // TODO: Define a title for the content shown.
				// TODO: Make sure this auto-generated URL is correct.
				.setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
				.build();
		return new Action.Builder(Action.TYPE_VIEW)
				.setObject(object)
				.setActionStatus(Action.STATUS_TYPE_COMPLETED)
				.build();
	}

	@Override
	public void onStart() {
		super.onStart();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.connect();
		AppIndex.AppIndexApi.start(client, getIndexApiAction());
	}

	@Override
	public void onStop() {
		super.onStop();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		AppIndex.AppIndexApi.end(client, getIndexApiAction());
		client.disconnect();
	}

	/*
	 * ActionBar mActionBar = getActionBar();
	 * mActionBar.setDisplayShowHomeEnabled(false);
	 * mActionBar.setDisplayShowTitleEnabled(false); LayoutInflater mInflater =
	 * LayoutInflater.from(this);
	 * 
	 * View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
	 * mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
	 * mTitleTextView.setText("My Own Title");
	 * 
	 * ImageButton imageButton = (ImageButton) mCustomView
	 * .findViewById(R.id.imageButton); imageButton.setOnClickListener(new
	 * OnClickListener() {
	 * 
	 * @Override public void onClick(View view) {
	 * Toast.makeText(getApplicationContext(), "Refresh Clicked!",
	 * Toast.LENGTH_LONG).show(); } });
	 * 
	 * ImageButton btnMenu = (ImageButton) mCustomView
	 * .findViewById(R.id.imageButtonMenu); btnMenu.setOnClickListener(new
	 * OnClickListener() {
	 * 
	 * @Override public void onClick(View view) {
	 * 
	 * if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
	 * mDrawerLayout.closeDrawer(mDrawerList); } else {
	 * mDrawerLayout.openDrawer(mDrawerList); }
	 * 
	 * Toast.makeText(getApplicationContext(), "menu Clicked!",
	 * Toast.LENGTH_LONG).show(); } });
	 * 
	 * mActionBar.setCustomView(mCustomView);
	 * mActionBar.setDisplayShowCustomEnabled(true);
	 */

}
