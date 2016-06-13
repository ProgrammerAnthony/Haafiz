package edu.com.app.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import edu.com.app.data.bean.Friends;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import rx.Observable;

public interface FriendsService {
    String ENDPOINT = "https://api.ribot.io/";

    @GET("ribots")
    Observable<List<Friends>> getFriends();

    /******** Helper class that sets up a new services *******/
    class Creator {

        public static FriendsService newFriendService() {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(FriendsService.ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(FriendsService.class);
        }
    }
}
