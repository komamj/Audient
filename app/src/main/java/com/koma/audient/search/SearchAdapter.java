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
package com.koma.audient.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koma.audient.R;
import com.koma.audient.audition.AuditionDialogFragment;
import com.koma.audient.comment.CommentActivity;
import com.koma.audient.helper.GlideApp;
import com.koma.audient.helper.GlideRequest;
import com.koma.audient.model.entities.Audient;
import com.koma.common.base.BaseAdapter;
import com.koma.common.base.BaseViewHolder;
import com.koma.common.util.Constants;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchAdapter extends BaseAdapter<Audient, SearchAdapter.SearchViewHolder> {
    private static final String TAG = SearchAdapter.class.getSimpleName();

    private final GlideRequest<Drawable> mGlideRequest;

    public SearchAdapter(Context context) {
        super(context);

        mGlideRequest = GlideApp.with(mContext)
                .asDrawable()
                .dontAnimate()
                .thumbnail(0.1f)
                .placeholder(new ColorDrawable(Color.GRAY));
    }

    @Override
    protected boolean areItemsTheSame(Audient oldItem, Audient newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(Audient oldItem, Audient newItem) {
        return false;
    }

    @Override
    protected Object getChangePayload(Audient oldItem, Audient newItem) {
        return null;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_search,
                parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        mGlideRequest.load(mData.get(position)).into(holder.mAlbum);

        holder.mMusicName.setText(mData.get(position).name);
        holder.mActorName.setText(mData.get(position).artist.name);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class SearchViewHolder extends BaseViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_album)
        ImageView mAlbum;
        @BindView(R.id.tv_music_name)
        TextView mMusicName;
        @BindView(R.id.tv_actor_name)
        TextView mActorName;

        @OnClick(R.id.iv_playlist_add)
        void addToPlaylis() {

        }

        @OnClick(R.id.iv_more)
        void showPopupView(View view) {
            PopupMenu popupMenu = new PopupMenu(mContext, view);
            popupMenu.getMenuInflater().inflate(R.menu.search_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_favorite:
                            break;
                        case R.id.action_comment:
                            Intent intent = new Intent(mContext, CommentActivity.class);
                            intent.putExtra(Constants.ID, mData.get(getAdapterPosition()).id);
                            mContext.startActivity(intent);
                            break;
                    }
                    return true;
                }
            });
            popupMenu.show();
        }

        SearchViewHolder(View view) {
            super(view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            AuditionDialogFragment.newInstance(mData.get(position).id)
                    .show(((AppCompatActivity) mContext).getSupportFragmentManager(),
                            Constants.AUDITION_TAG);
        }
    }
}
