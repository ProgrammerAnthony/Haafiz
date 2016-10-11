package starter.kit.retrofit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true) public class ErrorResponse {

  @JsonProperty("status_code") public int mStatusCode;
  @JsonProperty("message") public String mMessage;
  public String mTitle;

  public ErrorResponse() {
  }

  public ErrorResponse(int statusCode, String title, String msg) {
    this.mStatusCode = statusCode;
    this.mTitle = title;
    this.mMessage = msg;
  }

  public ErrorResponse(int statusCode, String msg) {
    this.mStatusCode = statusCode;
    this.mMessage = msg;
  }

  public String getMessage() {
    return mMessage == null ? "" : mMessage;
  }

  public int getStatusCode() {
    return mStatusCode;
  }
}
