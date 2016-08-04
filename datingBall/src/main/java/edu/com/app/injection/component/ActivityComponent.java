package edu.com.app.injection.component;

import dagger.Component;
import edu.com.app.adapter.TabViewPagerAdapter;
import edu.com.app.module.news.AbsListFragment;
import edu.com.app.injection.module.ActivityModule;
import edu.com.app.injection.scope.PerActivity;
import edu.com.app.module.find.FindFragment;
import edu.com.app.module.find.friendCircle.FriendCircleActivity;
import edu.com.app.module.find.nearby.NearByActivity;
import edu.com.app.module.friends.ListsFragment;
import edu.com.app.module.main.MainActivity;
import edu.com.app.module.news.list.NewsListFragment;
import edu.com.app.module.news.tab.NewsTabFragment;
import edu.com.app.module.personal.edit.PersonalEditActivity;
import edu.com.app.module.personal.info.PersonalInfoActivity;
import edu.com.app.module.personal.login.NewLoginActivity;
import edu.com.app.module.setting.SettingsActivity;
import edu.com.app.module.setting.SettingsFragment;
import edu.com.app.module.splash.SplashActivity;

//import edu.com.app.module.news.newsList.NewsListFragment;

/**
 * Created by Anthony on 2016/6/13.
 * Class Note:
 * depend on {@link ApplicationComponent},so with custom {@link PerActivity}Scope.
 *
 * In Dagger, an unscoped component cannot depend on a scoped component. As
 * {@link edu.com.app.injection.component.ApplicationComponent} is a scoped component ({@code @Singleton}, we create a custom
 * scope to be used by all fragment/activity components. Additionally, a component with a specific scope
 * cannot have a sub component with the same scope.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(SplashActivity splashActivity);

    void inject(SettingsActivity settingActivity);

    void inject(NewLoginActivity loginActivity);

    void inject(PersonalInfoActivity personalInfoActivity);

    void inject(PersonalEditActivity personalEditActivity);

    void inject(FriendCircleActivity friendCircleActivity);

    void inject(NearByActivity nearByActivity);

    void inject(FindFragment findFragment);

    void inject(SettingsFragment settingsFragment);

    void inject(ListsFragment listsFragment);

    void inject(NewsListFragment newsListFragment);

    void inject(NewsTabFragment tabFragment);


    void inject(TabViewPagerAdapter tabViewPagerAdapter);

    void inject(AbsListFragment listFragment);
}
