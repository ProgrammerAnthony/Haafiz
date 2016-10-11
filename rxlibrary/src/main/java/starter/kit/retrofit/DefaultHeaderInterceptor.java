package starter.kit.retrofit;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import starter.kit.account.AccountManager;
import starter.kit.util.Strings;

/**
 * @author <a href="mailto:smartydroid.com@gmail.com">Smartydroid</a>
 */
public class DefaultHeaderInterceptor implements HeaderInterceptor {

  private Headers.Builder builder;
  public DefaultHeaderInterceptor(Headers.Builder builder) {
    this.builder = builder;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request originalRequest = chain.request();
    if (builder == null) {
      builder = new Headers.Builder();
    }

    final String token = AccountManager.INSTANCE.token();
    if (!Strings.isBlank(token)) {
      builder.set("Authorization", "Bearer " + token);
    }

    Request compressedRequest = originalRequest
        .newBuilder()
        .headers(builder.build())
        .build();

    return chain.proceed(compressedRequest);
  }
}
