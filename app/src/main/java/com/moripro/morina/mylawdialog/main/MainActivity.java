package com.moripro.morina.mylawdialog.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import com.moripro.morina.mylawdialog.AlertDialog;
import com.moripro.morina.mylawdialog.R;
import com.moripro.morina.mylawdialog.SearchText;
import com.moripro.morina.mylawdialog.SetLawDialogFragment;
import com.moripro.morina.mylawdialog.SettingActivity;
import com.moripro.morina.mylawdialog.ViewPagerAdapter;
import com.moripro.morina.mylawdialog.preference.PrefManagement;
import com.moripro.morina.mylawdialog.preference.SettingsPref;
import com.moripro.morina.mylawdialog.viewmodel.ScrollYViewModel;
import com.moripro.morina.mylawdialog.viewmodel.TagViewModel;
import com.moripro.morina.mylawdialog.viewmodel.TextHighlightedViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private SearchText searchText;
    private AppUpdate appUpdate;
    private AlertDialog alertDialog = new AlertDialog();
    private ViewPagerAdapter adapter;
    private TagViewModel tagModel;
    private ScrollYViewModel scrollModel;
    private TextHighlightedViewModel highlightModel;
    private MainFragment mainFragment;
    private List<String> law_list;
    private TabLayout mTabLayout;
    private Toolbar mToolbar;
    private ViewPager2 mPager2;
    private FloatingActionButton fab;
    private ProgressBar progressBar;
    private SearchView searchView;
    private int searchResultIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        law_list = new ArrayList<String>();
        law_list = PrefManagement.createFirstList();

        tagModel = new ViewModelProvider(this).get(TagViewModel.class);
        scrollModel = new ViewModelProvider(this).get(ScrollYViewModel.class);
        highlightModel = new ViewModelProvider(this).get(TextHighlightedViewModel.class);

        mainFragment = new MainFragment(tagModel);
        searchText = new SearchText(tagModel, scrollModel);

        initView();
    }

    private void forOnClose(){
        fab.setVisibility(View.GONE);
        updateTabs(-1);
        highlightModel.setHighlitedOrNot(mPager2.getCurrentItem(), false);
        highlightModel.setSearchStarted(false);
        scrollModel.setScrolledToEnd(false);
        searchResultIndex = 0;
    }


    private void initView(){
        fab = findViewById(R.id.fab);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mPager2 = findViewById(R.id.mainPager);
        mTabLayout = findViewById(R.id.tabs);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        setMyWidget(mTabLayout, mPager2);
        progressBar = findViewById(R.id.filter_progressbar);
        searchView = findViewById(R.id.searchview);
        searchView.setOnSearchClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                fab.setVisibility(View.VISIBLE);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //ハイライトした次候補へ
                        if (highlightModel.getSearchStarted() && !scrollModel.getScrolledToEnd()) {
                            int actualResult = searchText.scrollToHighlightedWord(findTextView()
                                    , findScrollView()
                                    , searchResultIndex);
                            if (actualResult > searchResultIndex + 1) {
                                ++searchResultIndex;
                            }if (actualResult == searchResultIndex + 1) {
                                showToastAtMain("最後の候補です。");
                                scrollModel.setScrolledToEnd(true);
                            }
                        }else if (highlightModel.getSearchStarted() && scrollModel.getScrolledToEnd()){
                            forOnClose();
                        }else{
                            showToastAtMain("語句を入力し確定／検索ボタンを押してください。");
                        }
                    }
                });
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                forOnClose();
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                highlightModel.setSearchStarted(true);
                progressBar.setVisibility(View.VISIBLE);
                TextView tv = findTextView();
                if(tv != null) {
                    searchText.filterText(tv, query, MainActivity.this);
                    if(searchText.getListSize() == 0){
                        showToastAtMain("該当の語句が見つかりませんでした。");
                        progressBar.setVisibility(View.GONE);
                    }else {
                        highlightModel.setHighlitedOrNot(mPager2.getCurrentItem(), true);
                        progressBar.setVisibility(View.GONE);
                    }
                }else{
                    showToastAtMain(getString(R.string.text_not_found));
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        adapter = new ViewPagerAdapter(this, law_list, scrollModel);
        mPager2.setAdapter(adapter);
        setOnPageChangeCallback();

        new TabLayoutMediator(mTabLayout, mPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                String[] tabTitles = {"タブ１", "タブ２", "タブ３"};
                tab.setText(tabTitles[position]);
            }
        }
        ).attach();

        PrefManagement.setDefaltTab(MainActivity.this);
        SharedPreferences preferences = PrefManagement.getDownLoadedLawList(MainActivity.this);
        if(!preferences.contains("nihonkoku_ken_pou")) {
            alertDialog.showFirstUseDialog(MainActivity.this);
        }
        System.out.println("intitView() executed");

        appUpdate = new AppUpdate(MainActivity.this);
        appUpdate.checkForAppUpdate(1000);
    }


    private void setOnPageChangeCallback(){
        mPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(!searchView.isIconified()){
                    searchView.setIconified(true);
                }
                if(highlightModel.getHighlitedOrNot(position)){
                    updateTabs(position);  // for reset
                    highlightModel.setHighlitedOrNot(position, false);
                }
            }
        });
    }


    private TextView findTextView(){
        TextView tv = mPager2.findViewWithTag("tv_tab" + mPager2.getCurrentItem());
        return tv;
    }


    private ScrollView findScrollView(){
        ScrollView sv = mPager2.findViewWithTag("scv_tab" + mPager2.getCurrentItem());
        return sv;
    }


    private void setMyWidget(TabLayout tabLayout, ViewPager2 viewPager2){
        this.mTabLayout = tabLayout;
        this.mPager2 = viewPager2;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.set_law:
                showSetLawDialog();
                return true;
            case R.id.setting:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void showSetLawDialog(){
        FragmentManager fm = getSupportFragmentManager();
        SetLawDialogFragment srdFrag = SetLawDialogFragment.newInstance();
        srdFrag.show(fm, "setLawDialog");
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Log.i("ON_BACK_PRESSED", "pressed");
    }

    public void setTabTitle(final int position, final String key){
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                TabLayout.Tab tab = mTabLayout.getTabAt(position);

                SharedPreferences pref = PrefManagement.getTabNamePref(MainActivity.this);
                String title = pref.getString(key, "def");
                tab.setText(title);
            }
        });
        Log.i("SET_TAB_TITLE", "executed");
    }

    public void refreshTabsByDialog(final int position, String tag){
        tagModel.storeTagsState(tag, position);
        updateTabs(position);
    }

    public void updateTabs(final int position){
        runOnUiThread(new Runnable() {
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                adapter = new ViewPagerAdapter(MainActivity.this
                        , mainFragment.editLawList(MainActivity.this)
                        , scrollModel);
                mPager2.setAdapter(adapter);
                if(position > 0) {
                    adapter.notifyItemChanged(position);
                }
            }
        });
        scrollModel.storeScrollYState(0, position);
        mPager2.setCurrentItem(position);
        progressBar.setVisibility(View.GONE);
    }


    public void showToastAtMain(String text){
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(MainActivity.this, text, duration);
        toast.show();
    }


    @Override
    public void onResume(){
        super.onResume();
        if(!checkIfTextSizeIsSame()){
            updateTabs(-1);
        }
    }

    private boolean checkIfTextSizeIsSame(){
        TextView tv = findTextView();
        if(tv != null) {
            int currentSize = Math.round(tv.getTextSize());
            String setSize = SettingsPref.getTextPref(MainActivity.this);
            if (currentSize != Integer.parseInt(setSize)) {
                return false;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
}