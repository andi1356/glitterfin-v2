package arobu.glitterfinv2.configuration.security.web;

import arobu.glitterfinv2.service.OwnerService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class OwnerDetailsService implements UserDetailsService {

    private final OwnerService ownerService;

    public OwnerDetailsService(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return ownerService.getOwner(username);
    }
}
