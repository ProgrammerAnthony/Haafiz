package starter.kit.rx.app.feature.feed;

import android.graphics.Color;
import android.os.Bundle;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import nucleus.factory.RequiresPresenter;
import starter.kit.feature.StarterFragConfig;
import starter.kit.feature.rx.RxStarterRecyclerFragment;
import starter.kit.rx.app.R;
import starter.kit.rx.app.model.entity.Feed;

@RequiresPresenter(IdFeedPresenter.class)
public class IdFeedFragment extends RxStarterRecyclerFragment {

  public static IdFeedFragment create() {
    IdFeedFragment feedFragment = new IdFeedFragment();
    return feedFragment;
  }

  @Override public void onCreate(Bundle bundle) {
    super.onCreate(bundle);

    StarterFragConfig.Builder builder = new StarterFragConfig.Builder()
        .pageSize(5)
        .bind(Feed.class, FeedsViewHolder.class)
        .loadingTriggerThreshold(0)
        .recyclerViewDecor(new HorizontalDividerItemDecoration
            .Builder(getContext()).size(10)
            .colorResId(R.color.dividerColor)
            .build())
        .swipeRefreshLayoutColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);

    buildFragConfig(builder.build());
  }
}