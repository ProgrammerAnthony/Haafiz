package starter.kit.util;

import java.util.ArrayList;

import rx.functions.Action1;
import starter.kit.model.entity.Entity;

public class RxIdentifier implements RxRequestKey {

  private String sinceIdentifier; // 最新数据
  private String maxIdentifier; // 更多数据

  private final int pageSize;
  private boolean hasMoreData;
  private boolean isLoading;
  private int size = 0; // 当前接收数据
  private int requested = NOT_REQUESTED;
  private Action1<RxIdentifier> onRequest;

  public RxIdentifier(int pageSize, Action1<RxIdentifier> onRequest) {
    this.pageSize = pageSize;
    this.onRequest = onRequest;
    this.hasMoreData = true;
    this.isLoading = true;
  }

  @Override
  public void received(ArrayList<? extends Entity> items) {
    isLoading = false;
    final int itemCount = items.size();
    hasMoreData = itemCount >= pageSize;
    size += itemCount;
    requested = size;

    if (isFirstPage()) {
      // TODO
      //sinceIdentifier = items.get(0).identifier;
      maxIdentifier = null;
    } else if (hasMoreData()) {
      maxIdentifier = items.get(itemCount - 1).identifier;
      //sinceIdentifier = null;
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
    isLoading = false;
    maxIdentifier = null;
    maxIdentifier = null;
    this.hasMoreData = true;
    requested = NOT_REQUESTED;
  }

  @Override
  public boolean hasMoreData() {
    return hasMoreData && size % pageSize == 0;
  }

  @Override
  public String previousKey() {
    return sinceIdentifier;
  }

  @Override
  public String nextKey() {
    return maxIdentifier;
  }

  @Override
  public int pageSize() {
    return pageSize;
  }

  @Override
  public boolean isFirstPage() {
    return requested == NOT_REQUESTED;
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
