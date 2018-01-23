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
package com.koma.audient.toplist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koma.audient.R;
import com.koma.audient.helper.GlideApp;
import com.koma.audient.helper.GlideRequest;
import com.koma.audient.model.entities.TopListResult;
import com.koma.audient.toplist.detail.TopListDetailActivity;
import com.koma.common.base.BaseAdapter;
import com.koma.common.base.BaseViewHolder;
import com.koma.common.util.Constants;

import butterknife.BindView;

public class TopListAdapter extends BaseAdapter<TopListResult.TopList, TopListAdapter.TopListViewHolder> {
    private final GlideRequest<Drawable> mGlideRequest;

    public TopListAdapter(Context context) {
        super(context);

        mGlideRequest = GlideApp.with(mContext)
                .asDrawable()
                .dontAnimate()
                .thumbnail(0.1f)
                .placeholder(new ColorDrawable(Color.GRAY));
    }

    @Override
    protected boolean areItemsTheSame(TopListResult.TopList oldItem, TopListResult.TopList newItem) {
        return oldItem.topId == newItem.topId;
    }

    @Override
    protected boolean areContentsTheSame(TopListResult.TopList oldItem, TopListResult.TopList newItem) {
        return oldItem.equals(newItem);
    }

    @Override
    protected Object getChangePayload(TopListResult.TopList oldItem, TopListResult.TopList newItem) {
        return null;
    }

    @Override
    public TopListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_top_list, parent, false);

        return new TopListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TopListViewHolder holder, int position) {
        TopListResult.TopList topList = mData.get(position);
        mGlideRequest.load(topList.picUrl).into(holder.mAlbum);
        holder.mFirstSong.setText(buildString(topList.songBeans.get(0)));
        holder.mSecondSong.setText(buildString(topList.songBeans.get(1)));
        holder.mThirdSong.setText(buildString(topList.songBeans.get(2)));
    }

    private String buildString(TopListResult.TopList.SongBean songBean) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(songBean.musicName);
        stringBuilder.append(" - ");
        stringBuilder.append(songBean.actorName);

        return stringBuilder.toString();
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class TopListViewHolder extends BaseViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_album)
        ImageView mAlbum;
        @BindView(R.id.tv_first_song)
        TextView mFirstSong;
        @BindView(R.id.tv_second_song)
        TextView mSecondSong;
        @BindView(R.id.tv_third_song)
        TextView mThirdSong;

        TopListViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            TopListResult.TopList topList = mData.get(position);
            Intent intent = new Intent(mContext, TopListDetailActivity.class);
            intent.putExtra(Constants.KEY_TOP_ID, topList.topId);
            intent.putExtra(Constants.KEY_top_list_name, topList.listName);
            intent.putExtra(Constants.KEY_SHOW_TIME, topList.showTime);
            intent.putExtra(Constants.KEY_PIC_URL, topList.picUrl);
            mContext.startActivity(intent);
        }
    }
}
