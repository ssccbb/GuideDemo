package com.sung.guidedemo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.sung.guidedemo.R;

/**
 * Created by sung on 2018/3/14.
 */

public class VariableDirectionProgressbar extends View {
    public static final String TAG = VariableDirectionProgressbar.class.getSimpleName();

    private OnProgressChangeListener onProgressChangeListener;
    private int measuredHeight;
    private int measuredWidth;
    /**
     * 展示方式
     */
    private int Display_Mode = MODE_HORIZATAL;
    /**
     * 竖直/水平
     */
    public static final int MODE_VERTICAL = 0;
    public static final int MODE_HORIZATAL = 1;
    /**
     * 前景色/底色
     */
    private int color_fornt_progress = Color.parseColor("#f3cbe4");
    private int color_behind_progress = Color.parseColor("#f6f4fa");
    /**
     * 滑块颜色
     */
    public int color_thumb = Color.parseColor("#ef8cc2");
    /**
     * 滑块宽度
     * <p>
     * 此处滑块宽度关联进度条的高度
     * 当控件的实际高度/宽度 会导致
     * 滑块裁剪显示时会自动适配滑块
     * 的宽度 当前设值无效
     * <p>
     * 具体代码在onDraw方法中体现
     */
    private int thumb_width = 20;
    /**
     * 滑块显示
     * */
    private boolean thumb_visible = true;
    /**
     * 总刻度/当前刻度
     */
    private int total_progress = 100;
    private int current_progress = 100;

    public VariableDirectionProgressbar(Context context) {
        super(context);
    }

    public VariableDirectionProgressbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.VariableDirectionProgressbar);
        if (typedArray != null) {
            total_progress = typedArray.getInteger(R.styleable.VariableDirectionProgressbar_total_progress, total_progress);
            current_progress = typedArray.getInteger(R.styleable.VariableDirectionProgressbar_progress, 0);
            color_behind_progress = typedArray.getInteger(R.styleable.VariableDirectionProgressbar_progress_behind_color, color_behind_progress);
            color_fornt_progress = typedArray.getInteger(R.styleable.VariableDirectionProgressbar_progress_front_color, color_fornt_progress);
            color_thumb = typedArray.getInteger(R.styleable.VariableDirectionProgressbar_progress_thumb_color, color_thumb);
            Display_Mode = typedArray.getInteger(R.styleable.VariableDirectionProgressbar_progress_direction, Display_Mode);
            thumb_visible = typedArray.getBoolean(R.styleable.VariableDirectionProgressbar_thumb_visble, thumb_visible);
            thumb_width = typedArray.getInteger(R.styleable.VariableDirectionProgressbar_thumb_width, thumb_width);
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        measuredHeight = getMeasuredHeight();
        measuredWidth = getMeasuredWidth();

        if (current_progress > total_progress){
            current_progress = total_progress;
        }else if (current_progress < 0){
            current_progress = 0;
        }

        //底景
        Paint behind = new Paint();
        behind.setColor(color_behind_progress);
        behind.setStyle(Paint.Style.FILL);
        //前景
        Paint front = new Paint();
        front.setColor(color_fornt_progress);
        front.setStyle(Paint.Style.FILL);
        //滑块
        Paint thumb = new Paint();
        thumb.setColor(color_thumb);
        thumb.setStyle(Paint.Style.FILL);

        //水平画
        if (Display_Mode == MODE_HORIZATAL) {
            // TODO: 2018/3/14 宽度检测
            if ((measuredWidth - 20) / total_progress == 0){
                Log.e(TAG, "Method onDraw ----->> java.lang.ArithmeticException: divide by zero ! Please check the width var！！");
                return;
            }
            //高度安全标志预防（xml中高度会导致滑块裁剪显示就重设thumb_width）
            if (measuredHeight < (thumb_width + thumb_width)) {
                thumb_width = measuredHeight - thumb_width;
            }
            //进度安全标志位
            int safeProgress = thumb_width / ((measuredWidth - 20) / total_progress);
            if (current_progress <= safeProgress) {
                //以滑块右标志位为基准，所以此处不用考虑充顶的情况，但是在拖动的事件中需要区分小于安全标志位的数据拖动处理
                current_progress = safeProgress;
            }
            //进度条高度
            int progress_height = thumb_width;
            //滑块右标志位
            int thumbRight = (measuredWidth - 20) * current_progress / total_progress + 10;

            //画底部部分
            canvas.drawRoundRect(10,
                    (measuredHeight - progress_height) / 2,
                    measuredWidth - 10,
                    measuredHeight - ((measuredHeight - progress_height) / 2),
                    progress_height / 2, progress_height / 2, behind);
            //画前景部分
            canvas.drawRoundRect(10,
                    (measuredHeight - progress_height) / 2,
                    thumbRight,
                    measuredHeight - ((measuredHeight - progress_height) / 2),
                    progress_height / 2, progress_height / 2, front);
            //画滑块部分
            if (thumb_visible) {
                canvas.drawRoundRect((thumbRight - thumb_width) < 10 ? 10 : (thumbRight - thumb_width),//防止滑块出现在进度条左边
                        (measuredHeight - progress_height) / 2 - thumb_width / 2,
                        (thumbRight - thumb_width) < 10 ? (10 + thumb_width) : thumbRight,
                        measuredHeight - ((measuredHeight - progress_height) / 2) + thumb_width / 2,
                        thumb_width / 2, thumb_width / 2, thumb);
            }
        }

        //垂直画
        if (Display_Mode == MODE_VERTICAL) {
            // TODO: 2018/3/14 高度检测
            if ((measuredHeight - 20) / total_progress == 0){
                Log.e(TAG, "Method onDraw ----->> java.lang.ArithmeticException: divide by zero ! Please check the height var！！");
                return;
            }
            //宽度安全标志预防（xml中宽度会导致滑块裁剪显示就重设thumb_width）
            if (measuredWidth < (thumb_width + thumb_width)){
                //以滑块上标志位为基准，所以此处不用考虑充顶的情况，但是在拖动的事件中需要区分小于安全标志位的数据拖动处理
                thumb_width = measuredWidth - thumb_width;
            }
            //进度安全标志位
            int safeProgress = thumb_width / ((measuredHeight - 20) / total_progress);
            if (current_progress <= safeProgress){
                current_progress = safeProgress;
            }
            //进度条宽度
            int progress_width = thumb_width;
            //滑块上部标志位
            int thumbTop = measuredHeight - ( (measuredHeight - 20) * current_progress / total_progress ) - 10;
            //画底部部分
            canvas.drawRoundRect(measuredWidth - (measuredWidth - progress_width) / 2 - progress_width,
                    10,
                    measuredWidth - (measuredWidth - progress_width) / 2,
                    measuredHeight - 10,
                    progress_width / 2, progress_width / 2, behind);
            //画前景部分
            canvas.drawRoundRect(measuredWidth - (measuredWidth - progress_width) / 2 - progress_width,
                    thumbTop,
                    measuredWidth - (measuredWidth - progress_width) / 2,
                    measuredHeight - 10,
                    progress_width / 2, progress_width / 2, front);
            //画滑块部分
            if (thumb_visible) {
                canvas.drawRoundRect(measuredWidth - (measuredWidth - progress_width) / 2 - progress_width - thumb_width / 2,
                        (thumbTop + thumb_width) > (measuredHeight - 10) ? (measuredHeight - 10 - thumb_width) : thumbTop,//防止滑块画出现在进度条以下
                        measuredWidth - (measuredWidth - progress_width) / 2 + thumb_width / 2,
                        (thumbTop + thumb_width) > (measuredHeight - 10) ? (measuredHeight - 10) : (thumbTop + thumb_width),
                        thumb_width / 2, thumb_width / 2, thumb);
            }
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        //Log.d(TAG, "onTouchEvent: x="+x+"\ty="+y +"\th:"+getMeasuredHeight()+"/w:"+getMeasuredWidth());
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_MOVE:
                if (Display_Mode == MODE_HORIZATAL){
                    if (x > measuredWidth) x = measuredWidth;
                    if (x < 0) x = 0;
                    x = measuredWidth - x;
                    current_progress = (int) (x / (measuredWidth / total_progress));
                }

                if (Display_Mode == MODE_VERTICAL){
                    if (y > measuredHeight) y = measuredHeight;
                    if (y < 0) y = 0;
                    y = measuredHeight - y;
                    current_progress = (int) (y / (measuredHeight / total_progress));
                }

                if (onProgressChangeListener != null){
                    onProgressChangeListener.onProgressChange(current_progress);
                }
                break;
        }
        postInvalidate();
        return true;
    }

    public void setDisplayMode(int display_Mode) {
        Display_Mode = display_Mode;
        postInvalidate();
    }

    public void setThumbWidth(int thumb_width) {
        this.thumb_width = thumb_width;
        postInvalidate();
    }

    public void setThumbVisible(boolean thumb_visible) {
        this.thumb_visible = thumb_visible;
        postInvalidate();
    }

    public int getCurrentProgress() {
        return current_progress;
    }

    public void setCurrentProgress(int current_progress) {
        this.current_progress = current_progress;
        postInvalidate();
    }

    public interface OnProgressChangeListener{
        void onProgressChange(int progress);
    }

    public void addOnProgressChangeListener(OnProgressChangeListener onProgressChangeListener){
        this.onProgressChangeListener = onProgressChangeListener;
    }

}
