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
package com.koma.audient.toplist.detail;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.koma.audient.model.entities.ToplistDetailResult;
import com.koma.common.base.BaseAdapter;
import com.koma.common.base.BaseViewHolder;

public class ToplistDetailAdapter extends BaseAdapter<ToplistDetailResult.ToplistDetail, ToplistDetailAdapter.ToplistDetailViewHolder> {
    public ToplistDetailAdapter(Context context) {
        super(context);
    }

    @Override
    protected boolean areItemsTheSame(ToplistDetailResult.ToplistDetail oldItem, ToplistDetailResult.ToplistDetail newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(ToplistDetailResult.ToplistDetail oldItem, ToplistDetailResult.ToplistDetail newItem) {
        return false;
    }

    @Override
    protected Object getChangePayload(ToplistDetailResult.ToplistDetail oldItem, ToplistDetailResult.ToplistDetail newItem) {
        return null;
    }

    @Override
    public ToplistDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ToplistDetailViewHolder holder, int position) {

    }

    class ToplistDetailViewHolder extends BaseViewHolder {
        public ToplistDetailViewHolder(View view) {
            super(view);
        }
    }
}
