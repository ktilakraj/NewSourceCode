package com.eNotes.notesharedatabase;

import java.util.Comparator;

public class DBNoteItems {
	String note_Id;
	String note_Title;
	String note_Color;
	String note_Created_Time;
	String note_Modified_Time;
	String note_TimeBomb;
	String note_Reminder_Time;
	String note_UserId;
	String note_Element;
	String note_Server_Key;
	String note_Folder_Id;
	String note_Deleted_Status;
	String note_Lock_Status;

	public boolean isOpen() {
		return isOpen;
	}

	public void setIsOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	boolean isOpen;

	



	public String getNote_Lock_Status() {
		return note_Lock_Status;
	}

	public void setNote_Lock_Status(String note_Lock_Status) {
		this.note_Lock_Status = note_Lock_Status;
	}

	public String getNote_Id() {
		return note_Id;
	}

	public void setNote_Id(String note_Id) {
		this.note_Id = note_Id;
	}

	public String getNote_Title() {
		return note_Title;
	}

	public void setNote_Title(String note_Title) {
		this.note_Title = note_Title;
	}

	public String getNote_Created_Time() {
		return note_Created_Time;
	}

	public void setNote_Created_Time(String note_Created_Time) {
		this.note_Created_Time = note_Created_Time;
	}

	public String getNote_Modified_Time() {
		return note_Modified_Time;
	}

	public void setNote_Modified_Time(String note_Modified_Time) {
		this.note_Modified_Time = note_Modified_Time;
	}

	public String getNote_TimeBomb() {
		return note_TimeBomb;
	}

	public void setNote_TimeBomb(String note_TimeBomb) {
		this.note_TimeBomb = note_TimeBomb;
	}

	public String getNote_Reminder_Time() {
		return note_Reminder_Time;
	}

	public void setNote_Reminder_Time(String note_Reminder_Time) {
		this.note_Reminder_Time = note_Reminder_Time;
	}

	public String getNote_UserId() {
		return note_UserId;
	}

	public void setNote_UserId(String note_UserId) {
		this.note_UserId = note_UserId;
	}

	public String getNote_Element() {
		return note_Element;
	}

	public void setNote_Element(String note_Element) {
		this.note_Element = note_Element;
	}

	public String getNote_Server_Key() {
		return note_Server_Key;
	}

	public void setNote_Server_Key(String note_Server_Key) {
		this.note_Server_Key = note_Server_Key;
	}

	public String getNote_Folder_Id() {
		return note_Folder_Id;
	}

	public void setNote_Folder_Id(String note_Folder_Id) {
		this.note_Folder_Id = note_Folder_Id;
	}

	public String getNote_Deleted_Status() {
		return note_Deleted_Status;
	}

	public void setNote_Deleted_Status(String note_Deleted_Status) {
		this.note_Deleted_Status = note_Deleted_Status;
	}

	public String getNote_Color() {
		return note_Color;
	}

	public void setNote_Color(String note_Color) {
		this.note_Color = note_Color;
	}


	public static Comparator<DBNoteItems> DBNoteTitle = new Comparator<DBNoteItems>() {

		public int compare(DBNoteItems s1, DBNoteItems s2) {
			String StudentName1 = s1.getNote_Title();
			String StudentName2 = s2.getNote_Title();

			//ascending order
			return StudentName1.compareTo(StudentName2);

			//descending order
			//return StudentName2.compareTo(StudentName1);
		}};

	
}
