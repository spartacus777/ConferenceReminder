package apps.android.kizema.medconfreminder.util;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by A.Kizema on 22.11.2016.
 */

public class UserHelper {

    private static SecureRandom random = new SecureRandom();

    public static String generateUserId(){
        return new BigInteger(130, random).toString(32);
    }

}
