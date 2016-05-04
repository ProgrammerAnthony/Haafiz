package edu.com.mvplibrary.model;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.com.mvplibrary.model.config.Constants;
import edu.com.mvplibrary.util.JSONObjectHelper;

/**
 * Created by Anthony on 2016/2/25.
 * Class Note: base types of data
 */
public class Menu {
    private String type;
    private ArrayList<Channel> channels=new ArrayList<>();

    public Menu(String objStr) throws JSONException {
        this(new JSONObject(objStr));
    }
    public Menu(JSONObject obj) throws JSONException {
        JSONObjectHelper helper = new JSONObjectHelper(obj);
        setType(helper.getString(Constants.TYPE_NAMES, null));

        JSONArray array = helper.getJSONArray(Constants.CHANNEL_NAMES, null);
        for (int i = 0; i < array.length(); i++) {
            Channel channel = new Channel(array.getJSONObject(i));
            channels.add(channel);
        }
        setChannels(channels);
    }

    public String getType(){return type;}
    public void setType(String type){this.type=type;}
    public ArrayList<Channel> getChannels(){return channels;}
    public void setChannels(ArrayList<Channel> channels) {
        this.channels = channels;
    }

}
