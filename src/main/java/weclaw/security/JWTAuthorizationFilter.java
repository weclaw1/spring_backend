package weclaw.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import weclaw.config.SecurityConfigProperties;

import static weclaw.security.SecurityConstants.HEADER_STRING;
import static weclaw.security.SecurityConstants.TOKEN_PREFIX;

@Component
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    @Autowired
    private SecurityConfigProperties securityConfigProperties;
    
    @Autowired
    public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
                                        
        Optional<String> header = Optional.ofNullable(req.getHeader(HEADER_STRING));

        header.ifPresent(headerVal -> {
            if(headerVal.startsWith(TOKEN_PREFIX)) {
                UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        });

        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        Optional<String> token = Optional.ofNullable(request.getHeader(HEADER_STRING));
        if (token.isPresent()) {
            // parse the token.
            String user = Jwts.parser()
                    .setSigningKey(securityConfigProperties.getSecret().getBytes())
                    .parseClaimsJws(token.get().replace(TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject();

            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}