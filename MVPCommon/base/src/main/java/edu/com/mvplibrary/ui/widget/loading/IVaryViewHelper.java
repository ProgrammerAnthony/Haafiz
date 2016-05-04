package edu.com.mvplibrary.ui.widget.loading;

import android.content.Context;
import android.view.View;

/**
 * Created by Anthony on 2016/5/4.
 * Class Note:
 * show view and hide view for any layout
 */
public interface IVaryViewHelper {
    View getCurrentLayout();

    void restoreView();

    void showLayout(View view);

    View inflate(int layoutId);

    Context getContext();

    View getView();
}
