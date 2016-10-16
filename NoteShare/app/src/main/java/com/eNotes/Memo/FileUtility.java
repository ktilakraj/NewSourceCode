package com.eNotes.Memo;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Random;

/**
 * Created by Tilak on 10/5/16.
 */
public class FileUtility
{

    static String ROOTKEY="quickmemo";
    static String ELEMENTKEY="memo";
    static String ELEMENTTITLEKEY="memotitle";
    static String FILENAME="memo.txt";
    static  int memoselectedindex=-1;
    public static  String checkListfilename="";
    public static String FILE_CheckList="checklistmain.txt";
    public static String CHECKLIST_ROOTKEY="checklist";

    public static String Element_title="title";
    public static String Element_isCheck="isCheck";
    public static String Element_content ="content";

    public static String readFromFile(Context context,String FILENAME) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(FILENAME);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
                Log.d("The File Out Put:",""+ret);
            }
        }
        catch (Exception e) {

            Log.e("login activity", "File not found: " + e.toString());
        }

        return ret;
    }

    public static void writeToFile(String data,Context context,String FILENAME) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(FILENAME, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (Exception e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    public static String randomUniqueNumberGenerator() {

        final int NUMBER_RANGE = 1000000;
        Random random = new Random();
        Integer numberis = random.nextInt(NUMBER_RANGE);
        return ""+numberis;
    }

}
