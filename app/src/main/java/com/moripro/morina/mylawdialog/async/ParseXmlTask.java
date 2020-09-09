package com.moripro.morina.mylawdialog.async;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import com.moripro.morina.mylawdialog.App;
import com.moripro.morina.mylawdialog.R;
import com.moripro.morina.mylawdialog.preference.PrefManagement;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import static com.moripro.morina.util.AddLawUtil.getLineNth;
import static com.moripro.morina.util.AddLawUtil.getResourceId;


public class ParseXmlTask extends AsyncTask<String, Integer, String> {

    private String id;
    private AddLawCallback mCallback;

    public void setId(String id){    this.id = id;     }
    public void setCallback(AddLawCallback cb){     mCallback = cb;    }

    @Override
    protected void onPreExecute(){
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(App.getMcontext(), R.string.before_dwnld_toast, duration);
        toast.show();
    }

    @Override
    protected String doInBackground(String... did){
        String result = readXml(id);
        return result;
    }

    @Override
    protected void onPostExecute(String result){
        SharedPreferences preferences = PrefManagement.getDownLoadedLawList(App.getMcontext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(id, true);
        editor.commit();

        String fileName = id ;
        try{
            FileOutputStream fos = App.getMcontext().openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(result.getBytes());
            fos.close();
        }catch(IOException e){
            e.printStackTrace();
        }

        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(App.getMcontext(), R.string.after_dwnld_toast, duration);
        toast.show();

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                mCallback.refreshList();
            }
        });
    }

    private String readXml(String id){
        int resId = getResourceId(id, "xml", App.getMcontext().getPackageName());
        StringBuffer buffer = new StringBuffer();
        int lineNum;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = App.getMcontext().getResources().getXml(resId);

            int eventType = parser.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.TEXT) {
                    lineNum = parser.getLineNumber();
                    if(getLineNth(lineNum)){
                        Log.i("LINE_NUM", lineNum + "行");
                    }
                    buffer.append("\n" + parser.getText());
                }
                eventType = parser.next();
            }
        }catch(XmlPullParserException e){
            e.printStackTrace();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return buffer.toString();
    }

    public interface AddLawCallback {

        void refreshList();
    }

}