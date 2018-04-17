/*
 * Copyright 2017 Koma
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xinshang.audient.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.xinshang.common.util.LogUtils;

import java.util.Random;

/**
 * a music visualizer sort of animation (with random data)
 */
public class MusicVisualizer extends View {
    private static final String TAG = MusicVisualizer.class.getSimpleName();

    Random random = new Random();

    Paint paint = new Paint();

    private Runnable mAnimateRunnable = new Runnable() {
        @Override
        public void run() {
            //run every 120 ms
            postDelayed(this, 120);

            invalidate();
        }
    };

    public MusicVisualizer(Context context) {
        this(context, null);
    }

    public MusicVisualizer(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MusicVisualizer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        /*//start runnable
        removeCallbacks(mAnimateRunnable);
        post(mAnimateRunnable);*/
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //set paint style, Style.FILL will fill the color, Style.STROKE will stroke the color
        paint.setStyle(Paint.Style.FILL);

        canvas.drawRect(getDimensionInPixel(0), getHeight() - (40 + random.nextInt((int) (getHeight() / 1.5f) - 25)), getDimensionInPixel(7), getHeight() - 15, paint);
        canvas.drawRect(getDimensionInPixel(10), getHeight() - (40 + random.nextInt((int) (getHeight() / 1.5f) - 25)), getDimensionInPixel(17), getHeight() - 15, paint);
        canvas.drawRect(getDimensionInPixel(20), getHeight() - (40 + random.nextInt((int) (getHeight() / 1.5f) - 25)), getDimensionInPixel(27), getHeight() - 15, paint);
    }

    public void setColor(int color) {
        paint.setColor(color);

        invalidate();
    }

    //get all dimensions in dp so that views behaves properly on different screen resolutions
    private int getDimensionInPixel(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);

        LogUtils.i(TAG, "onWindowVisibilityChanged");

        if (visibility == VISIBLE) {
            removeCallbacks(mAnimateRunnable);
            post(mAnimateRunnable);
        } else if (visibility == GONE) {
            removeCallbacks(mAnimateRunnable);
        }
    }
}