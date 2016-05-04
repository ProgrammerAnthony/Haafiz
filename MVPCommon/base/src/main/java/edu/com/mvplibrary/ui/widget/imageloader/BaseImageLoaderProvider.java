package edu.com.mvplibrary.ui.widget.imageloader;

import android.content.Context;

/**
 * Created by Anthony on 2016/3/3.
 * Class Note:
 * abstract class defined to load image
 */
public abstract class BaseImageLoaderProvider {
    public abstract void loadImage(Context ctx, ImageLoader img);
}
