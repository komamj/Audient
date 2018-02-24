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
package com.xinshang.audient;

import android.app.Application;
import android.os.StrictMode;

import com.bumptech.glide.Glide;
import com.xinshang.audient.model.ApplicationModule;
import com.xinshang.audient.model.AudientRepositoryComponent;
import com.xinshang.audient.model.AudientRepositoryModule;
import com.xinshang.audient.model.DaggerAudientRepositoryComponent;
import com.xinshang.common.util.Constants;
import com.xinshang.common.util.LogUtils;
import com.squareup.leakcanary.LeakCanary;

public class AudientApplication extends Application {
    private static final String TAG = AudientApplication.class.getSimpleName();

    private AudientRepositoryComponent mRepositoryComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mRepositoryComponent = DaggerAudientRepositoryComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .audientRepositoryModule(new AudientRepositoryModule(Constants.AUDIENT_HOST))
                .build();

        enableStrictMode();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }

        LeakCanary.install(this);
    }

    public AudientRepositoryComponent getRepositoryComponent() {
        return mRepositoryComponent;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        LogUtils.e(TAG, "onLowMemory");

        //clear cache
        Glide.get(this).clearMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

        LogUtils.i(TAG, "onTrimMemory level : " + level);

        if (level == TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(this).clearMemory();
        }

        Glide.get(this).trimMemory(level);
    }

    private void enableStrictMode() {
        final StrictMode.ThreadPolicy.Builder threadPolicyBuilder =
                new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog();

        final StrictMode.VmPolicy.Builder vmPolicyBuilder = new StrictMode.VmPolicy.Builder()
                .detectAll().penaltyLog();

        threadPolicyBuilder.penaltyFlashScreen();
        StrictMode.setThreadPolicy(threadPolicyBuilder.build());
        StrictMode.setVmPolicy(vmPolicyBuilder.build());
    }
}
