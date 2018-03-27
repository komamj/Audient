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
package com.xinshang.audient.store;

import com.xinshang.audient.model.AudientRepositoryComponent;
import com.xinshang.common.util.FragmentScoped;

import dagger.Component;

/**
 * Created by koma on 3/26/18.
 */
@FragmentScoped
@Component(dependencies = AudientRepositoryComponent.class, modules = StoresPresenterModule.class)
public interface StoresComponent {
    void inject(StoresDialogFragment dialogFragment);
}