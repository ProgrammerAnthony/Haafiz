package com.anthony.app.module.github;

import com.anthony.app.common.Constants;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Anthony on 2016/10/13.
 * Class Note:
 * Github API
 *
 * Github 接口
 */

public interface GithubApi {
    String end_point = Constants.Remote_BASE_END_POINT_GITHUB;

    /**
     * load user's following list
     * @param user
     * @return
     */
    @GET("/users/{user}/following")
    Observable<ResponseBody> loadUserFollowingString(@Path(value = "user") String user);


    @GET("/users/{user}/following")
    Observable<List<GithubUser>> loadUserFollowingList(@Path(value = "user") String user);
}
