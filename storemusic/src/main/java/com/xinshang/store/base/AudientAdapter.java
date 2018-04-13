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
package com.xinshang.store.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
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
import com.xinshang.store.audition.AuditionDialogFragment;
import com.xinshang.store.comment.CommentActivity;
import com.xinshang.store.data.entities.Song;
import com.xinshang.store.helper.GlideApp;
import com.xinshang.store.helper.GlideRequest;
import com.xinshang.store.utils.Constants;

import butterknife.BindView;
import butterknife.OnClick;

public class AudientAdapter extends BaseAdapter<Song, AudientAdapter.AudientViewHolder> {
    private static final String TAG = AudientAdapter.class.getSimpleName();

    private final GlideRequest<Bitmap> mGlideRequest;

    private EventListener mEventListener;

    public AudientAdapter(Context context) {
        super(context);

        mGlideRequest = GlideApp.with(mContext)
                .asBitmap()
                .thumbnail(0.1f)
                .transition(new BitmapTransitionOptions().crossFade())
                .placeholder(new ColorDrawable(Color.GRAY));
    }

    public void setEventListener(EventListener listener) {
        this.mEventListener = listener;
    }

    @Override
    protected boolean areItemsTheSame(Song oldItem, Song newItem) {
        return TextUtils.equals(oldItem.mediaId, newItem.mediaId);
    }

    @Override
    protected boolean areContentsTheSame(Song oldItem, Song newItem) {
        return oldItem.equals(newItem);
    }

    @Override
    protected Object getChangePayload(Song oldItem, Song newItem) {
        return null;
    }

    @Override
    public AudientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_audient,
                parent, false);

        return new AudientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AudientViewHolder holder, int position) {
        Song audient = mData.get(position);

        mGlideRequest.load(audient).into(holder.mAlbum);

        holder.mName.setText(audient.mediaName);
        holder.mArtistName.setText(audient.artistName);
    }

    public interface EventListener {
        void onFavoriteMenuClick(Song audient);

        void onPlaylistChanged(Song audient);
    }

    public class AudientViewHolder extends BaseViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_album)
        ImageView mAlbum;
        @BindView(R.id.tv_name)
        TextView mName;
        @BindView(R.id.tv_artist_name)
        TextView mArtistName;

        AudientViewHolder(View view) {
            super(view);

            itemView.setOnClickListener(this);
        }

        @OnClick(R.id.iv_playlist_add)
        void addToPlaylis() {
            if (mEventListener != null) {
                mEventListener.onPlaylistChanged(mData.get(getAdapterPosition()));
            }
        }

        @OnClick(R.id.iv_more)
        void showPopupView(View view) {
            PopupMenu popupMenu = new PopupMenu(mContext, view);
            popupMenu.getMenuInflater().inflate(R.menu.search_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Song audient = mData.get(getAdapterPosition());

                    switch (item.getItemId()) {
                        case R.id.action_favorite:
                            if (mEventListener != null) {
                                mEventListener.onFavoriteMenuClick(audient);
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

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            AuditionDialogFragment.newInstance(mData.get(position))
                    .show(((AppCompatActivity) mContext).getSupportFragmentManager(),
                            Constants.AUDITION_TAG);
        }
    }
}
