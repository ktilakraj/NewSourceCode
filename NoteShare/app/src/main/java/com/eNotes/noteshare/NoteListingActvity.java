package com.eNotes.noteshare;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.eNotes.adpters.FolderListingAdapter;
import com.eNotes.adpters.NoteList_Adapter_New;
import com.eNotes.colorPicker.ColorPickerDialog;
import com.eNotes.dataAccess.DataManager;
import com.eNotes.notesharedatabase.DBNoteItemElement;
import com.eNotes.notesharedatabase.DBNoteItems;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
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


public class NoteListingActvity extends DrawerActivity {


    public ImageButton imageButtonFolder, imageButtonMore, imageButtonHamburg, btnAddNote;
    public NoteList_Adapter_New adapter_new;
    public ArrayList<DBNoteItems> arrDBDataNote;
    ListView listView;
    public ArrayList<DBNoteItems> arrDataFolder;
    ;
    public FolderListingAdapter folderListingAdapter;
    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID = 1;

    static final int Reminder_TIME_DIALOG_ID = 2;
    static final int Reminder_DATE_DIALOG_ID = 3;


    private int mYear, mMonth, mDay, mHour, mMinute, mSeconds;
    public int year, month, day, hour, minute, seconds;

    public String selecteddate, selectedTime, timeZone;
    public Calendar c;
    public DBNoteItems selectedDbItem;
    SharedPreferences preference;
    static String SAVELOCK = "LOCK";
    public SORTTYPE sortType;
    boolean isdateChnage = false, isTimeChange = false ,isfolderId =false;

    static String LOCKTYPE = "LOCKTYPE";
    static String REMINDTYPE = "REMINDTYPE";
    static String TIMEBOMBTYPE = "TIMEBOMBTYPE";

    LinearLayout LayoutNoNote;

    private AdView mAdView;
    private Button btnFullscreenAd;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    ArrayList<DBNoteItems> arrinsertedNote =null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_note_listing_actvity);

        DataManager.sharedDataManager().setSelectedItemIndex(-1);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_note_listing_actvity, null, false);

        mDrawerLayout.addView(contentView, 0);

        imageButtonMore = (ImageButton) contentView.findViewById(R.id.header_menu).findViewById(R.id.imageButtoncalander);
        imageButtonFolder = (ImageButton) contentView.findViewById(R.id.header_menu).findViewById(R.id.imageButtonsquence);
        imageButtonHamburg = (ImageButton) contentView.findViewById(R.id.header_menu).findViewById(R.id.imageButtonHamburg);
        listView = (ListView) contentView.findViewById(R.id.listviewNotes);
        btnAddNote = (ImageButton) contentView.findViewById(R.id.btnAddNote);

        LayoutNoNote = (LinearLayout) contentView.findViewById(R.id.LayoutNoNote);

        //Drawable mDrawable = this.getResources().getDrawable(R.drawable.create_circle);
       // mDrawable.setColorFilter(new PorterDuffColorFilter(0xFF00FF, PorterDuff.Mode.SRC_IN));
       // mDrawable.setColorFilter(0xff00ff00, PorterDuff.Mode.SRC);

        //btnAddNote.setBackground(mDrawable);
        // mDrawable.setColorFilter(new PorterDuffColorFilter(0x00000,PorterDuff.Mode.MULTIPLY));


        arrDBDataNote = new ArrayList<DBNoteItems>();


        adapter_new = new NoteList_Adapter_New(NoteListingActvity.this, arrDBDataNote, new NoteList_Adapter_New.NoteList_Adapter_New_Listner() {
            @Override
            public void didMoreSelected(final DBNoteItems item1, View selectedbutton, int selectedindex) {

                selectedDbItem = item1;

                switch (selectedbutton.getId()) {
                    case R.id.editNoteTitle: {
                        showAlertWithUpdateTitleEditText(NoteListingActvity.this, item1, item1.getNote_Title());
                    }
                    break;
                    case R.id.noteDelete: {
                        showAlertWithDeleteMessage("Are you sure you want delete this note", NoteListingActvity.this, item1);
                    }
                    break;
                    case R.id.noteTimeBomb: {

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
                    case R.id.noteLock: {

                        showAlertToLockNUnlockNote(item1, NoteListingActvity.this, true, false);

                    }
                    break;
                    case R.id.noteunlockLock: {
                        showAlertToLockNUnlockNote(item1, NoteListingActvity.this, false, false);
                    }
                    break;
                    case R.id.noteShare: {

                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/html");
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("<p>" + item1.getNote_Title().toString() + "</p>"));
                        startActivity(Intent.createChooser(sharingIntent, "Share using"));

                    }
                    break;

                    case R.id.noteMoveToFolder: {
                        if (arrDataFolder != null) {
                            if (arrDataFolder.size() > 0) {
                                showMoveToFolderDialog("", NoteListingActvity.this, item1);
                            } else {
                                showAlertWithFolder("Please create at least one folder", NoteListingActvity.this);
                            }
                        }
                    }
                    break;

                    case R.id.noteAddColor: {

                        showColorPickerDialogDemo(selectedDbItem);

                    }
                    break;
                    case R.id.noteremindertime: {

                        showDialog(Reminder_DATE_DIALOG_ID);

                    }
                    break;

                    case R.id.imageLock: {

                        // showAlertWithMessage("Do you want to remove lock?",NoteListingActvity.this,selectedDbItem,LOCKTYPE);

                    }
                    break;
                    case R.id.imageTimeBomb: {

                        showAlertWithMessage("Do you want to remove or reset time bomb?", NoteListingActvity.this, selectedDbItem, TIMEBOMBTYPE);

                    }
                    break;

                    case R.id.imageReminder: {
                        showAlertWithMessage("Are you want to remove or reset reminder time?", NoteListingActvity.this, selectedDbItem, REMINDTYPE);
                    }
                    break;

                    default:
                        break;
                }


            }

            @Override
            public void didlistItemClick(DBNoteItems item1, View selectedbutton, int selectedindex) {

                Log.d("The listitem : ", "ID: " + item1.getNote_Id() + "\n");
                Log.d("The listitem: ", "Text: " + selectedbutton.getTag() + "\n");
                Log.d("The listitem: ", "Index: " + selectedindex + "\n");


                selectedDbItem = item1;
                DataManager.sharedDataManager().setSeletedDBNoteItem(item1);


                if (true)///menuItem.isIsdefaultNote()!=true)
                {

                    if (item1.getNote_Lock_Status() != null) {
                        if (item1.getNote_Lock_Status().equalsIgnoreCase("1")) {

                            showAlertToLockNUnlockNote(item1, NoteListingActvity.this, false, true);
                        } else {

                            //DataManager.sharedDataManager().setSeletedDBNoteItem(item1);
                            startActivity(new Intent(NoteListingActvity.this, NoteMainActivity.class));
                        }

                    } else {

                        //DataManager.sharedDataManager().setSeletedDBNoteItem(item1);
                         startActivity(new Intent(NoteListingActvity.this, NoteMainActivity.class));

                        //startActivity(new Intent(NoteListingActvity.this, NoteDetailActivity_1.class));
                    }


                } else {
                    Toast.makeText(NoteListingActvity.this, "Default Note can't be open", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void didExpandViewAtIndex(DBNoteItems item1, View selectedbutton, int selectedindex) {


                if (DataManager.sharedDataManager().getSelectedItemIndex() != -1) {
                    if (DataManager.sharedDataManager().getSelectedItemIndex() == selectedindex) {
                        DataManager.sharedDataManager().setSelectedItemIndex(-1);

                    } else {
                        DataManager.sharedDataManager().setSelectedItemIndex(selectedindex);
                    }


                } else {
                    DataManager.sharedDataManager().setSelectedItemIndex(selectedindex);
                }

                adapter_new.notifyDataSetChanged();

            }
        });


        listView.setAdapter(adapter_new);
        adapter_new.notifyDataSetChanged();


        addListners_new();
        getallNotes();
        getAllFolder();
        initilizesDateAndTime();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        loadAds();
    }

    void  loadAds()
    {
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4042620180347128~4456337699");


        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
    }

    private void showColorPickerDialogDemo(final DBNoteItems items) {

        int initialColor = Color.WHITE;

        com.eNotes.colorPicker.ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this, initialColor, new ColorPickerDialog.OnColorSelectedListener() {


            public void onColorSelected(int color) {
                //showToast(color);


                String hexColor = String.format("#%06X", (0xFFFFFF & color));
                Log.d("the Selected Color:", "" + hexColor);

                items.setNote_Color(hexColor);
                boolean status = androidOpenDbHelperObj.updateNoteitems_Color(items);


                if (status == true) {
                    Toast.makeText(NoteListingActvity.this,
                            "Color added successfully",
                            Toast.LENGTH_SHORT).show();
                    getallNotes();

                } else {

                    Toast.makeText(NoteListingActvity.this,
                            "Color added unsuccessfully",
                            Toast.LENGTH_SHORT).show();
                }


            }

        });
        colorPickerDialog.show();


    }


    void showSettingDialogWith(String message, Context context) {

        final Dialog dialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.more_setting_view, null, false);

        TextView textViewTitleAlert = (TextView) contentView
                .findViewById(R.id.textViewTitleAlert);
        //  textViewTitleAlert.setText("" + message);
        // textViewTitleAlert.setTextColor(Color.WHITE);
        TextView textViewTitleAlertMessage = (TextView) contentView
                .findViewById(R.id.textViewTitleAlertMessage);
        // textViewTitleAlertMessage.setText(message);

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
                // System.exit(0);
                dialog.dismiss();

            }
        });

        textViewTitleAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //View By

                showActionSheet(view);
                dialog.dismiss();

            }
        });

        textViewTitleAlertMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Sort By

                showActionSheet_sort(view);
                dialog.dismiss();

            }
        });


        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        window.setGravity(Gravity.RIGHT | Gravity.TOP);
        dialog.getWindow().getAttributes().verticalMargin = 0.01F;
        dialog.getWindow().getAttributes().horizontalMargin = 0.01F;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);

        dialog.setContentView(contentView);
        dialog.show();

    }


    public void showActionSheet(View v) {

        final Dialog myDialog = new Dialog(NoteListingActvity.this,
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

        layoutGridTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // notefoleserGridList.setVisibility(View.VISIBLE);
                // notefoleserList.setVisibility(View.GONE);
                // notefoleserPintrestList.setVisibility(View.GONE);
                myDialog.dismiss();

            }
        });

        layoutPintrestTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // notefoleserGridList.setVisibility(View.GONE);
                // notefoleserList.setVisibility(View.GONE);
                // notefoleserPintrestList.setVisibility(View.VISIBLE);

                // updatePintrestView();
                myDialog.dismiss();

            }
        });

        layoutDetailTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                DataManager.sharedDataManager().setTypeofListView(false);

                // TODO Auto-generated method stub
                // notefoleserGridList.setVisibility(View.GONE);
                // notefoleserList.setVisibility(View.VISIBLE);
                //notefoleserPintrestList.setVisibility(View.GONE);

                // DataManager.sharedDataManager().setSelectedIndex(-1);
                adapter.notifyDataSetChanged();
                myDialog.dismiss();

            }
        });

        layoutListTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                DataManager.sharedDataManager().setTypeofListView(true);

                // notefoleserGridList.setVisibility(View.GONE);
                // notefoleserList.setVisibility(View.VISIBLE);
                // notefoleserPintrestList.setVisibility(View.GONE);
                //DataManager.sharedDataManager().setSelectedIndex(-1);
                adapter.notifyDataSetChanged();
                myDialog.dismiss();

            }
        });

        buttonDissmiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                myDialog.dismiss();
            }
        });

        // myDialog.getWindow().getAttributes().windowAnimations = R.anim.slide_up_1;
        myDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        myDialog.show();


        myDialog.getWindow().setGravity(Gravity.CENTER);

    }

    public void showActionSheet_sort(View v) {

        final Dialog myDialog = new Dialog(NoteListingActvity.this,
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
        layoutDetailTextView.setText("Colors");

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

        ImageView layoutListImageViewTimebomb = (ImageView) layoutListTimeBomb
                .findViewById(R.id.imageViewSlidemenu);
        layoutListImageViewTimebomb
                .setImageResource(R.drawable.timebomb_sort_view_1);




        layoutListTextViewTimeBomb.setText("Time Bomb");

        layoutGridTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //Toast.makeText(getApplicationContext(), "Created Time",
                       // Toast.LENGTH_SHORT).show();
                sortType = SORTTYPE.CREATED_TIME;
                sortingArray();
                adapter.notifyDataSetChanged();

                myDialog.dismiss();

            }
        });

        layoutPintrestTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //Toast.makeText(getApplicationContext(), "Modified Time",
                        //Toast.LENGTH_SHORT).show();
                sortType = SORTTYPE.MODIFIED_TIME;
                sortingArray();
                adapter.notifyDataSetChanged();

                myDialog.dismiss();

            }
        });

        layoutDetailTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                //Toast.makeText(getApplicationContext(), "Colours",
                       // Toast.LENGTH_SHORT).show();

                sortType = SORTTYPE.COLOURS;
                sortingArray();
                adapter.notifyDataSetChanged();

                myDialog.dismiss();

            }
        });

        layoutListTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub



                sortType = SORTTYPE.ALPHABET;
                sortingArray();
                adapter.notifyDataSetChanged();

               // Toast.makeText(getApplicationContext(), "Alphabetical",
                       // Toast.LENGTH_SHORT).show();

                myDialog.dismiss();

            }
        });

        layoutListTextViewTimeBomb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

               // Toast.makeText(getApplicationContext(), "Time Bomb",
                       // Toast.LENGTH_SHORT).show();



                sortType = SORTTYPE.TIME_BOMB;
                sortingArray();
                adapter.notifyDataSetChanged();

                myDialog.dismiss();

            }
        });

        layoutListTextViewReminderTime
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        //Toast.makeText(getApplicationContext(),
                                //"Reminder Time", Toast.LENGTH_SHORT).show();

                        sortType = SORTTYPE.REMINDER_TIME;
                        sortingArray();
                        adapter.notifyDataSetChanged();

                        myDialog.dismiss();
                    }
                });

        buttonDissmiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                myDialog.dismiss();
            }
        });

        // myDialog.getWindow().getAttributes().windowAnimations = R.anim.slide_up_1;

        myDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

        myDialog.show();

        myDialog.getWindow().setGravity(Gravity.CENTER);

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

               // Collections.sort(arrDBDataNote, DBNoteItems.DBNoteTitle);

                adapter_new.notifyDataSetChanged();

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
                adapter_new.notifyDataSetChanged();


            }
            break;
            case CREATED_TIME: {

                Collections.sort(arrDBDataNote, new Comparator<DBNoteItems>() {

                    @Override
                    public int compare(DBNoteItems lhs, DBNoteItems rhs) {
                        // TODO Auto-generated method stub
                        return rhs.getNote_Created_Time().compareToIgnoreCase(
                                lhs.getNote_Created_Time());
                    }
                });
                adapter_new.notifyDataSetChanged();
            }
            break;
            case MODIFIED_TIME: {

                Collections.sort(arrDBDataNote, new Comparator<DBNoteItems>() {

                    @Override
                    public int compare(DBNoteItems lhs, DBNoteItems rhs) {
                        // TODO Auto-generated method stub
                        return rhs.getNote_Modified_Time().compareToIgnoreCase(
                                lhs.getNote_Modified_Time());
                    }
                });
                adapter_new.notifyDataSetChanged();

            }
            break;
            case REMINDER_TIME: {

                Collections.sort(arrDBDataNote, new Comparator<DBNoteItems>() {

                    @Override
                    public int compare(DBNoteItems lhs, DBNoteItems rhs) {
                        // TODO Auto-generated method stub
                        return rhs.getNote_Reminder_Time().compareToIgnoreCase(
                                lhs.getNote_Reminder_Time());
                    }
                });
                adapter_new.notifyDataSetChanged();

            }
            break;
            case TIME_BOMB: {

                Collections.sort(arrDBDataNote, new Comparator<DBNoteItems>() {

                    @Override
                    public int compare(DBNoteItems lhs, DBNoteItems rhs) {
                        // TODO Auto-generated method stub
                        return rhs.getNote_TimeBomb().compareToIgnoreCase(
                                lhs.getNote_TimeBomb());
                    }
                });
                adapter_new.notifyDataSetChanged();


            }
            break;

            default:
                break;
        }
    }

    void showAlertWithFolder(String message, Context context) {

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
               // System.exit(0);
                dialog.dismiss();
                startActivity(new Intent(NoteListingActvity.this, NewFolderMainActivity.class));


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
                System.exit(0);

            }
        });

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);

        dialog.setContentView(contentView);
        dialog.show();

    }

    void showMoveToFolderDialog(String message, Context context, final DBNoteItems items) {


        folderListingAdapter = new FolderListingAdapter(NoteListingActvity.this, arrDataFolder);
        final Dialog dialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.folderlisting_view, null, false);

        TextView textViewTitleAlert = (TextView) contentView
                .findViewById(R.id.textViewTitleAlert);
        textViewTitleAlert.setText("Move to folder");
        textViewTitleAlert.setTextColor(Color.WHITE);

        ListView listfolderView = (ListView) contentView.findViewById(R.id.listViewFolderList);
        listfolderView.setAdapter(folderListingAdapter);
        folderListingAdapter.notifyDataSetChanged();


        listfolderView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (items != null) {
                    DBNoteItems itemSelected = arrDataFolder.get(i);
                    items.setNote_Folder_Id(itemSelected.getNote_Id());

                    boolean status = androidOpenDbHelperObj.updateNoteitems_MoveToFolder(items);

                    if (status == true) {

                        Toast.makeText(NoteListingActvity.this, "Moved successfully", Toast.LENGTH_SHORT).show();
                        getallNotes();
                    } else {
                        Toast.makeText(NoteListingActvity.this, "Move unsuccessfully", Toast.LENGTH_SHORT).show();

                    }
                }

                dialog.dismiss();

            }
        });


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
                dialog.dismiss();

            }
        });

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);

        dialog.setContentView(contentView);
        dialog.show();

    }


    void showAlertWithBackPress(String message, Context context) {

        final Dialog dialog = new Dialog(context);


        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.alert_view_back, null, false);

        TextView textViewTitleAlert = (TextView) contentView
                .findViewById(R.id.textViewTitleAlert);
        textViewTitleAlert.setBackgroundColor(Color.WHITE);
        textViewTitleAlert.setText("");
        textViewTitleAlert.setTextColor(Color.WHITE);
        textViewTitleAlert.setVisibility(View.GONE);
        TextView textViewTitleAlertMessage = (TextView) contentView
                .findViewById(R.id.textViewTitleAlertMessage);
        textViewTitleAlertMessage.setText("Do you want to exit?");

        Button buttonAlertCancel = (Button) contentView
                .findViewById(R.id.buttonAlertCancel);
        Button buttonAlertOk = (Button) contentView
                .findViewById(R.id.buttonAlertOk);
        buttonAlertCancel.setText("No");
        buttonAlertOk.setText("Yes");
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
                //
                dialog.dismiss();
                finish();
                System.exit(0);



            }
        });

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);

        dialog.setContentView(contentView);
        dialog.show();

    }


    void showAlertWithMessage(String message, Context context, final DBNoteItems dbNoteItems, final String Type) {

        final Dialog dialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.reset_alert_view, null, false);

        TextView textViewTitleAlert = (TextView) contentView
                .findViewById(R.id.textViewTitleAlert);
        textViewTitleAlert.setText("" + dbNoteItems.getNote_Title());
        textViewTitleAlert.setTextColor(Color.WHITE);
        TextView textViewTitleAlertMessage = (TextView) contentView
                .findViewById(R.id.textViewTitleAlertMessage);
        textViewTitleAlertMessage.setText(message);

        Button buttonAlertCancel = (Button) contentView
                .findViewById(R.id.buttonAlertCancel);
        Button buttonAlertRemove = (Button) contentView
                .findViewById(R.id.buttonAlertOk);

        Button buttonAlertReset = (Button) contentView
                .findViewById(R.id.buttonAlertReset);


        buttonAlertCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0)

            {
                // TODO Auto-generated method stub
                dialog.dismiss();

            }
        });
        buttonAlertRemove.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                //remove

                if (Type.equalsIgnoreCase(REMINDTYPE)) {
                    dbNoteItems.setNote_Reminder_Time("0");
                    boolean status = androidOpenDbHelperObj.updateNoteitems_ReminderTime(dbNoteItems);


                    if (status == true) {
                        Toast.makeText(NoteListingActvity.this,
                                "Reminder  Remove successfully",
                                Toast.LENGTH_SHORT).show();
                        getallNotes();

                    } else {

                        Toast.makeText(NoteListingActvity.this,
                                "Reminder Remove unsuccessfully",
                                Toast.LENGTH_SHORT).show();
                    }
                } else if (Type.equalsIgnoreCase(TIMEBOMBTYPE)) {
                    dbNoteItems.setNote_TimeBomb("0");
                    boolean status = androidOpenDbHelperObj.updateNoteitems_timeBomb(dbNoteItems);


                    if (status == true) {
                        Toast.makeText(NoteListingActvity.this,
                                "Timebomb  Remove successfully",
                                Toast.LENGTH_SHORT).show();
                        getallNotes();

                    } else {

                        Toast.makeText(NoteListingActvity.this,
                                "Timebomb Remove unsuccessfully",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                dialog.dismiss();

            }
        });

        buttonAlertReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //reset


                if (Type.equalsIgnoreCase(REMINDTYPE)) {

                    showDialog(Reminder_DATE_DIALOG_ID);
                } else if (Type.equalsIgnoreCase(TIMEBOMBTYPE)) {
                    showDialog(DATE_DIALOG_ID);
                }


                dialog.dismiss();

            }
        });

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(contentView);
        dialog.show();

    }

    void showAlertWithDeleteMessage(String message, Context context, final DBNoteItems dbNoteItems) {

        final Dialog dialog = new Dialog(context);

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
                dialog.dismiss();

            }
        });
        buttonAlertOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                //boolean status = androidOpenDbHelperObj.deleteNote(dbNoteItems.getNote_Id());
                boolean status = androidOpenDbHelperObj.updateNoteitems_Delete(dbNoteItems.getNote_Id(),"-1");
                //
                if (status == true) {

                    Toast.makeText(NoteListingActvity.this, "Note deleted successfully", Toast.LENGTH_SHORT).show();
                    getallNotes();
                } else {
                    Toast.makeText(NoteListingActvity.this, "Note deleted unsuccessfully", Toast.LENGTH_SHORT).show();

                }
                dialog.dismiss();

            }
        });

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(contentView);
        dialog.show();

    }

    void showAlertWithUpdateTitleEditText(Context context, final DBNoteItems selectedItem, String message) {

        final Dialog dialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // inflate your activity layout here!

        // LinearLayout layoutAlertbox1=

        View contentView = inflater.inflate(R.layout.edit_alert_view, null,
                false);
        TextView textViewTitleAlert = (TextView) contentView
                .findViewById(R.id.textViewTitleAlert);
        textViewTitleAlert.setText("Edit eNote title");
        textViewTitleAlert.setTextColor(Color.WHITE);
        final EditText textViewTitleAlertMessage = (EditText) contentView
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
                if (textViewTitleAlertMessage.getText().toString().length() > 0) {

                    // Call insert db call

                    SimpleDateFormat dateFormatGmt = new SimpleDateFormat(
                            "dd/MM/yyyy HH:mm:ss");
                   // dateFormatGmt.setTimeZone(TimeZone.getDefault());
                    dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
                    Date d = new Date();
                    String strDate = dateFormatGmt.format(d);
                    System.out.println("The note created date: " + strDate
                            + "note title: "
                            + textViewTitleAlertMessage.getText().toString());

                    selectedItem.setNote_Title(textViewTitleAlertMessage.getText().toString());

                    boolean status = androidOpenDbHelperObj.updateNoteitems_title(selectedItem);


                    if (status == true) {
                        //.makeText(NoteListingActvity.this,
                               // "data inserted successfully",
                               // Toast.LENGTH_SHORT).show();


                        ArrayList<DBNoteItems> selectecitem_list = androidOpenDbHelperObj.getAllNotesWithNote_Id(selectedItem.getNote_Id());

                        getallNotes();


                    } else {

                        Toast.makeText(NoteListingActvity.this,
                                "data not inserted successfully",
                                Toast.LENGTH_SHORT).show();
                    }

                    dialog.dismiss();
                }

            }
        });
        buttonAlertOk.setOnClickListener(new View.OnClickListener() {

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

    void addListners_new() {

        imageButtonHamburg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openSlideMenu();


            }
        });

        imageButtonMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("THE MORE CLICK", "MORE BUTTON PRESS");

               // showSettingDialogWith("", NoteListingActvity.this);

                showActionSheet_sort(view);

            }
        });

        imageButtonFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("THE FOLDER CLICK", "FOLDER BUTTON PRESS");

                if (arrDataFolder.size()>0) {
                    showMoveToFolderDialog("FOLDERS", NoteListingActvity.this, null);
                }



            }
        });

        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showAlertWithEditText(NoteListingActvity.this);

            }
        });


    }

    void getAllFolder() {

        ArrayList<DBNoteItems> tempArrDataFolder = androidOpenDbHelperObj
                .getAllFolder();

         arrDataFolder = new ArrayList<>();
        for (DBNoteItems  dbNoteItems :tempArrDataFolder) {
            if (dbNoteItems.getNote_Deleted_Status()!= null) {
                if (dbNoteItems.getNote_Deleted_Status().equalsIgnoreCase("-1")) {

                } else {
                    arrDataFolder.add(dbNoteItems);
                }
            } else {

                arrDataFolder.add(dbNoteItems);
            }
        }

        if (arrDataFolder.size()>0) {

            imageButtonFolder.setVisibility(View.VISIBLE);
        } else {
            imageButtonFolder.setVisibility(View.GONE);
        }
    }

    void getallNotes() {
        //arrDataNote.clear();
        arrDBDataNote.clear();
        DataManager.sharedDataManager().setSelectedItemIndex(-1);


        isfolderId=false;
        if (DataManager.sharedDataManager().getSelectedFolderId() !=null){

            if ( DataManager.sharedDataManager().getSelectedFolderId().length()>0) {

                isfolderId=true;
                arrinsertedNote = androidOpenDbHelperObj
                        .getAllNotesWithFolder_Id(DataManager.sharedDataManager().getSelectedFolderId());

            } else {
                arrinsertedNote = androidOpenDbHelperObj
                        .getAllNotes();
            }
        } else {

            arrinsertedNote = androidOpenDbHelperObj
                    .getAllNotes();
        }



        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Your code to run in GUI thread here

                updateDataOnListView(arrinsertedNote);

                if (arrinsertedNote.size()>0) {

                    imageButtonMore.setVisibility(View.VISIBLE);
                    LayoutNoNote.setVisibility(View.GONE);
                } else {
                    imageButtonMore.setVisibility(View.GONE);
                    LayoutNoNote.setVisibility(View.VISIBLE);
                }


            }//public void run() {
        });

    }

    void  updateDataOnListView(ArrayList<DBNoteItems> arrinsertedNote)
    {
        for (DBNoteItems dbNoteItems : arrinsertedNote) {
            System.out.println("note id: " + dbNoteItems.getNote_Id()
                    + " note_title: " + dbNoteItems.getNote_Title());


            ArrayList<DBNoteItemElement> dbNoteItemElements = androidOpenDbHelperObj.getLastNotesElementWithNote_Id(dbNoteItems.getNote_Id());

            dbNoteItems.setNote_Element("" + (dbNoteItemElements.size() > 0 ? dbNoteItemElements.get(0).getNOTE_ELEMENT_TYPE() : ""));


            if (dbNoteItems.getNote_Color().length() > 0
                    && dbNoteItems.getNote_Color() != null) {

                // item1.setColours("#" + dbNoteItems.getNote_Color());
            }


            if (dbNoteItems.getNote_TimeBomb() != null)

            {
                if (dbNoteItems.getNote_TimeBomb().equalsIgnoreCase("0")) {

                    Log.d("time bomb", "not set for this note");
                    //arrDataNote.add(item1);
                    arrDBDataNote.add(dbNoteItems);
                } else {
                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "dd/MM/yyyy HH:mm:ss", Locale.US);


                    Date currentdate = new Date();
                    String strDate = formatter.format(currentdate);


                    try {

                        Date timebombdate = formatter.parse(dbNoteItems.getNote_TimeBomb());

                        Date dateCurrent = formatter.parse(strDate);

                        System.out.println(timebombdate);
                        System.out.println(formatter.format(timebombdate));

                        if (dateCurrent.compareTo(timebombdate) > 0) {
                            System.out.println("Date1 is after Date2 --Delete note here");

                            androidOpenDbHelperObj.deleteNote(dbNoteItems.getNote_Id());
                        } else if (dateCurrent.compareTo(timebombdate) < 0) {
                            System.out.println("Date1 is before Date2");


                            arrDBDataNote.add(dbNoteItems);
                        } else if (dateCurrent.compareTo(timebombdate) == 0) {
                            System.out.println("Date1 is equal to Date2");
                            //arrDataNote.add(item1);
                            //arrDBDataNote.add(dbNoteItems);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }


        }
        adapter_new.notifyDataSetChanged();
    }

    void showAlertWithEditText(final Context context) {

        final Dialog dialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // inflate your activity layout here!

        // LinearLayout layoutAlertbox1=

        View contentView = inflater.inflate(R.layout.edit_alert_view, null,
                false);
        TextView textViewTitleAlert = (TextView) contentView
                .findViewById(R.id.textViewTitleAlert);
        textViewTitleAlert.setText("Create a note");
        textViewTitleAlert.setTextColor(Color.WHITE);
        final EditText textViewTitleAlertMessage = (EditText) contentView
                .findViewById(R.id.textViewTitleAlertMessage);
        // textViewTitleAlertMessage.setText(message);

        Button buttonAlertCancel = (Button) contentView
                .findViewById(R.id.buttonAlertCancel);
        Button buttonAlertOk = (Button) contentView
                .findViewById(R.id.buttonAlertOk);

        textViewTitleAlert.requestFocus();
      final   InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


        buttonAlertCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (textViewTitleAlertMessage.getText().toString().length() > 0) {

                    // Call insert db call

                    SimpleDateFormat dateFormatGmt = new SimpleDateFormat(
                            "dd/MM/yyyy HH:mm:ss");
                    //dateFormatGmt.setTimeZone(TimeZone.getDefault());
                    dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
                    Date d = new Date();
                    String strDate = dateFormatGmt.format(d);


                    System.out.println("The note created date: " + strDate
                            + "note title: "
                            + textViewTitleAlertMessage.getText().toString());

                    boolean status=false;;

                    if (isfolderId == true) {

                        String folderID=DataManager.sharedDataManager().getSelectedFolderId();
                      status =  androidOpenDbHelperObj.databaseInsertion(
                                textViewTitleAlertMessage.getText().toString(),
                                "cccccc", strDate, strDate, "0", "0", "1234",folderID,
                                "", "", "0");

                    } else {

                        status = androidOpenDbHelperObj.databaseInsertion(
                                textViewTitleAlertMessage.getText().toString(),
                                "cccccc", strDate, strDate, "0", "0", "1234", "",
                                "", "", "0");
                    }


                    if (status == true) {
                       // Toast.makeText(NoteListingActvity.this,
                               // "data inserted successfully",
                               // Toast.LENGTH_SHORT).show();

                        getNoteWithTitle(textViewTitleAlertMessage.getText()
                                .toString());

                        DBNoteItems item = arrDBDataNote.get(arrDBDataNote.size() - 1);

                        // ArrayList<DBNoteItems>	selectecitem_list=androidOpenDbHelperObj.getAllNotesWithNote_Id(item.getNote_Id());

                        if (item != null) {
                            DataManager.sharedDataManager().setSeletedDBNoteItem(item);
                            startActivity(new Intent(NoteListingActvity.this, NoteMainActivity.class));

                        }




                    } else {

                        Toast.makeText(NoteListingActvity.this,
                                "data not inserted successfully",
                                Toast.LENGTH_SHORT).show();
                    }

                    dialog.dismiss();
                } else {

                    Toast.makeText(NoteListingActvity.this,
                            "Please create a note to submit",
                            Toast.LENGTH_SHORT).show();

                }
               // imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

            }
        });
        buttonAlertOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                dialog.dismiss();

                // System.exit(0);
                //imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

            }
        });

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        dialog.setCancelable(true);
        dialog.setContentView(contentView);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
        dialog.show();

    }


    void getNoteWithTitle(String note_title) {
        ArrayList<DBNoteItems> arrinsertedNote = androidOpenDbHelperObj.getAllNotesWithTitle(note_title);

        for (DBNoteItems dbNoteItems : arrinsertedNote) {
            System.out.println("note id: " + dbNoteItems.getNote_Id()
                    + " note_title: " + dbNoteItems.getNote_Title());
            arrDBDataNote.add(dbNoteItems);
        }
        adapter_new.notifyDataSetChanged();

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


    // Method automatically gets Called when you call showDialog()  method
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

                                Toast.makeText(NoteListingActvity.this, "Selected date:" + selecteddate, Toast.LENGTH_SHORT).show();

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
                                Toast.makeText(NoteListingActvity.this, "Selected Time:" + selectedTime, Toast.LENGTH_SHORT).show();


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
                    Toast.makeText(NoteListingActvity.this, "Selected Time:" + selectedTime, Toast.LENGTH_SHORT).show();


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

                    Toast.makeText(NoteListingActvity.this, "Selected date:" + selecteddate, Toast.LENGTH_SHORT).show();

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

            //Toast.makeText(NoteListingActvity.this, "the final time bomb :" + strDate, Toast.LENGTH_LONG).show();

            selectedDbItem.setNote_Reminder_Time(strDate);
            boolean status = androidOpenDbHelperObj.updateNoteitems_ReminderTime(selectedDbItem);


            if (status == true) {
                Toast.makeText(NoteListingActvity.this,
                        "Reminder  set successfully",
                        Toast.LENGTH_SHORT).show();
                getallNotes();

            } else {

                Toast.makeText(NoteListingActvity.this,
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

          //  Toast.makeText(NoteListingActvity.this, "the final time bomb :" + strDate, Toast.LENGTH_LONG).show();

            selectedDbItem.setNote_TimeBomb(strDate);
            boolean status = androidOpenDbHelperObj.updateNoteitems_timeBomb(selectedDbItem);


            if (status == true) {
                Toast.makeText(NoteListingActvity.this,
                        "time bomb set successfully",
                        Toast.LENGTH_SHORT).show();
                getallNotes();

            } else {

                Toast.makeText(NoteListingActvity.this,
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


        preference = PreferenceManager.getDefaultSharedPreferences(NoteListingActvity.this);
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
        ;

        if (textLock.getText().length() == 4) {
            preference = PreferenceManager.getDefaultSharedPreferences(NoteListingActvity.this);
            if (!preference.getString(SAVELOCK, "").equalsIgnoreCase("")) {
                if (preference.getString(SAVELOCK, "").equalsIgnoreCase(textLock.getText().toString())) {

                    //Toast.makeText(NoteListingActvity.this, "Password matched,note Open.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                    if (isOpenLockedNote == false) {

                        if (islock == true) {
                            selectedDbItem.setNote_Lock_Status("1");
                            boolean status = androidOpenDbHelperObj.updateNoteitems_Notelock(selectedDbItem);


                            if (status == true) {

                                Toast.makeText(NoteListingActvity.this, "Note locked", Toast.LENGTH_SHORT).show();

                            } else {

                                Toast.makeText(NoteListingActvity.this, "Note Not locked", Toast.LENGTH_SHORT).show();
                            }


                            getallNotes();

                        } else {

                            selectedDbItem.setNote_Lock_Status("0");
                            boolean status = androidOpenDbHelperObj.updateNoteitems_Notelock(selectedDbItem);


                            if (status == true) {

                                Toast.makeText(NoteListingActvity.this, "Note unlocked", Toast.LENGTH_SHORT).show();

                            } else {

                                Toast.makeText(NoteListingActvity.this, "Note not Unlocked", Toast.LENGTH_SHORT).show();
                            }

                            getallNotes();
                        }
                    } else {

                        DataManager.sharedDataManager().setSeletedDBNoteItem(selectedDbItem);
                        startActivity(new Intent(NoteListingActvity.this, NoteMainActivity.class));
                    }

                } else {
                    //textLock.setText("");

                    Toast.makeText(NoteListingActvity.this, "Password not matched", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            } else {
                SharedPreferences.Editor editor = preference.edit();
                editor.putString(SAVELOCK, textLock.getText() + "");
                editor.commit();

                if (islock == true) {
                    selectedDbItem.setNote_Lock_Status("1");
                    boolean status = androidOpenDbHelperObj.updateNoteitems_Notelock(selectedDbItem);


                    if (status == true) {

                        Toast.makeText(NoteListingActvity.this, "Note locked", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(NoteListingActvity.this, "Note not locked", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    selectedDbItem.setNote_Lock_Status("0");
                    boolean status = androidOpenDbHelperObj.updateNoteitems_Notelock(selectedDbItem);


                    if (status == true) {

                        Toast.makeText(NoteListingActvity.this, "Note unlocked", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(NoteListingActvity.this, "Note not Unlocked", Toast.LENGTH_SHORT).show();
                    }
                }


                dialog.dismiss();

            }


        }
    }

    @Override
    public void onBackPressed() {

        if(isfolderId == false) {

            showAlertWithBackPress("",this);
        }
        else  {
            finish();
        }
    }

    @Override
    protected void onResume() {
        //DataManager.sharedDataManagtr().setSelectedItemIndex(-1);
        super.onResume();
        getallNotes();
        Log.d("Onresume", "Onresume Call");
        if (mAdView != null) {
            mAdView.resume();
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "NoteListingActvity Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.mobiapp.ventures.eNotes.noteshare/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "NoteListingActvity Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.mobiapp.ventures.eNotes.noteshare/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }


    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
