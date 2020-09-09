package com.moripro.morina.mylawdialog.preference;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class PrefManagement {

    public static void setDefaltTab(Context context){
        createTabNamePref(context);
        SettingsPref.setDefaultPref(context);
    }

    public static List<String> createFirstList(){
        List<String> law_list = new ArrayList<String>();

        if(law_list.size() == 0){
            law_list.add(0, "タブに法令をセットしてください");
            law_list.add(1, "タブに法令をセットしてください");
            law_list.add(2, "タブに法令をセットしてください");
        }
        return law_list;
    }

    public static void createDownLoadedLawList(Context context){
        SharedPreferences preferences = context.getSharedPreferences("pref_dwnld_law", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("nihonkoku_ken_pou", false);
        editor.putBoolean("min_pou", false);
        editor.putBoolean("kei_hou", false);
        editor.putBoolean("shou_hou", false);
        editor.putBoolean("hudousan_touki_hou", false);
        editor.putBoolean("chihou_jichi_hou", false);
        editor.putBoolean("minji_soshou_hou", false);
        editor.putBoolean("minji_shikkou_hou", false);
        editor.putBoolean("minji_hozen_hou", false);
        editor.putBoolean("shihoushoshi_hou", false);
        editor.putBoolean("kyoutaku_hou", false);
        editor.putBoolean("shougyou_touki_hou", false);
        editor.putBoolean("shougyou_touki_kisoku", false);
        editor.putBoolean("hudousan_touki_rei", false);
        editor.putBoolean("husousan_touki_kisoku", false);
        editor.commit();
    }

    private static void createTabNamePref(Context context) {
        SharedPreferences pref_tab = context.getSharedPreferences("pref_tab_name", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref_tab.edit();
        editor.putString("nihonkoku_ken_pou", "憲法");
        editor.putString("min_pou", "民法");
        editor.putString("kei_hou", "刑法");
        editor.putString("shou_hou", "商法");
        editor.putString("hudousan_touki_hou", "不登法");
        editor.putString("chihou_jichi_hou", "地自法");
        editor.putString("minji_soshou_hou", "民訴法");
        editor.putString("minji_shikkou_hou", "民執法");
        editor.putString("minji_hozen_hou", "民保法");
        editor.putString("shihoushoshi_hou", "司士法");
        editor.putString("kyoutaku_hou", "供託法");
        editor.putString("shougyou_touki_hou", "商登法");
        editor.putString("shougyou_touki_kisoku", "商登規");
        editor.putString("hudousan_touki_rei", "不登令");
        editor.putString("husousan_touki_kisoku", "不登規");
        editor.commit();
    }

    public static SharedPreferences getTabNamePref(Context context){
        return context.getSharedPreferences("pref_tab_name", Context.MODE_PRIVATE);
    }

    public static  SharedPreferences getDownLoadedLawList(Context context){
        return context.getSharedPreferences("pref_dwnld_law", Context.MODE_PRIVATE);
    }
}
