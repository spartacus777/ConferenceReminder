package apps.android.kizema.medconfreminder.util.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by A.Kizema on 23.11.2016.
 */

public class StringValidator {
    private static final String PASSWORD_PATTERN =
        "(.{1,200})";

        private static StringValidator instance;

        private Pattern pattern;
        private Matcher matcher;

        private StringValidator() {
            pattern = Pattern.compile(PASSWORD_PATTERN);
        }

        public static StringValidator getInstance() {
            if (instance == null) {
                instance = new StringValidator();
            }

            return instance;
        }

        public boolean validate(final String name) {
            matcher = pattern.matcher(name);
            return matcher.matches();

        }

}
