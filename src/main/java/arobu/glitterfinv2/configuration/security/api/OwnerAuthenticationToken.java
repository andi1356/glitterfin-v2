package arobu.glitterfinv2.configuration.security.api;

import arobu.glitterfinv2.model.entity.Owner;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class OwnerAuthenticationToken extends AbstractAuthenticationToken {

    private final Owner owner;
    private final Object credentials;

    public OwnerAuthenticationToken(Owner owner,
                                    Object credentials,
                                    Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.owner = owner;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Owner getPrincipal() {
        return owner;
    }
}