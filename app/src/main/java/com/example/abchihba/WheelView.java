package com.example.abchihba;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class WheelView extends View {
    private Paint paint;
    private RectF rect;
    private List<String> genres;
    private int[] colors = {
            Color.parseColor("#FF62BAB4"),
            Color.parseColor("#FFEA21BE"),
            Color.parseColor("#FF449955"),
            Color.parseColor("#FFEA6464"),
            Color.parseColor("#FFD7BD30"),
            Color.parseColor("#FF4F5DD9"),
            Color.parseColor("#FFA73BB5"),
            Color.parseColor("#FF991549"),
            Color.parseColor("#FFBA7E62"),
            Color.parseColor("#FF8362BA"),
            Color.parseColor("#FF4FA659")
    };
    private float anglePerItem;
    private float rotationAngle = 0f;
    private float targetAngle = 0f;
    private int centerX, centerY;
    private MutableLiveData<String> selectedGenre = new MutableLiveData<>();
    private boolean spinning;


    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rect = new RectF();
    }
    public void setGenres(List<String> genres) {
        this.genres = genres;
        if (genres != null && !genres.isEmpty()) {
            anglePerItem = 360f / genres.size();
        } else {
            anglePerItem = 0;
        }
        invalidate();
    }
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w,h,oldw,oldh);
        int padding = 20;
        rect.set(padding,padding,w-padding,h-padding);
        centerX = w/2;
        centerY = h/2;
    }
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (genres == null || genres.isEmpty()) {
            // Не рисуем, если список пустой или null
            return;
        }

        canvas.save();
        canvas.rotate(rotationAngle, centerX, centerY);

        float startAngle = 0;
        for (int i = 0; i < genres.size(); i++) {
            // Устанавливаем цвет для сегмента
            paint.setColor(colors[i % colors.length]);
            canvas.drawArc(rect, startAngle, anglePerItem, true, paint);

            // Устанавливаем параметры для текста
            paint.setColor(ContextCompat.getColor(getContext(), R.color.secondaryText)); // Цвет текста
            paint.setTextSize(50);
            paint.setTypeface(Typeface.DEFAULT_BOLD);;
            paint.setTextAlign(Paint.Align.LEFT);

            float textAngle = startAngle + anglePerItem / 2 + 5;


            float textX = (float) (centerX + Math.cos(Math.toRadians(textAngle)) * rect.width()/7);
            float textY = (float) (centerY + Math.sin(Math.toRadians(textAngle)) * rect.height()/7);

            canvas.save();
            canvas.rotate(textAngle, textX, textY);

            canvas.drawText(genres.get(i), textX, textY, paint);

            canvas.restore();

            startAngle += anglePerItem;
        }
        canvas.restore();
    }
    public void setRotationAngle(float rotationAngle) {
        this.rotationAngle = rotationAngle;
        invalidate();
    }
    public LiveData<String> getGenre(){
        return selectedGenre;
    }

    public boolean isSpinning() {
        return spinning;
    }

    public void spinToRandomGenre() {
        spinning = true;

        if (genres == null || genres.isEmpty()) return;

        int randomIndex = (int) (Math.random() * genres.size());

        float extraAngle = 20f*360f;

        float targetAngle = rotationAngle + extraAngle + anglePerItem * ((float) genres.size()-(float) randomIndex) - (90f+anglePerItem/2);

        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(this, "rotationAngle", rotationAngle, targetAngle-rotationAngle);
        rotationAnimator.setDuration(10000);
        rotationAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        rotationAnimator.start();
        rotationAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationEnd(@NonNull Animator animator) {
                rotationAngle = rotationAngle-extraAngle;
                selectedGenre.setValue(genres.get(randomIndex));
                spinning = false;
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animator) {

            }
        });
    }

}
