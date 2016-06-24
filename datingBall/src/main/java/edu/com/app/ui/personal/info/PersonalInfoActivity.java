package edu.com.app.ui.personal.info;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import edu.com.app.R;
import edu.com.app.base.AbsSwipeBackActivity;
import edu.com.app.base.widget.CircleImageView;
import edu.com.app.base.widget.ViewDisplay;
import edu.com.app.base.widget.recyclerviewHelper.ParallaxRecyclerAdapter;
import edu.com.app.util.ToastUtils;

/**
 * Created by Anthony on 2016/5/26.
 * Class Note:
 *利用RecyclerView 处理个人中心，利用{@link ParallaxRecyclerAdapter}实现视差效果。
 *todo 处理（头像和个人签名）
 *todo 处理编辑编辑界面
 */
public class PersonalInfoActivity extends AbsSwipeBackActivity implements PersonalInfoContract.View{

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.title_image_left)
    CircleImageView titleImageLeft;
    @Bind(R.id.title_txt_center)
    TextView titleTxtCenter;
    @Bind(R.id.title_txt_right)
    TextView titleTxtRight;
    @Bind(R.id.personal_top_bar)
    RelativeLayout personalTopBar;

    @Bind(R.id.delete_account_txt)
    RelativeLayout deleteAccountTxt;
//    @Bind(R.id.toolbar)
//    Toolbar toolbar;

    @Inject
    ViewDisplay viewDisplay;
    @Inject
    ToastUtils toastUtils;

    @Override
    protected void initViewsAndEvents() {
        titleImageLeft.setImageResource(R.drawable.ico_back);
        titleTxtCenter.setVisibility(View.GONE);
        titleTxtRight.setText("编辑");

        createAdapter(recyclerView);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_personal;
    }

    @Override
    protected void injectDagger() {
        activityComponent().inject(this);
    }

    @Override
    protected void initToolBar() {

    }

    /**
     * create adapter for recyclerView using {@link ParallaxRecyclerAdapter}
     * @param recyclerView
     */
    private void createAdapter(RecyclerView recyclerView) {
        final List<String> content = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            content.add("item " + i);
        }

        final ParallaxRecyclerAdapter<String> adapter = new ParallaxRecyclerAdapter<String>(content) {
            @Override
            public void onBindViewHolderImpl(RecyclerView.ViewHolder viewHolder, ParallaxRecyclerAdapter<String> adapter, int i) {
                ((ViewHolder) viewHolder).textView.setText(adapter.getData().get(i));
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup, final ParallaxRecyclerAdapter<String> adapter, int i) {
                return new ViewHolder(getLayoutInflater().inflate(R.layout.row_recyclerview, viewGroup, false));
            }

            @Override
            public int getItemCountImpl(ParallaxRecyclerAdapter<String> adapter) {
                return content.size();
            }
        };

        adapter.setOnClickEvent(new ParallaxRecyclerAdapter.OnClickEvent() {
            @Override
            public void onClick(View v, int position) {
                Toast.makeText(PersonalInfoActivity.this, "You clicked '" + position + "'", Toast.LENGTH_SHORT).show();
            }
        });

        //alpha effect in TopBar
        adapter.setOnParallaxScroll(new ParallaxRecyclerAdapter.OnParallaxScroll() {
            @Override
            public void onParallaxScroll(float percentage, float offset, View parallax) {
                Drawable c = personalTopBar.getBackground();
                c.setAlpha(Math.round(percentage * 255));
                personalTopBar.setBackground(c);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        View header = getLayoutInflater().inflate(R.layout.header, recyclerView, false);
        adapter.setParallaxHeader(header, recyclerView);
        adapter.setData(content);
        recyclerView.setAdapter(adapter);
    }



    @OnClick({R.id.title_image_left, R.id.title_txt_right, R.id.delete_account_txt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_image_left:
                scrollToFinishActivity();
                break;
            case R.id.title_txt_right:
                toastUtils.showToast("修改个人数据");
                viewDisplay.showActivity(mContext,"1008");
                break;
            case R.id.delete_account_txt:
                toastUtils.showToast("注销当前账号");
                break;
        }
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }
    }

/*******implements method in MVP View**************/
    @Override
    public void updateInfo() {

    }

    @Override
    public void toEditInfoActivity() {

    }

    @Override
    public void toMainActivity() {

    }

}