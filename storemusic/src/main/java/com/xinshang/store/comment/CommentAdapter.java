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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.xinshang.store.R;
import com.xinshang.store.base.BaseAdapter;
import com.xinshang.store.base.BaseViewHolder;
import com.xinshang.store.data.entities.Comment;
import com.xinshang.store.helper.GlideApp;
import com.xinshang.store.helper.GlideRequest;

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

        mGlideRequest.load(comment).into(holder.mUserImage);
        holder.mComment.setText(comment.comment);
        holder.mUserName.setText(comment.userNickname);
        holder.mTime.setText(comment.commentDate);
        holder.mVoteCount.setText(String.valueOf(comment.voteCount));
    }

    public interface EventListener {
        void onThumbUpClick();
    }

    class CommentViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_user)
        ImageView mUserImage;
        @BindView(R.id.tv_user_name)
        TextView mUserName;
        @BindView(R.id.tv_comment)
        TextView mComment;
        @BindView(R.id.tv_time)
        TextView mTime;
        @BindView(R.id.tv_vote_count)
        TextView mVoteCount;

        CommentViewHolder(View view) {
            super(view);
        }

        @OnClick(R.id.iv_thumb_up)
        void thumbUp() {
            if (mListener != null) {
                mListener.onThumbUpClick();
            }
        }
    }
}
