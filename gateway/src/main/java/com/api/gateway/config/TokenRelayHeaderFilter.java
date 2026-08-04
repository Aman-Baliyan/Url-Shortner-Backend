package com.api.gateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class TokenRelayHeaderFilter extends AbstractGatewayFilterFactory<TokenRelayHeaderFilter.Config> {
    public TokenRelayHeaderFilter() { super(Config.class); }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> securityContext.getAuthentication().getPrincipal())
                .filter(principal -> principal instanceof Jwt)
                .cast(Jwt.class)
                .map(jwt -> exchange.getRequest().mutate()
                        .header("X-User-Id", jwt.getClaimAsString("userId"))
                        .header("X-User-Roles", String.join(",", jwt.getClaimAsStringList("roles")))
                        .build())
                .flatMap(mutatedRequest -> chain.filter(exchange.mutate().request(mutatedRequest).build()))
                .switchIfEmpty(chain.filter(exchange));
    }
    public static class Config {}
}
