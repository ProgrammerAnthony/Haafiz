package support.ui.adapters;

import android.content.Context;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseEasyViewHolderFactory {

  protected Context context;

  private Map<Class, Class<? extends EasyViewHolder>> boundViewHolders = new HashMap<>();
  private List<Class> valueClassTypes = new ArrayList<>();

  public BaseEasyViewHolderFactory(Context context) {
    this.context = context;
  }

  public EasyViewHolder create(int viewType, ViewGroup parent) {
    Class valueClass = valueClassTypes.get(viewType);
    try {
      Class<? extends EasyViewHolder> easyViewHolderClass = boundViewHolders.get(valueClass);
      Constructor<? extends EasyViewHolder> constructor =
          easyViewHolderClass.getDeclaredConstructor(Context.class, ViewGroup.class);
      return constructor.newInstance(context, parent);
    } catch (Throwable e) {
      throw new RuntimeException(
          "Unable to create ViewHolder for" + valueClass + ". " + e.getCause().getMessage(), e);
    }
  }

  void bind(Class valueClass, Class<? extends EasyViewHolder> viewHolder) {
    valueClassTypes.add(valueClass);
    boundViewHolders.put(valueClass, viewHolder);
  }

  public int itemViewType(Object object) {
    return valueClassTypes.indexOf(object.getClass());
  }

  public List<Class> getValueClassTypes() {
    return valueClassTypes;
  }

  public Map<Class, Class<? extends EasyViewHolder>> getBoundViewHolders() {
    return boundViewHolders;
  }
}
