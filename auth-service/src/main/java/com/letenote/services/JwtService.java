package com.letenote.services;

import io.smallrye.jwt.build.Jwt;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class JwtService {
    public String generateToken(){
        Set<String> roles = new HashSet<>(
                Arrays.asList("admin", "merchant")
        );
        return Jwt.issuer("auth-service-secret-issuer")
                .subject("auth-service")
                .groups(roles)
                .expiresAt(System.currentTimeMillis() + 3600)
                .sign();
    }
}
