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

import edu.com.mvplibrary.R;

import edu.com.mvplibrary.ui.activity.AbsBaseActivity;
import edu.com.mvplibrary.ui.widget.CircleImageView;
import edu.com.mvplibrary.ui.widget.netstatus.NetUtils;
import edu.com.mvplibrary.util.BaseUtil;
import edu.com.mvplibrary.util.ToastUtils;

/**
 * Created by Anthony on 2016/5/3.
 * Class Note:
 * 1 use{@link DrawerLayout} to
 * acts as a top-level container for window content that allows for
 * interactive "drawer" views to be pulled out from the edge of the window.
 * 2 View in MVP
 * see {@link DrawerMainContract}-------------------------------------------Manager role of MVP
 * {@link DrawerMainPresenter}------Presenter
 * &{@link DrawerMainActivity}-------------View
 * &{@link DrawerItemsData}------------------------------------------------Model
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
    protected void initViewsAndEvents() {
        super.initViewsAndEvents();//一定要调用super，进行父类中的一些初始化操作
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
        //create and initialize presenter
        mPresenter = new DrawerMainPresenter(this, mContext);
        mPresenter.getDrawerList();
//        selectItem(0);
        mPresenter.getSelectFragment(0);
    }




    protected void initDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout == null) {
            // current activity does not have a drawer.
            return;
        }
        if (getDrawerLayoutId() != 0) {
            FrameLayout leftLayout = (FrameLayout) findViewById(R.id.left_drawer_layout);
            View nav_drawer_layout = getLayoutInflater().inflate(getDrawerLayoutId(), null);
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


    protected int getDrawerLayoutId() {
        return R.layout.nav_drawer_layout;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.user_img) {
            mPresenter.onDrawerIconClicked();
        }
    }

    @Override
    public void onDrawerListGet(final ArrayList<DrawerItemsData.DrawerItem> mDrawerItems) {
        mAdapter = new NavDrawerListAdapter(this,
                mDrawerItems);
        mDrawerMenu.setAdapter(mAdapter);
        mDrawerMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!BaseUtil.isEmpty(mDrawerItems, i)) {
                    DrawerItemsData.DrawerItem drawerItem = mDrawerItems.get(i);
                    if (drawerItem != null) {
                        mPresenter.getSelectFragment(i);
                    }
                }
            }
        });
    }

    @Override
    public void setDrawerIcon(int resId) {
        mUserImg.setImageResource(resId);
    }

    @Override
    public void onSelectFragmentGet(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content, fragment).commit();
        closeDrawer();
    }


    private void selectItem(int position) {
//        Fragment fragment = mPresenter.getSelectFragment(position);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.content, fragment).commit();
//        closeDrawer();
    }


    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {
        ToastUtils.getInstance().showToast("this type is"+type);
    }

    @Override
    protected void onNetworkDisConnected() {
        ToastUtils.getInstance().showToast("no network now");
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }
}
