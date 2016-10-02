package com.eNotes.noteshare;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eNotes.Utlity.DialogUtill;
import com.eNotes.adpters.TrashAdapter;
import com.eNotes.notesharedatabase.DBNoteItems;
import com.eNotes.notesharedatabase.NoteshareDatabaseHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.mobiapp.ventures.eNotes.R;

import java.util.ArrayList;


public class TrashActivity extends DrawerActivity {

    public TextView textViewheaderTitle,textDeletedNote;
    public RelativeLayout layoutHeader,LayoutDeleteNote;
    public ImageButton imageButtonHamburg;
    public ListView notefoleserList;
    public Button btnNoteClick,btnFolderClick;
    NoteshareDatabaseHelper androidOpenDbHelperObj;
    SQLiteDatabase sqliteDatabase;
    ArrayList<DBNoteItems> arrDeletedFolder,arrDeletedNotes,arrDeletedNotesCommon;
    final Context context = this;
    private TrashAdapter trashAdapter;

    private  int selectedTabs=R.id.btnNoteClick;
    private AdView mAdView;
    private Button btnFullscreenAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_trash);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_trash, null, false);
        mDrawerLayout.addView(contentView, 0);
        initlizeUIElement(contentView);

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

    void initlizeUIElement(View contentview) {

        layoutHeader = (RelativeLayout) contentview.findViewById(R.id.mainHeadermenue);
        LayoutDeleteNote = (RelativeLayout) contentview.findViewById(R.id.LayoutDeleteNote);
        textDeletedNote = (TextView)LayoutDeleteNote.findViewById(R.id.textDeletedNote);
        LayoutDeleteNote.setVisibility(View.GONE);
        textViewheaderTitle = (TextView) layoutHeader.findViewById(R.id.textViewheaderTitle);
        textViewheaderTitle.setText("Trash");
        textDeletedNote.setText("");
        imageButtonHamburg = (ImageButton) layoutHeader
                .findViewById(R.id.imageButtonHamburg);
        notefoleserList = (ListView) findViewById(R.id.trashlist);
        btnNoteClick=(Button) contentview.findViewById(R.id.btnNoteClick);
        btnFolderClick=(Button) contentview.findViewById(R.id.btnFolderClick);
        arrDeletedNotesCommon=new ArrayList<>();
        trashAdapter = new TrashAdapter(this, arrDeletedNotesCommon,adapterListener());
        notefoleserList.setAdapter(trashAdapter);
        btnNoteClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resettabs(v.getId());
            }
        });

        btnFolderClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resettabs(v.getId());
            }
        });
        imageButtonHamburg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                openSlideMenu();

            }
        });

        getDeletedItems();
    }

    void  resettabs(int viewID) {

        btnFolderClick.setBackgroundColor(getResources().getColor(R.color.header_bg));
        btnNoteClick.setBackgroundColor(getResources().getColor(R.color.header_bg));
        LayoutDeleteNote.setVisibility(View.GONE);
       switch (viewID) {
           case R.id.btnFolderClick:
           {
               btnFolderClick.setBackgroundColor(getResources().getColor(R.color.A8b241b));
               if (arrDeletedFolder.size()<= 0){
                   LayoutDeleteNote.setVisibility(View.VISIBLE);
                   textDeletedNote.setText("No folder in trash");
               } else {
                   arrDeletedNotesCommon.clear();
                   arrDeletedNotesCommon.addAll(arrDeletedFolder);
               }
           }
           break;
           case R.id.btnNoteClick:
           {
               btnNoteClick.setBackgroundColor(getResources().getColor(R.color.A8b241b));
               if (arrDeletedNotes.size()<= 0){
                   LayoutDeleteNote.setVisibility(View.VISIBLE);
                   textDeletedNote.setText("No Note in trash");
               } else {
                   arrDeletedNotesCommon.clear();
                   arrDeletedNotesCommon.addAll(arrDeletedNotes);

               }
           }
           break;

       }

        selectedTabs=viewID;
        if (arrDeletedNotesCommon !=null) {
            if (arrDeletedNotesCommon.size()>0) {

                trashAdapter.notifyDataSetChanged();
            }
        }

    }

    void getDeletedItems() {

        androidOpenDbHelperObj = new NoteshareDatabaseHelper(context);
        sqliteDatabase = androidOpenDbHelperObj.getWritableDatabase();

         arrDeletedFolder = androidOpenDbHelperObj
                .getAllDeletedFolder();
          arrDeletedNotes = androidOpenDbHelperObj
                .getAllDeleteNotes();

        resettabs(selectedTabs);
    }


    void showAlertWithDeleteMessage(String message, final DBNoteItems item) {

        final Dialog dialog = new Dialog(TrashActivity.this);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.alert_view, null, false);

        TextView textViewTitleAlert = (TextView) contentView
                .findViewById(R.id.textViewTitleAlert);
        textViewTitleAlert.setText("" + item.getNote_Title());
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
                switch (selectedTabs) {
                    case R.id.btnNoteClick: {
                        boolean status = androidOpenDbHelperObj.deleteNote(item.getNote_Id());
                        if (status) {
                            Toast.makeText(TrashActivity.this,"Note deleted successfully",Toast.LENGTH_SHORT).show();
                            getDeletedItems();
                        }
                    }
                    break;
                    case R.id.btnFolderClick: {
                        boolean status = androidOpenDbHelperObj.deleteFolder(item.getNote_Id());
                        if (status) {
                            Toast.makeText(TrashActivity.this,"Folder deleted successfully",Toast.LENGTH_SHORT).show();
                            getDeletedItems();
                        }
                    }
                    break;
                }
                dialog.dismiss();

            }
        });

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(contentView);
        dialog.show();

    }

    public TrashAdapter.TrashAdapterListener adapterListener () {

        return  (new TrashAdapter.TrashAdapterListener() {
            @Override
            public void didSelectedAction(View vi, DBNoteItems item) {

                switch (vi.getId()) {
                    case R.id.delete:
                    {
                            Log.d("The Listener","delete");
                           switch (selectedTabs) {
                               case R.id.btnNoteClick: {

                                   showAlertWithDeleteMessage("Do you want to delete this note permanently?",item);
                               }
                               break;
                               case R.id.btnFolderClick: {
                                   showAlertWithDeleteMessage("Do you want to delete this folder permanently?",item);
                               }
                               break;
                           }

                    }
                     break;
                    case R.id.restore :
                    {
                        Log.d("The Listener","restore");
                    }
                    break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {


        super.onBackPressed();

        DialogUtill.backDialog(this);

    }
}
