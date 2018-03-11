package com.xinshang.store.data.source.remote;

import com.xinshang.store.data.entities.BaseResponse;
import com.xinshang.store.data.entities.CommentResult;
import com.xinshang.store.data.entities.FavoriteListResult;
import com.xinshang.store.data.entities.FavoritesResult;
import com.xinshang.store.data.entities.FileResult;
import com.xinshang.store.data.entities.LyricResult;
import com.xinshang.store.data.entities.NowPlayingResponse;
import com.xinshang.store.data.entities.SearchResult;
import com.xinshang.store.data.entities.SongDetailResult;
import com.xinshang.store.data.entities.StoreKeeper;
import com.xinshang.store.data.entities.StoreKeeperResponse;
import com.xinshang.store.data.entities.TencentMusic;
import com.xinshang.store.data.entities.Token;
import com.xinshang.store.data.entities.ToplistDetailResult;
import com.xinshang.store.data.entities.ToplistResult;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by koma on 3/6/18.
 */

public interface IRemoteDataSource {
    Flowable<StoreKeeperResponse> getStoreKeeperInfo();

    Flowable<List<ToplistResult>> getTopList();

    Flowable<ToplistDetailResult> getToplistDetail(int topId, String showTime, int page, int count);

    Flowable<SearchResult> getSearchReult(String keyword, int page, int count);

    Flowable<LyricResult> getLyricResult(String id);

    Flowable<SongDetailResult> getSongDetailResult(String id);

    Flowable<FileResult> getFileResult(String id);

    Flowable<NowPlayingResponse> getNowPlaying(String storeId);

    Flowable<CommentResult> getCommentResult(String id, int page, int count, String sortord);

    Flowable<BaseResponse> addToFavorite(String favoriteId, TencentMusic audient);

    Flowable<FavoritesResult> getFavoriteResult();

    Flowable<FavoriteListResult> getFavoriteListResult(String favoriteId);

    Flowable<BaseResponse> modifyFavoritesName(String favoritesId, String name);

    Flowable<BaseResponse> deleteFavorite(String id);

    Flowable<BaseResponse> deleteFavoritesSong(String favoritesId);

    Flowable<BaseResponse> addFavorite(String name);

    Flowable<Token> getToken(String userName, String password);
}
