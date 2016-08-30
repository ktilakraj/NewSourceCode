package com.eNotes.adpters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eNotes.dataAccess.DataManager;
import com.eNotes.notesharedatabase.DBNoteItems;
import com.mobiapp.ventures.eNotes.R;

import java.util.ArrayList;

import static com.mobiapp.ventures.eNotes.R.id.btnmoreOption1;

public class NoteList_Adapter_New extends BaseAdapter implements View.OnClickListener {

	public interface  NoteList_Adapter_New_Listner
	{
		void didMoreSelected(DBNoteItems item1,View selectedbutton, int selectedindex);
		void  didlistItemClick(DBNoteItems item1,View selectedbutton, int selectedindex);
		void  didExpandViewAtIndex(DBNoteItems item1,View selectedbutton, int selectedindex);

	}

	public Activity activity;
	LayoutInflater inflater;
	public ArrayList<DBNoteItems> arrDataMenu;
	NoteList_Adapter_New_Listner listner;

	public NoteList_Adapter_New(Activity context,
								ArrayList<DBNoteItems> arrDataMenu,NoteList_Adapter_New_Listner listner) {

		this.activity = context;
		this.arrDataMenu = arrDataMenu;
		this.listner=listner;

		System.out.println("adapter notify");
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.arrDataMenu.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub

		View vi = convertView;
		final ViewHolder holder;

		if (convertView == null)
		{

			/****** Inflate tabitem.xml file for each row ( Defined below ) *******/
			vi = inflater.inflate(R.layout.notelist_row_new, null);

			/****** View Holder Object to contain tabitem.xml file elements ******/

			holder = new ViewHolder();

			holder.colorMarker=(LinearLayout)vi.findViewById(R.id.colorMarker1);
			holder.textViewSlideMenuName = (TextView) vi
					.findViewById(R.id.noteTitle);

			holder.textViewSlideMenuNameSubTitle = (TextView) vi
					.findViewById(R.id.noteTitleType);
			holder.elementType=(ImageView)vi.findViewById(R.id.elementType);
			holder.btnmoreOption1=(ImageButton) vi.findViewById(btnmoreOption1);
			holder.layoutMoreinfo=(LinearLayout) vi.findViewById(R.id.layoutMoreinfo);
			holder.layoutMoreinfo.setClickable(true);
			holder.showhideView=(LinearLayout)vi.findViewById(R.id.showhideView);

			holder.editNoteTitle=(ImageButton)holder.showhideView.findViewById(R.id.editNoteTitle);
			holder.noteTimeBomb=(ImageButton)holder.showhideView.findViewById(R.id.noteTimeBomb);
			holder.noteLock=(ImageButton)holder.showhideView.findViewById(R.id.noteLock);
			holder.noteMoveToFolder=(ImageButton)holder.showhideView.findViewById(R.id.noteMoveToFolder);
			holder.noteAddColor=(ImageButton)holder.showhideView.findViewById(R.id.noteAddColor);
			holder.noteDelete=(ImageButton)holder.showhideView.findViewById(R.id.noteDelete);
			holder.noteShare=(ImageButton)holder.showhideView.findViewById(R.id.noteShare);
			holder.noteunlockLock=(ImageButton)holder.showhideView.findViewById(R.id.noteunlockLock);
			holder.noteremindertime=(ImageButton)holder.showhideView.findViewById(R.id.noteremindertime);



			holder.imageLock=(ImageView)vi.findViewById(R.id.imageLock);
			holder.imageReminder=(ImageView)vi.findViewById(R.id.imageReminder);
			holder.imageTimeBomb=(ImageView)vi.findViewById(R.id.imageTimeBomb);

			holder.imageLock.setVisibility(View.GONE);
			holder.layoutsepreter=(View)vi.findViewById(R.id.layoutsepreter);
			holder.layoutsepreter.setBackgroundColor(Color.BLUE);
			holder.Layouttags=(LinearLayout)vi.findViewById(R.id.Layouttags);

			holder.itemTag=position;


			vi.setTag(holder);


		}
		else

			holder = (ViewHolder) vi.getTag();

		holder.imageLock.setVisibility(View.GONE);
		holder.imageReminder.setVisibility(View.GONE);
		holder.imageTimeBomb.setVisibility(View.GONE);

		holder.noteShare.setVisibility(View.GONE);

		holder.btnmoreOption1.setTag(position);
		holder.layoutMoreinfo.setTag(position);
		holder.noteShare.setTag(position);
		holder.noteDelete.setTag(position);
		holder.noteAddColor.setTag(position);
		holder.noteMoveToFolder.setTag(position);
		holder.noteLock.setTag(position);
		holder.noteTimeBomb.setTag(position);
		holder.editNoteTitle.setTag(position);
		holder.noteunlockLock.setTag(position);
		holder.noteremindertime.setTag(position);

		holder.imageTimeBomb.setTag(position);
		holder.imageLock.setTag(position);
		holder.imageReminder.setTag(position);




		holder.noteShare.setOnClickListener(this);
		holder.noteDelete.setOnClickListener(this);
		holder.noteAddColor.setOnClickListener(this);
		holder.noteMoveToFolder.setOnClickListener(this);
		holder.noteLock.setOnClickListener(this);
		holder.noteTimeBomb.setOnClickListener(this);
		holder.editNoteTitle.setOnClickListener(this);
		holder.noteunlockLock.setOnClickListener(this);
		holder.noteremindertime.setOnClickListener(this);

		holder.imageReminder.setOnClickListener(this);
		holder.imageTimeBomb.setOnClickListener(this);
		holder.imageLock.setOnClickListener(this);



		holder.noteLock.setVisibility(View.VISIBLE);
		holder.noteunlockLock.setVisibility(View.GONE);
	holder.Layouttags.setBackgroundColor(Color.WHITE);





		DBNoteItems model = arrDataMenu.get(position);






		if (model.getNote_Lock_Status()!=null)
		{

			if (model.getNote_Lock_Status().equalsIgnoreCase("1"))
			{

				holder.imageLock.setVisibility(View.VISIBLE);
				holder.noteLock.setVisibility(View.GONE);
				holder.noteunlockLock.setVisibility(View.VISIBLE);

			}

		}

		if (model.getNote_TimeBomb()!=null)
		{
			if (model.getNote_TimeBomb().equalsIgnoreCase("")||model.getNote_TimeBomb().equalsIgnoreCase("0"))
			{

			}else
			{

				holder.imageTimeBomb.setVisibility(View.VISIBLE);
			}
		}

		if (model.getNote_Reminder_Time()!=null)
		{
			if (model.getNote_Reminder_Time().equalsIgnoreCase("")||model.getNote_Reminder_Time().equalsIgnoreCase("0"))
			{

			}else
			{

				holder.imageReminder.setVisibility(View.VISIBLE);
			}
		}



		holder.textViewSlideMenuName.setText(model.getNote_Title());

		//holder.textViewSlideMenuNameSubTitle.setText(model.getNote_Element());

		String date_before = model.getNote_Created_Time();
		String date_after =DataManager.getFormateDateFromstring(date_before);// formateDateFromstring("dd/MM/yyyy HH:mm:ss", "EEE, dd MMM yyyy, hh:mm a", date_before);

		holder.textViewSlideMenuNameSubTitle.setText(date_after);
		if (model.getNote_Element().equalsIgnoreCase("TEXT"))
		{


			holder.elementType.setImageResource(R.drawable.text_icon_1);


			holder.elementType.setVisibility(View.VISIBLE);

		}
		else if (model.getNote_Element().equalsIgnoreCase("AUDIO"))
		{
			holder.elementType.setImageResource(R.drawable.audio_icon_1);
			holder.elementType.setVisibility(View.VISIBLE);

		}else  if (model.getNote_Element().equalsIgnoreCase("IMAGE"))
		{
			holder.elementType.setImageResource(R.drawable.gallary_icon);
			holder.elementType.setVisibility(View.VISIBLE);
		}else
		{
			holder.elementType.setVisibility(View.GONE);
		}


		if (DataManager.sharedDataManager().getSelectedItemIndex()==position)
		{
			holder.showhideView.setVisibility(View.VISIBLE);

		}
		else
		{
			holder.showhideView.setVisibility(View.GONE);
		}


			holder.layoutMoreinfo.setOnClickListener(new View.OnClickListener()
	{
			@Override
			public void onClick(View view)
			{

				Log.d("the btn More click", "" + holder.layoutMoreinfo.getTag());

				DBNoteItems model1 = arrDataMenu.get((Integer)holder.layoutMoreinfo.getTag());

				//listner.didMoreSelected(model1,view,(Integer)holder.btnmoreOption1.getTag());


				listner.didExpandViewAtIndex(model1, view, (Integer) holder.layoutMoreinfo.getTag());

			}
		});





		vi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				ViewHolder	holder1 = (ViewHolder) view.getTag();
				DBNoteItems model1 = arrDataMenu.get(holder1.itemTag);
				Log.d("list item click", "clicking" + view.getTag());

				listner.didlistItemClick(model1, view,holder1.itemTag);
			}
		});


		if (model.getNote_Color()!=null)
			if (model.getNote_Color().length() > 0)
			{
				String temp = model.getNote_Color();
				if(temp.indexOf("#")!=-1)
				{
					//System.out.println("there is 'b' in temp string");
					holder.layoutsepreter.setBackgroundColor(Color.parseColor(model.getNote_Color()));
					holder.Layouttags.setBackgroundColor(Color.parseColor(model.getNote_Color()));
				}
				else {
					//System.out.println("there is no 'b' in temp string");

					holder.layoutsepreter.setBackgroundColor(Color.parseColor("#"+model.getNote_Color()));
					holder.Layouttags.setBackgroundColor(Color.parseColor("#"+model.getNote_Color()));
				}

			}

		else
		{
			holder.layoutsepreter.setBackgroundColor(Color.BLUE);
		}

			vi.setBackgroundColor(Color.WHITE);



		return vi;



	}

	public static class ViewHolder {

		public TextView textViewSlideMenuName;
		public TextView textViewSlideMenuNameSubTitle;
		public View layoutsepreter;
		public ImageButton btnmoreOption1;
		public ImageButton noteunlockLock,editNoteTitle,noteTimeBomb,noteLock,noteMoveToFolder,noteAddColor,noteDelete,noteShare,noteremindertime;
		public int itemTag;
		public LinearLayout showhideView,colorMarker,layoutMoreinfo,Layouttags;
		public ImageView imageLock,imageReminder,imageTimeBomb,elementType;

	}


	@Override
	public void onClick(View view)
	{

		Log.d("the btn More click", "" + view.getTag());

		DBNoteItems model1 = arrDataMenu.get((Integer)view.getTag());

		listner.didMoreSelected(model1,view,(Integer)view.getTag());

	}



}
