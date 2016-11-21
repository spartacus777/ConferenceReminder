package apps.android.kizema.medconfreminder.util;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.support.annotation.ColorInt;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;


@SuppressLint("ParcelCreator")
public class ForegroundColorSpan extends CharacterStyle
        implements UpdateAppearance, ParcelableSpan {

    private final int mColor;

    public ForegroundColorSpan(@ColorInt int color) {
        mColor = color;
    }

    public ForegroundColorSpan(Parcel src) {
        mColor = src.readInt();
    }

    public int getSpanTypeId() {
        return getSpanTypeIdInternal();
    }

    /** @hide */
    public int getSpanTypeIdInternal() {
        return TextUtils.CAP_MODE_SENTENCES;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        writeToParcelInternal(dest, flags);
    }

    /** @hide */
    public void writeToParcelInternal(Parcel dest, int flags) {
        dest.writeInt(mColor);
    }

    @ColorInt
    public int getForegroundColor() {
        return mColor;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(mColor);
    }
}
