package edu.com.app.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import javax.inject.Inject;

import edu.com.app.R;
import edu.com.app.util.BlurViewUtil;
import pl.aprilapps.easyphotopicker.EasyImage;

/**
 * Create By Anthony on 2016/1/15
 * Class Note:
 * show center dialog to choose pics in gallery/take small video like WeChat
 * 类似微信朋友圈，中间的dialog（图片/小视频）
 */

public class ChooseFriendCircleDialog implements View.OnClickListener {
    Button btnGallery;
    Button btnSmallVideo;

    private Dialog mDialog;
    Activity activity;
    private Window window;

    @Inject
    public ChooseFriendCircleDialog(Activity activity) {
        this.activity = activity;
        mDialog = new Dialog(this.activity, R.style.transparentFrameWindowStyle);

        initDialog();


    }

    private void initDialog() {
        View view = activity.getLayoutInflater().inflate(R.layout.friend_circle_choose_dialog, null);
        btnGallery = (Button) view.findViewById(R.id.btn_gallery);
        btnSmallVideo = (Button) view.findViewById(R.id.btn_small_video);

        mDialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

         window = mDialog.getWindow();

        // 设置显示动画
//        window.setWindowAnimations(R.style.main_menu_animstyle);
//        window.setBackgroundDrawable(new ColorDrawable(0x11000000)); //设置透明
//        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams wl = window.getAttributes();
        //Dialog默认的Gravity是Center,x,y为0,表示在正中间
        wl.x = 0;
        wl.y = 0;
//        wl.alpha=0.8f; //not working
//        wl.dimAmount=0.8f;//only darken the screen
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        mDialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        mDialog.setCanceledOnTouchOutside(true);

        initListener();
    }


    private void initListener() {
        btnGallery.setOnClickListener(this);
        btnSmallVideo.setOnClickListener(this);
    }

    public void show() {
        if (!mDialog.isShowing()) {
//            new BlurAsyncTask().execute();  //using this to blur background of dialog
            mDialog.show();
        } else {
            dismiss();
        }
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_gallery) {
            EasyImage.openGallery(activity, 0);
            dismiss();
        } else if (v.getId() == R.id.btn_small_video) {
            // TODO: 2016/6/27   实现小视频录制上传
            EasyImage.openGallery(activity, 0);
            dismiss();

        }
    }


    /**
     * not recommend use in app cos slow responding
     */
    class BlurAsyncTask extends AsyncTask<Void, Integer, Bitmap> {

        private final String TAG = BlurAsyncTask.class.getName();

        protected Bitmap doInBackground(Void... arg0) {

            Bitmap map = BlurViewUtil.takeScreenShot(activity);

            Bitmap fast = new BlurViewUtil().fastBlur(map, 10);
            return fast;
        }


        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                blurDialogBackground(result);
            }
            mDialog.show();
        }
    }

    private void blurDialogBackground(Bitmap blurBackground) {

        final Drawable draw=new BitmapDrawable(activity.getResources(),blurBackground);

        window.setBackgroundDrawable(draw);
        WindowManager.LayoutParams wl = window.getAttributes();
        //Dialog默认的Gravity是Center,x,y为0,表示在正中间
        wl.x = 0;
        wl.y = 0;
//        wl.alpha=0.8f; //not working
//        wl.dimAmount=0.8f;//only darken the screen
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
    }
}
