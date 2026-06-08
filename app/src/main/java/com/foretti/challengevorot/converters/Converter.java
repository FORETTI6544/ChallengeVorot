package com.foretti.challengevorot.converters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    public static byte[] bitmapToByteArray(Bitmap bitmap, int quality, Bitmap.CompressFormat format) {
        if (bitmap == null || bitmap.isRecycled()) {
            return new byte[0];
        }
        quality = Math.max(0, Math.min(quality, 100));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            if (!bitmap.compress(format, quality, byteArrayOutputStream)) {
                return new byte[0];
            }
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            return new byte[0];
        } finally {
            try {
                byteArrayOutputStream.close();
            } catch (IOException ignored) {

            }
        }
    }
    public static Bitmap cropToSquare(Bitmap originalBitmap) {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        int size = Math.min(width, height);

        int startX = (width - size) / 2;
        int startY = (height - size) / 2;

        return Bitmap.createBitmap(originalBitmap, startX, startY, size, size);
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
