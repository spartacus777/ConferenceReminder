package apps.android.kizema.medconfreminder;

import org.junit.Test;

import apps.android.kizema.medconfreminder.util.validator.EmailValidator;
import apps.android.kizema.medconfreminder.util.validator.NameValidator;
import apps.android.kizema.medconfreminder.util.validator.PasswordValidator;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ValidatorUnitTest {
    @Test
    public void name_test() throws Exception {
        NameValidator validator = NameValidator.getInstance();

        assertEquals(validator.validate(""), false);

        assertEquals(validator.validate("Name"), true);

        assertEquals(validator.validate("jkad37dh2"), true);

        assertEquals(validator.validate("jkad37 dnwe edew"), true);

        assertEquals(validator.validate("jkad37dh2djwendwe dewdwekdnwedewnndwedwe dwenwdew"), false);
    }

    @Test
    public void email_test() throws Exception {
        EmailValidator validator = EmailValidator.getInstance();

        assertEquals(validator.validate(""), false);

        assertEquals(validator.validate("Name"), false);

        assertEquals(validator.validate("jkad37dh2@"), false);

        assertEquals(validator.validate("@ndejnwdw"), false);

        assertEquals(validator.validate("jkad37dh2@dklnwedew"), false);

        assertEquals(validator.validate("jkad37dh2@nwedejwk.com"), true);

        assertEquals(validator.validate("@djenwd.com"), false);

        assertEquals(validator.validate("jkad37 dnwe edew@tr.com"), false);

        assertEquals(validator.validate("jkad37dh2djwendwe dewdwekdnwedewnndwedwe dwenwdew@dewdewd.com"), false);
    }

    @Test
    public void password_test() throws Exception {
        PasswordValidator validator = PasswordValidator.getInstance();

        assertEquals(validator.validate(""), false);

        assertEquals(validator.validate("Name"), false);

        assertEquals(validator.validate("jdnejwkbdkwedjwe"), false);

        assertEquals(validator.validate("jkad37 dnwe edew"), true);
        
        assertEquals(validator.validate("21832732732"), true);

        assertEquals(validator.validate("djw2oe"), true);

        assertEquals(validator.validate("jkad37dh2djwendwe dewdwekdnwedewnndwedwe dwenwdew"), false);
    }
}