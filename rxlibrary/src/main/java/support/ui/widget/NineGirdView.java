package support.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;

import com.anthony.rxlibrary.R;

import java.util.List;

import support.ui.utilities.ViewUtils;

public class NineGirdView extends RecyclerView {

  public final static int STYLE_GRID = 0;     // 宫格布局
  public final static int STYLE_FILL = 1;     // 全填充布局

  private int mSpanCount;    // 列数

  private int mMaxSize;        // 最大图片数
  private int mShowStyle;     // 显示风格
  private int mGap;           // 宫格间距
  private int mSingleImgSize; // 单张图片时的尺寸

  private NineGridAdapter mAdapter;
  private GridItemDecoration mGridItemDecoration;
  private List<Uri> mImageUrls;
  private boolean hasPopulated = false;

  public NineGirdView(Context context) {
    this(context, null);
  }

  public NineGirdView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public NineGirdView(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initialize(context, attrs);
  }

  private void initialize(Context context, @Nullable AttributeSet attrs) {
    final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NineGirdView);
    this.mGap = a.getDimensionPixelSize(R.styleable.NineGirdView_ngGap, 0);
    this.mSingleImgSize = a.getDimensionPixelSize(R.styleable.NineGirdView_ngSingleSize, -1);
    this.mShowStyle = a.getInt(R.styleable.NineGirdView_ngStyle, STYLE_GRID);
    this.mMaxSize = a.getInt(R.styleable.NineGirdView_ngMaxSize, 9);
    a.recycle();

    setNestedScrollingEnabled(false);
  }

  public void setStyle(int style) {
    this.mShowStyle = style;
  }

  public void setNineGridAdapter(NineGridAdapter adapter) {
    mAdapter = adapter;
    setAdapter(mAdapter);
  }

  private boolean isValid(List<Uri> imageUrls) {
    return imageUrls != null && imageUrls.size() > 0;
  }

  public void notifyDataSetChanged(List<Uri> imageUrls) {
    if (!isValid(imageUrls)) {
      return;
    }

    final int count = imageUrls.size();
    final int maxSize = mMaxSize;
    if (maxSize > 0 && count > maxSize) {
      mImageUrls = imageUrls.subList(0, maxSize);
    } else {
      mImageUrls = imageUrls;
    }

    final ViewTreeObserver vto = getViewTreeObserver();
    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        ViewUtils.removeOnGlobalLayoutListener(NineGirdView.this, this);
        populate();
      }
    });
  }

  private void populate() {
    if (hasPopulated) {
      return;
    }

    final int count = mImageUrls.size();
    final int width = getWidth();
    int totalWidth = width - getPaddingLeft() - getPaddingRight();
    calculateSpanCount(count);

    final int itemSize;
    if (count == 1 && mSingleImgSize != -1) {
      itemSize = mSingleImgSize > totalWidth ? totalWidth : mSingleImgSize;
    } else {
      itemSize = (totalWidth - mGap * (mSpanCount - 1)) / mSpanCount;
    }

    if (mGridItemDecoration == null) {
      mGridItemDecoration = new GridItemDecoration(mSpanCount, mGap, false);
    }
    removeItemDecoration(mGridItemDecoration);
    addItemDecoration(mGridItemDecoration);
    setLayoutManager(new GridLayoutManager(getContext(), mSpanCount));
    mAdapter.setItemSize(itemSize);
    mAdapter.notifyDataSetChanged(mImageUrls);

    hasPopulated = true;
  }

  protected void calculateSpanCount(int count) {
    switch (mShowStyle) {
      case STYLE_FILL:
        if (count < 3) {
          mSpanCount = count;
        } else if (count <= 4) {
          mSpanCount = 2;
        } else {
          mSpanCount = 3;
        }
        break;
      case STYLE_GRID:
      default:
        mSpanCount = 3;
        break;
    }
  }
}
