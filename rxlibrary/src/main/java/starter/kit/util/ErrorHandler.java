package starter.kit.util;

import java.io.IOException;

import starter.kit.retrofit.ErrorResponse;
import starter.kit.retrofit.RetrofitException;

public final class ErrorHandler {

  public static ErrorResponse handleThrowable(Throwable throwable) {
    if (throwable instanceof RetrofitException) {
      RetrofitException retrofitException = (RetrofitException) throwable;
      try {
        return retrofitException.getErrorBodyAs(ErrorResponse.class);
      } catch (IOException e) {
        e.printStackTrace();
        return new ErrorResponse(500, e.getLocalizedMessage());
      }
    }
    return null;
  }
}
