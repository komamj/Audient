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
package com.xinshang.store.mine;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.xinshang.store.R;
import com.xinshang.store.StoreMusicApplication;
import com.xinshang.store.utils.LogUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddFavoritesDialog extends DialogFragment implements AddFavoriteContract.View {
    private static final String TAG = AddFavoritesDialog.class.getSimpleName();

    private static final String TAG_ADD_PLAYLIST = "tag_add_playlist";

    @BindView(R.id.edit_text)
    EditText mEditText;
    @Inject
    AddFavoritePresenter mPresenter;
    private Context mContext;

    public static void show(FragmentManager fm) {
        final AddFavoritesDialog dialog = new AddFavoritesDialog();
        dialog.show(fm, TAG_ADD_PLAYLIST);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.i(TAG, "onCreate");

        // inject presenter
        DaggerAddFavoriteComponent.builder()
                .audientRepositoryComponent(((StoreMusicApplication) getActivity()
                        .getApplication()).getRepositoryComponent())
                .addFavoritePresenterModule(new AddFavoritePresenterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        final View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit_text,
                null, false);
        ButterKnife.bind(this, view);

        mEditText.setHint(R.string.new_playlist_title);

        builder.setTitle(R.string.add_playlist);
        builder.setView(view);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mPresenter != null) {
                    mPresenter.addFavorite(mEditText.getText().toString());
                }
            }
        });

        builder.setNegativeButton(android.R.string.cancel, null);

        return builder.create();
    }

    @Override
    public void setPresenter(AddFavoriteContract.Presenter presenter) {

    }
}
