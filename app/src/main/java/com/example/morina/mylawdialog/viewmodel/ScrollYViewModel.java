package com.example.morina.mylawdialog.viewmodel;

import androidx.lifecycle.ViewModel;

public class ScrollYViewModel extends ViewModel {

    private int tab1_scroll = 0;
    private int tab2_scroll = 0;
    private int tab3_scroll = 0;

    public void storeScrollYState(int y, int position){
        switch (position){
            case 0:
                this.tab1_scroll = y;
                System.out.println("scrollmodel :" + tab1_scroll + "," +  tab2_scroll + "," + tab3_scroll);
                break;
            case 1:
                this.tab2_scroll = y;
                System.out.println("scrollmodel :" + tab1_scroll + "," +  tab2_scroll + "," + tab3_scroll);
                break;
            case 2:
                this.tab3_scroll = y;
                System.out.println("scrollmodel :" + tab1_scroll + "," +  tab2_scroll + "," + tab3_scroll);
                break;
            case -1:
                break;
        }
    }


    public int getPreviousScrollY(int position) {
        switch (position) {
            case 0:
                return tab1_scroll;
            case 1:
                return tab2_scroll;
            case 2:
                return tab3_scroll;
            default:
                return 0;
        }
    }
}
