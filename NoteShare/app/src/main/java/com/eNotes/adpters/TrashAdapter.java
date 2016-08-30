package com.eNotes.adpters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.eNotes.dataAccess.DataManager;
import com.eNotes.notesharedatabase.DBNoteItems;
import com.mobiapp.ventures.eNotes.R;

import java.util.ArrayList;

public class TrashAdapter extends BaseAdapter {


	public  interface   TrashAdapterListener {

		void  didSelectedAction(View vi,DBNoteItems item);
	}
	public Activity activity;

	LayoutInflater inflater;

	public ArrayList<DBNoteItems> arrDataMenu;
	private  TrashAdapterListener listener;

	public TrashAdapter(Activity context,ArrayList<DBNoteItems> arrDataMenu,TrashAdapterListener listener) {

		this.activity = context;
		this.arrDataMenu = arrDataMenu;
		this.listener = listener;

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
			ViewHolder holder;

			if (convertView == null) {

				/****** Inflate tabitem.xml file for each row ( Defined below ) *******/
				vi = inflater.inflate(R.layout.trashrow, null);

				/****** View Holder Object to contain tabitem.xml file elements ******/

				holder = new ViewHolder();
				holder.textViewSlideMenuName = (TextView) vi
						.findViewById(R.id.textViewSlideMenuName);
				holder.textViewsubTitle = (TextView)vi.findViewById(R.id.textViewsubTitle);
				holder.imageViewSlideMenuImage = (ImageView) vi
						.findViewById(R.id.imageViewSlidemenu);
				holder.layoutsepreter= (View) vi
						.findViewById(R.id.layoutsepreter);

				holder.delete = (ImageButton) vi
						.findViewById(R.id.delete);
				holder.restore = (ImageButton) vi
						.findViewById(R.id.restore);

				/************ Set holder with LayoutInflater ************/
				vi.setTag(holder);
			} else
				holder = (ViewHolder) vi.getTag();

			final DBNoteItems model = arrDataMenu.get(position);
			String date_before = model.getNote_Created_Time();
			String date_after = DataManager.getFormateDateFromstring(date_before);
			holder.textViewSlideMenuName.setText(model.getNote_Title());
			holder.textViewsubTitle.setText(""+date_after);
			holder.layoutsepreter.setVisibility(View.VISIBLE);
		holder.restore.setVisibility(View.GONE);
		holder.restore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (listener !=null) {

					listener.didSelectedAction(v,model);

				}
			}
		});

		holder.delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (listener !=null) {

					listener.didSelectedAction(v,model);

				}
			}
		});

		return vi;
	}

	public static class ViewHolder {

		public TextView textViewSlideMenuName,textViewsubTitle;
		public ImageView imageViewSlideMenuImage;
		public View layoutsepreter;
		ImageButton delete,restore;

	}

	public static class ViewHolder1 {

		public TextView textViewusername;
		public ImageView imageViewUserImage;
		public TextView textViewUserbalance;
		

	}

}
