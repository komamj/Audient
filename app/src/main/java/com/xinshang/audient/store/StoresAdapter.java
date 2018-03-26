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
package com.xinshang.audient.store;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xinshang.audient.R;
import com.xinshang.audient.model.entities.Store;
import com.xinshang.common.base.BaseAdapter;
import com.xinshang.common.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;

/**
 * Created by koma on 3/26/18.
 */

public class StoresAdapter extends BaseAdapter<Store, StoresAdapter.StoresVH> {
    private static final String TAG = StoresAdapter.class.getSimpleName();

    private OnCheckedChangeListener mListener;

    public StoresAdapter(Context context) {
        super(context);
    }

    public void setListener(OnCheckedChangeListener listener) {
        this.mListener = listener;
    }

    @Override
    protected boolean areItemsTheSame(Store oldItem, Store newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(Store oldItem, Store newItem) {
        return false;
    }

    @Override
    protected Object getChangePayload(Store oldItem, Store newItem) {
        return null;
    }

    @NonNull
    @Override
    public StoresVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_store, parent, false);
        return new StoresVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoresVH holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull StoresVH holder, int position) {
        bind(mData.get(position), holder);
    }

    private void bind(Store store, StoresVH viewHolder) {
        viewHolder.mName.setText(store.name);
        viewHolder.mAddress.setText(store.address);
    }

    public class StoresVH extends BaseViewHolder {
        @BindView(R.id.tv_name)
        TextView mName;
        @BindView(R.id.tv_address)
        TextView mAddress;
        @BindView(R.id.cb_confirm)
        CheckBox mConfirm;

        @OnCheckedChanged(R.id.cb_confirm)
        void onCheckedChange() {
            if (mListener != null) {
                mListener.onCheckedChange();
            }
        }

        public StoresVH(View view) {
            super(view);
        }
    }

    public interface OnCheckedChangeListener {
        void onCheckedChange();
    }
}
