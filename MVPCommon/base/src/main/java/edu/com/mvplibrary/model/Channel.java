package edu.com.mvplibrary.model;



import org.json.JSONException;
import org.json.JSONObject;

import edu.com.mvplibrary.model.config.Constants;
import edu.com.mvplibrary.util.JSONObjectHelper;

/**
 * Created by Anthony on 2016/2/24.
 * Class Note:  base types of data
 *
 */
public class Channel {
    private String type;
    private String title;
    private String url;
    private String img;
    private String extra;

    public Channel(String objStr) throws JSONException {
        this(new JSONObject(objStr));
    }

    public Channel(JSONObject obj) {
        JSONObjectHelper helper = new JSONObjectHelper(obj);
        setType(helper.getString(Constants.TYPE_NAMES, null));
        setImg(helper.getString(Constants.IMAGE_URL_NAMES, null));
        setUrl(helper.getString(Constants.URL_NAMES, null));
        setTitle(helper.getString(Constants.TITLE_NAMES, null));
        setExtra(helper.getString(Constants.EXTRA_NAMES, null));
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this){
            return true;
        }

        if(o == null || o.getClass() != ((Object)this).getClass()){
            return false;
        }

        Channel c = (Channel)o;
        if(!c.getImg().equals(getImg())){
            return false;
        }

        if(!c.getUrl().equals(getUrl())){
            return false;
        }

        if(!c.getType().equals(getType())){
            return false;
        }

        if(!c.getTitle().equals(getTitle())){
            return false;
        }

        return true;
    }
}
