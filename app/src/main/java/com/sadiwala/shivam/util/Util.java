package com.sadiwala.shivam.util;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.sadiwala.shivam.R;
import com.sadiwala.shivam.base.ShivamApplication;
import com.sadiwala.shivam.models.Customer;
import com.sadiwala.shivam.models.common.CodeName;
import com.sadiwala.shivam.preferences.DataController;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Util {

    /**
     * returns true if list is either null or has no data
     */
    public static boolean isListEmpty(List list) {
        if (list == null || list.size() <= 0) {
            return true;
        }
        return false;
    }

    /**
     * convert sp to px, this can be used to give font size programmatically in spannable strings.
     */
    public static float getSpToPixel(int sp) {
        float scaledDensity = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return sp * scaledDensity;
    }

    public static int getDpToPixel(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static String truncateNumber(double floatNumber) {
        long thousands = 1000L;
        long million = 1000000L;
        long billion = 1000000000L;
        long trillion = 1000000000000L;
        long number = Math.round(floatNumber);
        if ((number >= thousands) && (number < million)) {
            double fraction = calculateFraction(number, thousands);
            String stringFraction = new DecimalFormat("##.##").format(fraction);
            return stringFraction + " K";
        } else if ((number >= million) && (number < billion)) {
            double fraction = calculateFraction(number, million);
            String stringFraction = new DecimalFormat("##.##").format(fraction);
            return stringFraction + " M";
        } else if ((number >= billion) && (number < trillion)) {
            double fraction = calculateFraction(number, billion);
            String stringFraction = new DecimalFormat("##.##").format(fraction);
            return stringFraction + " B";
        }
        return Long.toString(number);
    }

    public static double calculateFraction(long number, long divisor) {
        long truncate = (number * 10L + (divisor / 2L)) / divisor;
        float fraction = (float) truncate * 0.10F;
        return fraction;
    }

    public static Drawable paintImageDrawable(Drawable drawable, int color) {
        drawable.mutate().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        return drawable;
    }

    public static ColorStateList getColorStateList(int colorChecked, int colorUnchecked) {
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{-android.R.attr.state_checked}
                },
                new int[]{
                        colorChecked, colorUnchecked
                }
        );

        return colorStateList;
    }

    public static int getColor(int color) {
        return ContextCompat.getColor(ShivamApplication.getAppContext(), color);
    }

    public static int getBrandedPrimaryColorWithDefault() {
        int backgroundColor = Util.getColor(R.color.red);
        return backgroundColor;
    }

    public static String getFirstWord(String fullString) {
        String arr[] = fullString.split(" ", 2);
        return arr[0];
    }

    public static CodeName[] getCachedCustomers() {
        ArrayList<Customer> customers = DataController.getPrefCustomers();
        CodeName[] codeNames = new CodeName[customers.size()];
        for (int i = 0; i < customers.size(); i++) {
            CodeName codeName = new CodeName(customers.get(i).getId(), customers.get(i).getName().getValue());
            codeNames[i] = codeName;
        }
        return codeNames;
    }


}
