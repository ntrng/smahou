package com.example.morina.mylawdialog.viewmodel;

import androidx.lifecycle.ViewModel;

public class TagViewModel extends ViewModel {

    private  String tab1_tag = "def";
    private String tab2_tag = "def";
    private String tab3_tag = "def";


    public void storeTagsState(String tag, int position){
        switch (position){
            case 0:
                this.tab1_tag = tag;
                break;
            case 1:
                this.tab2_tag = tag;
                break;
            case 2:
                this.tab3_tag = tag;
                break;
        }
    }

    public String getTagState(int position){
        switch (position){
            case 0:
                return tab1_tag;
            case 1:
                return tab2_tag;
            case 2:
                return tab3_tag;
                default:
                    return null;
        }
    }

    public String[] getCurrentAllTags(){
        String[] tags = new String[3];
        for(int i = 0; i < 3; i++){
            switch(i) {
                case 0: tags[i] = tab1_tag;
                break;
                case 1: tags[i] = tab2_tag;
                break;
                case 2: tags[i] = tab3_tag;
                break;
            }
        }
        return tags;
    }

    @Override
    public void onCleared(){
        super.onCleared();
        System.out.println("TagViewModel_onCleared");
    }
}
