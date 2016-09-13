package com.anthony.app.common.injection.component;


import com.anthony.app.common.base.AbsListFragment;
import com.anthony.app.common.injection.module.ActivityModule;
import com.anthony.app.common.injection.scope.PerActivity;
import com.anthony.app.common.widgets.imagebrowse.ImageBrowserActivity;
import com.anthony.app.common.widgets.webview.WebViewCommentActivity;
import com.anthony.app.module.main.MainActivity;
import com.anthony.app.module.splash.LoadingActivity;

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
    void inject(WebViewCommentActivity webViewActivity);

    void inject(ImageBrowserActivity imageBrowserActivity);

    void inject(AbsListFragment absListFragment);

    //this project
    void inject(LoadingActivity loadingActivity);

    void inject(MainActivity mainActivity);

}
