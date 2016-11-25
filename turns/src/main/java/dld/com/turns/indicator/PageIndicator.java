package dld.com.turns.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dld.com.turns.AutoCycleViewPager;
import dld.com.turns.CycleViewPager;
import dld.com.turns.R;
import dld.com.turns.adapter.TurnsPagerAdapter;

/**
 * Created by song on 2016/11/21.
 */
public class PageIndicator<T extends Indicator> extends View implements ViewPager.OnPageChangeListener {


    private static final String TAG = "PageIndicator";

    private final int DEFAULT_INDICATOR_RADIUS = dp2Px(3f);
    private static final int DEFAULT_INDICATOR_MARGIN = 20;
    private static final int DEFAULT_INDICATOR_SELECTED_COLOR = Color.parseColor("#4579D3");
    private static final int DEFAULT_INDICATOR_UNSELECTED_COLOR = Color.WHITE;
    private static final boolean DEFAULT_INDICATOR_IS_SCROLL = false;

    private boolean mIndicatorIsScroll;
    private int mIndicatorRadius;
    private int  mIndicatorSelectedColor;
    private int mIndicatorUnselectedColor;
    private int mIndicatorMargin;
    private int mBackgroundColor;
    private Shape mIndicatorShape;
    private int mCurrentPosition;
    private float mCurrentPositionOffset;
    private List<T> mIndicators;
    private Indicator mCurrentIndicator;

    private boolean isAutoCycleViewPager;
    private int count;
    private int previousPosition = -1;

    private static final Shape[] shapes = {
            Shape.CIRCLE,
            Shape.SQUARE,
            Shape.ROUND_RECT
    };

    public PageIndicator(Context context) {
        this(context, null);
    }

    public PageIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUp(context, attrs);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(mIndicatorIsScroll){
            nextItem(position, positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if(!mIndicatorIsScroll){
            nextItem(position, 0f);
        }else {
            if ((position == count - 1 && previousPosition == 0) ||
                    position == 0 && previousPosition == count - 1){
                nextItem(position, 0f);
            }
            previousPosition = position;
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private synchronized void nextItem(int position, float positionOffset) {
        mCurrentPosition = position;
        mCurrentPositionOffset = positionOffset;
        requestLayout();
        invalidate();
    }


    public enum Shape{
        CIRCLE,
        SQUARE,
        ROUND_RECT
    }

    private void setUp(Context context, AttributeSet attrs) {
        mIndicators = new ArrayList<>();
        if(attrs != null){
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PageIndicator);
            mIndicatorRadius = ta.getDimensionPixelSize(R.styleable.PageIndicator_indicator_radius, DEFAULT_INDICATOR_RADIUS);
            mIndicatorSelectedColor = ta.getColor(R.styleable.PageIndicator_indicator_selectedColor, DEFAULT_INDICATOR_SELECTED_COLOR);
            mIndicatorUnselectedColor = ta.getColor(R.styleable.PageIndicator_indicator_unselectedColor, DEFAULT_INDICATOR_UNSELECTED_COLOR);
            mIndicatorMargin = ta.getDimensionPixelSize(R.styleable.PageIndicator_indicator_margin, DEFAULT_INDICATOR_MARGIN);
            mIndicatorIsScroll = ta.getBoolean(R.styleable.PageIndicator_indicator_isScroll, DEFAULT_INDICATOR_IS_SCROLL);
            mBackgroundColor = ta.getColor(R.styleable.PageIndicator_indicator_background, Color.TRANSPARENT);
            if(ta.hasValue(R.styleable.PageIndicator_indicator_shape)){
                int integer = ta.getInteger(R.styleable.PageIndicator_indicator_shape, -1);
                setIndicatorShape(shapes[integer]);
            }
            ta.recycle();
        }
    }

    public void setIndicatorIsScroll(boolean isScroll) {
        this.mIndicatorIsScroll = isScroll;
    }

    public void setViewPager(ViewPager viewPager){
        if(viewPager == null || viewPager.getAdapter() == null)
            return;

        String simpleName = viewPager.getClass().getSimpleName();
        Log.d(TAG, String.format(Locale.ENGLISH, "simpleName:%s", simpleName));

        clear();
        if(AutoCycleViewPager.class.getSimpleName().equalsIgnoreCase(simpleName)){
            isAutoCycleViewPager = true;
            count = ((TurnsPagerAdapter)viewPager.getAdapter()).getItemCount();
        }else {
            isAutoCycleViewPager = false;
            count = viewPager.getAdapter().getCount();
        }

        Log.d(TAG, String.format("count:%d", count));
        addBgItem(count);
        addListener(viewPager);
        requestLayout();
        invalidate();
    }

    private void clear(){
        mCurrentIndicator = null;
        previousPosition = -1;
        count = 0;
    }

    private void addListener(ViewPager viewPager) {
        if(isAutoCycleViewPager){
            ((CycleViewPager)viewPager).setOnCycleViewPageChangeListener(new CycleViewPager.OnCycleViewPageChangeListener() {
                @Override
                public void onTurnsPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    onPageScrolled(position, positionOffset, positionOffsetPixels);
                }

                @Override
                public void onTurnsPageSelected(int position) {
                    onPageSelected(position);
                }

                @Override
                public void onTurnsPageScrollStateChanged(int state) {
                    onPageScrollStateChanged(state);
                }
            });
        }else {
            viewPager.addOnPageChangeListener(this);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        layoutIndicator();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(count > 0){
            int width = count * mIndicatorRadius * 2 + (count - 1) * mIndicatorMargin + getPaddingLeft() + getPaddingRight();
            int height = mIndicatorRadius * 2 + getPaddingTop() + getPaddingBottom();

            setMeasuredDimension(width, height);
        }

    }

    private void layoutIndicator() {
        if(mIndicators.size() == 0) return;

        int y = getMeasuredHeight()/2 - mIndicatorRadius;
        layoutBg(0, y);
        layoutSelectedIndicator(y);
    }

    private void layoutSelectedIndicator(int y) {
        mCurrentIndicator.setX(mIndicators.get(mCurrentPosition).getX() + mCurrentPositionOffset * (2 * mIndicatorRadius + mIndicatorMargin));
        mCurrentIndicator.setY(y);
    }

    private void layoutBg(int startX, int y) {
        Log.d(TAG, String.format("bg length:%d", mIndicators.size()));
        for(int i = 0;i < mIndicators.size();i++){
            Indicator indicator = mIndicators.get(i);
            indicator.setX(startX + i * (2 * mIndicatorRadius + mIndicatorMargin));
            indicator.setY(y);
        }
    }

    private void addBgItem(int count) {
        mIndicators.clear();
        for(int i=0;i< count;i++){
            Indicator indicator = new Indicator.Builder()
            .setColor(mIndicatorUnselectedColor)
            .setHeight(mIndicatorRadius * 2)
            .setWidth(mIndicatorRadius * 2)
            .setShape(mIndicatorShape)
            .setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN))
            .build();
            mIndicators.add((T) indicator);
        }
        mCurrentIndicator = new Indicator.Builder()
            .setColor(mIndicatorSelectedColor)
            .setHeight(mIndicatorRadius * 2)
            .setWidth(mIndicatorRadius * 2)
            .setShape(mIndicatorShape)
            .setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER))
            .build();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(mBackgroundColor);
        drawBg(canvas);
        drawIndicator(canvas);
    }

    private void drawIndicator(Canvas canvas) {
        if(mCurrentIndicator != null){
            canvas.save();
            canvas.translate(mCurrentIndicator.getX(), mCurrentIndicator.getY());
            mCurrentIndicator.getDrawable().draw(canvas);
            canvas.restore();
        }
    }

    private void drawBg(Canvas canvas) {
        for(Indicator indicator: mIndicators){
            canvas.save();
            canvas.translate(indicator.getX(), indicator.getY());
            indicator.getDrawable().draw(canvas);
            canvas.restore();
        }
    }

    public int getIndicatorRadius() {
        return mIndicatorRadius;
    }

    public void setIndicatorRadius(int mIndicatorRadius) {
        this.mIndicatorRadius = mIndicatorRadius;
    }

    public int getIndicatorSelectedColor() {
        return mIndicatorSelectedColor;
    }

    public void setIndicatorSelectedColor(int mIndicatorSelectedColor) {
        this.mIndicatorSelectedColor = mIndicatorSelectedColor;
    }

    public int getIndicatorunselectedColor() {
        return mIndicatorUnselectedColor;
    }

    public void setIndicatorunselectedColor(int mIndicatorunselectedColor) {
        this.mIndicatorUnselectedColor = mIndicatorunselectedColor;
    }

    public int getIndicatorMargin() {
        return mIndicatorMargin;
    }

    public void setIndicatorMargin(int mIndicatorMargin) {
        this.mIndicatorMargin = mIndicatorMargin;
    }

    public Shape getIndicatorShape() {
        return mIndicatorShape;
    }

    public void setIndicatorShape(Shape mIndicatorShape) {
        this.mIndicatorShape = mIndicatorShape;
    }

    private int dp2Px(float dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
