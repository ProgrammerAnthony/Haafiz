package edu.com.app.ui.main;

import android.content.Intent;
import android.os.Bundle;
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

//import com.hyphenate.easeui.EaseConstant;

import java.io.File;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import edu.com.app.base.widget.dialog.DialogManager;
import edu.com.app.data.rx.RxBus;
import edu.com.app.base.widget.dialog.ChoosePicDialog;
import edu.com.app.base.widget.statusbar.StatusBarUtil;
import edu.com.app.base.widget.ViewDisplay;
import edu.com.app.R;
import edu.com.app.ui.chat.ChattingListFragment;
import edu.com.app.ui.personal.info.PersonalInfoActivity;
import edu.com.app.ui.personal.login.NewLoginActivity;
import edu.com.app.ui.setting.about.AboutActivity;
import edu.com.app.data.bean.Channel;
import edu.com.app.base.AbsBaseActivity;
import edu.com.app.data.local.PreferencesHelper;
import pl.aprilapps.easyphotopicker.EasyImage;

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
 * &{@link }------------Model
 */
public class MainActivity extends AbsBaseActivity implements MainContract.View, NavigationView.OnNavigationItemSelectedListener {

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

    @Inject
    MainPresenter mMainPresenter;

    @Inject
    RxBus mRxBus;

    @Inject
    ChoosePicDialog mPicDialog;

    @Inject
    PreferencesHelper mPref;

    @Inject
    ViewDisplay viewDisplay;

    ImageView iv_avatar;
    TextView tv_nick;

    private static final String EXTRA_TRIGGER_SYNC_FLAG = "edu.com.app.module.main.MainActivity.EXTRA_TRIGGER_SYNC_FLAG";
    //    private MainContract.Presenter mMainPresenter;
    //    private RxBus mRxBus;
    private boolean isLogin;

    private boolean isConflictDialogShow = false;
    //    private RxLeanCloud mRxLeanCloud;
//    private ChoosePicDialog mPicDialog;//底部选择对话框（图库，照相，取消）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.detachView();
    }

    @Override
    protected void initViewsAndEvents() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
//        mPicDialog = new ChoosePicDialog(this);
        // todo 消息推送
//        PushService.setDefaultPushCallback(this, MainActivity.class);
        // todo 账号在异地登陆处理
//        if (getIntent().getBooleanExtra(EaseConstant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
//            ConflictAngRestart();
//        }
        //todo  获取登陆状态,默认为false
        isLogin = mPref.isLogined();
        //初始化Presenter

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
        fab.setVisibility(View.INVISIBLE);

        mMainPresenter.initData(iv_avatar, tv_nick, collapsingBackground);

        navView.getMenu().getItem(0).setChecked(true);

        mMainPresenter.replaceFragment(new ChattingListFragment(), "聊天", false);
        collapsingToolbarLayout.setTitle("聊天");

        //todo  同步数据
//        if (getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
//            startService(SyncService.getStartIntent(this));
//        }
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_main;
    }


    @Override
    protected void injectDagger() {

        activityComponent().inject(this);
 /*       DaggerMainActivityComponent.builder().mainActivityModule(new MainActivityModule(this, mMainPresenter, new ChoosePicDialog(this)))
                .applicationComponent(((MyApplication) getApplication()).getAppComponent()).build().inject(this);*/
    }

    @Override
    protected void initToolBar() {

    }


    @Override
    public void toLoginActivity() {
        Intent intent = new Intent(this, NewLoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void toPersonalActivity() {
        Intent intent = new Intent(this, PersonalInfoActivity.class);
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
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            DialogManager.showErrorDialog(mContext, st, "The same account was loggedin in other device", new SweetAlertDialog.OnSweetClickListener() {
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
        mPicDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Channel mChannel = new Channel();
        String mFragmentTag = null;
        switch (id) {
            case R.id.nav_chat:                //聊天列表fragment
                mChannel.setType("2002");
                mFragmentTag = "聊天";
                collapsingToolbarLayout.setTitle("聊天");
                break;
            case R.id.nav_friends:                 //好友列表fragment
                mChannel.setType("2003");
                mFragmentTag = "好友";
                collapsingToolbarLayout.setTitle("好友");
                break;
            case R.id.nav_find:                  //发现列表fragment（好友圈+附近的人）
                mChannel.setType("2007");  // TODO: 2016/5/26  目前是NearByListFragment 附近的人，待修改
                mFragmentTag = "发现";
                collapsingToolbarLayout.setTitle("发现");
                break;
            case R.id.nav_news:                 //资讯列表fragment
                mChannel.setType("2005");
                mFragmentTag = "资讯";
                collapsingToolbarLayout.setTitle("资讯");
                break;
            case R.id.nav_setting:                //设置界面（activity）
                mChannel.setType("1005");
                mFragmentTag = null;
                break;
            default:
                break;
        }
        Fragment fragment = viewDisplay.initialView(mContext, mChannel);
        if (mFragmentTag != null && fragment != null) {
            mMainPresenter.replaceFragment(fragment, mFragmentTag, false);
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        return true;
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

    // TODO: 2016/5/31  process onActivity Result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
/*        if (requestCode == ChangeProfile && resultCode == RESULT_OK) {
            tv_nick.setText(AVUser.getCurrentUser().getString(UserDao.NICK));
            Glide.with(this).load(AVUser.getCurrentUser().getString(UserDao.AVATARURL)).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).bitmapTransform(new CropCircleTransformation(this)).into(iv_avatar);
        }
*/
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource imageSource, int i) {
//                showToast("出错啦，请稍后再试");
            }

            @Override
            public void onImagePicked(File file, EasyImage.ImageSource imageSource, int i) {
//                UCrop.Options options = new UCrop.Options();
//                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
//                if (isAlbum) {
//                    UCrop.of(Uri.fromFile(file), Uri.fromFile(file))
//                            .withOptions(options)
//                            .start(MainActivity.this, AlbumPhoto);
//                } else {
//                    UCrop.of(Uri.fromFile(file), Uri.fromFile(file))
//                            .withOptions(options)
//                            .start(MainActivity.this, GalleryPhoto);
//                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource imageSource, int i) {
                if (imageSource == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(MainActivity.this);
                    if (photoFile != null) {
                        photoFile.delete();
                    }

                }
            }
        });
/*
        if (resultCode == RESULT_OK && requestCode == GalleryPhoto) {
            mRxbus.post(new UploadPhotoUri(0, UCrop.getOutput(data)));
        } else if (resultCode == RESULT_OK && requestCode == AlbumPhoto) {
            Glide.with(MainActivity.this).load(UCrop.getOutput(data)).crossFade().fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL).into(ivAlbum);
            mMainPersenter.UploadPicTure(UCrop.getOutput(data));
        } else if (resultCode == UCrop.RESULT_ERROR) {
            //noinspection ConstantConditions
            Logger.e(UCrop.getError(data).getMessage());
            showToast("出错啦，重新试试吧");
        }
*/

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
