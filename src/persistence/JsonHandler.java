package persistence;

import interfaces.Persistence;
import core.*;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonHandler implements Persistence {

    private static final String ACCOUNT_FILE = "account.json";
    private static final String ATM_FILE = "atm_state.json"; // Changed name to be safe

    @Override
    public Account loadAccount(String card) {
        try {
            JSONObject obj = new JSONObject(Files.readString(Paths.get(ACCOUNT_FILE)));
            return new Account(
                    obj.getString("card"),
                    obj.getString("pin"),
                    obj.getDouble("balance")
            );
        } catch (Exception e) {
            return new Account(card, "1234", 500);
        }
    }

    @Override
    public void saveAccount(Account account) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("card", account.getCardNumber());
            obj.put("pin", account.getPin());
            obj.put("balance", account.getBalance());
            Files.writeString(Paths.get(ACCOUNT_FILE), obj.toString(2));
        } catch (Exception ignored) {}
    }

    @Override
    public ATMState loadATMState() {
        try {
            JSONObject obj = new JSONObject(Files.readString(Paths.get(ATM_FILE)));

            // 1. Create the state object
            ATMState state = new ATMState(
                    obj.getDouble("cash"),
                    obj.getString("firmware"),
                    new PaperTank(obj.getInt("paper"))
            );

            // 2. LOAD THE INK LEVEL FROM JSON
            if (obj.has("ink")) {
                state.setInkLevel(obj.getInt("ink"));
            } else {
                state.setInkLevel(15); // Default hint if missing
            }

            return state;
        } catch (Exception e) {
            // Default "Hint" state for your demo if file doesn't exist
            return new ATMState(2000, "v1.0.0", new PaperTank(5));
        }
    }

    @Override
    public void saveATMState(ATMState state) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("cash", state.getCashAvailable());
            obj.put("firmware", state.getFirmwareVersion());
            obj.put("paper", state.getPaperTank().getPaperCount()); // Use the correct method name

            // 3. SAVE THE INK LEVEL TO JSON
            obj.put("ink", state.getInkLevel());

            Files.writeString(Paths.get(ATM_FILE), obj.toString(2));
        } catch (Exception ignored) {
            System.out.println("DEBUG: Save failed!");
        }
    }
}