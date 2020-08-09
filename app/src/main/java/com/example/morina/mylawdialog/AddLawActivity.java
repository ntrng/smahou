package com.example.morina.mylawdialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.morina.mylawdialog.preference.PrefManagement;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class AddLawActivity extends AppCompatActivity {

    PrefManagement prefManagement = new PrefManagement();
    Context context = App.getMcontext();


    @Override
    public void onCreate(Bundle savedInstanceState){
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_law);

        Toolbar mToolbar = findViewById(R.id.addlaw_toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        SharedPreferences preferences = context.getSharedPreferences("pref_dwnld_law", Context.MODE_PRIVATE);
        if(!preferences.contains("nihonkoku_ken_pou")){
            PrefManagement.createDownLoadedLawList(AddLawActivity.this);
        }

        ListView list_d = findViewById(R.id.list_downloadable);

        //set mapString and pref in Adapter.
        AddLawAdapter adapter = new AddLawAdapter();
        list_d.setAdapter(adapter);
    }

    @Override
    public void onBackPressed(){
        finish();
    }


    //--------------------------------------------------------------------------------------------------------------------------------
    public class ParseXmlTask extends AsyncTask<String, Integer, String> {

        private String id;


        public void setId(String id){    this.id = id;     }

        private int getResourceId(String pVariableName, String pResourceName, String pPackageName){
            try {
                return getResources().getIdentifier(pVariableName, pResourceName, pPackageName);
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }

        //--------execute---------

        @Override
        protected void onPreExecute(){
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, R.string.before_dwnld_toast, duration);
            toast.show();
        }

        @Override
        protected String doInBackground(String... did){
            String result = readXml(id);
            return result;
        }

        @Override
        protected void onPostExecute(String result){
            SharedPreferences preferences = PrefManagement.getDownLoadedLawList(AddLawActivity.this);
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
            Toast toast = Toast.makeText(context, R.string.after_dwnld_toast, duration);
            toast.show();

            runOnUiThread(new Runnable() {
                public void run() {
                    ListView list_d = findViewById(R.id.list_downloadable);
                    AddLawAdapter adapter = new AddLawAdapter();
                    list_d.setAdapter(adapter);
                }
            });
        }

        private String readXml(String id){
            int resId = getResourceId(id, "xml", getPackageName());
            StringBuffer buffer = new StringBuffer();
            int lineNum;

            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser parser = getResources().getXml(resId);

                int eventType = parser.getEventType();

                while(eventType != XmlPullParser.END_DOCUMENT) {
                    if(eventType == XmlPullParser.TEXT) {
                        lineNum = parser.getLineNumber();
                        if(getLineNth(lineNum) == true){
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

        private boolean getLineNth(int lineNum){
            return lineNum % 50 == 0;
        }
    }

//--------------------------------------------------------------------------------------------------------------------------------

    public class AddLawAdapter extends BaseAdapter {
        private AddLawMap addLawMap = new AddLawMap();
        private ParseXmlTask parseTask = new ParseXmlTask();
        private final ArrayList mapStr;


        public AddLawAdapter(){
            mapStr = new ArrayList();
            mapStr.addAll((addLawMap.getMapString()).entrySet());
        }

        @Override
        public int getCount(){    return mapStr.size();    }

        @Override
        public HashMap.Entry<String, String> getItem(int position){
            return (HashMap.Entry) mapStr.get(position);
        }

        @Override
        public long getItemId(int position){
            return 0;
        }

        @Override
        public View getView(int position, View convertView, final ViewGroup parent){
            final View result;

            if(convertView == null){
                result = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_law_item, parent, false);
            }else{
                result = convertView;
            }

            HashMap.Entry<String, String> itemId = getItem(position);
            TextView tv1 = result.findViewById(R.id.name_id);
            tv1.setText(itemId.getValue());

            final String prefKey = itemId.getKey();
            String trueOrFalse;
            SharedPreferences preferences = PrefManagement.getDownLoadedLawList(AddLawActivity.this);
            final boolean bool = preferences.getBoolean(prefKey, false);
            if(!bool){
                trueOrFalse = "未ダウンロード";

            }else{
                trueOrFalse = "ダウンロード済み";
            }
            TextView tv2 = result.findViewById(R.id.boolean_value);
            tv2.setText(trueOrFalse);

            result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if(!bool) {
                        parseTask.setId(prefKey);
                        parseTask.execute(prefKey);
                    }else{
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(AddLawActivity.this, R.string.already_dwnld_toast, duration);
                        toast.show();
                    }
                }
            });

            return result;
        }


    }


}