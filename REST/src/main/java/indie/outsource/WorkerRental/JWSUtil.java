package indie.outsource.WorkerRental;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;

import java.text.ParseException;
import java.util.Collections;

public class JWSUtil {

    private static final String SECRET_KEY = "kt1FXZwHnetaSqGv6GANVozKLaWfGbDvDWYI1klelFh8tbiBw3t7fSSI+MYqySVP";

    public static String sign(String payload) throws JOSEException {
        Payload detachedPayload = new Payload(payload);

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256)
                .base64URLEncodePayload(false)
                .criticalParams(Collections.singleton("b64"))
                .build();

        JWSObject jwsObject = new JWSObject(header, detachedPayload);
        jwsObject.sign(new MACSigner(SECRET_KEY));

        return jwsObject.serialize(true);
    }

    public static boolean verifySignature(String jws, String detachedPayload) throws ParseException, JOSEException {
        JWSObject parsedJWSObject = JWSObject.parse(jws, new Payload(detachedPayload));
        return parsedJWSObject.verify(new MACVerifier(SECRET_KEY));
    }
}