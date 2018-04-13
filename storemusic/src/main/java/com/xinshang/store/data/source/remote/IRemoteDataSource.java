package com.xinshang.store.data.source.remote;

import com.xinshang.store.data.entities.ApiResponse;
import com.xinshang.store.data.entities.BaseResponse;
import com.xinshang.store.data.entities.CommentDataBean;
import com.xinshang.store.data.entities.FavoriteListResult;
import com.xinshang.store.data.entities.FavoritesResult;
import com.xinshang.store.data.entities.FileResult;
import com.xinshang.store.data.entities.LyricResult;
import com.xinshang.store.data.entities.Music;
import com.xinshang.store.data.entities.NowPlayingResponse;
import com.xinshang.store.data.entities.PlayAllRequest;
import com.xinshang.store.data.entities.PlaylistResponse;
import com.xinshang.store.data.entities.PlaylistSongResponse;
import com.xinshang.store.data.entities.SearchResult;
import com.xinshang.store.data.entities.Song;
import com.xinshang.store.data.entities.SongDetailResult;
import com.xinshang.store.data.entities.Store;
import com.xinshang.store.data.entities.StoreKeeperResponse;
import com.xinshang.store.data.entities.StoreSong;
import com.xinshang.store.data.entities.Token;
import com.xinshang.store.data.entities.ToplistDataBean;
import com.xinshang.store.data.entities.ToplistDetailResult;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by koma on 3/6/18.
 */

public interface IRemoteDataSource {
    Flowable<StoreKeeperResponse> getStoreKeeperInfo();

    Flowable<ApiResponse<List<ToplistDataBean>>> getToplists();

    Flowable<ToplistDetailResult> getToplistDetail(int topId, String showTime, int page, int count);

    Flowable<SearchResult> searchSongs(String keyword, int page, int size);

    Flowable<ApiResponse<PlaylistResponse>> searchPlaylists(String keyword, int page, int size);

    Flowable<ApiResponse<PlaylistSongResponse>> getPlaylistDetails(String id);

    Flowable<LyricResult> getLyricResult(String id);

    Flowable<SongDetailResult> getSongDetailResult(String id);

    Flowable<FileResult> getFileResult(String id);

    Flowable<NowPlayingResponse> getNowPlaying(String storeId);

    Flowable<ApiResponse<CommentDataBean>> getComments(String mid, String sortord, String storeId, int page,
                                                       int size);

    Flowable<BaseResponse> addToFavorite(String favoriteId, Song audient);

    Flowable<FavoritesResult> getFavoriteResult();

    Flowable<FavoriteListResult> getFavoriteListResult(String favoriteId);

    Flowable<BaseResponse> modifyFavoritesName(String favoritesId, String name);

    Flowable<BaseResponse> deleteFavorite(String id);

    Flowable<BaseResponse> deleteFavoritesSong(String favoritesId);

    Flowable<BaseResponse> addFavorite(String name);

    Flowable<Token> getToken(String userName, String password);

    Flowable<BaseResponse> addToPlaylist(Music music);

    Flowable<BaseResponse> deleteSongFromPlaylist(String id);

    Flowable<BaseResponse> addAllToPlaylist(String storeId, String favoritesId);

    Flowable<ApiResponse<List<StoreSong>>> getStorePlaylist(String storeId);

    Flowable<ApiResponse<List<Store>>> getStoreInfo();

    Flowable<BaseResponse> playAllSongs(String id, PlayAllRequest playAllRequest);
}
