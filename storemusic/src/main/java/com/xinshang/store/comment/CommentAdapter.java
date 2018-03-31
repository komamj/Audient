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
package com.xinshang.store.comment;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinshang.store.R;
import com.xinshang.store.base.BaseAdapter;
import com.xinshang.store.base.BaseViewHolder;
import com.xinshang.store.data.entities.Comment;
import com.xinshang.store.data.entities.CommentDataBean;
import com.xinshang.store.helper.GlideApp;
import com.xinshang.store.helper.GlideRequest;

import java.util.ArrayList;

import butterknife.BindView;

public class CommentAdapter extends BaseAdapter<Comment, CommentAdapter.CommentViewHolder> {
    private static final int VIEW_TYPE_NORMAL = 0;
    private static final int VIEW_TYPE_HEADER = 1;

    private final GlideRequest<Bitmap> mGlideRequest;

    private CommentDataBean mCommentDataBean;

    public CommentAdapter(Context context) {
        super(context);

        mGlideRequest = GlideApp.with(mContext)
                .asBitmap()
                .thumbnail(0.1f)
                .circleCrop()
                .placeholder(R.drawable.ic_user);
    }

    public void setCommentDataBean(CommentDataBean commentDataBean) {
        mCommentDataBean = commentDataBean;
    }

    public void addComment(Comment comment) {
        if (mData == null) {
            mData = new ArrayList<>();
            mData.add(comment);

            notifyItemInserted(0);
        } else {
            mData.add(comment);

            notifyItemInserted(mData.size());
        }
    }

    @Override
    protected boolean areItemsTheSame(Comment oldItem, Comment newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(Comment oldItem, Comment newItem) {
        return false;
    }

    @Override
    protected Object getChangePayload(Comment oldItem, Comment newItem) {
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (!mCommentDataBean.inStoreComment.comments.isEmpty()) {
            int size = mCommentDataBean.inStoreComment.comments.size();
            if (position == 0 || position == size) {
                return VIEW_TYPE_HEADER;
            }
        }
        return VIEW_TYPE_NORMAL;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment_header, parent,
                    false);

            return new CommentHeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment, parent,
                    false);

            return new CommentViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        Comment comment = mData.get(position);

        if (getItemViewType(position) == VIEW_TYPE_HEADER) {
            if (position == 0) {
                ((CommentHeaderViewHolder) holder).mTitle.setText(
                        mContext.getString(R.string.store_comment_description));
            } else {
                ((CommentHeaderViewHolder) holder).mTitle.setText(
                        mContext.getString(R.string.all_comment_description));
            }
        }

        mGlideRequest.load(comment.avatar).into(holder.mUserImage);
        holder.mMessage.setText(comment.comment);
        String userName = comment.userNickname;
        if (TextUtils.isEmpty(userName)) {
            userName = comment.userName;
        }
        holder.mUserName.setText(userName);
        holder.mStoreName.setText(comment.storeName);
        holder.mTime.setText(comment.commentDate);
        holder.mCount.setText(String.valueOf(comment.voteCount));
    }

    class CommentViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_user)
        ImageView mUserImage;
        @BindView(R.id.tv_user_name)
        TextView mUserName;
        @BindView(R.id.tv_store_name)
        TextView mStoreName;
        @BindView(R.id.tv_comment)
        TextView mMessage;
        @BindView(R.id.tv_time)
        TextView mTime;
        @BindView(R.id.tv_thumb_up_count)
        TextView mCount;

        CommentViewHolder(View view) {
            super(view);
        }
    }

    class CommentHeaderViewHolder extends CommentViewHolder {
        @BindView(R.id.tv_title)
        TextView mTitle;

        CommentHeaderViewHolder(View view) {
            super(view);
        }
    }
}
