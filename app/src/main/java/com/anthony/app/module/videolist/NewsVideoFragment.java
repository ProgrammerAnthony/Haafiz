package com.anthony.app.module.videolist;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.anthony.app.R;
import com.anthony.app.dagger.DaggerListFragment;
import com.anthony.app.dagger.component.ActivityComponent;
import com.anthony.imageloader.ImageLoader;
import com.anthony.imageloader.ImageLoaderUtil;
import com.anthony.library.data.RxBus;
import com.anthony.library.data.bean.NewsItem;
import com.anthony.library.data.bean.NormalJsonInfo;
import com.anthony.library.data.database.dao.NewsItemDao;
import com.anthony.library.data.event.VideoListClickEvent;
import com.anthony.library.data.event.VideoOrientationChangeEvent;
import com.anthony.library.data.event.ViewPagerSelectedEvent;
import com.anthony.rvhelper.adapter.CommonAdapter;
import com.anthony.rvhelper.adapter.MultiItemTypeAdapter;
import com.anthony.rvhelper.base.ViewHolder;
import com.anthony.rvhelper.divider.RecycleViewDivider;
import com.anthony.videolistplayer.VideoPlayView;
import com.anthony.videolistplayer.media.IjkVideoView;

import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;
import tv.danmaku.ijk.media.player.IMediaPlayer;


/**
 * Created by Anthony on 2016/10/19.
 * Class Note:
 * video list fragment is a sub-component of {@link VideoListActivity}
 * currently load data from local "raw://video_list_data"
 */
public class NewsVideoFragment extends DaggerListFragment {
    private int current_play_position = -1; //正在播放的item位置，如果为-1表明当前没有视频在播放
    private VideoPlayView mVideoPlayView;
    private static final int PLAY_MODE_IN_LIST = 0;
    private static final int PLAY_MODE_WINDOW = 1;
    private static final int PLAY_MODE_FULL_SCREEN = 2;
    private int mPlayMode = PLAY_MODE_IN_LIST;
    private RelativeLayout mVideoPlayWindowLayout;
    private FrameLayout mVideoPlayWindowHolder;
    private FrameLayout mVideoPlayFullHolder;
    private ImageView mClosePlayWindowBtn;
    private LinearLayoutManager mLayoutManager;
//    @Inject
//    RxBus rxBus;
    @Inject
NewsItemDao newsItemDao;
    @Inject
    ImageLoaderUtil imageLoaderUtil;


    @Override
    protected int getLayoutId() {
        return R.layout.lib_fragment_list_video;
    }

    @Override
    protected void initDagger2(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initViews(View rootView, Bundle savedInstanceState) {
        super.initViews(rootView,  savedInstanceState);

        mVideoPlayWindowLayout = (RelativeLayout) rootView.findViewById(R.id.layout_play_window);
        mVideoPlayWindowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //全屏播放
                dismissVideoPlayLayoutInWindow(PLAY_MODE_FULL_SCREEN);
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        });

        mClosePlayWindowBtn = (ImageView) rootView.findViewById(R.id.btn_close);
        mClosePlayWindowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVideoPlayView.isPlay()) {
                    dismissVideoPlayLayoutInWindow(PLAY_MODE_IN_LIST);
                    mVideoPlayView.stop();
                    mVideoPlayView.release();
                    current_play_position = -1;
                }
            }
        });

        mVideoPlayWindowHolder = (FrameLayout) rootView.findViewById(R.id.layout_play_window_holder);
        mVideoPlayFullHolder = (FrameLayout) rootView.findViewById(R.id.layout_play_full_holder);

        mVideoPlayView = new VideoPlayView(getActivity());

        mVideoPlayView.setCompletionListener(new VideoPlayView.CompletionListener() {
            @Override
            public void completion(IMediaPlayer mp) {
                current_play_position = -1;

                //关闭播放视频layout
                FrameLayout frameLayout = (FrameLayout) mVideoPlayView.getParent();
                dismissVideoPlayLayout(frameLayout);
                //释放mVideoPlayView
                mVideoPlayView.release();
                mPlayMode = PLAY_MODE_IN_LIST;
            }
        });

        //增加RecyclerView中itemView的Attach和Detach监听
        //当item移回屏幕显示List播放，当item移出屏幕显示窗口播放
        mRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                int index = mRecyclerView.getChildAdapterPosition(view);
                if (index == current_play_position && mVideoPlayView.VideoStatus() == IjkVideoView.STATE_PLAYING) {//&& mPlayMode != PLAY_MODE_FULL_SCREEN
                    dismissVideoPlayLayoutInWindow(PLAY_MODE_IN_LIST);

                    FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.layout_video_holder);
                    displayVideoPlayLayout(frameLayout);
                } else {
                    View layout_thumbnail = view.findViewById(R.id.layout_thumbnail);
                    if (layout_thumbnail != null) {
                        layout_thumbnail.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                int index = mRecyclerView.getChildAdapterPosition(view);
                if (index == current_play_position) {
                    FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.layout_video_holder);
                    dismissVideoPlayLayout(frameLayout);

                    //小窗口播放
                    displayVideoPlayLayoutInWindow();
                }
            }
        });

        //监听item中layout_thumbnail的点击事件，开始播放视频
        RxBus.getDefault().toObserverable(VideoListClickEvent.class)
                .subscribe(new Action1<VideoListClickEvent>() {
                    @Override
                    public void call(VideoListClickEvent event) {
                        //点击一个item时，如果另外一个item中的视频正处于暂停状态，那么停止并释放mVideoPlayView
                        //否则将不会播放点击的视频而是继续播放暂停的视频
                        if (mVideoPlayView.VideoStatus() == IjkVideoView.STATE_PAUSED) {
                            mVideoPlayView.stop();
                            mVideoPlayView.release();
                        }

                        //点击一个item时，如果当正在小窗播放，那么清除小窗
                        if (mPlayMode == PLAY_MODE_WINDOW) {
                            dismissVideoPlayLayoutInWindow(PLAY_MODE_IN_LIST);
                        }

                        //点击一个item时，如果在正在播放另外一个item中的视频，那么先关闭正在播放的视频layout
                        if (current_play_position != event.position && current_play_position != -1) {
                            ViewGroup frameLayout = (ViewGroup) mVideoPlayView.getParent();
                            dismissVideoPlayLayout(frameLayout);
                        }

                        //将mVideoPlayView加入到layout_video_holder中，并开始播放
                        View itemView = mRecyclerView.findViewHolderForAdapterPosition(event.position).itemView;
                        FrameLayout frameLayout = (FrameLayout) itemView.findViewById(R.id.layout_video_holder);
                        frameLayout.removeAllViews();
                        frameLayout.addView(mVideoPlayView);
//                        if (event.position == 0) {
////                            mVideoPlayView.start("rtmp://live.zgbctv.com/zgtv/m10");//测试rtmp直播
////                            mVideoPlayView.start("http://live.hkstv.hk.lxdns.com/live/hks/playlist.m3u8");//测试m3u8直播1
//                            mVideoPlayView.start("http://vevoplaylist-live.hls.adaptive.level3.net/vevo/ch1/appleman.m3u8");//测试m3u8直播2
////                            mVideoPlayView.start("rtmp://11223.mpull.live.lecloud.com/live/trs?tm=20160909180244&sign=93e2c2f3c97ad5b9bfd88b5614570826");//测试直播
//                        } else {
//                            mVideoPlayView.start(event.item.getVideo().get(0).url);
//                        }
                        mVideoPlayView.start(event.item.getVideo().get(0).url);

                        current_play_position = event.position;
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });

        RxBus.getDefault().toObserverable(VideoOrientationChangeEvent.class)
                .subscribe(new Action1<VideoOrientationChangeEvent>() {
                    @Override
                    public void call(VideoOrientationChangeEvent event) {
                        Configuration newConfig = event.newConfig;

                        if (mVideoPlayView != null) {
                            mVideoPlayView.onChanged(newConfig);
                            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                                mVideoPlayFullHolder.setVisibility(View.GONE);
                                mRecyclerView.setVisibility(View.VISIBLE);
                                mVideoPlayFullHolder.removeAllViews();

                                //全屏模式下自动播放完毕之后，需要刷新列表，避免出现item中图片空白的情况
                                if (current_play_position == -1) {
                                    mRecyclerView.getAdapter().notifyDataSetChanged();
                                }

                                if (current_play_position <= mLayoutManager.findLastVisibleItemPosition()
                                        && current_play_position >= mLayoutManager.findFirstVisibleItemPosition()) {
                                    View view = mRecyclerView.findViewHolderForAdapterPosition(current_play_position).itemView;
                                    FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.layout_video_holder);
                                    displayVideoPlayLayout(frameLayout);
                                    mVideoPlayView.setShowContoller(true);
                                    mPlayMode = PLAY_MODE_IN_LIST;
                                } else {
                                    displayVideoPlayLayoutInWindow();
                                    mPlayMode = PLAY_MODE_WINDOW;
                                }
//                            videoItemView.setContorllerVisiable();
                            } else {
                                ViewGroup viewGroup = (ViewGroup) mVideoPlayView.getParent();
                                if (viewGroup != null)
                                    viewGroup.removeAllViews();
                                mVideoPlayFullHolder.addView(mVideoPlayView);
                                mRecyclerView.setVisibility(View.GONE);
                                mVideoPlayFullHolder.setVisibility(View.VISIBLE);
                            }
                        } else {
                            mRecyclerView.getAdapter().notifyDataSetChanged();
                            mRecyclerView.setVisibility(View.VISIBLE);
                            mVideoPlayFullHolder.setVisibility(View.GONE);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });

        //监听ViewPager切换的event，以便及时停止视频播放
        RxBus.getDefault().toObserverable(ViewPagerSelectedEvent.class)
                .subscribe(new Action1<ViewPagerSelectedEvent>() {
                    @Override
                    public void call(ViewPagerSelectedEvent event) {
                        if (!event.channel.getUrl().equals(getFragmentUrl())
                                && mVideoPlayView.VideoStatus() == IjkVideoView.STATE_PLAYING) {
                            if (mPlayMode == PLAY_MODE_IN_LIST) {
                                FrameLayout frameLayout = (FrameLayout) mVideoPlayView.getParent();
                                dismissVideoPlayLayout(frameLayout);
                            } else if (mPlayMode == PLAY_MODE_WINDOW) {
                                dismissVideoPlayLayoutInWindow(PLAY_MODE_IN_LIST);
                            }
                            mVideoPlayView.stop();
                            mVideoPlayView.release();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    protected MultiItemTypeAdapter getAdapter() {
        return new NewsVideoAdapter(getActivity());
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, 20,
                getResources().getColor(R.color.common_bg_dark));
    }

    @Override
    protected String getRequestUrl(int index) {
        if (index == 0) {
            return getFragmentUrl();
        } else {
            String prefix = getFragmentUrl().substring(0, getFragmentUrl().lastIndexOf("."));
            return prefix + "_" + String.valueOf(index) + ".json";
        }
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        return mLayoutManager;
    }

    @Override
    protected MultiItemTypeAdapter.OnItemClickListener getItemListener() {
        return null;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mVideoPlayView == null) {
            mVideoPlayView = new VideoPlayView(getActivity());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

//        if (videoLayout == null)
//            return;
//
//        if (smallLayout.getVisibility() == View.VISIBLE) {
//            smallLayout.setVisibility(View.GONE);
//            videoLayout.removeAllViews();
//        }

        if (current_play_position != -1) {
            ViewGroup view = (ViewGroup) mVideoPlayView.getParent();
            if (view != null) {
                view.removeAllViews();
            }
        }

        mVideoPlayView.stop();
        mVideoPlayView.release();
        mVideoPlayView.onDestroy();
        mVideoPlayView = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mVideoPlayView != null) {
            mVideoPlayView.stop();
        }
    }

    /**
     * 显示视频播放View，隐藏缩略图
     *
     * @param videoPlayHolder 必须是prj_list_item_video.xml中的layout_video_holder(FrameLayout)
     */
    private void displayVideoPlayLayout(ViewGroup videoPlayHolder) {
        if (videoPlayHolder != null) {
            videoPlayHolder.removeAllViews();
            videoPlayHolder.addView(mVideoPlayView);
            View itemView = (View) videoPlayHolder.getParent();
            if (itemView != null) {
                itemView.findViewById(R.id.layout_thumbnail).setVisibility(View.GONE);
            }
        }
    }

    /**
     * 隐藏视频播放View，显示缩略图
     *
     * @param videoPlayHolder 必须是prj_list_item_video.xml中的layout_video_holder(FrameLayout)
     */
    private void dismissVideoPlayLayout(ViewGroup videoPlayHolder) {
        if (videoPlayHolder != null) {
            videoPlayHolder.removeAllViews();
            View itemView = (View) videoPlayHolder.getParent();
            if (itemView != null) {
                itemView.findViewById(R.id.layout_thumbnail).setVisibility(View.VISIBLE);
            }
        }
    }

    private void displayVideoPlayLayoutInWindow() {
        if (mVideoPlayWindowLayout.getVisibility() == View.GONE && mVideoPlayView != null
                && mVideoPlayView.isPlay()) {
            mVideoPlayWindowLayout.setVisibility(View.VISIBLE);
            mVideoPlayWindowHolder.removeAllViews();
            mVideoPlayView.setShowContoller(false);
            mVideoPlayWindowHolder.addView(mVideoPlayView);
            mPlayMode = PLAY_MODE_WINDOW;
        }
    }

    private void dismissVideoPlayLayoutInWindow(int toMode) {
        if (mVideoPlayWindowLayout.getVisibility() == View.VISIBLE && mVideoPlayView != null
                && mVideoPlayView.isPlay()) {
            mVideoPlayWindowLayout.setVisibility(View.GONE);
            mVideoPlayWindowHolder.removeAllViews();
            mVideoPlayView.setShowContoller(true);
            mPlayMode = toMode;
        }
    }

    @Override
    protected List parseData(NormalJsonInfo jsonInfo) {
        return jsonInfo.datas;
    }

    @Override
    protected List parseTopic(NormalJsonInfo jsonInfo) {
        return jsonInfo.topic_datas;
    }

    @Override
    protected int parsePageCount(NormalJsonInfo jsonInfo) {
        return Integer.parseInt(jsonInfo.page_info.page_count);
    }


    @Override
    protected void restoreData(List data) {
        restore(data, false);
    }

    @Override
    protected void restoreTopic(List data) {
        restore(data, true);
    }


    private void restore(List data, boolean isTopic) {
        if (data == null || data.size() == 0) {
            return;
        }
        List<NewsItem> list = data;
        for (NewsItem e : list) {
//            e.setChannel(mChannel);
            e.setTopic(isTopic);
        }

        for (NewsItem e : list) {
            if (isExistInDb(e)) {
                newsItemDao.update(e);
            } else {
                newsItemDao.add(e);
            }
        }
    }

    private boolean isExistInDb(NewsItem e) {
        List<NewsItem> list = newsItemDao.queryByColumn("id", e.getId());
        return list != null && list.size() > 0;
    }

//    @Override
//    public String getFragmentUrl() {
//        return Constants.MAIN_PAGE_URL;
//    }


    public class NewsVideoAdapter extends CommonAdapter<NewsItem> {
        public NewsVideoAdapter(Context context) {
            super(context, R.layout.lib_list_item_video);
        }

        @Override
        protected void convert(final ViewHolder holder, final NewsItem item, int position) {
            holder.setText(R.id.tv_news_title, item.getTitle());
            holder.setText(R.id.tv_news_summary, item.getSummary());
            holder.setText(R.id.tv_news_date, item.getTime());

            if (item.getImgs().get(0) != null && item.getImgs().size() > 0) {
                ImageView news_img = holder.getView(R.id.iv_video);
                String url = item.getImgs().get(0);
                ImageLoader.Builder builder = new ImageLoader.Builder();
                ImageLoader img = builder.url(url)
                        .imgView(news_img).strategy(ImageLoaderUtil.LOAD_STRATEGY_ONLY_WIFI).build();
                imageLoaderUtil.loadImage(mContext, img);
            }

            holder.setOnClickListener(R.id.layout_thumbnail, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setVisibility(View.GONE);
                    RxBus.getDefault().post(new VideoListClickEvent(holder.getAdapterPosition(), item));
                }
            });
        }
    }



}
