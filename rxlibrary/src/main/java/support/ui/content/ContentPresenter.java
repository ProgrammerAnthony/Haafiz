package support.ui.content;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.util.SparseArrayCompat;
import android.view.View;
import android.view.ViewGroup;

import com.anthony.rxlibrary.R;

import java.lang.reflect.Constructor;

import support.ui.utilities.LayoutHelper;

public final class ContentPresenter {

  private static final int ID_NONE = -1;
  private static final int LoadViewId = R.id.support_ui_load_view;
  private static final int EmptyViewId = R.id.support_ui_empty_view;
  private static final int ErrorViewId = R.id.support_ui_error_view;
  private static final int ContentViewId = R.id.support_ui_content_view;

  private SparseArrayCompat<Class<View>> mViewClassArray = new SparseArrayCompat<>(4);
  private SparseArrayCompat<View> mViewArray = new SparseArrayCompat<>(4);
  int mCurrentId = ID_NONE;
  ViewGroup mContainer;
  View mContentView;
  Context mContext;

  private EmptyView.OnEmptyViewClickListener onEmptyViewClickListener;
  private ErrorView.OnErrorViewClickListener onErrorViewClickListener;

  public ContentPresenter(Class<View> loadViewClass, Class<View> emptyViewClass, Class<View> errorViewClass) {
    buildViewClassArray(loadViewClass, emptyViewClass, errorViewClass);
  }

  public ContentPresenter onCreate(Context context) {
    mContext = context;
    return this;
  }

  public ContentPresenter attachContainer(ViewGroup container) {
    mContainer = container;
    return this;
  }

  public ContentPresenter attachContentView(View contentView) {
    mContentView = contentView;
    return this;
  }

  public void setOnEmptyViewClickListener(EmptyView.OnEmptyViewClickListener listener) {
    this.onEmptyViewClickListener = listener;
  }

  public void setOnErrorViewClickListener(
      ErrorView.OnErrorViewClickListener onErrorViewClickListener) {
    this.onErrorViewClickListener = onErrorViewClickListener;
  }

  public void onDestroyView() {
    mCurrentId = ID_NONE;
    mContentView = null;
    mViewArray.clear();
  }

  public void onDestroy() {
    onEmptyViewClickListener = null;
    onErrorViewClickListener = null;
    mContext = null;
    mContainer = null;
    mViewClassArray = null;
    mViewArray = null;
  }

  /**
   * 显示进度条
   */
  public ContentPresenter displayLoadView() {
    final int loadViewId = LoadViewId;
    if (mCurrentId != loadViewId) {
      displayView(loadViewId);
    }
    return this;
  }

  /**
   * 显示空白页
   */
  public ContentPresenter displayEmptyView() {
    final int emptyViewId = EmptyViewId;
    if (mCurrentId != emptyViewId) {
      View view = displayView(emptyViewId);
      if (view instanceof EmptyView) {
        EmptyView emptyView = (EmptyView) view;
        emptyView.setOnEmptyViewClickListener(onEmptyViewClickListener);
      }
    }
    return this;
  }

  /**
   * 显示错误页面
   */
  public ContentPresenter displayErrorView() {
    final int errorViewId = ErrorViewId;
    if (mCurrentId != errorViewId) {
      View view = displayView(errorViewId);
      if (view instanceof ErrorView) {
        ErrorView errorView = (ErrorView) view;
        errorView.setOnErrorViewClickListener(onErrorViewClickListener);
      }
    }
    return this;
  }

  /**
   * 显示内容
   */
  public ContentPresenter displayContentView() {
    final int contentViewId = ContentViewId;
    if (mCurrentId != contentViewId && mContentView != null) {
      final ViewGroup container = mContainer;
      container.removeAllViews();
      final ViewGroup.LayoutParams layoutParams = LayoutHelper.createViewGroupLayoutParams();
      container.addView(mContentView, layoutParams);
      mCurrentId = contentViewId;
    }
    return this;
  }

  // empty start
  public ContentPresenter buildEmptyImageView(@DrawableRes int drawableRes) {
    View view = checkView(EmptyViewId);
    if (view instanceof EmptyView) {
      EmptyView emptyView = (EmptyView) view;
      emptyView.buildEmptyImageView(drawableRes);
    }
    return this;
  }

  public ContentPresenter buildEmptyTitle(@StringRes int stringRes) {
    return buildEmptyTitle(mContext.getResources().getString(stringRes));
  }

  public ContentPresenter buildEmptyTitle(String title) {
    View view = checkView(EmptyViewId);
    if (view instanceof EmptyView) {
      EmptyView emptyView = (EmptyView) view;
      emptyView.buildEmptyTitle(title);
    }
    return this;
  }

  public ContentPresenter buildEmptySubtitle(@StringRes int stringRes) {
    return buildEmptySubtitle(mContext.getResources().getString(stringRes));
  }

  public ContentPresenter buildEmptySubtitle(String subtitle) {
    View view = checkView(EmptyViewId);
    if (view instanceof EmptyView) {
      EmptyView emptyView = (EmptyView) view;
      emptyView.buildEmptySubtitle(subtitle);
    }
    return this;
  }

  public ContentPresenter shouldDisplayEmptySubtitle(boolean display) {
    View view = checkView(EmptyViewId);
    if (view instanceof EmptyView) {
      EmptyView emptyView = (EmptyView) view;
      emptyView.shouldDisplayEmptySubtitle(display);
    }
    return this;
  }

  public ContentPresenter shouldDisplayEmptyTitle(boolean display) {
    View view = checkView(EmptyViewId);
    if (view instanceof EmptyView) {
      EmptyView emptyView = (EmptyView) view;
      emptyView.shouldDisplayEmptyTitle(display);
    }
    return this;
  }

  public ContentPresenter shouldDisplayEmptyImageView(boolean display) {
    View view = checkView(EmptyViewId);
    if (view instanceof EmptyView) {
      EmptyView emptyView = (EmptyView) view;
      emptyView.shouldDisplayEmptyImageView(display);
    }
    return this;
  }

  // error start
  public ContentPresenter buildErrorImageView(@DrawableRes int drawableRes) {
    View view = checkView(ErrorViewId);
    if (view instanceof ErrorView) {
      ErrorView errorView = (ErrorView) view;
      errorView.buildErrorImageView(drawableRes);
    }
    return this;
  }

  public ContentPresenter buildErrorTitle(@StringRes int stringRes) {
    return buildErrorTitle(mContext.getResources().getString(stringRes));
  }

  public ContentPresenter buildErrorTitle(String title) {
    View view = checkView(ErrorViewId);
    if (view instanceof ErrorView) {
      ErrorView errorView = (ErrorView) view;
      errorView.buildErrorTitle(title);
    }
    return this;
  }

  public ContentPresenter buildErrorSubtitle(@StringRes int stringRes) {
    return buildErrorSubtitle(mContext.getResources().getString(stringRes));
  }

  public ContentPresenter buildErrorSubtitle(String subtitle) {
    View view = checkView(ErrorViewId);
    if (view instanceof ErrorView) {
      ErrorView errorView = (ErrorView) view;
      errorView.buildErrorSubtitle(subtitle);
    }
    return this;
  }

  public ContentPresenter shouldDisplayErrorSubtitle(boolean display) {
    View view = checkView(ErrorViewId);
    if (view instanceof ErrorView) {
      ErrorView errorView = (ErrorView) view;
      errorView.shouldDisplayErrorSubtitle(display);
    }
    return this;
  }

  public ContentPresenter shouldDisplayErrorTitle(boolean display) {
    View view = checkView(ErrorViewId);
    if (view instanceof ErrorView) {
      ErrorView errorView = (ErrorView) view;
      errorView.shouldDisplayErrorTitle(display);
    }
    return this;
  }

  public ContentPresenter shouldDisplayErrorImageView(boolean display) {
    View view = checkView(ErrorViewId);
    if (view instanceof ErrorView) {
      ErrorView errorView = (ErrorView) view;
      errorView.shouldDisplayErrorImageView(display);
    }
    return this;
  }

  private View displayView(@IdRes int viewId) {
    final ViewGroup container = mContainer;
    container.removeAllViews();
    final View view = checkView(viewId);
    final ViewGroup.LayoutParams layoutParams = LayoutHelper.createViewGroupLayoutParams();
    container.removeView(view);
    container.addView(view, layoutParams);
    mCurrentId = viewId;
    return view;
  }

  @Nullable
  private View checkView(int viewId) {
    final SparseArrayCompat<View> viewArray = mViewArray;
    View view = viewArray.get(viewId);
    if (view == null) {
      view = buildView(viewId);
      viewArray.put(viewId, view);
    }
    return view;
  }

  private ContentPresenter buildViewClassArray(Class<View> loadViewClass,
                                               Class<View> emptyViewClass, Class<View> errorViewClass) {
    final SparseArrayCompat<Class<View>> viewClassArray = mViewClassArray;
    viewClassArray.put(LoadViewId, loadViewClass);
    viewClassArray.put(EmptyViewId, emptyViewClass);
    viewClassArray.put(ErrorViewId, errorViewClass);
    return this;
  }

  @Nullable
  private View buildView(int viewId) {
    final SparseArrayCompat<Class<View>> viewClassArray = mViewClassArray;
    final Class<View> viewClass = viewClassArray.get(viewId);
    try {
      Constructor<? extends View> constructor = viewClass.getDeclaredConstructor(Context.class);
      final Context context = mContext;
      return constructor.newInstance(context);
    } catch (Throwable e) {
      throw new RuntimeException(
          "Unable to create View for" + viewClass + ". " + e.getLocalizedMessage(), e);
    }
  }
}
