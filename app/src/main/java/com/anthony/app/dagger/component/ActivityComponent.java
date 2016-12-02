package com.anthony.app.dagger.component;


import com.anthony.app.dagger.DaggerActivity;
import com.anthony.app.dagger.DaggerFragment;
import com.anthony.app.dagger.module.ActivityModule;
import com.anthony.app.dagger.scope.PerActivity;
import com.anthony.app.module.MainListActivity;
import com.anthony.app.module.banner.BannerActivity;
import com.anthony.app.module.github.GithubActivity;
import com.anthony.app.module.imagebrowse.ImageBrowserActivity;
import com.anthony.app.module.newslist.AbsListFragment;
import com.anthony.app.module.newslist.NewsListActivity;
import com.anthony.app.module.newslist.NewsListFragment;
import com.anthony.app.module.segment.SegmentControlActivity;
import com.anthony.app.module.splash.LoadingActivity;
import com.anthony.app.module.statusview.ChooseStatusActivity;
import com.anthony.app.module.statusview.ShowStatusActivity;
import com.anthony.app.module.tab.TabActivity;
import com.anthony.app.module.videolist.NewsVideoFragment;
import com.anthony.app.module.videolist.VideoListActivity;
import com.anthony.app.module.weather.WeatherActivity;
import com.anthony.app.module.wechatlist.WechatListActivity;
import com.anthony.app.module.zhihu.ZhihuDailyListActivity;

import dagger.Component;

//import edu.com.app.module.news.newsList.NewsListFragment;

/**
 * Created by Anthony on 2016/6/13.
 * Class Note:
 * depend on {@link ApplicationComponent},so with custom {@link PerActivity}Scope.
 * <p>
 * In Dagger, an unscoped component cannot depend on a scoped component. As
 * {@link ApplicationComponent} is a scoped component ({@code @Singleton}, we create a custom
 * scope to be used by all fragment/activity components. Additionally, a component with a specific scope
 * cannot have a sub component with the same scope.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    //all of the project

    void inject(DaggerActivity daggerActivity);

    void inject(DaggerFragment daggerFragment);



    void inject(ImageBrowserActivity imageBrowserActivity);

    void inject(AbsListFragment absListFragment);

    //this project
    void inject(LoadingActivity loadingActivity);

    void inject(TabActivity tabActivity);

    void inject(MainListActivity mainListActivity);

    void inject(BannerActivity bannerActivity);

    void inject(WeatherActivity weatherActivity);

    void inject(GithubActivity githubActivity);

    void inject(SegmentControlActivity segmentControlActivity);

    void inject(VideoListActivity videoListActivity);

    void inject(NewsVideoFragment newsVideoFragment);

    void inject(NewsListFragment newsListFragment);

    void inject(NewsListActivity newsListActivity);

    void inject(ShowStatusActivity showStatusActivity);

    void inject(ChooseStatusActivity chooseStatusActivity);

    void inject(WechatListActivity wechatListActivity);

    void inject(ZhihuDailyListActivity zhihuDailyListActivity);
}
