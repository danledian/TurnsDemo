package dld.com.turns.scroller;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class CycleViewScroller extends Scroller {

    private int mDuration = 800;

    public CycleViewScroller(Context context) {
        super(context);
    }

    public CycleViewScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

}
