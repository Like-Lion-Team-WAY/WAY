package like.lion.way.jwt.token;


import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private String token;
    private Object principal;
    private Object credentials;

    public JwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities , Object principal, Object credentials) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.setAuthenticated(true);
    }
    JwtAuthenticationToken(String token){
        super(null);
        this.token = token;
        this.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
