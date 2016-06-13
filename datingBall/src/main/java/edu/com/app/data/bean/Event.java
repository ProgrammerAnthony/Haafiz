package edu.com.app.data.bean;

/**
 * Created by Anthony on 2016/5/24.
 * Class Note:
 */
public class Event {
    String id;
    String name;

    public Event(String id,String name){
        this.id=id;
        this.name=name;
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
