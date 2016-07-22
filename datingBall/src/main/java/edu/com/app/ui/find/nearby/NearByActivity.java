package edu.com.app.ui.find.nearby;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.anthony.ultimateswipetool.cards.SwipeCards;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import edu.com.app.R;
import edu.com.app.base.AbsSwipeBackActivity;

import edu.com.app.injection.component.ActivityComponent;
import edu.com.app.util.ToastUtils;

/**
 * Created by Anthony on 2016/6/27.
 * Class Note:
 */
public class NearByActivity extends AbsSwipeBackActivity implements SwipeCards.SwipeCardsListener {


    @Bind(R.id.title_image_left)
    ImageView titleImageLeft;
    @Bind(R.id.title_txt_center)
    TextView titleTxtCenter;
    @Bind(R.id.swipe_cards)
    SwipeCards swipeCards;
    @Bind(R.id.buttonSwipeLeft)
    Button buttonSwipeLeft;
    @Bind(R.id.buttonSwipeRight)
    Button buttonSwipeRight;
    @Bind(R.id.title_image_right)
    ImageView titleImageRight;


    @Inject
    ToastUtils toast;
    private SwipeStackAdapter mAdapter;
    private ArrayList<String> mData;


    @Override
    protected void initViewsAndEvents() {
        mData = new ArrayList<>();
        mAdapter = new SwipeStackAdapter(mData);
        swipeCards.setAdapter(mAdapter);
        swipeCards.setListener(this);
        fillWithTestData();
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_nearby;
    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }


    @OnClick({R.id.title_image_left,R.id.buttonSwipeLeft, R.id.buttonSwipeRight,R.id.title_image_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_image_left:
                scrollToFinishActivity();
                break;

            case R.id.buttonSwipeLeft:
                swipeCards.swipeTopViewToLeft();
                toast.showToast("left");
                break;
            case R.id.buttonSwipeRight:
                swipeCards.swipeTopViewToRight();
                toast.showToast("right");
                break;
            case R.id.title_image_right:
                mData.add(getString(R.string.dummy_add));
                mAdapter.notifyDataSetChanged();
                toast.showToast("add");
                break;
        }
    }

    private void fillWithTestData() {
        for (int x = 0; x < 5; x++) {
            mData.add(getString(R.string.dummy_text) + " " + (x + 1));
        }
    }

    @Override
    public void onViewSwipedToLeft(int position) {
        String swipedElement = mAdapter.getItem(position);
        toast.showToast(getString(R.string.view_swiped_left, swipedElement));
    }

    @Override
    public void onViewSwipedToRight(int position) {
        String swipedElement = mAdapter.getItem(position);
        toast.showToast(getString(R.string.view_swiped_right, swipedElement));
    }

    @Override
    public void onCardsEmpty() {
//        Toast.makeText(this, R.string.stack_empty, Toast.LENGTH_SHORT).show();
        toast.showToast(getResources().getString(R.string.stack_empty));
    }



    public class SwipeStackAdapter extends BaseAdapter {

        private List<String> mData;

        public SwipeStackAdapter(List<String> data) {
            this.mData = data;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public String getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.card, parent, false);
            }

            TextView textViewCard = (TextView) convertView.findViewById(R.id.textViewCard);
            textViewCard.setText(mData.get(position));

            return convertView;
        }
    }


}
