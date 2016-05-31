package edu.com.app.splash;

import android.content.Context;
import android.os.AsyncTask;

import edu.com.base.model.http.HttpUtil;
import edu.com.base.model.http.callback.StringHttpCallback;
import edu.com.base.model.http.request.HttpRequest;
import edu.com.base.ui.BaseView;
import edu.com.base.util.ToastUtils;

/**
 * Created by Anthony on 2016/5/31.
 * Class Note:
 */
public class SplashPresenterImpl implements SplashContract.Presenter {
    private SplashContract.View mView;
    private Context mContext;
    private String firstUrl;
    private static final short SPLASH_SHOW_SECONDS = 1;
    private long mShowMainTime;


    public SplashPresenterImpl(Context mContext) {
        this.mContext = mContext;
        mShowMainTime = System.currentTimeMillis() + SPLASH_SHOW_SECONDS * 2000;
    }


    @Override
    public void initData() {
        // TODO: 2016/5/31  url to get data
        firstUrl = "file://xxx";
        HttpRequest.Builder builder = new HttpRequest.Builder();
        HttpRequest request = builder.url(firstUrl).build();
        HttpUtil.getInstance().loadString(request, new StringHttpCallback() {
            @Override
            public void onResponse(String response) {
                // TODO: 2016/5/31  url get
                showView();
                ToastUtils.getInstance().showToast("获取数据成功");
            }

            @Override
            public void onError(String error) {
                // TODO: 2016/5/31 get url failed
                showView();
                ToastUtils.getInstance().showToast("获取数据失败");
            }
        });
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

    @Override
    public void attachView(BaseView view) {
        mView = (SplashContract.View) view;
    }

    @Override
    public void detachView() {

    }


}
