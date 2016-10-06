package com.eNotes.Memo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.eNotes.Utlity.DialogUtill;
import com.eNotes.Utlity.OnSwipeTouchListener;
import com.eNotes.noteshare.DrawerActivity;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.mobiapp.ventures.eNotes.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MemoListingActivity extends DrawerActivity {

    public ImageButton btnAdd;
    public ScrollView scrollView;
    public LinearLayout layoutChildScroll;

    boolean isFirstTime =false;

    public HashMap<String,Object> dataHashMap =new HashMap<>();
    public ArrayList<LinkedTreeMap<String,String>> arrayList = new ArrayList<>();

    ImageButton imageButtonHamburg;
    TextView textViewheaderTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.memolistingactivity);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.memolistingactivity, null, false);

        mDrawerLayout.addView(contentView, 0);

        btnAdd=(ImageButton) contentView.findViewById(R.id.btnAddClick1);
        scrollView=(ScrollView) contentView.findViewById(R.id.scrollMemo);

         imageButtonHamburg = (ImageButton) contentView.findViewById(R.id.mainHeadermenue).findViewById(R.id.imageButtonHamburg);
         textViewheaderTitle = (TextView) contentView.findViewById(R.id.mainHeadermenue).findViewById(R.id.textViewheaderTitle);
            textViewheaderTitle.setText("");

        imageButtonHamburg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openSlideMenu();
            }
        });

        layoutChildScroll=(LinearLayout)contentView.findViewById(R.id.layoutChildScroll) ;
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtility.memoselectedindex=-1;
            moveToMemoDisplay();
            }
        });

       // setTitle("Quick Note");

    }

    void moveToMemoDisplay()
    {
        startActivity(new Intent(MemoListingActivity.this,MemoDisplayActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();

        readFileDetail();

    }

    void updateUi()
    {
        layoutChildScroll.removeAllViews();
        arrayList =(ArrayList<LinkedTreeMap<String,String>>)dataHashMap.get(FileUtility.ROOTKEY);
        Collections.reverse(arrayList);

        View view;
        int tag=0;
        int numberOfMemo=0;
        LayoutInflater inflater = (LayoutInflater)  this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (LinkedTreeMap<String,String> obj:arrayList)
        {
            view = inflater.inflate(R.layout.memorow, null);
            TextView item = (TextView) view.findViewById(R.id.input);
            TextView itemTitle = (TextView) view.findViewById(R.id.titletext);
            LinearLayout mainlayout1= (LinearLayout) view.findViewById(R.id.mainlayout1);
            item.setText(""+obj.get(FileUtility.ELEMENTKEY));
            itemTitle.setText(""+obj.get(FileUtility.ELEMENTTITLEKEY)==null?"":obj.get(FileUtility.ELEMENTTITLEKEY));
            view.setTag(tag);
            if (obj.get(FileUtility.ELEMENTTITLEKEY)==null || obj.get(FileUtility.ELEMENTTITLEKEY).length()==0) {
                itemTitle.setVisibility(View.GONE);
            }
            if (obj.get(FileUtility.ELEMENTKEY)==null || obj.get(FileUtility.ELEMENTKEY).length()==0) {
                item.setVisibility(View.GONE);
            }

            tag++;
            if (obj.get(FileUtility.ELEMENTKEY).length() > 0 || obj.get(FileUtility.ELEMENTTITLEKEY).length() > 0)
            {

                layoutChildScroll.addView(view);
                numberOfMemo++;

            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d(" the Click view:",""+v.getTag());
                    FileUtility.memoselectedindex=(int)v.getTag();
                    moveToMemoDisplay();

                }
            });


            view.setOnTouchListener(new OnSwipeTouchListener(MemoListingActivity.this) {
                public void onSwipeTop() {
                    Toast.makeText(MemoListingActivity.this, "top", Toast.LENGTH_SHORT).show();
                }
                public void onSwipeRight() {
                    Toast.makeText(MemoListingActivity.this, "right", Toast.LENGTH_SHORT).show();
                }
                public void onSwipeLeft() {
                    Toast.makeText(MemoListingActivity.this, "left", Toast.LENGTH_SHORT).show();
                }
                public void onSwipeBottom() {
                    Toast.makeText(MemoListingActivity.this, "bottom", Toast.LENGTH_SHORT).show();
                }

            });

            int color = Color.parseColor("#f1f1f1");
            Drawable background = mainlayout1.getBackground();
            if (background instanceof ShapeDrawable) {
                ((ShapeDrawable)background).getPaint().setColor(color);
            } else if (background instanceof GradientDrawable) {
                ((GradientDrawable)background).setColor(color);
            } else if (background instanceof ColorDrawable) {
                ((ColorDrawable)background).setColor(color);
            }
        }


        scrollView.scrollTo(0,0);

        if (numberOfMemo>0)
        {
            textViewheaderTitle.setText("Quick Note ("+(numberOfMemo)+")");
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

            if (isFirstTime == false) {

                moveToMemoDisplay();
                isFirstTime=true;
            }

        }

        updateUi();
    }

    public void onBackPressed()
    {

        DialogUtill.backDialog(this);

    }
}