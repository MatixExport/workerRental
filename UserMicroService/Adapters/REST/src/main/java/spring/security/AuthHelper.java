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
    private static final long EXPIRATION_TIME = (long)1000*60*30; // ms*s*min

    private String signClaimSet(JWTClaimsSet claimSet) {
        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                claimSet
        );

        try{
            JWSSigner signer = new MACSigner(SECRET_KEY);
            signedJWT.sign(signer);
        }
        catch (JOSEException e){
            throw new RuntimeException("Error while signing the token", e);
        }

        return signedJWT.serialize();
    }

    public String generateJWT(UserEnt user,long expirationTime) {
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getLogin())
                .issuer("Worker Rental")
                .expirationTime(new Date(System.currentTimeMillis() + expirationTime))
                .claim("groups", user.getGroups())
                .build();

        return signClaimSet(jwtClaimsSet);
    }

    public String generateJWT(UserEnt user) {
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getLogin())
                .issuer("Worker Rental")
                .expirationTime(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .claim("groups", user.getGroups())
                .build();

        return signClaimSet(jwtClaimsSet);
    }

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
