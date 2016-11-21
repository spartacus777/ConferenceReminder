package apps.android.kizema.medconfreminder.util.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by A.Kizema on 22.11.2016.
 */

public class NameValidator {
    private static final String PASSWORD_PATTERN =
            "(.{1,20})";

    private static NameValidator instance;

    private Pattern pattern;
    private Matcher matcher;

    private NameValidator() {
        pattern = Pattern.compile(PASSWORD_PATTERN);
    }

    public static NameValidator getInstance() {
        if (instance == null) {
            instance = new NameValidator();
        }

        return instance;
    }

    /**
     * Validate password with regular expression
     *
     * @param password password for validation
     * @return true valid password, false invalid password
     */
    public boolean validate(final String password) {

        matcher = pattern.matcher(password);
        return matcher.matches();

    }

}
