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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koma.audient.R;
import com.koma.audient.dialog.audition.AuditionDialogFragment;
import com.koma.audient.helper.GlideApp;
import com.koma.audient.helper.GlideRequest;
import com.koma.audient.model.entities.Audient;
import com.koma.common.base.BaseViewHolder;
import com.koma.common.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private static final String TAG = SearchAdapter.class.getSimpleName();

    private final GlideRequest<Drawable> mGlideRequest;

    private List<Audient> mData;

    private final Context mContext;

    public SearchAdapter(Context context) {
        mContext = context;

        mGlideRequest = GlideApp.with(mContext)
                .asDrawable()
                .centerCrop()
                .placeholder(new ColorDrawable(Color.GRAY))
                .thumbnail(0.1f);
    }

    public void updateData(List<Audient> data) {
        if (mData == null) {
            mData = new ArrayList<>();

            mData = data;

            int itemCount = mData.size();

            notifyItemRangeInserted(0, itemCount);
        } else {
            int positionStart = mData.size();
            int itemCount = data.size();

            mData.addAll(data);

            notifyItemRangeInserted(positionStart, itemCount);
        }
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_search,
                parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        mGlideRequest.load(mData.get(position).albumUrl)
                .into(holder.mAlbum);
        holder.mMusicName.setText(mData.get(position).musicName);
        holder.mActorName.setText(mData.get(position).actorName);
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

        @OnClick(R.id.iv_more)
        void showPopupView() {

        }

        SearchViewHolder(View view) {
            super(view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            Audient audient = mData.get(position);

            AuditionDialogFragment.newInstance(audient)
                    .show(((AppCompatActivity) mContext).getSupportFragmentManager(),
                            Constants.AUDITION_TAG);
        }
    }
}
