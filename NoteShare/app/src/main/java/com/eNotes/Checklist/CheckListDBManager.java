package com.eNotes.Checklist;

/**
 * Created by Tilak on 10/14/16.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class CheckListDBManager  extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "CheckList.db";
    public static final String CHECKLISTMAIN_TABLE_NAME = "checkListMain";
    public static final String CHECKLIST_TABLE_NAME = "checkList";
    public static final String CheckList_COLUMN_ID = "id";
    public static final String CheckList_COLUMN_TITLE = "title";
    public static final String CheckList_COLUMN_CONTENT  = "content";
    public static final String CheckList_COLUMN_CHECKED = "isChecked";
    public static final String CheckList_COLUMN_IDS = "ids";


    public CheckListDBManager(Context context) {

        super(context,DATABASE_NAME,null,1);

    }

    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table  checkListMain (id integer primary key , ids text)");
        db.execSQL("create table  checkList (id integer primary key , title text,content text,isChecked text, ids text)");

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS checkListMain");
        db.execSQL("DROP TABLE IF EXISTS checkList");
        onCreate(db);
    }

   /* Insert check List Items*/

    public boolean insertcheckList  (String title, String content, String isChecked, String ids)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("content", content);
        contentValues.put("isChecked", isChecked);
        contentValues.put("ids", ids);
        db.insert("checkList", null, contentValues);
        return true;
    }

    /* Insert check List Element root*/

    public boolean insertcheckListmain  (String ids)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ids", ids);
        db.insert("checkListMain", null, contentValues);
        return true;
    }

    /* get  check List All root*/

    public ArrayList<String> getAllCheckListMain()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from checkListMain", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CheckList_COLUMN_IDS)));
            res.moveToNext();
        }
        return array_list;
    }

    /* get  check List All root Element */

    public ArrayList<String> getAllCheckList(String mainIds)
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from checkList where ids="+mainIds, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CheckList_COLUMN_CONTENT)));
            res.moveToNext();
        }
        return array_list;
    }


    public Integer deletecheckListMain (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("checkListMain",
                "ids = ? ",
                new String[] { Integer.toString(id) });
    }

    public Integer deletecheckList (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("checkList",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public boolean updateCheckList (Integer id, String title, String content, String isChecked, String ids)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("content", content);
        contentValues.put("isChecked", isChecked);
        contentValues.put("ids", ids);
        db.update("checkList", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }


}
