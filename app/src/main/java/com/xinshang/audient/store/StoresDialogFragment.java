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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.xinshang.audient.AudientApplication;
import com.xinshang.audient.R;
import com.xinshang.audient.base.BaseDialogFragment;
import com.xinshang.audient.model.entities.Store;
import com.xinshang.audient.widget.AudientItemDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by koma on 3/26/18.
 */

public class StoresDialogFragment extends BaseDialogFragment implements StoresConstract.View {
    private static final String DIALOG_TAG = "dialog_stores";

    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar mProgressbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecylerView;

    @Inject
    StoresPresenter mPresenter;

    private StoresAdapter mAdapter;

    public static void show(FragmentManager fragmentManager) {
        StoresDialogFragment fragment = new StoresDialogFragment();
        fragment.show(fragmentManager, DIALOG_TAG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerStoresComponent.builder().audientRepositoryComponent(
                ((AudientApplication) (getActivity().getApplication())).getRepositoryComponent())
                .storesPresenterModule(new StoresPresenterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(false);

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        final View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_stores,
                null, false);
        ButterKnife.bind(this, view);

        builder.setTitle(R.string.store_title);
        builder.setView(view);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        mAdapter = new StoresAdapter(mContext);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecylerView.setLayoutManager(layoutManager);
        mRecylerView.addItemDecoration(new AudientItemDecoration(mContext));
        mRecylerView.setAdapter(mAdapter);

        if (mPresenter != null) {
            mPresenter.subscribe();
        }

        return builder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }

    @Override
    public void setPresenter(StoresConstract.Presenter presenter) {

    }

    @Override
    public boolean isActive() {
        return this.isAdded();
    }

    @Override
    public void showStores(List<Store> stores) {
        mAdapter.replace(stores);
    }

    @Override
    public void showLoadingError() {
        if (getView() == null) {
            return;
        }
        Snackbar.make(getView(), R.string.loading_error_message, Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void setLoadingIndicator(boolean isActive) {
        if (isActive) {
            mProgressbar.show();
        } else {
            mProgressbar.hide();
        }
    }
}
