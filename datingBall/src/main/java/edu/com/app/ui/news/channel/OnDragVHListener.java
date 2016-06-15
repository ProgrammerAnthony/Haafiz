package edu.com.app.ui.news.channel;


/**
 * Created by Anthony on 2016/6/14.
 * Class Note:
 *  ViewHolder 被选中 以及 拖拽释放 触发监听器
 */
public interface OnDragVHListener {
    /**
     * Item被选中时触发
     */
    void onItemSelected();


    /**
     * Item在拖拽结束/滑动结束后触发
     */
    void onItemFinish();
}
