package weclaw.security;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import weclaw.config.SecurityConfigProperties;

import static weclaw.security.SecurityConstants.HEADER_STRING;
import static weclaw.security.SecurityConstants.TOKEN_PREFIX;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private SecurityConfigProperties securityConfigProperties;
    
    public JWTAuthorizationFilter(AuthenticationManager authManager, SecurityConfigProperties securityConfigProperties) {
        super(authManager);
        this.securityConfigProperties = securityConfigProperties;
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
        if(token.isPresent()) {
            // parse the token.
            Claims claims = Jwts.parser()
                    .setSigningKey(securityConfigProperties.getSecret().getBytes())
                    .parseClaimsJws(token.get().replace(TOKEN_PREFIX, ""))
                    .getBody();

            Optional<String> username = Optional.ofNullable(claims.getSubject());

            if(username.isPresent()) {
                List<GrantedAuthority> grantedAuthorities = AuthorityUtils.NO_AUTHORITIES;
                Boolean isAdmin = Optional.ofNullable((Boolean) claims.get("admin")).orElse(false);
                if(isAdmin) {
                    grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN");
                }
                return new UsernamePasswordAuthenticationToken(username.get(), null, grantedAuthorities);
            }
            return null;
        }
        return null;
    }
}