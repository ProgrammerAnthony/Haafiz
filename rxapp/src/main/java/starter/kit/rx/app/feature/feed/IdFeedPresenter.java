package starter.kit.rx.app.feature.feed;

import android.os.Bundle;
import java.util.ArrayList;
import rx.Observable;
import starter.kit.feature.rx.RxResourcePresenter;
import starter.kit.rx.app.model.entity.Feed;
import starter.kit.rx.app.network.ApiService;
import starter.kit.rx.app.network.service.FeedService;

public class IdFeedPresenter extends RxResourcePresenter<Feed> {

  private FeedService mFeedService;

  @Override protected void onCreate(Bundle savedState) {
    super.onCreate(savedState);
    mFeedService = ApiService.createFeedService();
  }

  @Override
  public Observable<ArrayList<Feed>> request(String previousKey, String nextKey, int pageSize) {
    return mFeedService.fetchFeeds(previousKey, nextKey, pageSize, "IdFeedPresenter");
  }

  @Override public int restartableId() {
    return 1001;
  }
}
