package com.eNotes.notesharedatabase;

public class DBNoteItemElement 
{
	
	static String TEXTMODE;
	static String IMAGEMODE;
	static String SCRIBBLEMODE;
	static String AUDIOMODE;
	static String SIZE;
	static String FONT;
	
	String NOTE_ID;
	String NOTE_ELEMENT_ID;
	String NOTE_ELEMENT_CONTENT;
	String NOTE_ELEMENT_TYPE;
	String NOTE_ELEMENT_MEDIA_TIME;
	String NOTE_ELEMENT_ISCHECKED;

	public String getNOTE_ELEMENT_DATE_TIME() {
		return NOTE_ELEMENT_DATE_TIME;
	}

	public void setNOTE_ELEMENT_DATE_TIME(String NOTE_ELEMENT_DATE_TIME) {
		this.NOTE_ELEMENT_DATE_TIME = NOTE_ELEMENT_DATE_TIME;
	}

	String NOTE_ELEMENT_DATE_TIME;
	
	public String getNOTE_ID() {
		return NOTE_ID;
	}
	public void setNOTE_ID(String nOTE_ID) {
		NOTE_ID = nOTE_ID;
	}
	public String getNOTE_ELEMENT_ID() {
		return NOTE_ELEMENT_ID;
	}
	public void setNOTE_ELEMENT_ID(String nOTE_ELEMENT_ID) {
		NOTE_ELEMENT_ID = nOTE_ELEMENT_ID;
	}
	public String getNOTE_ELEMENT_CONTENT() {
		return NOTE_ELEMENT_CONTENT;
	}
	public void setNOTE_ELEMENT_CONTENT(String nOTE_ELEMENT_CONTENT) {
		NOTE_ELEMENT_CONTENT = nOTE_ELEMENT_CONTENT;
	}
	public String getNOTE_ELEMENT_TYPE() {
		return NOTE_ELEMENT_TYPE;
	}
	public void setNOTE_ELEMENT_TYPE(String nOTE_ELEMENT_TYPE) {
		NOTE_ELEMENT_TYPE = nOTE_ELEMENT_TYPE;
	}
	public String getNOTE_ELEMENT_MEDIA_TIME() {
		return NOTE_ELEMENT_MEDIA_TIME;
	}
	public void setNOTE_ELEMENT_MEDIA_TIME(String nOTE_ELEMENT_MEDIA_TIME) {
		NOTE_ELEMENT_MEDIA_TIME = nOTE_ELEMENT_MEDIA_TIME;
	}
	public String getNOTE_ELEMENT_ISCHECKED() {
		return NOTE_ELEMENT_ISCHECKED;
	}
	public void setNOTE_ELEMENT_ISCHECKED(String nOTE_ELEMENT_ISCHECKED) {
		NOTE_ELEMENT_ISCHECKED = nOTE_ELEMENT_ISCHECKED;
	}
}
