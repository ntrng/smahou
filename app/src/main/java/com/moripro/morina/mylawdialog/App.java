package com.moripro.morina.mylawdialog;

import android.app.Application;
import android.content.Context;


public class App extends Application {

    private static Application instance;

    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;
    }

    public static Context getMcontext(){    return instance.getApplicationContext();    }

}
