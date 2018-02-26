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
package com.xinshang.audient.mine;

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
import com.xinshang.audient.R;
import com.xinshang.audient.audition.AuditionDialogFragment;
import com.xinshang.audient.comment.CommentActivity;
import com.xinshang.audient.helper.GlideApp;
import com.xinshang.audient.helper.GlideRequest;
import com.xinshang.audient.model.entities.Audient;
import com.xinshang.audient.model.entities.Favorite;
import com.xinshang.common.base.BaseAdapter;
import com.xinshang.common.base.BaseViewHolder;
import com.xinshang.common.util.Constants;

import butterknife.BindView;
import butterknife.OnClick;

public class FavoritesSongAdapter extends BaseAdapter<Favorite.FavoritesSong, FavoritesSongAdapter.FavoriteSongVH> {
    private final GlideRequest<Bitmap> mGlideRequest;

    private EventListener mEventListener;

    public FavoritesSongAdapter(Context context) {
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
    protected boolean areItemsTheSame(Favorite.FavoritesSong oldItem, Favorite.FavoritesSong newItem) {
        return TextUtils.equals(oldItem.favoritesId, newItem.favoritesId);
    }

    @Override
    protected boolean areContentsTheSame(Favorite.FavoritesSong oldItem, Favorite.FavoritesSong newItem) {
        return false;
    }

    @Override
    protected Object getChangePayload(Favorite.FavoritesSong oldItem, Favorite.FavoritesSong newItem) {
        return null;
    }

    @Override
    public FavoriteSongVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_audient,
                parent, false);

        return new FavoriteSongVH(view);
    }

    @Override
    public void onBindViewHolder(FavoriteSongVH holder, int position) {
        Favorite.FavoritesSong favoritesSong = mData.get(position);

        mGlideRequest.load(favoritesSong).into(holder.mAlbum);

        holder.mName.setText(favoritesSong.mediaName);
        holder.mArtistName.setText(favoritesSong.artistName);
    }

    class FavoriteSongVH extends BaseViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_album)
        ImageView mAlbum;
        @BindView(R.id.tv_name)
        TextView mName;
        @BindView(R.id.tv_artist_name)
        TextView mArtistName;

        @OnClick(R.id.iv_playlist_add)
        void addToPlaylis() {
            if (mEventListener != null) {
                Favorite.FavoritesSong favoritesSong = mData.get(getAdapterPosition());

                mEventListener.onPlaylistChanged(buildAudient(favoritesSong));
            }
        }

        @OnClick(R.id.iv_more)
        void showPopupView(View view) {
            PopupMenu popupMenu = new PopupMenu(mContext, view);
            popupMenu.getMenuInflater().inflate(R.menu.favorites_song_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Favorite.FavoritesSong favoritesSong = mData.get(getAdapterPosition());

                    switch (item.getItemId()) {
                        case R.id.action_delete:
                            if (mEventListener != null) {
                                mEventListener.onDeleteEventChanged(favoritesSong);
                            }
                            break;
                        case R.id.action_comment:
                            Intent intent = new Intent(mContext, CommentActivity.class);
                            intent.putExtra(Constants.KEY_AUDIENT, buildAudient(favoritesSong));
                            mContext.startActivity(intent);
                            break;
                    }
                    return true;
                }
            });
            popupMenu.show();
        }

        public FavoriteSongVH(View view) {
            super(view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            Favorite.FavoritesSong favoritesSong = mData.get(position);

            Audient audient = buildAudient(favoritesSong);

            AuditionDialogFragment.newInstance(audient)
                    .show(((AppCompatActivity) mContext).getSupportFragmentManager(),
                            Constants.AUDITION_TAG);
        }
    }

    private static final Audient buildAudient(Favorite.FavoritesSong favoritesSong) {
        Audient audient = new Audient();

        audient.albumId = favoritesSong.albumId;
        audient.albumName = favoritesSong.albumName;
        audient.artistId = favoritesSong.artistId;
        audient.artistName = favoritesSong.artistName;
        audient.mediaId = favoritesSong.mediaId;
        audient.mediaName = favoritesSong.mediaName;
        audient.duration = favoritesSong.mediaInterval;

        return audient;
    }

    public interface EventListener {
        void onDeleteEventChanged(Favorite.FavoritesSong audient);

        void onPlaylistChanged(Audient audient);
    }
}
