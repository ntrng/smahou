package com.example.morina.mylawdialog.async;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AsyncExecutor {


    public AsyncExecutor(){}

    public String makeGetTextRequest(String key){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> result = executor.submit(new GetTextFragment(key));
        try {
            String text = result.get();
            executor.shutdown();
            return text;
        }catch(InterruptedException ie){
            Log.e("make_get_text_request", "ie");
        }catch (ExecutionException ee){
            Log.e("make_get_text_request", "ee");
        }
        executor.shutdown();
        return null;
    }

    public SpannableAndList makeSpannableRequest(String fulltext, String textToHighlight, Context context){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<SpannableAndList> result = executor.submit(new SetTextHighlight(fulltext, textToHighlight, context));
        try {
            SpannableAndList spanAndList = result.get();
            executor.shutdown();
            return spanAndList;
        }catch(InterruptedException ie){
            Log.e("make_spannable_request", "ie");
        }catch (ExecutionException ee){
            Log.e("make_spannable_request", "ee");
        }
        executor.shutdown();
        return null;
    }

}
