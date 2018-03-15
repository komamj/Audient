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
package com.xinshang.audient.comment;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.xinshang.audient.R;
import com.xinshang.audient.helper.GlideApp;
import com.xinshang.audient.helper.GlideRequest;
import com.xinshang.audient.model.entities.Comment;
import com.xinshang.common.base.BaseAdapter;
import com.xinshang.common.base.BaseViewHolder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class CommentAdapter extends BaseAdapter<Comment, CommentAdapter.CommentViewHolder> {
    private final GlideRequest<Bitmap> mGlideRequest;

    private EventListener mListener;

    public CommentAdapter(Context context) {
        super(context);

        mGlideRequest = GlideApp.with(mContext)
                .asBitmap()
                .thumbnail(0.1f)
                .circleCrop()
                .transition(new BitmapTransitionOptions().crossFade())
                .placeholder(R.drawable.ic_user);
    }

    public void setListener(EventListener listener) {
        this.mListener = listener;
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
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment, parent,
                false);

        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        Comment comment = mData.get(position);

        mGlideRequest.load(comment.avatar).into(holder.mUserImage);
        holder.mMessage.setText(comment.comment);
        String userName = comment.userNickname;
        if (TextUtils.isEmpty(userName)) {
            userName = comment.userName;
        }
        holder.mUserName.setText(userName);
        holder.mTime.setText(comment.commentDate);
        holder.mCount.setText(String.valueOf(comment.voteCount));
        if (comment.voted) {
            holder.mThumbUp.setImageResource(R.drawable.ic_thumb_uped);
        } else {
            holder.mThumbUp.setImageResource(R.drawable.ic_thumb_up_black);
        }
    }

    class CommentViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_user)
        ImageView mUserImage;
        @BindView(R.id.tv_dynamic)
        TextView mUserName;
        @BindView(R.id.tv_comment)
        TextView mMessage;
        @BindView(R.id.tv_time)
        TextView mTime;
        @BindView(R.id.tv_thumb_up_count)
        TextView mCount;
        @BindView(R.id.iv_thumb_up)
        ImageView mThumbUp;

        @OnClick(R.id.iv_thumb_up)
        void thumbUp() {
            Comment comment = mData.get(getAdapterPosition());
            if (mListener != null) {
                mListener.onThumbUpClick(comment);
            }
            if (comment.voted) {
                mThumbUp.setImageResource(R.drawable.ic_thumb_up_black);
                mCount.setText(String.valueOf(comment.voteCount - 1));
                comment.voted = false;
                comment.voteCount -= 1;
            } else {
                mCount.setText(String.valueOf(comment.voteCount + 1));
                mThumbUp.setImageResource(R.drawable.ic_thumb_uped);
                comment.voted = true;
                comment.voteCount += 1;
            }
        }

        CommentViewHolder(View view) {
            super(view);
        }
    }

    public interface EventListener {
        void onThumbUpClick(Comment comment);
    }
}
