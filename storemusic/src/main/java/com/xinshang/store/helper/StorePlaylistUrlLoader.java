package com.xinshang.store.helper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;
import com.xinshang.store.data.entities.StorePlaylist;
import com.xinshang.store.utils.Utils;

import java.io.InputStream;

/**
 * Created by koma_20 on 2018/3/17.
 */

public class StorePlaylistUrlLoader extends BaseGlideUrlLoader<StorePlaylist> {
    protected StorePlaylistUrlLoader(ModelLoader<GlideUrl, InputStream> concreteLoader,
                                     @Nullable ModelCache<StorePlaylist, GlideUrl> modelCache) {
        super(concreteLoader, modelCache);
    }

    @Override
    protected String getUrl(StorePlaylist storePlaylist, int width, int height, Options options) {
        return Utils.buildUrl(storePlaylist.albumId);
    }

    @Override
    public boolean handles(@NonNull StorePlaylist storePlaylist) {
        return true;
    }

    public static class Factory implements ModelLoaderFactory<StorePlaylist, InputStream> {
        private final ModelCache<StorePlaylist, GlideUrl> modelCache = new ModelCache<>(500);

        @NonNull
        @Override
        public ModelLoader<StorePlaylist, InputStream> build(@NonNull MultiModelLoaderFactory multiFactory) {
            return new StorePlaylistUrlLoader(multiFactory.build(GlideUrl.class, InputStream.class),
                    modelCache);
        }

        @Override
        public void teardown() {

        }
    }
}