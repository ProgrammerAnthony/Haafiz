package starter.kit.rx.app.util;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import starter.kit.rx.app.R;
import starter.kit.util.ImageLoader;

public class NineGridAdapter extends support.ui.widget.NineGridAdapter {

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return SimpleViewHolder.create(parent);
  }

  @Override public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
    final Uri image = getItem(position);
    final SimpleViewHolder viewHolder = (SimpleViewHolder) holder;
    viewHolder.setItemSize(itemSize);
    viewHolder.bind(image);
  }

  static class SimpleViewHolder extends RecyclerView.ViewHolder {
    protected int itemSize;
    public Uri image;
    SimpleDraweeView mThumbnailView;

    public void setItemSize(int itemSize) {
      this.itemSize = itemSize;
    }

    static SimpleViewHolder create(ViewGroup parent) {
      return new SimpleViewHolder(parent.getContext(), parent);
    }

    private SimpleViewHolder(Context context, ViewGroup parent) {
      super(LayoutInflater.from(context).inflate(R.layout.list_item_image, parent, false));
      mThumbnailView = ButterKnife.findById(itemView, R.id.image_feed_thumbnail);
    }

    public void bind(Uri imageUrl) {
      this.image = imageUrl;
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mThumbnailView.getLayoutParams();
      params.width = itemSize;
      params.height = itemSize;
      mThumbnailView.setLayoutParams(params);
      ImageLoader.getInstance()
          .displayImageView(mThumbnailView, imageUrl, itemSize, itemSize, null);
    }
  }
}
