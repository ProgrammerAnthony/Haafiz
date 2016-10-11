package starter.kit.account;

import android.content.Context;
import android.text.TextUtils;

import starter.kit.app.RxStarterApp;
import starter.kit.model.entity.Account;

public enum AccountManager {

  INSTANCE;

  private final AuthPreferences authPreferences;
  private Account mCurrentAccount;
  private Context mContext;

  AccountManager() {
    mContext = RxStarterApp.appContext();
    authPreferences = new AuthPreferences(mContext);
  }

  public boolean isLogin() {
    return authPreferences.isLogin();
  }

  public void logout() {
    mCurrentAccount = null;
    authPreferences.clear();
  }

  public void storeAccount(Account account) {
    mCurrentAccount = account;
    authPreferences.setToken(account.token());
    authPreferences.setUser(account.name());
    authPreferences.setUserData(account.toJson());
  }

  public String token() {
    return authPreferences.getToken();
  }

  public String user() {
    return authPreferences.getUser();
  }

  @SuppressWarnings("unchecked") public <T extends Account> T getCurrentAccount() {
    if (mCurrentAccount == null) {
      String accountJson = authPreferences.getUserData();
      if (!TextUtils.isEmpty(accountJson) && mContext instanceof AccountProvider) {
        mCurrentAccount = ((AccountProvider) mContext).provideAccount(accountJson);
      }
    }
    return (T) mCurrentAccount;
  }
}
