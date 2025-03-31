package helper;

import entities.user.UserEnt;
import spring.security.AuthHelper;

public class JwtHelper {
    public static String getJwt(UserEnt userEnt){
        AuthHelper authHelper = new AuthHelper();
        return authHelper.generateJWT(userEnt);
    }
}
