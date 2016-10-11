package starter.kit.util;

import java.util.ArrayList;

import starter.kit.model.entity.Entity;

public interface RxRequestKey {

  int NOT_REQUESTED = -1;

  void received(ArrayList<? extends Entity> items);

  void next();

  void reset();

  boolean hasMoreData();

  String previousKey();

  String nextKey();

  int pageSize();

  boolean isFirstPage();

  boolean requested();

  boolean isLoading();
}
