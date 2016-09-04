package com.eNotes.Drawing;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eNotes.dataAccess.DataManager;
import com.eNotes.notesharedatabase.DBNoteItems;
import com.eNotes.notesharedatabase.NoteshareDatabaseHelper;
import com.mobiapp.ventures.eNotes.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewDrawingActivity extends AppCompatActivity {


    private CanvasView canvas = null;
    private RelativeLayout layoutStroke,mainHeadermenue;
    private LinearLayout layoutColor;
    private SeekBar strokeSeekbar;
    private LinearLayout strokeView;
    TextView textView;
    private int progress = 5;
    private int colorSelected = Color.BLACK;
    public DBNoteItems selectedDBNoteItem;
    public NoteshareDatabaseHelper androidOpenDbHelperObj;
    SQLiteDatabase sqliteDatabase;
    static  String imageFilePath="/eNote/Images/";
    static  String imageFolderPath="/eNote/Images";

    Button btnNew,btnUndo,btnRedo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.newdrawing_main);
        DataManager.sharedDataManager().setUpUserPermission(this,2);//READ
        mainHeadermenue=(RelativeLayout) findViewById(R.id.mainHeadermenue);
        textView=(TextView) mainHeadermenue.findViewById(R.id.textView);
        textView.setText("Drawing");
        this.canvas = (CanvasView)findViewById(R.id.canvas);
        layoutStroke=(RelativeLayout) findViewById(R.id.layoutStroke);
        layoutColor=(LinearLayout) findViewById(R.id.layoutColor);
        strokeSeekbar=(SeekBar)layoutStroke.findViewById(R.id.strokeSeekBareekBar);
        strokeView=(LinearLayout)layoutStroke.findViewById(R.id.viewCircle);
        strokeSeekbar.setProgress(progress);
        strokeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                progress=i;
                updateViewOnSeekProgress();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if (layoutStroke.getVisibility() == View.VISIBLE) {
                    slideToTop(layoutStroke);
                }
            }
        });

        btnNew = (Button) findViewById(R.id.newDraw);
        btnRedo = (Button) findViewById(R.id.redo);
        btnUndo = (Button) findViewById(R.id.undo);

        setupcolorLayout();

        updateViewOnSeekProgress();
        addColorToCircle();
        databaseInitlization();

        if (canvas.isCanvasDrawn == false) {

            btnNew.setVisibility(View.GONE);
            btnUndo.setVisibility(View.GONE);
            btnRedo.setVisibility(View.GONE);
        }


        canvas.setOnTouchCanvas(new CanvasView.OnTouchCanvas() {
            @Override
            public void onDrawCanvasByUser(boolean drawn) {
                btnNew.setVisibility(View.VISIBLE);
                btnUndo.setVisibility(View.VISIBLE);
                btnRedo.setVisibility(View.VISIBLE);
            }
        });


    }



    void databaseInitlization()
    {
        try
        {
            androidOpenDbHelperObj = new NoteshareDatabaseHelper(NewDrawingActivity.this);
            sqliteDatabase = androidOpenDbHelperObj.getWritableDatabase();
            selectedDBNoteItem = DataManager.sharedDataManager().getSeletedDBNoteItem();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    void setupcolorLayout() {

        NewColorPickerView viewColorpicker =new NewColorPickerView(NewDrawingActivity.this, new NewColorPickerView.OnColorChangedListener() {
            @Override
            public void colorChanged(int color) {

                colorSelected=color;
                canvas.setPaintStrokeColor(colorSelected);
                if (layoutColor.getVisibility() == View.VISIBLE) {
                    slideToTop(layoutColor);
                }
                addColorToCircle();

            }
        },canvas.getPaintStrokeColor());

        layoutColor.addView(viewColorpicker, 1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));

        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewSep = vi.inflate(R.layout.seprator_layout, null);
        layoutColor.addView(viewSep, 2, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
    }
    void  updateViewOnSeekProgress() {

        RelativeLayout.LayoutParams   params = (RelativeLayout.LayoutParams) strokeView.getLayoutParams();
        params.height=progress+5;
        params.width=progress+5;
        strokeView.setLayoutParams(params);
        canvas.setPaintStrokeWidth(progress);
    }

    void addColorToCircle() {


        GradientDrawable backgroundGradient = (GradientDrawable)strokeView.getBackground();
        //backgroundGradient.setColor(getResources().getColor(R.color.colorPrimaryDark));
        backgroundGradient.setColor(colorSelected);

    }


    public void optionsSelected(View v) {

        canvas.setVisibility(View.VISIBLE);
        switch (v.getId()) {
            case R.id.saveImage :
            {
                layoutStroke.setVisibility(View.GONE);

                if (canvas.isCanvasDrawn) {
                    boolean status = storeImage(canvas.getBitmap());
                    if (status) {
                        canvas.isCanvasDrawn=false;
                        canvas.clear();
                        Toast.makeText(NewDrawingActivity.this, "Image saved successfully", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(NewDrawingActivity.this, "Please draw on canvas", Toast.LENGTH_SHORT).show();
                }

            }
            break;

            case R.id.undo: {
                //customCanvas.clearCanvas();
                Log.v("the option","clear"); //Undo
                canvas.undo();
                if (layoutStroke.getVisibility() == View.VISIBLE) {
                    slideToTop(layoutStroke);
                }
                if (layoutColor.getVisibility() == View.VISIBLE) {
                    slideToTop(layoutColor);
                }
            }
            break;
            case R.id.redo: {
                Log.v("the option","save"); //redo
                canvas.redo();
                if (layoutStroke.getVisibility() == View.VISIBLE) {
                    slideToTop(layoutStroke);
                }
                if (layoutColor.getVisibility() == View.VISIBLE) {
                    slideToTop(layoutColor);
                }

            }
            break;
            case R.id.newDraw: {

                canvas.clear();
                canvas.setPaintStrokeColor(colorSelected);
                canvas.setPaintStrokeWidth(progress);
                canvas.isCanvasDrawn=false;
                Log.v("the option","new draw");
                if (layoutStroke.getVisibility() == View.VISIBLE) {
                    slideToTop(layoutStroke);
                }
                if (layoutColor.getVisibility() == View.VISIBLE) {
                    slideToTop(layoutColor);
                }
            }
            break;
            case R.id.stroke: {
                if (layoutColor.getVisibility() == View.VISIBLE) {
                    slideToTop(layoutColor);
                }
                Log.v("the option","stroke");
                if (layoutStroke.getVisibility() == View.VISIBLE) {
                    slideToTop(layoutStroke);
                } else {
                    slideToBottom(layoutStroke);
                }
            }
            break;
            case R.id.color: {

                Log.v("the option","color");

                if (layoutStroke.getVisibility() == View.VISIBLE) {
                    slideToTop(layoutStroke);
                }

                   /* colorPickerDialog =  new ColorPickerDialog(NewDrawingActivity.this, new ColorPickerDialog.OnColorChangedListener() {
                      @Override
                      public void colorChanged(int color) {
                          colorSelected=color;
                          colorPickerDialog.dismiss();
                      }
                  },canvas.getPaintStrokeColor());

                colorPickerDialog.show();

                colorPickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {

                        canvas.setPaintStrokeColor(colorSelected);
                        addColorToCircle();
                    }
                });*/

                if (layoutColor.getVisibility() == View.VISIBLE) {
                    slideToTop(layoutColor);
                } else {
                    slideToBottom(layoutColor);
                }
            }
            break;
        }

    }

    // To animate view slide out from bottom to top
    public void slideToTop(View view){
        TranslateAnimation animate = new TranslateAnimation(0,0,0,-view.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);
        //view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }
    // To animate view slide out from top to bottom
    public void slideToBottom(View view){
        TranslateAnimation animate = new TranslateAnimation(0,0,0,0);
        animate.setDuration(1000);
        animate.setFillAfter(true);
       // view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
    }
    // To animate view slide out from left to right
    public void slideToRight(View view){
        TranslateAnimation animate = new TranslateAnimation(0,view.getWidth(),0,0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }
    // To animate view slide out from right to left
    public void slideToLeft(View view){
        TranslateAnimation animate = new TranslateAnimation(0,-view.getWidth(),0,0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }


    private boolean storeImage(Bitmap imageData) {
        //get path to external storage (SD card)

        if (imageData == null) {
            return false;
        }
        String filename;

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        filename =  sdf.format(date);
        String appendPath= filename+".jpg";

       // String iconsStoragePath = Environment.getExternalStorageDirectory() + "/eNotes/Images";
        String iconsStoragePath = Environment.getExternalStorageDirectory() + imageFolderPath;

        File sdIconStorageDir = new File(iconsStoragePath);

        //create storage directories, if they don't exist
        sdIconStorageDir.mkdirs();

        try {
            String filePath = sdIconStorageDir.toString() + "/"+appendPath;
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);

            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
            //choose another format if PNG doesn't suit you
            imageData.compress(Bitmap.CompressFormat.JPEG, 100, bos);

            bos.flush();
            bos.close();

            androidOpenDbHelperObj.noteElementdatabaseInsertion(selectedDBNoteItem.getNote_Id(), appendPath, "DRAWING", "", "");

        }  catch (Exception e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        if (canvas.isCanvasDrawn) {

            showAlertWithDeleteMessage("Do want to save this drawing?",NewDrawingActivity.this);

        } else {

            finish();
        }

    }


    void showAlertWithDeleteMessage(String message, Context context) {

        final Dialog dialogdelete = new Dialog(NewDrawingActivity.this);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.alert_view, null, false);

        TextView textViewTitleAlert = (TextView) contentView
                .findViewById(R.id.textViewTitleAlert);
        textViewTitleAlert.setText("Save");
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
                finish();

            }
        });
        buttonAlertOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                boolean status = storeImage(canvas.getBitmap());
                if (status) {
                    Toast.makeText(NewDrawingActivity.this, "Image saved successfully", Toast.LENGTH_SHORT).show();
                }
                dialogdelete.dismiss();
                finish();

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
