package com.eNotes.noteshare;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eNotes.datamodels.SideMenuitems;
import com.eNotes.notesharedatabase.DBNoteItems;
import com.mobiapp.ventures.eNotes.R;

import java.util.ArrayList;

public class FolderActivity extends DrawerActivity {
	public RelativeLayout layoutHeder;
	public ImageButton btnheaderMenu, btnsequence, btncalander;
	public TextView textheadertitle, textViewSubHeaderTitle;
	public LinearLayout layoutTitleHeaderview;

	public ArrayList<SideMenuitems> arrDataNote;
	public ArrayList<DBNoteItems> arrDBDataNote;

	public ListView listViewFoldernotelist;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// inflate your activity layout here!
		View contentView = inflater.inflate(R.layout.foldernotelist_activity, null,
				false);
		mDrawerLayout.addView(contentView, 0);
		initlizeUIElement(contentView);
	}

	void initlizeUIElement(View contentView) {


		arrDataNote = new ArrayList<SideMenuitems>();
		arrDBDataNote= new ArrayList<DBNoteItems>();

		// mainHeadermenue
		layoutHeder = (RelativeLayout) contentView
				.findViewById(R.id.mainHeadermenue);
		btnheaderMenu = (ImageButton) layoutHeder
				.findViewById(R.id.imageButtonHamburg);

		btnsequence = (ImageButton) layoutHeder
				.findViewById(R.id.imageButtonsquence);
		btncalander = (ImageButton) layoutHeder
				.findViewById(R.id.imageButtoncalander);
		btncalander.setVisibility(View.GONE);
		btnsequence.setVisibility(View.GONE);


		layoutTitleHeaderview = (LinearLayout) contentView
				.findViewById(R.id.titleHeaderview1);
		textViewSubHeaderTitle = (TextView) layoutTitleHeaderview
				.findViewById(R.id.textViewHeaderTitle1);
		textViewSubHeaderTitle.setText("");

		listViewFoldernotelist=(ListView)contentView.findViewById(R.id.listViewFoldernotelist);



		addListners();
	}

	@Override
	public void addListners() {
		// TODO Auto-generated method stub
		super.addListners();
		btnheaderMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openSlideMenu();
			}
		});

		listViewFoldernotelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



			}
		});

	}
}
