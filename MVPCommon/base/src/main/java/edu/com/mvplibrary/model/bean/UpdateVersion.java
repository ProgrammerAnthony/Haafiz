package edu.com.mvplibrary.model.bean;

/**
 * Created by Administrator on 2016/5/5.
 */
public class UpdateVersion {


    /**
     * name : Baby
     * version : 20160505
     * changelog : 1.修复了聊天推送的问题
     * 2.语音视频的问题
     * 3.主页背景无法上传的问题
     * updated_at : 1462453712
     * versionShort : 1.1
     * build : 20160505
     * installUrl : http://download.fir.im/v2/app/install/5728af16e75e2d0825000018?download_token=07d8b48f7dd5c416052d05e1a9a628a4
     * install_url : http://download.fir.im/v2/app/install/5728af16e75e2d0825000018?download_token=07d8b48f7dd5c416052d05e1a9a628a4
     * direct_install_url : http://download.fir.im/v2/app/install/5728af16e75e2d0825000018?download_token=07d8b48f7dd5c416052d05e1a9a628a4
     * update_url : http://fir.im/b9u8
     * binary : {"fsize":28288774}
     */

    private String name;
    private long version;
    private String changelog;
    private int updated_at;
    private String versionShort;
    private String build;
    private String installUrl;
    private String install_url;
    private String direct_install_url;
    private String update_url;
    /**
     * fsize : 28288774
     */

    private BinaryEntity binary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getChangelog() {
        return changelog;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    public int getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(int updated_at) {
        this.updated_at = updated_at;
    }

    public String getVersionShort() {
        return versionShort;
    }

    public void setVersionShort(String versionShort) {
        this.versionShort = versionShort;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getInstallUrl() {
        return installUrl;
    }

    public void setInstallUrl(String installUrl) {
        this.installUrl = installUrl;
    }

    public String getInstall_url() {
        return install_url;
    }

    public void setInstall_url(String install_url) {
        this.install_url = install_url;
    }

    public String getDirect_install_url() {
        return direct_install_url;
    }

    public void setDirect_install_url(String direct_install_url) {
        this.direct_install_url = direct_install_url;
    }

    public String getUpdate_url() {
        return update_url;
    }

    public void setUpdate_url(String update_url) {
        this.update_url = update_url;
    }

    public BinaryEntity getBinary() {
        return binary;
    }

    public void setBinary(BinaryEntity binary) {
        this.binary = binary;
    }

    public static class BinaryEntity {
        private int fsize;

        public int getFsize() {
            return fsize;
        }

        public void setFsize(int fsize) {
            this.fsize = fsize;
        }
    }
}
