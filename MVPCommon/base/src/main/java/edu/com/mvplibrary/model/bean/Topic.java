package edu.com.mvplibrary.model.bean;





import org.json.JSONException;
import org.json.JSONObject;

import edu.com.mvplibrary.model.config.Constants;
import edu.com.mvplibrary.util.JSONObjectHelper;

/**
 * Created by Anthony on 2016/2/24.
 * Class Note:  base types of data
 *
 */
public class Topic {
    private String id;
    private String title;
    private String imgUrl;
    private String url;
    private String type;

    public Topic(String objStr) throws JSONException {
        this(new JSONObject(objStr));
    }

    public Topic(JSONObject obj) throws JSONException {
        JSONObjectHelper helper = new JSONObjectHelper(obj);
        setId(helper.getString(Constants.ID_NAMES, null));
        setTitle(helper.getString(Constants.TITLE_NAMES, null));
        setImgUrl(helper.getString(Constants.IMAGE_URL_NAMES, null));
        setUrl(helper.getString(Constants.URL_NAMES, null));
        setType(helper.getString(Constants.TYPE_NAMES, null));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
