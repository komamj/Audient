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
package com.xinshang.audient.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.xinshang.audient.AudientApplication;
import com.xinshang.audient.R;
import com.xinshang.audient.base.AudientAdapter;
import com.xinshang.audient.favorite.MyFavoritesActivity;
import com.xinshang.audient.model.entities.Audient;
import com.xinshang.audient.model.entities.Favorite;
import com.xinshang.audient.model.entities.MessageEvent;
import com.xinshang.audient.payment.PaymentDialogFragment;
import com.xinshang.audient.widget.AudientItemDecoration;
import com.xinshang.common.base.BaseFragment;
import com.xinshang.common.util.Constants;
import com.xinshang.common.util.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class MineFragment extends BaseFragment implements MineContract.View {
    private static final String TAG = MineFragment.class.getSimpleName();

    @BindView(R.id.recycler_view_favorite)
    RecyclerView mRecyclerViewFavorite;
    @BindView(R.id.recycler_view_dynamic)
    RecyclerView mRecyclerViewUser;
    @BindView(R.id.progress_bar_favorite)
    ContentLoadingProgressBar mFavoriteProgressBar;
    @BindView(R.id.progress_bar_user)
    ContentLoadingProgressBar mUserProgressBar;

    @OnClick(R.id.iv_add_playlist)
    void showAddPlaylistDilog() {
        AddFavoritesDialog.show(getChildFragmentManager());
    }

    private boolean mIsPrepared;

    private AudientAdapter mDynamicAdapter;

    private FavoriteAdapter mFavoriteAdapter;

    @Inject
    MinePresenter mPresenter;

    public MineFragment() {
    }

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && mIsPrepared) {
            if (mPresenter != null) {
                mPresenter.subscribe();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.i(TAG, "onCreate");

        DaggerMineComponent.builder()
                .audientRepositoryComponent(
                        (((AudientApplication) getActivity().getApplication()).getRepositoryComponent()))
                .minePresenterModule(new MinePresenterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LogUtils.i(TAG, "onViewCreated");

        mDynamicAdapter = new AudientAdapter(mContext);
        mDynamicAdapter.setEventListener(new AudientAdapter.EventListener() {
            @Override
            public void onFavoriteMenuClick(Audient audient) {
                Intent intent = new Intent(mContext, MyFavoritesActivity.class);
                intent.putExtra(Constants.KEY_AUDIENT, audient);
                mContext.startActivity(intent);
            }

            @Override
            public void onPlaylistChanged(Audient audient) {
                PaymentDialogFragment.show(getChildFragmentManager(), audient);
            }
        });

        mFavoriteAdapter = new FavoriteAdapter(mContext);
        mFavoriteAdapter.setListener(new FavoriteAdapter.EventListner() {
            @Override
            public void onModifyEventChange(Favorite favorite) {
                EditNameDialogFragment.show(getChildFragmentManager(), favorite);
            }

            @Override
            public void onDeleteEventChange(Favorite favorite) {
                if (mPresenter != null) {
                    mPresenter.deleteMyFavorite(favorite);
                }
            }
        });

        LinearLayoutManager layoutManagerFavorite = new LinearLayoutManager(mContext);
        layoutManagerFavorite.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewFavorite.setLayoutManager(layoutManagerFavorite);
        mRecyclerViewFavorite.setAdapter(mFavoriteAdapter);

        LinearLayoutManager layoutManagerUser = new LinearLayoutManager(mContext);
        layoutManagerUser.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewUser.setLayoutManager(layoutManagerUser);
        mRecyclerViewUser.addItemDecoration(new AudientItemDecoration(mContext));
        mRecyclerViewUser.setAdapter(mDynamicAdapter);

        mIsPrepared = true;
    }

    @Override
    public void onStart() {
        super.onStart();

        LogUtils.i(TAG, "onStart");

        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        LogUtils.i(TAG, "onMessageEvent");

        if (TextUtils.equals(messageEvent.getMessage(), Constants.MESSAGE_MY_FAVORITES_CHANGED)) {
            if (mPresenter != null) {
                mPresenter.loadFavorites();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        LogUtils.i(TAG, "onStop");

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LogUtils.i(TAG, "onDestroy");

        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void setPresenter(MineContract.Presenter presenter) {

    }

    @Override
    public boolean isActive() {
        return this.isAdded();
    }

    @Override
    public void showLoadingError() {

    }

    @Override
    public void showEmpty(boolean forceShow) {

    }

    @Override
    public void showFavoriteProgressBar(boolean forceShow) {
        if (forceShow) {
            mFavoriteProgressBar.show();
        } else {
            mFavoriteProgressBar.hide();
        }
    }

    @Override
    public void showUserProgressBar(boolean forceShow) {
        if (forceShow) {
            mUserProgressBar.show();
        } else {
            mUserProgressBar.hide();
        }
    }

    @Override
    public void showFavorites(List<Favorite> favorites) {
        mFavoriteAdapter.replace(favorites);
    }

    @Override
    public void showDynamics(List<Audient> audients) {
        mDynamicAdapter.replace(audients);
    }
}
