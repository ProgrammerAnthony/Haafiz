package edu.com.mvpcommon.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;


import edu.com.mvpcommon.R;
import edu.com.mvplibrary.ui.activity.AbsBaseActivity;
import edu.com.mvplibrary.ui.widget.CircleImageView;
import edu.com.mvplibrary.ui.widget.imageloader.ImageLoader;
import edu.com.mvplibrary.ui.widget.imageloader.ImageLoaderUtil;
import edu.com.mvplibrary.util.BaseUtil;
import edu.com.mvplibrary.util.ToastUtils;

/**
 * Created by Anthony on 2016/5/3.
 * Class Note:
 * 1 use{@link DrawerLayout} to
 * acts as a top-level container for window content that allows for
 * interactive "drawer" views to be pulled out from the edge of the window.
 * 2 View in MVP
 * see {@link DrawerMainContract}------Manager role of MVP
 * {@link DrawerMainPresenter}---------Presenter
 * &{@link DrawerMainActivity}---------View
 * &{@link DrawerData}------------Model
 */
public class DrawerMainActivity extends AbsBaseActivity implements DrawerMainContract.View, View.OnClickListener {
    private Toolbar actionBarToolbar;
    public static DrawerLayout drawerLayout;
    private ListView mDrawerMenu;
    private CircleImageView mUserImg;
    private NavDrawerListAdapter mAdapter;
   private DrawerMainPresenter mPresenter;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_drawer;
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    protected void initViewsAndEvents() {
//        super.initViewsAndEvents();//一定要调用super，进行父类中的一些初始化操作
        initDrawerLayout();
        setupToolBar();

        //hide toolBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mUserImg = (CircleImageView) findViewById(R.id.user_img);
        mUserImg.setOnClickListener(this);
        mDrawerMenu = (ListView) findViewById(R.id.left_menu);


//        mPresenter = new DrawerMainPresenter(this, mContext);
        //create and initialize presenter,
       mPresenter= new DrawerMainPresenter(this,mContext);

        //use presenter to load data
        mPresenter.getDrawerData();
        //default select first fragment
        mPresenter.getSelectView(1);
    }




    protected void initDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout == null) {
            // current activity does not have a drawer.
            return;
        }
        if (getLeftDrawerID() != 0) {
            FrameLayout leftLayout = (FrameLayout) findViewById(R.id.left_drawer_layout);
            View nav_drawer_layout = getLayoutInflater().inflate(getLeftDrawerID(), null);
            leftLayout.addView(nav_drawer_layout);
        }

    }


    protected void setupToolBar() {
        if (actionBarToolbar == null) {
            actionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (actionBarToolbar != null) {
                setSupportActionBar(actionBarToolbar);
            }
        }
        final ActionBar ab = getSupportActionBar();
        if (ab == null)
            return;
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }


    public static void openDrawer() {
        if (drawerLayout == null)
            return;
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer() {
        if (drawerLayout == null)
            return;
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    /**
     * close drawer if drawer is opening
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }


    protected int getLeftDrawerID() {
        return R.layout.nav_drawer_layout;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.user_img) {
//            mPresenter.onDrawerIconClicked();
            mPresenter.getSelectView(0);
        }
    }

    @Override
    public void onDrawerListGet(final ArrayList<DrawerData.DrawerItem> mDrawerItems) {
        mAdapter = new NavDrawerListAdapter(this,
                mDrawerItems);
        mDrawerMenu.setAdapter(mAdapter);
        mDrawerMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!BaseUtil.isEmpty(mDrawerItems, i)) {
                    DrawerData.DrawerItem drawerItem = mDrawerItems.get(i);
                    if (drawerItem != null) {
                        mPresenter.getSelectView(i+1);
                    }
                }
            }
        });
    }

    @Override
    public void setDrawerIcon(String url) {
        ImageLoader imageLoader =new ImageLoader.Builder().url(url).imgView(mUserImg).build();
        ImageLoaderUtil.getInstance().loadImage(this,imageLoader);
    }

    @Override
    public void onSelectFragmentGet(Fragment fragment) {
        closeDrawer();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content, fragment).commit();

    }


    @Override
    protected View getLoadingTargetView() {
        return null;
    }


//    @Override
//    public void setPresenter(DrawerMainContract.Presenter presenter) {
//        mPresenter= (DrawerMainPresenter) presenter;
//    }

    @Override
    public void showLoading(String msg) {
        toggleShowLoading(true, msg);
    }

    @Override
    public void hideLoading() {
        toggleShowLoading(false, "");
    }

    @Override
    public void showError(String msg, View.OnClickListener onClickListener) {

    }

    @Override
    public void showEmpty(String msg, View.OnClickListener onClickListener) {
        toggleShowEmpty(true, msg, onClickListener);
    }

    @Override
    public void showEmpty(String msg, View.OnClickListener onClickListener, int imageId) {
        toggleShowEmpty(true, msg, onClickListener, imageId);
    }

    @Override
    public void showNetError(View.OnClickListener onClickListener) {
        ToastUtils.getInstance().showToast("oops ,no network now!");
    }
}
