package com.eNotes.adpters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eNotes.dataAccess.DataManager;
import com.eNotes.datamodels.SideMenuitems;
import com.mobiapp.ventures.eNotes.R;

import java.util.ArrayList;



public class NewNoteFolderAdapter extends BaseAdapter {

	public interface  NewNoteFolderAdapter_Listner
	{
		void 	didFolderMoreSelected(SideMenuitems item1, int selectedindex);
		void  	didFolderExpandViewAtIndex(SideMenuitems item1,View selectedbutton, int selectedindex);
		void  	didFolderlistItemClick(SideMenuitems item1, View selectedbutton, int selectedindex);

	}
	public Activity activity;

	LayoutInflater inflater;

	public boolean theviewtype;
	public NewNoteFolderAdapter_Listner listener;

	public ArrayList<SideMenuitems> arrDataMenu;
	public ArrayList<SideMenuitems> mDisplayedValues;

	public NewNoteFolderAdapter(Activity context,
			ArrayList<SideMenuitems> arrDataMenu,NewNoteFolderAdapter_Listner listener) {

		this.activity = context;
		this.arrDataMenu = arrDataMenu;
		this.listener=listener;

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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View vi = convertView;

		ViewHolder holder;

		if (convertView == null) {

			/****** Inflate tabitem.xml file for each row ( Defined below ) *******/
			vi = inflater.inflate(R.layout.notefolderrow, null);

			/****** View Holder Object to contain tabitem.xml file elements ******/

			holder = new ViewHolder();
			holder.textViewSlideMenuName = (TextView) vi
					.findViewById(R.id.textViewSlideMenuName);
			holder.layoutsepreter = (View) vi.findViewById(R.id.layoutsepreter);
			holder.textViewSlideMenuNameSubTitle = (TextView) vi
					.findViewById(R.id.textViewSlideMenuNameSubTitle);
			holder.layoutnotefolderAdapter = (LinearLayout) vi
					.findViewById(R.id.layoutnotefolderAdapter);
			holder.layoutFolderMoreOprtion = (LinearLayout) vi.findViewById(R.id.LayoutFolderMoreOption);
			holder.layoutDateAndShare =  (RelativeLayout) vi.findViewById(R.id.layoutDateAndShare);
			holder.folderDeleted = (ImageButton)  vi.findViewById(R.id.folderDeleted);
			holder.imageButtomFolderMore = (ImageButton) holder.layoutFolderMoreOprtion.findViewById(R.id.imageButtomFolderMore);
			/************ Set holder with LayoutInflater ************/
			vi.setTag(holder);
		} else
			holder = (ViewHolder) vi.getTag();

		vi.setClickable(true);

		final SideMenuitems model = arrDataMenu.get(position);
		holder.layoutFolderMoreOprtion.setVisibility(View.VISIBLE);
		holder.layoutFolderMoreOprtion.setTag(position);
		holder.imageButtomFolderMore.setTag(position);
		holder.folderDeleted.setTag(FOLDER_ACTION.FOLDERDELETE);
		holder.layoutDateAndShare.setVisibility(View.GONE);
		holder.imageButtomFolderMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				listener.didFolderMoreSelected(model,position);
			}
		});

		holder.folderDeleted.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.didFolderExpandViewAtIndex(model,v,position);
			}
		});

		vi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

			listener.didFolderlistItemClick(model,view,position);

			}
		});


		if (DataManager.sharedDataManager().getSelectedFolderItemIndex() == position) {

			holder.layoutDateAndShare.setVisibility(View.VISIBLE);
		}




		holder.textViewSlideMenuName.setText(model.getMenuName());
		this.theviewtype = DataManager.sharedDataManager().isTypeofListView();

		String date_before = model.getMenuNameDetail();
		String date_after =DataManager.getFormateDateFromstring(date_before);// formateDateFromstring("dd/MM/yyyy HH:mm:ss", "EEE, dd MMM yyyy, hh:mm a", date_before);

		if (theviewtype == true) {
			holder.textViewSlideMenuNameSubTitle.setText(model
					.getMenuNameDetail());
			holder.textViewSlideMenuNameSubTitle.setVisibility(View.VISIBLE);
			System.out.println("list title");
			holder.textViewSlideMenuNameSubTitle.setText(date_after);
		} else {
			holder.textViewSlideMenuNameSubTitle.setVisibility(View.VISIBLE);
			holder.textViewSlideMenuNameSubTitle.setText(date_after);
			System.out.println("detail");
		}

		holder.layoutsepreter.setVisibility(View.VISIBLE);
		vi.setBackgroundColor(Color
				.parseColor(model.getColours()));
		
		if (model.getColours().equalsIgnoreCase("#ffffff"))
		{
			
		}else
		{
			holder.layoutsepreter.setBackgroundColor(Color
					.parseColor(model.getColours()));
			
		}

		return vi;
	}

	public static class ViewHolder {

		public TextView textViewSlideMenuName;
		public TextView textViewSlideMenuNameSubTitle;
		public LinearLayout layoutnotefolderAdapter;
		public View layoutsepreter;
		public  LinearLayout layoutFolderMoreOprtion;
		public ImageButton imageButtomFolderMore,folderDeleted;
		public RelativeLayout layoutDateAndShare;

	}
	
//	public Filter getFilter()
//	{
//	    Filter filter = new Filter() {
//
//	        @SuppressWarnings("unchecked")
//	        @Override
//	        protected void publishResults(CharSequence constraint,FilterResults results) {
//
//	            mDisplayedValues = (ArrayList<SideMenuitems>) results.values; // has the filtered values
//	            notifyDataSetChanged();  // notifies the data with new filtered values
//	        }
//
//	        protected FilterResults performFiltering(CharSequence constraint) {
//	            FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
//	            ArrayList<SideMenuitems> FilteredArrList = new ArrayList<SideMenuitems>();
//
//	            if (arrDataMenu == null) {
//	            	arrDataMenu = new ArrayList<SideMenuitems>(mDisplayedValues); // saves the original data in mOriginalValues
//	            }
//
//	            /********
//	             * 
//	             *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
//	             *  else does the Filtering and returns FilteredArrList(Filtered)  
//	             *
//	             ********/
//	            if (constraint == null || constraint.length() == 0) {
//
//	                // set the Original result to return  
//	                results.count = arrDataMenu.size();
//	                results.values = arrDataMenu;
//	            } else {
//	                constraint = constraint.toString().toLowerCase();
//	                for (int i = 0; i < arrDataMenu.size(); i++)
//	                {
//	                    String data = arrDataMenu.get(i).getMenuName();
//	                    if (data.toLowerCase().startsWith(constraint.toString()))
//	                    {
//	                        FilteredArrList.add(arrDataMenu.get(i));
//	                    }
//	                }
//	                // set the Filtered result to return
//	                results.count = FilteredArrList.size();
//	                results.values = FilteredArrList;
//	            }
//	            return results;
//	        }
//
//			@Override
//			public boolean onLoadClass(Class clazz) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//	    };
//	    return filter;
//
//		
//	}

}
