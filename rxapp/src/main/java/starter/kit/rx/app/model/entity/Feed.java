package starter.kit.rx.app.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import org.parceler.Parcel;
import starter.kit.model.entity.Entity;

@Parcel @JsonIgnoreProperties(ignoreUnknown = true) public class Feed extends Entity {

  public String content;
  @JsonProperty("created_at") public int createdAt;

  @JsonProperty("user_info") public UserInfo userInfo;

  public Category category;

  public ArrayList<Image> images;
}
