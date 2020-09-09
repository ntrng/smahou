package com.moripro.morina.mylawdialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import com.moripro.morina.mylawdialog.main.MainActivity;
import java.util.ArrayList;
import java.util.Map;


public class SetLawDialogFragment extends DialogFragment {

    private int tab_selected;
    private RadioGroup tab_group;

    private SetLawDialogFragment(){ }

    public static SetLawDialogFragment newInstance(){
        SetLawDialogFragment fragment = new SetLawDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", "setLaw");
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.dialog_set_law, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        tab_group = view.findViewById(R.id.tab_group);
        tab_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_tab1:
                        tab_selected = 0;
                        break;
                    case R.id.radio_tab2:
                        tab_selected = 1;
                        break;
                    case R.id.radio_tab3:
                        tab_selected = 2;
                        break;
                }
            }
        });

        final SharedPreferences preferences = App.getMcontext().getSharedPreferences("pref_dwnld_law", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = preferences.getAll();
        final ArrayList<String> listBoolean = createListBoolean(allEntries);

        ListView home_list = view.findViewById(R.id.list_downloaded);
        HomeListAdapter adapter = new HomeListAdapter(listBoolean);
        home_list.setAdapter(adapter);

    }

    private ArrayList<String> createListBoolean( Map<String, ?> allEntries){
        ArrayList<String> listBool = new ArrayList<>();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if ((entry.getValue().toString()) == "true") {
                listBool.add(entry.getKey());
            }
        }
        return  listBool;
    }

    private boolean isTabSelected(){
        return tab_group.getCheckedRadioButtonId() != -1;
    }


    public class HomeListAdapter extends BaseAdapter {
        AddLawMap addLawMap = new AddLawMap();
        private final ArrayList<String> list;
        private MainActivity mainActivity = (MainActivity) getActivity();
        private int selectedPosition = -1;
        private int mapSize;


        public HomeListAdapter(ArrayList<String> listBoolean){
            list = new ArrayList<>();
            list.addAll(listBoolean);
        }

        @Override
        public int getCount(){    return list.size();    }

        @Override
        public String getItem(int position){
            return list.get(position);
        }

        @Override
        public long getItemId(int position){
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            final View result;

            if(convertView == null){
                result = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_set_law_item, parent, false);
            }else{
                result = convertView;
            }

            if(getCount() != 0) {
                mapSize = addLawMap.getMapString().size();

                String itemId = getItem(position);
                final TextView tv1 = result.findViewById(R.id.text_home_item);
                tv1.setTag(itemId);
                tv1.setText(addLawMap.getMapString().get(itemId));

                if(selectedPosition == position && isTabSelected()){
                    tv1.setBackgroundColor(Color.LTGRAY);
                    if(selectedPosition != position){
                        tv1.setBackgroundColor(Color.WHITE);
                    }
                    notifyDataSetChanged();
                }

                result.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        if (isTabSelected()) {
                            result.setSelected(true);
                            selectedPosition = position;

                            final String tag = tv1.getTag().toString();
                            mainActivity.setTabTitle(tab_selected, tag);
                            mainActivity.refreshTabsByDialog(tab_selected, tag);
                            getDialog().dismiss();
                        }else{
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(App.getMcontext(), R.string.toase_tab_not_selected, duration);
                            toast.show();
                        }
                    }
                });
                return result;

            }else {
                return result;
            }
        }
    }

}
