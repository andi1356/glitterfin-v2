package arobu.glitterfinv2.configuration;

import arobu.glitterfinv2.model.entity.Owner;
import arobu.glitterfinv2.model.repository.OwnerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Creates or refreshes the demo account that is referenced by the project documentation.
 */
@Component
public class DemoAccountInitializer implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoAccountInitializer.class);

    public static final String DEMO_USERNAME = "demo@glitterfin.app";
    public static final String DEMO_PASSWORD = "DemoPassword123!";
    public static final String DEMO_API_TOKEN = "demo-api-token";
    public static final String DEMO_USER_AGENT = "GlitterfinDemoClient";
    public static final String DEMO_DETAILS = "Demo account generated on application startup";

    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;

    public DemoAccountInitializer(OwnerRepository ownerRepository,
                                  PasswordEncoder passwordEncoder) {
        this.ownerRepository = ownerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        ownerRepository.findById(DEMO_USERNAME)
                .ifPresentOrElse(this::refreshDemoOwner, this::createDemoOwner);
    }

    private void createDemoOwner() {
        LOGGER.info("Creating demo account {}", DEMO_USERNAME);
        Owner owner = new Owner()
                .setUsername(DEMO_USERNAME)
                .setDetails(DEMO_DETAILS)
                .setUserAgentId(DEMO_USER_AGENT)
                .setPassword(passwordEncoder.encode(DEMO_PASSWORD))
                .setApiToken(passwordEncoder.encode(DEMO_API_TOKEN));
        ownerRepository.save(owner);
    }

    private void refreshDemoOwner(Owner owner) {
        boolean updated = false;

        if (!passwordEncoder.matches(DEMO_PASSWORD, owner.getPassword())) {
            owner.setPassword(passwordEncoder.encode(DEMO_PASSWORD));
            updated = true;
        }

        if (!passwordEncoder.matches(DEMO_API_TOKEN, owner.getApiToken())) {
            owner.setApiToken(passwordEncoder.encode(DEMO_API_TOKEN));
            updated = true;
        }

        if (!DEMO_USER_AGENT.equals(owner.getUserAgentId())) {
            owner.setUserAgentId(DEMO_USER_AGENT);
            updated = true;
        }

        if (!DEMO_DETAILS.equals(owner.getDetails())) {
            owner.setDetails(DEMO_DETAILS);
            updated = true;
        }

        if (updated) {
            LOGGER.info("Refreshing credentials for demo account {}", DEMO_USERNAME);
            ownerRepository.save(owner);
        }
    }
}

