package services;

public class AuthService {
    private final String USER = "admin";
    private final String PASS = "1234";

    public boolean authenticate(String u, String p) {
        return USER.equals(u) && PASS.equals(p);
    }
}
