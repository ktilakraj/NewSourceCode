package com.eNotes.Checklist;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eNotes.Memo.FileUtility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobiapp.ventures.eNotes.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CheckListDetailActivity extends AppCompatActivity {

    CheckListDBManager checkListDBManager = new CheckListDBManager(this);

    String randomfileNumber;
    HashMap<String,ArrayList<String>> dataHashMap;
    ArrayList<String> arrayListContent;
    HashMap<String, ArrayList<HashMap<String,String>>> dataHashMapContent;
    ArrayList<HashMap<String,String>> arrayContent;

    ImageButton btnAddlist ;
    EditText entermemo;
    ArrayList<String>  arrayList;
    public ScrollView scrollView;
    public LinearLayout mainLayout;
    ImageButton imageButtonHamburg;
    TextView textViewheaderTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list_detail);
        btnAddlist= (ImageButton) findViewById(R.id.btnAddlist);
        entermemo= (EditText) findViewById(R.id.entermemo);
        scrollView=(ScrollView) findViewById(R.id.scrollView);
        mainLayout=(LinearLayout) findViewById(R.id.mainLayout);



        imageButtonHamburg = (ImageButton) findViewById(R.id.mainHeadermenue).findViewById(R.id.imageButtonHamburg);
        textViewheaderTitle = (TextView) findViewById(R.id.mainHeadermenue).findViewById(R.id.textViewheaderTitle);
        textViewheaderTitle.setText("");
        imageButtonHamburg.setImageResource(R.drawable.back_button_icon);
        imageButtonHamburg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                onBackPressed();
            }
        });

        btnAddlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* arrayContent = dataHashMapContent.get(randomfileNumber);
                if(arrayContent == null) {
                    arrayContent =new ArrayList<>();
                }

                HashMap<String,String> InnerHash=new HashMap<>();
                InnerHash.put(FileUtility.Element_title,"");
                InnerHash.put(FileUtility.Element_content,entermemo.getText().toString());
                InnerHash.put(FileUtility.Element_isCheck,"0");
                arrayContent.add(InnerHash);*/

                if (entermemo.getText().toString().length()>0) {

                    checkListDBManager.insertcheckList("",entermemo.getText().toString() , "0", randomfileNumber);
                    entermemo.setText("");
                    getAllCheckListItem();
                }


            }
        });

        if (FileUtility.checkListfilename.length()>0) {

            randomfileNumber = FileUtility.checkListfilename;

        } else {

            randomfileNumber = FileUtility.randomUniqueNumberGenerator();
            checkListDBManager.insertcheckListmain(randomfileNumber);
        }

        Log.d("The random number is: ",""+randomfileNumber);
       // readCheckListMainFile();
        //readRandomCheckListFile();


        getAllCheckListItem();


    }

    void getAllCheckListItem()
    {
        arrayList =  checkListDBManager.getAllCheckList(randomfileNumber);
        Log.d("Check List Item is: ",""+arrayList);
        updateUi();
    }

    void updateUi() {

        mainLayout.removeAllViews();
        LayoutInflater inflater = (LayoutInflater)  this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int tag=0;
        for (String str:arrayList) {

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
                    //FileUtility.checkListfilename= arrItems.get((int)v.getTag());
                    //moveToMemoDisplay();

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


    void  readCheckListMainFile() {

        String outPut = FileUtility.readFromFile(this,FileUtility.FILE_CheckList);

        if (outPut == null || outPut.length() == 0) {

            Gson gson = new Gson();
            dataHashMap = gson.fromJson(outPut, new TypeToken<HashMap<String, Object>>() {
            }.getType());

            if (dataHashMap == null || dataHashMap.get(FileUtility.CHECKLIST_ROOTKEY) == null) {

                dataHashMap = new HashMap<>();
                arrayListContent = new ArrayList<>();
                dataHashMap.put(FileUtility.CHECKLIST_ROOTKEY, arrayListContent);
            }
        } else
        {
            Gson gson = new Gson();
            dataHashMap = gson.fromJson(outPut, new TypeToken<HashMap<String, Object>>() {
            }.getType());
        }

    }


    void  readRandomCheckListFile() {

        String outPut = FileUtility.readFromFile(this,randomfileNumber);

        if (outPut == null || outPut.length() == 0) {

            Gson gson = new Gson();
            dataHashMapContent = gson.fromJson(outPut, new TypeToken<HashMap<String, Object>>() {
            }.getType());

            if (dataHashMapContent == null || dataHashMapContent.get(randomfileNumber) == null) {

                dataHashMapContent = new HashMap<>();
                arrayContent = new ArrayList<>();
                dataHashMapContent.put(randomfileNumber, arrayContent);
            }
        } else
        {
            Gson gson = new Gson();
            dataHashMapContent = gson.fromJson(outPut, new TypeToken<HashMap<String, Object>>() {
            }.getType());
        }

    }

    /*

    @ save data in main file

    */

    void  saveData()
    {
        arrayListContent = dataHashMap.get(FileUtility.CHECKLIST_ROOTKEY);
        if(arrayListContent == null) {
            arrayListContent =new ArrayList<>();
        }


        if (FileUtility.checkListfilename.length()>0) {

        } else {

            arrayListContent.add(randomfileNumber);

            dataHashMap.put(FileUtility.CHECKLIST_ROOTKEY,arrayListContent);
            Gson gson = new Gson();
            String json = gson.toJson(dataHashMap);
            FileUtility.writeToFile(json,CheckListDetailActivity.this,FileUtility.FILE_CheckList);
        }



    }

    void  saveRandomFileData() {


        dataHashMapContent.put(randomfileNumber,arrayContent);
        Gson gson = new Gson();
        String json = gson.toJson(dataHashMapContent);
        FileUtility.writeToFile(json,CheckListDetailActivity.this,randomfileNumber);
        readRandomCheckListFile();
    }


    @Override
    public void onBackPressed() {

        //saveData();
        //saveRandomFileData();
        FileUtility.checkListfilename="";

        super.onBackPressed();
    }
}
