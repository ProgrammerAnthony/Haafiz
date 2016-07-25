package edu.com.app.module.splash;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import javax.inject.Inject;

import edu.com.app.MyApplication;
import edu.com.app.data.DataManager;
import edu.com.app.data.bean.Menu;
import edu.com.app.data.retrofit.HttpSubscriber;
import edu.com.app.injection.scope.ActivityContext;
import edu.com.app.util.ToastUtils;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Anthony on 2016/5/31.
 * Class Note:
 * presenter for splash view
 */
public class SplashPresenter implements SplashContract.Presenter {

    private SplashContract.View mView;
    private Context mContext;
    private String firstUrl;
    private static final short SPLASH_SHOW_SECONDS = 1;
    private long mShowMainTime;
    private Subscription mSubscription;
    @Inject
    ToastUtils toastUtils;

    @Inject
    DataManager dataManager;

    @Inject
    Application application;

    @Inject
    public SplashPresenter(@ActivityContext Context context) {
        mContext = context;
    }


    @Override
    public void initData(Subscription subscription) {
        mShowMainTime = System.currentTimeMillis() + SPLASH_SHOW_SECONDS * 2000;
//        firstUrl = "file://xxx";
//        showView();

        mSubscription=subscription;

        mSubscription = dataManager.loadMenu(getFirstMenuUrl()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpSubscriber<Menu>() {
                    @Override
                    public void onNext(Menu menu) {
                        MyApplication.setFirstLevelMenu(menu);
                        showView();
                    }
                });
    }

    private String getFirstMenuUrl() {
        return  "raw://main_menu";
    }

    private void showView() {
        AsyncTask<String, String, String> showMainTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String[] params) {
                if (System.currentTimeMillis() < mShowMainTime) {
                    try {
                        long sleepTime = mShowMainTime - System.currentTimeMillis();
                        if (sleepTime > 0) {
                            Thread.sleep(mShowMainTime - System.currentTimeMillis());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(String o) {
                mView.toMainActivity();
                mView.close();
            }
        };

        showMainTask.execute();
    }

    @Override
    public void attachView(SplashContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView=null;
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }


}
