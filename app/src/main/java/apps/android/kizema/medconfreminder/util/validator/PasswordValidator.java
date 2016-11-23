package apps.android.kizema.medconfreminder.util.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by A.Kizema on 21.11.2016.
 */

public class PasswordValidator {
    private static final String PASSWORD_PATTERN =
            "((?=.*\\d).{6,40})";

    private static PasswordValidator instance;

    private Pattern pattern;
    private Matcher matcher;

    private PasswordValidator(){
        pattern = Pattern.compile(PASSWORD_PATTERN);
    }

    public static PasswordValidator getInstance(){
        if (instance == null){
            instance = new PasswordValidator();
        }

        return instance;
    }

    /**
     * Validate password with regular expression
     * @param password password for validation
     * @return true valid password, false invalid password
     */
    public boolean validate(final String password){

        matcher = pattern.matcher(password);
        return matcher.matches();
//        return true;
    }

}
