package com.example.filters;

import com.example.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                if (exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        authHeader = authHeader.substring(7);
                    }
                    try {
                        jwtUtil.validateToken(authHeader);
                        System.out.println("Accessed....");
                    } catch (Exception e) {
                        System.out.println("invalid access...!");
                        return Mono.error(new RuntimeException("unauthorized access to application"));
                    }
                } else {
                    // If AUTHORIZATION header is missing, check the token query parameter
                    String tokenQueryParam = exchange.getRequest().getQueryParams().getFirst("token");
                    if (tokenQueryParam == null || tokenQueryParam.isEmpty()) {
                        return Mono.error(new RuntimeException("missing authorization header and token query parameter"));
                    }
                    try {
                        jwtUtil.validateToken(tokenQueryParam);
                        System.out.println("Accessed....");
                    } catch (Exception e) {
                        System.out.println("invalid access...!");
                        return Mono.error(new RuntimeException("unauthorized access to application"));
                    }
                }
            }
            return chain.filter(exchange);
        });
    }
    public static class Config {

    }

}
