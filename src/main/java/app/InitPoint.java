package app;

import java.io.Serializable;

public class InitPoint implements Serializable {
    private String urlToMySQL;
    private String loginToMySQL;
    private String passwordToMySQL;
    private int numberOfDataBaseItems;

    public InitPoint() {
    }

    public String getLoginToMySQL() {
        return loginToMySQL;
    }

    public String getUrlToMySQL() {
        return urlToMySQL;
    }

    public void setUrlToMySQL(String urlToMySQL) {
        this.urlToMySQL = urlToMySQL;
    }

    public void setLoginToMySQL(String loginToMySQL) {
        this.loginToMySQL = loginToMySQL;
    }

    public String getPasswordToMySQL() {
        return passwordToMySQL;
    }

    public void setPasswordToMySQL(String passwordToMySQL) {
        this.passwordToMySQL = passwordToMySQL;
    }

    public int getNumberOfDataBaseItems() {
        return numberOfDataBaseItems;
    }

    public void setNumberOfDataBaseItems(int numberOfDataBaseItems) {
        this.numberOfDataBaseItems = numberOfDataBaseItems;
    }

    @Override
    public String toString() {
        return "app.InitPoint{" +
                "Number of DB items = " + numberOfDataBaseItems +
                '}';
    }
}
