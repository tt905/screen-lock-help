package com.mo.screenlock.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatTextView;

import com.mo.screenlock.R;


/**
 * @author : beta mtw905@gmail.com
 * date : 2020/7/6 13:56
 * desc : 自定义按钮
 */
public class MUIButton extends AppCompatTextView {

    public MUIButton(Context context) {
        this(context, null);
    }

    public MUIButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MUIButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //获取自定义样式的属性
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MUIButton, 0, 0);
        int radius = ta.getDimensionPixelOffset(R.styleable.MUIButton_mui_button_radius, 0);
        int startColor = ta.getColor(R.styleable.MUIButton_mui_start_color, -1);
        int endColor = ta.getColor(R.styleable.MUIButton_mui_end_color, -1);
        int strokeColor = ta.getColor(R.styleable.MUIButton_mui_stroke_color, -1);
        ta.recycle();

        setGradientDrawable(radius, startColor, endColor, strokeColor);
    }

    /**
     * 设置渐变色圆角背景(渐变从左到右）
     *
     * @param radius      圆角半径
     * @param startColor  渐变开始颜色
     * @param endColor    渐变结束颜色
     * @param strokeColor 描边颜色
     */
    public void setGradientDrawable(int radius, int startColor, int endColor, int strokeColor) {
        GradientDrawable bgDrawable = new GradientDrawable();
        if (startColor != -1 && endColor != -1) {
            int[] colors = {startColor, endColor};
            bgDrawable.setColors(colors);
            // 默认线性，从左到右
            bgDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            bgDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        } else if (startColor != -1) {
            bgDrawable.setColor(startColor);
        }
        if (radius > 0) {
            bgDrawable.setCornerRadius(radius);
        }

        if (strokeColor != -1) {
            bgDrawable.setStroke(dp2px(1), strokeColor);
        }
        setBackground(bgDrawable);
    }

    /**
     * 设置单颜色圆角背景
     *
     * @param radius 圆角半径
     * @param color  背景色
     */
    public void setGradientDrawable(int radius, int color) {
        GradientDrawable bgDrawable = new GradientDrawable();
        bgDrawable.setColor(color);
        if (radius > 0) {
            bgDrawable.setCornerRadius(radius);
        }
        setBackground(bgDrawable);
    }

    /**
     * dp转px
     */
    public int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getContext().getResources().getDisplayMetrics());
    }
}
