package edu.com.app.module.splash;

import android.content.Context;
import android.os.AsyncTask;

import javax.inject.Inject;

import edu.com.app.injection.scope.ActivityContext;
import edu.com.app.util.ToastUtils;

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

    @Inject
    ToastUtils toastUtils;

    @Inject
    public SplashPresenter(@ActivityContext Context context) {
        mContext = context;
    }


    @Override
    public void initData() {
        mShowMainTime = System.currentTimeMillis() + SPLASH_SHOW_SECONDS * 2000;
        // TODO: 2016/5/31  url to get data
        firstUrl = "file://xxx";
        showView();
/*         HttpRequest.Builder builder = new HttpRequest.Builder();
        HttpRequest request = builder.url(firstUrl).build();
       HttpUtil.getInstance(mContext).loadString(request, new StringHttpCallback() {
            @Override
            public void onResponse(String response) {
                // TODO: 2016/5/31  url get
                showView();
                toastUtils.showToast("获取数据成功");
            }

            @Override
            public void onError(String error) {
                // TODO: 2016/5/31 get url failed
                showView();
                toastUtils.showToast("获取数据失败");
            }
        });*/
    }

    // TODO: 2016/5/31  using rxJava to process
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

//    @Override
//    public void attachView(BaseView view) {
//        mView = (SplashContract.View) view;
//    }

    @Override
    public void attachView(SplashContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }


}
