package edu.com.base.ui.widget.imageloader;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.stream.StreamModelLoader;

import java.io.IOException;
import java.io.InputStream;

import edu.com.base.util.AppUtils;
import edu.com.base.util.SettingUtils;

/**
 * Created by Anthony on 2016/3/3.
 * Class Note:
 * using Picasso to load image
 */
public class PicassoImageLoaderStrategy implements BaseImageLoaderStrategy {
    @Override
    public void loadImage(Context ctx, ImageLoader img) {
// TODO: 2016/6/7
    }
}
