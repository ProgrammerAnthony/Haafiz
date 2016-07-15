package edu.com.app.widget.recyclerview.base;

/**
 * Created by Anthony on 2016/7/15.
 * Class Note:
 * { @see https://github.com/hongyangAndroid/baseAdapter}
 */
public interface ItemViewDelegate <T>{
    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(ViewHolder holder, T t, int position);
}
