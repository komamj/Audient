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
package com.koma.audient.helper;

import android.support.annotation.NonNull;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;
import com.koma.audient.model.entities.Music;

import java.io.InputStream;

public class MusicModelLoader extends BaseGlideUrlLoader<Music> {

    public static class Factory implements ModelLoaderFactory<Music, InputStream> {
        private final ModelCache<Music, GlideUrl> modelCache = new ModelCache<>(500);

        @NonNull
        @Override
        public ModelLoader<Music, InputStream> build(@NonNull MultiModelLoaderFactory multiFactory) {
            return new MusicModelLoader(multiFactory.build(GlideUrl.class, InputStream.class),
                    modelCache);
        }

        @Override
        public void teardown() {
        }
    }

    private MusicModelLoader(ModelLoader<GlideUrl, InputStream> urlLoader,
                             ModelCache<Music, GlideUrl> modelCache) {
        super(urlLoader, modelCache);
    }

    @Override
    public boolean handles(@NonNull Music model) {
        return true;
    }

    @Override
    protected String getUrl(Music model, int width, int height, Options options) {
        return null;
    }

    /*@Override
    protected List<String> getAlternateUrls(Music photo, int width, int height, Options options) {
        return null;
    }*/
}
