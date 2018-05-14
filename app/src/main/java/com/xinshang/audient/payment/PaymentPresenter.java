package com.xinshang.audient.payment;

import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.xinshang.audient.model.AudientRepository;
import com.xinshang.audient.model.entities.ApiResponse;
import com.xinshang.audient.model.entities.Audient;
import com.xinshang.audient.model.entities.BaseResponse;
import com.xinshang.audient.model.entities.Coupon;
import com.xinshang.audient.model.entities.FreeSong;
import com.xinshang.audient.model.entities.Music;
import com.xinshang.audient.model.entities.OrderResponse;
import com.xinshang.audient.model.entities.WXPayRequest;
import com.xinshang.audient.util.WXPayEntryMessageEvent;
import com.xinshang.common.util.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by koma on 3/1/18.
 */

public class PaymentPresenter implements PaymentContract.Presenter {
    private static final String TAG = PaymentPresenter.class.getSimpleName();
    private static final String PAYMENT_WAY = "微信支付";

    private static final float PRICE = 1.00f;

    private final PaymentContract.View mView;

    private final AudientRepository mRepository;

    private final CompositeDisposable mDisposables;

    // private Audient mAudient;

    private String mOrderId;

    private Coupon mCoupon;

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
        loadMyCoupons("available");

        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WXPayEntryMessageEvent messageEvent) {
        this.processWXPayResponse(messageEvent.getResp());
    }

    @Override
    public void unSubscribe() {
        EventBus.getDefault().unregister(this);

        mDisposables.clear();
    }

    @Override
    public void loadMyCoupons(String type) {
        Disposable disposable = mRepository.getMyCoupon(type)
                .map(new Function<ApiResponse<List<Coupon>>, Coupon>() {
                    @Override
                    public Coupon apply(ApiResponse<List<Coupon>> response) {
                        for (Coupon coupon : response.data) {
                            if (coupon != null && !coupon.used) {
                                return coupon;
                            }
                        }
                        return new Coupon();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Coupon>() {
                    @Override
                    public void onNext(Coupon coupon) {
                        mCoupon = coupon;

                        if (mView.isActive()) {
                            mView.setFreeIndicator(coupon.id != null && !coupon.used);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "getMyCoupons error : " + t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mDisposables.add(disposable);
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
        music.price = PRICE;
        music.discount = 0;
        music.discountPrice = PRICE;
        music.paymentWay = PAYMENT_WAY;
        music.free = false;
        music.freeWay = 0;
        Disposable disposable = mRepository.addToPlaylist(music)
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
                        if (mView.isActive()) {
                            mView.setLoadingIndicator(false);

                            mView.showSuccessfullyMessage();
                        }
                    }
                });

        mDisposables.add(disposable);
    }

    @Override
    public void postOrder(Audient audient) {
        // mAudient = audient;

        if (mView.isActive()) {
            mView.setLoadingIndicator(true);
        }

        String storeId = mRepository.getStoreId();

        if (mCoupon != null && mCoupon.id != null && !mCoupon.used) {
            FreeSong freeSong = new FreeSong();
            freeSong.albumId = audient.albumId;
            freeSong.albumName = audient.albumName;
            freeSong.artistId = audient.artistId;
            freeSong.artistName = audient.artistName;
            freeSong.cuponId = mCoupon.id;
            freeSong.mediaId = audient.mediaId;
            freeSong.mediaName = audient.mediaName;
            freeSong.mediaInterval = String.valueOf(audient.duration);
            freeSong.storeId = storeId;
            Disposable disposable = mRepository.postOrderByCoupon(freeSong)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<ApiResponse>() {
                        @Override
                        public void onNext(ApiResponse response) {
                            if (response.resultCode == 0) {
                                LogUtils.e(TAG, "postOrder successful");
                                mView.showSuccessfullyMessage();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            LogUtils.e(TAG, "postOrder error : " + t.getMessage());

                            if (mView.isActive()) {
                                mView.setLoadingIndicator(false);

                                mView.showFailedMessage();
                            }
                        }

                        @Override
                        public void onComplete() {
                            if (mView.isActive()) {
                                mView.setLoadingIndicator(false);
                            }
                        }
                    });
            mDisposables.add(disposable);
        } else {
            WXPayRequest wxPayRequest = new WXPayRequest();
            wxPayRequest.storeId = storeId;
            wxPayRequest.mediaId = audient.mediaId;
            wxPayRequest.mediaName = audient.mediaName;
            wxPayRequest.mediaInterval = String.valueOf(audient.duration);
            wxPayRequest.albumId = audient.albumId;
            wxPayRequest.albumName = audient.albumName;
            wxPayRequest.artistId = audient.artistId;
            wxPayRequest.artistName = audient.artistName;
            wxPayRequest.price = PRICE;

            Disposable disposable = mRepository.postOrder(wxPayRequest)
                    .map(new Function<ApiResponse<OrderResponse>, OrderResponse>() {
                        @Override
                        public OrderResponse apply(ApiResponse<OrderResponse> response) {
                            return response.data;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<OrderResponse>() {
                        @Override
                        public void onNext(OrderResponse orderResponse) {
                            mOrderId = orderResponse.order.id;

                            if (mView.isActive()) {
                                mRepository.sendWXPayRequest(orderResponse.payRequestInfo);
                            }
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

    @Override
    public void processWXPayResponse(BaseResp response) {
        LogUtils.i(TAG, "onResp " + response.errCode);

        switch (response.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                postOrderResult(null, mOrderId);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                break;
            default:
                break;
        }
    }

    @Override
    public void postOrderResult(String tid, String oid) {
        mRepository.getOrderResult(tid, oid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (mView.isActive() && response.resultCode == 0) {
                            mView.showSuccessfullyMessage();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "postOrderResult");
                    }

                    @Override
                    public void onComplete() {
                        if (mView.isActive()) {
                            mView.setLoadingIndicator(false);
                        }
                    }
                });
    }
}
