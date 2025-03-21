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

    public SignedCreateUserDTO signUser(UserEnt user) throws JOSEException {
        SignedCreateUserDTO signedUserDTO = UserMapper.getSignedUserDTO(user);
        signedUserDTO.setSignature(jwsUtil.sign(signedUserDTO.getPayload()));
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
        return true;
    }
}
