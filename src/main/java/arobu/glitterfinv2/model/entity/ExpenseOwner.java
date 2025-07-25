package arobu.glitterfinv2.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "owner")
public class ExpenseOwner {
    @Id
    private String id;
    private String userAgentId;

    public String getId() {
        return id;
    }

    public ExpenseOwner setId(String id) {
        this.id = id;
        return this;
    }

    public String getUserAgentId() {
        return userAgentId;
    }

    public ExpenseOwner setUserAgentId(String userAgentId) {
        this.userAgentId = userAgentId;
        return this;
    }
}
