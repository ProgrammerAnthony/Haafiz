package edu.com.app.personal.login;

import android.support.design.widget.TextInputLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

import edu.com.base.presenter.BasePresenter;
import edu.com.base.ui.BaseView;

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
