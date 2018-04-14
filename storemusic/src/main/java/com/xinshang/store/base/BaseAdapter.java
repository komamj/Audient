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

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public abstract class BaseAdapter<T, V extends BaseViewHolder> extends RecyclerView.Adapter<V> {
    protected final Context mContext;

    @Nullable
    protected List<T> mData;

    public BaseAdapter(Context context) {
        mContext = context;
    }

    public void update(final List<T> update) {
        mData = update;

        notifyDataSetChanged();
    }

    public void appendData(final List<T> data) {
        if (mData == null || data == null || data.isEmpty()) {
            return;
        }

        int positionStart = mData.size();

        mData.addAll(data);

        notifyItemRangeInserted(positionStart, data.size());
    }

    @SuppressLint("StaticFieldLeak")
    @MainThread
    public void replace(final List<T> update) {
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
            final List<T> oldItems = mData;
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
                            T oldItem = oldItems.get(oldItemPosition);
                            T newItem = update.get(newItemPosition);
                            return BaseAdapter.this.areItemsTheSame(oldItem, newItem);
                        }

                        @Override
                        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                            T oldItem = oldItems.get(oldItemPosition);
                            T newItem = update.get(newItemPosition);
                            return BaseAdapter.this.areContentsTheSame(oldItem, newItem);
                        }

                        @Nullable
                        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                            T oldItem = oldItems.get(oldItemPosition);
                            T newItem = update.get(newItemPosition);
                            return BaseAdapter.this.getChangePayload(oldItem, newItem);
                        }
                    });
                }

                @Override
                protected void onPostExecute(DiffUtil.DiffResult diffResult) {
                    mData = update;
                    diffResult.dispatchUpdatesTo(BaseAdapter.this);

                }
            }.execute();
        }
    }

    protected abstract boolean areItemsTheSame(T oldItem, T newItem);

    protected abstract boolean areContentsTheSame(T oldItem, T newItem);

    protected abstract Object getChangePayload(T oldItem, T newItem);

    public List<T> getData() {
        return this.mData;
    }

    public T getDataForPosition(int position) {
        return mData.get(position);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }
}
