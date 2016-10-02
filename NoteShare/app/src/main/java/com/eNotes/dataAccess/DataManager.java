package com.eNotes.dataAccess;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.eNotes.datamodels.NOTETYPE;
import com.eNotes.datamodels.NoteListDataModel;
import com.eNotes.datamodels.SideMenuitems;
import com.eNotes.notesharedatabase.DBNoteItems;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


public class DataManager
{

	public static final String Mylock = "lock" ;

	public static DataManager manager;
	public Bitmap userImageBitMap;
	public boolean typeofListView;
	public NOTETYPE SELECTED_TEXT_OPTION;
	public SideMenuitems seletedListNoteItem;
	public DBNoteItems seletedDBNoteItem;

	public String getShare_url() {
		return share_url;
	}

	public void setShare_url(String share_url) {
		this.share_url = share_url;
	}

	public String getVersion_no() {
		return version_no;
	}

	public void setVersion_no(String version_no) {
		this.version_no = version_no;
	}

	public String getNew_feature() {
		return new_feature;
	}

	public void setNew_feature(String new_feature) {
		this.new_feature = new_feature;
	}

	public  String    share_url;
	public  String      version_no;
	public  String      new_feature;

	public Uri getCamraURI() {
		return camraURI;
	}

	public void setCamraURI(Uri camraURI) {
		this.camraURI = camraURI;
	}

	public Uri camraURI;

	public String getCamraAppendPath() {
		return camraAppendPath;
	}

	public void setCamraAppendPath(String camraAppendPath) {
		this.camraAppendPath = camraAppendPath;
	}

	public String camraAppendPath;

	public int getSelectedItemIndex() {
		return selectedItemIndex;
	}

	public void setSelectedItemIndex(int selectedItemIndex) {
		this.selectedItemIndex = selectedItemIndex;
	}

	public boolean isExpanded() {
		return isExpanded;
	}

	public void setIsExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}

	int selectedItemIndex;

	public int getSelectedFolderItemIndex() {
		return selectedFolderItemIndex;
	}

	public void setSelectedFolderItemIndex(int selectedFolderItemIndex) {
		this.selectedFolderItemIndex = selectedFolderItemIndex;
	}

	int selectedFolderItemIndex;

	boolean isExpanded;
	
	 public DBNoteItems getSeletedDBNoteItem() {
		return seletedDBNoteItem;
	}

	public void setSeletedDBNoteItem(DBNoteItems seletedDBNoteItem) {
		this.seletedDBNoteItem = seletedDBNoteItem;
	}

	int selectedIndex;

	public String getSelectedFolderId() {
		return selectedFolderId;
	}

	public void setSelectedFolderId(String selectedFolderId) {
		this.selectedFolderId = selectedFolderId;
	}

	String selectedFolderId;

	
	public SideMenuitems getSeletedListNoteItem() {
		return seletedListNoteItem;
	}

	public void setSeletedListNoteItem(SideMenuitems seletedListNoteItem) {
		this.seletedListNoteItem = seletedListNoteItem;
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}



	public NOTETYPE getSELECTED_TEXT_OPTION() 
	{
		return SELECTED_TEXT_OPTION;
	}

	public void setSELECTED_TEXT_OPTION(NOTETYPE sELECTED_TEXT_OPTION) {
		SELECTED_TEXT_OPTION = sELECTED_TEXT_OPTION;
	}

	public ArrayList<NoteListDataModel> arrNoteListData;
	
	
	NoteListDataModel notelistData;
	

	
	public NoteListDataModel getNotelistData() {
		return notelistData;
	}

	public void setNotelistData(NoteListDataModel notelistData) {
		this.notelistData = notelistData;
	}

	public static DataManager sharedDataManager()
	{
		if(manager==null)
		{
			manager=new DataManager();
		}
		
		return manager;
	}
	
	public void printname()
	{
		System.out.println("DataManager.printname()");
	}
	
	public Bitmap getUserImageBitMap() 
	{
		return userImageBitMap;
	}

	public void setUserImageBitMap(Bitmap userImageBitMap) 
	{
		this.userImageBitMap = userImageBitMap;
	}
	
	public boolean isTypeofListView() {
		return typeofListView;
	}

	public void setTypeofListView(boolean typeofListView) {
		this.typeofListView = typeofListView;
	}
	
	public ArrayList<NoteListDataModel> getArrNoteListData() {
		return arrNoteListData;
	}

	public void setArrNoteListData(ArrayList<NoteListDataModel> arrNoteListData) {
		this.arrNoteListData = arrNoteListData;
	}

	public  static  String  getFormateDateFromstring(String date_before) {
		return 	formateDateFromstring("dd/MM/yyyy HH:mm:ss", "EEE, dd MMM hh:mm a", date_before);
	}

	public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate){

		Date parsed = null;
		String outputDate = "";

		SimpleDateFormat df_input = new SimpleDateFormat(inputFormat);
		df_input.setTimeZone(TimeZone.getTimeZone("GMT"));
		SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

		try {
			parsed = df_input.parse(inputDate);
			outputDate = df_output.format(parsed);

		} catch (Exception e) {
			Log.d("kdflksjdflk", "ParseException - dateFormat");
		}

		return outputDate;

	}

	public  void  setUpUserPermission(Activity context,int requestCode)
	{
		switch (requestCode) {

			case 1:
			{
				if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
					final String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO};
					ActivityCompat.requestPermissions(context, permissions, 0);
				}
			}
			break;
			case 3:
			{
				if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
					final String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
					ActivityCompat.requestPermissions(context, permissions, 0);
				}
			}
			break;
			case 2:
			{
				if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
					final String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
					ActivityCompat.requestPermissions(context, permissions, 0);
				}
			}
			break;

		}




	}

}
