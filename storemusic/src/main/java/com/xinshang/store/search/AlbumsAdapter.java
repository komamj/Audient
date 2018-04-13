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
package com.xinshang.store.search;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.xinshang.store.R;
import com.xinshang.store.base.BaseAdapter;
import com.xinshang.store.base.BaseViewHolder;
import com.xinshang.store.data.entities.AlbumResponse;
import com.xinshang.store.helper.GlideApp;
import com.xinshang.store.helper.GlideRequest;

import butterknife.BindView;

/**
 * Created by koma on 4/13/18.
 */

public class AlbumsAdapter extends BaseAdapter<AlbumResponse.Album, AlbumsAdapter.AlbumsVH> {
    private final GlideRequest<Bitmap> mGlideRequest;

    private OnItemClickListener mListener;

    public AlbumsAdapter(Context context) {
        super(context);

        mGlideRequest = GlideApp.with(mContext)
                .asBitmap()
                .thumbnail(0.1f)
                .transition(new BitmapTransitionOptions().crossFade())
                .placeholder(new ColorDrawable(Color.GRAY));
    }

    public void setListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    protected boolean areItemsTheSame(AlbumResponse.Album oldItem, AlbumResponse.Album newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(AlbumResponse.Album oldItem, AlbumResponse.Album newItem) {
        return false;
    }

    @Override
    protected Object getChangePayload(AlbumResponse.Album oldItem, AlbumResponse.Album newItem) {
        return null;
    }

    @NonNull
    @Override
    public AlbumsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumsVH holder, int position) {

    }

    class AlbumsVH extends BaseViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_album)
        ImageView mAlbum;
        @BindView(R.id.tv_name)
        TextView mName;
        @BindView(R.id.tv_description)
        TextView mDescription;

        public AlbumsVH(View view) {
            super(view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(mData.get(getAdapterPosition()));
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(AlbumResponse.Album album);
    }
}
