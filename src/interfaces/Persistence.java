package interfaces;

import core.Account;
import core.ATMState;

public interface Persistence {
    Account loadAccount(String cardNumber);
    void saveAccount(Account account);

    ATMState loadATMState();
    void saveATMState(ATMState state);
}
