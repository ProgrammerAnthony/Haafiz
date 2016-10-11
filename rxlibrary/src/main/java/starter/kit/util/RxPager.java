package starter.kit.util;

import java.util.ArrayList;

import rx.functions.Action1;
import starter.kit.model.entity.Entity;

public class RxPager implements RxRequestKey {

  private final int startPage; // 第一页
  private int nextPage; // 下一页

  private final int pageSize; // 每次请求数据大小
  private int requested = NOT_REQUESTED; // 已请求数据
  private int size = 0; // 当前总共接收数据

  private boolean hasMoreData;
  private boolean isLoading;

  private Action1<RxPager> onRequest;

  public RxPager(int startPage, int pageSize, Action1<RxPager> onRequest) {
    this.startPage = startPage;
    this.pageSize = pageSize;
    this.onRequest = onRequest;
    this.nextPage = startPage;
    this.hasMoreData = true;
    this.isLoading = true;
  }

  @Override
  public void received(ArrayList<? extends Entity> items) {
    isLoading = false;
    final int itemCount = items.size();
    hasMoreData = itemCount >= pageSize;
    size += itemCount;
    requested += itemCount;
    if (hasMoreData()) {
      nextPage = size / pageSize + startPage;
    }
  }

  @Override
  public void next() {
    if (hasMoreData()) {
      isLoading = true;
      onRequest.call(this);
    }
  }

  @Override
  public void reset() {
    size = 0;
    nextPage = startPage;
    isLoading = true;
    hasMoreData = true;
    requested = NOT_REQUESTED;
  }

  @Override
  public boolean hasMoreData() {
    return hasMoreData && size % pageSize == 0;
  }

  @Override
  public String nextKey() {
    return String.valueOf(nextPage);
  }

  @Override
  public String previousKey() {
    return String.valueOf(startPage);
  }

  @Override
  public int pageSize() {
    return pageSize;
  }

  @Override
  public boolean isFirstPage() {
    return nextPage == startPage;
  }

  @Override
  public boolean requested() {
    return requested != NOT_REQUESTED;
  }

  @Override
  public boolean isLoading() {
    return isLoading;
  }
}
