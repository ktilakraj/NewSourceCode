package com.eNotes.noteshare;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eNotes.dataAccess.DataManager;
import com.eNotes.notesharedatabase.DBNoteItemElement;
import com.eNotes.notesharedatabase.DBNoteItems;
import com.eNotes.notesharedatabase.NoteshareDatabaseHelper;
import com.mobiapp.ventures.eNotes.R;

import java.util.ArrayList;

import static com.eNotes.noteshare.NoteMainActivity.notetypeText;

public class TextEditorActivity extends Activity {

    public NoteshareDatabaseHelper androidOpenDbHelperObj;
    SQLiteDatabase sqliteDatabase;
    public DBNoteItems selectedDBNoteItem;
    public EditText noteEditor;
    public  DBNoteItemElement lastdbItem;
    RelativeLayout mainHeadermenue;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_editor);
      noteEditor =(EditText) findViewById(R.id.noteEditor);
        mainHeadermenue=(RelativeLayout) findViewById(R.id.mainHeadermenue);
        textView=(TextView) mainHeadermenue.findViewById(R.id.textView);
        textView.setText("Text");

        initlizeLoadingData();

        noteEditor.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        noteEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                androidOpenDbHelperObj.updateNoteElementText(lastdbItem.getNOTE_ELEMENT_ID(), noteEditor.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

                androidOpenDbHelperObj.updateNoteElementText(lastdbItem.getNOTE_ELEMENT_ID(), noteEditor.getText().toString());

            }
        });

        noteEditor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {

                    int itemIndex = (Integer)v.getTag();

                    String enteredPrice = ((EditText) v).getText()
                            .toString();
                }

            }
        });

    }

   void initlizeLoadingData()
    {

        selectedDBNoteItem = DataManager.sharedDataManager().getSeletedDBNoteItem();
        databaseInitlization();
        androidOpenDbHelperObj.noteElementdatabaseInsertion(selectedDBNoteItem.getNote_Id(),"",notetypeText,"","");
        ArrayList<DBNoteItemElement> lastdbItems=androidOpenDbHelperObj.getLastNotesElementWithNote_Id(selectedDBNoteItem.getNote_Id());
        lastdbItem =lastdbItems.get(0);
    }
    void databaseInitlization()
    {
        // Data base initilization

        try
        {
            androidOpenDbHelperObj = new NoteshareDatabaseHelper(getApplicationContext());
            sqliteDatabase = androidOpenDbHelperObj.getWritableDatabase();


        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {

        finish();
    }
}
