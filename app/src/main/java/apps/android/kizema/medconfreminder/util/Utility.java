package apps.android.kizema.medconfreminder.util;

import android.text.SpannableString;
import android.widget.TextView;


public class Utility {

    public static SpannableString getSpannableStringWithColorValue(String s, int colorValue) {
        SpannableString s1 = new SpannableString(s);
        s1.setSpan(new android.text.style.ForegroundColorSpan(colorValue), 0, s1.length(), 0);
        return s1;
    }

    public static boolean isEmpty(TextView tv){
        if (tv == null || tv.getText() == null || tv.getText().length() == 0){
            return true;
        }

        return false;
    }

}
