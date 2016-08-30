package com.eNotes.adpters;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eNotes.dataAccess.DataManager;
import com.eNotes.datamodels.NoteListDataModel;
import com.eNotes.noteshare.NoteMainActivity;
import com.mobiapp.ventures.eNotes.R;

import java.util.ArrayList;


public class NotesListAdapter extends BaseAdapter {



//	public interface AudiorecordingClick
//	{
//		public void didClickPlaybutton(NoteListDataModel notelistData, int index);
//
//		public void didClickStopbutton(NoteListDataModel notelistData, int index);
//	}
//
//	public interface OnTextNoteEditing
//	{
//		public void didEditedTextField(NoteListDataModel notelistData, int index,String text);
//
//	}


	public Activity activity;
	LayoutInflater inflater;
	public ArrayList<NoteListDataModel> arrNoteListData;

	public NotesListAdapter(Activity context,
			ArrayList<NoteListDataModel> arrNoteListData) {

		this.activity = context;
		this.arrNoteListData = arrNoteListData;

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.arrNoteListData.size();
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

		if (convertView == null) {

			/****** Inflate tabitem.xml file for each row ( Defined below ) *******/
			vi = inflater.inflate(R.layout.notelistrow, null);

			/****** View Holder Object to contain tabitem.xml file elements ******/

			holder = new ViewHolder();
			holder.textViewSlideMenuName = (EditText) vi
					.findViewById(R.id.textViewSlideMenuName);
			holder.imageViewSlideMenuImage = (ImageView) vi
					.findViewById(R.id.imageViewSlidemenu);


			holder.LayoutAudioRecording = (RelativeLayout) vi
					.findViewById(R.id.LayoutAudioRecording);

			holder.buttonPlay = (ImageButton) holder.LayoutAudioRecording
					.findViewById(R.id.buttonPlay);
			holder.buttonStop = (ImageButton) holder.LayoutAudioRecording
					.findViewById(R.id.buttonStop);
			holder.buttonRecord = (ImageButton) holder.LayoutAudioRecording
					.findViewById(R.id.buttonRecord);
			holder.buttonPause = (ImageButton) holder.LayoutAudioRecording
					.findViewById(R.id.buttonPause);

			holder.audioClose=(ImageButton)holder.LayoutAudioRecording.findViewById(R.id.audioClose);

			holder.buttonRecordPause = (ImageButton) holder.LayoutAudioRecording
					.findViewById(R.id.buttonRecordPause);
			holder.progressRecord1 = (SeekBar) holder.LayoutAudioRecording
					.findViewById(R.id.progressRecord1);
			holder.textViewDuration = (TextView) holder.LayoutAudioRecording
					.findViewById(R.id.textViewDuration);
			holder.textViewcreationDate = (TextView) holder.LayoutAudioRecording
					.findViewById(R.id.textViewcreationDate);

			holder.mediaPlayer = new MediaPlayer();

			/************ Set holder with LayoutInflater ************/
			vi.setTag(holder);
		} else
			holder = (ViewHolder) vi.getTag();
		holder.buttonPause.setTag(position);
		holder.buttonPlay.setTag(position);
		holder.textViewSlideMenuName.setTag(position);
		final NoteListDataModel model = arrNoteListData.get(position);
		holder.textViewSlideMenuName.setVisibility(View.GONE);
		holder.imageViewSlideMenuImage.setVisibility(View.GONE);
		holder.audioClose.setVisibility(View.GONE);
		holder.LayoutAudioRecording.setVisibility(View.GONE);

		String date_before = model.getNoteElementDateTime();
		String date_after = DataManager.getFormateDateFromstring(date_before);// formateDateFromstring("dd/MM/yyyy HH:mm:ss", "EEE, dd MMM hh:mm a", date_before);

		switch (model.noteType)
		{
			case AUDIOMODE:
			{
				if (model.strAudioFilePath.length() > 0)
				{
					holder.LayoutAudioRecording.setVisibility(View.VISIBLE);
					System.out.println("the out put file path:"
							+ model.strAudioFilePath);

					holder.buttonRecord.setVisibility(View.GONE);
					holder.buttonStop.setVisibility(View.GONE);
					holder.buttonPlay.setVisibility(View.VISIBLE);
					holder.buttonRecordPause.setVisibility(View.GONE);
					holder.buttonPause.setVisibility(View.GONE);
					holder.progressRecord1.setProgress(5);
					holder.progressRecord1.setVisibility(View.VISIBLE);
					//holder.textViewDuration.setText(""+date_after);
					holder.textViewcreationDate.setText(""+date_after);

					holder.buttonPlay.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							System.out.println("List button click play");

							try {

								System.out.println("file playing path:" + model.strAudioFilePath);
								holder.mediaPlayer.stop();
								holder.mediaPlayer.reset();
								holder.mediaPlayer.release();
								holder.mediaPlayer = new MediaPlayer();
								holder.mediaPlayer.setDataSource(model.strAudioFilePath);
								holder.mediaPlayer.prepare();
								holder.buttonPlay.setVisibility(View.GONE);
								holder.buttonPause.setVisibility(View.VISIBLE);
							}

							catch (Exception e) {
								e.printStackTrace();
							}
							holder.mediaPlayer
									.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
										@Override
										public void onCompletion(MediaPlayer arg0) {

											//holder.buttonPlay.setEnabled(true);
											//holder.buttonPause.setEnabled(true);
											holder.buttonPlay.setVisibility(View.VISIBLE);
											holder.buttonPause.setVisibility(View.GONE);
											// progressRecord1.setProgress(0);

										}
									});

							holder.progressRecord1.setMax(holder.mediaPlayer.getDuration() / 1000);

							//holder.progressRecord1.setMax(1);
							holder.mediaPlayer.start();

							final Handler mHandler = new Handler();
							// Make sure you update Seekbar on UI thread

							activity.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									if (holder.mediaPlayer != null) {
										int mCurrentPosition = holder.mediaPlayer
												.getCurrentPosition() / 1000;
										String currentduration = getDurationBreakdown(holder.mediaPlayer
												.getCurrentPosition());
										String currentduration1 = getDurationBreakdown(holder.mediaPlayer
												.getDuration());

										if (mCurrentPosition <= holder.mediaPlayer
												.getDuration() / 1000) {
											System.out.println("CurrentDuration:"
													+ currentduration);
											holder.progressRecord1.setProgress(mCurrentPosition);

											//holder.progressRecord1.setProgress(1);

											holder.textViewDuration
													.setText(currentduration + "/"
															+ currentduration1);

										}

									}
									mHandler.postDelayed(this, 1000);
								}
							});

							Toast.makeText(activity, "Recording playing",
									Toast.LENGTH_SHORT).show();

						}
					});

					holder.buttonPause.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							System.out.println("List button click pause");
							holder.mediaPlayer.pause();
							holder.buttonPlay.setVisibility(View.VISIBLE);
							holder.buttonPause.setVisibility(View.GONE);


						}
					});

				}
			}
				break;

			case IMAGEMODE:
			{
				holder.imageViewSlideMenuImage.setVisibility(View.VISIBLE);
				holder.imageViewSlideMenuImage.setImageBitmap(model.getBitmap());
			}
				break;

			case SCRIBBLEMODE:
			{
				holder.imageViewSlideMenuImage.setVisibility(View.VISIBLE);
				holder.imageViewSlideMenuImage.setImageBitmap(model.getBitmap());
			}
			break;

			case TEXTMODE:
			{
				holder.textViewSlideMenuName.setText("");
				holder.textViewSlideMenuName.setVisibility(View.VISIBLE);
				if (model.stringtext != null && model.stringtext.length() > 0) {
					holder.textViewSlideMenuName.setText(model.stringtext);
				} else {
				}
				holder.textViewSlideMenuName.addTextChangedListener(new TextWatcher()
				{
					@Override
					public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
					{
						final int position2 =(Integer) holder.textViewSlideMenuName.getTag();
						final EditText Caption =  holder.textViewSlideMenuName;
					}

					@Override
					public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
					{
						final int position2 = (Integer) holder.textViewSlideMenuName.getTag();
						final EditText Caption =holder.textViewSlideMenuName;
						//Log.d("On text Change","Position"+position2 +"text:"+Caption.getText());
						((NoteMainActivity)activity).didEditedTextField(arrNoteListData.get(position2), position2, Caption.getText().toString());
					}

					@Override
					public void afterTextChanged(Editable editable)
					{
						final int position2 =(Integer) holder.textViewSlideMenuName.getTag();
						final EditText Caption =holder.textViewSlideMenuName;
						//Log.d("After text Change","Position"+position2 +"text:"+Caption.getText());

						((NoteMainActivity)activity).didEditedTextField(arrNoteListData.get(position2), position2, Caption.getText().toString());

					}
				});

				holder.textViewSlideMenuName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {

					if (!hasFocus) {
							int itemIndex = (Integer)v.getTag();
							String textOnFocous = ((EditText) v).getText()
									.toString();
							Log.d("On focous Change", "Position" + itemIndex + "text:" + textOnFocous);
						}

					}
				});
			}
				break;

				default:
				break;

		}
		return vi;
	}

	public static class ViewHolder {

		public TextView textViewDuration,textViewcreationDate;
		public EditText textViewSlideMenuName;
		public ImageView imageViewSlideMenuImage;
		public RelativeLayout LayoutAudioRecording;
		public ImageButton buttonPlay,audioClose;
		public ImageButton buttonStop;
		public ImageButton buttonRecord, buttonPause, buttonRecordPause;
		public SeekBar progressRecord1;
		public MediaPlayer mediaPlayer;
		// public DrawingView viewScibble;

	}

	public static class ViewHolder1 {

		public TextView textViewusername;
		public ImageView imageViewUserImage;
		public TextView textViewUserbalance;

	}

	public static String getDurationBreakdown(long millis) {
		if (millis < 0) {
			throw new IllegalArgumentException(
					"Duration must be greater than zero!");
		}

		// long days = TimeUnit.MILLISECONDS.toDays(millis);
		// millis -= TimeUnit.DAYS.toMillis(days);
		// long hours = TimeUnit.MILLISECONDS.toHours(millis);
		// millis -= TimeUnit.HOURS.toMillis(hours);
		// long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		// millis -= TimeUnit.MINUTES.toMillis(minutes);
		//
		// long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

		// StringBuilder sb = new StringBuilder(64);

		StringBuffer sb = new StringBuffer();

		int hours = (int) (millis / (1000 * 60 * 60));
		int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
		int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

		sb.append(String.format("%02d", hours)).append(":")
				.append(String.format("%02d", minutes)).append(":")
				.append(String.format("%02d", seconds));

		System.out.println("time is:" + sb.toString());

		return sb.toString();

	}



}
