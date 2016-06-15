package edu.com.app.ui.news.channel;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import edu.com.app.data.bean.ChannelEntity;
import edu.com.app.injection.scope.ActivityContext;

/**
 * Created by Anthony on 2016/6/14.
 * Class Note:
 */
public class ChannelChoosePresenter implements ChannelChooseContract.Presenter {
    private Context mContext;

    private ChannelChooseContract.View mView;
    private ArrayList<ChannelEntity> otherItems;
    private ArrayList<ChannelEntity> myItems;

    @Inject
    public ChannelChoosePresenter(@ActivityContext Context context) {
        mContext = context;
    }

    @Override
    public void initChannelData() {
        //step1 load data TODO: 2016/6/14  load  online data if needed
        myItems = new ArrayList<>();
        for (int i = 0; i < 18; i++) {
            ChannelEntity entity = new ChannelEntity();
            entity.setName("频道" + i);
            myItems.add(entity);
        }
        otherItems = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ChannelEntity entity = new ChannelEntity();
            entity.setName("其他" + i);
            otherItems.add(entity);
        }

        //step2 parse data to view & show
        mView.showChannelChooseData(myItems, otherItems);

    }

    @Override
    public void saveData(ArrayList<ChannelEntity> myChannels) {
        mView.toMainActivity();

    }

    @Override
    public void attachView(ChannelChooseContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }
}
