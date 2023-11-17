package ink.ckx.mo.auth.authentication.base;

import lombok.Getter;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @author chenkaixin
 * @description 身份验证抽象类
 * @since 2023/11/11
 */
@Getter
public abstract class BaseAuthenticationToken extends AbstractAuthenticationToken {

    private final AuthorizationGrantType authorizationGrantType;

    private final Authentication clientPrincipal;

    private final Set<String> scopes;

    private final Map<String, Object> additionalParameters;

    public BaseAuthenticationToken(AuthorizationGrantType authorizationGrantType,
                                   Authentication clientPrincipal, @Nullable Set<String> scopes,
                                   @Nullable Map<String, Object> additionalParameters) {
        super(Collections.emptyList());
        Assert.notNull(authorizationGrantType, "authorizationGrantType cannot be null");
        Assert.notNull(clientPrincipal, "clientPrincipal cannot be null");
        this.authorizationGrantType = authorizationGrantType;
        this.clientPrincipal = clientPrincipal;
        this.scopes = Collections.unmodifiableSet(scopes != null ? new HashSet<>(scopes) : Collections.emptySet());
        this.additionalParameters = Collections.unmodifiableMap(
                additionalParameters != null ? new HashMap<>(additionalParameters) : Collections.emptyMap());
    }

    /**
     * 获取密码
     */
    @Override
    public Object getCredentials() {
        return "";
    }

    /**
     * 获取用户名
     */
    @Override
    public Object getPrincipal() {
        return this.clientPrincipal;
    }
}