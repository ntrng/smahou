package com.moripro.morina.mylawdialog;

import java.util.HashMap;

public class AddLawMap {

    private HashMap<String, String> mapString = new HashMap<>();

    public HashMap<String, String> getMapString(){
        mapString.put("nihonkoku_ken_pou", "日本国憲法");
        mapString.put("min_pou", "民法");
        mapString.put("kei_hou", "刑法");
        mapString.put("shou_hou", "商法");
        mapString.put("hudousan_touki_hou", "不動産登記法");
        mapString.put("chihou_jichi_hou", "地方自治法");
        mapString.put("minji_soshou_hou", "民事訴訟法");
        mapString.put("minji_shikkou_hou", "民事執行法");
        mapString.put("minji_hozen_hou", "民事保全法");
        mapString.put("shihoushoshi_hou", "司法書士法");
        mapString.put("kyoutaku_hou", "供託法");
        mapString.put("shougyou_touki_hou", "商業登記法");
        mapString.put("shougyou_touki_kisoku", "商業登記規則");
        mapString.put("hudousan_touki_rei", "不動登記令");
        mapString.put("hudousan_touki_kisoku", "不動産登記規則");

        return mapString;
    }
}
