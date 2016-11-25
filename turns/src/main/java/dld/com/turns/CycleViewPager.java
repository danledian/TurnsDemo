package dld.com.turns;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import java.lang.reflect.Field;

import dld.com.turns.adapter.TurnsPagerAdapter;
import dld.com.turns.scroller.CycleViewScroller;

/**
 * Created by song on 2016/11/18.
 */

public class CycleViewPager extends ViewPager {

    public interface OnCycleViewPageChangeListener {

        void onTurnsPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onTurnsPageSelected(int position);

        void onTurnsPageScrollStateChanged(int state);
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position, long id);
    }


    private CycleViewScroller mScroller;
    private OnCycleViewPageChangeListener mOnCycleViewPageChangeListener;
    private OnItemClickListener mOnItemClickListener;
    private int count;

    public CycleViewPager(Context context) {
        this(context, null);
    }

    public CycleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        initViewPagerScroll();
        super.addOnPageChangeListener(new OnPageChangeListener());
    }

    public void setOnCycleViewPageChangeListener(OnCycleViewPageChangeListener listener) {
        this.mOnCycleViewPageChangeListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
        if(getAdapter() != null){
            ((TurnsPagerAdapter)getAdapter()).setOnItemClickListener(mOnItemClickListener);
        }
    }

    public void setAdapter(TurnsPagerAdapter adapter){
        if(adapter == null)
            return;
        super.setAdapter(adapter);
        count = adapter.getCount();
        if(adapter.getCount() != 0)
            setCurrentItem(1, false);
    }

    private void initViewPagerScroll() {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new CycleViewScroller(getContext());
            mField.set(this, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class OnPageChangeListener implements ViewPager.OnPageChangeListener{

        private int position;
        private int previousPosition;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if(position > 0 && position < count - 2){
                position--;
                onTurnsPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            int realCount = count - 2;
            position = (position + realCount - 1)%realCount;
            if(position != previousPosition){
                previousPosition = position;
                onTurnsPageSelected(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

            position = getCurrentItem();

            if(state == 1 || state == 0){
//                int count = cyclePagerAdapter.getCount();
//                int currentItem = -1;
//                if(position == 0){
//                    currentItem = count - 2;
//                }else if(position == count-1){
//                    currentItem = 1;
//                }
//                if(currentItem != -1)
//                    setCurrentItem(currentItem, false);

                int currentItem = getAtPosition(position);

                if(currentItem != -1)
                    setCurrentItem(currentItem, false);
            }

        }
    }

    protected int getAtPosition(int position){
        int currentItem = -1;
        if(position == 0){
            currentItem = count - 2;
        }else if(position == count-1){
            currentItem = 1;
        }
        return currentItem;
    }

    protected int getRealPosition(int position){
        int realCount = count - 2;
        position = (position + realCount - 1)%realCount;
        return position;
    }

    public void onTurnsPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(mOnCycleViewPageChangeListener != null)
            mOnCycleViewPageChangeListener.onTurnsPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    public void onTurnsPageSelected(int position) {
        if(mOnCycleViewPageChangeListener != null)
            mOnCycleViewPageChangeListener.onTurnsPageSelected(position);
    }

    public void onTurnsPageScrollStateChanged(int state) {
        if(mOnCycleViewPageChangeListener != null)
            mOnCycleViewPageChangeListener.onTurnsPageScrollStateChanged(state);
    }

    protected void onItemClick(View view, int position, long id){
        if(mOnItemClickListener != null)
            mOnItemClickListener.onItemClick(view, position, id);
    }

}
