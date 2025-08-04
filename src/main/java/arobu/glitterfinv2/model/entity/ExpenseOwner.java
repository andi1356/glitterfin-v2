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

    public String getUserAgentId() {
        return userAgentId;
    }

    @Override
    public String toString() {
        return "ExpenseOwner{" +
                "id='" + id + '\'' +
                ", userAgentId='" + userAgentId + '\'' +
                '}';
    }
}
