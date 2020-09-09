package com.moripro.morina.mylawdialog;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.moripro.morina.mylawdialog.async.ParseXmlTask;
import com.moripro.morina.mylawdialog.preference.PrefManagement;
import java.util.ArrayList;
import java.util.HashMap;

public class AddLawAdapter extends BaseAdapter {

    private AddLawMap addLawMap = new AddLawMap();
    private ParseXmlTask parseTask = new ParseXmlTask();
    private final ArrayList mapStr;
    private ParseXmlTask.AddLawCallback mCallback;


    public AddLawAdapter(ParseXmlTask.AddLawCallback cb){
        mCallback = cb;
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
        SharedPreferences preferences = PrefManagement.getDownLoadedLawList(App.getMcontext());
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
                    parseTask.setCallback(mCallback);
                    parseTask.execute(prefKey);
                }else{
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(App.getMcontext(), R.string.already_dwnld_toast, duration);
                    toast.show();
                }
            }
        });

        return result;
    }
}
