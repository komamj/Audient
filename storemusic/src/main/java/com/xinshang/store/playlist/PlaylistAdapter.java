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
package com.xinshang.store.playlist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.xinshang.store.R;
import com.xinshang.store.base.BaseAdapter;
import com.xinshang.store.base.BaseViewHolder;
import com.xinshang.store.comment.CommentActivity;
import com.xinshang.store.data.entities.Song;
import com.xinshang.store.data.entities.StoreSong;
import com.xinshang.store.helper.GlideApp;
import com.xinshang.store.helper.GlideRequest;
import com.xinshang.store.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PlaylistAdapter extends BaseAdapter<StoreSong, PlaylistAdapter.PlaylistViewHolder> {
    private static final String TAG = PlaylistAdapter.class.getSimpleName();

    private final GlideRequest<Bitmap> mGlideRequest;

    private EventListener mListener;

    public PlaylistAdapter(Context context) {
        super(context);

        mGlideRequest = GlideApp.with(mContext)
                .asBitmap()
                .thumbnail(0.1f)
                .transition(new BitmapTransitionOptions().crossFade())
                .placeholder(new ColorDrawable(Color.GRAY));
    }

    @Override
    protected boolean areItemsTheSame(StoreSong oldItem, StoreSong newItem) {
        return TextUtils.equals(oldItem.id, newItem.id);
    }

    @Override
    protected boolean areContentsTheSame(StoreSong oldItem, StoreSong newItem) {
        return oldItem.equals(newItem);
    }

    @Override
    protected Object getChangePayload(StoreSong oldItem, StoreSong newItem) {
        if (oldItem.isPlaying != newItem.isPlaying) {
            return Constants.PAYLOAD_PLAYING;
        } else {
            return null;
        }
    }

    public void setEventListener(EventListener listener) {
        this.mListener = listener;
    }


    @Override
    public PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_playlist,
                parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaylistViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else if (((int) payloads.get(0)) == Constants.PAYLOAD_PLAYING) {
            if (mData.get(position).isPlaying) {
                holder.mName.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            } else {
                holder.mName.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryText));
            }
        }
    }

    @Override
    public void onBindViewHolder(PlaylistViewHolder holder, int position) {
        StoreSong storePlaylist = mData.get(position);

        mGlideRequest.load(storePlaylist).into(holder.mAlbum);

        holder.mName.setText(storePlaylist.mediaName);
        if (storePlaylist.isPlaying) {
            holder.mName.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        } else {
            holder.mName.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryText));
        }
        if (TextUtils.isEmpty(storePlaylist.demandId)) {
            holder.mUserIndicator.setVisibility(View.GONE);
        } else {
            holder.mUserIndicator.setVisibility(View.VISIBLE);
        }
        holder.mArtistName.setText(storePlaylist.artistName);
    }

    public interface EventListener {
        void onFavoriteMenuClick(Song audient);

        void onDeleteMenuClick(StoreSong storeSong);

        void onItemClick(StoreSong storeSong);
    }

    class PlaylistViewHolder extends BaseViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_album)
        ImageView mAlbum;
        @BindView(R.id.tv_name)
        TextView mName;
        @BindView(R.id.tv_artist_name)
        TextView mArtistName;
        @BindView(R.id.tv_user)
        TextView mUserIndicator;

        PlaylistViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
        }

        @OnClick(R.id.iv_more)
        void showPopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(mContext, view);
            popupMenu.getMenuInflater().inflate(R.menu.playlist_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    StoreSong storeSong = mData.get(getAdapterPosition());
                    Song tencentMusic = new Song();
                    tencentMusic.duration = Long.parseLong(storeSong.mediaInterval);
                    tencentMusic.albumId = storeSong.albumId;
                    tencentMusic.artistId = storeSong.artistId;
                    tencentMusic.albumName = storeSong.albumName;
                    tencentMusic.artistName = storeSong.artistName;
                    tencentMusic.mediaId = storeSong.mediaId;
                    tencentMusic.mediaName = storeSong.mediaName;

                    switch (item.getItemId()) {
                        case R.id.action_favorite:
                            if (mListener != null) {
                                mListener.onFavoriteMenuClick(tencentMusic);
                            }
                            break;
                        case R.id.action_comment:
                            Intent intent = new Intent(mContext, CommentActivity.class);
                            intent.putExtra(Constants.KEY_AUDIENT, tencentMusic);
                            mContext.startActivity(intent);
                            break;
                        case R.id.action_delete:
                            if (mListener != null) {
                                mListener.onDeleteMenuClick(storeSong);
                            }
                            break;
                        default:
                            break;
                    }
                    return true;
                }
            });
            popupMenu.show();
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(mData.get(getAdapterPosition()));
            }
        }
    }
}
