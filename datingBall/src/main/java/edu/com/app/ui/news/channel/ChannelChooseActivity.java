package edu.com.app.ui.news.channel;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import edu.com.app.R;
import edu.com.app.data.bean.ChannelEntity;
import edu.com.app.base.AbsSwipeBackActivity;
import edu.com.app.base.widget.CircleImageView;
import edu.com.app.util.ToastUtils;

/**
 * Created by Anthony on 2016/6/14.
 * Class Note:
 * 用于栏目选择（通过RecyclerView拖拽进行选择），并保存
 * load channel,drag to choose channel(delete), and save channel
 */
public class ChannelChooseActivity extends AbsSwipeBackActivity implements ChannelChooseContract.View {

    @Bind(R.id.title_image_left)
    CircleImageView titleImageLeft;
    @Bind(R.id.title_txt_center)
    TextView titleTxtCenter;
    @Bind(R.id.title_txt_right)
    TextView titleTxtRight;


    @Bind(R.id.channel_ry_view)
    RecyclerView channelRyView;

    @Inject
    ToastUtils toastUtils;

    @Inject
    ChannelChoosePresenter mPresenter;


    @Override
    protected void initViewsAndEvents() {
        titleImageLeft.setImageResource(R.drawable.ico_back);
        titleTxtCenter.setText("栏目编辑");
        titleTxtRight.setText("保存");

        mPresenter.attachView(this);
        //   step1   load channel
        mPresenter.initChannelData();
        // step2 implements drag to choose channel(delete)--->in method showChannelChooseData

        // TODO: 2016/6/14  step3   save channel
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_channle_choose;
    }

    @Override
    protected void injectDagger() {
        activityComponent().inject(this);
    }

    @Override
    protected void initToolBar() {

    }


    @OnClick({R.id.title_image_left, R.id.title_txt_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_image_left:
                toastUtils.showToast("取消保存");
                this.finish();
                break;
            case R.id.title_txt_right:

                // TODO: 2016/6/14  获取页面数据
                ArrayList<ChannelEntity> myItems = new ArrayList<>();
                for (int i = 0; i < 18; i++) {
                    ChannelEntity entity = new ChannelEntity();
                    entity.setName("修改频道" + i);
                    myItems.add(entity);
                }
                mPresenter.saveData(myItems);
                toastUtils.showToast("保存成功");
                break;

        }
    }

    @Override
    public void showChannelChooseData(final List<ChannelEntity> myChannels, List<ChannelEntity> otherChannels) {
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        channelRyView.setLayoutManager(manager);

        ItemDragHelperCallback callback = new ItemDragHelperCallback();
        final ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(channelRyView);

        final ChannelAdapter adapter = new ChannelAdapter(this, helper, myChannels, otherChannels);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = adapter.getItemViewType(position);
                return viewType == ChannelAdapter.TYPE_MY || viewType == ChannelAdapter.TYPE_OTHER ? 1 : 4;
            }
        });
        channelRyView.setAdapter(adapter);

        adapter.setOnMyChannelItemClickListener(new ChannelAdapter.OnMyChannelItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Toast.makeText(ChannelChooseActivity.this, myChannels.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void toMainActivity() {
        this.finish();
    }


}
