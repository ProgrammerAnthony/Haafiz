package starter.kit.rx.app.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import starter.kit.rx.app.R;
import starter.kit.util.Hud;
import starter.kit.util.ImageLoader;
import work.wanghao.simplehud.SimpleHUD;

public class InitializeUtil {

  public static void initialize() {
    initializeDrawerImageLoader();
    initializeImageLoader();
    initializeHud();
  }

  private static void initializeDrawerImageLoader() {
    //initialize and create the image loader logic
    DrawerImageLoader.init(new AbstractDrawerImageLoader() {
      @Override public void set(ImageView imageView, Uri uri, Drawable placeholder) {
        Glide.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
      }

      @Override public void cancel(ImageView imageView) {
        Glide.clear(imageView);
      }

      @Override public Drawable placeholder(Context ctx, String tag) {
        //define different placeholders for different imageView targets
        //default tags are accessible via the DrawerImageLoader.Tags
        //custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
        if (DrawerImageLoader.Tags.PROFILE.name().equals(tag)) {
          return DrawerUIUtils.getPlaceHolder(ctx);
        } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name().equals(tag)) {
          return new IconicsDrawable(ctx).iconText(" ")
              .backgroundColorRes(com.mikepenz.materialdrawer.R.color.primary)
              .sizeDp(56);
        } else if ("customUrlItem".equals(tag)) {
          return new IconicsDrawable(ctx).iconText(" ")
              .backgroundColorRes(R.color.md_red_500)
              .sizeDp(56);
        }

        //we use the default one for
        //DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()
        return super.placeholder(ctx, tag);
      }
    });
  }

  private static void initializeImageLoader() {
    ImageLoader.initialize(new ImageLoader.AbstractImageLoader() {
      @Override public void displayImageView(ImageView imageView, Uri uri, Drawable placeholder, int width, int height) {
        if (imageView instanceof SimpleDraweeView) {
          SimpleDraweeView simpleDraweeView = (SimpleDraweeView) imageView;
          ResizeOptions options = new ResizeOptions(width, height);
          ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri).setResizeOptions(options).build();
          PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
              .setOldController(simpleDraweeView.getController())
              .setImageRequest(request)
              .build();
          simpleDraweeView.setController(controller);
        }
      }
    });
  }

  private static void initializeHud() {
    SimpleHUD.backgroundHexColor = "#aaEF6C00";
    Hud.initialize(new Hud.HudInterface() {
      @Override public void showMessage(Context context, String msg) {
        SimpleHUD.showErrorMessage(context, msg);
      }

      @Override public void showHud(Context context, String msg, boolean cancelable,
          Hud.HudCallback callback) {
        if (cancelable) {
          SimpleHUD.showLoadingMessage(context, msg, true, new SimpleHUD.SimpleHUDCallback() {
            @Override public void onSimpleHUDDismissed() {
              if (callback != null) {
                callback.onDismissed();
              }
            }
          });
        } else {
          SimpleHUD.showLoadingMessage(context, msg, false);
        }
      }

      @Override public void dismissHud() {
        SimpleHUD.dismiss();
      }
    });
  }
}
