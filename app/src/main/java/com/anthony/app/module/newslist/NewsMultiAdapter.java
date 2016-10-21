package com.anthony.app.module.newslist;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.anthony.app.R;
import com.anthony.app.common.data.bean.NewsItem;
import com.anthony.rvhelper.adapter.MultiItemTypeAdapter;
import com.anthony.rvhelper.base.ItemViewDelegate;
import com.anthony.rvhelper.base.ViewHolder;


/**
 * Created by Anthony
 * <p>
 * 支持多种样式+顶部轮播图的新闻列表样式
 */
public class NewsMultiAdapter extends MultiItemTypeAdapter<NewsItem> {

    public NewsMultiAdapter(Context context) {
        super(context);
        addItemViewDelegate(new TodayTopicDelegate());// docType =  1,  今日头条样式
        addItemViewDelegate(new NormalNewsViewDelegate());//docType = 2,  左侧图片 + 右侧标题，描述字段样式
        //todo add more


    }


    /*
     docType = 0，顶部轮播图；
     docType = 1,  今日头条样式；
     docType = 2,  左侧图片 + 右侧标题，描述字段样式
     docType = 3,  专题样式；
     docType = 4,  纯文字样式；
     docType = 5,  顶部标题+下方大图样式；
     docType = 6, 顶部标题+下方三张图片样式；

     todo 图集....更多样式
        */
    public class TodayTopicDelegate implements ItemViewDelegate<NewsItem> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.prj_news_item_today_topic;
        }

        @Override
        public boolean isForViewType(NewsItem item, int position) {
            return item.getType() == 1;
        }

        @Override
        public void convert(ViewHolder holder, NewsItem item, int position) {
            holder.setText(R.id.tv_title_center, item.getTitle());
            holder.setText(R.id.tv_news_date, item.getTime());
        }
    }


    /**
     * 普通的新闻列表项样式 ，图片+标题+时间+新闻来源
     * 无图片的新闻列表样式，标题+时间+来源
     */
    public class NormalNewsViewDelegate implements ItemViewDelegate<NewsItem> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.prj_news_item_normal_news;
        }

        @Override
        public boolean isForViewType(NewsItem item, int position) {
            return item.getType() != 1;
        }

        @Override
        public void convert(ViewHolder holder, NewsItem item, int position) {
            if (item.getImgs() != null || !item.getImgs().get(0).equals("")) {//是否有图
                holder.getView(R.id.img_news_image).setVisibility(View.VISIBLE);
                String url = item.getImgs().get(0);
                holder.setImageUrl(R.id.img_news_image, url);

            } else {
                holder.getView(R.id.img_news_image).setVisibility(View.GONE);
            }
            holder.setText(R.id.tv_title_center, item.getTitle());//标题

            if (item.getSummary() == null || item.getSummary().equals("")) {//是否有来源字段
                holder.setVisible(R.id.tv_news_source, false);
            } else {
                holder.setVisible(R.id.tv_news_source, true);
                holder.setText(R.id.tv_news_source, item.getSummary());
            }

            if (TextUtils.isEmpty(item.getDate())) {  //是否有日期
                holder.setVisible(R.id.tv_news_date, false);
                holder.setVisible(R.id.news_date_icon, false);
            } else {
                holder.setVisible(R.id.news_date_icon, true);
                holder.setVisible(R.id.tv_news_date, true);
                holder.setText(R.id.tv_news_date, item.getDate());
            }

        }
    }

}
