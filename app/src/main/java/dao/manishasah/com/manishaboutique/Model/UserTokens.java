package dao.manishasah.com.manishaboutique.Model;

/**
 * Created by Sourav Guchait on 4/24/2018.
 */

public class UserTokens {

    private User user;
    private String token;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
