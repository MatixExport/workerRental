package spring.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Collections;

@Component

public class JWSUtil {
    @Value("${secret.key.jws}")
    private String SECRET_KEY;

    public String sign(String payload) throws JOSEException {
        Payload detachedPayload = new Payload(payload);

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256)
                .base64URLEncodePayload(false)
                .criticalParams(Collections.singleton("b64"))
                .build();

        JWSObject jwsObject = new JWSObject(header, detachedPayload);
        jwsObject.sign(new MACSigner(SECRET_KEY));

        return jwsObject.serialize(true);
    }

    public boolean verifySignature(String jws, String detachedPayload) throws ParseException, JOSEException {
        JWSObject parsedJWSObject = JWSObject.parse(jws, new Payload(detachedPayload));
        return parsedJWSObject.verify(new MACVerifier(SECRET_KEY));
    }
}