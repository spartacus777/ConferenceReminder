package apps.android.kizema.medconfreminder.util;

import android.text.SpannableString;


public class Utility {

    public static SpannableString getSpannableStringWithColorValue(String s, int colorValue) {
        SpannableString s1 = new SpannableString(s);
        s1.setSpan(new android.text.style.ForegroundColorSpan(colorValue), 0, s1.length(), 0);
        return s1;
    }

}
