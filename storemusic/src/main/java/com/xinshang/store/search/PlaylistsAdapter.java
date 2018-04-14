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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.xinshang.store.R;
import com.xinshang.store.base.BaseAdapter;
import com.xinshang.store.base.BaseViewHolder;
import com.xinshang.store.data.entities.Playlist;
import com.xinshang.store.helper.GlideApp;
import com.xinshang.store.helper.GlideRequest;

import butterknife.BindView;

public class PlaylistsAdapter extends BaseAdapter<Playlist, PlaylistsAdapter.PlaylistsVH> {
    private final GlideRequest<Bitmap> mGlideRequest;

    private OnItemClickListener mListener;

    public PlaylistsAdapter(Context context) {
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
    protected boolean areItemsTheSame(Playlist oldItem, Playlist newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(Playlist oldItem, Playlist newItem) {
        return false;
    }

    @Override
    protected Object getChangePayload(Playlist oldItem, Playlist newItem) {
        return null;
    }

    @NonNull
    @Override
    public PlaylistsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_playlists,
                parent, false);

        return new PlaylistsVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistsVH holder, int position) {
        Playlist playlist = mData.get(position);

        mGlideRequest.load(playlist.imageUrl).into(holder.mAlbum);

        holder.mName.setText(playlist.name);
        holder.mDescription.setText(buildDescription(playlist));
    }

    private String buildDescription(Playlist playlist) {
        StringBuilder builder = new StringBuilder();
        builder.append(playlist.songCount);
        builder.append("首音乐");
        builder.append(" | ");
        builder.append("播放");
        builder.append(playlist.listenNum);
        builder.append("次");
        return builder.toString();
    }

    class PlaylistsVH extends BaseViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_album)
        ImageView mAlbum;
        @BindView(R.id.tv_name)
        TextView mName;
        @BindView(R.id.tv_description)
        TextView mDescription;

        public PlaylistsVH(View view) {
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
        void onItemClick(Playlist playlist);
    }
}
