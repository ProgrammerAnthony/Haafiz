package starter.kit.rx.app.model.entity;

import android.net.Uri;
import android.text.TextUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.parceler.Parcel;
import starter.kit.model.entity.Entity;

@Parcel @JsonIgnoreProperties(ignoreUnknown = true) public class UserInfo extends Entity {

  @JsonProperty("user_id") public String userId;
  public String nickname;
  public String avatar;

  public Uri uri() {
    if (TextUtils.isEmpty(avatar)) return null;

    if (avatar.startsWith("http://")) {
      return Uri.parse(avatar);
    }

    return null;
  }
}
