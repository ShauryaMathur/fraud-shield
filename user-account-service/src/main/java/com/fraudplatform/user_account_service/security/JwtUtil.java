package com.fraudplatform.user_account_service.security;

import com.fraudplatform.user_account_service.entity.Permission;
import com.fraudplatform.user_account_service.entity.Role;
import com.fraudplatform.user_account_service.entity.RolePermission;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String userId){
        return Jwts.builder()
                .subject(userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateToken(String userId, List<RolePermission> permissions, Role role){
        return Jwts.builder()
                .subject(userId)
                .claim("permissions", getJoinedPermissions(permissions))
                .claim("role", role.getName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    private static String getJoinedPermissions(List<RolePermission> permissions){
        StringBuilder joinedPermissions = new StringBuilder();
        for(RolePermission permission : permissions){
            joinedPermissions.append(permission.getPermission().getValue()).append(",");
        }
        joinedPermissions.deleteCharAt(joinedPermissions.length()-1);
        return joinedPermissions.toString();
    }

    public String extractUserId(String token){
        return getClaimsFromToken(token).getSubject();
    }

    private Claims getClaimsFromToken(String token){
        var parser = Jwts.parser()
                .verifyWith(getSigningKey())
                .build();
        return parser.parseSignedClaims(token).getPayload();
    }

    public boolean isTokenValid(String token){
        try{
            getClaimsFromToken(token);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
