package starter.kit.rx.app.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.parceler.Parcel;
import starter.kit.model.entity.Entity;

@Parcel @JsonIgnoreProperties(ignoreUnknown = true) public class Category extends Entity {

  public String name;
}
