package edu.com.mvplibrary.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.ButterKnife;
import edu.com.mvplibrary.R;
import edu.com.mvplibrary.ui.BaseView;
import edu.com.mvplibrary.ui.widget.loading.VaryViewHelperController;
import edu.com.mvplibrary.util.AppUtils;

//import butterknife.ButterKnife;

/**
 * Created by Anthony on 2016/2/25.
 * Class Note:
 * Base Fragment for all the Fragment defined in the project
 * 1 extended from {@link AbsBaseFragment} to do
 * some base operation.
 * 2 do operation in initViewAndEvents(){@link #initViewsAndEvents(View rootView)}
 */
public abstract class AbsBaseFragment extends Fragment {
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

    /**
     * Screen information
     */
    protected int mScreenWidth = 0;
    protected int mScreenHeight = 0;
    protected float mScreenDensity = 0.0f;

    private VaryViewHelperController mVaryViewHelperController = null;

//    private View mTopBar; //common top bar

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG_LOG = this.getClass().getSimpleName();
        mContext = getActivity();
        if (getArguments() != null) {
            mUrl = getArguments().getString(EXTRA_URL);
        }
    }

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

        if (null != getLoadingTargetView()) {
            mVaryViewHelperController = new VaryViewHelperController(getLoadingTargetView());
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        mScreenDensity = displayMetrics.density;
        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;

        initViewsAndEvents(view);

    }

    /**
     * add loading view to default view parent
     */
    protected abstract View getLoadingTargetView();


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }



    public String getFragmentUrl() {
        return mUrl;
    }



//


    /**
     * show toast
     */
    protected void showToast(String msg) {
        if (null != msg && !AppUtils.isEmpty(msg)) {
//            Snackbar.make(((Activity) mContext).getWindow().getDecorView(), msg, Snackbar.LENGTH_SHORT).show();
        }
    }


    /**
     * toggle show loading
     */
    protected void toggleShowLoading(boolean toggle, String msg) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }
        if (toggle) {
            mVaryViewHelperController.showLoading(msg);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    /**
     * toggle show empty
     */
    protected void toggleShowEmpty(boolean toggle, String msg, View.OnClickListener onClickListener) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showEmpty(msg, onClickListener);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    /**
     * toggle show empty
     */
    protected void toggleShowEmpty(boolean toggle, String msg, View.OnClickListener onClickListener, int img) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showEmpty(msg, onClickListener, img);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    /**
     * toggle show error
     */
    protected void toggleShowError(boolean toggle, String msg, View.OnClickListener onClickListener) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showError(msg, onClickListener);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    /**
     * toggle show network error
     */
    protected void toggleNetworkError(boolean toggle, View.OnClickListener onClickListener) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showNetworkError(onClickListener);
        } else {
            mVaryViewHelperController.restore();
        }
    }

//    protected int getTopBarViewID() {
//        return R.layout.title_bar_common;
//    }
    /**
     * override this method to do operation in the fragment
     */
    protected abstract void initViewsAndEvents(View rootView);


    /**
     * override this method to return content view id of the fragment
     */
    protected abstract int getContentViewID() ;

}
