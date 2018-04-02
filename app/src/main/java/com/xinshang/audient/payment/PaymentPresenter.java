package com.xinshang.audient.payment;

import com.xinshang.audient.model.AudientRepository;
import com.xinshang.audient.model.entities.Audient;
import com.xinshang.audient.model.entities.BaseResponse;
import com.xinshang.audient.model.entities.Music;
import com.xinshang.audient.model.entities.WXPayRequest;
import com.xinshang.common.util.LogUtils;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by koma on 3/1/18.
 */

public class PaymentPresenter implements PaymentContract.Presenter {
    private static final String TAG = PaymentPresenter.class.getSimpleName();

    private final PaymentContract.View mView;
    private final AudientRepository mRepository;
    private final CompositeDisposable mDisposables;

    @Inject
    public PaymentPresenter(PaymentContract.View view, AudientRepository repository) {
        mView = view;
        mRepository = repository;
        mDisposables = new CompositeDisposable();
    }

    @Inject
    void setUpListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mDisposables.clear();
    }

    @Override
    public void addToPlaylist(Audient audient) {
        String storeId = mRepository.getStoreId();
        Music music = new Music();
        music.storeId = storeId;
        music.mediaId = audient.mediaId;
        music.mediaName = audient.mediaName;
        music.mediaInterval = String.valueOf(audient.duration);
        music.artistId = audient.artistId;
        music.artistName = audient.artistName;
        music.albumId = audient.albumId;
        music.albumName = audient.albumName;
        music.price = 1;
        music.discount = 0;
        music.discountPrice = 1;
        music.paymentWay = "微信支付";
        music.free = false;
        music.freeWay = 0;
        mRepository.addToPlaylist(music)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.resultCode == 0) {
                            LogUtils.i(TAG, "addToPlaylist successful");
                        } else {

                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "addToPlaylist error : " + t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void postOrder(Audient audient) {
        WXPayRequest wxPayRequest = new WXPayRequest();
        wxPayRequest.mediaId = audient.mediaId;
        wxPayRequest.mediaName = audient.mediaName;
        wxPayRequest.mediaInterval = String.valueOf(audient.duration);
        wxPayRequest.albumId = audient.albumId;
        wxPayRequest.albumName = audient.albumName;
        wxPayRequest.artistId = audient.artistId;
        wxPayRequest.artistName = audient.artistName;
        wxPayRequest.price = 1.00f;
        wxPayRequest.paymentWay = 1;
        wxPayRequest.clientIP = "";

        Disposable disposable = mRepository.postOrder(wxPayRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {

                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "postOrder error : " + t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mDisposables.add(disposable);
    }
}
