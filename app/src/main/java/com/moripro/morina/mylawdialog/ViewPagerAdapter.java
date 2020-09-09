package com.moripro.morina.mylawdialog;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.moripro.morina.mylawdialog.preference.SettingsPref;
import com.moripro.morina.mylawdialog.viewmodel.ScrollYViewModel;
import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<String> mText;
    private ScrollYViewModel scrollmodel;


    public ViewPagerAdapter(Context context, List<String> data, ScrollYViewModel scrollmodel){
        this.mInflater = LayoutInflater.from(context);
        this.mText = data;
        this.scrollmodel = scrollmodel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.tab_scroll_item, parent, false);
        return new ViewPagerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewPagerAdapter.ViewHolder holder, final int position){
        holder.scrollView.setTag("scv_tab" + position);
        holder.scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY != 0) {
                    scrollmodel.storeScrollYState(scrollY, position);
                }
                System.out.println("onScrollChanged : " + scrollY);
            }
        });

        holder.textView.setEnabled(false);
        holder.textView.setEnabled(true);
        holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, applyTextSize());
        holder.textView.setText(mText.get(position));
        holder.textView.setTag("tv_tab" + position);

        holder.scrollView.post(new Runnable() {
            @Override
            public void run() {
                int y = scrollmodel.getPreviousScrollY(position);
                holder.scrollView.scrollTo(0, y);
            }
        });
    }

    @Override
    public int getItemCount(){
        return mText.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ScrollView scrollView;
        TextView textView;

        public ViewHolder(View itemView){
            super(itemView);
            scrollView = itemView.findViewById(R.id.tab_scroll);
            textView = itemView.findViewById(R.id.tab_textview);
        }
    }

    private int applyTextSize(){
        String sizeString = SettingsPref.getTextPref(App.getMcontext());
        return Integer.parseInt(sizeString);
    }
}
