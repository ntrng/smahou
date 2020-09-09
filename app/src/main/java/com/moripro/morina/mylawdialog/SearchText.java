package com.moripro.morina.mylawdialog;

import android.content.Context;
import android.text.Spannable;
import android.widget.ScrollView;
import android.widget.TextView;
import com.moripro.morina.mylawdialog.async.AsyncExecutor;
import com.moripro.morina.mylawdialog.async.SpannableAndList;
import com.moripro.morina.mylawdialog.viewmodel.ScrollYViewModel;
import com.moripro.morina.mylawdialog.viewmodel.TagViewModel;
import java.util.ArrayList;
import java.util.List;

public class SearchText {

    private List<Integer> lineList = new ArrayList<>();
    private TextView textView;
    private TagViewModel tagmodel;
    private ScrollYViewModel scrollmodel;
    private AsyncExecutor asyncExecutor;

    public SearchText(TagViewModel tagmodel, ScrollYViewModel scrollmodel){
        this.tagmodel = tagmodel;
        this.scrollmodel = scrollmodel;
        this.asyncExecutor = new AsyncExecutor();
    }


    public void filterText(TextView tv, String textToHighlight, Context context){
        textView = tv;
        SpannableAndList spanAndList = asyncExecutor.makeSpannableRequest(tv.getText().toString(), textToHighlight, context);

        Spannable span = spanAndList.getSpan();
        tv.setText(span, TextView.BufferType.SPANNABLE);
        List<Integer> ofeList = spanAndList.getList();
        convertOfeToLine(ofeList);
    }


    private void convertOfeToLine(List<Integer> ofeList){
        int line;
        for(int i = 0; i < ofeList.size(); i++ ){
            line = textView.getLayout().getLineForOffset(ofeList.get(i));
            lineList.add(i, line);
        }
    }


    public int scrollToHighlightedWord(final TextView tv, final ScrollView sv, final int i){
        sv.post(new Runnable() {
            @Override
            public void run() {
                int z = lineList.get(i);
                int y = tv.getLayout().getLineTop(z);
                sv.scrollTo(0, y);
            }
        });
        return lineList.size();
    }


    public void deleteTextHighlight(int position, ScrollView sv){
        lineList.clear();
        if(textView != null) {
            textView.setText(getOriginalText(position), TextView.BufferType.NORMAL);
            int y = scrollmodel.getPreviousScrollY(position);
            sv.scrollTo(0, y);
        }
        textView = null;
    }


    private String getOriginalText(int position){
        String text;
        String tag;

        tag = tagmodel.getTagState(position);
        text = asyncExecutor.makeGetTextRequest(tag);
        return text;
    }


    public int getListSize(){
        return lineList.size();
    }
}
