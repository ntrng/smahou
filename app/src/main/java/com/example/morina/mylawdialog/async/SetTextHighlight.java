package com.example.morina.mylawdialog.async;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.Log;

import com.example.morina.mylawdialog.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class SetTextHighlight implements Callable<SpannableAndList> {

    private List<Integer> ofeList = new ArrayList<>();
    private String fulltext;
    private String textToHighlight;
    private  Context context;

    public SetTextHighlight(String fulltext, String textToHighlight, Context context){
        this.fulltext = fulltext;
        this.textToHighlight = textToHighlight;
        this.context = context;
    }

    public SpannableAndList call(){
        return setHighlightedText();
    }

    private SpannableAndList setHighlightedText(){
        int ofe = fulltext.indexOf(textToHighlight);
        Spannable wordToSpan = new SpannableString(fulltext);

        for(int ofs = 0; ofs < fulltext.length() && ofe != -1; ofs = ofe + 1){
            ofe = fulltext.indexOf(textToHighlight, ofs);
            ofeList.add(ofe);
            if(ofe == -1){
                Log.i("SET_HIGHLIGHTED_TEXT", "break");
                break;
            }else{
                wordToSpan.setSpan(new BackgroundColorSpan(context.getResources().getColor(R.color.to_spann))
                        , ofe, ofe + textToHighlight.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                //return wordToSpan;
            }
        }
        SpannableAndList spanAndList = new SpannableAndList(wordToSpan, ofeList);
        System.out.println("ofeList : " + ofeList);
        return spanAndList;
    }

}

