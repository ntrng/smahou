package com.example.morina.mylawdialog;

import android.app.Application;
import android.content.Context;


public class App extends Application {

    private static Application instance;

    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;
        registerActivityLifecycleCallbacks(new ActivityLifeCycle());
    }

    public static Context getMcontext(){    return instance.getApplicationContext();    }

}
