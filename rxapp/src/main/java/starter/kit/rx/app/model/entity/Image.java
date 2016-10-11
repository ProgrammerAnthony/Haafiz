package starter.kit.rx.app.model.entity;

import android.net.Uri;
import android.text.TextUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.parceler.Parcel;
import starter.kit.model.entity.Entity;

@Parcel @JsonIgnoreProperties(ignoreUnknown = true) public class Image extends Entity {
  public String url;
  public int width;
  public int height;

  public Uri uri() {
    if (TextUtils.isEmpty(url)) return null;

    if (url.startsWith("http://")) {
      return Uri.parse(url);
    }

    return null;
  }

  public Uri uri(int w, int h) {
    if (TextUtils.isEmpty(url)) return null;

    if (url.startsWith("http://")) {
      return Uri.parse(url);
    }

    // 200w_300h_1e_1c
    //String.format("%s%dw_%dh_1e_1c", Profile.OSS_ENDPOINT + url, w, h);
    return null;
  }
}
