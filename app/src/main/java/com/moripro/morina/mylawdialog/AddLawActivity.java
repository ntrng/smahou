package com.moripro.morina.mylawdialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.moripro.morina.mylawdialog.async.ParseXmlTask;
import com.moripro.morina.mylawdialog.preference.PrefManagement;


public class AddLawActivity extends AppCompatActivity implements ParseXmlTask.AddLawCallback {


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

        SharedPreferences preferences = App.getMcontext().getSharedPreferences("pref_dwnld_law", Context.MODE_PRIVATE);
        if(!preferences.contains("nihonkoku_ken_pou")){
            PrefManagement.createDownLoadedLawList(AddLawActivity.this);
        }
        refreshList();
    }

    @Override
    public void onBackPressed(){
        finish();
    }


    @Override
    public void refreshList(){
        ListView list_d = findViewById(R.id.list_downloadable);

        //set mapString and pref in Adapter.
        AddLawAdapter adapter = new AddLawAdapter(this);
        list_d.setAdapter(adapter);
    }

}