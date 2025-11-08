package com.example.abchihba.converters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;

public class Converter {
    public static Bitmap base64ToBitmap(String base64String) {
        try {
            // Декодируем Base64 в массив байтов
            byte[] decodedBytes = Base64.getDecoder().decode(base64String);
            // Конвертируем байты в Bitmap
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static int dpToPx(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public static String timeFormat(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date(time * 1000L));
    }
    public static String dateFormat(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyyг.", Locale.getDefault());
        return sdf.format(new Date(time * 1000L));
    }
}
