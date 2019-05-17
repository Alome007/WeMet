package com.urbler.wemet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

public class RoundRectImageView extends android.support.v7.widget.AppCompatImageView {
    private Path path;
    public RoundRectImageView(Context context) {
        super(context);
        init();
    }

    public RoundRectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundRectImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        path = new Path();

    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        float radius = 30.0f;
        path.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}