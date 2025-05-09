package spring.security;

import entities.user.UserEnt;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

@Component
public class AuthHelper {

    @Value("${secret.key.jwt}")
    private String SECRET_KEY;


    public boolean verifyJWT(String jwt) {
        try{
            SignedJWT signedJWT = SignedJWT.parse(jwt);

            JWSVerifier verifier = new MACVerifier(SECRET_KEY);

            return signedJWT.verify(verifier) && !isTokenExpired(signedJWT);
        }
        catch (Exception e){
            throw new JWTException("Error while verifying the token", e);
        }
    }

    private boolean isTokenExpired(SignedJWT signedJWT) throws ParseException {
        Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
        return expiration.before(new Date());
    }

    public String extractLogin(String jwt){
        return extractClaim(jwt,"sub");
    }

    public String extractGroups(String jwt){
       return extractClaim(jwt, "groups");
    }

    private String extractClaim(String jwt, String claim){
        try{
            SignedJWT signedJWT = SignedJWT.parse(jwt);
            return signedJWT.getJWTClaimsSet().getClaim(claim).toString();
        }
        catch (Exception e){
            throw new JWTException("Error while parsing the token", e);
        }
    }

}
