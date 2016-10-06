package com.eNotes.Memo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.mobiapp.ventures.eNotes.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoDisplayActivity extends AppCompatActivity {

    public EditText editText;
    public EditText editTextTitle;
    public HashMap<String,Object> dataHashMap =new HashMap<>();
    public ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
//back_icon_1
    ImageButton imageButtonHamburg;
    TextView textViewheaderTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memodisplayactivity);
        //setTitle("Add Quick Note");
        editText=(EditText) findViewById(R.id.entermemo) ;
        editTextTitle=(EditText) findViewById(R.id.entermemoTitle) ;

        imageButtonHamburg = (ImageButton) findViewById(R.id.mainHeadermenue).findViewById(R.id.imageButtonHamburg);
        textViewheaderTitle = (TextView) findViewById(R.id.mainHeadermenue).findViewById(R.id.textViewheaderTitle);
        textViewheaderTitle.setText("Add Quick Note");
        imageButtonHamburg.setImageResource(R.drawable.back_button_icon);
        imageButtonHamburg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               onBackPressed();
            }
        });

        readFileDetail();
    }

    void  saveDataInFile() {

        arrayList =(ArrayList<HashMap<String,String>>) dataHashMap.get(FileUtility.ROOTKEY);
        if(arrayList == null) {

            arrayList =new ArrayList<>();
        }

        if (editText.getText().length()>0 || editTextTitle.getText().length()>0) {


            if ( FileUtility.memoselectedindex ==-1) {

                HashMap<String,String> InnerHash=new HashMap<>();
                InnerHash.put(FileUtility.ELEMENTKEY,editText.getText().toString());
                InnerHash.put(FileUtility.ELEMENTTITLEKEY,editTextTitle.getText().toString());

                arrayList.add(InnerHash);
            } else {
                HashMap<String,String> InnerHash=new HashMap<>();
                InnerHash.put(FileUtility.ELEMENTKEY,editText.getText().toString());
                InnerHash.put(FileUtility.ELEMENTTITLEKEY,editTextTitle.getText().toString());
                arrayList.set((arrayList.size()-1)- FileUtility.memoselectedindex,InnerHash);

            }


            dataHashMap.put(FileUtility.ROOTKEY,arrayList);

            Gson gson = new Gson();
            String json = gson.toJson(dataHashMap);
            FileUtility.writeToFile(json,MemoDisplayActivity.this);
        }
    }

    void  readFileDetail() {

        String outPut = FileUtility.readFromFile(this);
        Gson gson = new Gson();
        dataHashMap = gson.fromJson(outPut, new TypeToken<HashMap<String, Object>>() {
        }.getType());

        if (dataHashMap == null || dataHashMap.get(FileUtility.ROOTKEY) == null) {

            dataHashMap = new HashMap<>();
            arrayList = new ArrayList<>();
            dataHashMap.put(FileUtility.ROOTKEY, arrayList);
        }

        if (FileUtility.memoselectedindex!=-1)
        {
            ArrayList<LinkedTreeMap<String,String>>  arrayListTemp =(ArrayList<LinkedTreeMap<String,String>>)dataHashMap.get(FileUtility.ROOTKEY);
            LinkedTreeMap<String,String> maps= arrayListTemp.get((arrayListTemp.size()-1)- FileUtility.memoselectedindex);
            editText.setText(""+maps.get(FileUtility.ELEMENTKEY));
            editTextTitle.setText(""+maps.get(FileUtility.ELEMENTTITLEKEY)==null?"":maps.get(FileUtility.ELEMENTTITLEKEY));
        }

    }

    @Override
    public void onBackPressed() {

        saveDataInFile();
        FileUtility.memoselectedindex=-1;
        super.onBackPressed();

    }
}
