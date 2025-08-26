package arobu.glitterfinv2.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "owner")
public class ExpenseOwner {
    @Id
    private String userAgentId;
    private String apiToken;
    private String details;
    private String username;
    private String password;

    public String getUserAgentId() {
        return userAgentId;
    }

    public String getApiToken() {
        return apiToken;
    }

    public String getDetails() {
        return details;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "ExpenseOwner{" +
                "userAgentId='" + userAgentId + '\'' +
                '}';
    }
}
