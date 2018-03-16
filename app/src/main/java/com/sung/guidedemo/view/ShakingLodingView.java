package com.sung.guidedemo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by sung on 2018/3/15.
 * <p>
 * left(固定) ------------> right 充满 count=1
 * left ------------> (固定)right 缩短 count=2
 * left <------------ (固定)right 充满 count=3
 * left(固定) <------------ right 缩短 count=4
 * 循环
 */

public class ShakingLodingView extends View {
    /**
     * 是否显示圆角
     * */
    private boolean as_circle = true;
    /**
     * view宽高
     * */
    private int width = 0;
    private int height = 0;
    /**
     * 进度 动态变化端 x标示位
     * */
    private int dynamicX = 11;
    /**
     * 进度条宽度
     * */
    private float loadingWidth = 15.0F;
    /**
     * 进度行进的速度
     * */
    private long speed = 3L;
    /**
     * 画笔
     * */
    private Paint basePaint;
    private Paint frontPaint;
    /**
     * 背景色以及前景色
     * */
    private int baseColor = Color.parseColor("#f7f7f7");
    private int frontColor = Color.parseColor("#a3a7eb");
    /**
     * 变化规律（共四种）
     * */
    private int count = 0;
    /**
     * 是否以左端为固定端
     * */
    private boolean left_paint_mode = true;
    /**
     * 是否开始动画
     * */
    private volatile boolean started = true;

    public ShakingLodingView(Context context) {
        super(context);
    }

    public ShakingLodingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShakingLodingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initView() {
        basePaint = new Paint();
        basePaint.setColor(baseColor);
        basePaint.setStyle(Paint.Style.FILL);

        frontPaint = new Paint();
        frontPaint.setColor(frontColor);
        frontPaint.setStyle(Paint.Style.FILL);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //防止未初始化
        if (basePaint == null || frontPaint == null) {
            initView();
        }

        //获取宽高
        if (width == 0 || height == 0) {
            width = getMeasuredWidth();
            height = getMeasuredHeight();
        }

        //画背景
        if (as_circle) {
            canvas.drawRoundRect(10, height / 2 - loadingWidth / 2,
                    width - 10, height / 2 + loadingWidth / 2,
                    loadingWidth / 2, loadingWidth / 2, basePaint);
        } else {
            canvas.drawRect(10, height / 2 - loadingWidth / 2,
                    width - 10, height / 2 + loadingWidth / 2, basePaint);
        }
        if (!started) return;

        //画前景
        if (left_paint_mode) {
            //左固定
            if (as_circle) {
                canvas.drawRoundRect(10, height / 2 - loadingWidth / 2,
                        dynamicX + 10, height / 2 + loadingWidth / 2,
                        loadingWidth / 2, loadingWidth / 2, frontPaint);
            } else {
                canvas.drawRect(10, height / 2 - loadingWidth / 2,
                        dynamicX + 10, height / 2 + loadingWidth / 2, frontPaint);
            }
        } else {
            //右固定
            if (as_circle) {
                canvas.drawRoundRect(dynamicX, height / 2 - loadingWidth / 2,
                        width - 10, height / 2 + loadingWidth / 2,
                        loadingWidth / 2, loadingWidth / 2, frontPaint);
            } else {
                canvas.drawRect(dynamicX, height / 2 - loadingWidth / 2,
                        width - 10, height / 2 + loadingWidth / 2, frontPaint);
            }
        }

    }

    /**
     * 动画更新线程
     * */
    private Runnable thread = new Runnable() {
        @Override
        public void run() {
            while (started) {
                //安全保障 防止动态变化的x位置不在画出的底部背景上
                if (dynamicX < 10) {
                    dynamicX = 10;
                } else if (dynamicX > (width - 10)) {
                    dynamicX = width - 10;
                }

                //临界状态
                if (dynamicX == 10 || dynamicX == width - 10) {
                    //每次进度条走到最右/最左时切换到下一种显示模式
                    count++;
                    if (count > 4) {
                        count = 0;
                    }

                    switch (count) {
                        case 1://第一种 & 第二种 重设进度坐标为最左
                        case 2:
                            dynamicX = 11;
                            break;
                        case 3://第三种 & 第四种 重设进度坐标为最右
                        case 4:
                            dynamicX = width - 11;
                            break;
                    }
                }

                //第一种变换规律
                if (count == 1) {
                    left_paint_mode = true;
                    dynamicX = dynamicX + 5;
                }
                //第二种变换规律
                if (count == 2) {
                    left_paint_mode = false;
                    dynamicX = dynamicX + 5;
                }
                //第三种变换规律
                if (count == 3) {
                    left_paint_mode = false;
                    dynamicX = dynamicX - 5;
                }
                //第四种变换规律
                if (count == 4) {
                    left_paint_mode = true;
                    dynamicX = dynamicX - 5;
                }

                //更新
                postInvalidate();

                try {
                    //更新速度
                    Thread.sleep(speed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void reStart(){
        stop();

        dynamicX = 11;
        left_paint_mode = true;
        start();
    }

    /**
     * 动画开始
     * */
    public void start() {
        started = true;
        new Thread(thread).start();
    }

    /**
     * 动画结束
     * */
    public void stop() {
        started = false;

        dynamicX = 11;
        left_paint_mode = true;
        postInvalidate();
    }

    /**
     * 销毁
     * */
    public void destroy(){
        stop();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    /**
     * 圆角
     * */
    public void setAsCircle(boolean as_circle) {
        this.as_circle = as_circle;
        reStart();
    }

    /**
     * 高度
     * */
    public void setLoadingWidth(float loadingWidth) {
        this.loadingWidth = loadingWidth;
        reStart();
    }

    /**
     * 速度
     * */
    public void setSpeed(long speed) {
        this.speed = speed;
        reStart();
    }

    /**
     * 底色
     * */
    public void setBaseColor(int baseColor) {
        this.baseColor = baseColor;
        reStart();
    }

    /**
     * 进度色
     * */
    public void setFrontColor(int frontColor) {
        this.frontColor = frontColor;
        reStart();
    }
}
