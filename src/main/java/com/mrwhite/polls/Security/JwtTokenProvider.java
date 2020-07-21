package com.mrwhite.polls.Security;

import com.mrwhite.polls.Exception.PollErrorCode;
import com.mrwhite.polls.Exception.PollException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {
    @Value("${app.jwtSecret}")
    private String jwtSecretKey;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    @PostConstruct
    protected void init() {
        jwtSecretKey = Base64.getEncoder().encodeToString(jwtSecretKey.getBytes());
    }

    public String generateToken(Authentication authentication){
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        //subject can contain id, and username or email as subject
        Claims claims = Jwts.claims().setId(Long.toString(customUserDetails.getId())).setSubject(customUserDetails.getEmail());
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) customUserDetails.getAuthorities();
        //claims.put(key,value) key is string and value is object
        //this is used to add a new claim, in this case roles
        claims.put("roles", authorities.stream().map(s->s.getAuthority()).filter(Objects::nonNull).collect(Collectors.toList()));

        return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey).compact();
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) throws PollException {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) throws PollException {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtSecretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException exception){
            log.error("Expired or Invalid JWT token");
            throw new PollException(PollErrorCode.UNPROCESSABLE_ENTITY, "Expired or Invalid JWT token");
        }
    }

    public Long getIdFromToken(String token) throws PollException{
        return Long.parseLong(getClaimFromToken(token,Claims::getId),10);
    }

    public Date getExpirationDateFromToken(String token) throws PollException {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public List<String> getRolesFromToken(String token) throws PollException{
        return getClaimFromToken(token,claims -> (List) claims.get("roles"));
//        Claims claims = getAllClaimsFromToken(token);
//        return claims.get("roles",List.class);
    }

    public Boolean isTokenExpired(String token) throws PollException{
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public boolean validateToken(String token) throws PollException{
        try {
            Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("could not validate Jwt Token");
            throw new PollException(PollErrorCode.INTERNAL_SERVER_ERROR, "Expired or invalid JWT token");
        }
    }

    public String getJwtFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
