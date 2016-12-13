package dld.com.turns.adapter;

import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import dld.com.turns.CycleViewPager;

/**
 * Created by song on 2016/11/21.
 */

public abstract class TurnsPagerAdapter extends PagerAdapter {

    private SparseArray<Object> mItemViews;
    private CycleViewPager.OnItemClickListener mOnItemClickListener;

    public TurnsPagerAdapter() {
        mItemViews = new SparseArray<>();
    }

    public class ViewPagerOnClickListener implements View.OnClickListener{

        private int position;

        public ViewPagerOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            onItemClick(v, position, v.getId());
        }
    }

    public void setOnItemClickListener(CycleViewPager.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    protected void onItemClick(View view, int position, long id){
        if(mOnItemClickListener != null)
            mOnItemClickListener.onItemClick(view, position, id);
    }

    protected abstract Object instantiateTurnsItem(ViewGroup container, int position);

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    final public int getCount() {
        return getItemCount()==0?0:getItemCount() + 2;
    }

    public abstract int getItemCount();

    @Override
    final public Object instantiateItem(ViewGroup container, int position) {
        int itemPosition;
        if(position == 0){
            itemPosition = getItemCount() - 1;
        }else if(position == getCount() - 1){
            itemPosition = 0;
        }else {
            itemPosition = position - 1;
        }
        Object obj = mItemViews.get(position);
        if(obj == null){
            obj = instantiateTurnsItem(container, itemPosition);
            ((View)obj).setOnClickListener(new ViewPagerOnClickListener(getRealPosition(position)));
            mItemViews.put(position, obj);
        }
        ViewParent viewParent = ((View)obj).getParent();
        if(viewParent != null){
            ((ViewGroup)viewParent).removeView((View)obj);
        }
        container.addView(((View) obj), ((View)obj).getLayoutParams());
        return obj;
    }

    @Override
    final public void destroyItem(ViewGroup container, int position, Object object) {
        if(position > 1 && position < getItemCount())
            container.removeView((View) mItemViews.get(position));
    }

    @Override
    final public Object instantiateItem(View container, int position) {
        return instantiateItem(container, position);
    }

    @Override
    final public void destroyItem(View container, int position, Object object) {
        destroyItem(container, position, object);
    }

    private int getRealPosition(int position){
        int realCount = getItemCount();
        position = (position + realCount - 1)%realCount;
        return position;
    }

}
