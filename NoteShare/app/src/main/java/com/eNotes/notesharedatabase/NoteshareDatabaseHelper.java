package com.eNotes.notesharedatabase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class NoteshareDatabaseHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "NoteShareDB";
	private static final int DATABASE_VERSION = 1;

	// / Columns name for Service table
	public static String NOTE_ID = "NOTE_ID";
	public static String NOTE_TITLE = "NOTE_TITLE";
	public static String COLOR = "COLOR";
	public static String CREATED_TIME = "CREATED_TIME";
	public static String MODIFIED_TIME = "MODIFIED_TIME";
	public static String TIME_BOMB = "TIME_BOMB";
	public static String REMINDER_TIME = "REMINDER_TIME";
	public static String USER_ID = "USER_ID";
	public static String NOTE_ELEMENT = "NOTE_ELEMENT";
	public static String SERVER_KEY = "SERVER_KEY";
	public static String FOLDER_ID = "FOLDER_ID";
	public static String NOTE_DELETE_STATUS = "NOTE_DELETE_STATUS";
	public static String NOTE_COLOR="NOTE_COLOR";
	public static String NOTE_LOCK="NOTE_LOCK";
	public static String NOTE_LAST_SYNC_TIME="NOTE_LAST_SYNC_TIME";

	//FOLDER ELEMENTS
	public static String FOLDER_NOTE_ID = "FOLDER_NOTE_ID";
	public static String FOLDER_TITLE = "FOLDER__TITLE";
	public static String FOLDER_COLOR = "FOLDER_COLOR";
	public static String FOLDER_CREATED_TIME = "FOLDER_CREATED_TIME";
	public static String FOLDER_MODIFIED_TIME = "FOLDER_MODIFIED_TIME";
	public static String FOLDER_TIME_BOMB = "FOLDER_TIME_BOMB";
	public static String FOLDER_REMINDER_TIME = "FOLDER_REMINDER_TIME";
	public static String FOLDER_USER_ID = "FOLDER_USER_ID";
	public static String FOLDER_SERVER_KEY = "FOLDER_SERVER_KEY";
	public static String FOLDER_DELETE_STATUS = "FOLDER_DELETE_STATUS";
	public static String FOLDER_LOCK="FOLDER_LOCK";
	public static String FOLDER__LAST_SYNC_TIME="FOLDER_LAST_SYNC_TIME";
	
	
	
	public static String NOTE_ELEMENT_ID="NOTE_ELEMENT_ID";
	public static String NOTE_ELEMENT_CONTENT="NOTE_ELEMENT_CONTENT";
	public static String NOTE_ELEMENT_TYPE="NOTE_ELEMENT_TYPE";
	public static String NOTE_ELEMENT_MEDIA_TIME="NOTE_ELEMENT_MEDIA_TIME";
	public static String NOTE_ELEMENT_ISCHECKED="NOTE_ELEMENT_ISCHECKED";
	public static String NOTE_ELEMENT_DATETIME="NOTE_ELEMENT_DATETIME";
	
	
	

	
	
	
	//TABLES
	public static String NOTE_TABLE_NAME = "NOTESHARE";
	public static String NOTE_FOLDER_TABLE_NAME = "NOTESHAREFOLDER";
	public static String NOTE_ELEMENT_TABLE_NAME = "NOTESHAREELEMENT";
	
	
	public NoteshareDatabaseHelper(Context context) {

		super(context, DB_NAME, null, DATABASE_VERSION);

		String DB_PATH;
		if (android.os.Build.VERSION.SDK_INT >= 17) {
			DB_PATH = context.getApplicationInfo().dataDir + "/databases/"
					+ DB_NAME;
			Log.d("dbpath>17", DB_PATH);
		} else {
			DB_PATH = "/data/data/"
					+ context.getApplicationContext().getPackageName()
					+ "/databases/" + DB_NAME;
			Log.d("dbpath<17", DB_PATH);
		}
	}

	@SuppressLint("SdCardPath")
	public void exportDatabse(String databaseName) {
		Log.d("export databse", databaseName);
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				
				// String currentDBPath =
				// "/data/data/"+"com.project.salonmain"+"/databases/"+DB_NAME;
				
				String currentDBPath = "/data/NoteShare/databases/"
						+ DB_NAME;

				String backupDBPath = "NoteShareDB.db";
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);

				if (currentDB.exists()) {
					@SuppressWarnings("resource")
					FileChannel src = new FileInputStream(currentDB)
							.getChannel();
					@SuppressWarnings("resource")
					FileChannel dst = new FileOutputStream(backupDB)
							.getChannel();
					dst.transferFrom(src, 0, src.size());
					src.close();
					dst.close();
					Log.d("export databse exists", databaseName);
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		// IF NOT EXISTS
		
		
		/*NOTE LIST TABLE*/
		
		String TABLE_NoteShare = "CREATE TABLE IF NOT EXISTS  " + NOTE_TABLE_NAME + "("
				+ NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + NOTE_TITLE
				+ " TEXT," + COLOR + " TEXT," + CREATED_TIME + " TEXT,"
				+ MODIFIED_TIME + " TEXT," + TIME_BOMB + " TEXT,"
				+ REMINDER_TIME + " TEXT," + USER_ID + " TEXT," + NOTE_ELEMENT
				+ " TEXT," + SERVER_KEY + " TEXT," + FOLDER_ID + " TEXT,"
				+ NOTE_DELETE_STATUS + " TEXT," 
				+ NOTE_COLOR +" TEXT,"+NOTE_LOCK+" TEXT,"+NOTE_LAST_SYNC_TIME+" TEXT"
				+ ")";

		System.out.println("Create table NoteShare: " + TABLE_NoteShare);
		db.execSQL(TABLE_NoteShare);
		
		
		/*NOTE FOLDER TABLE CREATION*/
		
		String TABLE_NoteShare_FOLDER = "CREATE TABLE IF NOT EXISTS  " + NOTE_FOLDER_TABLE_NAME + "("
				+ FOLDER_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + FOLDER_TITLE
				+ " TEXT," + FOLDER_COLOR + " TEXT," + FOLDER_CREATED_TIME + " TEXT,"
				+ FOLDER_MODIFIED_TIME + " TEXT," + FOLDER_TIME_BOMB + " TEXT,"
				+ FOLDER_REMINDER_TIME + " TEXT," + FOLDER_USER_ID + " TEXT,"+ FOLDER_SERVER_KEY + " TEXT," 
				+ FOLDER_DELETE_STATUS + " TEXT," 
				+FOLDER_LOCK+" TEXT,"
				+FOLDER__LAST_SYNC_TIME+" TEXT"
				+ ")";


		System.out.println("Create table Folder: " + TABLE_NoteShare_FOLDER);
		db.execSQL(TABLE_NoteShare_FOLDER);
		
		
		String note_element_table ="CREATE TABLE IF NOT EXISTS "+NOTE_ELEMENT_TABLE_NAME
   		+ "("+NOTE_ELEMENT_ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"
   		+  NOTE_ID +" text,"
   		+ NOTE_ELEMENT_CONTENT +" text, "
   		+ NOTE_ELEMENT_TYPE +" text,"
   		+ NOTE_ELEMENT_MEDIA_TIME +" text,"
   		+ NOTE_ELEMENT_ISCHECKED +" text,"
				+ NOTE_ELEMENT_DATETIME +" text)";
		
		System.out.println("Create element table : " + note_element_table);
		db.execSQL(note_element_table);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		/*NOTE LIST TABLE*/
		
		String TABLE_NoteShare = "CREATE TABLE IF NOT EXISTS "
				+ NOTE_TABLE_NAME + "(" + NOTE_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + NOTE_TITLE + " TEXT,"
				+ COLOR + " TEXT," + CREATED_TIME + " TEXT," + MODIFIED_TIME
				+ " TEXT," + TIME_BOMB + " TEXT," + REMINDER_TIME + " TEXT,"
				+ USER_ID + " TEXT," + NOTE_ELEMENT + " TEXT," + SERVER_KEY
				+ " TEXT," + FOLDER_ID + " TEXT," + NOTE_DELETE_STATUS
				+ " TEXT,"+ NOTE_COLOR +" TEXT,"+NOTE_LOCK+" TEXT,"+NOTE_LAST_SYNC_TIME+" TEXT"
				+ ")";

		System.out.println("Create table: " + TABLE_NoteShare);
		db.execSQL(TABLE_NoteShare);
		
		
		/*NOTE FOLDER TABLE CREATION*/
		
		String TABLE_NoteShare_FOLDER = "CREATE TABLE IF NOT EXISTS  " + NOTE_FOLDER_TABLE_NAME + "("
				+ FOLDER_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + FOLDER_TITLE
				+ " TEXT," + FOLDER_COLOR + " TEXT," + FOLDER_CREATED_TIME + " TEXT,"
				+ FOLDER_MODIFIED_TIME + " TEXT," + FOLDER_TIME_BOMB + " TEXT,"
				+ FOLDER_REMINDER_TIME + " TEXT," + FOLDER_USER_ID + " TEXT,"+ FOLDER_SERVER_KEY + " TEXT," 
				+ FOLDER_DELETE_STATUS + " TEXT," 
				+FOLDER_LOCK+" TEXT,"
				+FOLDER__LAST_SYNC_TIME+" TEXT"
				+ ")";

		System.out.println("Create table: " + TABLE_NoteShare_FOLDER);
		db.execSQL(TABLE_NoteShare_FOLDER);
		
		
		
		String note_element_table ="CREATE TABLE IF NOT EXISTS "+NOTE_ELEMENT_TABLE_NAME
				+ "("+NOTE_ELEMENT_ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"
				+  NOTE_ID +" text,"
				+ NOTE_ELEMENT_CONTENT +" text, "
				+ NOTE_ELEMENT_TYPE +" text,"
				+ NOTE_ELEMENT_MEDIA_TIME +" text,"
				+ NOTE_ELEMENT_ISCHECKED +" text)";
				
				System.out.println("Create table: " + note_element_table);
				db.execSQL(note_element_table);
	
		

	}

	/*folder  INSERT*/
	
	public boolean folderdatabaseInsertion(String folder_title, String folder_color,
			String created_time, String modified_time, String time_bomb,
			String reminder_time, String user_id, String server_key, String folder_delete_status)
	{

		boolean statusData = true;

		SQLiteDatabase sqliteDatabase = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();

		contentValues.put(NoteshareDatabaseHelper.FOLDER_TITLE, folder_title);
		contentValues.put(NoteshareDatabaseHelper.FOLDER_COLOR , folder_color);
		contentValues.put(NoteshareDatabaseHelper.FOLDER_CREATED_TIME, created_time);
		contentValues.put(NoteshareDatabaseHelper.FOLDER_MODIFIED_TIME, modified_time);
		contentValues.put(NoteshareDatabaseHelper.FOLDER_TIME_BOMB, time_bomb);
		contentValues.put(NoteshareDatabaseHelper.FOLDER_REMINDER_TIME, reminder_time);
		contentValues.put(NoteshareDatabaseHelper.FOLDER_USER_ID, user_id);
		contentValues.put(NoteshareDatabaseHelper.FOLDER_SERVER_KEY, server_key);
		contentValues.put(NoteshareDatabaseHelper.FOLDER__LAST_SYNC_TIME, folder_delete_status);

		long status = sqliteDatabase.insert(
				NoteshareDatabaseHelper.NOTE_FOLDER_TABLE_NAME, null, contentValues);
		
		
		System.out.println("Folder data insertion status: " + status);

		if (status == 1)
		{

			statusData = true;
		}

		sqliteDatabase.close();

		return statusData;
	}




	public boolean databaseInsertion(String Note_title, String color,
									 String created_time, String modified_time, String time_bomb,
									 String reminder_time, String user_id, String folder_id,
									 String note_element, String server_key, String note_delete_status) {

		boolean statusData = true;

		SQLiteDatabase sqliteDatabase = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();

		contentValues.put(NoteshareDatabaseHelper.NOTE_TITLE, Note_title);
		contentValues.put(NoteshareDatabaseHelper.COLOR, color);
		contentValues.put(NoteshareDatabaseHelper.CREATED_TIME, created_time);
		contentValues.put(NoteshareDatabaseHelper.MODIFIED_TIME, modified_time);
		contentValues.put(NoteshareDatabaseHelper.TIME_BOMB, time_bomb);
		contentValues.put(NoteshareDatabaseHelper.REMINDER_TIME, reminder_time);
		contentValues.put(NoteshareDatabaseHelper.USER_ID, user_id);
		contentValues.put(NoteshareDatabaseHelper.NOTE_ELEMENT, note_element);
		contentValues.put(NoteshareDatabaseHelper.SERVER_KEY, server_key);
		contentValues.put(NoteshareDatabaseHelper.FOLDER_ID, folder_id);
		contentValues.put(NoteshareDatabaseHelper.NOTE_DELETE_STATUS, note_delete_status);

		// sqliteDatabase.delete(NoteshareDatabaseHelper.NOTE_TABLE_NAME,null,
		// null);

		long status = sqliteDatabase.insert(
				NoteshareDatabaseHelper.NOTE_TABLE_NAME, null, contentValues);


		System.out.println("data insertion status: " + status);

		if (status == 1) {

			statusData = true;
		}

		sqliteDatabase.close();

		return statusData;
	}


	/*Note element insert*/
	
	public boolean noteElementdatabaseInsertion(String Note_Id, String note_element_content,
			String note_element_type, String note_element_media_time, String note_element_ischecked) {

		boolean statusData = true;

		SQLiteDatabase sqliteDatabase = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();

		contentValues.put(NoteshareDatabaseHelper.NOTE_ID, Note_Id);
		contentValues.put(NoteshareDatabaseHelper.NOTE_ELEMENT_CONTENT, note_element_content);
		contentValues.put(NoteshareDatabaseHelper.NOTE_ELEMENT_TYPE, note_element_type);
		contentValues.put(NoteshareDatabaseHelper.NOTE_ELEMENT_MEDIA_TIME, note_element_media_time);
		contentValues.put(NoteshareDatabaseHelper.NOTE_ELEMENT_ISCHECKED, note_element_ischecked);

		SimpleDateFormat dateFormatGmt = new SimpleDateFormat(
				"dd/MM/yyyy HH:mm:ss");
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date d = new Date();
		String strDate = dateFormatGmt.format(d);

		contentValues.put(NoteshareDatabaseHelper.NOTE_ELEMENT_DATETIME, strDate);

		long status = sqliteDatabase.insert(
				NoteshareDatabaseHelper.NOTE_ELEMENT_TABLE_NAME, null, contentValues);
		
		
		System.out.println(NoteshareDatabaseHelper.NOTE_ELEMENT_TABLE_NAME+" data insertion status:" + status);

		if (status == 1) {

			statusData = true;
		}

		sqliteDatabase.close();

		return statusData;
	}
	
	/******************* Get all notes**************************************/
	public ArrayList<DBNoteItems> getAllNotesWithTitle(String note_title) {
		ArrayList<DBNoteItems> user = new ArrayList<DBNoteItems>();
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM "
						+ NoteshareDatabaseHelper.NOTE_TABLE_NAME + " WHERE "
						+ NoteshareDatabaseHelper.NOTE_TITLE + "= ?",
				new String[]{note_title}, null);
		
		
		
		
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {

			DBNoteItems noteItems = new DBNoteItems();

			noteItems.setNote_Id(cursor.getString(0));
			noteItems.setNote_Title(cursor.getString(1));
			noteItems.setNote_Color(cursor.getString(2));
			noteItems.setNote_Created_Time(cursor.getString(3));
			noteItems.setNote_Modified_Time(cursor.getString(4));
			noteItems.setNote_TimeBomb(cursor.getString(5));
			noteItems.setNote_Reminder_Time(cursor.getString(6));
			noteItems.setNote_UserId(cursor.getString(7));
			noteItems.setNote_Element(cursor.getString(8));
			noteItems.setNote_Server_Key(cursor.getString(9));
			noteItems.setNote_Folder_Id(cursor.getString(10));
			noteItems.setNote_Deleted_Status(cursor.getString(11));
			noteItems.setNote_Lock_Status(cursor.getString(13));
			user.add(noteItems);
			
	

		}
		cursor.close();
		db.close();

		return user;
	}

	public ArrayList<DBNoteItems> getAllNotesWithNote_Id(String note_id) {
		ArrayList<DBNoteItems> user = new ArrayList<DBNoteItems>();

		

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM "
						+ NoteshareDatabaseHelper.NOTE_TABLE_NAME + " WHERE "
						+ NoteshareDatabaseHelper.NOTE_ID + "= ?",
				new String[]{note_id}, null);
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {

			DBNoteItems noteItems = new DBNoteItems();
			noteItems.setNote_Id(cursor.getString(0));
			noteItems.setNote_Title(cursor.getString(1));
			noteItems.setNote_Color(cursor.getString(2));
			noteItems.setNote_Created_Time(cursor.getString(3));
			noteItems.setNote_Modified_Time(cursor.getString(4));
			noteItems.setNote_TimeBomb(cursor.getString(5));
			noteItems.setNote_Reminder_Time(cursor.getString(6));
			noteItems.setNote_UserId(cursor.getString(7));
			noteItems.setNote_Element(cursor.getString(8));
			noteItems.setNote_Server_Key(cursor.getString(9));
			noteItems.setNote_Folder_Id(cursor.getString(10));
			noteItems.setNote_Deleted_Status(cursor.getString(11));
			noteItems.setNote_Lock_Status(cursor.getString(13));
			user.add(noteItems);
			
			

		}
		cursor.close();
		db.close();

		return user;
	}


	public ArrayList<DBNoteItems> getAllNotesWithFolder_Id(String folder_id)
	{
		ArrayList<DBNoteItems> user = new ArrayList<DBNoteItems>();

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM "
						+ NoteshareDatabaseHelper.NOTE_TABLE_NAME + " WHERE "
						+ NoteshareDatabaseHelper.FOLDER_ID+ "= ?",
				new String[] { folder_id }, null);
		//cursor.moveToFirst();
		if (cursor.getCount() > 0) {

			while (cursor.moveToNext()) {

				DBNoteItems noteItems = new DBNoteItems();
				noteItems.setNote_Id(cursor.getString(0));
				noteItems.setNote_Title(cursor.getString(1));
				noteItems.setNote_Color(cursor.getString(2));
				noteItems.setNote_Created_Time(cursor.getString(3));
				noteItems.setNote_Modified_Time(cursor.getString(4));
				noteItems.setNote_TimeBomb(cursor.getString(5));
				noteItems.setNote_Reminder_Time(cursor.getString(6));
				noteItems.setNote_UserId(cursor.getString(7));
				noteItems.setNote_Element(cursor.getString(8));
				noteItems.setNote_Server_Key(cursor.getString(9));
				noteItems.setNote_Folder_Id(cursor.getString(10));
				noteItems.setNote_Deleted_Status(cursor.getString(11));
				user.add(noteItems);
			}



		}
		cursor.close();
		db.close();

		return user;
	}

	/************************** Get All note Element ************************************/

	public ArrayList<DBNoteItemElement> getAllNotesElementWithNote_Id(String note_id) {
		ArrayList<DBNoteItemElement> arrNoteElements = new ArrayList<DBNoteItemElement>();

		

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM "
						+ NoteshareDatabaseHelper.NOTE_ELEMENT_TABLE_NAME + " WHERE "
						+ NoteshareDatabaseHelper.NOTE_ID + "= ?",
				new String[]{note_id}, null);

		//cursor.moveToFirst();
		if (cursor.getCount() > 0) 
		{
			while (cursor.moveToNext()) {

				DBNoteItemElement noteItems = new DBNoteItemElement();
				noteItems.setNOTE_ELEMENT_ID(cursor.getString(0));
				noteItems.setNOTE_ID(cursor.getString(1));
				noteItems.setNOTE_ELEMENT_CONTENT(cursor.getString(2));
				noteItems.setNOTE_ELEMENT_TYPE(cursor.getString(3));
				noteItems.setNOTE_ELEMENT_MEDIA_TIME(cursor.getString(4));
				noteItems.setNOTE_ELEMENT_ISCHECKED(cursor.getString(5));
				noteItems.setNOTE_ELEMENT_DATE_TIME(cursor.getString(6));

				arrNoteElements.add(noteItems);
			}
			
			

		}
		cursor.close();
		db.close();

		return arrNoteElements;
	}

	public ArrayList<DBNoteItemElement> getLastNotesElementWithNote_Id(String note_id) {
		ArrayList<DBNoteItemElement> arrNoteElements = new ArrayList<DBNoteItemElement>();

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM "
						+ NoteshareDatabaseHelper.NOTE_ELEMENT_TABLE_NAME + " WHERE "
						+ NoteshareDatabaseHelper.NOTE_ID + "= ?",
				new String[]{note_id}, null);
		cursor.moveToLast();
		if (cursor.getCount() > 0)
		{

			DBNoteItemElement noteItems = new DBNoteItemElement();
			noteItems.setNOTE_ELEMENT_ID(cursor.getString(0));
			noteItems.setNOTE_ID(cursor.getString(1));
			noteItems.setNOTE_ELEMENT_CONTENT(cursor.getString(2));
			noteItems.setNOTE_ELEMENT_TYPE(cursor.getString(3));
			noteItems.setNOTE_ELEMENT_MEDIA_TIME(cursor.getString(4));
			noteItems.setNOTE_ELEMENT_ISCHECKED(cursor.getString(5));


			arrNoteElements.add(noteItems);



		}
		cursor.close();
		db.close();

		return arrNoteElements;
	}


	public ArrayList<DBNoteItems> getAllFolder() {
		ArrayList<DBNoteItems> user = new ArrayList<DBNoteItems>();

		String selectQuery = "SELECT * FROM "
				+ NoteshareDatabaseHelper.NOTE_FOLDER_TABLE_NAME;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		//cursor.moveToFirst();
		if (cursor.getCount() > 0)
		{
			while (cursor.moveToNext())
			{
				DBNoteItems noteItems = new DBNoteItems();
				noteItems.setNote_Id(cursor.getString(0));
				noteItems.setNote_Title(cursor.getString(1));
				noteItems.setNote_Color(cursor.getString(2));
				noteItems.setNote_Created_Time(cursor.getString(3));
				noteItems.setNote_Modified_Time(cursor.getString(4));
				noteItems.setNote_TimeBomb(cursor.getString(5));
				noteItems.setNote_Reminder_Time(cursor.getString(6));
				noteItems.setNote_UserId(cursor.getString(7));
				noteItems.setNote_Server_Key(cursor.getString(8));
				noteItems.setNote_Deleted_Status(cursor.getString(9));
				user.add(noteItems);

			}
		}
		cursor.close();
		db.close();

		return user;
	}

	public ArrayList<DBNoteItems> getAllDeletedFolder() {
		ArrayList<DBNoteItems> user = new ArrayList<DBNoteItems>();

		//String selectQuery = "SELECT * FROM "
				//+ NoteshareDatabaseHelper.NOTE_FOLDER_TABLE_NAME;

		SQLiteDatabase db = this.getReadableDatabase();
		//Cursor cursor = db.rawQuery(selectQuery, null);

		Cursor cursor = db.rawQuery("SELECT * FROM "
						+ NoteshareDatabaseHelper.NOTE_FOLDER_TABLE_NAME + " WHERE "
						+ NoteshareDatabaseHelper.FOLDER_DELETE_STATUS + "= ?",
				new String[]{"-1"}, null);

		if (cursor.getCount() > 0)
		{
			while (cursor.moveToNext())
			{
				DBNoteItems noteItems = new DBNoteItems();
				noteItems.setNote_Id(cursor.getString(0));
				noteItems.setNote_Title(cursor.getString(1));
				noteItems.setNote_Color(cursor.getString(2));
				noteItems.setNote_Created_Time(cursor.getString(3));
				noteItems.setNote_Modified_Time(cursor.getString(4));
				noteItems.setNote_TimeBomb(cursor.getString(5));
				noteItems.setNote_Reminder_Time(cursor.getString(6));
				noteItems.setNote_UserId(cursor.getString(7));
				noteItems.setNote_Server_Key(cursor.getString(8));
				noteItems.setNote_Deleted_Status(cursor.getString(9));
				user.add(noteItems);

			}
		}
		cursor.close();
		db.close();

		return user;
	}

	public ArrayList<DBNoteItems> getAllNotes() {
		ArrayList<DBNoteItems> user = new ArrayList<DBNoteItems>();

		String selectQuery = "SELECT * FROM "
				+ NoteshareDatabaseHelper.NOTE_TABLE_NAME;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		//cursor.moveToFirst();
		if (cursor.getCount() > 0)
		{
			while (cursor.moveToNext()) {
				DBNoteItems noteItems = new DBNoteItems();
				noteItems.setNote_Id(cursor.getString(0));
				noteItems.setNote_Title(cursor.getString(1));
				noteItems.setNote_Color(cursor.getString(2));
				noteItems.setNote_Created_Time(cursor.getString(3));
				noteItems.setNote_Modified_Time(cursor.getString(4));
				noteItems.setNote_TimeBomb(cursor.getString(5));
				noteItems.setNote_Reminder_Time(cursor.getString(6));
				noteItems.setNote_UserId(cursor.getString(7));
				noteItems.setNote_Element(cursor.getString(8));
				noteItems.setNote_Server_Key(cursor.getString(9));
				noteItems.setNote_Folder_Id(cursor.getString(10));
				noteItems.setNote_Deleted_Status(cursor.getString(11));
				noteItems.setNote_Lock_Status(cursor.getString(13));

				if (noteItems.getNote_Deleted_Status() !=null) {

					if (noteItems.getNote_Deleted_Status().equalsIgnoreCase("-1") || noteItems.getNote_Deleted_Status().equalsIgnoreCase("-2")) {

					} else {
						user.add(noteItems);
					}
				} else {

					user.add(noteItems);
				}
			}
		}
		cursor.close();
		db.close();

		return user;
	}

	public ArrayList<DBNoteItems> getAllDeleteNotes() {
		ArrayList<DBNoteItems> user = new ArrayList<DBNoteItems>();

		//String selectQuery = "SELECT * FROM "
				//+ NoteshareDatabaseHelper.NOTE_TABLE_NAME;

		SQLiteDatabase db = this.getReadableDatabase();
		//Cursor cursor = db.rawQuery(selectQuery, null);

		Cursor cursor = db.rawQuery("SELECT * FROM "
						+ NoteshareDatabaseHelper.NOTE_TABLE_NAME + " WHERE "
						+ NoteshareDatabaseHelper.NOTE_DELETE_STATUS + "= ?",
				new String[]{"-1"}, null);

		if (cursor.getCount() > 0)
		{
			while (cursor.moveToNext()) {
				DBNoteItems noteItems = new DBNoteItems();
				noteItems.setNote_Id(cursor.getString(0));
				noteItems.setNote_Title(cursor.getString(1));
				noteItems.setNote_Color(cursor.getString(2));
				noteItems.setNote_Created_Time(cursor.getString(3));
				noteItems.setNote_Modified_Time(cursor.getString(4));
				noteItems.setNote_TimeBomb(cursor.getString(5));
				noteItems.setNote_Reminder_Time(cursor.getString(6));
				noteItems.setNote_UserId(cursor.getString(7));
				noteItems.setNote_Element(cursor.getString(8));
				noteItems.setNote_Server_Key(cursor.getString(9));
				noteItems.setNote_Folder_Id(cursor.getString(10));
				noteItems.setNote_Deleted_Status(cursor.getString(11));
				noteItems.setNote_Lock_Status(cursor.getString(13));
				user.add(noteItems);

			}
		}
		cursor.close();
		db.close();

		return user;
	}

	public boolean updateNoteitems(DBNoteItems items) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues args = new ContentValues();
		args.put(NoteshareDatabaseHelper.MODIFIED_TIME,
				items.getNote_Modified_Time());
		args.put(NoteshareDatabaseHelper.NOTE_ELEMENT, items.getNote_Element());

		return db.update(NoteshareDatabaseHelper.NOTE_TABLE_NAME, args,
				NoteshareDatabaseHelper.NOTE_ID + "=" + items.getNote_Id(),
				null) > 0;
	}

	//Update note time bomb
	public boolean updateNoteitems_timeBomb(DBNoteItems items) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues args = new ContentValues();
		args.put(NoteshareDatabaseHelper.TIME_BOMB, items.getNote_TimeBomb());
		return db.update(NoteshareDatabaseHelper.NOTE_TABLE_NAME, args,
				NoteshareDatabaseHelper.NOTE_ID + "=" + items.getNote_Id(),
				null) > 0;
	}

	//Update note time bomb
	public boolean updateNoteitems_Color(DBNoteItems items)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues args = new ContentValues();
		args.put(NoteshareDatabaseHelper.COLOR, items.getNote_Color());
		return db.update(NoteshareDatabaseHelper.NOTE_TABLE_NAME, args,
				NoteshareDatabaseHelper.NOTE_ID + "=" + items.getNote_Id(),
				null) > 0;
	}

//update note title
	public boolean updateNoteitems_title(DBNoteItems items)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues args = new ContentValues();
		args.put(NoteshareDatabaseHelper.NOTE_TITLE , items.getNote_Title());
		return db.update(NoteshareDatabaseHelper.NOTE_TABLE_NAME, args,
				NoteshareDatabaseHelper.NOTE_ID + "=" + items.getNote_Id(),
				null) > 0;
	}
	//Delete note item
	public  boolean deleteNote(String note_id)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete(NOTE_TABLE_NAME, NOTE_ID + "=" + note_id, null)>0;
	}
	public  boolean deleteFolder(String note_id)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete(NOTE_FOLDER_TABLE_NAME, FOLDER_NOTE_ID + "=" + note_id, null)>0;
	}


	//Update note lock
	public boolean updateNoteitems_Notelock(DBNoteItems items) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues args = new ContentValues();
		args.put(NoteshareDatabaseHelper.NOTE_LOCK, items.getNote_Lock_Status());
		return db.update(NoteshareDatabaseHelper.NOTE_TABLE_NAME, args,
				NoteshareDatabaseHelper.NOTE_ID + "=" + items.getNote_Id(),
				null) > 0;
	}
	
	//Update note Reminder time
	public boolean updateNoteitems_ReminderTime(DBNoteItems items) {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues args = new ContentValues();
			args.put(NoteshareDatabaseHelper.REMINDER_TIME, items.getNote_Reminder_Time());
			return db.update(NoteshareDatabaseHelper.NOTE_TABLE_NAME, args,
					NoteshareDatabaseHelper.NOTE_ID + "=" + items.getNote_Id(),
					null) > 0;
		}
		
	//Update note Move to folder
	public boolean updateNoteitems_MoveToFolder(DBNoteItems items) {
					SQLiteDatabase db = this.getWritableDatabase();
					ContentValues args = new ContentValues();
					args.put(NoteshareDatabaseHelper.FOLDER_ID, items.getNote_Folder_Id());
					return db.update(NoteshareDatabaseHelper.NOTE_TABLE_NAME, args,
							NoteshareDatabaseHelper.NOTE_ID + "=" + items.getNote_Id(),
							null) > 0;
				}

	//Update note delete Status
	public boolean updateNoteitems_Delete(String item_Id,String status) {
					SQLiteDatabase db = this.getWritableDatabase();
					ContentValues args = new ContentValues();
					args.put(NoteshareDatabaseHelper.NOTE_DELETE_STATUS,status);
					return db.update(NoteshareDatabaseHelper.NOTE_TABLE_NAME, args,
							NoteshareDatabaseHelper.NOTE_ID + "=" + item_Id,
							null) > 0;
				}

	//Update Folder Color
	public boolean updateFolder_Color(DBNoteItems items) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues args = new ContentValues();
		args.put(NoteshareDatabaseHelper.FOLDER_COLOR , items.getNote_Color());
		return db.update(NoteshareDatabaseHelper.NOTE_FOLDER_TABLE_NAME, args,
				NoteshareDatabaseHelper.FOLDER_NOTE_ID + "=" + items.getNote_Id(),
				null) > 0;
	}

	public boolean updateFolder_DeleteStatus(String folderId) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues args = new ContentValues();
		args.put(NoteshareDatabaseHelper.FOLDER_DELETE_STATUS , "-1");
		return db.update(NoteshareDatabaseHelper.NOTE_FOLDER_TABLE_NAME, args,
				NoteshareDatabaseHelper.FOLDER_NOTE_ID + "=" + folderId,
				null) > 0;
	}




	//Update note Element text Element
	public boolean updateNoteElementText(String noteElement_id,String text)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues args = new ContentValues();
		args.put(NoteshareDatabaseHelper.NOTE_ELEMENT_CONTENT ,text);
		return db.update(NoteshareDatabaseHelper.NOTE_ELEMENT_TABLE_NAME, args,
				NoteshareDatabaseHelper.NOTE_ELEMENT_ID + "=" + noteElement_id,
				null) > 0;
	}

	public  boolean deleteNoteElement(String note_element_id)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete(NOTE_ELEMENT_TABLE_NAME, NOTE_ELEMENT_ID + "="+note_element_id,null)>0;
	}
}
