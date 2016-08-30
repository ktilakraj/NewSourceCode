package com.eNotes.adpters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.eNotes.datamodels.SideMenuitems;
import com.eNotes.noteshare.MainActivity;
import com.mobiapp.ventures.eNotes.R;

import java.util.ArrayList;

public class NoteListAdapter_1 extends BaseAdapter {

	public Activity activity;
	LayoutInflater inflater;
	public ArrayList<SideMenuitems> arrDataMenu;
	public NoteListAdapter_1(Activity context,
							 ArrayList<SideMenuitems> arrDataMenu) {

		this.activity = context;
		this.arrDataMenu = arrDataMenu;

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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View vi = convertView;
		final ViewHolder holder;

		if (convertView == null)
		{

			/****** Inflate tabitem.xml file for each row ( Defined below ) *******/
			vi = inflater.inflate(R.layout.notefolderrow_1, null);

			/****** View Holder Object to contain tabitem.xml file elements ******/

			holder = new ViewHolder();



			holder.textViewSlideMenuName = (TextView) vi
					.findViewById(R.id.noteTitle);

			holder.textViewSlideMenuNameSubTitle = (TextView) vi
					.findViewById(R.id.noteTitleType);
			holder.btnmoreOption1=(ImageButton) vi.findViewById(R.id.btnmoreOption1);



			holder.itemTag=position;

			vi.setTag(holder);


		}
		else
			holder = (ViewHolder) vi.getTag();
		holder.btnmoreOption1.setTag(position);
		SideMenuitems model = arrDataMenu.get(position);
		holder.textViewSlideMenuName.setText(model.getMenuName());
		holder.textViewSlideMenuNameSubTitle.setText(model
				.getMenuNameDetail());


		holder.btnmoreOption1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Log.d("the btn More click", "" + holder.btnmoreOption1.getTag());

				SideMenuitems model1 = arrDataMenu.get((Integer)holder.btnmoreOption1.getTag());

				((MainActivity)activity).didMoreSelected(model1,view,(Integer)holder.btnmoreOption1.getTag());

			}
		});

		vi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				ViewHolder	holder1 = (ViewHolder) view.getTag();
				SideMenuitems model1 = arrDataMenu.get(holder1.itemTag);
				Log.d("list item click", "clicking" + view.getTag());

				((MainActivity)activity).didlistItemClick(model1, view,holder1.itemTag);
			}
		});
		return vi;

	}

	public static class ViewHolder {

		public TextView textViewSlideMenuName;
		public TextView textViewSlideMenuNameSubTitle;
		public View layoutsepreter;
		public ImageButton btnmoreOption1;
		public int itemTag;

	}
	


}
