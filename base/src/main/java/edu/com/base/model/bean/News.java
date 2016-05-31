package edu.com.base.model.bean;

import io.realm.RealmObject;

/**
 * Created by Anthony on 2016/5/24.
 * Class Note:
 */
public class News extends RealmObject {
    private String avatarUrl;
    private String title;
    private String installationId;
    private String content;
    private String time;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    private int action;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInstallationId() {
        return installationId;
    }

    public void setInstallationId(String installationId) {
        this.installationId = installationId;
    }
}
