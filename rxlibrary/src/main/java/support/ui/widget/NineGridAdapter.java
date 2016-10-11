package support.ui.widget;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class NineGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  protected int itemSize;
  private List<Uri> mImageUrls;

  public NineGridAdapter() {
    mImageUrls = new ArrayList<>();
  }

  public void setItemSize(int itemSize) {
    this.itemSize = itemSize;
  }

  public void notifyDataSetChanged(List<Uri> imageUrls) {
    mImageUrls.clear();
    mImageUrls.addAll(imageUrls);
    notifyDataSetChanged();
  }

  public Uri getItem(int position) {
    return mImageUrls.get(position);
  }

  @Override
  public int getItemCount() {
    return mImageUrls.size();
  }
}
