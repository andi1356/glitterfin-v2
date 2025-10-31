package arobu.glitterfinv2.service;

import arobu.glitterfinv2.model.entity.Owner;
import arobu.glitterfinv2.model.repository.OwnerRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;

    public OwnerService(OwnerRepository ownerRepository, PasswordEncoder passwordEncoder) {
        this.ownerRepository = ownerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Owner getOwner(String username) {
        return ownerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public Optional<Owner> getOwner(String userAgentId, String apiKey) {
        return ownerRepository
                .getOwnerByUserAgentId(userAgentId)
                .filter(owner -> passwordEncoder.matches(apiKey, owner.getApiToken()));
    }
}
