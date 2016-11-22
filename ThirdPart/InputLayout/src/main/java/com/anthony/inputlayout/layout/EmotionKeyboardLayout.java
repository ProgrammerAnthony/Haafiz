package com.anthony.inputlayout.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.anthony.inputlayout.utils.Utils;
import com.anthony.inputlayout.utils.EmotionUtil;
import com.trs.inputlayout.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class EmotionKeyboardLayout extends BaseCustomCompositeView {
    private static final int EMOTION_COLUMN = 7;
    private static final int EMOTION_ROW = 4;
    private static final int EMOTION_PAGE_SIZE = EMOTION_COLUMN * EMOTION_ROW - 1;

    private ViewPager mContentVp;
    private LinearLayout mIndicatorLl;
    private ArrayList<ImageView> mIndicatorIvList;
    private ArrayList<GridView> mGridViewList;

    private Callback mCallback;

    public EmotionKeyboardLayout(Context context) {
        super(context);
    }

    public EmotionKeyboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmotionKeyboardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_emotion_keyboard;
    }

    @Override
    protected void initView() {
        mContentVp = getViewById(R.id.vp_emotion_keyboard_content);
        mIndicatorLl = getViewById(R.id.ll_emotion_keyboard_indicator);
    }

    @Override
    protected void setListener() {
        mContentVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mIndicatorIvList.size(); i++) {
                    mIndicatorIvList.get(i).setEnabled(false);
                }
                mIndicatorIvList.get(position).setEnabled(true);
            }
        });
    }

    @Override
    protected int[] getAttrs() {
        return new int[0];
    }

    @Override
    protected void initAttr(int attr, TypedArray typedArray) {

    }

    @Override
    protected void processLogic() {
        mIndicatorIvList = new ArrayList<>();
        mGridViewList = new ArrayList<>();

        int emotionPageCount = (EmotionUtil.sEmotionKeyArr.length - 1) / EMOTION_PAGE_SIZE + 1;

        ImageView indicatorIv;
        LinearLayout.LayoutParams indicatorIvLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = Utils.dip2px(getContext(), 5);
        indicatorIvLp.setMargins(margin, margin, margin, margin);
        for (int i = 0; i < emotionPageCount; i++) {
            indicatorIv = new ImageView(getContext());
            indicatorIv.setLayoutParams(indicatorIvLp);
            indicatorIv.setImageResource(R.drawable.selector_emotion_indicator);
            indicatorIv.setEnabled(false);
            mIndicatorIvList.add(indicatorIv);
            mIndicatorLl.addView(indicatorIv);

            mGridViewList.add(getGridView(i));
        }
        mIndicatorIvList.get(0).setEnabled(true);
        mContentVp.setAdapter(new EmotionPagerAdapter());
    }

    private GridView getGridView(int position) {
        int edge = Utils.dip2px(getContext(), 5);
        GridView gridView = new GridView(getContext());

        gridView.setPadding(edge, edge, edge, edge);
        gridView.setNumColumns(EMOTION_COLUMN);
        gridView.setVerticalSpacing(edge);
        gridView.setHorizontalSpacing(edge);
        gridView.setOverScrollMode(OVER_SCROLL_NEVER);
        gridView.setVerticalScrollBarEnabled(false);
        gridView.setVerticalFadingEdgeEnabled(false);
        gridView.setHorizontalScrollBarEnabled(false);
        gridView.setHorizontalFadingEdgeEnabled(false);
        gridView.setSelector(android.R.color.transparent);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EmotionAdapter adapter = (EmotionAdapter) parent.getAdapter();
                if (position == adapter.getCount() - 1) {
                    // 删除
                    if (mCallback != null) {
                        mCallback.onDelete();
                    }
                } else {
                    // 插入表情
                    if (mCallback != null) {
                        mCallback.onInsert(adapter.getItem(position));
                    }
                }
            }
        });

        int start = position * EMOTION_PAGE_SIZE;
        List<String> tempEmotionList = Arrays.asList(Arrays.copyOfRange(EmotionUtil.sEmotionKeyArr, start, start + EMOTION_PAGE_SIZE));
        List<String> emotionList = new ArrayList<>();
        emotionList.addAll(tempEmotionList);
        emotionList.add("");

        gridView.setAdapter(new EmotionAdapter(emotionList));
        return gridView;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    class EmotionPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mGridViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mGridViewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mGridViewList.get(position));
            return mGridViewList.get(position);
        }
    }

    class EmotionAdapter extends BaseAdapter {
        private List<String> mDatas;

        public EmotionAdapter(List<String> datas) {
            mDatas = datas;
        }

        @Override
        public int getCount() {
            return mDatas == null ? 0 : mDatas.size();
        }

        @Override
        public String getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.item_emotion_keyboard, null);
            }

            ImageView iconIv = (ImageView) convertView;
            if (position == getCount() - 1) {
                iconIv.setImageResource(R.drawable.emoji_delete);
                iconIv.setVisibility(VISIBLE);
            } else {
                String key = mDatas.get(position);
                if (TextUtils.isEmpty(key)) {
                    iconIv.setVisibility(INVISIBLE);
                } else {
                    iconIv.setImageResource(EmotionUtil.getImgByName(key));
                    iconIv.setVisibility(VISIBLE);
                }
            }

            return convertView;
        }
    }

    public interface Callback {
        void onDelete();
        void onInsert(String text);
    }
}
