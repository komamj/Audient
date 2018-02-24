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
package com.xinshang.audient.helper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;
import com.xinshang.audient.model.entities.Audient;
import com.xinshang.audient.util.Utils;

import java.io.InputStream;

public class AudientUrlLoader extends BaseGlideUrlLoader<Audient> {

    private AudientUrlLoader(ModelLoader<GlideUrl, InputStream> concreteLoader,
                             @Nullable ModelCache<Audient, GlideUrl> modelCache) {
        super(concreteLoader, modelCache);
    }

    @Override
    protected String getUrl(Audient audient, int width, int height, Options options) {
        return Utils.buildUrl(audient);
    }


    @Override
    public boolean handles(@NonNull Audient audient) {
        return true;
    }

    public static class Factory implements ModelLoaderFactory<Audient, InputStream> {
        private final ModelCache<Audient, GlideUrl> modelCache = new ModelCache<>(500);

        @NonNull
        @Override
        public ModelLoader<Audient, InputStream> build(@NonNull MultiModelLoaderFactory multiFactory) {
            return new AudientUrlLoader(multiFactory.build(GlideUrl.class, InputStream.class),
                    modelCache);
        }

        @Override
        public void teardown() {

        }
    }
}
