package edu.com.app.injection.component;

import dagger.Component;
import edu.com.app.injection.module.ActivityModule;
import edu.com.app.injection.scope.PerActivity;
import edu.com.app.ui.find.FindFragment;
import edu.com.app.ui.friends.add.FriendsAddActivity;
import edu.com.app.ui.friends.list.FriendsListFragment;
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


    void inject(NewsFragment newsFragment);

    void inject(FriendsListFragment friendsListFragment);

    void inject(FindFragment findFragment);

    void inject(SettingsFragment settingsFragment);

}
