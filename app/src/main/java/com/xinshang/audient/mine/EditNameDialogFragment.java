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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.xinshang.audient.AudientApplication;
import com.xinshang.audient.R;
import com.xinshang.audient.base.BaseDialogFragment;
import com.xinshang.audient.model.entities.Favorite;
import com.xinshang.common.util.Constants;
import com.xinshang.common.util.LogUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditNameDialogFragment extends BaseDialogFragment implements EditNameContract.View {
    private static final String TAG = EditNameDialogFragment.class.getSimpleName();

    private static final String DIALOG_TAG = "edit_name_dialog";

    @BindView(R.id.edit_text)
    EditText mEditText;

    private Favorite mFavorite;

    @Inject
    EditNamePresenter mPresenter;

    public static void show(FragmentManager fragmentManager, Favorite favorite) {
        EditNameDialogFragment dialogFragment = new EditNameDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.KEY_FAVORITE, favorite);

        dialogFragment.setArguments(bundle);

        dialogFragment.show(fragmentManager, DIALOG_TAG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.i(TAG, "onCreate");

        if (getArguments() != null) {
            mFavorite = getArguments().getParcelable(Constants.KEY_FAVORITE);
        }

        // inject presenter
        DaggerEditNameComponent.builder()
                .audientRepositoryComponent(((AudientApplication) getActivity()
                        .getApplication()).getRepositoryComponent())
                .editNamePresenterModule(new EditNamePresenterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        final View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit_text,
                null, false);
        ButterKnife.bind(this, view);

        mEditText.setText(mFavorite.favoriteName);
        mEditText.setSelection(0, mFavorite.favoriteName.length());

        builder.setTitle(R.string.modify_name);
        builder.setView(view);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mPresenter != null) {
                    mPresenter.modifyFavoritesName(mFavorite.favoritesId,
                            mEditText.getText().toString());
                }
            }
        });

        builder.setNegativeButton(android.R.string.cancel, null);

        return builder.create();
    }

    @Override
    public void setPresenter(EditNameContract.Presenter presenter) {

    }
}
