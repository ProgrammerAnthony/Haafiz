package edu.com.app.module.personal.info;

import rx.Subscription;

/**
 * Created by Anthony on 2016/6/16.
 * Class Note:todo
 *
 */
public class PersonalInfoPresenter implements PersonalInfoContract.Presenter{

    private PersonalInfoContract.View mView;

    private Subscription mSubscription;
    @Override
    public void processAlphaTopBar() {

    }

    @Override
    public void loadData() {

    }

    @Override
    public void edit() {

    }

    @Override
    public void attachView(PersonalInfoContract.View view) {
        this.mView =view;
    }

    @Override
    public void detachView() {
        mView = null;
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
