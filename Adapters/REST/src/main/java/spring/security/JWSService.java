package spring.security;

import Entities.user.UserEnt;
import com.nimbusds.jose.JOSEException;
import indie.outsource.user.SignedCreateUserDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import spring.dtoMappers.UserMapper;

@Component
@AllArgsConstructor
public class JWSService {

    private JWSUtil jwsUtil;

    public SignedCreateUserDTO signUser(UserEnt user ) {
        SignedCreateUserDTO signedUserDTO = UserMapper.getSignedUserDTO(user);
        try {
            signedUserDTO.setSignature(jwsUtil.sign(signedUserDTO.getPayload()));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return signedUserDTO;
    }

    public Boolean verifySignedCreateUser(SignedCreateUserDTO signedCreateUserDTO, String signature) {
        try {
            if(!jwsUtil.verifySignature(signature,signedCreateUserDTO.getPayload())){
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        System.out.println("VERIFICATION OK");
        return true;
    }
}
