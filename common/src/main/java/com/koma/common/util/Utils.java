package com.koma.common.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.View;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public final class Utils {
    private Utils() {
    }

    public static boolean isLayoutRtlSupport(View view) {
        return ViewCompat.getLayoutDirection(view) == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

    public static String formatDuration(long duration) {
        long ss = duration / 1000 % 60;
        long mm = duration / 60000 % 60;
        long hh = duration / 3600000;
        if (duration < 60 * 60 * 1000) {
            return String.format(Locale.getDefault(), "%02d:%02d", mm, ss);
        } else {
            return String.format(Locale.getDefault(), "%02d:%02d:%02d", hh, mm, ss);
        }
    }

    public static String formatDateTaken(long dateTaken) {
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        return df.format(new Date(dateTaken));
    }

    public static String getCursorString(Cursor cursor, String columnName) {
        if (cursor == null)
            return null;
        final int index = cursor.getColumnIndex(columnName);
        return (index != -1) ? cursor.getString(index) : null;
    }

    /**
     * Missing or null values are returned as -1.
     */
    public static long getCursorLong(Cursor cursor, String columnName) {
        final int index = cursor.getColumnIndex(columnName);
        String value = null;
        if (index == -1) return -1;
        try {
            value = cursor.getString(index);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return -1;
        }
        if (value == null) return -1;
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * API 21
     *
     * @see Build.VERSION_CODES#LOLLIPOP
     */
    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * API 23
     *
     * @see Build.VERSION_CODES#M
     */
    public static boolean hasMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * Check that all given permissions have been granted by verifying that each entry in the
     * given array is of the value {@link PackageManager#PERMISSION_GRANTED}.
     *
     * @see Activity#onRequestPermissionsResult(int, String[], int[])
     */
    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
