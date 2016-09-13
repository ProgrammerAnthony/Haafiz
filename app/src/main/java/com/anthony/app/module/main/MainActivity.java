package com.anthony.app.module.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.anthony.app.R;
import com.anthony.app.common.base.AbsBaseActivity;
import com.anthony.app.common.injection.component.ActivityComponent;
import com.anthony.app.module.find.FindFragment;
import com.anthony.app.module.local.LocalFragment;
import com.anthony.app.module.mine.MineFragment;
import com.anthony.app.module.store.StoreFragment;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Anthony on 2016/9/12.
 * Class Note:
 * 主界面 ，子模块为：
 * 1 首页
 * 2 本地
 * 3 积分商城
 * 4 发现
 * 5 我的
 */
public class MainActivity extends AbsBaseActivity {
    @Bind(R.id.main_tab_content)
    FrameLayout tabContent;
    @Bind(R.id.prj_layout_reload)
    RelativeLayout prjLayoutReload;
    @Bind(R.id.layout_bottom_tab1_main)
    LinearLayout layoutBottomTab1Main;
    @Bind(R.id.layout_bottom_tab2_local)
    LinearLayout layoutBottomTab2Local;
    @Bind(R.id.layout_bottom_tab3_store)
    LinearLayout layoutBottomTab3Store;
    @Bind(R.id.layout_bottom_tab4_find)
    LinearLayout layoutBottomTab4Find;
    @Bind(R.id.layout_bottom_tab5_mine)
    LinearLayout layoutBottomTab5Mine;
    @Bind(R.id.bottomId)
    LinearLayout bottomId;

    private static final int INIT_TAB_INDEX = 0;

    private int mCurrentIndex = INIT_TAB_INDEX;
    private FragmentManager mFragmentManager;

    @Override
    protected int getContentViewID() {
        return R.layout.prj_bottom_content;
    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initViewsAndEvents() {
        mFragmentManager = getSupportFragmentManager();
        setTabFragmentSelect(0);
    }



    @OnClick({R.id.prj_layout_reload, R.id.layout_bottom_tab1_main, R.id.layout_bottom_tab2_local, R.id.layout_bottom_tab3_store, R.id.layout_bottom_tab4_find, R.id.layout_bottom_tab5_mine})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.prj_layout_reload:
                break;
            case R.id.layout_bottom_tab1_main:
                setTabFragmentSelect(0);
                break;
            case R.id.layout_bottom_tab2_local:
                setTabFragmentSelect(1);
                break;
            case R.id.layout_bottom_tab3_store:
                setTabFragmentSelect(2);
                break;
            case R.id.layout_bottom_tab4_find:
                setTabFragmentSelect(3);
                break;
            case R.id.layout_bottom_tab5_mine:
                setTabFragmentSelect(4);
                break;
        }
    }

    private void setTabFragmentSelect(int index) {
        clearSelectTabState(index);
        switchContentFragment(index);
    }


    private void clearSelectTabState(int index) {
        for (int i = 0; i < bottomId.getChildCount(); i++) {
            View childView = bottomId.getChildAt(i);
            childView.setSelected(false);
        }
        bottomId.getChildAt(index).setSelected(true);
    }

    private void switchContentFragment(int index) {
        String indexTag = Integer.toString(index);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        Fragment from = FindCreateFragment(mCurrentIndex);
        Fragment to = FindCreateFragment(index);
        if (!to.isAdded()) { // 先判断是否被add过
            transaction.hide(from).add(R.id.main_tab_content, to, indexTag); // 隐藏当前的fragment，add下一个到Activity中
            transaction.commit();
        } else {
            transaction.hide(from).show(to); // 隐藏当前的fragment，显示下一个
            transaction.commit();
        }
        mCurrentIndex = index;
    }

    private Fragment FindCreateFragment(int index) {
        String indexTag = Integer.toString(index);
        Fragment f = mFragmentManager.findFragmentByTag(indexTag);
        if (f == null) {
            switch (index) {
                case 0:
                    f = new HomeFragment();
                    break;
                case 1:
                    f = new LocalFragment();
                    break;
                case 2:
                    f = new StoreFragment();
                    break;
                case 3:
                    f = new FindFragment();
                    break;
                case 4:
                    f = new MineFragment();
                    break;
            }
        }
        return f;
    }
}
