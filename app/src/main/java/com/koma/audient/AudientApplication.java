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
package com.koma.audient;

import com.koma.audient.model.ApplicationModule;
import com.koma.audient.model.AudientRepositoryComponent;
import com.koma.audient.model.AudientRepositoryModule;
import com.koma.audient.model.DaggerAudientRepositoryComponent;
import com.koma.common.base.BaseApplication;

/**
 * Created by koma on 1/3/18.
 */

public class AudientApplication extends BaseApplication {
    private AudientRepositoryComponent mRepositoryComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mRepositoryComponent = DaggerAudientRepositoryComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .audientRepositoryModule(new AudientRepositoryModule())
                .build();
    }

    public AudientRepositoryComponent getRepositoryComponent() {
        return mRepositoryComponent;
    }
}
