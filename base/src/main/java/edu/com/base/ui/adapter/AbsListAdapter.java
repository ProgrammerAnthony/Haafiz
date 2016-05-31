package edu.com.base.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Anthony on 2016/2/24.
 * Class Note:
 * Only Support One Layout in the List
 * If you want to get a Multi Type Layout adapter,use{@link AbsMultiListAdapter}
 */
public abstract class AbsListAdapter<E> extends BaseAdapter {
    public Object holder;
    protected Context mContext;
    private List<E> data;
    public Class<?> cls;
    private int layoutId;
    public Class<?> holderClass;
    private boolean initFlag = true;

    public AbsListAdapter(Context context, List<E> data, Class<?> cls) {
        this.mContext = context;
        this.data = data;
        this.cls = cls;
    }

    public AbsListAdapter(Context ctx, List<E> data,
                          int layoutId, Class<?> holderClass, Class<?> cls) {
        this.mContext = ctx;
        this.data = data;
        this.cls = cls;
        this.layoutId = layoutId;
        this.holderClass = holderClass;
    }

    @Override
    public E getItem(int position) {
        if (!isEmpty(data)) {
            return data.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        if (!isEmpty(data)) {
            return data.size();
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
            holder = buildHolder(convertView);
//            setTextTypeFace();
            convertView.setTag(holder);
        } else {
            holder = convertView.getTag();
        }

        if (initFlag) {
//            setTextView();
            initFlag = false;
        }

        // 避免Item在滚动中出现黑色背景
        convertView.setDrawingCacheEnabled(false);
        E item = getItem(position);
        updateView(convertView, item, position);
        return convertView;
    }

    private Object buildHolder(View convertView) {
        try {
            holder = holderClass.newInstance();
            for (Field f : holderClass.getDeclaredFields()) {
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

    public List<E> getItems() {
        return data;
    }

    private boolean isEmpty(List<?> list) {
        return (list == null || list.size() == 0);
    }

    public void addItem(E extras) {
        data.add(extras);
        notifyDataSetChanged();
    }

    public void reload(List<E> newData) {
        data.clear();
        addItems(newData);
    }

    public void remove(E item) {
        data.remove(item);
        notifyDataSetChanged();
    }

    public void removeAll() {
        data.clear();
        notifyDataSetChanged();
    }

    public void addItems(List<E> extras) {
        if (isEmpty(extras)) {
            return;
        }
        data.addAll(getCount(), extras);
        notifyDataSetChanged();
    }

    /**
     * get id by name
     */
    public int getId(String name, Class<?> cls) {
        try {
//			Class<R.id> cls = R.id.class;
            Field f = cls.getDeclaredField(name);
            f.setAccessible(true);
            return f.getInt(cls);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Override to return view type which must begin with 0
     */
    protected abstract void updateView(View convertView, E item, int position);
}
