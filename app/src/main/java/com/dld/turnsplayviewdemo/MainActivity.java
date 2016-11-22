package com.dld.turnsplayviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;


import dld.com.turns.AutoCycleViewPager;
import dld.com.turns.CycleViewPager;
import dld.com.turns.adapter.TurnsPagerAdapter;
import dld.com.turns.indicator.PageIndicator;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";

    private AutoCycleViewPager mCycleViewPager;
    private PageIndicator mPageIndicator;
    private CheckBox mScrollEnableCb;

    private String[] imageUrls = new String[]{
        "http://d.hiphotos.baidu.com/zhidao/pic/item/4ec2d5628535e5dd5c955af875c6a7efce1b6258.jpg",
        "http://pic1a.nipic.com/2008-11-14/2008111411513285_2.jpg",
        "http://f.hiphotos.baidu.com/lvpics/h=800/sign=fc26b1af912bd4075dc7defd4b889e9c/b21c8701a18b87d6d27d8498010828381f30fd7e.jpg"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCycleViewPager = (AutoCycleViewPager)findViewById(R.id.viewPager);
        mPageIndicator = (PageIndicator)findViewById(R.id.pageIndicator);
        mScrollEnableCb = (CheckBox)findViewById(R.id.isScrollEnable_cb);
        mCycleViewPager.setAdapter(new BasePagerAdapter());
        mPageIndicator.setViewPager(mCycleViewPager);

        mCycleViewPager.setOnItemClickListener(new CycleViewPager.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                Toast.makeText(MainActivity.this, position + "", Toast.LENGTH_SHORT).show();
            }
        });
        mCycleViewPager.startAutoCycle();

        setListener();
    }

    private void setListener() {

        mScrollEnableCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPageIndicator.setIndicatorIsScroll(isChecked);
            }
        });
    }

    class BasePagerAdapter extends TurnsPagerAdapter {

        private int count;

        @Override
        public int getItemCount() {
            return imageUrls.length;
        }

        @Override
        protected Object instantiateItem(int position) {
            ImageView iv = new ImageView(MainActivity.this);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageLoader.load(MainActivity.this, imageUrls[position], iv);
            Log.d(TAG, String.format("count:%d", ++count));
            return iv;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mCycleViewPager.startAutoCycle();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCycleViewPager.stopAutoCycle();
    }

}
