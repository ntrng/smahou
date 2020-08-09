package com.example.morina.mylawdialog.viewmodel;

import androidx.lifecycle.ViewModel;

public class TextHighlightedViewModel extends ViewModel {

    private boolean tab1_highlighted = false;
    private boolean tab2_highlighted = false;
    private boolean tab3_highlighted = false;

    public void setHighlitedOrNot(int position, boolean bool){
        switch (position){
            case 0:
                this.tab1_highlighted = bool;
                break;
            case 1:
                this.tab2_highlighted = bool;
                break;
            case 2:
                this.tab3_highlighted = bool;
                break;
        }
    }

    public boolean getHighlitedOrNot(int position){
        switch (position){
            case 0:
                return tab1_highlighted;
            case 1:
                return tab2_highlighted;
            case 2:
                return tab3_highlighted;
                default:
                    return false;
        }
    }
}
