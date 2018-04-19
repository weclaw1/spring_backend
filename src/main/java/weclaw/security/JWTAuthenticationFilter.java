package weclaw.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.User;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import weclaw.config.SecurityConfigProperties;
import weclaw.domain.ApplicationUser;
import weclaw.repositories.ApplicationUserRepository;

import static weclaw.security.SecurityConstants.HEADER_STRING;
import static weclaw.security.SecurityConstants.TOKEN_PREFIX;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    
    private AuthenticationManager authenticationManager;

    private SecurityConfigProperties securityConfigProperties;

    private ApplicationUserRepository applicationUserRepository;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, SecurityConfigProperties securityConfigProperties,
                                   ApplicationUserRepository applicationUserRepository) {
        this.authenticationManager = authenticationManager;
        this.securityConfigProperties = securityConfigProperties;
        this.applicationUserRepository = applicationUserRepository;
    }

    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
            ApplicationUser user = new ObjectMapper().readValue(req.getInputStream(), ApplicationUser.class);

            return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword(),
                    new ArrayList<>()
                )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException, NoSuchElementException {

        ApplicationUser user = applicationUserRepository.findByUsername(((User) auth.getPrincipal()).getUsername()).get();

        Claims claims = Jwts.claims().setSubject(user.getUsername())
                            .setExpiration(new Date(System.currentTimeMillis() + securityConfigProperties.getExpirationTime()));
                            
        claims.put("admin", user.getAdmin());
        claims.put("id", user.getId());

        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, securityConfigProperties.getSecret().getBytes())
                .setClaims(claims)
                .compact();

        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode tokenNode = objectMapper.createObjectNode();
        tokenNode.put("token", token);

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(res.getWriter(), tokenNode);
    }
}