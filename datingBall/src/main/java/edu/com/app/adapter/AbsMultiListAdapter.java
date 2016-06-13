package edu.com.app.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Anthony on 2016/2/24.
 * Class Note:
 * Support Multi Type Layout adapter
 *
 */
public abstract class AbsMultiListAdapter<E> extends AbsListAdapter<E> {
    private SparseIntArray layoutId = new SparseIntArray();
    private SparseArray<Class<?>> holderClass = new SparseArray<Class<?>>();

    public AbsMultiListAdapter(Context ctx, List<E> data, SparseIntArray layoutId,
                               SparseArray<Class<?>> holderClass, Class<?> cls) {
        super(ctx, data, cls);
        this.layoutId = layoutId;
        this.holderClass = holderClass;
    }

    @Override
    public int getViewTypeCount() {
        return holderClass.size();
    }

    @Override
    public int getItemViewType(int position) {
        return getViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(layoutId.get(type), parent, false);
            holder = buildHolder(type, convertView);
            convertView.setTag(holder);
        } else {
            holder = convertView.getTag();
        }
        //避免Item在滚动中出现黑色背景
        convertView.setDrawingCacheEnabled(false);
        E item = getItem(position);
        updateView(convertView, item, position);
        return convertView;
    }

    private Object buildHolder(int type, View convertView) {
        try {
            holder = holderClass.get(type).newInstance();
            for (Field f : holderClass.get(type).getDeclaredFields()) {
                String name = f.getName();
                f.setAccessible(true);
                // ViewHolder的属性，不论类型都初始化赋值
                f.set(holder, convertView.findViewById(getId(name, cls)));
            }
        } catch (Exception e) {
            throw new RuntimeException("holder初始化出错    " + e);
        }
        return holder;
    }

    /**
     * Override to return view type which must begin with 0
     */
    protected abstract int getViewType(int position);
}
