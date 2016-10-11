package starter.kit.rx.app.feature.auth;

import android.os.Bundle;
import icepick.State;
import rx.Observable;
import starter.kit.feature.rx.RxNetworkPresenter;
import starter.kit.rx.app.model.entity.User;
import starter.kit.rx.app.network.ApiService;
import starter.kit.rx.app.network.service.AuthService;
import starter.kit.util.RxUtils;

/**
 * Created by YuGang Yang on 06 29, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
public class AuthPresenter extends RxNetworkPresenter<User, LoginActivity> {

  private AuthService mAuthService;

  @State String username;
  @State String password;

  @Override protected void onCreate(Bundle savedState) {
    super.onCreate(savedState);
    mAuthService = ApiService.createAuthService();
  }

  @Override public Observable<User> request() {
    return mAuthService.login(username, password);
  }

  @Override public void showHud() {
    RxUtils.showHud(getView(), "Login...", () -> stop());
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    mAuthService = null;
  }

  void requestItem(String username, String password) {
    this.username = username;
    this.password = password;
    start();
  }
}
