package com.eNotes.adpters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eNotes.notesharedatabase.DBNoteItems;
import com.mobiapp.ventures.eNotes.R;

import java.util.ArrayList;

public class FolderListingAdapter extends BaseAdapter {

	public Activity activity;
	LayoutInflater inflater;

	public ArrayList<DBNoteItems> arrDataMenu;

	public FolderListingAdapter(Activity context,
								ArrayList<DBNoteItems> arrDataMenu) {
		//this.arrData = arrData;
		this.activity = context;
		this.arrDataMenu = arrDataMenu;

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

		View vi = convertView=null;
		
		switch (position)
		{
		default:
		{
			ViewHolder holder;

			if (convertView == null) {


				vi = inflater.inflate(R.layout.folderlistingrow, null);



				holder = new ViewHolder();
				holder.textViewSlideMenuName = (TextView) vi
						.findViewById(R.id.textViewSlideMenuName);
				holder.imageViewSlideMenuImage = (ImageView) vi
						.findViewById(R.id.imageViewSlidemenu);
				holder.layoutsepreter= (View) vi
						.findViewById(R.id.layoutsepreter);


				vi.setTag(holder);
			} else
				holder = (ViewHolder) vi.getTag();

			DBNoteItems model = arrDataMenu.get(position);
			holder.textViewSlideMenuName.setText(model.getNote_Title());
			//holder.imageViewSlideMenuImage.setImageResource(model.resourceId);
			holder.layoutsepreter.setVisibility(View.VISIBLE);

			

			
		
		}
			break;
		}



		return vi;
	}

	public static class ViewHolder {

		public TextView textViewSlideMenuName;
		public ImageView imageViewSlideMenuImage;
		public View layoutsepreter;

	}



}
