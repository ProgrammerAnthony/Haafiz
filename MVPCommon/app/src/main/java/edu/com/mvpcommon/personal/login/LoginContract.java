package edu.com.mvpcommon.personal.login;

import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import edu.com.mvpcommon.main.DrawerData;
import edu.com.mvplibrary.presenter.BasePresenter;
import edu.com.mvplibrary.ui.BaseView;

/**
 * Created by Anthony on 2016/5/16.
 * Class Note:
 */
public interface LoginContract {
    interface Presenter extends BasePresenter {
        void onActivityStart();

        void onActivityPause();

        void beginAnimation(ImageView imageView, TextView slogan, ShimmerFrameLayout shimmerFrameLayout);

        void Register(TextInputLayout registerUser, TextInputLayout registerPass, TextInputLayout registerRepeatPasswd);

        void Login(TextInputLayout usernameLogin, TextInputLayout passwdLogin);


        void doingSplash();

        boolean isAnimationRunning();
    }

    interface View extends BaseView {
        void toMainActivity();

        boolean isLoginViewShowing();

        void showLoginView();

        void dismissLoginView();

        void hideLoginButton();

        void showLoginButton();

    }


}
