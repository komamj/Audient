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
package com.xinshang.audient.favorite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.xinshang.audient.R;
import com.xinshang.audient.helper.GlideApp;
import com.xinshang.audient.helper.GlideRequest;
import com.xinshang.audient.model.entities.Favorite;
import com.xinshang.common.base.BaseAdapter;
import com.xinshang.common.base.BaseViewHolder;

import butterknife.BindView;

public class MyFavoritesAdapter extends BaseAdapter<Favorite, MyFavoritesAdapter.MyFavoritesVH> {
    private final GlideRequest<Bitmap> mGlideRequest;

    private EventListener mListener;

    public MyFavoritesAdapter(Context context) {
        super(context);

        mGlideRequest = GlideApp.with(mContext)
                .asBitmap()
                .thumbnail(0.1f)
                .transition(new BitmapTransitionOptions().crossFade())
                .placeholder(new ColorDrawable(Color.GRAY));
    }

    public void setListener(EventListener listener) {
        this.mListener = listener;
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
    public MyFavoritesVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_favorites,
                parent, false);

        return new MyFavoritesVH(view);
    }

    @Override
    public void onBindViewHolder(MyFavoritesVH holder, int position) {
        Favorite favorite = mData.get(position);

        mGlideRequest.load(favorite.coverImageUrl).into(holder.mImage);
        holder.mName.setText(favorite.favoriteName);
        holder.mCount.setText(mContext.getResources().getQuantityString(R.plurals.song_count,
                favorite.itemCount, favorite.itemCount));
    }

    public class MyFavoritesVH extends BaseViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_name)
        TextView mName;
        @BindView(R.id.iv_album)
        ImageView mImage;
        @BindView(R.id.tv_count)
        TextView mCount;

        public MyFavoritesVH(View view) {
            super(view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                Favorite favorite = mData.get(getAdapterPosition());
                mListener.onItemClick(favorite);
            }
        }
    }

    public interface EventListener {
        void onItemClick(Favorite favorite);
    }
}
