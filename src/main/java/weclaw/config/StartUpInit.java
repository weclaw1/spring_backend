package weclaw.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import weclaw.domain.ApplicationUser;
import weclaw.repositories.ApplicationUserRepository;

@Component
public class StartUpInit {

    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityConfigProperties securityConfigProperties;

    @PostConstruct
    public void init(){
        ApplicationUser admin = new ApplicationUser();
        admin.setUsername(securityConfigProperties.getAdminUsername());
        admin.setPassword(passwordEncoder.encode(securityConfigProperties.getAdminPassword()));
        admin.setAdmin(true);
        applicationUserRepository.save(admin);
    }
}