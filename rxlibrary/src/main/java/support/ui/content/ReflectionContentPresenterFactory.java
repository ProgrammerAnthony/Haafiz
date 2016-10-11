package support.ui.content;

import android.support.annotation.Nullable;
import android.view.View;

public class ReflectionContentPresenterFactory implements ContentPresenterFactory {

  private Class<View> loadViewClass;
  private Class<View> emptyViewClass;
  private Class<View> errorViewClass;

  @Nullable
  @SuppressWarnings("unchecked")
  public static ReflectionContentPresenterFactory fromViewClass(Class<?> viewClass) {
    RequiresContent annotation = viewClass.getAnnotation(RequiresContent.class);
    //noinspection unchecked
    Class<View> loadViewClass = annotation == null ? null : (Class<View>) annotation.loadView();
    Class<View> emptyViewClass = annotation == null ? null : (Class<View>) annotation.emptyView();
    Class<View> errorViewClass = annotation == null ? null : (Class<View>) annotation.errorView();
    return new ReflectionContentPresenterFactory(loadViewClass, emptyViewClass, errorViewClass);
  }

  public ReflectionContentPresenterFactory(Class<View> loadViewClass,
                                           Class<View> emptyViewClass, Class<View> errorViewClass) {
    this.loadViewClass = loadViewClass;
    this.emptyViewClass = emptyViewClass;
    this.errorViewClass = errorViewClass;
  }

  @Override
  public ContentPresenter createContentPresenter() {
    try {
      return new ContentPresenter(loadViewClass, emptyViewClass, errorViewClass);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
