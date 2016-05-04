package edu.com.mvplibrary.contract;

import edu.com.mvplibrary.contract.view.BaseView;

/**
 * Created by Anthony on 2016/5/3.
 * Class Note:
 */
public interface NewsListContract {
    interface Presenter {
        void getTopImgs();

        void getNewsList();
    }

    interface View extends BaseView {
        void showTopImgs();

        void showNewsList();
    }
}
