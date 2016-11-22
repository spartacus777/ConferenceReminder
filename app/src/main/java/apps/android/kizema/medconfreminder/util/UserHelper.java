package apps.android.kizema.medconfreminder.util;

import java.math.BigInteger;
import java.security.SecureRandom;

import apps.android.kizema.medconfreminder.auth.helpers.Session;
import apps.android.kizema.medconfreminder.model.AccountUser;
import apps.android.kizema.medconfreminder.model.User;

/**
 * Created by A.Kizema on 22.11.2016.
 */

public class UserHelper {

    private static SecureRandom random = new SecureRandom();

    public static String generateUserId(){
        return new BigInteger(130, random).toString(32);
    }

    public static User getMyUser(){
        String pass = Session.getInstance().getPassToken();
        String name = Session.getInstance().getNameToken();

        AccountUser accountUser = AccountUser.find(name, pass);
        if (accountUser != null){
            String userId = accountUser.getUserId();
            User user = User.findById(userId);
            return user;
        }

        return null;
    }

    public static AccountUser getMyAccUser(){
        String pass = Session.getInstance().getPassToken();
        String name = Session.getInstance().getNameToken();

        AccountUser accountUser = AccountUser.find(name, pass);
        return accountUser;
    }

}
