package com.sadiwala.shivam.util;

import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.URLSpan;

import androidx.annotation.StringRes;

import com.sadiwala.shivam.base.ShivamApplication;

import java.util.Random;


public class StringUtils {

    public static String getMaskedString(String str, int maskLength) {
        if (str == null) {
            return "";
        }

        if (str.length() <= maskLength) {
            return str;
        }

        StringBuilder maskedStr = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (i >= (str.length() - maskLength)) {
                maskedStr.append("x");
            } else {
                maskedStr.append(str.charAt(i));
            }
        }
        return maskedStr.toString();
    }

    /**
     * Replaces all character with * for given length, If showLastDigits is true then it will show last character.
     */
    public static String getMaskedString(String str, int maskLength, boolean showLastDigits) {
        if (str == null) {
            return "";
        }

        if (str.length() <= maskLength) {
            return str;
        }

        if (!showLastDigits) {
            return getMaskedString(str, maskLength);
        }

        StringBuilder maskedStr = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (i < (str.length() - maskLength)) {
                maskedStr.append("x");
            } else {
                maskedStr.append(str.charAt(i));
            }
        }
        return maskedStr.toString();
    }

    public static String getPhoneMaskedString(String str, int skipLength) {
        if (str == null) {
            return "";
        }

        if (str.length() <= skipLength) {
            return str;
        }

        String maskedPhone = str.substring(skipLength, str.length() - skipLength);

        StringBuilder maskedStr = new StringBuilder();

        maskedStr.append(str.substring(0, skipLength));
        for (int i = 0; i < maskedPhone.length(); i++) {
            maskedStr.append("x");
        }
        maskedStr.append(str.substring(str.length() - skipLength));
        return maskedStr.toString();
    }

    /**
     * @param str        email id to be masked.
     * @param skipLength is number of characters we need to mask before '@'.
     */
    public static String getEmailMaskedString(String str, int skipLength) {
        if (str == null) {
            return "";
        }

        if (str.length() <= skipLength) {
            return str;
        }

        if (str.indexOf("@") <= 0) {
            return str;
        }

        String maskedEmail = str.substring(0, str.indexOf("@"));
        String remainingMaskedEmail = str.substring(str.indexOf("@"), str.length());

        StringBuilder maskedStr = new StringBuilder();

        maskedStr.append(getMaskedString(maskedEmail, skipLength));

        maskedStr.append(remainingMaskedEmail);
        return maskedStr.toString();
    }

    public static String generateRandomString(int length) {
        final String stringTemplate = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder randomString = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            randomString.append(stringTemplate.charAt(random.nextInt(stringTemplate.length())));
        }

        return randomString.toString();
    }

    public static String removeBracketsFromString(String string) {
        if (!TextUtils.isEmpty(string)) {
            return string.substring(0, string.indexOf("("));
        }
        return string;
    }

    public static String toSmallSnakeCase(String text) {
        if (TextUtils.isEmpty(text)) {
            return text;
        }
        return text.replaceAll("\\s+", "_").toLowerCase();
    }

    /**
     * Capitalize first char of each word.
     */
    public static String toCamelCase(String input) {
        if (TextUtils.isEmpty(input)) {
            return "";
        }

        final StringBuilder result = new StringBuilder(input.length());
        String words[] = input.toLowerCase().split("\\ "); // space found then split it
        for (int i = 0; i < words.length; i++) {
            if (i > 0) {
                result.append(" ");
            }
            if (!words[i].isEmpty()) {
                result.append(Character.toUpperCase(words[i].charAt(0))).append(
                        words[i].substring(1));
            }
        }
        return result.toString();
    }

    /**
     * Capitalizes the field name the first character is in uppercase.
     * ex : fieldName = name , result = Name
     *
     * @param fieldName
     * @return the capitalised field name
     */
    public static String capatalizeString(String fieldName) {
        final String result;

        if (fieldName != null && !fieldName.isEmpty()
                && Character.isLowerCase(fieldName.charAt(0))) {
            result = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        } else {
            result = fieldName;
        }
        return result;
    }

    public static boolean doesStringStartsWithGivenPrefix(String value, String prefix) {
        return !TextUtils.isEmpty(value) && value.startsWith(prefix);
    }

    public static String getString(int resourceId) {
        return ShivamApplication.getAppContext().getString(resourceId);
    }

    public static String getString(@StringRes int resId, Object... formatArgs) {
        return ShivamApplication.getAppContext().getResources().getString(resId, formatArgs);
    }

    public static String getInitials(String name) {
        if (TextUtils.isEmpty(name)) {
            return "";
        }
        return String.valueOf(name.charAt(0)).toUpperCase();
    }

    public static String trimString(String text, int length) {
        if (TextUtils.isEmpty(text) || length <= 0) {
            return "";
        }

        if (length > text.length()) {
            return text;
        }
        return text.substring(0, length);
    }

    public static boolean isStringContainingOnlySpaces(String text) {
        return text.length() > 0 && text.trim().length() == 0;
    }


    private static class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String url) {
            super(url);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }

}
