package dld.com.turns;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.lang.ref.SoftReference;


/**
 * Created by song on 2016/11/21.
 */

public class AutoCycleViewPager extends CycleViewPager {

    private static final String TAG = "AutoCycleViewPager";

    private static final int FAST_INTERVAL = 3000;

    private static final int MSG_AUTO_CYCLE = 1;
    private static final int MSG_RESET_POSITION = 2;

    private CycleHandler mCycleHandler;

    private int intervalTime;
    private boolean autoCycle;
    private boolean stopByEvent;


    public AutoCycleViewPager(Context context) {
        this(context, null);
    }

    public AutoCycleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        intervalTime = FAST_INTERVAL;
        mCycleHandler = new CycleHandler(this, intervalTime);
    }

    public void setIntervalTime(int intervalTime) {
        if(intervalTime < 500)
            intervalTime = 500;
        this.intervalTime = intervalTime;
    }

    public void startAutoCycle(){
        Log.d(TAG, "startAutoCycle");
        autoCycle = true;
        removeCycleMessage();
        int atPosition = getAtPosition(getCurrentItem());
        Log.d(TAG, String.format("atPosition:%d", atPosition));
        if(atPosition != -1){
            setCurrentItem(atPosition, false);
            mCycleHandler.sendEmptyMessageDelayed(MSG_RESET_POSITION, 500);
        }else {
            mCycleHandler.sendEmptyMessageDelayed(MSG_AUTO_CYCLE, intervalTime);
        }
    }

    public void stopAutoCycle(){
        Log.d(TAG, "stopAutoCycle");
        autoCycle = false;
        removeCycleMessage();
    }

    private void removeCycleMessage(){
        mCycleHandler.removeMessages(MSG_AUTO_CYCLE);
        mCycleHandler.removeMessages(MSG_AUTO_CYCLE);
    }

    private void sendCycleMessage(){
        removeCycleMessage();
        mCycleHandler.sendEmptyMessageDelayed(MSG_AUTO_CYCLE, intervalTime);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        if(action == MotionEvent.ACTION_DOWN){
            removeCycleMessage();
            if(autoCycle){
                stopByEvent = true;
            }
            getParent().requestDisallowInterceptTouchEvent(true);
        }else if(action == MotionEvent.ACTION_UP){
            if(stopByEvent){
                stopByEvent = false;
                sendCycleMessage();
            }
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        return super.dispatchTouchEvent(ev);
    }

    private static class CycleHandler extends Handler{

        private SoftReference<AutoCycleViewPager> mSoftReference;
        private int intervalTime;

        CycleHandler(AutoCycleViewPager autoCycleViewPager, int intervalTime) {
            this.intervalTime = intervalTime;
            mSoftReference = new SoftReference<>(autoCycleViewPager);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_AUTO_CYCLE:
                    AutoCycleViewPager viewPager = mSoftReference.get();
                    if(viewPager == null)
                        return;
                    int currentItem = viewPager.getCurrentItem();
                    Log.d(TAG, String.format("currentItem:%d", currentItem));

                    int atPosition = viewPager.getAtPosition(currentItem);

                    Log.d(TAG, String.format("atPosition:%d", atPosition));

                    if(atPosition != -1){
                        viewPager.setCurrentItem(atPosition, false);

                        sendEmptyMessageDelayed(MSG_RESET_POSITION, 500);
                    }else {
                        viewPager.setCurrentItem(++currentItem);
                        sendEmptyMessageDelayed(MSG_AUTO_CYCLE, intervalTime);
                    }
                    break;
                case MSG_RESET_POSITION:
                    Log.d(TAG, "MSG_RESET_POSITION");
                    sendEmptyMessageDelayed(MSG_AUTO_CYCLE, intervalTime);
                    break;
            }
        }
    }

}
