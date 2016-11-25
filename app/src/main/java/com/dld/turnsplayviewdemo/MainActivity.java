package com.dld.turnsplayviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;


import dld.com.turns.AutoCycleViewPager;
import dld.com.turns.adapter.TurnsPagerAdapter;
import dld.com.turns.indicator.PageIndicator;

public class MainActivity extends AppCompatActivity {

    private PageIndicator mPageIndicator;
    private CheckBox mScrollEnableCb;

    private AutoCycleViewPager mAutoCycleViewPager;


    private String[] imageUrls = new String[]{
        "http://pic34.nipic.com/20131022/12106323_115210747368_2.jpg",
        "http://img4.imgtn.bdimg.com/it/u=1765260951,797831689&fm=21&gp=0.jpg",
        "http://picyun.90sheji.com/design/00/03/78/94/s_1024_54efd5ac859cc.jpg"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setListener();
    }

    private void init() {

        mPageIndicator = (PageIndicator)findViewById(R.id.pageIndicator);
        mScrollEnableCb = (CheckBox)findViewById(R.id.isScrollEnable_cb);
        mAutoCycleViewPager = (AutoCycleViewPager) findViewById(R.id.autoCycleViewPager);

        mAutoCycleViewPager.setAdapter(new BasePagerAdapter());
        mPageIndicator.setViewPager(mAutoCycleViewPager);

    }

    public void onClicked(View view) {
        switch (view.getId()){
            case R.id.start_bt:
                mAutoCycleViewPager.startAutoCycle();
                break;
            case R.id.stop_bt:
                mAutoCycleViewPager.stopAutoCycle();
                break;
        }
    }

    class BasePagerAdapter extends TurnsPagerAdapter {

        @Override
        public int getItemCount() {
            return imageUrls.length;
        }

        @Override
        protected Object instantiateTurnsItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_advert, container, false);
            ImageView iv = (ImageView) view.findViewById(R.id.bg_iv);
            ImageLoader.load(MainActivity.this, imageUrls[position], iv);
            return view;
        }
    }


    private void setListener() {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
