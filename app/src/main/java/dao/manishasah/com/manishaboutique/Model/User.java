package dao.manishasah.com.manishaboutique.Model;

import java.io.Serializable;

/**
 * Created by Sourav Guchait on 4/18/2018.
 */

public class User implements Serializable {
private String userId;
private int clientMode;
private int theme;
private String password;
private String clientPassword;
private int loginStatus;


    public User() {
        userId="defualt";
        clientMode=0;
        theme=1;
        password="default";
        clientPassword="clientDefault";
        loginStatus=0;
    }

    public int getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getClientMode() {
        return clientMode;
    }

    public void setClientMode(int clientMode) {
        this.clientMode = clientMode;
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientPassword() {
        return clientPassword;
    }

    public void setClientPassword(String clientPassword) {
        this.clientPassword = clientPassword;
    }
}
