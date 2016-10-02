package com.eNotes.noteshare;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eNotes.dataAccess.DataManager;
import com.eNotes.datamodels.NOTETYPE;
import com.eNotes.datamodels.NoteListDataModel;
import com.eNotes.notesharedatabase.DBNoteItems;
import com.eNotes.notesharedatabase.NoteshareDatabaseHelper;
import com.mobiapp.ventures.eNotes.R;
import com.squareup.picasso.Picasso;

import java.io.File;


public class NoteItemFullScreen extends Activity {

	public ImageView imageviewfullscreenmode;
	Button imageClose,imageDelete ;
	public NoteshareDatabaseHelper androidOpenDbHelperObj;
	SQLiteDatabase sqliteDatabase;
	NoteListDataModel notelistitem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notelist_fullscreen_activity);
		imageviewfullscreenmode = (ImageView) findViewById(R.id.imageviewfullscreenmode);
		 notelistitem = DataManager.sharedDataManager()
				.getNotelistData();

		if (notelistitem.noteType == NOTETYPE.IMAGEMODE)
		{
			imageviewfullscreenmode.setImageBitmap(notelistitem.getBitmap());
			Picasso.with(this).load(new File(notelistitem.getBitmapPath())).into(imageviewfullscreenmode);

		} else if (notelistitem.noteType == NOTETYPE.CAMERAIMAGEMODE) {

			try {

				Uri outPutfileUri =Uri.parse(notelistitem.getBitmapPath());
				Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outPutfileUri);
				Drawable d = new BitmapDrawable(this.getResources(), bitmap);
				imageviewfullscreenmode.setImageDrawable(d);

			} catch (Exception e){

			}
		}


		imageClose=(Button) findViewById(R.id.imageClose);
		imageDelete=(Button) findViewById(R.id.imageDelete);
		imageClose.setOnClickListener(new OnClickListener() {
		@Override
			public void onClick(View arg0) {
			// TODO Auto-generated method stub
				finish();
			}
		});

		imageDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showAlertWithDeleteMessage("Do you want to delete image?",NoteItemFullScreen.this,null);
			}
		});

		databaseInitlization();

	}

	void databaseInitlization()
	{
		// Data base initilization

		try
		{
			androidOpenDbHelperObj = new NoteshareDatabaseHelper(this);
			sqliteDatabase = androidOpenDbHelperObj.getWritableDatabase();


		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	void showAlertWithDeleteMessage(String message, Context context, final DBNoteItems dbNoteItems) {

		final Dialog dialogdelete = new Dialog(NoteItemFullScreen.this);

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// inflate your activity layout here!
		View contentView = inflater.inflate(R.layout.alert_view, null, false);

		TextView textViewTitleAlert = (TextView) contentView
				.findViewById(R.id.textViewTitleAlert);
		textViewTitleAlert.setText("Delete");
		textViewTitleAlert.setTextColor(Color.WHITE);
		TextView textViewTitleAlertMessage = (TextView) contentView
				.findViewById(R.id.textViewTitleAlertMessage);
		textViewTitleAlertMessage.setText(message);

		Button buttonAlertCancel = (Button) contentView
				.findViewById(R.id.buttonAlertCancel);
		Button buttonAlertOk = (Button) contentView
				.findViewById(R.id.buttonAlertOk);
		buttonAlertCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialogdelete.dismiss();

			}
		});
		buttonAlertOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				boolean status = androidOpenDbHelperObj.deleteNoteElement(notelistitem.getNoteElmentId());
				if (status == true) {

					Toast.makeText(NoteItemFullScreen.this, "Image deleted successfully", Toast.LENGTH_SHORT).show();
					finish();

				} else {
					Toast.makeText(NoteItemFullScreen.this, "Image deleted unsuccessfully", Toast.LENGTH_SHORT).show();

				}
				dialogdelete.dismiss();

			}
		});

		dialogdelete.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {


			}
		});

		dialogdelete.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogdelete.setCancelable(false);
		dialogdelete.setContentView(contentView);
		dialogdelete.show();

	}

}
