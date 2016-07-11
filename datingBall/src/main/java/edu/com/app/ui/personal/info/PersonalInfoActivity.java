package edu.com.app.ui.personal.info;

import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import edu.com.app.R;
import edu.com.app.base.AbsSwipeBackActivity;
import edu.com.app.base.widget.autoscrollViewPager.AutoScrollViewPager;
import edu.com.app.base.widget.autoscrollViewPager.ImagePagerAdapter;
import edu.com.app.base.widget.recyclerviewHelper.ParallaxRecyclerAdapter;

import edu.com.app.util.ListUtils;
import edu.com.app.util.ToastUtils;

/**
 * Created by Anthony on 2016/5/26.
 * Class Note:
 * 利用RecyclerView 处理个人中心，利用{@link ParallaxRecyclerAdapter}实现视差效果。
 * todo 处理（头像和个人签名）
 * todo 处理编辑编辑界面
 */
public class PersonalInfoActivity extends AbsSwipeBackActivity implements PersonalInfoContract.View {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;


    @Bind(R.id.personal_top_bar)
    RelativeLayout personalTopBar;

    @Bind(R.id.delete_account_txt)
    RelativeLayout deleteAccountTxt;


    @Inject
    ToastUtils toastUtils;
    private AutoScrollViewPager autoScrollViewpager;
    private List<Integer> imageIdList;

    @Override
    protected void initViewsAndEvents() {
//        StatusBarUtil.setTranslucent(this);
//        StatusBarUtil.setColor(this,R.color.colorPrimary);
        createAdapter(recyclerView);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_personal;
    }

    @Override
    protected void injectDagger() {
        activityComponent().inject(this);
    }

    @Override
    protected void initToolBar() {

    }

    /**
     * create adapter for recyclerView using {@link ParallaxRecyclerAdapter}
     *
     * @param recyclerView
     */
    private void createAdapter(RecyclerView recyclerView) {
        final List<String> content = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            content.add("item " + i);
        }

        final ParallaxRecyclerAdapter<String> adapter = new ParallaxRecyclerAdapter<String>(content) {
            @Override
            public void onBindViewHolderImpl(RecyclerView.ViewHolder viewHolder, ParallaxRecyclerAdapter<String> adapter, int i) {
                ((ViewHolder) viewHolder).textView.setText(adapter.getData().get(i));
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup, final ParallaxRecyclerAdapter<String> adapter, int i) {
                return new ViewHolder(getLayoutInflater().inflate(R.layout.row_recyclerview, viewGroup, false));
            }

            @Override
            public int getItemCountImpl(ParallaxRecyclerAdapter<String> adapter) {
                return content.size();
            }
        };

        adapter.setOnClickEvent(new ParallaxRecyclerAdapter.OnClickEvent() {
            @Override
            public void onClick(View v, int position) {
                Toast.makeText(PersonalInfoActivity.this, "You clicked '" + position + "'", Toast.LENGTH_SHORT).show();
            }
        });

        //alpha effect in TopBar
        adapter.setOnParallaxScroll(new ParallaxRecyclerAdapter.OnParallaxScroll() {
            @Override
            public void onParallaxScroll(float percentage, float offset, View parallax) {
                Drawable c = personalTopBar.getBackground();
                c.setAlpha(Math.round(percentage * 255));
                personalTopBar.setBackground(c);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        View header = getLayoutInflater().inflate(R.layout.header, recyclerView, false);
        adapter.setParallaxHeader(header, recyclerView);
        adapter.setData(content);
        recyclerView.setAdapter(adapter);

        autoScrollViewpager = (AutoScrollViewPager) header.findViewById(R.id.auto_scroll_viewpager);
//using autoScrollViewpager
        imageIdList = new ArrayList<>();
        imageIdList.add(R.drawable.banner1);
        imageIdList.add(R.drawable.banner2);
        imageIdList.add(R.drawable.banner3);
        imageIdList.add(R.drawable.banner4);
        autoScrollViewpager.setAdapter(new ImagePagerAdapter(mContext, imageIdList).setInfiniteLoop(true));
        autoScrollViewpager.setOnPageChangeListener(new MyOnPageChangeListener());

        autoScrollViewpager.setInterval(3500);
        autoScrollViewpager.startAutoScroll();
        autoScrollViewpager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % ListUtils.getSize(imageIdList));

        // the more properties whose you can set
        // // set whether stop auto scroll when touching, default is true
        // viewPager.setStopScrollWhenTouch(false);
        // // set whether automatic cycle when auto scroll reaching the last or first item
        // // default is true
        // viewPager.setCycle(false);
        // /** set auto scroll direction, default is AutoScrollViewPager#RIGHT **/
        // viewPager.setDirection(AutoScrollViewPager.LEFT);
        // // set how to process when sliding at the last or first item
        // // default is AutoScrollViewPager#SLIDE_BORDER_NONE
        // viewPager.setBorderProcessWhenSlide(AutoScrollViewPager.SLIDE_BORDER_CYCLE);
        // viewPager.setScrollDurationFactor(3);
        // viewPager.setBorderAnimation(false);


    }

    @OnClick({R.id.title_image_left, R.id.title_image_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_image_left:
                scrollToFinishActivity();
                Drawable c = personalTopBar.getBackground();
                c.setAlpha(Math.round(255));
                personalTopBar.setBackground(c);
                break;
            case R.id.title_image_right:
                toastUtils.showToast("修改个人数据");
                break;
        }
    }

    @Override
    public void finish() {
        Drawable c = personalTopBar.getBackground();
        c.setAlpha(Math.round(255));
        super.finish();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
/*            indexText.setText(new StringBuilder().append((position) % ListUtils.getSize(imageIdList) + 1).append("/")
                    .append(ListUtils.getSize(imageIdList)));*/
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // stop auto scroll when onPause
        autoScrollViewpager.stopAutoScroll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // start auto scroll when onResume
        autoScrollViewpager.startAutoScroll();
    }

    /*******
     * implements method in MVP View
     **************/
    @Override
    public void updateInfo() {

    }

    @Override
    public void toEditInfoActivity() {

    }

    @Override
    public void toMainActivity() {

    }

}