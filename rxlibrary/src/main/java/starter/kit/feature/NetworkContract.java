package starter.kit.feature;

import starter.kit.retrofit.RetrofitException;

public class NetworkContract {

  public interface View {
    void onSuccess(Object item);

    void onError(RetrofitException exception);
  }
}
