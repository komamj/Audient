package com.xinshang.store.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xinshang.store.R;
import com.xinshang.store.base.AudientAdapter;
import com.xinshang.store.base.BaseFragment;
import com.xinshang.store.data.entities.AlbumResponse;
import com.xinshang.store.data.entities.Song;
import com.xinshang.store.favorite.MyFavoritesActivity;
import com.xinshang.store.playlist.ConfirmDialog;
import com.xinshang.store.utils.Constants;
import com.xinshang.store.utils.LogUtils;
import com.xinshang.store.widget.AudientItemDecoration;

import java.util.List;

import butterknife.BindView;

/**
 * Created by koma on 4/13/18.
 */

public class AlbumDetailFragment extends BaseFragment implements AlbumDetailContract.View {
    private static final String TAG = PlaylistsDetailFragment.class.getSimpleName();

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private AudientAdapter mAdapter;

    private AlbumDetailContract.Presenter mPresenter;

    private AlbumResponse.Album mAlbum;

    public static AlbumDetailFragment newInstance(AlbumResponse.Album album) {
        AlbumDetailFragment fragment = new AlbumDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.KEY_ALBUMS, album);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mAlbum = getArguments().getParcelable(Constants.KEY_ALBUMS);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LogUtils.i(TAG, "onViewCreated");

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPresenter != null) {
                    mPresenter.loadAlbumSongs(mAlbum);
                }
            }
        });

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark,
                R.color.colorPrimary);

        setLoadingIndicator(true);

        mAdapter = new AudientAdapter(mContext);
        mAdapter.setEventListener(new AudientAdapter.EventListener() {
            @Override
            public void onFavoriteMenuClick(Song audient) {
                Intent intent = new Intent(mContext, MyFavoritesActivity.class);
                intent.putExtra(Constants.KEY_AUDIENT, audient);
                mContext.startActivity(intent);
            }

            @Override
            public void onPlaylistChanged(final Song audient) {
                ConfirmDialog.showConfirmDialog(getChildFragmentManager(),
                        new ConfirmDialog.OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                if (mPresenter != null) {
                                    mPresenter.addToPlaylist(audient);
                                }
                            }
                        }, mContext.getString(R.string.action_playlist));
            }
        });

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new AudientItemDecoration(mContext));
        mRecyclerView.setAdapter(mAdapter);

        if (mPresenter != null) {
            mPresenter.loadAlbumSongs(mAlbum);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        LogUtils.i(TAG, "onDestroyView");

        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_base;
    }

    @Override
    public boolean isActive() {
        return this.isAdded();
    }

    @Override
    public void setLoadingIndicator(final boolean isActive) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(isActive);
            }
        });
    }

    @Override
    public void showSongs(List<Song> songs) {
        mAdapter.replace(songs);
    }

    @Override
    public void setPresenter(AlbumDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
