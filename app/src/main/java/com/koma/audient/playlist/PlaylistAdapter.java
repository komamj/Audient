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
package com.koma.audient.playlist;

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
import com.koma.audient.R;
import com.koma.audient.comment.CommentActivity;
import com.koma.audient.helper.GlideApp;
import com.koma.audient.helper.GlideRequest;
import com.koma.audient.model.entities.Audient;
import com.koma.common.base.BaseAdapter;
import com.koma.common.base.BaseViewHolder;
import com.koma.common.util.Constants;

import butterknife.BindView;
import butterknife.OnClick;

public class PlaylistAdapter extends BaseAdapter<Audient, PlaylistAdapter.PlaylistViewHolder> {
    private static final String TAG = PlaylistAdapter.class.getSimpleName();

    private EventListener mListener;

    private final GlideRequest<Bitmap> mGlideRequest;

    public PlaylistAdapter(Context context) {
        super(context);

        mGlideRequest = GlideApp.with(mContext)
                .asBitmap()
                .thumbnail(0.1f)
                .transition(new BitmapTransitionOptions().crossFade())
                .placeholder(new ColorDrawable(Color.GRAY));
    }

    public void setEventListener(EventListener listener) {
        this.mListener = listener;
    }

    @Override
    protected boolean areItemsTheSame(Audient oldItem, Audient newItem) {
        return TextUtils.equals(oldItem.mediaId, newItem.mediaId);
    }

    @Override
    protected boolean areContentsTheSame(Audient oldItem, Audient newItem) {
        return oldItem.equals(newItem);
    }

    @Override
    protected Object getChangePayload(Audient oldItem, Audient newItem) {
        return null;
    }

    @Override
    public PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_playlist,
                parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaylistViewHolder holder, int position) {
        Audient audient = mData.get(position);

        mGlideRequest.load(audient).into(holder.mAlbum);

        holder.mName.setText(audient.mediaName);
        holder.mArtistName.setText(audient.artistName);
    }

    class PlaylistViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_album)
        ImageView mAlbum;
        @BindView(R.id.tv_name)
        TextView mName;
        @BindView(R.id.tv_artist_name)
        TextView mArtistName;

        @OnClick(R.id.iv_thumb_up)
        void thumbUp() {
            if (mListener != null) {
                Audient audient = mData.get(getAdapterPosition());

                mListener.onThumbUpClick(audient);
            }
        }

        @OnClick(R.id.iv_more)
        void showPopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(mContext, view);
            popupMenu.getMenuInflater().inflate(R.menu.search_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Audient audient = mData.get(getAdapterPosition());

                    switch (item.getItemId()) {
                        case R.id.action_favorite:
                            if (mListener != null) {
                                mListener.onFavoriteMenuClick(audient);
                            }
                            break;
                        case R.id.action_comment:
                            Intent intent = new Intent(mContext, CommentActivity.class);
                            intent.putExtra(Constants.KEY_AUDIENT, audient);
                            mContext.startActivity(intent);
                            break;
                    }
                    return true;
                }
            });
            popupMenu.show();
        }

        PlaylistViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface EventListener {
        void onFavoriteMenuClick(Audient audient);

        void onThumbUpClick(Audient audient);
    }
}
