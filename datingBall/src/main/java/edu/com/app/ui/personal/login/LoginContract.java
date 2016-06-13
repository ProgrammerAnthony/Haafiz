package edu.com.app.ui.personal.login;

import android.support.design.widget.TextInputLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

import edu.com.app.ui.base.BasePresenter;
import edu.com.app.ui.base.BaseView;

/**
 * Created by Anthony on 2016/5/16.
 * Class Note:
 */
public interface LoginContract {
    interface Presenter extends BasePresenter<View> {
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
