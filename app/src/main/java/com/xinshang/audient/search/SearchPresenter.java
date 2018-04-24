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
package com.xinshang.audient.search;

import android.text.TextUtils;

import com.xinshang.audient.model.AudientRepository;
import com.xinshang.audient.model.entities.Audient;
import com.xinshang.audient.model.entities.SearchResult;
import com.xinshang.common.util.LogUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class SearchPresenter implements SearchContract.Presenter {
    public static final String TAG = SearchPresenter.class.getSimpleName();

    private SearchContract.View mView;

    private AudientRepository mRepository;

    private CompositeDisposable mDisposables;

    private Set<String> mBlacklist = new HashSet<>();

    private int mPage = 0;

    @Inject
    public SearchPresenter(SearchContract.View view, AudientRepository repository) {
        mView = view;

        mRepository = repository;

        mDisposables = new CompositeDisposable();

        mBlacklist.addAll(Arrays.asList("DJ/舞曲/DISCO/迪斯科/卡拉/OK/蹦/劲爆/舞/DANCE/节奏/嗨/HIGH/REMIX/MIX/摇/DOWNTEMPO/滚/ROCK/说唱/RAP/嘻哈/动次/打次/打碟/电音/控/午夜/打/敲/BOX/哀/葬/FUNERAL/棺/蛊/灵/魂/SOUL/诅/咒/呪/亡/凶/噩/喊麦/MC/神曲/串烧/畜/鬼/朋克/PUNK/金属/METAL/雷鬼/REGGAE/恐怖/惊悚/TERROR/HORRIBLE/思春/性爱/广场/大妈/二人/戏/曲/剧/腔/调/奏/鸣/二胡/唢呐/进行/解放军/民/红/军/国/兵/警察/公安/疆/藏/抗战/抗日/救国/义勇军进行曲/党/共产/主义/中国/太平/盛世/新时代/中国梦/复兴/崛起/族/雷/歌/东北/佛教/宗教/咒/经/诵/释/法师/道长/尼姑/活佛/施主/祷告/祈祷/真主/耶稣/基督/伊斯兰/BLESS/GOD/综艺/歌手/梦想的声音/季/期/春晚/卫视/铃声/选秀/游戏/原创/合唱/歌/学/呻/吟/叫/联唱/连唱/style/川话/方言/高音/年会/节目/电台/朗/龙/党".split("/")));
    }

    @Inject
    void setUpListener() {
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        LogUtils.i(TAG, "subscribe");
    }

    @Override
    public void unSubscribe() {
        LogUtils.i(TAG, "unSubscribe");

        mDisposables.clear();
    }

    @Override
    public void loadSongs(String keyword) {
        LogUtils.i(TAG, "loadSongs :" + keyword);

        mDisposables.clear();

        mPage = 0;

        if (isInvalid(keyword)) {
            if (mView.isActive()) {
                mView.setLoadingIndicator(false);
                mView.showEmpty(true);
            }

            return;
        }

        if (mView != null) {
            mView.setLoadingIndicator(true);
        }

        Disposable disposable = mRepository.searchSongs(keyword, mPage, 20)
                .map(new Function<SearchResult, List<Audient>>() {
                    @Override
                    public List<Audient> apply(SearchResult searchResult) throws Exception {
                        return searchResult.dataBean.audients;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Audient>>() {
                    @Override
                    public void onNext(List<Audient> audients) {
                        if (mView.isActive()) {
                            mView.setLoadingIndicator(false);
                            mView.showAudients(audients);

                            mView.showEmpty(audients.isEmpty());
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "loadSongs error :" + t.toString());

                        if (mView.isActive()) {
                            mView.setLoadingIndicator(false);

                            mView.showLoadingError();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        mDisposables.add(disposable);
    }

    @Override
    public void loadNextPageSongs(String keyWord) {
        if (TextUtils.isEmpty(keyWord)) {
            return;
        }

        mDisposables.clear();

        if (mView.isActive()) {
            mView.setLoadingMoreIndicator(true);
        }

        Disposable disposable = mRepository.searchSongs(keyWord, mPage + 1, 20)
                .map(new Function<SearchResult, List<Audient>>() {
                    @Override
                    public List<Audient> apply(SearchResult searchResult) throws Exception {
                        return searchResult.dataBean.audients;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Audient>>() {
                    @Override
                    public void onNext(List<Audient> songs) {
                        if (mView.isActive()) {
                            if (songs.isEmpty()) {
                                mView.showNoMoreMessage();
                            } else {
                                ++mPage;

                                mView.showNextPageSongs(songs);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (mView.isActive()) {
                            mView.setLoadingMoreIndicator(false);
                            mView.showLoadingError();
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mView.isActive()) {
                            mView.setLoadingMoreIndicator(false);
                        }
                    }
                });
        mDisposables.add(disposable);
    }

    @Override
    public void demand(Audient audient) {
        if (mView.isActive()) {
            if (mRepository.isFirstDemand()) {
                mView.showHintDialog(audient);
            } else {
                mView.showPaymentDialog(audient);
            }
        }
    }

    private boolean isInvalid(String word) {
        if (TextUtils.isEmpty(word)) {
            return true;
        }

        String keyword = word.trim().toUpperCase();

        return mBlacklist.contains(keyword);
    }
}
