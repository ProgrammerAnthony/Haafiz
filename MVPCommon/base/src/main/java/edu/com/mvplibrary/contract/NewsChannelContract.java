package edu.com.mvplibrary.contract;

import android.support.v4.app.Fragment;

import edu.com.mvplibrary.contract.presenter.NewsChannelPresenter;
import edu.com.mvplibrary.contract.view.BaseView;
import edu.com.mvplibrary.model.Channel;
import edu.com.mvplibrary.model.NewsChannelData;

/**
 * Created by Anthony on 2016/5/3.
 * Class Note:
 * 1 define your Views & Presenters in Contract Classes;
 * 2 define listener used for View & Model indirectly interaction;
 * 3 {@link NewsChannelContract}-------------------------------------------Manager role of MVP
 * {@link NewsChannelPresenter}---------Presenter
 * &{@link edu.com.mvplibrary.ui.fragment.NewsChannelFragment}---------------------------View
 * &{@link  NewsChannelData}--------------------Model
 *
 */
public interface NewsChannelContract {

    interface Presenter {
        void loadChannel();

        void loadDetailFragment();
    }

    interface View extends BaseView {
        void showChannel(Channel channel);

        void showDetailFragment(Fragment fragment);
    }

    interface onGetChannelListListener{
        void onSuccess();
        void onError();
    }
}
