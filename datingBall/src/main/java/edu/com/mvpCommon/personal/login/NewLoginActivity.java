package edu.com.mvpCommon.personal.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import edu.com.mvpCommon.R;
import edu.com.base.ui.activity.AbsSwipeBackActivity;
import edu.com.base.util.LogUtil;
import edu.com.mvpCommon.main.MainActivity;
import jp.wasabeef.blurry.Blurry;
import shem.com.materiallogin.MaterialLoginView;
import shem.com.materiallogin.MaterialLoginViewListener;

/**
 * Created by Anthony on 2016/5/24.
 * Class Note:
 */
public class NewLoginActivity extends AbsSwipeBackActivity implements LoginContract.View {

    LoginPresenter mLoginPresenter;
    @Bind(R.id.splash_bg)
    ImageView splashBg;
    @Bind(R.id.tv_slogan)
    TextView tvSlogan;
    @Bind(R.id.login)
    MaterialLoginView login;
    @Bind(R.id.tv_loginOrRegister)
    TextView tvLoginOrRegister;
    @Bind(R.id.shimmer_layout)
    ShimmerFrameLayout shimmerLayout;
    @Bind(R.id.rootview)
    RelativeLayout rootview;

    boolean isBlured = false;


    @Override
    protected void initViewsAndEvents() {
        mLoginPresenter = new LoginPresenter(mContext);
        mLoginPresenter.attachView(this);
        mLoginPresenter.beginAnimation(splashBg, tvSlogan, shimmerLayout);
        mLoginPresenter.doingSplash();
//        EMClient.getInstance().chatManager().loadAllConversations();
        login.setListener(new MaterialLoginViewListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onRegister(TextInputLayout registerUser, TextInputLayout registerPass, TextInputLayout registerPassRep) {
                mLoginPresenter.Register(registerUser, registerPass, registerPassRep);

            }

            @Override
            public void onLogin(TextInputLayout loginUser, TextInputLayout loginPass) {
                mLoginPresenter.Login(loginUser, loginPass);
            }
        });
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_new_login;
    }

    @Override
    protected void initDagger() {

    }

    @Override
    protected void initToolBar() {

    }


    @Override
    public void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean isLoginViewShowing() {
        return login.getVisibility() == View.VISIBLE;
    }

    @Override
    public void showLoginView() {
        if (!isBlured) {
            Blurry.with(NewLoginActivity.this).radius(25).sampling(2).async().capture(splashBg).into(splashBg);
            isBlured = true;
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(login, "alpha", 0.1f, 1f);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Logger.d("onAnimationEnd:visable");
                ViewGroup parent = (ViewGroup) login.getParent();
                if (parent != null) {
                    login.setVisibility(View.VISIBLE);
                }
            }
        });
        animator.start();
    }

    @Override
    public void dismissLoginView() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(login, "alpha", 1f, 0.1f);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                LogUtil.d("onAnimationEnd:dismiss");

                ViewGroup parent = (ViewGroup) login.getParent();
                if (parent != null) {
                    login.setVisibility(View.GONE);
                }
            }
        });
        animator.start();
    }

    @Override
    public void hideLoginButton() {
        tvLoginOrRegister.setVisibility(View.GONE);
    }

    @Override
    public void showLoginButton() {
        tvLoginOrRegister.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.tv_loginOrRegister)
    public void onClick() {
        if (!isLoginViewShowing()) {
            showLoginView();
        } else {
            dismissLoginView();
        }
    }



}
