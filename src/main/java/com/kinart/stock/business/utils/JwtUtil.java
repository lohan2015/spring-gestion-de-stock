package com.kinart.stock.business.utils;

import com.kinart.stock.business.model.auth.ExtendedUser;
import io.jsonwebtoken.*;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtUtil {

  private String SECRET_KEY = "secret";

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public String extractIdEntreprise(String token) {
    final Claims claims = extractAllClaims(token);

    return claims.get("idEntreprise", String.class);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    try {
      return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    } catch (ExpiredJwtException e) {
      System.out.println(" Token expired ");
      e.printStackTrace();
    } catch (SignatureException e) {
      e.printStackTrace();
    } catch(Exception e){
      System.out.println(" Some other exception in JWT parsing ");
    }
    return null;
  }

  private Boolean isTokenExpired(String token) {
    //return false;
    return extractExpiration(token).before(new Date());
  }

  public String generateToken(ExtendedUser userDetails) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, userDetails);
  }

  private String createToken(Map<String, Object> claims, ExtendedUser userDetails) {
    //System.out.println("GENERATE TOCKEN.................");
    return Jwts.builder().setClaims(claims)
        .setSubject(userDetails.getUsername())
        //.setIssuedAt(new Date(System.currentTimeMillis()))
        // Fri Jun 24 2016 15:33:42 GMT-0400 (EDT)
        .setIssuedAt(Date.from(Instant.ofEpochSecond(1466796822L)))
        // Sat Jun 24 2116 15:33:42 GMT-0400 (EDT)
        .setExpiration(Date.from(Instant.ofEpochSecond(4622470422L)))
        //.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60000 * 60000 * 1000000))
        .claim("idEntreprise", userDetails.getIdEntreprise().toString())
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

}
