package app.Rest;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import entities.user.UserEnt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component

public class TestAuthHelper {

    @Value("${secret.key.jwt}")
    private String SECRET_KEY;
    private static final long EXPIRATION_TIME = (long) 1000 * 60 * 30; // ms*s*min

    private String signClaimSet(JWTClaimsSet claimSet) {
        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                claimSet
        );

        try {
            JWSSigner signer = new MACSigner(SECRET_KEY);
            signedJWT.sign(signer);
        } catch (JOSEException e) {
            throw new RuntimeException("Error while signing the token", e);
        }

        return signedJWT.serialize();
    }

    public String generateJWT(UserEnt user, long expirationTime, String groups) {
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getLogin())
                .issuer("Worker Rental")
                .expirationTime(new Date(System.currentTimeMillis() + expirationTime))
                .claim("groups", groups)
                .build();

        return signClaimSet(jwtClaimsSet);

    }
}
