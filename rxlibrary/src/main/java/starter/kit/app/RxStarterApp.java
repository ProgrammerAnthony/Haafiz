package starter.kit.app;

import starter.kit.account.AccountProvider;
import starter.kit.model.entity.Account;
import support.ui.app.SupportApp;

public class RxStarterApp extends SupportApp implements AccountProvider {

  @Override
  public void onCreate() {
    super.onCreate();
  }

  @Override
  public Account provideAccount(String accountJson) {
    return null;
  }
}
