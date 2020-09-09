package com.moripro.morina.mylawdialog;

public class AddLawUtil {

    public static boolean getLineNth(int lineNum){
        return lineNum % 50 == 0;
    }

    public static int getResourceId(String pVariableName, String pResourceName, String pPackageName){
        try {
            return App.getMcontext().getResources().getIdentifier(pVariableName, pResourceName, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
