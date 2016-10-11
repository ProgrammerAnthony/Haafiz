package starter.kit.rx.app.network;

import retrofit2.Retrofit;
import starter.kit.retrofit.Network;
import starter.kit.rx.app.network.service.AuthService;
import starter.kit.rx.app.network.service.FeedService;

public class ApiService {

  public static AuthService createAuthService() {
    return retrofit().create(AuthService.class);
  }

  public static FeedService createFeedService() {
    return retrofit().create(FeedService.class);
  }

  private static Retrofit retrofit() {
    return Network.get().retrofit();
  }
}
