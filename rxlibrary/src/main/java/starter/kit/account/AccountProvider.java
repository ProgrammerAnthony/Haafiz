package starter.kit.account;

import starter.kit.model.entity.Account;

public interface AccountProvider {
  Account provideAccount(String accountJson);
}
