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
package com.xinshang.store.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by koma_20 on 2018/3/21.
 */

public class ControlImageView extends AppCompatImageView {
    private static final float ACTIVE_ALPHA = 1.0f;
    private static final float INACTIVE_ALPHA = 0.54f;

    public ControlImageView(Context context) {
        this(context, null);
    }

    public ControlImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ControlImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled) {
            setAlpha(ACTIVE_ALPHA);
        } else {
            setAlpha(INACTIVE_ALPHA);
        }

        super.setEnabled(enabled);
    }
}
