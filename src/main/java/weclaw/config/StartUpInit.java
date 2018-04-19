package weclaw.config;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
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

    @Autowired
    DataSource dataSource;

    @PostConstruct
    public void init(){

        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.migrate();

        if(!applicationUserRepository.findByUsername(securityConfigProperties.getAdminUsername()).isPresent()) {
            ApplicationUser admin = new ApplicationUser();
            admin.setUsername(securityConfigProperties.getAdminUsername());
            admin.setPassword(passwordEncoder.encode(securityConfigProperties.getAdminPassword()));
            admin.setAdmin(true);
            applicationUserRepository.save(admin);
        }
    }
}