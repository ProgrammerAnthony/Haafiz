package edu.com.app.data.retrofit;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.com.app.data.bean.Channel;
import timber.log.Timber;

/**
 * Created by Anthony on 2016/7/26.
 * Class Note:
 * custom TypeAdapter for {@link edu.com.app.data.bean.Channel}
 * used in Gson
 */
public class ChannelTypeAdapter extends TypeAdapter<HttpResult<List<Channel>>> {
    @Override
    public void write(JsonWriter out, HttpResult<List<Channel>> value) throws IOException {
        // currently not used here
    }

    @Override
    public HttpResult<List<Channel>> read(JsonReader in) throws IOException {
        HttpResult<List<Channel>> httpResult = new HttpResult<>();
        String title = null;
        String url = null;
        int type = 0;
        int isFix = 0;
        int isSubscribe = 0;

        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "code":
                    httpResult.code = in.nextInt();
                    break;
                case "message":
                    httpResult.message = in.nextString();
                    break;
                case "datas":
                    in.beginArray();
                    List<Channel> channels = new ArrayList<>();
                    while (in.hasNext()) {
                        Channel channel = null;
                        in.beginObject();
                        while (in.hasNext()) {
                            switch (in.nextName()) {
                                case "title":
                                    title = in.nextString();
                                    Timber.i("title " + title);
                                    break;
                                case "url":
                                    url = in.nextString();
                                    Timber.i("url " + url);
                                    break;
                                case "channelType":
                                    type = in.nextInt();
                                    Timber.i("type " + type);
                                    break;
                                case "isFix":
                                    isFix = in.nextInt();
                                    Timber.i("isFix " + isFix);
                                    break;
                                case "isSubscribe":
                                    isSubscribe = in.nextInt();
                                    Timber.i("isSubscribe " + isSubscribe);
                                    break;
                            }
                        }
                        channel = Channel.create(title, type, url, isFix, isSubscribe);
                        channels.add(channel);
                        in.endObject();
                    }
                    in.endArray();
                    httpResult.data = channels;
                    break;
            }
        }
        in.endObject();

        return httpResult;
    }
}