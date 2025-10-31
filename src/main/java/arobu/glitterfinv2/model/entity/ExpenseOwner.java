package arobu.glitterfinv2.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity(name = "owner")
public class ExpenseOwner implements UserDetails {
    @Id
    private String username;
    private String userAgentId;
    private String apiToken;
    private String details;
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

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return DEFAULT_AUTHORITIES;
    }

    @Transient
    private static final List<GrantedAuthority> DEFAULT_AUTHORITIES =
            List.of(new SimpleGrantedAuthority("ROLE_USER"));


    @Override
    public String toString() {
        return "ExpenseOwner{" + username + '}';
    }
}
