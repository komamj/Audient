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

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koma.audient.R;
import com.koma.audient.helper.GlideApp;
import com.koma.audient.helper.GlideRequest;
import com.koma.audient.model.entities.Audient;
import com.koma.common.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {
    private final Context mContext;

    private final GlideRequest<Drawable> mGlideRequest;

    private List<Audient> mData;

    public PlaylistAdapter(Context context) {
        mContext = context;

        mGlideRequest = GlideApp.with(mContext)
                .asDrawable()
                .circleCrop()
                .placeholder(new ColorDrawable(Color.GRAY));
    }

    @SuppressLint("StaticFieldLeak")
    @MainThread
    public void replace(final List<Audient> update) {
        if (mData == null) {
            if (update == null) {
                return;
            }
            mData = update;
            notifyItemRangeInserted(0, update.size());
        } else if (update == null) {
            int oldSize = mData.size();
            mData = null;
            notifyItemRangeRemoved(0, oldSize);
        } else {
            final List<Audient> oldItems = mData;
            new AsyncTask<Void, Void, DiffUtil.DiffResult>() {
                @Override
                protected DiffUtil.DiffResult doInBackground(Void... voids) {
                    return DiffUtil.calculateDiff(new DiffUtil.Callback() {
                        @Override
                        public int getOldListSize() {
                            return oldItems.size();
                        }

                        @Override
                        public int getNewListSize() {
                            return update.size();
                        }

                        @Override
                        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                            Audient oldItem = oldItems.get(oldItemPosition);
                            Audient newItem = update.get(newItemPosition);
                            return false;
                        }

                        @Override
                        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                            Audient oldItem = oldItems.get(oldItemPosition);
                            Audient newItem = update.get(newItemPosition);
                            return false;
                        }

                        @Nullable
                        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                            Audient oldItem = oldItems.get(oldItemPosition);
                            Audient newItem = update.get(newItemPosition);
                            return false;
                        }
                    });
                }

                @Override
                protected void onPostExecute(DiffUtil.DiffResult diffResult) {
                    mData = update;
                    diffResult.dispatchUpdatesTo(PlaylistAdapter.this);

                }
            }.execute();
        }
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
        if (position == 0) {
            holder.mEqualizer.setVisibility(View.VISIBLE);
        } else {
            holder.mEqualizer.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    static class PlaylistViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_album)
        ImageView mAlbum;
        @BindView(R.id.tv_music_name)
        TextView mMusicName;
        @BindView(R.id.tv_actor_name)
        TextView mActorName;
        @BindView(R.id.ic_equalizer)
        ImageView mEqualizer;

        PlaylistViewHolder(View itemView) {
            super(itemView);
        }
    }
}
