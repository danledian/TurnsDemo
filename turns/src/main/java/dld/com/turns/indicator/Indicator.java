package dld.com.turns.indicator;

import android.graphics.Paint;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;


/**
 * Created by song on 2016/11/21.
 */
public class Indicator {

    private Drawable mDrawable;
    private float x;
    private float y;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public void setDrawable(Drawable mDrawable) {
        this.mDrawable = mDrawable;
    }

    public Indicator(Drawable drawable){
        mDrawable = drawable;
    }

    public static class Builder{

        private int width;
        private int height;
        private int color;
        private PageIndicator.Shape shape;
        private Drawable drawable;
        private Xfermode mXfermode;

        public Xfermode getXfermode() {
            return mXfermode;
        }

        public Builder setXfermode(Xfermode mXfermode) {
            this.mXfermode = mXfermode;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setColor(int color) {
            this.color = color;
            return this;
        }

        public Builder setShape(PageIndicator.Shape shape) {
            this.shape = shape;
            return this;
        }

        public Builder setDrawable(Drawable drawable) {
            this.drawable = drawable;
            return this;
        }

        public Indicator build(){
            Shape dShape = null;
            Drawable drawable;
            if(shape != null){
                switch (shape){
                    case CIRCLE:
                        dShape = new OvalShape();
                        break;
                    case SQUARE:
                        dShape = new RectShape();
                        break;
                    case ROUND_RECT:
                        dShape = new RoundRectShape(new float[]{10, 10, 10, 10, 10, 10, 10, 10},
                                null, new float[]{0, 0, 0, 0, 0, 0, 0, 0});
                        break;
                    default:
                        break;
                }
                dShape.resize(width, height);
                ShapeDrawable shapeDrawable = new ShapeDrawable(dShape);
                Paint paint = shapeDrawable.getPaint();
                paint.setAntiAlias(true);
                paint.setColor(color);
                if(mXfermode != null)
                    paint.setXfermode(mXfermode);
                drawable = shapeDrawable;
            }else {
                drawable = this.drawable;
            }
            return new Indicator(drawable);
        }
    }


}
