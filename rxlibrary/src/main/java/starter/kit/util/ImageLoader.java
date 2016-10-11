package starter.kit.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader {

  private static ImageLoader SINGLETON = null;

  private ImageLoaderInterface imageLoader;
  private boolean mHandleAllUris = false;

  private ImageLoader(ImageLoaderInterface loaderImpl) {
    imageLoader = loaderImpl;
  }

  public static ImageLoader initialize(ImageLoaderInterface loaderImpl) {
    SINGLETON = new ImageLoader(loaderImpl);
    return SINGLETON;
  }

  public static ImageLoader getInstance() {
    if (SINGLETON == null) {
      SINGLETON = new ImageLoader(new AbstractImageLoader() {
      });
    }
    return SINGLETON;
  }

  public ImageLoader withHandleAllUris(boolean handleAllUris) {
    this.mHandleAllUris = handleAllUris;
    return this;
  }

  public boolean displayImageView(ImageView imageView, Uri uri) {
    if (consumed(uri)) {
      Drawable placeHolder = placeholder(imageView, uri, null);
      imageLoader.displayImageView(imageView, uri, placeHolder);
      return true;
    }
    return false;
  }

  /**
   * @return false if not consumed
   */
  public boolean displayImageView(ImageView imageView, Uri uri, String tag) {
    if (consumed(uri)) {
      Drawable placeHolder = placeholder(imageView, uri, tag);
      imageLoader.displayImageView(imageView, uri, placeHolder);
      return true;
    }
    return false;
  }

  public boolean displayImageView(ImageView imageView, Uri uri, int width, int height, String tag) {
    if (consumed(uri)) {
      Drawable placeHolder = placeholder(imageView, uri, tag);
      imageLoader.displayImageView(imageView, uri, placeHolder, width, height);
      return true;
    }
    return false;
  }

  public void cancelImage(ImageView imageView) {
    if (imageLoader != null) {
      imageLoader.cancel(imageView);
    }
  }

  public ImageLoaderInterface getImageLoader() {
    return imageLoader;
  }

  public void setImageLoader(ImageLoaderInterface imageLoader) {
    this.imageLoader = imageLoader;
  }

  private boolean consumed(Uri uri) {
    //if we do not handle all uris and are not http or https we keep the original behavior
    return mHandleAllUris || "http".equals(uri.getScheme()) || "https".equals(uri.getScheme());
  }

  private Drawable placeholder(ImageView imageView, Uri uri, String tag) {
    if (imageLoader != null) {
      if (TextUtils.isEmpty(tag)) {
        return imageLoader.placeholder(imageView.getContext());
      }
      return imageLoader.placeholder(imageView.getContext(), tag);
    }
    return null;
  }

  public interface ImageLoaderInterface {
    void displayImageView(ImageView imageView, Uri uri, Drawable placeholder);

    void displayImageView(ImageView imageView, Uri uri, Drawable placeholder, int width, int height);

    void cancel(ImageView imageView);

    Drawable placeholder(Context ctx);

    Drawable placeholder(Context context, String tag);
  }

  public static abstract class AbstractImageLoader implements ImageLoaderInterface {

    @Override
    public void displayImageView(ImageView imageView, Uri uri, Drawable placeholder) {
      Log.i("ImageLoader", "you have not specified a ImageLoader implementation through the ImageLoader.initialize(ImageLoaderInterface) method");
    }

    @Override
    public void displayImageView(ImageView imageView, Uri uri, Drawable placeholder, int width,
                                 int height) {
      Log.i("ImageLoader", "you have not specified a ImageLoader implementation through the ImageLoader.initialize(ImageLoaderInterface) method");
    }

    @Override
    public void cancel(ImageView imageView) {
    }

    @Override
    public Drawable placeholder(Context ctx) {
      return null;
    }

    @Override
    public Drawable placeholder(Context context, String tag) {
      return null;
    }
  }
}
