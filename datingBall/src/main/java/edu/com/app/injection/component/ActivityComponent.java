package edu.com.app.injection.component;

import dagger.Component;
import edu.com.app.injection.module.ActivityModule;
import edu.com.app.injection.scope.PerActivity;
import edu.com.app.ui.find.FindFragment;
import edu.com.app.ui.find.friendCircle.FriendCircleActivity;
import edu.com.app.ui.find.nearby.NearByActivity;
import edu.com.app.ui.friends.add.FriendsAddActivity;
import edu.com.app.ui.friends.list.FriendsListFragment;
import edu.com.app.ui.friends.test.ListsFragment;
import edu.com.app.ui.main.MainActivity;
import edu.com.app.ui.news.channel.ChannelChooseActivity;
import edu.com.app.ui.news.newsList.NewsFragment;
import edu.com.app.ui.personal.edit.PersonalEditActivity;
import edu.com.app.ui.personal.info.PersonalInfoActivity;
import edu.com.app.ui.personal.login.NewLoginActivity;
import edu.com.app.ui.setting.SettingsActivity;
import edu.com.app.ui.setting.SettingsFragment;
import edu.com.app.ui.splash.SplashActivity;

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

    void inject(FriendsAddActivity friendsAddActivity);

    void inject(ChannelChooseActivity channelChooseActivity);

    void inject(FriendCircleActivity friendCircleActivity);

    void inject(NearByActivity nearByActivity);

    void inject(NewsFragment newsFragment);

    void inject(FriendsListFragment friendsListFragment);

    void inject(FindFragment findFragment);

    void inject(SettingsFragment settingsFragment);

    void inject(ListsFragment listsFragment);
}
