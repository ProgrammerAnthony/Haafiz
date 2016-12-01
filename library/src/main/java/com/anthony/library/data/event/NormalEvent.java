package com.anthony.library.data.event;

/**
 * Created by Anthony on 2016/5/24.
 * Class Note:
 * this is just a normal  event
 */
public class NormalEvent {
    String id;
    String name;

    public NormalEvent(String name) {
        this.id = "defaultId";
        this.name = name;
    }

    public NormalEvent(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
