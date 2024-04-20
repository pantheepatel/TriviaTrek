public interface Authenticator {
    boolean registerUser(String username, String password);
    boolean loginUser(String username, String password);
    void logoutUser(String username);
}
