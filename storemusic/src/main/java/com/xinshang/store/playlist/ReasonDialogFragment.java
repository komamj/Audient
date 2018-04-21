package com.xinshang.store.playlist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.xinshang.store.R;
import com.xinshang.store.StoreMusicApplication;
import com.xinshang.store.base.BaseDialogFragment;
import com.xinshang.store.data.entities.MessageEvent;
import com.xinshang.store.utils.Constants;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReasonDialogFragment extends BaseDialogFragment implements ReasonContract.View {
    private static final String TAG = "ReasonDialogFragment";

    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @Inject
    ReasonPresenter mPresenter;

    private String mId;

    @OnClick(R.id.tv_ok)
    void doConfirm() {
        RadioButton radioButton = mRadioGroup.findViewById(mRadioGroup.getCheckedRadioButtonId());
        String reason = radioButton.getText().toString();

        if (mPresenter != null) {
            mPresenter.deleteStoreSong(mId, reason);
        }
    }

    @OnClick(R.id.tv_cancel)
    void doCancel() {
        this.dismiss();
    }


    public static void showReasonDialog(FragmentManager fragmentManager, String id) {
        ReasonDialogFragment reasonDialogFragment = new ReasonDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_STORE_SONG_ID, id);
        reasonDialogFragment.setArguments(bundle);

        reasonDialogFragment.show(fragmentManager, TAG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.AuditionDilogTheme);

        if (getArguments() != null) {
            mId = getArguments().getString(Constants.KEY_STORE_SONG_ID);
        }

        // inject presenter
        DaggerReasonComponent.builder()
                .audientRepositoryComponent(((StoreMusicApplication) getActivity().getApplication()).getRepositoryComponent())
                .reasonPresenterModule(new ReasonPresenterModule(this))
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reason_dialog_fragment, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }

    @Override
    public void setPresenter(ReasonContract.Presenter presenter) {

    }

    @Override
    public boolean isActive() {
        return this.isAdded();
    }

    @Override
    public void setLoadingIndicator(boolean isActive) {
        if (isActive) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void dismissReasonDialogFragment() {
        EventBus.getDefault().post(new MessageEvent(Constants.MESSAGE_PLAYLIST_CHANGED));

        this.dismiss();
    }
}
