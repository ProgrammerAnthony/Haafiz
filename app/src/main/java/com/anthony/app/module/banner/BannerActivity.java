package com.anthony.app.module.banner;

import android.widget.RelativeLayout;

import com.anthony.app.R;
import com.anthony.app.common.base.AbsBaseActivity;
import com.anthony.app.common.data.bean.NewsItem;
import com.anthony.app.common.injection.component.ActivityComponent;
import com.anthony.app.common.widgets.banner.RecommendController;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Anthony on 2016/10/10.
 * Class Note:
 * banner view ,using {@link RecommendController} to implement banner view
 *
 */

public class BannerActivity extends AbsBaseActivity {
    String[] str = new String[]{
            "http://g.hiphotos.baidu.com/image/h%3D360/sign=5381d7c63b01213fd03348da64e636f8/fc1f4134970a304efb8e43e5d3c8a786c9175c05.jpg",
            "http://a.hiphotos.baidu.com/image/h%3D360/sign=cb8bf0660db30f242a9aea05f895d192/a8014c086e061d95ba796c3f79f40ad162d9cafe.jpg",
            "http://f.hiphotos.baidu.com/image/pic/item/b151f8198618367ac7d2a1e92b738bd4b31ce5af.jpg",
            "http://c.hiphotos.baidu.com/image/pic/item/0b7b02087bf40ad128102ae7552c11dfa9ecce3a.jpg"
    };
    @BindView(R.id.banner_bg)
    RelativeLayout bannerBg;
    private RecommendController mController;


    @Override
    protected int getContentViewID() {
        return R.layout.prj_layout_banner;
    }

    @Override
    protected void initViewsAndEvents() {
        mController = new RecommendController(mContext);
        mController.setOnClickListener(new RecommendController.OnItemClickListener() {
            @Override
            public void onItemClick(NewsItem topic) {
                showToast(topic.getTitle());
            }

        });
        bannerBg.addView(mController.getView());

        loadData();
    }


    private void loadData() {
        List<NewsItem> newsItems = new ArrayList<>();
        for (int i = 0; i < str.length; i++) {
            NewsItem newsItem = new NewsItem();
            newsItem.setTitle("图片" + (i + 1));
            ArrayList<String> images = new ArrayList<>();
            images.add(str[i]);
            newsItem.setImages(images);
            newsItems.add(newsItem);
        }

        addTopic(newsItems);
    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    public void addTopic(List list) {
        if (mController != null)
            mController.setTopicList(list);
    }
}
