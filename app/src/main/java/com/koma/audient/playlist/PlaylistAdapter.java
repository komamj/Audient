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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koma.audient.R;
import com.koma.audient.helper.GlideApp;
import com.koma.audient.helper.GlideRequest;
import com.koma.audient.model.entities.AudientTest;
import com.koma.common.base.BaseAdapter;
import com.koma.common.base.BaseViewHolder;

import butterknife.BindView;
import butterknife.OnClick;

public class PlaylistAdapter extends BaseAdapter<AudientTest, PlaylistAdapter.PlaylistViewHolder> {

    private final GlideRequest<Drawable> mGlideRequest;

    public PlaylistAdapter(Context context) {
        super(context);

        mGlideRequest = GlideApp.with(mContext)
                .asDrawable()
                .dontAnimate()
                .thumbnail(0.1f)
                .placeholder(new ColorDrawable(Color.GRAY));
    }

    @Override
    protected boolean areItemsTheSame(AudientTest oldItem, AudientTest newItem) {
        return oldItem.equals(newItem);
    }

    @Override
    protected boolean areContentsTheSame(AudientTest oldItem, AudientTest newItem) {
        return false;
    }

    @Override
    protected Object getChangePayload(AudientTest oldItem, AudientTest newItem) {
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
        mGlideRequest.load(mData.get(position).albumUrl).into(holder.mAlbum);
        holder.mMusicName.setText(mData.get(position).musicName);
        holder.mActorName.setText(mData.get(position).actorName);
    }

    class PlaylistViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_album)
        ImageView mAlbum;
        @BindView(R.id.tv_music_name)
        TextView mMusicName;
        @BindView(R.id.tv_actor_name)
        TextView mActorName;

        @OnClick(R.id.ic_more)
        void showPopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(mContext, view);
            popupMenu.getMenuInflater().inflate(R.menu.playlist_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return false;
                }
            });
            popupMenu.show();
        }

        ImageView mEqualizer;

        PlaylistViewHolder(View itemView) {
            super(itemView);
        }
    }
}
