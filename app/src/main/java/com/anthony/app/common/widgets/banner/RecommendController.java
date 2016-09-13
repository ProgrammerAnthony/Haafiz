package com.anthony.app.common.widgets.banner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anthony.app.R;
import com.anthony.app.common.data.bean.NewsItem;
import com.anthony.app.common.widgets.imageloader.ImageLoader;
import com.anthony.app.common.widgets.imageloader.ImageLoaderUtil;
import com.viewpagerindicator.PageIndicator;

import java.util.ArrayList;
import java.util.List;


public class RecommendController {


    ImageLoaderUtil imageLoaderUtil;
    public static final int SWITCH_PAGE_DURATION = 5 * 1000;

    public interface OnItemClickListener {
        void onItemClick(NewsItem topic);
    }

    private ArrayList<NewsItem> mTopicList = new ArrayList<>();
    private View mView;
    private ViewPager mViewPager;
    private TextView mTitle;
    private PageIndicator mIndicator;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private EDog mSwitchPageEDog = new EDog();
    private boolean mAutoSwitchPage;
    private int mLayoutID;
    private Runnable mSwitchPageTask = new Runnable() {
        @Override
        public void run() {
            if (mViewPager.getAdapter().getCount() > 0) {
                int switchToItem = (mViewPager.getCurrentItem() + 1) % mViewPager.getAdapter().getCount();
                mViewPager.setCurrentItem(switchToItem);
                startSwitchPage();
            }
        }
    };

    public RecommendController(Context context) {
        this(context, true);
    }

    public RecommendController(Context context, boolean autoSwitchPage) {
        this(context, R.layout.prj_recommend_controller, autoSwitchPage);
    }

    /**
     * Requirement of layout id
     * Required view:
     * * ViewPager with id: pager
     * * PageIndicator with id: pager_indicator
     * Optional view:
     * TextView with id: title
     *
     * @param context        context
     * @param layoutId       layoutID
     * @param autoSwitchPage is auto switch page
     */
    public RecommendController(Context context, int layoutId, boolean autoSwitchPage) {
        this.mContext = context;
        this.mLayoutID = layoutId;
        this.mAutoSwitchPage = autoSwitchPage;
        imageLoaderUtil = new ImageLoaderUtil();
        createView();
        startSwitchPage();
    }

    private PagerAdapter mAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return mTopicList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final NewsItem topic = mTopicList.get(position);
            ImageView imgView = new ImageView(mContext);
            imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            String url = topic.getImgs().get(0);

            ImageLoader.Builder builder = new ImageLoader.Builder();
            ImageLoader img = builder.url(url)
                    .imgView(imgView).strategy(ImageLoaderUtil.LOAD_STRATEGY_ONLY_WIFI).build();
            imageLoaderUtil.loadImage(mContext, img);

            container.addView(imgView);
            imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(topic);
                    }
                }
            });
            return imgView;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTopicList.get(position).getTitle();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    };

    public View getView() {
        return mView;
    }

    private void createView() {
        LayoutInflater factory = LayoutInflater.from(mContext);
        mView = factory.inflate(mLayoutID, null);

        mViewPager = (ViewPager) mView.findViewById(R.id.pager);
        mTitle = (TextView) mView.findViewById(R.id.title);
        mIndicator = (PageIndicator) mView.findViewById(R.id.pager_indicator);

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        cancelSwitchPage();
                        System.out.println("Touches ViewPager, cancel EDog task");
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        startSwitchPage();
                        System.out.println("Un-touches ViewPager, start EDog task");
                        break;
                }
                return false;
            }
        });
        mIndicator.setViewPager(mViewPager);
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                RecommendController.this.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (mTopicList.size() == 0) {
            mView.setVisibility(View.GONE);
        }
    }

    private void onPageSelected(int position) {
        if (position >= 0) {
            String titleText = mAdapter.getPageTitle(position).toString();
            mTitle.setText(titleText);
        }
    }

    public void setTopicList(List<NewsItem> topicList) {
        mTopicList.clear();
        if (topicList != null) {
            mTopicList.addAll(topicList);
        }
        mAdapter.notifyDataSetChanged();
        if (mAdapter.getCount() > 0) {
            onPageSelected(mViewPager.getCurrentItem());
        }
        mView.setVisibility(mAdapter.getCount() > 0 ? View.VISIBLE : View.GONE);
        startSwitchPage();
    }

    public void clearTopicList() {
        mTopicList.clear();
    }

    public void setOnClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void cancelSwitchPage() {
        mSwitchPageEDog.cancel();
    }

    public void startSwitchPage() {
        if (mAutoSwitchPage) {
            mSwitchPageEDog.feed(mSwitchPageTask, SWITCH_PAGE_DURATION);
        }
    }

    public void onPause() {
        cancelSwitchPage();
    }

    public void onResume() {
        startSwitchPage();
    }

}
