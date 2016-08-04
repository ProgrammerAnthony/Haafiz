package edu.com.app.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

import edu.com.app.widget.dialog.DialogManager;
import edu.com.app.util.ToastUtils;
import rx.Subscription;
import timber.log.Timber;

/**
 * Created by Anthony on 2016/2/25.
 * Class Note:
 *
 * Base Fragment for all the Fragment defined in the project
 * 1 extended from {@link AbsBaseFragment} to do
 * some base operation.
 * 2 do operation in initViewAndEvents(){@link #initViewsAndEvents(View rootView)}
 */
public abstract class AbsBaseFragment extends Fragment implements BaseView{
    /**
     * Log tag
     */
    protected static String TAG_LOG = null;
    /**
     * url passed into fragment
     */
    public static String EXTRA_URL = "url";
    private String mUrl;
    /**
     * activity context of fragment
     */
    protected Context mContext;

    @Inject
    ToastUtils toastUtils;

    protected Subscription mSubscription;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//set timber tag
        TAG_LOG = this.getClass().getSimpleName();
        Timber.tag(TAG_LOG);

        mContext = getActivity();
//url
        if (getArguments() != null) {
            mUrl = getArguments().getString(EXTRA_URL);
        }
        initDagger();
    }

    // ((MainActivity) (getActivity())).activityComponent().inject(this);
    protected abstract void initDagger();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getContentViewID() != 0) {
            return inflater.inflate(getContentViewID(), null);
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initViewsAndEvents(view);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mSubscription !=null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public String getFragmentUrl() {
        return mUrl;
    }


    /**
     * override this method to do operation in the fragment
     */
    protected abstract void initViewsAndEvents(View rootView);


    /**
     * override this method to return content view id of the fragment
     */
    protected abstract int getContentViewID();


    /**-----------------------implements methods in BaseView------------**/
    @Override
    public void showMessage(String msg) {
        toastUtils.showToast(msg);
 //            Snackbar.make(this, msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void close() {
//        finish();
    }

    @Override
    public void showProgress(String message) {
        DialogManager.showProgressDialog(mContext,message);
    }

    @Override
    public void showProgress(String message, int progress) {
        DialogManager.showProgressDialog(mContext,message, progress);
    }

    @Override
    public void hideProgress() {
        DialogManager.hideProgressDialog();
    }

    @Override
    public void showErrorMessage(String msg, String content) {
        DialogManager.showErrorDialog(mContext,msg, content, new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
            }
        });
    }

}

