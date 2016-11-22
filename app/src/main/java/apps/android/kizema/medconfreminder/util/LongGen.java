package apps.android.kizema.medconfreminder.util;

import java.util.Random;

/**
 * Created by A.Kizema on 22.11.2016.
 */

public class LongGen {

    private static Random random = new Random();

    public static long generate(){
        return random.nextLong();
    }
}
