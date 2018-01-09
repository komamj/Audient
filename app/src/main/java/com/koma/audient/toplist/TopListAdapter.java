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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.koma.audient.helper.GlideApp;
import com.koma.audient.helper.GlideRequest;
import com.koma.audient.model.entities.TopList;
import com.koma.common.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class TopListAdapter extends RecyclerView.Adapter<TopListAdapter.TopListViewHolder> {
    private final GlideRequest<Drawable> mGlideRequest;

    private List<TopList> mData;

    private final Context mContext;

    public TopListAdapter(Context context) {
        mContext = context;

        mGlideRequest = GlideApp.with(mContext)
                .asDrawable()
                .centerCrop()
                .placeholder(new ColorDrawable(Color.GRAY))
                .thumbnail(0.1f);
    }

    public void updateData(List<TopList> data) {
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
    public TopListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(TopListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class TopListViewHolder extends BaseViewHolder {
        public TopListViewHolder(View itemView) {
            super(itemView);
        }
    }
}
