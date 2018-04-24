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
package com.koma.musicvisualizerlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.Random;

/**
 * a music visualizer sort of animation (with mRandom data)
 */
public class MusicVisualizer extends View {
    private static final String TAG = MusicVisualizer.class.getSimpleName();

    private Random mRandom;

    private Paint mPaint;

    private boolean mIsPlaying = true;

    private Runnable mAnimateRunnable = new Runnable() {
        @Override
        public void run() {
            postDelayed(this, 120);

            invalidate();
        }
    };

    public void setPlayingStatus(boolean isPlaying) {
        mIsPlaying = isPlaying;

        if (mIsPlaying) {
            removeCallbacks(mAnimateRunnable);
            post(mAnimateRunnable);
        } else {
            removeCallbacks(mAnimateRunnable);

            invalidate();
        }
    }

    public MusicVisualizer(Context context) {
        this(context, null);
    }

    public MusicVisualizer(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MusicVisualizer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#FF000000"));

        mRandom = new Random();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int paddingStart = getPaddingStart();
        int padddingEnd = getPaddingEnd();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        canvas.drawRect(getDimensionInPixel(paddingStart), getHeight() - (40 + mRandom.nextInt((int) (getHeight() / 1.5f) - 25)), getDimensionInPixel(paddingStart + 7), getHeight() - 15, mPaint);
        canvas.drawRect(getDimensionInPixel(paddingStart + 10), getHeight() - (40 + mRandom.nextInt((int) (getHeight() / 1.5f) - 25)), getDimensionInPixel(paddingStart + 17), getHeight() - 15, mPaint);
        canvas.drawRect(getDimensionInPixel(paddingStart + 20), getHeight() - (40 + mRandom.nextInt((int) (getHeight() / 1.5f) - 25)), getDimensionInPixel(paddingStart + 27), getHeight() - 15, mPaint);
    }

    //get all dimensions in dp so that views behaves properly on different screen resolutions
    private int getDimensionInPixel(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);

        if (mIsPlaying) {
            if (visibility == VISIBLE) {
                removeCallbacks(mAnimateRunnable);
                post(mAnimateRunnable);
            } else if (visibility == GONE) {
                removeCallbacks(mAnimateRunnable);
            }
        }
    }
}