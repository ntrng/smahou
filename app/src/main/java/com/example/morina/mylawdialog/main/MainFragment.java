package com.example.morina.mylawdialog.main;

import android.content.Context;
import androidx.fragment.app.Fragment;
import com.example.morina.mylawdialog.async.AsyncExecutor;
import com.example.morina.mylawdialog.preference.PrefManagement;
import com.example.morina.mylawdialog.viewmodel.TagViewModel;
import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    /*
    create data to display in Main UI.
    law_list for displaying law text in each tab.
    tags for saving state of each tab and displaying for tab title.
     */

    private List<String> law_list = new ArrayList<>();
    private TagViewModel tagmodel;
    private AsyncExecutor asyncExecutor = new AsyncExecutor();


    public MainFragment(TagViewModel tagmodel){
        this.tagmodel = tagmodel;
    }

    public List<String> editLawList(Context context){
        law_list = PrefManagement.createFirstList();
        law_list = setTextToLawlist(context, tagmodel.getCurrentAllTags());
        return law_list;
    }


    private List<String> setTextToLawlist(Context context, String[] tags){
        for(int i = 0; i < 3; i++){
            law_list.set(i, (updateLawTextWithTag(i, tags[i], context)));
        }
        System.out.println(tags);
        return law_list;
    }

    private String updateLawTextWithTag(int position, String tag, Context context){
        String text = asyncExecutor.makeGetTextRequest(tag);
        return text;
    }
}
