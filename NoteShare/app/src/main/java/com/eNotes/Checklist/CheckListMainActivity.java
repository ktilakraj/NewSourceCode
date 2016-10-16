package com.eNotes.Checklist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eNotes.Memo.FileUtility;
import com.mobiapp.ventures.eNotes.R;

import java.util.ArrayList;

public class CheckListMainActivity extends AppCompatActivity {



    CheckListDBManager checkListDBManager = new CheckListDBManager(this);
    public ScrollView scrollView;
    public LinearLayout mainLayout;
     ArrayList<String>  arrItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list_main);

        scrollView=(ScrollView) findViewById(R.id.scrollView);
        mainLayout=(LinearLayout) findViewById(R.id.mainLayout);




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
}
