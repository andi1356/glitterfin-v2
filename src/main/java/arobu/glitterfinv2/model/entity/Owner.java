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
public class Owner implements UserDetails {
    @Id
    private String username;
    private String userAgentId;
    private String apiToken;
    private String details;
    private String password;

    public String getUserAgentId() {
        return userAgentId;
    }

    public Owner setUserAgentId(String userAgentId) {
        this.userAgentId = userAgentId;
        return this;
    }

    public String getApiToken() {
        return apiToken;
    }

    public Owner setApiToken(String apiToken) {
        this.apiToken = apiToken;
        return this;
    }

    public String getDetails() {
        return details;
    }

    public Owner setDetails(String details) {
        this.details = details;
        return this;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public Owner setUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public Owner setPassword(String password) {
        this.password = password;
        return this;
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
        return "Owner{" + username + '}';
    }
}
