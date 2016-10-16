package com.eNotes.Checklist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eNotes.Memo.FileUtility;
import com.eNotes.Utlity.DialogUtill;
import com.eNotes.noteshare.DrawerActivity;
import com.mobiapp.ventures.eNotes.R;

import java.util.ArrayList;

public class CheckListMainActivity extends DrawerActivity {



    CheckListDBManager checkListDBManager = new CheckListDBManager(this);
     ScrollView scrollView;
     LinearLayout mainLayout;
     ArrayList<String>  arrItems;
    ImageButton imageButtonHamburg,imageButtoncalander;
    TextView textViewheaderTitle;
    LinearLayout noQuickNote;
     ImageButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_check_list_main);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_check_list_main, null, false);
        mDrawerLayout.addView(contentView, 0);

        btnAdd=(ImageButton) contentView.findViewById(R.id.btnAddClick1);
        scrollView=(ScrollView) contentView.findViewById(R.id.scrollView);
        mainLayout=(LinearLayout) contentView.findViewById(R.id.mainLayout1);
        noQuickNote = (LinearLayout) contentView.findViewById(R.id.noQuickNote) ;
        noQuickNote.setVisibility(View.GONE);

        imageButtonHamburg = (ImageButton) contentView.findViewById(R.id.mainHeadermenue).findViewById(R.id.imageButtonHamburg);
        textViewheaderTitle = (TextView) contentView.findViewById(R.id.mainHeadermenue).findViewById(R.id.textViewheaderTitle);
        textViewheaderTitle.setText("");
        imageButtoncalander = (ImageButton) contentView.findViewById(R.id.mainHeadermenue).findViewById(R.id.imageButtoncalander);
        imageButtoncalander.setImageResource(R.drawable.delete_white_icon);
        imageButtoncalander.setVisibility(View.GONE);

        imageButtoncalander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // deleteMemo();

            }
        });

        imageButtonHamburg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openSlideMenu();

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtility.checkListfilename="";
                moveToMemoDisplay();
            }
        });
        
    }


    @Override
    protected void onResume() {
        super.onResume();
        readCheckListMainFile();
        updateUi();
    }

    void updateUi() {

        mainLayout.removeAllViews();

        LayoutInflater inflater = (LayoutInflater)  this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int tag=0;
        for (String str:arrItems) {

            final View view = inflater.inflate(R.layout.checklistrow, null);
            TextView item = (TextView) view.findViewById(R.id.textViewDisplay);
            item.setText(str);
            view.setTag(tag);
            mainLayout.addView(view);
            tag++;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d(" the Click view:",""+v.getTag());
                    FileUtility.checkListfilename= arrItems.get((int)v.getTag());
                    moveToMemoDisplay();

                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    Log.e("", "Long Clcik detected");
                   // v.setBackgroundColor(Color.CYAN);
                    return true;
                }
            });
        }


    }

    void moveToMemoDisplay()
    {
        startActivity(new Intent(CheckListMainActivity.this,CheckListDetailActivity.class));
    }


    void  readCheckListMainFile() {

        arrItems =  checkListDBManager.getAllCheckListMain();
        if (arrItems.size() <=0)
        {
            startActivity(new Intent(this,CheckListDetailActivity.class));
        }

//        String outPut = FileUtility.readFromFile(this,FileUtility.FILE_CheckList);
//        if (outPut == null ||outPut.length() == 0) {
//
//            startActivity(new Intent(this,CheckListDetailActivity.class));
//        }
//        startActivity(new Intent(this,CheckListDetailActivity.class));


    }

    @Override
    public void onBackPressed() {

        DialogUtill.backDialog(this);

    }
}
