package com.moripro.morina.mylawdialog.async;

import android.text.Spannable;

import java.util.List;

public class SpannableAndList {

    private final Spannable span;
    private final List<Integer> list;

    public SpannableAndList(Spannable span, List<Integer> list){
        this.span = span;
        this.list = list;
    }

    public Spannable getSpan(){  return span;  }

    public List<Integer> getList(){  return list;  }
}
