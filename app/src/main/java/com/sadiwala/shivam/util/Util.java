package com.sadiwala.shivam.util;

import static com.sadiwala.shivam.util.AaryaConstants.PERMISSIONS_WRITE_STORAGE;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.sadiwala.shivam.R;
import com.sadiwala.shivam.base.PermissionManager;
import com.sadiwala.shivam.base.ShivamApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public static ArrayList<Method> findGetters(Class<?> c) {
        ArrayList<Method> list = new ArrayList<Method>();
        Method[] methods = c.getDeclaredMethods();
        for (Method method : methods)
            if (isGetter(method))
                list.add(method);
        return list;
    }

    public static boolean isGetter(Method method) {
        if (Modifier.isPublic(method.getModifiers()) &&
                method.getParameterTypes().length == 0) {
            if (method.getName().matches("^get[A-Z].*") &&
                    !method.getReturnType().equals(void.class))
                return true;
            if (method.getName().matches("^is[A-Z].*") &&
                    method.getReturnType().equals(boolean.class))
                return true;
        }
        return false;
    }

    public static String getGetterMethodNameByFieldName(ArrayList<Method> methods, String fieldName) {
        for (Method method : methods) {
            String methodNameString = method.toString();
            if (methodNameString.toLowerCase().contains(fieldName.toLowerCase())) {
                String[] res = methodNameString.split("[.]");
                if (res != null && res.length > 0) {
                    return res[res.length - 1];
                }
            }
        }
        return null;
    }

    /**
     * This method will open WhatsApp screen
     *
     * @param number
     */
    public static void sendWhatsAppMessage(Activity activity, String message, Uri imageUri) {
        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, message);
            sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            sendIntent.setType("image/jpeg");
            sendIntent.setPackage("com.whatsapp");
            activity.startActivity(Intent.createChooser(sendIntent, ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getScreenshot(Activity activity, View view) {

        if (!(Build.VERSION.SDK_INT > Build.VERSION_CODES.R)) {
            if (!PermissionManager.hasSelfPermissions(activity, new String[]{PermissionManager.WRITE_EXTERNAL_STORAGE})) {
                ActivityCompat.requestPermissions(activity, new String[]{PermissionManager.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_WRITE_STORAGE);
                return null;
            }
        }

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + generateNewUUID() + ".jpg";

            // create bitmap screen capture
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            return mPath;
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }

        return null;
    }

    public static String generateNewUUID() {
        return UUID.randomUUID().toString();
    }

}
