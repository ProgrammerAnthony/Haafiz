package edu.com.mvpCommon.main;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.PushService;
//import com.hyphenate.easeui.EaseConstant;

import butterknife.Bind;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import edu.com.mvpCommon.R;
import edu.com.mvpCommon.personal.info.PersonalInfoActivity;
import edu.com.mvpCommon.personal.login.NewLoginActivity;
import edu.com.mvpCommon.setting.about.AboutActivity;
import edu.com.base.model.bean.Channel;
import edu.com.base.model.rx.RxLeanCloud;
import edu.com.base.ui.activity.AbsBaseActivity;
import edu.com.base.util.PreferenceManager;

/**
 * Created by Anthony on 2016/5/3.
 * Class Note:
 * 1 use{@link DrawerLayout} to
 * acts as a top-level container for window content that allows for
 * interactive "drawer" views to be pulled out from the edge of the window.
 * 2 View in MVP
 * see {@link MainContract}------Manager role of MVP
 * {@link MainPresenter}---------Presenter
 * &{@link MainActivity}---------View
 * &{@link DrawerData}------------Model
 */
public class NewDrawerMainActivity extends AbsBaseActivity implements NewMainContract.View, NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.collapsingBackground)
    ImageView collapsingBackground;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.app_bar)
    AppBarLayout appBar;
    @Bind(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    ImageView iv_avatar;
    TextView tv_nick;

    private NewMainContract.Presenter mMainPresenter;
    //    private RxBus mRxBus;
    private boolean isLogin;

    private boolean isConflictDialogShow = false;
    private RxLeanCloud mRxLeanCloud;
    @Override
    protected void initViewsAndEvents() {
        // 消息推送
        PushService.setDefaultPushCallback(this, MainActivity.class);
        // 账号在异地登陆处理
//        if (getIntent().getBooleanExtra(EaseConstant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
//            ConflictAngRestart();
//        }
        //  获取登陆状态
        isLogin = PreferenceManager.getInstance().isLogined();
        //初始化Presenter
        mMainPresenter = new NewMainPresenter(mContext,mRxLeanCloud);
        mMainPresenter.attachView(this);

        setSupportActionBar(toolbar);
        navView.setNavigationItemSelectedListener(this);
        //noinspection ConstantConditions
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);
        View headerView = navView.getHeaderView(0);
        iv_avatar = (ImageView) headerView.findViewById(R.id.iv_avatar);
        tv_nick = (TextView) headerView.findViewById(R.id.tv_nick);
        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //头像点击处理
                mMainPresenter.processHeadIconClicked(isLogin);
            }
        });

        mMainPresenter.initData(iv_avatar, tv_nick, collapsingBackground);

        navView.getMenu().getItem(0).setChecked(true);

        mMainPresenter.replaceFragment(new Fragment(), "聊天列表", true);


    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initDagger() {

    }


    @Override
    public void toLoginActivity() {
        Intent intent =new Intent(this, NewLoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void toPersonalActivity() {
        Intent intent =new Intent(this, PersonalInfoActivity.class);
        startActivity(intent);

    }

    @Override
    public void showScrollView() {
        appBar.setExpanded(true, true);
    }

    @Override
    public void hideScrollView() {
        appBar.setExpanded(false, true);
    }

    @Override
    public void ConflictAngRestart() {
        /**
         * 显示帐号在别处登录dialog
         */
        isConflictDialogShow = true;
        String st = "Logoff notification";
        if (!NewDrawerMainActivity.this.isFinishing()) {
            // clear up global variables
            showErrorDialog(st, "The same account was loggedin in other device", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    mMainPresenter.Logout();
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
        }
    }

    @Override
    public void showPicDialog() {
//        mPicDialog.show();  // TODO: 2016/5/26
    }

    @Override
    public void showMessage(String msg) {
        showMessageDialog(msg);
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public void showProgress(String message) {
        showProgressDialog(message);
    }

    @Override
    public void showProgress(String message, int progress) {
        showProgressDialog(message, progress);
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    public void showErrorMessage(String msg, String content) {
        showErrorDialog(msg, content, new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // TODO: 2016/5/26  navigation item selected
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Channel channel =new Channel();
        switch (id){
            case R.id.nav_chat:                //聊天列表fragment
                channel.setType("2002");
                break;
            case R.id.nav_friends:                 //好友列表fragment
                channel.setType("2003");
                break;
            case R.id.nav_find:                  //发现列表fragment（好友圈+附近的人）
                channel.setType("2004");  // TODO: 2016/5/26  目前是NearByListFragment 附近的人，待修改
                break;
            case R.id.nav_news:                 //资讯列表fragment
                channel.setType("2005");
                break;
            case R.id.nav_setting:                //设置界面（activity）
                channel.setType("1005");
                break;
            default:
                break;
        }

        return  true;
/*        if (id == R.id.nav_moment) {
            if (souvenirFragment == null) {
                souvenirFragment = SouvenirFragment.newInsatance();
            }
            isAlbum = true;
            collaspingToolBarlayout.setTitle("Moment");
            mMainPersenter.replaceFragment(souvenirFragment, "Moment", true);
        } else if (id == R.id.nav_gallery) {
            if (galleryFragment == null) {
                galleryFragment = GalleryFragment.newInstance();
            }
            isAlbum = false;
            collaspingToolBarlayout.setTitle("相册");
            mMainPersenter.replaceFragment(galleryFragment, "Gallery", false);
        } else if (id == R.id.nav_manage) {
            FeedbackAgent agent = new FeedbackAgent(MainActivity.this);
            agent.startDefaultThreadActivity();
        } else if (id == R.id.nav_logout) {
            showWarningDialog("退出", "确定要退出吗？", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                    mMainPersenter.Logout();

                }
            });

        } else if (id == R.id.nav_share) {
            mMainPersenter.Share();
        } else if (id == R.id.nav_send) {
            if (mMainPersenter.isHavedLover()) {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID, User.getCurrentUser(User.class).getLoverusername());
                startActivity(intent);
            } else {
                toProfileActivity();
                showToast("老实说，这是个两个人使用的APP");
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;*/
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);

        }
        return true;
    }

    @OnClick({R.id.collapsingBackground, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.collapsingBackground:
                showPicDialog();
                break;
            case R.id.fab:
                mMainPresenter.fabOnclick();
                break;
            default:
                break;
        }
    }
}
