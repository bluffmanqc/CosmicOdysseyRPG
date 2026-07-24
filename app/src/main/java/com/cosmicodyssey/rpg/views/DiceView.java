package com.cosmicodyssey.rpg.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;

import java.util.Random;

public class DiceView extends View {
    private Paint paint;
    private Paint dotPaint;
    private Paint glowPaint;
    private RectF diceRect;
    private int diceValue = 1;
    private int diceSides = 6;
    private boolean isRolling = false;
    private float rotation = 0f;
    private float scale = 1f;
    private float glowAlpha = 0f;
    private Random random = new Random();
    private OnRollCompleteListener listener;
    private int rarityColor = 0xFF00FF88;

    public DiceView(Context context) { super(context); init(); }
    public DiceView(Context context, AttributeSet attrs) { super(context, attrs); init(); }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#1a1a2e"));
        paint.setStyle(Paint.Style.FILL);
        
        dotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dotPaint.setColor(Color.parseColor("#00ff88"));
        
        glowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        glowPaint.setStyle(Paint.Style.FILL);
        
        diceRect = new RectF();
    }

    public void setRarityColor(int color) {
        this.rarityColor = color;
        dotPaint.setColor(color);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float size = Math.min(getWidth(), getHeight()) * 0.8f;
        float left = (getWidth() - size) / 2;
        float top = (getHeight() - size) / 2;
        diceRect.set(left, top, left + size, top + size);

        canvas.save();
        canvas.rotate(rotation, getWidth() / 2f, getHeight() / 2f);
        canvas.scale(scale, scale, getWidth() / 2f, getHeight() / 2f);

        // Glow effect
        if (glowAlpha > 0) {
            glowPaint.setColor(rarityColor);
            glowPaint.setAlpha((int)(glowAlpha * 80));
            canvas.drawRoundRect(left - 10, top - 10, left + size + 10, top + size + 10, size * 0.2f, size * 0.2f, glowPaint);
        }

        // Dice body
        paint.setShadowLayer(20, 0, 0, rarityColor & 0x44FFFFFF);
        canvas.drawRoundRect(diceRect, size * 0.15f, size * 0.15f, paint);

        // Draw dots based on value
        drawDots(canvas, diceValue, size, left, top);

        canvas.restore();
    }

    private void drawDots(Canvas canvas, int value, float size, float left, float top) {
        float dotRadius = size * 0.08f;
        float centerX = left + size / 2;
        float centerY = top + size / 2;
        float offset = size * 0.25f;

        switch (value) {
            case 1:
                canvas.drawCircle(centerX, centerY, dotRadius, dotPaint);
                break;
            case 2:
                canvas.drawCircle(centerX - offset, centerY - offset, dotRadius, dotPaint);
                canvas.drawCircle(centerX + offset, centerY + offset, dotRadius, dotPaint);
                break;
            case 3:
                canvas.drawCircle(centerX - offset, centerY - offset, dotRadius, dotPaint);
                canvas.drawCircle(centerX, centerY, dotRadius, dotPaint);
                canvas.drawCircle(centerX + offset, centerY + offset, dotRadius, dotPaint);
                break;
            case 4:
                canvas.drawCircle(centerX - offset, centerY - offset, dotRadius, dotPaint);
                canvas.drawCircle(centerX + offset, centerY - offset, dotRadius, dotPaint);
                canvas.drawCircle(centerX - offset, centerY + offset, dotRadius, dotPaint);
                canvas.drawCircle(centerX + offset, centerY + offset, dotRadius, dotPaint);
                break;
            case 5:
                canvas.drawCircle(centerX - offset, centerY - offset, dotRadius, dotPaint);
                canvas.drawCircle(centerX + offset, centerY - offset, dotRadius, dotPaint);
                canvas.drawCircle(centerX, centerY, dotRadius, dotPaint);
                canvas.drawCircle(centerX - offset, centerY + offset, dotRadius, dotPaint);
                canvas.drawCircle(centerX + offset, centerY + offset, dotRadius, dotPaint);
                break;
            case 6:
                canvas.drawCircle(centerX - offset, centerY - offset, dotRadius, dotPaint);
                canvas.drawCircle(centerX + offset, centerY - offset, dotRadius, dotPaint);
                canvas.drawCircle(centerX - offset, centerY, dotRadius, dotPaint);
                canvas.drawCircle(centerX + offset, centerY, dotRadius, dotPaint);
                canvas.drawCircle(centerX - offset, centerY + offset, dotRadius, dotPaint);
                canvas.drawCircle(centerX + offset, centerY + offset, dotRadius, dotPaint);
                break;
        }
    }

    public void roll(final int sides) {
        if (isRolling) return;
        this.diceSides = sides;
        isRolling = true;

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(2000);
        animator.setInterpolator(new BounceInterpolator());

        animator.addUpdateListener(animation -> {
            float progress = (float) animation.getAnimatedValue();
            rotation = progress * 1080f;
            scale = 1f + (float) Math.sin(progress * Math.PI * 2) * 0.4f;
            glowAlpha = (float) Math.sin(progress * Math.PI) * 2f;
            
            if (progress < 0.8f) {
                diceValue = random.nextInt(sides) + 1;
            }
            
            invalidate();
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) {}
            @Override public void onAnimationEnd(Animator animation) {
                diceValue = random.nextInt(sides) + 1;
                isRolling = false;
                rotation = 0f;
                scale = 1f;
                glowAlpha = 0f;
                invalidate();
                if (listener != null) listener.onRollComplete(diceValue);
            }
            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
        });

        animator.start();
    }

    public void setOnRollCompleteListener(OnRollCompleteListener listener) {
        this.listener = listener;
    }

    public interface OnRollCompleteListener {
        void onRollComplete(int result);
    }

    public int getValue() { return diceValue; }
    public boolean isRolling() { return isRolling; }
}
