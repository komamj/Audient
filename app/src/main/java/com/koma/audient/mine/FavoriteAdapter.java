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
package com.koma.audient.mine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.koma.audient.R;
import com.koma.audient.helper.GlideApp;
import com.koma.audient.helper.GlideRequest;
import com.koma.audient.model.entities.Favorite;
import com.koma.common.base.BaseAdapter;
import com.koma.common.base.BaseViewHolder;

import butterknife.BindView;

public class FavoriteAdapter extends BaseAdapter<Favorite, FavoriteAdapter.FavoriteViewHolder> {
    private final GlideRequest<Bitmap> mGlideRequest;

    public FavoriteAdapter(Context context) {
        super(context);

        mGlideRequest = GlideApp.with(mContext)
                .asBitmap()
                .thumbnail(0.1f)
                .transition(new BitmapTransitionOptions().crossFade())
                .placeholder(new ColorDrawable(Color.GRAY));
    }

    @Override
    protected boolean areItemsTheSame(Favorite oldItem, Favorite newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(Favorite oldItem, Favorite newItem) {
        return false;
    }

    @Override
    protected Object getChangePayload(Favorite oldItem, Favorite newItem) {
        return null;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_favorite,
                parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        mGlideRequest.load("").into(holder.mAlbum);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 1 : mData.size();
    }

    public class FavoriteViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_album)
        ImageView mAlbum;

        public FavoriteViewHolder(View view) {
            super(view);
        }
    }
}
