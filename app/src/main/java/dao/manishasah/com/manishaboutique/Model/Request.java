package dao.manishasah.com.manishaboutique.Model;

/**
 * Created by Sourav Guchait on 4/16/2018.
 */

public class Request {
    private String DateTime;
    private Product product;
    private String UserId;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public Request() {
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
