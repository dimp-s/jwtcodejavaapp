package dev.dipesh.jwtcodejavaapp.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import dev.dipesh.jwtcodejavaapp.userapi.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenUtil {
    private static final long EXPIRE_DURATION = 24*60*60*1000; // 24 hrs 

    //logging error 
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
    @Value("${app.jwt.secret}")
    private String secretKey;

    public String generateAccessToken(User user){
        return Jwts.builder().setSubject(user.getId() + "," +user.getEmail())
        .setIssuer("Darsetech")
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
        .signWith(SignatureAlgorithm.HS512, secretKey)
        .compact();
    }

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException error) {
            LOGGER.error("Token has expired!", error);
        } catch(IllegalArgumentException error){
            LOGGER.error("Token is empty!", error);
        } catch(MalformedJwtException error) {
            LOGGER.error("Token invalid!", error);
        } catch (UnsupportedJwtException error){
            LOGGER.error("Unsupported token!", error);
        } catch (SignatureException error){
            LOGGER.error("Signature validation failed!", error);
        }
        return false;        
    }

    public String getSubject(String token){
        return parseClaims(token).getSubject();
    }

    private Claims parseClaims(String token){
        return Jwts.parser()
        .setSigningKey(secretKey).
        parseClaimsJws(token).
        getBody();
    }
}
